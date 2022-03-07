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
public interface MessageEvent<E : MessageEventExtra> : Event<E> {
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
@kotlinx.serialization.Serializable
internal data class MessageEventImpl<E : MessageEventExtra>(
    override val channelType: Channel.Type,
    override val type: Event.Type,
    override val targetId: CharSequenceID,
    override val authorId: CharSequenceID,
    override val content: String,
    override val msgId: CharSequenceID,
    override val msgTimestamp: Timestamp,
    override val nonce: String,
    override val extra: E
) : MessageEvent<E>


/**
 * 与消息事件相关的抽象类定义。
 */
public abstract class AbstractMessageEvent<E : MessageEventExtra> : MessageEvent<E>


// public fun EventLocator.registerMessageEventCoordinates() {
//     TextEventImpl.run {
//         registerCoordinates()
//     }
//
//     ImageEventImpl.run {
//         registerCoordinates()
//     }
//
//     VideoEventImpl.run {
//         registerCoordinates()
//     }
//
//     FileEventImpl.run {
//         registerCoordinates()
//     }
//
//     CardEventImpl.run {
//         registerCoordinates()
//     }
//
//     KMarkdownEventImpl.run {
//         registerCoordinates()
//     }
// }


//region External interface
//

/**
 * 所有的消息事件
 */
public interface MessageEventExternal {
    public interface Group : MessageEventExternal
    public interface Person : MessageEventExternal
}

/**sea 纯文本消息事件。
 */
public sealed interface TextEvent : MessageEvent<TextEventExtra>, MessageEventExternal {
    public interface Group : TextEvent, MessageEventExternal.Group
    public interface Person : TextEvent, MessageEventExternal.Person
}

/**
 * 图片消息事件。
 */
public sealed interface ImageEvent : MessageEvent<ImageEventExtra>, MessageEventExternal {
    public interface Group : ImageEvent, MessageEventExternal.Group
    public interface Person : ImageEvent, MessageEventExternal.Person
}

/**
 * 文件消息事件。
 */
public sealed interface FileEvent : MessageEvent<FileEventExtra>, MessageEventExternal {
    public interface Group : FileEvent, MessageEventExternal.Group
    public interface Person : FileEvent, MessageEventExternal.Person
}

/**
 * 视频消息事件。
 */
public sealed interface VideoEvent : MessageEvent<VideoEventExtra>, MessageEventExternal {
    public interface Group : VideoEvent, MessageEventExternal.Group
    public interface Person : VideoEvent, MessageEventExternal.Person
}

/**
 * 卡片消息事件。
 */
public sealed interface CardEvent : MessageEvent<CardEventExtra>, MessageEventExternal {
    public interface Group : CardEvent, MessageEventExternal.Group
    public interface Person : CardEvent, MessageEventExternal.Person
}

/**
 * `KMarkdown` 消息事件。
 */
public sealed interface KMarkdownEvent : MessageEvent<KMarkdownEventExtra>, MessageEventExternal {
    public interface Group : KMarkdownEvent, MessageEventExternal.Group
    public interface Person : KMarkdownEvent, MessageEventExternal.Person
}

//endregion



