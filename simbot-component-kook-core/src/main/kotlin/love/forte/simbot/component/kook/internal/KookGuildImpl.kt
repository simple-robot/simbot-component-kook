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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.component.kook.KookChannel
import love.forte.simbot.component.kook.KookComponentGuildBot
import love.forte.simbot.component.kook.KookGuild
import love.forte.simbot.component.kook.KookGuildMember
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
internal class KookGuildImpl constructor(
    private val baseBot: KookComponentBotImpl,
    @Volatile override var source: GuildModel,
) : KookGuild, CoroutineScope {
    
    
    internal val job = SupervisorJob(baseBot.job)
    override val coroutineContext: CoroutineContext = baseBot.coroutineContext + job
    
    override val createTime: Timestamp get() = Timestamp.notSupport()
    
    override val maximumMember: Int get() = source.maximumMember
    override val id: ID get() = source.id
    override val description: String get() = source.description
    override val maximumChannel: Int get() = source.maximumChannel
    
    override val ownerId: ID get() = source.ownerId
    override val name: String get() = source.name
    override val icon: String get() = source.icon
    
    // TODO impl
    @JvmSynthetic
    internal lateinit var internalChannelCategories: ConcurrentHashMap<String, KookChannelCategoryImpl>
    
    @JvmSynthetic
    internal lateinit var internalChannels: ConcurrentHashMap<String, KookChannelImpl>
    
    @JvmSynthetic
    internal lateinit var internalMembers: ConcurrentHashMap<String, KookGuildMemberImpl>
    
    @JvmSynthetic
    internal fun internalChannel(id: ID): KookChannelImpl? = internalChannel(id.literal)
    
    @JvmSynthetic
    internal fun internalChannel(id: String): KookChannelImpl? = internalChannels[id]
    
    @JvmSynthetic
    internal fun internalMember(id: ID): KookGuildMemberImpl? = internalMember(id.literal)
    
    @JvmSynthetic
    internal fun internalMember(id: String): KookGuildMemberImpl? = internalMembers[id]
    
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
    
    @JvmSynthetic
    @Volatile
    private lateinit var lastOwnerMember: KookGuildMemberImpl
    
    @Volatile
    internal var initTimestamp: Long = 0L
    
    internal suspend fun init() {
        val guildId = source.id
        
        val channelsMap = ConcurrentHashMap<String, KookChannelImpl>()
        val membersMap = ConcurrentHashMap<String, KookGuildMemberImpl>()
        var owner: KookGuildMemberImpl? = null
        
        // sync channels
        requestChannels(guildId)
            .buffer(100)
            .map { it.toModel() }
            .map { channel -> KookChannelImpl(baseBot, this, channel) }
            .collect {
                channelsMap.merge(it.id.literal, it) { old, cur ->
                    old.cancel()
                    cur
                }
            }
        
        baseBot.logger.info("Sync channel data finished. {} channels of data have been synchronized.", channelsMap.size)
        
        
        // sync members
        requestGuildUsers(guildId)
            .buffer(100)
            .map { it.toModel() }
            .map { user -> KookGuildMemberImpl(baseBot, this, user) }
            .collect {
                val member = membersMap.merge(it.id.literal, it) { old, cur ->
                    old.cancel()
                    cur
                }
                
                if (owner == null && member != null && member.id == ownerId) {
                    owner = member
                }
            }
        
        baseBot.logger.info("Sync member data finished, {} members of data have been synchronized.", membersMap.size)
        
        
        this.internalChannels = channelsMap
        this.internalMembers = membersMap
        this.lastOwnerMember = owner ?: KookGuildMemberImpl(
            baseBot,
            this,
            UserViewRequest(ownerId, guildId).requestDataBy(baseBot).toModel()
        )
        initTimestamp = System.currentTimeMillis()
    }
    
    override val currentMember: Int
        get() = internalChannels.values.sumOf { c -> c.currentMember }
    
    override val currentChannel: Int
        get() = internalChannels.size
    
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
    
    override suspend fun member(id: ID): KookGuildMember? {
        return internalMembers[id.literal]
    }
    
    override fun getMember(id: ID): KookGuildMember? = internalMembers[id.literal]
    
    
    override val children: Items<KookChannel>
        get() = internalChannels.values.asItems()
    
    
    override fun toString(): String {
        return "KookGuild(source=$source)"
    }
    
    /**
     * 同步当前频道服务器中的频道与成员信息。
     */
    internal suspend fun sync(batchDelay: Long) {
        val guildId = source.id
        syncChannels(guildId, batchDelay)
        syncMembers(guildId, batchDelay)
    }
    
    
    private suspend fun syncChannels(guildId: ID, batchDelay: Long = 0L) {
        requestChannels(guildId, batchDelay = batchDelay)
            .buffer(100)
            .map { it.toModel() }
            .collect { model ->
                internalChannels.compute(model.id.literal) { _, old ->
                    if (old == null) {
                        KookChannelImpl(baseBot, this, model)
                    } else {
                        // update source.
                        old.source = model
                        old
                    }
                }
            }
    }
    
    
    private suspend fun syncMembers(guildId: ID, batchDelay: Long = 0L) {
        requestGuildUsers(guildId, batchDelay = batchDelay)
            .buffer(100)
            .map { it.toModel() }
            .collect { model ->
                internalMembers.compute(model.id.literal) { _, old ->
                    if (old == null) {
                        KookGuildMemberImpl(baseBot, this, model)
                    } else {
                        old.source = model
                        old
                    }
                }
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
    
}