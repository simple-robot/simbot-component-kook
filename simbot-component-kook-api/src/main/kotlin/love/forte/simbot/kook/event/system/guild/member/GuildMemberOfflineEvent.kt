/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kook.event.system.guild.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.Timestamp

/**
 *
 *
 *
 * `guild_member_offline`
 *
 * @author ForteScarlet
 */
public interface GuildMemberOfflineEventBody : GuildMemberEventExtraBody {

    /**
     *用户id
     */
    public val userId: ID

    /**
     *用户下线时间(毫秒)
     */
    public val eventTime: Timestamp

    /**
     *服务器id组成的数组, 代表与该用户所在的共同的服务器
     */
    public val guilds: List<ID>
}

/**
 *
 *
 *
 * `guild_member_offline`
 *
 * @author ForteScarlet
 */
@Serializable
internal data class GuildMemberOfflineEventBodyImpl(
    /**
     *用户id
     */
    @SerialName("user_id")
    override val userId: CharSequenceID,
    /**
     *用户下线时间(毫秒)
     */
    @SerialName("event_time")
    override val eventTime: Timestamp,
    /**
     *服务器id组成的数组, 代表与该用户所在的共同的服务器
     */
    override val guilds: List<CharSequenceID>,
) : GuildMemberOfflineEventBody

