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

package love.forte.simbot.component.kaihieila.internal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import love.forte.simbot.*
import love.forte.simbot.component.kaihieila.KaiheilaGuild
import love.forte.simbot.component.kaihieila.KaiheilaGuildMember
import love.forte.simbot.component.kaihieila.util.requestDataBy
import love.forte.simbot.definition.Role
import love.forte.simbot.kaiheila.api.channel.ChannelListRequest
import love.forte.simbot.kaiheila.api.guild.GuildUserListRequest
import love.forte.simbot.kaiheila.api.user.UserViewRequest
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Stream
import kotlin.coroutines.CoroutineContext
import love.forte.simbot.kaiheila.objects.Channel as KhlChannel
import love.forte.simbot.kaiheila.objects.Guild as KhlGuild

/**
 *
 * @author ForteScarlet
 */
internal class KaiheilaGuildImpl constructor(
    override val bot: KaiheilaComponentBotImpl,
    override val source: KhlGuild,
) : KaiheilaGuild, CoroutineScope {
    internal val job = SupervisorJob(bot.job)
    override val coroutineContext: CoroutineContext = bot.coroutineContext + job

    override val createTime: Timestamp get() = Timestamp.notSupport()

    override val maximumMember: Int get() = source.maximumMember
    override val id: ID get() = source.id
    override val description: String get() = source.description
    override val maximumChannel: Int get() = source.maximumChannel

    override var ownerId: ID = source.ownerId
        internal set(value) {
            if (field != value) {
                field = value
                ownerMember = members[value.literal] ?: runBlocking {
                    KaiheilaGuildMemberImpl(
                        bot,
                        this@KaiheilaGuildImpl,
                        UserViewRequest(ownerId, source.id).requestDataBy(bot)
                    )
                }
            }
        }

    override var name: String = source.name
        internal set
    override var icon: String = source.icon
        internal set

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
    private lateinit var ownerMember: KaiheilaGuildMemberImpl

    @Volatile
    internal var initTimestamp: Long = 0L

    internal suspend fun init() {
        val channelsMap = ConcurrentHashMap<String, KaiheilaChannelImpl>()

        val channelInfoList: List<KhlChannel> = source.channels.ifEmpty {
            ChannelListRequest(source.id).requestDataBy(bot).items
        }

        var owner: KaiheilaGuildMemberImpl? = null

        channelInfoList.forEach {
            val channelImpl = KaiheilaChannelImpl(bot, this, it)
            channelsMap[it.id.literal] = channelImpl
        }

        bot.logger.debug("Sync channel data, {} channel data have been cached.", channelsMap.size)


        val membersMap = ConcurrentHashMap<String, KaiheilaGuildMemberImpl>()

        flow {
            var page = 0
            do {
                bot.logger.debug("Sync member data ... page {}", page)
                val usersResult = GuildUserListRequest(guildId = source.id, page = page).requestDataBy(bot)
                page = usersResult.meta.page + 1
                val users = usersResult.items
                bot.logger.debug("{} member data synchronized in page {}", users.size, page - 1)
                users.forEach {
                    emit(it)
                }
            } while (users.isNotEmpty() && usersResult.meta.page < usersResult.meta.pageTotal)
        }.buffer(100).map { user ->
            KaiheilaGuildMemberImpl(bot, this, user)
        }.collect {
            membersMap[it.id.literal] = it
            if (owner == null && it.id == ownerId) {
                owner = it
            }
        }

        bot.logger.debug("Sync member data, {} member data have been cached.", membersMap.size)


        this.channels = channelsMap
        this.members = membersMap
        this.ownerMember = owner ?: KaiheilaGuildMemberImpl(bot, this, UserViewRequest(ownerId, source.id).requestDataBy(bot))
        initTimestamp = System.currentTimeMillis()
    }

    override val currentMember: Int
        get() = channels.values.sumOf { c -> c.currentMember }

    override val currentChannel: Int
        get() = channels.size


    override suspend fun owner(): KaiheilaGuildMember = ownerMember

    override val owner: KaiheilaGuildMember get() = ownerMember


    override suspend fun member(id: ID): KaiheilaGuildMember? {
        return members[id.literal]

        // return channels.values.find { channel -> channel }
        // val user: User? = kotlin.runCatching {
        //     UserViewRequest(id, sourceGuild.id).requestDataBy(bot)
        // }.getOrElse { e ->
        //     if (e is KaiheilaApiException && e.code == KaiheilaApiException.NOT_FOUNT) {
        //         null
        //     } else {
        //         throw e
        //     }
        // }
        //
        // return user?.asMember(bot, this)
    }

    override fun getMember(id: ID): KaiheilaGuildMember? = members[id.literal]


    override suspend fun members(groupingId: ID?, limiter: Limiter): Flow<KaiheilaGuildMember> {
        return members.values.asFlow().withLimiter(limiter)

        // return members.values.asFlow().withLimiter(limiter)

        // val offset = limiter.offset
        // val limit = limiter.limit
        // val batchSize = limiter.batchSize
        //
        // return if (groupingId == null && (offset == 0 && limit == 0)) {
        //     flow<GuildUser> {
        //         var page = 0
        //         var result: GuildUserList
        //         do {
        //             result = GuildUserListRequest(
        //                 guildId = id, page = page++, pageSize = batchSize
        //             ).requestDataBy(bot)
        //         } while (result.items.isNotEmpty());
        //     }.map {
        //         it.asMember(bot, this)
        //     }
        // } else {
        //     GuildUserListRequest(
        //         guildId = id, page = limiter.pageNum, pageSize = limiter.pageSize
        //     ).requestDataBy(bot).items.asFlow().map {
        //         it.asMember(bot, this)
        //     }
        // }
    }

    override fun getMembers(groupingId: ID?, limiter: Limiter): Stream<out KaiheilaGuildMember> {
        return members.values.stream().withLimiter(limiter)

        // return members.values.stream().withLimiter(limiter)
        // val offset = limiter.offset
        // val limit = limiter.limit
        // val batchSize = limiter.batchSize
        //
        // return if (groupingId == null && (offset == 0 && limit == 0)) {
        //     sequence<GuildUser> {
        //         var page = 0
        //         var result: GuildUserList
        //         do {
        //             result = runInBlocking {
        //                 GuildUserListRequest(
        //                     guildId = id, page = page++, pageSize = batchSize
        //                 ).requestDataBy(bot)
        //             }
        //
        //         } while (result.items.isNotEmpty());
        //     }.map {
        //         it.asMember(bot, this)
        //     }.asStream()
        // } else {
        //     val result = runInBlocking {
        //         GuildUserListRequest(
        //             guildId = id, page = limiter.pageNum, pageSize = limiter.pageSize
        //         ).requestDataBy(bot)
        //     }
        //
        //     result.items.asSequence().map {
        //         it.asMember(bot, this)
        //     }.asStream()
        // }
    }


    //region channels
    override suspend fun children(groupingId: ID?): Flow<KaiheilaChannelImpl> = channels.values.asFlow()

    override suspend fun children(groupingId: ID?, limiter: Limiter): Flow<KaiheilaChannelImpl> =
        channels.values.asFlow().withLimiter(limiter)

    override fun getChildren(groupingId: ID?, limiter: Limiter): Stream<out KaiheilaChannelImpl> =
        channels.values.stream().withLimiter(limiter)
    //endregion

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
}