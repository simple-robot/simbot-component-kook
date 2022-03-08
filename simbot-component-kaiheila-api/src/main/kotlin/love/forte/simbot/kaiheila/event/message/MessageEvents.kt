/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kaiheila.event.message

import kotlinx.serialization.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.event.*
import love.forte.simbot.kaiheila.objects.*


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


/**
 * 系统相关事件接口。
 */
public interface SystemEvent<out B, out EX : Event.Extra.Sys<B>> : Event<EX> {
    override val channelType: Channel.Type
    override val type: Event.Type
    override val targetId: ID
    override val authorId: ID
    override val content: String
    override val msgId: ID
    override val msgTimestamp: Timestamp
    override val nonce: String
    override val extra: EX
}

/**
 * 针对 [Event.Extra.Sys] 的简单实现。
 */
@Serializable
public data class SimpleSystemEventExtra<out B>(override val type: String, override val body: B) : Event.Extra.Sys<B>


@Serializable
internal data class SystemEventImpl<B>(
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
    override val extra: SimpleSystemEventExtra<B>
) : SystemEvent<B, SimpleSystemEventExtra<B>>

