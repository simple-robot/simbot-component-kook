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

import kotlinx.coroutines.flow.Flow
import love.forte.simbot.Api4J
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.definition.GuildMember
import love.forte.simbot.definition.Role
import love.forte.simbot.definition.UserStatus
import love.forte.simbot.kaiheila.api.guild.GuildMuteType
import love.forte.simbot.utils.runInBlocking
import java.util.concurrent.TimeUnit
import java.util.stream.Stream
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import love.forte.simbot.kaiheila.objects.User as KhlUser

/**
 *
 * 开黑啦组件下，[频道服务器][KaiheilaGuild] 的成员信息.
 *
 * @author ForteScarlet
 */
public interface KaiheilaGuildMember : GuildMember, KaiheilaComponentDefinition<KhlUser> {
    override val bot: KaiheilaComponentBot
    override val id: ID

    /**
     * 此成员对应的源用户实例。
     */
    override val source: KhlUser

    //region mute api
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


    /**
     * 对此用户进行静音操作。
     * 默认情况下，[mute] 代表使用类型 `1`: 麦克风静音。
     *
     * @see love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest
     */
    @Api4J
    override fun muteBlocking(time: Long, unit: TimeUnit): Boolean = runInBlocking { mute(unit.toMillis(time).milliseconds) }

    /**
     * 对此用户进行静音操作。
     *
     * @see love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest
     */
    @Api4J
    public fun muteBlocking(time: Long, unit: TimeUnit, type: Int): Boolean = runInBlocking { mute(unit.toMillis(time).milliseconds, type) }

    /**
     * 取消禁言。没有参数的 [unmute] 默认情况下，代表使用类型 `1`: 麦克风静音。
     *
     * @see love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest
     */
    @Api4J
    override fun unmuteBlocking(): Boolean = runInBlocking { unmute() }


    /**
     * 取消禁言。[type] 代表 [love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest] 的参数 `type`. 默认使用 `1`.
     *
     * @see love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest
     */
    @Api4J
    public fun unmuteBlocking(type: Int): Boolean = runInBlocking { unmute(type) }

    //endregion


    @OptIn(Api4J::class)
    override val guild: KaiheilaGuild

    @OptIn(Api4J::class)
    override val organization: KaiheilaGuild
        get() = guild

    override suspend fun organization(): KaiheilaGuild = guild
    override suspend fun guild(): KaiheilaGuild = guild

    @Api4J
    override val roles: Stream<out Role>
    override suspend fun roles(): Flow<Role>

    override val joinTime: Timestamp get() = Timestamp.notSupport()
    override val nickname: String
    override val avatar: String
    override val status: UserStatus
    override val username: String

    public companion object {
        }

}