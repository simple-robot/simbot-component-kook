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

package love.forte.simbot.component.kook.event.internal

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import love.forte.simbot.SimbotIllegalArgumentException
import love.forte.simbot.component.kook.KookChannel
import love.forte.simbot.component.kook.KookMember
import love.forte.simbot.component.kook.KookUserChat
import love.forte.simbot.component.kook.bot.internal.KookBotImpl
import love.forte.simbot.component.kook.event.KookBotSelfChannelMessageEvent
import love.forte.simbot.component.kook.event.KookBotSelfMessageEvent
import love.forte.simbot.component.kook.event.KookChannelMessageEvent
import love.forte.simbot.component.kook.event.KookContactMessageEvent
import love.forte.simbot.component.kook.internal.KookChannelImpl
import love.forte.simbot.component.kook.internal.KookMemberImpl
import love.forte.simbot.component.kook.internal.KookUserChatImpl
import love.forte.simbot.component.kook.message.KookMessageReceipt
import love.forte.simbot.component.kook.message.KookReceiveMessageContent
import love.forte.simbot.component.kook.message.sendToChannel
import love.forte.simbot.component.kook.message.toContent
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.kook.api.userchat.CreateUserChatApi
import love.forte.simbot.kook.event.TextExtra
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.utils.lazyValue
import love.forte.simbot.kook.event.Event as KEvent


internal class KookChannelMessageEventImpl(
    override val bot: KookBotImpl,
    override val sourceEvent: KEvent<TextExtra>,
    private val _author: KookMemberImpl,
    private val _channel: KookChannelImpl,
    override val sourceEventContent: String
) : KookChannelMessageEvent() {

    override suspend fun author(): KookMember = _author

    override suspend fun channel(): KookChannel = _channel

    override suspend fun reply(message: Message): KookMessageReceipt {
        return message.sendToChannel(
            bot,
            targetId = _channel.source.id,
            quote = sourceEvent.msgId,
            tempTargetId = null,
            defaultTempTargetId = _author.source.id
        )
            ?: throw SimbotIllegalArgumentException("Valid messages must not be empty.")
    }

    override suspend fun reply(text: String): KookMessageReceipt {
        return _channel.send(text = text, quote = sourceEvent.msgId)
    }

    override suspend fun reply(message: MessageContent): KookMessageReceipt {
        return _channel.send(message, sourceEvent.msgId)
    }

    override val messageContent: KookReceiveMessageContent = sourceEvent.toContent(false, bot)
}


internal class KookContactMessageEventImpl(
    override val bot: KookBotImpl,
    override val sourceEvent: KEvent<TextExtra>,
    override val sourceEventContent: String
) : KookContactMessageEvent() {

    //
    // private val userChatView = lazyValue {
    //     val view = UserChatCreateRequest(sourceEvent.authorId).requestDataBy(bot)
    //     KookUserChatImpl(bot, view.toModel())
    // }

    private val userChatViewLock = Mutex()

    private var userChatView: KookUserChatImpl? = null

    private suspend fun userChatView(): KookUserChatImpl {
        val view = CreateUserChatApi.create(sourceEvent.authorId).requestDataBy(bot)
        return KookUserChatImpl(bot, view)
    }

    override suspend fun user(): KookUserChat = userChatView ?: userChatViewLock.withLock {
        userChatView ?: userChatView().also { userChatView = it }
    }

    override suspend fun reply(message: Message): KookMessageReceipt {
        return user().send(message)
    }

    override suspend fun reply(text: String): KookMessageReceipt {
        return user().send(text)
    }

    override suspend fun reply(message: MessageContent): KookMessageReceipt {
        return user().send(message)
    }

    override val messageContent: KookReceiveMessageContent = sourceEvent.toContent(true, bot)
}


internal class KookBotSelfChannelMessageEventImpl(
    override val bot: KookBotImpl,
    override val sourceEvent: KEvent<TextExtra>,
    private val _channel: KookChannelImpl,
    private val _member: KookMemberImpl,
    override val sourceEventContent: String
) : KookBotSelfChannelMessageEvent() {
    override suspend fun channel(): KookChannel = _channel

    override suspend fun member(): KookMember = _member

    override suspend fun reply(message: Message): KookMessageReceipt {
        return _channel.send(message, sourceEvent.msgId)
    }

    override suspend fun reply(text: String): KookMessageReceipt {
        return _channel.send(text, sourceEvent.msgId)
    }

    override suspend fun reply(message: MessageContent): KookMessageReceipt {
        return _channel.send(message, sourceEvent.msgId)
    }

    override val messageContent: KookReceiveMessageContent = sourceEvent.toContent(false, bot)

}


internal class KookBotSelfMessageEventImpl(
    override val bot: KookBotImpl,
    override val sourceEvent: KEvent<TextExtra>,
    override val sourceEventContent: String
) : KookBotSelfMessageEvent() {

    private val userChatView = lazyValue {
        val view = CreateUserChatApi.create(sourceEvent.authorId).requestDataBy(bot)
        KookUserChatImpl(bot, view)
    }

    override suspend fun source(): KookUserChat = userChatView()


    override suspend fun reply(message: Message): KookMessageReceipt {
        return source().send(message)
    }

    override suspend fun reply(text: String): KookMessageReceipt {
        return source().send(text)
    }

    override suspend fun reply(message: MessageContent): KookMessageReceipt {
        return source().send(message)
    }

    override val messageContent: KookReceiveMessageContent = sourceEvent.toContent(true, bot)
}
