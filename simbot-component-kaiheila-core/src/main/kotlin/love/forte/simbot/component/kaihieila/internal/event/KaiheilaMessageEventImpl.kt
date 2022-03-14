package love.forte.simbot.component.kaihieila.internal.event

import love.forte.simbot.*
import love.forte.simbot.component.kaihieila.*
import love.forte.simbot.component.kaihieila.event.*
import love.forte.simbot.component.kaihieila.internal.*
import love.forte.simbot.component.kaihieila.message.*
import love.forte.simbot.component.kaihieila.util.*
import love.forte.simbot.kaiheila.api.message.*
import love.forte.simbot.kaiheila.api.userchat.*
import love.forte.simbot.kaiheila.event.message.*
import love.forte.simbot.utils.*


internal class KaiheilaNormalGroupMessageEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: MessageEvent<MessageEventExtra>,
    override val author: KaiheilaMemberImpl,
    override val channel: KaiheilaChannelImpl
) : KaiheilaNormalMessageEvent.Group<MessageEventExtra>() {
    override val id: ID get() = sourceEvent.msgId

    /**
     * 删除这条消息。
     */
    override suspend fun delete(): Boolean {
        return MessageDeleteRequest(id).requestBy(bot).isSuccess
    }

    override val timestamp: Timestamp
        get() = sourceEvent.msgTimestamp
    override val messageContent: KaiheilaReceiveMessageContent = sourceEvent.toContent()


}


internal class KaiheilaNormalPersonMessageEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: MessageEvent<MessageEventExtra>
) : KaiheilaNormalMessageEvent.Person<MessageEventExtra>() {
    override val id: ID get() = sourceEvent.msgId

    @OptIn(ExperimentalSimbotApi::class)
    private val userChatView = lazyValue {
        val view = UserChatCreateRequest(sourceEvent.authorId).requestDataBy(bot)
        KaiheilaUserChatImpl(bot, view)
    }

    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun user(): KaiheilaUserChat = userChatView()
    override val messageContent: KaiheilaReceiveMessageContent = sourceEvent.toContent()
    override val timestamp: Timestamp
        get() = sourceEvent.msgTimestamp
}


internal class KaiheilaBotSelfGroupMessageEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: MessageEvent<MessageEventExtra>,
    override val author: KaiheilaMemberImpl,
    override val channel: KaiheilaChannelImpl
) : KaiheilaBotSelfMessageEvent.Group<MessageEventExtra>() {
    override val id: ID get() = sourceEvent.msgId

    /**
     * 删除这条消息。
     */
    override suspend fun delete(): Boolean {
        return MessageDeleteRequest(id).requestBy(bot).isSuccess
    }

    override val timestamp: Timestamp
        get() = sourceEvent.msgTimestamp
    override val messageContent: KaiheilaReceiveMessageContent = sourceEvent.toContent()


}


internal class KaiheilaBotSelfPersonMessageEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: MessageEvent<MessageEventExtra>
) : KaiheilaBotSelfMessageEvent.Person<MessageEventExtra>() {
    override val id: ID get() = sourceEvent.msgId

    @OptIn(ExperimentalSimbotApi::class)
    private val userChatView = lazyValue {
        val view = UserChatCreateRequest(sourceEvent.authorId).requestDataBy(bot)
        KaiheilaUserChatImpl(bot, view)
    }

    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun user(): KaiheilaUserChat = userChatView()
    override val messageContent: KaiheilaReceiveMessageContent = sourceEvent.toContent()
    override val timestamp: Timestamp
        get() = sourceEvent.msgTimestamp
}
