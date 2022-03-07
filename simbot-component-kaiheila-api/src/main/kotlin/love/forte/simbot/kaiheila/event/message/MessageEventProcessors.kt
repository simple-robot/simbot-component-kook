package love.forte.simbot.kaiheila.event.message

import love.forte.simbot.kaiheila.event.*


/**
 * 文本消息事件处理器。
 * @see TextEventExtra
 */
public val TextEventParser: MessageEventParser<TextEventExtra> = MessageEventParser(Event.Type.TEXT, TextEventExtraImpl.serializer())

/**
 * 图片消息事件处理器。
 * @see ImageEventExtra
 */
public val ImageEventParser: MessageEventParser<ImageEventExtra> = MessageEventParser(Event.Type.IMAGE, ImageEventExtraImpl.serializer())

/**
 * 视频消息事件处理器。
 * @see VideoEventExtra
 */
public val VideoEventParser: MessageEventParser<VideoEventExtra> = MessageEventParser(Event.Type.VIDEO, VideoEventExtraImpl.serializer())

/**
 * 文件消息事件处理器。
 * @see FileEventExtra
 */
public val FileEventParser: MessageEventParser<FileEventExtra> = MessageEventParser(Event.Type.FILE, FileEventExtraImpl.serializer())

/**
 * KMarkdown消息事件处理器。
 * @see KMarkdownEventExtra
 */
public val KMarkdownEventParser: MessageEventParser<KMarkdownEventExtra> = MessageEventParser(Event.Type.KMD, KMarkdownEventExtraImpl.serializer())

/**
 * Card消息事件处理器。
 * @see CardEventExtra
 */
public val CardEventParser: MessageEventParser<CardEventExtra> = MessageEventParser(Event.Type.CARD, CardEventExtraImpl.serializer())

