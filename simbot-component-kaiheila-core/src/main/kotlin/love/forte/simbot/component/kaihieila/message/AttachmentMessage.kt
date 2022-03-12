package love.forte.simbot.component.kaihieila.message

import kotlinx.serialization.*
import love.forte.simbot.kaiheila.objects.*
import love.forte.simbot.message.*


/**
 *
 * 将 [Attachments] 作为消息对象。
 *
 * 通常在接收时使用。
 *
 * @author ForteScarlet
 */
@SerialName("khl.attachment")
@Serializable
public data class AttachmentMessage(public val attachment: Attachments) : Message.Element<AttachmentMessage> {

    override val key: Message.Key<AttachmentMessage>
        get() = Key

    public companion object Key : Message.Key<AttachmentMessage> {
        override fun safeCast(value: Any): AttachmentMessage? = doSafeCast(value)
    }
}

/**
 * 将 [Attachments] 转化为 [AttachmentMessage]。
 */
public fun Attachments.asMessage(): AttachmentMessage = AttachmentMessage(this)