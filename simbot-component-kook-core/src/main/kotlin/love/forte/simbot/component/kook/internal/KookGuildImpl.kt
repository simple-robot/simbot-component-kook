/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kook 的一部分。
 *
 *  simbot-component-kook 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kook 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

package love.forte.simbot.component.kook.internal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.component.kook.*
import love.forte.simbot.component.kook.internal.KookComponentGuildBotImpl.Companion.toMemberBot
import love.forte.simbot.component.kook.model.GuildModel
import love.forte.simbot.component.kook.model.toModel
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.kook.api.channel.ChannelInfo
import love.forte.simbot.kook.api.channel.ChannelListRequest
import love.forte.simbot.kook.api.guild.GuildUser
import love.forte.simbot.kook.api.guild.GuildUserListRequest
import love.forte.simbot.kook.api.user.UserViewRequest
import love.forte.simbot.literal
import love.forte.simbot.utils.item.Items
import love.forte.simbot.utils.item.Items.Companion.asItems
import love.forte.simbot.utils.runInBlocking
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext

/**
 *
 * @author ForteScarlet
 */
internal class KookGuildImpl private constructor(
    internal val baseBot: KookComponentBotImpl,
    @Volatile override var source: GuildModel,
) : KookGuild, CoroutineScope {
    override val id: ID get() = source.id
    internal val job = SupervisorJob(baseBot.job)
    
    override val coroutineContext: CoroutineContext = baseBot.coroutineContext + job
    
    override val createTime: Timestamp get() = Timestamp.notSupport()
    override val maximumMember: Int get() = source.maximumMember
    override val description: String get() = source.description
    override val maximumChannel: Int get() = source.maximumChannel
    
    override val ownerId: ID get() = source.ownerId
    override val name: String get() = source.name
    override val icon: String get() = source.icon
    
    internal val internalChannelCategories = ConcurrentHashMap<String, KookChannelCategoryImpl>()
    
    internal val internalChannels = ConcurrentHashMap<String, KookChannelImpl>()
    
    internal val internalMembers = ConcurrentHashMap<String, KookGuildMemberImpl>()
    
    internal fun internalMember(id: ID): KookGuildMemberImpl? = internalMembers[id.literal]
    
    private lateinit var botMember: KookComponentGuildBot
    
    override val bot: KookComponentGuildBot
        get() {
            // 不关心实例唯一性
            if (::botMember.isInitialized) {
                return botMember
            }
            
            return baseBot.toMemberBot(internalMember(baseBot.id)!!).also {
                botMember = it
            }
        }
    
    @Volatile
    private lateinit var lastOwnerMember: KookGuildMemberImpl
    
    @Volatile
    internal var initTimestamp: Long = 0L
    
    private suspend fun init() {
        val guildId = source.id
        var owner: KookGuildMemberImpl? = null
        
        syncChannels()
        
        baseBot.logger.info(
            "Sync channels data and channel categories data finished. {} channels of data have been synchronized, {} channel categories of date have been synchronized.",
            internalChannels.size,
            internalChannelCategories.size
        )
        
        syncMembers { member ->
            if (owner == null && member != null && member.id == ownerId) {
                owner = member
            }
        }
        
        baseBot.logger.info(
            "Sync member data finished, {} members of data have been synchronized.",
            internalMembers.size
        )
        
        
        this.lastOwnerMember = owner ?: KookGuildMemberImpl(
            baseBot,
            this,
            UserViewRequest(ownerId, guildId).requestDataBy(baseBot).toModel()
        )
        initTimestamp = System.currentTimeMillis()
    }
    
    
    override val currentMember: Int
        get() = internalMembers.size
    
    override val currentChannel: Int
        get() = internalChannelCategories.size
    
    private val ownerSyncLock = Mutex()
    
    private suspend fun syncLastOwnerMember(id: String): KookGuildMemberImpl {
        return internalMembers[id] ?: ownerSyncLock.withLock {
            internalMembers[id] ?: run {
                val member = KookGuildMemberImpl(
                    baseBot,
                    this@KookGuildImpl,
                    UserViewRequest(ownerId, source.id).requestDataBy(baseBot).toModel()
                )
                internalMembers.merge(id, member) { _, cur -> cur }!!
            }
        }.also {
            lastOwnerMember = it
        }
    }
    
    override suspend fun owner(): KookGuildMember {
        val ownerId = ownerId
        if (lastOwnerMember.id == ownerId) {
            return lastOwnerMember
        }
        return syncLastOwnerMember(ownerId.literal)
    }
    
    override val owner: KookGuildMember
        get() {
            val ownerId = ownerId
            if (lastOwnerMember.id == ownerId) {
                return lastOwnerMember
            }
            return runInBlocking { syncLastOwnerMember(ownerId.literal) }
        }
    
    override val members: Items<KookGuildMember>
        get() = internalMembers.values.asItems()
    
    override val memberList: List<KookGuildMember>
        get() = internalMembers.values.toList()
    
    override fun getMember(id: ID): KookGuildMember? = internalMembers[id.literal]
    
    override val channels: Items<KookChannel>
        get() = internalChannels.values.asItems()
    
    override fun getChannel(id: ID): KookChannel? = internalChannels[id.literal]
    
    override val channelList: List<KookChannel>
        get() = internalChannels.values.toList()
    
    override val categories: List<KookChannelCategory>
        get() = internalChannelCategories.values.toList()
    
    override fun getCategory(id: ID): KookChannelCategory? = internalChannelCategories[id.literal]
    
    override fun toString(): String {
        return "KookGuildImpl(id=$id, name=$name, source=$source)"
    }
    
    /**
     * 同步当前频道服务器中的频道与成员信息。
     */
    internal suspend fun sync(batchDelay: Long) {
        syncChannels(batchDelay)
        syncMembers(batchDelay)
    }
    
    
    /**
     * 同步频道列表。
     */
    private suspend fun syncChannels(batchDelay: Long = 0L) {
        requestChannels(source.id, batchDelay = batchDelay)
            .buffer(100)
            .map(ChannelInfo::toModel)
            .collect(::computeMergeChannelModel)
    }
    
    private suspend inline fun syncMembers(
        batchDelay: Long = 0L,
        crossinline onComputed: (KookGuildMemberImpl?) -> Unit = {},
    ) {
        requestGuildUsers(source.id, batchDelay = batchDelay)
            .buffer(100)
            .map(GuildUser::toModel)
            .collect { model ->
                val computed = internalMembers.compute(model.id.literal) { _, current ->
                    if (current != null) {
                        current.source = model
                        current
                    } else {
                        KookGuildMemberImpl(baseBot, this, model)
                    }
                }
                
                onComputed(computed)
            }
    }
    
    private fun requestChannels(guildId: ID, type: Int? = null, batchDelay: Long = 0L): Flow<ChannelInfo> = flow {
        var page = 1
        do {
            if (page > 1) {
                delay(batchDelay)
            }
            baseBot.logger.debug("Sync channel data ... page {}", page)
            val result = ChannelListRequest(guildId = guildId, type = type, page = page).requestDataBy(baseBot)
            val channels = result.items
            baseBot.logger.debug("{} channel data synced in page {}", channels.size, page)
            channels.forEach {
                emit(it)
            }
            page = result.meta.page + 1
            
        } while (channels.isNotEmpty() && result.meta.page < result.meta.pageTotal)
    }
    
    
    private fun requestGuildUsers(guildId: ID, batchDelay: Long = 0L): Flow<GuildUser> = flow {
        var page = 1
        do {
            if (page > 1) {
                delay(batchDelay)
            }
            baseBot.logger.debug("Sync member data ... page {}", page)
            val usersResult = GuildUserListRequest(guildId = guildId, page = page).requestDataBy(baseBot)
            val users = usersResult.items
            baseBot.logger.debug("{} member data synced in page {}", users.size, page)
            users.forEach {
                emit(it)
            }
            page = usersResult.meta.page + 1
            delay(batchDelay)
        } while (users.isNotEmpty() && usersResult.meta.page < usersResult.meta.pageTotal)
        
    }
    
    
    companion object {
        internal suspend fun GuildModel.toKookGuild(
            baseBot: KookComponentBotImpl,
        ): KookGuildImpl {
            return KookGuildImpl(baseBot, this).apply { init() }
        }
    }
}


