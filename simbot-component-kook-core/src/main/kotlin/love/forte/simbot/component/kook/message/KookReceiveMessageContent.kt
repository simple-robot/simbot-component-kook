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
import love.forte.simbot.message.*

/**
 * Kook 消息事件所收到的消息正文类型。
 *
 * @author ForteScarlet
 */
public class KookReceiveMessageContent(private val isDirect: Boolean, internal val source: Event<Event.Extra.Text>, private val bot: KookComponentBot) : ReceivedMessageContent() {

    /**
     * 消息ID。
     */
    override val messageId: ID = source.msgId

    /**
     * Kook 消息事件中所收到的消息列表。
     */
    override val messages: Messages = source.toMessages()
    
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
 */
@OptIn(ExperimentalSimbotApi::class)
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
            extra.toMessages {
                listOf(content.toText(), extra.kmarkdown.asMessage())
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
public fun Event<Event.Extra.Text>.toContent(isDirect: Boolean, bot: KookComponentBot): KookReceiveMessageContent = KookReceiveMessageContent(isDirect, this, bot)


internal fun toMessages(
    contentMessage: List<Message.Element<*>>,
    mention: Collection<ID>, mentionRoles: Collection<ID>,
    isMentionAll: Boolean, isMentionHere: Boolean
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



