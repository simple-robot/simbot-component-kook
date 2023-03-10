/*
 * Copyright (c) 2021-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

@file:JvmName("Events")
@file:Suppress("unused")

package love.forte.simbot.kook.event

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.kook.objects.Channel
import love.forte.simbot.kook.objects.User
import java.util.*


/**
 *
 * Kook Event - [事件Event](https://developer.kaiheila.cn/doc/event)
 *
 *
 * 当 websocket 或 webhook 收到 s=0 的消息时，代表当前收到的消息是事件(包含用户的聊天消息及系统的通知消息等)。
 *
 * @author ForteScarlet
 */
public interface Event<out Extra : Event.Extra> {

    /**
     * 消息频道类型.
     */
    public val channelType: Channel.Type

    /**
     * 事件的类型。
     *
     * - 1:文字消息
     * - 2:图片消息
     * - 3:视频消息
     * - 4:文件消息
     * - 8:音频消息
     * - 9:KMarkdown
     * - 10:card消息
     * - 255:系统消息
     *
     * 其它的暂未开放
     * @see Type
     */
    public val type: Type


    /**
     * 事件基本类型。详见 [事件 - 事件主要格式](https://developer.kaiheila.cn/doc/event)
     *
     * - 1:文字消息,
     * - 2:图片消息，
     * - 3:视频消息，
     * - 4:文件消息，
     * - 8:音频消息，
     * - 9:KMarkdown，
     * - 10:card消息，
     * - 255:系统消息,
     * - 其它的暂未开放
     *
     */
    @Serializable(with = EventTypeSerializer::class)
    @Suppress("unused")
    public enum class Type(public val type: Int) {
        UNKNOWN(-999999),

        TEXT(EventTypeConstant.TEXT),
        IMAGE(EventTypeConstant.IMAGE),
        VIDEO(EventTypeConstant.VIDEO),
        FILE(EventTypeConstant.FILE),
        VOICE(EventTypeConstant.VOICE),
        KMD(EventTypeConstant.KMD),
        CARD(EventTypeConstant.CARD),
        SYS(EventTypeConstant.SYS),
        ;

        public companion object {
            @JvmStatic
            public fun byType(type: Int): Type {
                if (type == UNKNOWN.type) {
                    return UNKNOWN
                }
                if (type !in EventTypeConstant) {
                    throw IndexOutOfBoundsException("Type $type")
                }

                val values = values()
                for (i in 1..values.size) {
                    val v = values[i]
                    if (v.type == type) {
                        return v
                    }
                }

                throw NoSuchElementException("Type $type")
            }

            /**
             * Get instance of [Event.Type] by [type], or default value (like null).
             */
            @JvmStatic
            @JvmOverloads
            public fun byTypeOr(type: Int, default: Type? = null): Type? {
                if (type == UNKNOWN.type) {
                    return UNKNOWN
                }
                if (type !in EventTypeConstant) {
                    return default
                }

                val values = values()
                for (i in 1..values.size) {
                    val v = values[i]
                    if (v.type == type) {
                        return v
                    }
                }

                return default
            }
        }

    }


    /**
     * 发送目的 id，如果为是 GROUP 消息，则 target_id 代表频道 id
     */
    public val targetId: ID

    /**
     * 发送者 id, `1` 代表系统
     */
    public val authorId: ID

    /**
     * 消息内容, 文件，图片，视频时，content 为 url
     */
    public val content: String

    /**
     * msgId
     */
    public val msgId: ID


    /**
     * 消息发送时间的**毫秒**时间戳.
     */
    public val msgTimestamp: Timestamp

    /**
     * 随机串，与用户消息发送 api 中传的 nonce 保持一致
     */
    public val nonce: String

    /**
     * 不同的消息类型，结构不一致。
     */
    public val extra: Extra


    /**
     * 事件中的额外消息结构。
     *
     * 分为两种情况：[Event.type] == `255` 的时候与相反的时候。
     *
     *
     * 等于 `255` 的时候即代表为 *系统事件消息*，否则是 *文字频道消息*
     *
     * @see Event.extra
     */
    public sealed interface Extra {
        //
        // /**
        //  * Type.
        //  */
        // public val type: T


        /**
         * 当 [Event.type] == `255` 时的 [结构](https://developer.kaiheila.cn/doc/event/event-introduction#).
         *
         *
         *
         */
        public interface Sys<out B> : Extra {
            /**
             * 标识该事件的类型
             */
            public val type: String

            /**
             * 该事件关联的具体数据, 详见各系统消息事件示例.
             */
            public val body: B
        }

        /**
         * 当 [Event.type] != `255` 时的 [结构](https://developer.kaiheila.cn/doc/event/event-introduction#)
         */
        public interface Text : Extra {
            /**
             * 同上面的type（[Event.type]）
             */
            public val type: Type

            /**
             * 服务器 id
             */
            public val guildId: ID

            /**
             * 频道名
             */
            public val channelName: String

            /**
             * 提及到的用户 id 的列表
             */
            public val mention: List<ID>

            /**
             * 是否 mention 所有用户
             */
            public val isMentionAll: Boolean

            /**
             * mention 用户角色的数组
             */
            public val mentionRoles: List<ID>

            /**
             * 是否 mention 在线用户
             */
            public val isMentionHere: Boolean

            /**
             * 用户信息, 见 [对象-用户User](https://developer.kaiheila.cn/doc/objects#%E7%94%A8%E6%88%B7User) ([User])
             */
            public val author: User
        }

    }

}



/**
 * 类型枚举 [Event.Type] 的类型常量类。
 */
public object EventTypeConstant {
    public const val TEXT: Int = 1
    public const val IMAGE: Int = 2
    public const val VIDEO: Int = 3
    public const val FILE: Int = 4
    public const val VOICE: Int = 8
    public const val KMD: Int = 9
    public const val CARD: Int = 10

    /** sys目前与上述几种类型的关联性/连续性差距较大，暂时用于单独判断。 */
    public const val SYS: Int = 255

    /** all types */
    private val types = BitSet(16).apply {
        set(TEXT)
        set(IMAGE)
        set(VIDEO)
        set(FILE)
        set(VOICE)
        set(KMD)
        set(CARD)
    }

    /** 判断是否存在某个类型。 */
    public operator fun contains(type: Int): Boolean = types[type] || type == SYS
}


/**
 * [Event.Type] 序列化器。
 */
public object EventTypeSerializer : KSerializer<Event.Type> {
    /** descriptor for Int */
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("EventType", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): Event.Type {
        val value = decoder.decodeInt()
        return Event.Type.byType(value)
    }

    override fun serialize(encoder: Encoder, value: Event.Type) {
        encoder.encodeInt(value.type)
    }
}
