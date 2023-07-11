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
 * 取消频道置顶消息
 *
 * `unpinned_message`
 *
 * @author ForteScarlet
 */
public interface UnpinnedMessageExtraBody : ChannelEventExtraBody {
    public val channelId: ID
    public val operatorId: ID
    public val msgId : ID
}

/**
 *
 * 取消频道置顶消息
 *
 * `unpinned_message`
 *
 * @author ForteScarlet
 */
@Serializable
public data class UnpinnedMessageExtraBodyImpl(
    @SerialName("channel_id")
    override val channelId: CharSequenceID,
    @SerialName("operator_id")
    override val operatorId: CharSequenceID,
    @SerialName("msg_id")
    override val msgId : CharSequenceID,
) : UnpinnedMessageExtraBody