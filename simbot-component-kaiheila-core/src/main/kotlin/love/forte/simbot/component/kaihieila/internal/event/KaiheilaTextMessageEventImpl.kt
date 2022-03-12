package love.forte.simbot.component.kaihieila.internal.event

import love.forte.simbot.*
import love.forte.simbot.component.kaihieila.*
import love.forte.simbot.component.kaihieila.event.*
import love.forte.simbot.component.kaihieila.internal.*
import love.forte.simbot.definition.*
import love.forte.simbot.event.*
import love.forte.simbot.kaiheila.event.message.*
import love.forte.simbot.kaiheila.event.message.MessageEvent
import love.forte.simbot.message.*


internal class KaiheilaGroupTextMessageEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: MessageEvent<TextEventExtra>
) : KaiheilaMessageEvent.Group<TextEventExtra>() {
    override val id: ID
        get() = TODO("Not yet implemented")

    override val author: KaiheilaGuildMember
        get() = TODO("Not yet implemented")
    override val channel: KaiheilaChannel
        get() = TODO("Not yet implemented")

    override suspend fun delete(): Boolean {
        TODO("Not yet implemented")
    }

    override val timestamp: Timestamp
        get() = TODO("Not yet implemented")
    override val visibleScope: Event.VisibleScope
        get() = TODO("Not yet implemented")
    override val messageContent: ReceivedMessageContent
        get() = TODO("Not yet implemented")


}


internal class KaiheilaPersonTextMessageEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: MessageEvent<TextEventExtra>
) : KaiheilaMessageEvent.Person<TextEventExtra>() {
    override val id: ID
        get() = TODO("Not yet implemented")

    override suspend fun user(): Contact {
        TODO("Not yet implemented")
    }

    override val messageContent: ReceivedMessageContent
        get() = TODO("Not yet implemented")

    override val timestamp: Timestamp
        get() = TODO("Not yet implemented")
}
