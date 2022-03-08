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

package love.forte.simbot.kaiheila.event.system.channel

import kotlinx.serialization.*
import love.forte.simbot.*


/**
 *
 * 新的频道置顶消息
 *
 * `pinned_message`
 *
 * @author ForteScarlet
 */
public interface PinnedMessageExtraBody : ChannelEventExtraBody {
    /**
     * 发生操作的频道id
     */
    public val channelId: ID

    /**
     * 操作人的用户id
     */
    public val operatorId: ID

    /**
     * 被操作的消息id
     */
    public val msgId: ID
}

/**
 *
 * 新的频道置顶消息
 *
 * `pinned_message`
 *
 * @author ForteScarlet
 */
@Serializable
internal data class PinnedMessageExtraBodyImpl(
    /**
     * 发生操作的频道id
     */
    @SerialName("channel_id")
    override val channelId: CharSequenceID,
    /**
     * 操作人的用户id
     */
    @SerialName("operator_id")
    override val operatorId: CharSequenceID,
    /**
     * 被操作的消息id
     */
    @SerialName("msg_id")
    override val msgId: CharSequenceID
) : PinnedMessageExtraBody