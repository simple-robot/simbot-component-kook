package love.forte.simbot.kook.objects.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.kook.objects.Attachments

@Serializable
@SerialName(AttachmentsImpl.SERIAL_NAME)
internal data class AttachmentsImpl(
    override val type: String,
    override val url: String,
    override val name: String,
    override val size: Long = -1
) : Attachments {
    internal companion object {
        const val SERIAL_NAME = "ATTACHMENTS_I"
    }
}