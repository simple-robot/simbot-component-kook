/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.component.kaihieila.message

import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.component.kaihieila.message.KaiheilaMessages.AT_TYPE_ROLE
import love.forte.simbot.component.kaihieila.message.KaiheilaMessages.AT_TYPE_USER
import love.forte.simbot.kaiheila.event.Event
import love.forte.simbot.kaiheila.event.message.*
import love.forte.simbot.message.*

/**
 * 开黑啦消息事件所收到的消息正文类型。
 *
 * @author ForteScarlet
 */
public class KaiheilaReceiveMessageContent(internal val source: Event<Event.Extra.Text>) : ReceivedMessageContent() {

    /**
     * 消息ID。
     */
    override val messageId: ID = source.msgId

    /**
     * 开黑啦消息事件中所收到的消息列表。
     */
    override val messages: Messages = source.toMessages()


    override fun toString(): String {
        return "KaiheilaReceiveMessageContent(sourceEvent=$source)"
    }

}

/**
 * 将消息事件相关内容转化为 [Messages].
 */
@OptIn(ExperimentalSimbotApi::class)
public fun Event<Event.Extra.Text>.toMessages(): Messages {
    return when (val extra = extra) {
        is TextEventExtra -> {
            extra.toMessages { content.toText() }
        }
        is ImageEventExtra -> {
            extra.toMessages { extra.attachments.asMessage() }
        }
        is FileEventExtra -> {
            extra.toMessages { extra.attachments.asMessage() }
        }
        is VideoEventExtra -> {
            extra.toMessages { extra.attachments.asMessage() }
        }
        is KMarkdownEventExtra -> {
            extra.toMessages { extra.kmarkdown.asMessage() }
        }
        is CardEventExtra -> {
            extra.toMessages { content.toText() }
        }
        else -> {
            extra.toMessages { content.toText() }
        }
    }
}

private inline fun Event.Extra.Text.toMessages(contentElement: () -> Message.Element<*>?): Messages {
    return toMessages(
        contentElement(),
        mention, mentionRoles, isMentionAll, isMentionHere
    )

}

/**
 * 使用消息事件并将其中的消息内容转化为 [KaiheilaChannelMessageDetailsContent].
 */
public fun Event<Event.Extra.Text>.toContent(): KaiheilaReceiveMessageContent = KaiheilaReceiveMessageContent(this)


internal fun toMessages(
    contentMessage: Message.Element<*>?,
    mention: Collection<ID>, mentionRoles: Collection<ID>,
    isMentionAll: Boolean, isMentionHere: Boolean
): Messages {
    if (mention.isEmpty() && mentionRoles.isEmpty() && !isMentionAll && !isMentionHere) {
        return contentMessage?.toMessages() ?: emptyMessages()
    }
    val messages = buildList(mention.size + mentionRoles.size + 3) {
        if (contentMessage != null) {
            add(contentMessage)
        }

        if (isMentionAll) {
            add(AtAll)
        }

        if (isMentionHere) {
            add(AtAllHere)
        }

        for (mentionId in mention) {
            add(At(mentionId, atType = AT_TYPE_USER))
        }

        for (mentionRoleId in mentionRoles) {
            add(At(mentionRoleId, atType = AT_TYPE_ROLE))
        }
    }

    return messages.toMessages()
}



