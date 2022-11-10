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
        public fun Attachments.asMessage(): KookAttachmentMessage<*> = when (type.lowercase()) {
            "image" -> KookAttachmentImage(this)
            "file" -> KookAttachmentFile(this)
            "video" -> KookAttachmentVideo(this)
            else -> SimpleKookAttachmentMessage(this)
        }
    }
}

/**
 * 普通的 [KookAttachmentMessage] 实现。
 */
@Serializable
@SerialName("kook.attachment.simple")
public class SimpleKookAttachmentMessage internal constructor(override val attachment: Attachments) :
    KookAttachmentMessage<SimpleKookAttachmentMessage>() {
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
public class KookAttachmentImage internal constructor(override val attachment: Attachments) :
    KookAttachmentMessage<KookAttachmentImage>(), Image<KookAttachmentImage> {
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
public class KookAttachmentFile internal constructor(override val attachment: Attachments) :
    KookAttachmentMessage<KookAttachmentFile>() {
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
public class KookAttachmentVideo internal constructor(override val attachment: Attachments) :
    KookAttachmentMessage<KookAttachmentVideo>() {
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