/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.event.message

import love.forte.simbot.ExperimentalSimbotApi
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
@ExperimentalSimbotApi
public val KMarkdownEventParser: MessageEventParser<KMarkdownEventExtra> =
    MessageEventParser(Event.Type.KMD, KMarkdownEventExtraImpl.serializer())

/**
 * KMarkdown消息事件定义
 */
@ExperimentalSimbotApi
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
