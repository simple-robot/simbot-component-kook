/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kaiheila.event.system.guild.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.Timestamp

/**
 * 新成员加入服务器
 *
 * `joined_guild`
 * @author ForteScarlet
 */

public interface JoinedGuildEventBody : GuildMemberEventExtraBody {
    
    /**
     * 用户ID。
     */
    public val userId: ID
    
    /**
     * 加入时间。
     */
    public val joinedAt: Timestamp


}


/**
 * 新成员加入服务器
 *
 * `joined_guild`
 * @author ForteScarlet
 */
@Serializable
internal data class JoinedGuildEventBodyImpl(
    @SerialName("user_id")
    override val userId: CharSequenceID,

    @SerialName("joined_at")
    override val joinedAt: Timestamp,

) : JoinedGuildEventBody

