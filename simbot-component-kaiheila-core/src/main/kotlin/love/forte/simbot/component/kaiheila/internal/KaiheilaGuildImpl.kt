/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kaiheila 的一部分。
 *
 *  simbot-component-kaiheila 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kaiheila 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

package love.forte.simbot.component.kaiheila.internal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import love.forte.simbot.*
import love.forte.simbot.component.kaiheila.KaiheilaGuild
import love.forte.simbot.component.kaiheila.KaiheilaGuildMember
import love.forte.simbot.component.kaiheila.model.GuildModel
import love.forte.simbot.component.kaiheila.model.toModel
import love.forte.simbot.component.kaiheila.util.requestDataBy
import love.forte.simbot.definition.Role
import love.forte.simbot.kaiheila.api.channel.ChannelInfo
import love.forte.simbot.kaiheila.api.channel.ChannelListRequest
import love.forte.simbot.kaiheila.api.guild.GuildUser
import love.forte.simbot.kaiheila.api.guild.GuildUserListRequest
import love.forte.simbot.kaiheila.api.user.UserViewRequest
import love.forte.simbot.utils.runInBlocking
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Stream
import kotlin.coroutines.CoroutineContext

/**
 *
 * @author ForteScarlet
 */
internal class KaiheilaGuildImpl constructor(
    override val bot: KaiheilaComponentBotImpl,
    @Volatile override var source: GuildModel,
) : KaiheilaGuild, CoroutineScope {
    internal val job = SupervisorJob(bot.job)
    override val coroutineContext: CoroutineContext = bot.coroutineContext + job
    
    override val createTime: Timestamp get() = Timestamp.notSupport()
    
    override val maximumMember: Int get() = source.maximumMember
    override val id: ID get() = source.id
    override val description: String get() = source.description
    override val maximumChannel: Int get() = source.maximumChannel
    
    override val ownerId: ID get() = source.ownerId
    override val name: String get() = source.name
    override val icon: String get() = source.icon
    
    @JvmSynthetic
    internal lateinit var channels: ConcurrentHashMap<String, KaiheilaChannelImpl>
    
    @JvmSynthetic
    internal lateinit var members: ConcurrentHashMap<String, KaiheilaGuildMemberImpl>
    
    @JvmSynthetic
    internal fun internalChannel(id: ID): KaiheilaChannelImpl? = internalChannel(id.literal)
    
    @JvmSynthetic
    internal fun internalChannel(id: String): KaiheilaChannelImpl? = channels[id]
    
    @JvmSynthetic
    internal fun internalMember(id: ID): KaiheilaGuildMemberImpl? = internalMember(id.literal)
    
    @JvmSynthetic
    internal fun internalMember(id: String): KaiheilaGuildMemberImpl? = members[id]
    
    @JvmSynthetic
    @Volatile
    private lateinit var lastOwnerMember: KaiheilaGuildMemberImpl
    
    @Volatile
    internal var initTimestamp: Long = 0L
    
    internal suspend fun init() {
        val guildId = source.id
        
        val channelsMap = ConcurrentHashMap<String, KaiheilaChannelImpl>()
        val membersMap = ConcurrentHashMap<String, KaiheilaGuildMemberImpl>()
        var owner: KaiheilaGuildMemberImpl? = null
        
        // sync channels
        requestChannels(guildId)
            .buffer(100)
            .map { it.toModel() }
            .map { channel -> KaiheilaChannelImpl(bot, this, channel) }
            .collect {
                channelsMap.merge(it.id.literal, it) { old, cur ->
                    old.cancel()
                    cur
                }
            }
        
        bot.logger.info("Sync channel data finished. {} channels of data have been synchronized.", channelsMap.size)
        
        
        // sync members
        requestGuildUsers(guildId)
            .buffer(100)
            .map { it.toModel() }
            .map { user -> KaiheilaGuildMemberImpl(bot, this, user) }
            .collect {
                val member = membersMap.merge(it.id.literal, it) { old, cur ->
                    old.cancel()
                    cur
                }
                
                if (owner == null && member != null && member.id == ownerId) {
                    owner = member
                }
            }
        
        bot.logger.info("Sync member data finished, {} members of data have been synchronized.", membersMap.size)
        
        
        this.channels = channelsMap
        this.members = membersMap
        this.lastOwnerMember = owner ?: KaiheilaGuildMemberImpl(bot,
            this,
            UserViewRequest(ownerId, guildId).requestDataBy(bot).toModel())
        initTimestamp = System.currentTimeMillis()
    }
    
    override val currentMember: Int
        get() = channels.values.sumOf { c -> c.currentMember }
    
    override val currentChannel: Int
        get() = channels.size
    
    private val ownerSyncLock = Mutex()
    
    private suspend fun syncLastOwnerMember(id: String): KaiheilaGuildMemberImpl {
        return members[id] ?: ownerSyncLock.withLock {
            members[id] ?: run {
                val member = KaiheilaGuildMemberImpl(bot,
                    this@KaiheilaGuildImpl,
                    UserViewRequest(ownerId, source.id).requestDataBy(bot).toModel())
                members.merge(id, member) { _, cur -> cur }!!
            }
        }.also {
            lastOwnerMember = it
        }
    }
    
    override suspend fun owner(): KaiheilaGuildMember {
        val ownerId = ownerId
        if (lastOwnerMember.id == ownerId) {
            return lastOwnerMember
        }
        return syncLastOwnerMember(ownerId.literal)
    }
    
    override val owner: KaiheilaGuildMember
        get() {
            val ownerId = ownerId
            if (lastOwnerMember.id == ownerId) {
                return lastOwnerMember
            }
            return runInBlocking { syncLastOwnerMember(ownerId.literal) }
        }
    
    
    override suspend fun member(id: ID): KaiheilaGuildMember? {
        return members[id.literal]
    }
    
    override fun getMember(id: ID): KaiheilaGuildMember? = members[id.literal]
    
    
    override suspend fun members(groupingId: ID?, limiter: Limiter): Flow<KaiheilaGuildMember> {
        return members.values.asFlow().withLimiter(limiter)
    }
    
    override fun getMembers(groupingId: ID?, limiter: Limiter): Stream<out KaiheilaGuildMember> {
        return members.values.stream().withLimiter(limiter)
    }
    
    
    // region channels
    override suspend fun children(groupingId: ID?): Flow<KaiheilaChannelImpl> = channels.values.asFlow()
    
    override suspend fun children(groupingId: ID?, limiter: Limiter): Flow<KaiheilaChannelImpl> =
        channels.values.asFlow().withLimiter(limiter)
    
    override fun getChildren(groupingId: ID?, limiter: Limiter): Stream<out KaiheilaChannelImpl> =
        channels.values.stream().withLimiter(limiter)
    // endregion
    
    override suspend fun roles(groupingId: ID?, limiter: Limiter): Flow<Role> {
        // TODO
        return emptyFlow()
    }
    
    @Api4J
    override fun getRoles(groupingId: ID?, limiter: Limiter): Stream<out Role> {
        // TODO
        return Stream.empty()
    }
    
    override fun toString(): String {
        return "KaiheilaGuild(source=$source)"
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
                channels.compute(model.id.literal) { _, old ->
                    if (old == null) {
                        KaiheilaChannelImpl(bot, this, model)
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
                members.compute(model.id.literal) { _, old ->
                    if (old == null) {
                        KaiheilaGuildMemberImpl(bot, this, model)
                    } else {
                        old.source = model
                        old
                    }
                }
            }
    }
    
    
    private fun requestChannels(guildId: ID, type: Int? = null, batchDelay: Long = 0L): Flow<ChannelInfo> = flow {
        var page = 0
        do {
            bot.logger.debug("Sync channel data ... page {}", page)
            val result = ChannelListRequest(guildId = guildId, type = type, page = page).requestDataBy(bot)
            val channels = result.items
            bot.logger.debug("{} channel data synced in page {}", channels.size, page - 1)
            channels.forEach {
                emit(it)
            }
            page = result.meta.page + 1
            delay(batchDelay)
        } while (channels.isNotEmpty() && result.meta.page < result.meta.pageTotal)
    }
    
    
    private fun requestGuildUsers(guildId: ID, batchDelay: Long = 0L): Flow<GuildUser> = flow {
        var page = 0
        do {
            bot.logger.debug("Sync member data ... page {}", page)
            val usersResult = GuildUserListRequest(guildId = guildId, page = page).requestDataBy(bot)
            val users = usersResult.items
            bot.logger.debug("{} member data synced in page {}", users.size, page - 1)
            users.forEach {
                emit(it)
            }
            page = usersResult.meta.page + 1
            delay(batchDelay)
        } while (users.isNotEmpty() && usersResult.meta.page < usersResult.meta.pageTotal)
        
    }
    
}