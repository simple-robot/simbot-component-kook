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
import love.forte.simbot.component.kook.message.KookKMarkdownMessage.Key.asMessage
import love.forte.simbot.component.kook.message.KookMessages.AT_TYPE_ROLE
import love.forte.simbot.component.kook.message.KookMessages.AT_TYPE_USER
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.kook.api.message.DirectMessageDeleteRequest
import love.forte.simbot.kook.api.message.MessageDeleteRequest
import love.forte.simbot.kook.event.Event
import love.forte.simbot.kook.event.message.*
import love.forte.simbot.literal
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
     * Kook 消息事件中所收到的消息列表。
     *
     * 当 [source] 中的 [extra][Event.extra] 属于 [KMarkdownEventExtra] 时，
     * messages 会至少包含两个部分：
     * 1. 会将这个 [KMarkdownEventExtra] 直接作为 [KookKMarkdownMessage] 置于消息链首。
     * 2. 会将 [Event.content] 作为 [Text] 进行追加，并且会根据当前消息进行**处理**。
     * 3. 其他消息元素解析。
     *
     * 这会在后续详细说明。
     *
     * ## [Event.content] 的处理
     * 如上所述，[Event.content] 被追加的时候会进行 **处理**。
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
     * (met)xxxx(met) hello
     * ```
     *
     * 而由于 [Event.Extra.Text.mention] 出现过一次上述的 `xxxx` 的信息，因此依次移除掉一个 `(met)xxxx(met)`。
     *
     * 如果你希望能够得到最原始的 [Event.content]，那么请通过事件对象获取而不是 [KookReceiveMessageContent]。
     *
     *
     *
     * ```kotlin
     * val event: KookMessageEvent = ... // 一个Kook的消息事件
     * val rawContent = event.sourceEvent.content
     * // ...
     * ```
     *
     * ## 元素冗余
     * 还是如上所述，你会发现 [messages] 会提供一些额外的冗余消息来保证消息链中的信息绝对完整，
     * 比如如果 [extra][Event.extra] 属于 [KMarkdownEventExtra] 类型，则会在消息链首追加一个
     * [KookKMarkdownMessage]。而这时，消息链首已经存在了一个完整的原始消息元素，而其后仍然会继续追加其他的元素拆分：
     * 这就会导致消息链中会出现信息量为原本两倍的内容。
     *
     * 当你了解这件事时，你需要额外的注意：如果你希望复述此消息，你应该使用 [KookReceiveMessageContent] 而不是 [messages]；
     * 同样的，如果你需要处理消息，那么请注意防止出现重复的处理。
     *
     * 有关 [Event.Extra] 类型与对应转化规则的描述请参考 [toMessages] 文档说明。
     *
     *
     */
    override val messages: Messages = source.toMessages()
    
    // TODO -> 内容转化：移除部分冗余文本
    override val plainText: String
        get() = super.plainText
    
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
        return "KookReceiveMessageContent(sourceEvent=$source)"
    }
    
}


/**
 * 将消息事件相关内容转化为 [Messages].
 *
 * ## 元素冗余
 * 将文本事件转化为 [Messages] 时可能会出现消息冗余。
 * 下述为对于各 [Event.Extra] 类型的转化规则表。
 *
 * | [Event.Extra] | 转化规则 |
 * | --- | --- |
 * | [TextEventExtra] | 将 [Event.content] 转化为 [Text] |
 * | [ImageEventExtra] | 将 [ImageEventExtra.attachments] 转化为 [KookAttachmentMessage] |
 * | [FileEventExtra] | 将 [FileEventExtra.attachments] 转化为 [KookAttachmentMessage] |
 * | [VideoEventExtra] | 将 [VideoEventExtra.attachments] 转化为 [KookAttachmentMessage] |
 * | [CardEventExtra] | 将 [Event.content] 转化为 [Text] |
 * | [KMarkdownEventExtra] | 将 [KMarkdownEventExtra] 自身作为 [KookKMarkdownMessage] 添加在链首；<br /> 追加 [Event.content] 转化的 [Text]；<br /> 追加其他由 [mention][Event.Extra.Text.mention]、[mentionRoles][Event.Extra.Text.mentionRoles]、[isMentionAll][Event.Extra.Text.isMentionAll]、[isMentionHere][Event.Extra.Text.isMentionHere] 等信息而解析出来的 [At]、[AtAll]、[KookAtAllHere] 等信息。其中，[AtAll]、[KookAtAllHere] 至多只会各自出现一次。 |
 * | 其他 | 将 [Event.content] 转化为 [Text] |
 *
 */
public fun Event<Event.Extra.Text>.toMessages(): Messages {
    return when (val extra = extra) {
        is TextEventExtra -> {
            extra.toMessages { listOf(content.toText()) }
        }
        is ImageEventExtra -> {
            extra.toMessages { listOf(extra.attachments.asMessage()) }
        }
        is FileEventExtra -> {
            extra.toMessages { listOf(extra.attachments.asMessage()) }
        }
        is VideoEventExtra -> {
            extra.toMessages { listOf(extra.attachments.asMessage()) }
        }
        is KMarkdownEventExtra -> {
            @OptIn(ExperimentalSimbotApi::class)
            extra.toMessages {
                listOf(extra.kmarkdown.asMessage(), content.toTextResolvedByTextEvent(extra))
            }
        }
        is CardEventExtra -> {
            extra.toMessages { listOf(content.toText()) }
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

private const val MET_NAME = "metId"
private const val ROL_NAME = "rolId"
private const val MET_REGEX_VALUE = "\\(met\\)(?<$MET_NAME>([a-zA-Z\\d]+|here|all))\\(met\\)"
private const val ROL_REGEX_VALUE = "\\(rol\\)(?<$ROL_NAME>[a-zA-Z\\d]+)\\(rol\\)"

private val matchRegex = Regex("($MET_REGEX_VALUE)|($ROL_REGEX_VALUE)")


/**
 *
 * 处理目标：
 * - `(met)用户id/here/all(met)`: @用户，all 代表 @所有用户，here 代表 @所有在线用户
 * - `(rol)角色ID(rol)`: @某角色所有用户
 *
 */
internal fun String.toTextResolvedByTextEvent(event: Event.Extra.Text): Text {
    val metAll = event.isMentionAll
    val metHere = event.isMentionHere
    val metSet = event.mention.mapTo(mutableSetOf()) { it.literal }
    val metRoleSet = event.mentionRoles.mapTo(mutableSetOf()) { it.literal }
    
    return toTextResolvedByTextEvent0(metAll, metHere, metSet, metRoleSet)
}

@Suppress("NOTHING_TO_INLINE")
internal inline fun String.toTextResolvedByTextEvent0(
    metAll0: Boolean,
    metHere0: Boolean,
    metSet: MutableSet<String>,
    metRoleSet: MutableSet<String>,
): Text {
    var metAll = metAll0
    var metHere = metHere0
    
    val zeroMark: Byte = 0
    var status: Byte = 0
    if (metAll) {
        status = status or 0b1000
    }
    if (metHere) {
        status = status or 0b0100
    }
    if (metSet.isNotEmpty()) {
        status = status or 0b0010
    }
    if (metRoleSet.isNotEmpty()) {
        status = status or 0b0001
    }
    
    if (status == zeroMark) {
        return toText()
    }
    
    val result = matchRegex.replace(this) { result ->
        if (status == zeroMark) {
            return@replace result.value
        }
        
        if (status and 0b1110 > 0) {
            val metId = result.groups[MET_NAME]
            if (metId != null) {
                val id = metId.value
                when {
                    metAll && id == "all" -> {
                        metAll = false
                        status = status and 0b0111
                        return@replace ""
                    }
                    metHere && id == "here" -> {
                        metHere = false
                        status = status and 0b1011
                        return@replace ""
                    }
                    else -> {
                        if (metSet.isEmpty()) {
                            status = status and 0b1101
                        } else if (metSet.remove(id)) {
                            // the id, try remove.
                            return@replace ""
                        }
                    }
                }
                // end.
                return@replace result.value
            }
        }
        
        
        if (status and 0b0001 > 0) {
            val roleId = result.groups[ROL_NAME]
            if (roleId != null) {
                val id = roleId.value
                if (metRoleSet.isEmpty()) {
                    status = status and 0b1110
                } else if (metRoleSet.remove(id)) {
                    // the id, try remove.
                    return@replace ""
                }
                return@replace result.value
            }
        }
        
        
        result.value
    }
    
    return result.toText()
}

internal fun toMessages(
    contentMessage: List<Message.Element<*>>,
    mention: Collection<ID>, mentionRoles: Collection<ID>,
    isMentionAll: Boolean, isMentionHere: Boolean,
): Messages {
    if (mention.isEmpty() && mentionRoles.isEmpty() && !isMentionAll && !isMentionHere) {
        return contentMessage.toMessages()
    }
    val messages = buildList(mention.size + mentionRoles.size + 3) {
        if (contentMessage.isNotEmpty()) {
            addAll(contentMessage)
        }
        
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



