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

import kotlinx.serialization.*
import love.forte.simbot.*


/**
 * [服务器成员退出](https://developer.kaiheila.cn/doc/event/guild-member#%E6%9C%8D%E5%8A%A1%E5%99%A8%E6%88%90%E5%91%98%E9%80%80%E5%87%BA)
 *
 *
 */
public interface ExitedGuildEventBody : GuildMemberEventExtraBody {
    /**
     * 用户 id
     */
    public val userId: ID

    /**
     * 退出服务器的时间(毫秒)
     */
    public val exitedAt: Timestamp
}


@Serializable
internal data class ExitedGuildEventBodyImpl(
    @SerialName("user_id")
    override val userId: CharSequenceID,
    @SerialName("exited_at")
    override val exitedAt: Timestamp

) : ExitedGuildEventBody
