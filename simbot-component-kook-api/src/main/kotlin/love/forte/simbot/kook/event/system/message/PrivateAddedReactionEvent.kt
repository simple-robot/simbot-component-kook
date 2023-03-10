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

package love.forte.simbot.kook.event.system.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.kook.objects.ReactionEmoji

/**
 * 私聊内用户添加reaction
 *
 * type: [love.forte.simbot.kook.event.system.message.MessageEvents.PRIVATE_ADDED_REACTION]
 * @author ForteScarlet
 */
public interface PrivateAddedReactionEventBody : PrivateMessageEventExtraBody {
    public val msgId: ID
    public val chatCode: ID
    public val channelId: ID
    public val emoji: ReactionEmoji
    public val userId: ID
}


/**
 * 私聊内用户添加reaction
 *
 * `private_added_reaction`
 * @author ForteScarlet
 */
@Serializable
internal data class PrivateAddedReactionEventBodyImpl(
    @SerialName("msg_id")
    override val msgId: CharSequenceID,
    @SerialName("chat_code")
    override val chatCode: CharSequenceID,
    @SerialName("channel_id")
    override val channelId: CharSequenceID,
    override val emoji: ReactionEmoji,
    @SerialName("user_id")
    override val userId: CharSequenceID,
) : PrivateAddedReactionEventBody

