/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kook 的一部分。
 *
 *  simbot-component-kook 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kook 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

package love.forte.simbot.kook.event.message

import love.forte.simbot.kook.event.Event
import love.forte.simbot.kook.event.EventParser
import love.forte.simbot.kook.event.KookEventParserDefinition
import love.forte.simbot.kook.event.MessageEventParser


/**
 * 消息相关事件所使用的 [KookEventParserDefinition] 基础抽象类。
 */
public abstract class MessageEventDefinition<EX : MessageEventExtra> :
    KookEventParserDefinition<EX, MessageEvent<EX>>


/**
 * 文本消息事件处理器。
 * @see TextEventExtra
 */
public val TextEventParser: MessageEventParser<TextEventExtra> =
    MessageEventParser(Event.Type.TEXT, TextEventExtraImpl.serializer())

/**
 * 文本消息事件定义
 */
public object TextEvent : MessageEventDefinition<TextEventExtra>() {
    override val parser: EventParser<TextEventExtra, MessageEvent<TextEventExtra>>
        get() = TextEventParser
}


/**
 * 图片消息事件处理器。
 * @see ImageEventExtra
 */
public val ImageEventParser: MessageEventParser<ImageEventExtra> =
    MessageEventParser(Event.Type.IMAGE, ImageEventExtraImpl.serializer())

/**
 * 图片消息事件定义
 */
public object ImageEvent : MessageEventDefinition<ImageEventExtra>() {
    override val parser: EventParser<ImageEventExtra, MessageEvent<ImageEventExtra>>
        get() = ImageEventParser
}

/**
 * 视频消息事件处理器。
 * @see VideoEventExtra
 */
public val VideoEventParser: MessageEventParser<VideoEventExtra> =
    MessageEventParser(Event.Type.VIDEO, VideoEventExtraImpl.serializer())

/**
 * 视频消息事件定义
 */
public object VideoEvent : MessageEventDefinition<VideoEventExtra>() {
    override val parser: EventParser<VideoEventExtra, MessageEvent<VideoEventExtra>>
        get() = VideoEventParser
}

/**
 * 文件消息事件处理器。
 * @see FileEventExtra
 */
public val FileEventParser: MessageEventParser<FileEventExtra> =
    MessageEventParser(Event.Type.FILE, FileEventExtraImpl.serializer())

/**
 * 文件消息事件定义
 */
public object FileEvent : MessageEventDefinition<FileEventExtra>() {
    override val parser: EventParser<FileEventExtra, MessageEvent<FileEventExtra>>
        get() = FileEventParser
}

/**
 * KMarkdown消息事件处理器。
 * @see KMarkdownEventExtra
 */
public val KMarkdownEventParser: MessageEventParser<KMarkdownEventExtra> =
    MessageEventParser(Event.Type.KMD, KMarkdownEventExtraImpl.serializer())

/**
 * KMarkdown消息事件定义
 */
public object KMarkdownEvent : MessageEventDefinition<KMarkdownEventExtra>() {
    override val parser: EventParser<KMarkdownEventExtra, MessageEvent<KMarkdownEventExtra>>
        get() = KMarkdownEventParser
}

/**
 * Card消息事件处理器。
 * @see CardEventExtra
 */
public val CardEventParser: MessageEventParser<CardEventExtra> =
    MessageEventParser(Event.Type.CARD, CardEventExtraImpl.serializer())

/**
 * KMarkdown消息事件定义
 */
public object CardEvent : MessageEventDefinition<CardEventExtra>() {
    override val parser: EventParser<CardEventExtra, MessageEvent<CardEventExtra>>
        get() = CardEventParser
}
