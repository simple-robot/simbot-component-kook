package love.forte.simbot.kaiheila.event.message

import love.forte.simbot.kaiheila.event.*


/**
 * 文本消息事件处理器。
 * @see TextEventExtra
 */
public val TextEventProcessor: MessageEventProcessor<TextEventExtra> = MessageEventProcessor(Event.Type.TEXT, TextEventExtraImpl.serializer())

/**
 * 图片消息事件处理器。
 * @see ImageEventExtra
 */
public val ImageEventProcessor: MessageEventProcessor<ImageEventExtra> = MessageEventProcessor(Event.Type.IMAGE, ImageEventExtraImpl.serializer())

/**
 * 视频消息事件处理器。
 * @see VideoEventExtra
 */
public val VideoEventProcessor: MessageEventProcessor<VideoEventExtra> = MessageEventProcessor(Event.Type.VIDEO, VideoEventExtraImpl.serializer())

/**
 * 文件消息事件处理器。
 * @see FileEventExtra
 */
public val FileEventProcessor: MessageEventProcessor<FileEventExtra> = MessageEventProcessor(Event.Type.FILE, FileEventExtraImpl.serializer())

/**
 * KMarkdown消息事件处理器。
 * @see KMarkdownEventExtra
 */
public val KMarkdownEventProcessor: MessageEventProcessor<KMarkdownEventExtra> = MessageEventProcessor(Event.Type.KMD, KMarkdownEventExtraImpl.serializer())

/**
 * Card消息事件处理器。
 * @see CardEventExtra
 */
public val CardEventProcessor: MessageEventProcessor<CardEventExtra> = MessageEventProcessor(Event.Type.CARD, CardEventExtraImpl.serializer())

