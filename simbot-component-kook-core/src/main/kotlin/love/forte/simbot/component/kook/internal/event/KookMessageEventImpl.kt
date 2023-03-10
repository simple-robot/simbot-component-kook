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

package love.forte.simbot.component.kook.internal.event

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.component.kook.KookChannel
import love.forte.simbot.component.kook.KookGuildMember
import love.forte.simbot.component.kook.KookUserChat
import love.forte.simbot.component.kook.event.KookBotSelfChannelMessageEvent
import love.forte.simbot.component.kook.event.KookBotSelfMessageEvent
import love.forte.simbot.component.kook.event.KookChannelMessageEvent
import love.forte.simbot.component.kook.event.KookContactMessageEvent
import love.forte.simbot.component.kook.internal.KookChannelImpl
import love.forte.simbot.component.kook.internal.KookComponentBotImpl
import love.forte.simbot.component.kook.internal.KookGuildMemberImpl
import love.forte.simbot.component.kook.internal.KookUserChatImpl
import love.forte.simbot.component.kook.message.KookMessageReceipt
import love.forte.simbot.component.kook.message.KookReceiveMessageContent
import love.forte.simbot.component.kook.message.toContent
import love.forte.simbot.component.kook.model.toModel
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.kook.api.userchat.UserChatCreateRequest
import love.forte.simbot.kook.event.message.MessageEvent
import love.forte.simbot.kook.event.message.MessageEventExtra
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.utils.lazyValue


internal class KookChannelMessageEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: MessageEvent<MessageEventExtra>,
    private val _author: KookGuildMemberImpl,
    private val _channel: KookChannelImpl,
) : KookChannelMessageEvent() {
    override val id: ID get() = sourceEvent.msgId
    
    override suspend fun author(): KookGuildMember = _author
    
    override suspend fun channel(): KookChannel = _channel
    
    override suspend fun reply(message: Message): KookMessageReceipt {
        return _channel.send(message, sourceEvent.msgId)
    }
    
    override suspend fun reply(text: String): KookMessageReceipt {
        return _channel.send(text, sourceEvent.msgId)
    }
    
    override suspend fun reply(message: MessageContent): KookMessageReceipt {
        return _channel.send(message, sourceEvent.msgId)
    }
    
    
    override val timestamp: Timestamp
        get() = sourceEvent.msgTimestamp
    override val messageContent: KookReceiveMessageContent = sourceEvent.toContent(false, bot)
    
    
}


internal class KookContactMessageEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: MessageEvent<MessageEventExtra>,
) : KookContactMessageEvent() {
    override val id: ID get() = sourceEvent.msgId
    
    //
    // private val userChatView = lazyValue {
    //     val view = UserChatCreateRequest(sourceEvent.authorId).requestDataBy(bot)
    //     KookUserChatImpl(bot, view.toModel())
    // }
    
    private val userChatViewLock = Mutex()
    
    private var userChatView: KookUserChatImpl? = null
    
    private suspend fun userChatView(): KookUserChatImpl {
        val view = UserChatCreateRequest.create(sourceEvent.authorId).requestDataBy(bot)
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
    override val timestamp: Timestamp
        get() = sourceEvent.msgTimestamp
}


internal class KookBotSelfChannelMessageEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: MessageEvent<MessageEventExtra>,
    private val _channel: KookChannelImpl,
    private val _member: KookGuildMemberImpl,
) : KookBotSelfChannelMessageEvent() {
    override val id: ID get() = sourceEvent.msgId
    
    override suspend fun channel(): KookChannel = _channel
    
    override suspend fun member(): KookGuildMember = _member
    
    override suspend fun reply(message: Message): KookMessageReceipt {
        return _channel.send(message, sourceEvent.msgId)
    }
    
    override suspend fun reply(text: String): KookMessageReceipt {
        return _channel.send(text, sourceEvent.msgId)
    }
    
    override suspend fun reply(message: MessageContent): KookMessageReceipt {
        return _channel.send(message, sourceEvent.msgId)
    }
    
    override val timestamp: Timestamp
        get() = sourceEvent.msgTimestamp
    override val messageContent: KookReceiveMessageContent = sourceEvent.toContent(false, bot)
    
    
}


internal class KookBotSelfMessageEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: MessageEvent<MessageEventExtra>,
) : KookBotSelfMessageEvent() {
    override val id: ID get() = sourceEvent.msgId
    
    
    private val userChatView = lazyValue {
        val view = UserChatCreateRequest.create(sourceEvent.authorId).requestDataBy(bot)
        KookUserChatImpl(bot, view.toModel())
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
    override val timestamp: Timestamp
        get() = sourceEvent.msgTimestamp
}
