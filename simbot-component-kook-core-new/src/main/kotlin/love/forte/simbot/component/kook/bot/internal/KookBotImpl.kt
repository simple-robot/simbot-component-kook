/*
 * Copyright (c) 2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook,
 * If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.component.kook.bot.internal

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import love.forte.simbot.ID
import love.forte.simbot.component.kook.KookComponent
import love.forte.simbot.component.kook.KookGuild
import love.forte.simbot.component.kook.bot.KookBot
import love.forte.simbot.component.kook.bot.KookBotConfiguration
import love.forte.simbot.component.kook.bot.KookBotManager
import love.forte.simbot.component.kook.event.KookBotStartedEvent
import love.forte.simbot.component.kook.event.internal.KookBotStartedEventImpl
import love.forte.simbot.component.kook.internal.KookChannelCategoryImpl
import love.forte.simbot.component.kook.internal.KookChannelImpl
import love.forte.simbot.component.kook.internal.KookGuildImpl
import love.forte.simbot.component.kook.internal.KookMemberImpl
import love.forte.simbot.definition.Contact
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.event.pushIfProcessable
import love.forte.simbot.kook.Bot
import love.forte.simbot.kook.api.channel.ChannelInfo
import love.forte.simbot.kook.api.channel.GetChannelListApi
import love.forte.simbot.kook.api.guild.GetGuildListApi
import love.forte.simbot.kook.api.member.GetGuildMemberListApi
import love.forte.simbot.kook.objects.Guild
import love.forte.simbot.kook.objects.SimpleUser
import love.forte.simbot.literal
import love.forte.simbot.logger.LoggerFactory
import love.forte.simbot.utils.item.Items
import love.forte.simbot.utils.item.Items.Companion.asItems
import org.slf4j.Logger
import java.util.concurrent.ConcurrentHashMap
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 *
 * @author ForteScarlet
 */
internal class KookBotImpl(
    override val eventProcessor: EventProcessor,
    override val sourceBot: Bot,
    override val component: KookComponent,
    override val manager: KookBotManager,
    private val configuration: KookBotConfiguration
) : KookBot {
    override val logger: Logger =
        LoggerFactory.getLogger("love.forte.simbot.component.kook.bot.${sourceBot.ticket.clientId}")

    override fun isMe(id: ID): Boolean {
        if (id.literal == sourceBot.ticket.clientId) {
            return true
        }

        return try {
            id.literal == sourceBot.botUserInfo.id
        } catch (e: IllegalStateException) {
            // ignore match and return true
            true
        }
    }

    /**
     * 对缓存数据进行刷新时候使用的上下文，用于保证并发线程只有1.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private val cacheModifyContext =
        Dispatchers.IO.limitedParallelism(1) + CoroutineName("KookBotCacheModify")

    private val internalCache = InternalCache()

    internal fun internalGuild(guildId: String) = internalCache.guilds[guildId]
    internal fun internalChannel(channelId: String) = internalCache.channels[channelId]
    internal fun internalCategory(categoryId: String) = internalCache.categories[categoryId]
    internal fun internalChannels(): Collection<KookChannelImpl> = internalCache.channels.values
    internal fun internalCategories(): Collection<KookChannelCategoryImpl> = internalCache.categories.values
    internal fun internalMember(guildId: String, userId: String) =
        internalCache.members[internalCache.memberCacheId(guildId, userId)]

    internal fun internalGuildChannelCount(guildId: String): Int =
        internalCache.channels.values.count { it._guildId == guildId }


    /**
     * 在从 [Dispatchers.IO] 中的单线程作用域中进行数据更新操作。
     *
     * 所有针对缓存的多步修改操作都应该在此处完成。
     */
    @OptIn(ExperimentalContracts::class)
    internal suspend inline fun inCacheModify(crossinline block: suspend InternalCache.() -> Unit) {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }

        withContext(cacheModifyContext) {
            internalCache.block()
        }
    }

    private val startLock = Mutex()
    private var syncJob: Job? = null

    override suspend fun start(): Boolean = startLock.withLock {
        val (guildSyncPeriod, batchDelay) = configuration.syncPeriods.guild

        inCacheModify {
            // TODO init event processor
            // 从 inCacheModify 中注册事件，防止一开始的事件对缓存有操作

            sourceBot.start()

            dataSync(batchDelay)
        }

        syncJob?.cancel()
        syncJob = guildDataSyncJob(guildSyncPeriod, batchDelay)

        // publish event
        publishStartEvent()

        return true
    }


    private fun guildDataSyncJob(syncPeriod: Long, batchDelay: Long): Job {
        if (syncPeriod > 0) {
            return launch {
                while (true) {
                    delay(syncPeriod)
                    dataSync(batchDelay)
                }
            }
        }

        return Job().apply { complete() }
    }

    private suspend fun dataSync(batchDelay: Long) {
        inCacheModify {
            syncGuilds(batchDelay, this)
        }
    }

    private suspend fun syncGuilds(batchDelay: Long, internalCache: InternalCache) {
        syncRequestGuilds(batchDelay)
            .buffer(100)
            .collect { guild ->
                var botMember: KookMemberImpl? = null
                var chc = 0
                var cac = 0
                var mc = 0
                // channel caches
                coroutineScope {
                    // 单并发并发的异步
                    launch {
                        requestGuildChannels(guild.id, batchDelay)
                            .buffer(500)
                            .collect { channelInfo ->
                                if (channelInfo.isCategory) {
                                    val categoryImpl = KookChannelCategoryImpl(this@KookBotImpl, channelInfo, guild.id)
                                    internalCache.categories[channelInfo.id] = categoryImpl
                                    cac++
                                } else {
                                    val channelImpl = KookChannelImpl(this@KookBotImpl, channelInfo, guild.id)
                                    internalCache.channels[channelInfo.id] = channelImpl
                                    chc++
                                }
                            }
                    }

                    // member caches
                    launch {
                        requestGuildMembers(guild.id, batchDelay)
                            .buffer(500)
                            .collect { user ->
                                val memberImpl = KookMemberImpl(this@KookBotImpl, user, guild.id)
                                internalCache.members[internalCache.memberCacheId(guild.id, user.id)] = memberImpl
                                if (user.id == sourceBot.botUserInfo.id) {
                                    // bot self
                                    botMember = memberImpl
                                }
                                mc++
                            }
                    }
                }

                val guildImpl = KookGuildImpl(this, guild)
                val bm = botMember
                if (bm != null) {
                    guildImpl._bot = KookGuildBotImpl(this, bm)
                }

                internalCache.guilds[guild.id] = guildImpl

                logger.debug("Sync guild {} with {} channels, {} categories and {} members", guild, chc, cac, mc)
            }

    }

    /**
     * 以 flow 的方式查询guild列表
     */
    private fun syncRequestGuilds(batchDelay: Long = 0): Flow<Guild> = flow {
        var page = 1
        do {
            if (page > 1) {
                delay(batchDelay)
            }
            logger.debug("Sync guild data ... page {}", page)
            val guildListResult = GetGuildListApi.create(page = page).requestBy(sourceBot)
            val guilds = guildListResult.items
            logger.debug("{} guild data synchronized in page {}", guilds.size, page)
            guilds.forEach {
                emit(it)
            }
            page = guildListResult.meta.page + 1
        } while (guilds.isNotEmpty() && guildListResult.meta.page < guildListResult.meta.pageTotal)
    }

    private fun requestGuildChannels(guildId: String, batchDelay: Long): Flow<ChannelInfo> = flow {
        var page = 1
        do {
            if (page > 1) {
                delay(batchDelay)
            }
            logger.debug("Sync channel data for guild {} ... page {}", guildId, page)
            val result = GetChannelListApi.create(guildId = guildId, page = page).requestBy(sourceBot)
            val channels = result.items
            logger.debug("{} channel data synced for guild {} in page {}", channels.size, guildId, page)
            channels.forEach {
                emit(it)
            }
            page = result.meta.page + 1
        } while (channels.isNotEmpty() && result.meta.page < result.meta.pageTotal)
    }

    private fun requestGuildMembers(guildId: String, batchDelay: Long): Flow<SimpleUser> = flow {
        var page = 1
        do {
            if (page > 1) {
                delay(batchDelay)
            }
            logger.debug("Sync member data for guild {} ... page {}", guildId, page)
            val usersResult = GetGuildMemberListApi.create(guildId = guildId, page = page).requestBy(sourceBot)
            val users = usersResult.items
            logger.debug("{} member data synced for guild {} in page {}", users.size, guildId, page)
            users.forEach {
                emit(it)
            }
            page = usersResult.meta.page + 1
            delay(batchDelay)
        } while (users.isNotEmpty() && usersResult.meta.page < usersResult.meta.pageTotal)

    }

    private fun publishStartEvent() {
        launch {
            eventProcessor.pushIfProcessable(KookBotStartedEvent) {
                KookBotStartedEventImpl(this@KookBotImpl)
            }
        }
    }

    override val contacts: Items<Contact>
        get() = TODO("Not yet implemented")

    override suspend fun contact(id: ID): Contact? {
        TODO("Not yet implemented")
    }

    override val guilds: Items<KookGuild>
        get() = internalCache.guilds.values.asItems()

    override suspend fun guild(id: ID): KookGuild? = internalGuild(id.literal)

    override suspend fun guildCount(): Int = internalCache.guilds.size


    companion object
}


internal class InternalCache {
    val guilds = ConcurrentHashMap<String, KookGuildImpl>()
    val channels = ConcurrentHashMap<String, KookChannelImpl>()
    val categories = ConcurrentHashMap<String, KookChannelCategoryImpl>()

    /**
     * member key: guildId + '$' + userId
     */
    val members = ConcurrentHashMap<String, KookMemberImpl>()

    fun memberCacheId(guildId: String, userId: String): String = "$guildId$GUILD_MEMBER_INFIX$userId"

    companion object {
        const val GUILD_MEMBER_INFIX = "$"
    }
}
