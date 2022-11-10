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

package love.forte.simbot.component.kook.message

import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.component.kook.KookComponentBot
import love.forte.simbot.component.kook.message.KookAttachmentMessage.Key.asMessage
import love.forte.simbot.component.kook.message.KookMessages.AT_TYPE_ROLE
import love.forte.simbot.component.kook.message.KookMessages.AT_TYPE_USER
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.component.kook.util.walk
import love.forte.simbot.kook.api.message.DirectMessageDeleteRequest
import love.forte.simbot.kook.api.message.MessageDeleteRequest
import love.forte.simbot.kook.event.Event
import love.forte.simbot.kook.event.message.*
import love.forte.simbot.kook.objects.CardMessage
import love.forte.simbot.literal
import love.forte.simbot.logger.Logger
import love.forte.simbot.logger.LoggerFactory
import love.forte.simbot.message.*
import kotlin.experimental.and
import kotlin.experimental.or

/**
 * Kook 消息事件所收到的消息正文类型。
 *
 * 在 Kook 中，一个完整的接收消息所得到 [messages] 中的元素可能是经过拆解/解析/冗余、扩展的。
 * 如果你希望直接根据真实消息进行**复读**，则请直接使用 [KookReceiveMessageContent]
 * 本身作为消息发送的实体而不是获取其中的 [messages]；同样的原因，如果你需要先对消息进行处理，
 * 请注意消息链中的冗余消息。
 *
 * @author ForteScarlet
 */
public class KookReceiveMessageContent(
    private val isDirect: Boolean,
    internal val source: Event<Event.Extra.Text>,
    private val bot: KookComponentBot,
) : ReceivedMessageContent() {
    
    /**
     * 消息ID。
     */
    override val messageId: ID = source.msgId
    
    /**
     * 消息接收到的原始消息内容。
     */
    public val sourceEvent: Event<Event.Extra.Text> get() = source
    
    /**
     * 得到消息的原始正文信息。同 `sourceEvent.content`。
     *
     * @see sourceEvent
     * @see Event.content
     */
    public val rawContent: String get() = source.content
    
    /**
     * Kook 消息事件中所收到的消息列表。
     *
     * messages 会对接收到的事件中的内容进行解析，并转化为各符合内容的消息链。
     *
     * ## [Event.content] 的处理
     * 当 [Event.extra] 为 [KMarkdownEventExtra] 类型时，[Event.content] 被追加的时候会进行 **处理**。
     * 所谓处理，就是指根据 [Event.Extra.Text] 中的 [mention][Event.Extra.Text.mention]、
     * [mentionRoles][Event.Extra.Text.mentionRoles]、[isMentionAll][Event.Extra.Text.isMentionAll]、
     * [isMentionHere][Event.Extra.Text.isMentionHere] 的信息，根据各信息出现数量依次移除 [Event.content]
     * 中出现的特殊字符串。
     *
     * 例如，一个消息为：
     * ```
     * @forliy hello
     * ```
     *
     * 则 [Event.content] 的实际内容为：
     * ```
     * (met)123456(met) hello
     * ```
     *
     * 而由于 [Event.Extra.Text.mention] 出现过一次上述的 `123456` 的信息，因此依次移除掉一个 `(met)123456(met)`，而最终的 [plainText] 结果则为：
     *
     * ```
     *  hello
     * ```
     *
     * 需要注意的是，这种处理不会移除或清理任何的空字符，所以你可能会发现上面处理结束后的 ` hello` 前是有一个空格的。
     *
     * 如果你希望能够得到最原始的 [Event.content]，那么请通过 [原始事件对象][sourceEvent] 或 [rawContent] 获取，
     * 而不是通过 [KookReceiveMessageContent.plainText] 或 [messages] 中的 [PlainText] 集。
     *
     * ```kotlin
     * val event: KookMessageEvent = ... // 一个Kook的消息事件
     * val rawContent = event.sourceEvent.content
     * // ...
     * ```
     *
     * ## 转化丢失
     * 将消息转化为一个消息链（尤其是转化kmarkdown类型的消息）的过程中有可能会丢失一部分原有的格式。因此当你直接通过 [messages] 重复发送消息时有可能会产生与收到的消息不一致的效果。
     *
     */
    override val messages: Messages by lazy(LazyThreadSafetyMode.PUBLICATION) { source.toMessages() }
    
    /**
     * 通过 [MessageDeleteRequest] 删除当前消息。
     */
    override suspend fun delete(): Boolean {
        val api = if (isDirect) {
            DirectMessageDeleteRequest(messageId)
        } else {
            MessageDeleteRequest(messageId)
        }
        api.requestDataBy(bot)
        
        return true
    }
    
    override fun toString(): String {
        return "KookReceiveMessageContent(sourceEvent=$sourceEvent)"
    }
    
}

private val logger = LoggerFactory.getLogger("love.forte.simbot.component.kook.message.ReceiveMessageContent")

/**
 * 将消息事件相关内容转化为 [Messages].
 *
 * | [Event.Extra] | 转化规则 |
 * | --- | --- |
 * | [TextEventExtra] | 将 [Event.content] 转化为 [Text] |
 * | [ImageEventExtra] | 将 [ImageEventExtra.attachments] 转化为 [KookAttachmentMessage] |
 * | [FileEventExtra] | 将 [FileEventExtra.attachments] 转化为 [KookAttachmentMessage] |
 * | [VideoEventExtra] | 将 [VideoEventExtra.attachments] 转化为 [KookAttachmentMessage] |
 * | [CardEventExtra] | 将 [Event.content] 转化为 [Text] |
 * | [KMarkdownEventExtra] | 将 [Event.content] 转化为 [Text]（会对特殊内容进行适当的移除）；<br /> 追加其他由 [mention][Event.Extra.Text.mention]、[mentionRoles][Event.Extra.Text.mentionRoles]、[isMentionAll][Event.Extra.Text.isMentionAll]、[isMentionHere][Event.Extra.Text.isMentionHere] 等信息而解析出来的 [At]、[AtAll]、[KookAtAllHere] 等信息。其中，[AtAll]、[KookAtAllHere] 至多只会各自出现一次。 |
 * | 其他 | 将 [Event.content] 转化为 [Text] |
 *
 */
public fun Event<Event.Extra.Text>.toMessages(): Messages {
    return when (val extra = extra) {
        is TextEventExtra -> {
            extra.toMessages { listOf(content.toText()) }
        }
        
        // ImageEventExtra, FileEventExtra,VideoEventExtra
        is AttachmentsMessageEventExtra<*> -> {
            extra.toMessages { listOf(extra.attachments.asMessage()) }
        }
        
        is KMarkdownEventExtra -> {
            toMessagesByKMarkdown(content, extra.mention, extra.mentionRoles, extra.isMentionAll, extra.isMentionHere)
        }
        
        is CardEventExtra -> {
            // try decode
            extra.toMessages { tryDecodeCardContent(content, logger) }
        }
        
        else -> {
            extra.toMessages { listOf(content.toText()) }
        }
    }
}

private inline fun Event.Extra.Text.toMessages(contentElement: () -> List<Message.Element<*>>): Messages {
    return toMessages(
        contentElement(),
        mention, mentionRoles, isMentionAll, isMentionHere
    )
}

/**
 * 使用消息事件并将其中的消息内容转化为 [KookChannelMessageDetailsContent].
 */
public fun Event<Event.Extra.Text>.toContent(isDirect: Boolean, bot: KookComponentBot): KookReceiveMessageContent =
    KookReceiveMessageContent(isDirect, this, bot)

@OptIn(ExperimentalSimbotApi::class)
internal fun tryDecodeCardContent(content: String, logger: Logger): List<Message.Element<*>> {
    return runCatching {
        listOf(KookCardMessage(CardMessage.decode(content)))
    }.getOrElse { ex ->
        if (logger.isDebugEnabled) {
            logger.debug("Cannot decode card message content [{}] to CardMessage, as text.", content, ex)
        } else {
            logger.warn("Cannot decode card message content to CardMessage, as text.", ex)
        }
        listOf(content.toText())
    }
}

private const val MET_NAME = "metId"
private const val ROL_NAME = "rolId"
private const val MET_REGEX_VALUE = "\\(met\\)(?<$MET_NAME>([a-zA-Z\\d]+|here|all))\\(met\\)"
private const val ROL_REGEX_VALUE = "\\(rol\\)(?<$ROL_NAME>[a-zA-Z\\d]+)\\(rol\\)"

private val matchRegex = Regex("($MET_REGEX_VALUE)|($ROL_REGEX_VALUE)")


internal data class MentionCount(val id: ID, var count: Int)

internal fun Collection<ID>.toMentionCount(): MutableMap<String, MentionCount> {
    val map = mutableMapOf<String, MentionCount>()
    this.forEach { id ->
        val idValue = id.literal
        map.compute(idValue) { _, current ->
            current?.also { it.count++ } ?: MentionCount(id, 1)
        }
    }
    return map
}

internal fun toMessages(
    contentMessage: List<Message.Element<*>>,
    mention: Collection<ID>, mentionRoles: Collection<ID>,
    isMentionAll: Boolean, isMentionHere: Boolean,
): Messages {
    if (mention.isEmpty() && mentionRoles.isEmpty() && !isMentionAll && !isMentionHere) {
        return contentMessage.toMessages()
    }
    val messages = buildList(contentMessage.size + mention.size + mentionRoles.size + 3) {
        addAll(contentMessage)
        
        if (isMentionAll) {
            add(AtAll)
        }
        
        if (isMentionHere) {
            add(KookAtAllHere)
        }
        
        for (mentionId in mention) {
            add(At(mentionId, type = AT_TYPE_USER))
        }
        
        for (mentionRoleId in mentionRoles) {
            add(At(mentionRoleId, type = AT_TYPE_ROLE))
        }
    }
    
    return messages.toMessages()
}

internal fun toMessagesByKMarkdown(
    content: String,
    mention: Collection<ID>, mentionRoles: Collection<ID>,
    isMentionAll: Boolean, isMentionHere: Boolean,
): Messages {
    if (mention.isEmpty() && mentionRoles.isEmpty() && !isMentionAll && !isMentionHere) {
        return content.toText().toMessages()
    }
    
    var metAll = isMentionAll
    var metHere = isMentionHere
    val metMap = mention.toMentionCount()
    val metRoleMap = mentionRoles.toMentionCount()
    
    val zeroMark: Byte = 0
    var status: Byte = 0
    if (metAll) {
        status = status or 0b1000
    }
    if (metHere) {
        status = status or 0b0100
    }
    if (metMap.isNotEmpty()) {
        status = status or 0b0010
    }
    if (metRoleMap.isNotEmpty()) {
        status = status or 0b0001
    }
    
    return buildMessages {
        var textBuffer: StringBuilder? = StringBuilder()
        fun addText(value: CharSequence, start: Int, end: Int) {
            val tb = textBuffer ?: StringBuilder().also { textBuffer = it }
            tb.append(value, start, end)
        }
        
        fun addText(value: String) {
            val tb = textBuffer ?: StringBuilder().also { textBuffer = it }
            tb.append(value)
        }
        
        fun addElement(element: Message.Element<*>) {
            val tb = textBuffer?.toString()
            if (tb?.isNotEmpty() == true) {
                text(tb)
                textBuffer = null
            }
            
            +element
        }
        
        fun clearBuffer() {
            textBuffer?.toString()?.takeIf { it.isNotEmpty() }?.let { t ->
                text(t)
            }
        }
        
        val matched = matchRegex.walk(content, { c, s, e ->
            addText(c, s, e)
        }) { result ->
            if (status == zeroMark) {
                addText(result.value)
            }
            
            if (status and 0b1110 > 0) {
                val metId = result.groups[MET_NAME]
                if (metId != null) {
                    val id = metId.value
                    var skipText = false
                    when {
                        metAll && id == "all" -> {
                            metAll = false
                            status = status and 0b0111
                            addElement(AtAll)
                            skipText = true
                        }
                        
                        metHere && id == "here" -> {
                            metHere = false
                            status = status and 0b1011
                            addElement(KookAtAllHere)
                            skipText = true
                        }
                        
                        else -> {
                            if (metMap.isEmpty()) {
                                status = status and 0b1101
                            } else {
                                val met = metMap[id]
                                if (met != null) {
                                    met.count--
                                    if (met.count <= 0) {
                                        metMap.remove(id)
                                    }
                                    // mention user
                                    addElement(At(met.id, type = AT_TYPE_USER))
                                    skipText = true
                                }
                            }
                        }
                    }
                    // end.
                    if (!skipText) {
                        addText(result.value)
                    }
                }
            }
            
            
            if (status and 0b0001 > 0) {
                val roleId = result.groups[ROL_NAME]
                if (roleId != null) {
                    var skipText = false
                    val id = roleId.value
                    if (metRoleMap.isEmpty()) {
                        status = status and 0b1110
                    } else {
                        val met = metRoleMap[id]
                        if (met != null) {
                            met.count--
                            if (met.count <= 0) {
                                metRoleMap.remove(id)
                            }
                            // mention role
                            addElement(At(met.id, type = AT_TYPE_ROLE))
                            skipText = true
                        }
                    }
                    if (!skipText) {
                        addText(result.value)
                    }
                }
            }
        }
        
        if (!matched) {
            addText(content)
        }
        
        clearBuffer()
    }
}



