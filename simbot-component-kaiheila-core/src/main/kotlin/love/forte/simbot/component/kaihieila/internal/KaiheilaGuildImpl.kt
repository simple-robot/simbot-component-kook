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

import kotlinx.coroutines.flow.*
import love.forte.simbot.*
import love.forte.simbot.component.kaihieila.*
import love.forte.simbot.component.kaihieila.util.*
import love.forte.simbot.definition.*
import love.forte.simbot.definition.Channel
import love.forte.simbot.definition.Role
import love.forte.simbot.kaiheila.api.*
import love.forte.simbot.kaiheila.api.guild.*
import love.forte.simbot.kaiheila.api.user.*
import love.forte.simbot.kaiheila.objects.User
import love.forte.simbot.utils.*
import java.util.stream.*
import kotlin.streams.*
import love.forte.simbot.kaiheila.objects.Guild as KhlGuild

/**
 *
 * @author ForteScarlet
 */
internal class KaiheilaGuildImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceGuild: KhlGuild,
    private val lazyOwner: LazyValue<KaiheilaGuildMember>
) : KaiheilaGuild, GuildInfo by sourceGuild {

    @Api4J
    override val owner: KaiheilaGuildMember
        get() = runInBlocking { owner() }

    override suspend fun owner(): KaiheilaGuildMember {
        return lazyOwner()
    }

    override suspend fun member(id: ID): KaiheilaGuildMember? {
        val user: User? = kotlin.runCatching {
            UserViewRequest(id, sourceGuild.id).requestDataBy(bot)
        }.getOrElse { e ->
            if (e is KaiheilaApiException && e.code == KaiheilaApiException.NOT_FOUNT) {
                null
            } else {
                throw e
            }
        }

        return user?.asMember(bot, this)
    }

    override fun getMember(id: ID): KaiheilaGuildMember? = runInBlocking { member(id) }

    override suspend fun members(groupingId: ID?, limiter: Limiter): Flow<KaiheilaGuildMember> {
        val offset = limiter.offset
        val limit = limiter.limit
        val batchSize = limiter.batchSize

        return if (groupingId == null && (offset == 0 && limit == 0)) {
            flow<GuildUser> {
                var page = 0
                var result: GuildUserList
                do {
                    result = GuildUserListRequest(
                        guildId = id, page = page++, pageSize = batchSize
                    ).requestDataBy(bot)
                } while (result.items.isNotEmpty());
            }.map {
                it.asMember(bot, this)
            }
        } else {
            GuildUserListRequest(
                guildId = id, page = limiter.pageNum, pageSize = limiter.pageSize
            ).requestDataBy(bot).items.asFlow().map {
                it.asMember(bot, this)
            }
        }
    }

    @Api4J
    override fun getMembers(groupingId: ID?, limiter: Limiter): Stream<out KaiheilaGuildMember> {
        val offset = limiter.offset
        val limit = limiter.limit
        val batchSize = limiter.batchSize

        return if (groupingId == null && (offset == 0 && limit == 0)) {
            sequence<GuildUser> {
                var page = 0
                var result: GuildUserList
                do {
                    result = runInBlocking {
                        GuildUserListRequest(
                            guildId = id, page = page++, pageSize = batchSize
                        ).requestDataBy(bot)
                    }

                } while (result.items.isNotEmpty());
            }.map {
                it.asMember(bot, this)
            }.asStream()
        } else {
            val result = runInBlocking {
                GuildUserListRequest(
                    guildId = id, page = limiter.pageNum, pageSize = limiter.pageSize
                ).requestDataBy(bot)
            }

            result.items.asSequence().map {
                it.asMember(bot, this)
            }.asStream()
        }
    }


    //region channels
    override suspend fun children(groupingId: ID?): Flow<Channel> {
        TODO("Not yet implemented")
    }

    override suspend fun children(groupingId: ID?, limiter: Limiter): Flow<Channel> {
        TODO("Not yet implemented")
    }

    @Api4J
    override fun getChildren(groupingId: ID?, limiter: Limiter): Stream<out Channel> {
        TODO("Not yet implemented")
    }
    //endregion

    override suspend fun roles(groupingId: ID?, limiter: Limiter): Flow<Role> { // TODO
        return emptyFlow()
    }

    @Api4J
    override fun getRoles(groupingId: ID?, limiter: Limiter): Stream<out Role> { // TODO
        return Stream.empty()
    }
}