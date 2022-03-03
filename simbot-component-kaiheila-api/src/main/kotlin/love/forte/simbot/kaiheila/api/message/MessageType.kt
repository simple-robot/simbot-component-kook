/*
 *
 *  * Copyright (c) 2021. ForteScarlet All rights reserved.
 *  * Project  simple-robot
 *  * File     MiraiAvatar.kt
 *  *
 *  * You can contact the author through the following channels:
 *  * github https://github.com/ForteScarlet
 *  * gitee  https://gitee.com/ForteScarlet
 *  * email  ForteScarlet@163.com
 *  * QQ     1149159218
 *
 */

@file:Suppress("unused")

package love.forte.simbot.kaiheila.api.message

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import love.forte.simbot.kaiheila.api.message.MessageType.Companion.CARD
import love.forte.simbot.kaiheila.api.message.MessageType.Companion.FILE
import love.forte.simbot.kaiheila.api.message.MessageType.Companion.IMAGE
import love.forte.simbot.kaiheila.api.message.MessageType.Companion.KMARKDOWN
import love.forte.simbot.kaiheila.api.message.MessageType.Companion.TEXT
import love.forte.simbot.kaiheila.api.message.MessageType.Companion.VIDEO


/**
 *
 * 消息的类型。
 * - [消息发送 - type字段](https://developer.kaiheila.cn/doc/http/message#%E5%8F%91%E9%80%81%E9%A2%91%E9%81%93%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF)
 * - [事件主要格式 - 格式说明](https://developer.kaiheila.cn/doc/event/event-introduction)
 *
 * @see TEXT
 * @see IMAGE
 * @see VIDEO
 * @see FILE
 * @see KMARKDOWN
 * @see CARD
 * @author ForteScarlet
 */
@Serializable(MessageTypeSerializer::class)
public sealed class MessageType {
    /**
     * type value.
     */
    public abstract val type: Int

    public companion object {

        /**
         * 文本消息
         */
        @JvmField
        public val TEXT: MessageType = StandardMessageType("TEXT", 1)

        /**
         * 图片消息
         */
        @JvmField
        public val IMAGE: MessageType = StandardMessageType("IMAGE", 2)

        /**
         * 视频消息
         */
        @JvmField
        public val VIDEO: MessageType = StandardMessageType("VIDEO", 3)

        /**
         * 文件消息
         */
        @JvmField
        public val FILE: MessageType = StandardMessageType("FILE", 4)

        /**
         * K Markdown 消息
         */
        @JvmField
        public val KMARKDOWN: MessageType = StandardMessageType("KMARKDOWN", 9)

        /**
         * Card Message 消息
         */
        @JvmField
        public val CARD: MessageType = StandardMessageType("CARD", 10)

        /**
         * 系统消息
         */
        @JvmField
        public val SYSTEM: MessageType = StandardMessageType("SYSTEM", 255)

        /**
         * 标准消息类型的汇总列表。
         *
         * @see TEXT
         * @see IMAGE
         * @see VIDEO
         * @see FILE
         * @see KMARKDOWN
         * @see CARD
         */
        public val standards: List<MessageType> = listOf(
            TEXT, IMAGE, VIDEO, FILE, KMARKDOWN, CARD, SYSTEM
        )


    }

    internal class StandardMessageType internal constructor(private val standardName: String, override val type: Int) :
        MessageType() {
        override fun toString(): String = "StandardMessageType(name=$standardName, type=$type)"
    }

    internal class MixedMessageType(override val type: Int) : MessageType()


    override fun toString(): String = "MessageType(type=$type)"
}


/**
 * [MessageType] 的序列化器, 使用 [MessageType.type] 作为字面量数字进行序列化。
 */
public object MessageTypeSerializer : KSerializer<MessageType> {
    override fun deserialize(decoder: Decoder): MessageType {
        val typeValue = decoder.decodeInt()
        return MessageType.standards.find { it.type == typeValue } ?: MessageType.MixedMessageType(typeValue)
    }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("messageType", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: MessageType) {
        encoder.encodeInt(value.type)
    }

}