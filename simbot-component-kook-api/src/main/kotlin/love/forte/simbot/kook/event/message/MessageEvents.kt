/*
 * Copyright (c) 2021-2023. ForteScarlet.
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

package love.forte.simbot.kook.event.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.kook.event.Event
import love.forte.simbot.kook.objects.Attachments
import love.forte.simbot.kook.objects.Channel


/**
 * [消息相关事件列表](https://developer.kaiheila.cn/doc/event/message)
 *
 * 由于消息相关事件中没有系统事件，因此 [Event.extra] 全部都是 [Event.Extra.Text] 类型。
 *
 */
public interface MessageEventExtra : Event.Extra.Text


/**
 * 与资源相关的extra.
 *
 * @see ImageEventExtra
 * @see FileEventExtra
 * @see VideoEventExtra
 *
 */
public interface AttachmentsMessageEventExtra<A : Attachments> : MessageEventExtra {
    public val attachments: A
}


/**
 * 消息相关事件接口。
 *
 */
public interface MessageEvent<out E : MessageEventExtra> : Event<E> {
    override val channelType: Channel.Type
    override val type: Event.Type
    override val targetId: ID
    override val authorId: ID
    override val content: String
    override val msgId: ID
    override val msgTimestamp: Timestamp
    override val nonce: String
    override val extra: E
}


/**
 * [MessageEvent] 的基础实现。
 */
@Serializable
internal data class MessageEventImpl<E : MessageEventExtra>(
    @SerialName("channel_type")
    override val channelType: Channel.Type,
    override val type: Event.Type,
    @SerialName("target_id")
    override val targetId: CharSequenceID,
    @SerialName("author_id")
    override val authorId: CharSequenceID,
    override val content: String,
    @SerialName("msg_id")
    override val msgId: CharSequenceID,
    @SerialName("msg_timestamp")
    override val msgTimestamp: Timestamp,
    override val nonce: String,
    override val extra: E
) : MessageEvent<E>

