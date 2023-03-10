/*
 * Copyright (c) 2021-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.event.system.channel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.kook.objects.ReactionEmoji


/**
 * [频道内用户添加 reaction](https://developer.kaiheila.cn/doc/event/channel#%E9%A2%91%E9%81%93%E5%86%85%E7%94%A8%E6%88%B7%E6%B7%BB%E5%8A%A0%20reaction)
 * @author ForteScarlet
 */
public interface AddedReactionExtraBody : ChannelEventExtraBody {

    /**
     * 用户点击的消息id
     */
    public val msgId: ID

    /**
     * 点击的用户
     */
    public val userId: ID

    /**
     * 频道id
     */
    public val channelId: ID

    /**
     * emoji	Map	消息对象, 包含 id 表情id, name 表情名称
     */
    public val emoji: ReactionEmoji

}

/**
 * [频道内用户添加 reaction](https://developer.kaiheila.cn/doc/event/channel#%E9%A2%91%E9%81%93%E5%86%85%E7%94%A8%E6%88%B7%E6%B7%BB%E5%8A%A0%20reaction)
 * @author ForteScarlet
 */
@Serializable
internal data class AddedReactionExtraBodyImpl(
    /**
     * 用户点击的消息id
     */
    @SerialName("msg_id")
    override val msgId: CharSequenceID,

    /**
     * 点击的用户
     */
    @SerialName("user_id")
    override val userId: CharSequenceID,

    /**
     * 频道id
     */
    @SerialName("channel_id")
    override val channelId: CharSequenceID,

    /**
     * emoji	Map	消息对象, 包含 id 表情id, name 表情名称
     */
    override val emoji: ReactionEmoji,
) : AddedReactionExtraBody
