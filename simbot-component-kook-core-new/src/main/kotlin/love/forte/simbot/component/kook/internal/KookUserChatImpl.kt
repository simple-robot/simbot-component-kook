/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook,
 * If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.component.kook.internal

import love.forte.simbot.ID
import love.forte.simbot.SimbotIllegalArgumentException
import love.forte.simbot.component.kook.KookUserChat
import love.forte.simbot.component.kook.bot.internal.KookBotImpl
import love.forte.simbot.component.kook.message.*
import love.forte.simbot.component.kook.message.KookMessageCreatedReceipt.Companion.asReceipt
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.component.kook.util.requestResultBy
import love.forte.simbot.delegate.getValue
import love.forte.simbot.delegate.stringID
import love.forte.simbot.kook.api.message.SendDirectMessageApi
import love.forte.simbot.kook.api.userchat.DeleteUserChatApi
import love.forte.simbot.kook.api.userchat.UserChatView
import love.forte.simbot.kook.messages.MessageType
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent

/**
 *
 * @author ForteScarlet
 */
internal class KookUserChatImpl(
    override val bot: KookBotImpl, override val source: UserChatView,
) : KookUserChat {
    override val id: ID by stringID { source.code }
    override val targetId: ID by stringID { source.targetInfo.id }

    override fun toString(): String {
        return "KookUserChat(code=${source.code}, target=${source.targetInfo})"
    }

    override suspend fun send(text: String): KookMessageCreatedReceipt {
        return SendDirectMessageApi
            .createByTargetId(source.targetInfo.id, text, MessageType.TEXT.type, null, null)
            .requestDataBy(bot)
            .asReceipt(true, bot)
    }

    override suspend fun send(message: Message): KookMessageReceipt {
        return message.sendToDirectByTargetId(bot, source.targetInfo.id, null, null, null)
            ?: throw SimbotIllegalArgumentException("Valid messages must not be empty.")
    }

    override suspend fun send(message: MessageContent): KookMessageReceipt {
        return when (message) {
            is KookReceiveMessageContent -> {
                val source = message.source
                SendDirectMessageApi.createByTargetId(
                    targetId = this.source.targetInfo.id,
                    content = source.content,
                    type = source.type?.value,
                    quote = null,
                    nonce = null,
                ).requestDataBy(bot).asReceipt(true, bot)
            }

            is KookChannelMessageDetailsContent -> {
                val details = message.details
                SendDirectMessageApi.createByTargetId(
                    targetId = this.source.targetInfo.id,
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
        return DeleteUserChatApi.create(source.code).requestResultBy(bot).isSuccess
    }
}
