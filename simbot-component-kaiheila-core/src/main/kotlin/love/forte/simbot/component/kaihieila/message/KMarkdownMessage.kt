package love.forte.simbot.component.kaihieila.message

import kotlinx.serialization.*
import love.forte.simbot.kaiheila.objects.*
import love.forte.simbot.message.*


/**
 * 将 [KMarkdown] 作为消息使用。
 * @author ForteScarlet
 */
@SerialName("khl.kmd")
@Serializable
public data class KMarkdownMessage(public val kMarkdown: KMarkdown) : Message.Element<KMarkdownMessage> {
    override val key: Message.Key<KMarkdownMessage>
        get() = Key

    public companion object Key : Message.Key<KMarkdownMessage> {
        override fun safeCast(value: Any): KMarkdownMessage? = doSafeCast(value)
    }
}

/**
 * 将 [KMarkdown] 作为 [KMarkdownMessage] 使用。
 */
public fun KMarkdown.asMessage(): KMarkdownMessage = KMarkdownMessage(this)