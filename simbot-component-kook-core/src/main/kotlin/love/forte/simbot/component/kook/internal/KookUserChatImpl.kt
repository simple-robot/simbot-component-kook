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

package love.forte.simbot.component.kook.internal

import love.forte.simbot.ID
import love.forte.simbot.SimbotIllegalArgumentException
import love.forte.simbot.component.kook.KookUserChat
import love.forte.simbot.component.kook.message.*
import love.forte.simbot.component.kook.message.KookMessageCreatedReceipt.Companion.asReceipt
import love.forte.simbot.component.kook.util.requestBy
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.kook.api.message.DirectMessageCreateRequest
import love.forte.simbot.kook.api.message.MessageCreated
import love.forte.simbot.kook.api.message.MessageType
import love.forte.simbot.kook.api.userchat.UserChatDeleteRequest
import love.forte.simbot.kook.api.userchat.UserChatView
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent

/**
 *
 * @author ForteScarlet
 */
internal class KookUserChatImpl(
    override val bot: KookComponentBotImpl, override val source: UserChatView,
) : KookUserChat {
    override val id: ID
        get() = source.targetInfo.id
    
    override fun toString(): String {
        return "KookUserChat(source=$source, bot=$bot)"
    }
    
    override suspend fun send(text: String): KookMessageCreatedReceipt {
        return DirectMessageCreateRequest.byChatCode(
            source.code, text, MessageType.TEXT, null, null
        ).requestDataBy(bot).asReceipt(true, bot)
    }
    
    override suspend fun send(message: Message): KookMessageReceipt {
        val request = message.toRequest(bot, source.code, null, null, null)
            ?: throw SimbotIllegalArgumentException("Valid messages must not be empty.")
        
        val result = request.requestDataBy(bot)
        return if (result is MessageCreated) {
            result.asReceipt(true, bot)
        } else {
            KookApiRequestedReceipt(result, true, bot)
        }
    }
    
    override suspend fun send(message: MessageContent): KookMessageReceipt {
        return when (message) {
            is KookReceiveMessageContent -> {
                val source = message.source
                DirectMessageCreateRequest.byTargetId(
                    targetId = this.id,
                    content = source.content,
                    type = source.type.type,
                    quote = null,
                    nonce = null,
                ).requestDataBy(bot).asReceipt(false, bot)
            }
            is KookChannelMessageDetailsContent -> {
                val details = message.details
                DirectMessageCreateRequest.byTargetId(
                    targetId = this.id,
                    content = details.content,
                    type = details.type,
                    quote = details.quote?.id,
                    nonce = null,
                ).requestDataBy(bot).asReceipt(false, bot)
            }
            else -> {
                send(message.messages)
            }
        }
    }
    
    override suspend fun delete(): Boolean {
        return UserChatDeleteRequest(id).requestBy(bot).isSuccess
    }
}