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

package love.forte.simbot.component.kook.internal

import love.forte.simbot.ID
import love.forte.simbot.SimbotIllegalArgumentException
import love.forte.simbot.component.kook.KookUserChat
import love.forte.simbot.component.kook.message.*
import love.forte.simbot.component.kook.message.KookMessageCreatedReceipt.Companion.asReceipt
import love.forte.simbot.component.kook.util.requestBy
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.kook.api.message.DirectMessageCreateRequest
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
        return DirectMessageCreateRequest.byTargetId(
            source.targetInfo.id, text, MessageType.TEXT, null, null
        ).requestDataBy(bot).asReceipt(true, bot)
    }
    
    override suspend fun send(message: Message): KookMessageReceipt {
        return message.sendToDirectByTargetId(bot, source.targetInfo.id, null, null, null)
            ?: throw SimbotIllegalArgumentException("Valid messages must not be empty.")
        // var request = message.toRequest(bot, source.targetInfo.id, null, null, null)
        //     ?: throw SimbotIllegalArgumentException("Valid messages must not be empty.")
        //
        // if (request is MessageCreateRequest) {
        //     request = request.toDirect()
        // }
        //
        // val result = request.requestDataBy(bot)
        // return if (result is MessageCreated) {
        //     result.asReceipt(true, bot)
        // } else {
        //     KookApiRequestedReceipt(result, true, bot)
        // }
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
                ).requestDataBy(bot).asReceipt(true, bot)
            }
            is KookChannelMessageDetailsContent -> {
                val details = message.details
                DirectMessageCreateRequest.byTargetId(
                    targetId = this.id,
                    content = details.content,
                    type = details.type,
                    quote = details.quote?.id,
                    nonce = null,
                ).requestDataBy(bot).asReceipt(true, bot)
            }
            else -> {
                send(message.messages)
            }
        }
    }
    
    override suspend fun delete(): Boolean {
        return UserChatDeleteRequest.create(id).requestBy(bot).isSuccess
    }
}
