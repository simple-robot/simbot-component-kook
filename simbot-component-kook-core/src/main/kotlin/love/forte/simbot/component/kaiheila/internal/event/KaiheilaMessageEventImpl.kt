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

package love.forte.simbot.component.kaiheila.internal.event

import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.component.kaiheila.KaiheilaUserChat
import love.forte.simbot.component.kaiheila.event.KaiheilaBotSelfGroupMessageEvent
import love.forte.simbot.component.kaiheila.event.KaiheilaBotSelfPersonMessageEvent
import love.forte.simbot.component.kaiheila.event.KaiheilaNormalGroupMessageEvent
import love.forte.simbot.component.kaiheila.event.KaiheilaNormalPersonMessageEvent
import love.forte.simbot.component.kaiheila.internal.KaiheilaChannelImpl
import love.forte.simbot.component.kaiheila.internal.KaiheilaComponentBotImpl
import love.forte.simbot.component.kaiheila.internal.KaiheilaGuildMemberImpl
import love.forte.simbot.component.kaiheila.internal.KaiheilaUserChatImpl
import love.forte.simbot.component.kaiheila.message.KaiheilaMessageReceipt
import love.forte.simbot.component.kaiheila.message.KaiheilaReceiveMessageContent
import love.forte.simbot.component.kaiheila.message.toContent
import love.forte.simbot.component.kaiheila.model.toModel
import love.forte.simbot.component.kaiheila.util.requestDataBy
import love.forte.simbot.kaiheila.api.userchat.UserChatCreateRequest
import love.forte.simbot.kaiheila.event.message.MessageEvent
import love.forte.simbot.kaiheila.event.message.MessageEventExtra
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.utils.lazyValue


internal class KaiheilaNormalGroupMessageEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: MessageEvent<MessageEventExtra>,
    override val author: KaiheilaGuildMemberImpl,
    override val channel: KaiheilaChannelImpl,
) : KaiheilaNormalGroupMessageEvent() {
    override val id: ID get() = sourceEvent.msgId
    
    override suspend fun reply(message: Message): KaiheilaMessageReceipt {
        return channel.send(message, sourceEvent.msgId)
    }
    
    override suspend fun reply(text: String): KaiheilaMessageReceipt {
        return channel.send(text, sourceEvent.msgId)
    }
    
    override suspend fun reply(message: MessageContent): KaiheilaMessageReceipt {
        return channel.send(message, sourceEvent.msgId)
    }
    
    override val timestamp: Timestamp
        get() = sourceEvent.msgTimestamp
    override val messageContent: KaiheilaReceiveMessageContent = sourceEvent.toContent(false, bot)


}


internal class KaiheilaNormalPersonMessageEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: MessageEvent<MessageEventExtra>,
) : KaiheilaNormalPersonMessageEvent() {
    override val id: ID get() = sourceEvent.msgId

    
    @OptIn(ExperimentalSimbotApi::class)
    private val userChatView = lazyValue {
        val view = UserChatCreateRequest(sourceEvent.authorId).requestDataBy(bot)
        KaiheilaUserChatImpl(bot, view.toModel())
    }
    
    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun user(): KaiheilaUserChat = userChatView()
    
    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun reply(message: Message): KaiheilaMessageReceipt {
        return user().send(message)
    }
    
    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun reply(text: String): KaiheilaMessageReceipt {
        return user().send(text)
    }
    
    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun reply(message: MessageContent): KaiheilaMessageReceipt {
        return user().send(message)
    }

    override val messageContent: KaiheilaReceiveMessageContent = sourceEvent.toContent(true, bot)
    override val timestamp: Timestamp
        get() = sourceEvent.msgTimestamp
}


internal class KaiheilaBotSelfGroupMessageEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: MessageEvent<MessageEventExtra>,
    override val channel: KaiheilaChannelImpl,
    override val member: KaiheilaGuildMemberImpl,
) : KaiheilaBotSelfGroupMessageEvent() {
    override val id: ID get() = sourceEvent.msgId
    
    
    override suspend fun reply(message: Message): KaiheilaMessageReceipt {
        return channel.send(message, sourceEvent.msgId)
    }
    
    override suspend fun reply(text: String): KaiheilaMessageReceipt {
        return channel.send(text, sourceEvent.msgId)
    }
    
    override suspend fun reply(message: MessageContent): KaiheilaMessageReceipt {
        return channel.send(message, sourceEvent.msgId)
    }
    
    override val timestamp: Timestamp
        get() = sourceEvent.msgTimestamp
    override val messageContent: KaiheilaReceiveMessageContent = sourceEvent.toContent(false, bot)


}


internal class KaiheilaBotSelfPersonMessageEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: MessageEvent<MessageEventExtra>,
) : KaiheilaBotSelfPersonMessageEvent() {
    override val id: ID get() = sourceEvent.msgId


    @OptIn(ExperimentalSimbotApi::class)
    private val userChatView = lazyValue {
        val view = UserChatCreateRequest(sourceEvent.authorId).requestDataBy(bot)
        KaiheilaUserChatImpl(bot, view.toModel())
    }

    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun source(): KaiheilaUserChat = userChatView()
    
    
    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun reply(message: Message): KaiheilaMessageReceipt {
        return source().send(message)
    }
    
    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun reply(text: String): KaiheilaMessageReceipt {
        return source().send(text)
    }
    
    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun reply(message: MessageContent): KaiheilaMessageReceipt {
        return source().send(message)
    }
    
    override val messageContent: KaiheilaReceiveMessageContent = sourceEvent.toContent(true, bot)
    override val timestamp: Timestamp
        get() = sourceEvent.msgTimestamp
}
