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

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import love.forte.simbot.*
import love.forte.simbot.component.kaihieila.*
import love.forte.simbot.component.kaihieila.KaiheilaGuildMember.Companion.botUserStatus
import love.forte.simbot.component.kaihieila.KaiheilaGuildMember.Companion.normalUserStatus
import love.forte.simbot.component.kaihieila.util.*
import love.forte.simbot.definition.*
import love.forte.simbot.definition.Role
import love.forte.simbot.kaiheila.api.guild.*
import love.forte.simbot.kaiheila.objects.User
import love.forte.simbot.utils.*
import java.util.stream.*
import kotlin.time.*

/**
 *
 * @author ForteScarlet
 */
internal class KaiheilaGuildMemberImpl(
    override val bot: KaiheilaComponentBotImpl,
    private val lazyGuild: LazyValue<KaiheilaGuildImpl>,
    override val sourceUser: User
) : KaiheilaGuildMember, UserInfo by sourceUser {

    override val nickname: String
        get() = sourceUser.nickname ?: ""

    override suspend fun unmute(type: Int): Boolean {
        val guildId = lazyGuild().id
        val userId = sourceUser.id
        GuildMuteCreateRequest(guildId, userId, type).requestDataBy(bot)
        bot.muteJobs.remove("$guildId-$userId")?.cancel()
        return true
    }

    override suspend fun mute(duration: Duration, type: Int): Boolean {
        val mill = duration.inWholeMilliseconds
        return if (mill == 0L) {
            unmute(type)
        } else {
            val guildId = lazyGuild().id
            val userId = sourceUser.id
            GuildMuteCreateRequest(guildId, userId, type).requestDataBy(bot)
            if (mill > 0L) {
                val id = "$guildId-$userId"
                val job = bot.launch(start = CoroutineStart.LAZY) {
                    delay(mill)
                    unmute(type)
                }
                bot.muteJobs.merge(id, job) { old, now ->
                    old.cancel()
                    now
                }
                job.start()
            }
            true
        }
    }

    override suspend fun guild(): KaiheilaGuild = lazyGuild()

    @Api4J
    override val roles: Stream<out Role> // TODO
        get() = Stream.empty()

    override suspend fun roles(): Flow<Role> { // TODO
        return emptyFlow()
    }

    override val status: UserStatus = if (sourceUser.isBot) botUserStatus else normalUserStatus


    override fun toString(): String {
        return "KaiheilaGuildMember(sourceUser=$sourceUser)"
    }


}