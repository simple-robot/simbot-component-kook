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

package love.forte.simbot.component.kaihieila

import kotlinx.coroutines.flow.*
import love.forte.simbot.*
import love.forte.simbot.definition.*
import love.forte.simbot.kaiheila.api.guild.*
import java.util.stream.*
import kotlin.time.*
import love.forte.simbot.kaiheila.objects.User as KhlUser

/**
 *
 * 开黑啦组件下，[频道服务器][KaiheilaGuild] 的成员信息.
 *
 * @author ForteScarlet
 */
public interface KaiheilaGuildMember : GuildMember {
    override val bot: KaiheilaComponentBot
    override val id: ID

    /**
     * 此成员对应的源用户实例。
     */
    public val sourceUser: KhlUser

    /**
     * 取消禁言。没有参数的 [unmute] 默认情况下，代表使用类型 `1`: 麦克风静音。
     *
     * @see love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest
     */
    override suspend fun unmute(): Boolean = unmute(GuildMuteType.TYPE_MICROPHONE)

    /**
     * 取消禁言。[type] 代表 [love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest] 的参数 `type`. 默认使用 `1`.
     *
     * @see love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest
     */
    public suspend fun unmute(type: Int): Boolean


    /**
     * 对此用户进行静音操作。
     * 默认情况下，[mute] 代表使用类型 `1`: 麦克风静音。
     *
     * @see love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest
     */
    override suspend fun mute(duration: Duration): Boolean = mute(duration, GuildMuteType.TYPE_MICROPHONE)

    /**
     * 对此用户进行静音操作。
     *
     * @see love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest
     */
    public suspend fun mute(duration: Duration, type: Int): Boolean


    override suspend fun guild(): KaiheilaGuild

    @Api4J
    override val roles: Stream<out Role>

    override suspend fun roles(): Flow<Role>

    override val joinTime: Timestamp get() = Timestamp.notSupport()
    override val nickname: String
    override val avatar: String
    override val status: UserStatus
    override val username: String

    public companion object {
        internal val botUserStatus = UserStatus.builder().bot().fakeUser().build()
        internal val normalUserStatus = UserStatus.builder().normal().build()
    }

}