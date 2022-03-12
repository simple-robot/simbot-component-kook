package love.forte.simbot.component.kaihieila.internal.event

import love.forte.simbot.*
import love.forte.simbot.component.kaihieila.event.*
import love.forte.simbot.component.kaihieila.internal.*
import love.forte.simbot.component.kaihieila.message.*
import love.forte.simbot.component.kaihieila.util.*
import love.forte.simbot.definition.*
import love.forte.simbot.kaiheila.api.message.*
import love.forte.simbot.kaiheila.event.message.*


internal class KaiheilaGroupMessageEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: MessageEvent<MessageEventExtra>,
    override val author: KaiheilaMemberImpl,
    override val channel: KaiheilaChannelImpl
) : KaiheilaMessageEvent.Group<MessageEventExtra>() {
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


internal class KaiheilaPersonMessageEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: MessageEvent<MessageEventExtra>
) : KaiheilaMessageEvent.Person<MessageEventExtra>() {
    override val id: ID get() = sourceEvent.msgId

    override val user: Contact
        get() = TODO("Not yet implemented")


    override val messageContent: KaiheilaReceiveMessageContent = sourceEvent.toContent()

    override val timestamp: Timestamp
        get() = sourceEvent.msgTimestamp
}
