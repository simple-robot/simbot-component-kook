/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.component.kook.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.component.kook.message.KookAttachmentMessage.Key.asMessage
import love.forte.simbot.definition.ResourceContainer
import love.forte.simbot.kook.objects.Attachments
import love.forte.simbot.message.Image
import love.forte.simbot.message.Message
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.resources.Resource
import love.forte.simbot.resources.Resource.Companion.toResource
import java.net.URL


/**
 *
 * 将 [Attachments] 作为消息对象。
 *
 * 通常在接收时使用.
 *
 * 使用 [Attachments.asMessage] 得到实例。
 *
 * @author ForteScarlet
 */
@SerialName("kook.attachment")
@Serializable
public sealed class KookAttachmentMessage<M : KookAttachmentMessage<M>> :
    KookMessageElement<M>, ResourceContainer {
    
    /**
     * 多媒体数据信息。
     */
    public abstract val attachment: Attachments
    
    override suspend fun resource(): Resource {
        return URL(attachment.url).toResource(attachment.name)
    }
    
    public companion object Key : Message.Key<KookAttachmentMessage<*>> {
        override fun safeCast(value: Any): KookAttachmentMessage<*>? = doSafeCast(value)
        
        
        /**
         * 将 [Attachments] 转化为 [KookAttachmentMessage]。
         *
         * - 如果 [Attachments.type] == `image`, 则会转化为 [KookAttachmentImage]
         * - 如果 [Attachments.type] == `file`, 则会转化为 [KookAttachmentFile]
         * - 如果 [Attachments.type] == `video`, 则会转化为 [KookAttachmentVideo]
         *
         * 其他情况则会直接使用 [SimpleKookAttachmentMessage]。
         *
         */
        @OptIn(ExperimentalSimbotApi::class)
        @JvmStatic
        @JvmName("of")
        public fun Attachments.asMessage(): KookAttachmentMessage<*> = when (type.lowercase()) {
            "image" -> KookAttachmentImage(this)
            "file" -> KookAttachmentFile(this)
            "video" -> KookAttachmentVideo(this)
            else -> SimpleKookAttachmentMessage(this)
        }
    }
}

@Serializable
private data class MessageAttachments(
    override val type: String,
    override val url: String,
    override val name: String,
    override val size: Long
) : Attachments

private fun Attachments.toMessageAttachment(): MessageAttachments = if (this is MessageAttachments) this else MessageAttachments(type, url, name, size)

/**
 * 普通的 [KookAttachmentMessage] 实现。
 */
@Serializable
@SerialName("kook.attachment.simple")
public class SimpleKookAttachmentMessage private constructor(@SerialName("attachment") private val _attachment: MessageAttachments) :
    KookAttachmentMessage<SimpleKookAttachmentMessage>() {
    internal constructor(attachments: Attachments): this(attachments.toMessageAttachment())
    
    override val attachment: Attachments
        get() = _attachment
    
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is SimpleKookAttachmentMessage) return false
        return attachment == other.attachment
    }
    
    override fun hashCode(): Int = attachment.hashCode()
    
    override fun toString(): String = "SimpleKookAttachmentMessage(attachment=$attachment)"
    
    override val key: Message.Key<SimpleKookAttachmentMessage>
        get() = Key
    
    public companion object Key : Message.Key<SimpleKookAttachmentMessage> {
        override fun safeCast(value: Any): SimpleKookAttachmentMessage? = doSafeCast(value)
    }
}

/**
 * 一个可以代表 [Image] 的 [KookAttachmentMessage]。
 */
@Serializable
@SerialName("kook.attachment.image")
@ExperimentalSimbotApi
public class KookAttachmentImage private constructor(@SerialName("attachment") private val _attachment: MessageAttachments) :
    KookAttachmentMessage<KookAttachmentImage>(), Image<KookAttachmentImage> {
    internal constructor(attachments: Attachments): this(attachments.toMessageAttachment())
    
    override val attachment: Attachments
        get() = _attachment
    
    override val id: ID = attachment.url.ID
    
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is KookAttachmentImage) return false
        return attachment == other.attachment
    }
    
    override fun toString(): String = "KookAttachmentImage(attachment=$attachment)"
    
    override fun hashCode(): Int = attachment.hashCode()
    
    override val key: Message.Key<KookAttachmentImage>
        get() = Key
    
    public companion object Key : Message.Key<KookAttachmentImage> {
        override fun safeCast(value: Any): KookAttachmentImage? = doSafeCast(value)
    }
}

/**
 * 文件附件类型的 [KookAttachmentMessage] 实现。
 */
@Serializable
@SerialName("kook.attachment.file")
@ExperimentalSimbotApi
public class KookAttachmentFile private constructor(@SerialName("attachment") private val _attachment: MessageAttachments) :
    KookAttachmentMessage<KookAttachmentFile>() {
    internal constructor(attachments: Attachments): this(attachments.toMessageAttachment())
    
    override val attachment: Attachments
        get() = _attachment
    
    override val key: Message.Key<KookAttachmentFile>
        get() = Key
    
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is KookAttachmentFile) return false
        return attachment == other.attachment
    }
    
    override fun toString(): String = "KookAttachmentFile(attachment=$attachment)"
    
    override fun hashCode(): Int = attachment.hashCode()
    
    public companion object Key : Message.Key<KookAttachmentFile> {
        override fun safeCast(value: Any): KookAttachmentFile? = doSafeCast(value)
    }
}

/**
 * 代表为视频类型的 [KookAttachmentMessage] 类型实现。
 */
@Serializable
@SerialName("kook.attachment.video")
@ExperimentalSimbotApi
public class KookAttachmentVideo private constructor(@SerialName("attachment") private val _attachment: MessageAttachments) :
    KookAttachmentMessage<KookAttachmentVideo>() {
    internal constructor(attachments: Attachments): this(attachments.toMessageAttachment())
    
    override val attachment: Attachments
        get() = _attachment
    
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is KookAttachmentVideo) return false
        return attachment == other.attachment
    }
    
    override fun toString(): String = "KookAttachmentVideo(attachment=$attachment)"
    
    override fun hashCode(): Int = attachment.hashCode()
    
    override val key: Message.Key<KookAttachmentVideo>
        get() = Key
    
    public companion object Key : Message.Key<KookAttachmentVideo> {
        override fun safeCast(value: Any): KookAttachmentVideo? = doSafeCast(value)
    }
}
