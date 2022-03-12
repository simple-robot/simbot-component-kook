package love.forte.simbot.component.kaihieila.message

import kotlinx.serialization.*
import love.forte.simbot.kaiheila.objects.*
import love.forte.simbot.message.*


/**
 * 将 [Card] 作为消息使用。
 * @author ForteScarlet
 */
@SerialName("khl.card")
@Serializable
public data class CardMessage(public val kMarkdown: Card) : Message.Element<CardMessage> {
    override val key: Message.Key<CardMessage>
        get() = Key

    public companion object Key : Message.Key<CardMessage> {
        override fun safeCast(value: Any): CardMessage? = doSafeCast(value)
    }
}

/**
 * 将 [Card] 作为 [CardMessage] 使用。
 */
public fun Card.asMessage(): CardMessage = CardMessage(this)