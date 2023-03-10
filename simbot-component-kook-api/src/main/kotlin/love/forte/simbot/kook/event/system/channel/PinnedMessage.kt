/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook,
 * If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.event.system.channel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID


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
