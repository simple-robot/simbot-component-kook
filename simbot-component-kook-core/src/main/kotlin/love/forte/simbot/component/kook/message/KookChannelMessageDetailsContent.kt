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

import love.forte.simbot.ID
import love.forte.simbot.component.kook.KookComponentBot
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.kook.api.message.ChannelMessageDetails
import love.forte.simbot.kook.api.message.MessageDeleteRequest
import love.forte.simbot.kook.api.message.MessageViewRequest
import love.forte.simbot.message.Messages
import love.forte.simbot.message.ReceivedMessageContent
import love.forte.simbot.message.toText

/**
 * 将 [ChannelMessageDetails] 作为消息正文实现。
 *
 * @see ChannelMessageDetails
 * @see MessageViewRequest
 * @author ForteScarlet
 */
public data class KookChannelMessageDetailsContent(internal val details: ChannelMessageDetails, private val bot: KookComponentBot) :
    ReceivedMessageContent() {

    /**
     * 消息ID。
     */
    override val messageId: ID = details.id

    /**
     * Kook 消息事件中所收到的消息列表。
     */
    override val messages: Messages = details.toMessages()
    
    /**
     * 删除当前的频道消息。
     *
     * 通过 [MessageDeleteRequest] 删除消息，除非 [delete] 抛出异常，否则将会恒返回 `true`。
     * 会抛出请求 [MessageDeleteRequest] 过程中可能出现的任何异常。
     *
     */
    override suspend fun delete(): Boolean {
        MessageDeleteRequest(messageId).requestDataBy(bot)
        return true
    }
    
    public companion object {
        /**
         * 使用消息事件并将其中的消息内容转化为 [Messages].
         */
        @JvmStatic
        public fun ChannelMessageDetails.toMessages(): Messages {
            return toMessages(listOf(content.toText()), mention, mentionRoles, isMentionAll, isMentionHere)
        }

        /**
         * 使用消息事件并将其中的消息内容转化为 [KookChannelMessageDetailsContent].
         */
        @JvmStatic
        public fun ChannelMessageDetails.toContent(bot: KookComponentBot): KookChannelMessageDetailsContent =
            KookChannelMessageDetailsContent(this, bot)

    }

}

