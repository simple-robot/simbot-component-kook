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

package love.forte.simbot.component.kook.internal.event

import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
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
    override val author: KookGuildMemberImpl,
    override val channel: KookChannelImpl,
) : KookChannelMessageEvent() {
    override val id: ID get() = sourceEvent.msgId
    
    override suspend fun reply(message: Message): KookMessageReceipt {
        return channel.send(message, sourceEvent.msgId)
    }
    
    override suspend fun reply(text: String): KookMessageReceipt {
        return channel.send(text, sourceEvent.msgId)
    }
    
    override suspend fun reply(message: MessageContent): KookMessageReceipt {
        return channel.send(message, sourceEvent.msgId)
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

    
    @OptIn(ExperimentalSimbotApi::class)
    private val userChatView = lazyValue {
        val view = UserChatCreateRequest(sourceEvent.authorId).requestDataBy(bot)
        KookUserChatImpl(bot, view.toModel())
    }
    
    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun user(): KookUserChat = userChatView()
    
    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun reply(message: Message): KookMessageReceipt {
        return user().send(message)
    }
    
    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun reply(text: String): KookMessageReceipt {
        return user().send(text)
    }
    
    @OptIn(ExperimentalSimbotApi::class)
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
    override val channel: KookChannelImpl,
    override val member: KookGuildMemberImpl,
) : KookBotSelfChannelMessageEvent() {
    override val id: ID get() = sourceEvent.msgId
    
    
    override suspend fun reply(message: Message): KookMessageReceipt {
        return channel.send(message, sourceEvent.msgId)
    }
    
    override suspend fun reply(text: String): KookMessageReceipt {
        return channel.send(text, sourceEvent.msgId)
    }
    
    override suspend fun reply(message: MessageContent): KookMessageReceipt {
        return channel.send(message, sourceEvent.msgId)
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


    @OptIn(ExperimentalSimbotApi::class)
    private val userChatView = lazyValue {
        val view = UserChatCreateRequest(sourceEvent.authorId).requestDataBy(bot)
        KookUserChatImpl(bot, view.toModel())
    }

    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun source(): KookUserChat = userChatView()
    
    
    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun reply(message: Message): KookMessageReceipt {
        return source().send(message)
    }
    
    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun reply(text: String): KookMessageReceipt {
        return source().send(text)
    }
    
    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun reply(message: MessageContent): KookMessageReceipt {
        return source().send(message)
    }
    
    override val messageContent: KookReceiveMessageContent = sourceEvent.toContent(true, bot)
    override val timestamp: Timestamp
        get() = sourceEvent.msgTimestamp
}
