/*
 * Copyright (c) 2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook,
 * If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.event

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.kook.objects.SimpleAttachments
import love.forte.simbot.kook.objects.SimpleUser
import love.forte.simbot.kook.objects.kmd.RawValueKMarkdown
import kotlin.jvm.JvmStatic


/**
 * [事件 Event](https://developer.kookapp.cn/doc/event/event-introduction)
 *
 * > 当 `websocket` 或 `webhook` 收到 `s=0` 的消息时，
 * 代表当前收到的消息是事件(包含用户的聊天消息及系统的通知消息等)。
 *
 */
@Serializable
public data class Event<E : EventExtra>(

    /**
     * 消息通道类型, `GROUP` 为组播消息, `PERSON` 为单播消息, `BROADCAST` 为广播消息
     */
    @SerialName("channel_type")
    public val channelType: String,

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
     * - 其它的暂未开放
     */
    @SerialName("type")
    public val typeValue: Int,

    /**
     * 发送目的, 频道消息类时, 代表的是频道 `channel_id`，如果 `channel_type` 为 `GROUP` 组播且 `type` 为 `255` 系统消息时，则代表服务器 `guild_id`
     */
    @SerialName("target_id")
    public val targetId: String,

    /**
     * 发送者 id, `1` 代表系统
     */
    @SerialName("author_id")
    public val authorId: String,

    /**
     * 消息内容, 文件，图片，视频时，content 为 url
     */
    public val content: String,

    /**
     * 消息的 id
     */
    @SerialName("msg_id")
    public val msgId: String,

    /**
     * 消息发送时间的毫秒时间戳
     */
    @SerialName("msg_timestamp")
    public val msgTimestamp: Long,

    /**
     * 随机串，与用户消息发送 api 中传的 nonce 保持一致
     */
    public val nonce: String,

    /**
     * 不同的消息类型，结构不一致
     */
    public val extra: E
) {

    /**
     * 事件的类型。
     *
     * 基于 [typeValue] 的值映射的 [Type] 元素。
     * 当类型值未知时得到 `null` 。
     *
     */
    public val type: Type?
        get() = when (typeValue) {
            Type.TEXT.value -> Type.TEXT
            Type.IMAGE.value -> Type.IMAGE
            Type.VIDEO.value -> Type.VIDEO
            Type.FILE.value -> Type.FILE
            Type.AUDIO.value -> Type.AUDIO
            Type.KMARKDOWN.value -> Type.KMARKDOWN
            Type.CARD.value -> Type.CARD
            Type.SYSTEM.value -> Type.SYSTEM
            else -> null
        }

    /**
     * 事件的类型枚举。
     */
    public enum class Type(public val value: Int) {
        /**
         * 文字消息
         */
        TEXT(1),

        /**
         * 图片消息
         */
        IMAGE(2),

        /**
         * 视频消息
         */
        VIDEO(3),

        /**
         * 文件消息
         */
        FILE(4),

        /**
         * 音频消息
         */
        AUDIO(8),

        /**
         * KMarkdown
         */
        KMARKDOWN(9),

        /**
         * card消息
         */
        CARD(10),

        /**
         * 系统消息
         */
        SYSTEM(255),
        ;

    }

}

/**
 * 事件的消息 `extra`。
 *
 * @see Event.extra
 * @see TextExtra
 * @see SystemExtra
 *
 */
@Serializable
public sealed class EventExtra {
    public companion object {

        /**
         * 通过 [json] 中的 `type` 和 `extra.type` 来获取对应的 [DeserializationStrategy] 。
         * 当无法获取时得到 `null`。
         *
         * @param json 一个事件 JSON 中的 `d` 属性值。
         *
         * @throws IllegalArgumentException 当 [json] 中某个节点的属性格式不符合预期 （例如 [json] 并非 [JsonObject] 类型）
         *
         */
        @JvmStatic
        public fun serializerOrNull(json: JsonElement): KSerializer<out EventExtra>? {
            val type = json.jsonObject["type"]?.jsonPrimitive?.intOrNull ?: return null
            return serializerOrNull(type)
        }

        /**
         * 通过 [type] 来获取对应的 [DeserializationStrategy] 。
         * 当无法获取时得到 `null`。
         */
        @JvmStatic
        public fun serializerOrNull(type: Int): KSerializer<out EventExtra>? {
            return when (type) {
                Event.Type.TEXT.value -> TextEventExtra.serializer()
                Event.Type.IMAGE.value -> ImageEventExtra.serializer()
                Event.Type.VIDEO.value -> VideoEventExtra.serializer()
                // 文件消息已转为卡片消息，详情请直接参考卡片消息
//                Event.Type.FILE.value -> FileEventExtra.serializer()
                // Unknown?
//                Event.Type.AUDIO.value -> AudioEventExtra.serializer()
                Event.Type.KMARKDOWN.value -> KMarkdownEventExtra.serializer()
                Event.Type.CARD.value -> CardEventExtra.serializer()
                Event.Type.SYSTEM.value -> SystemExtra.serializer()
                else -> null
            }
        }

    }
}

/**
 * 文字频道消息 `extra`
 *
 * > 当 [`type`][Event.typeValue] 非系统消息(255)时
 *
 * 当此事件的频道类型 [Event.channelType] 为 `PERSON` 时，例如 [guildId] 等频道才有的属性可能会使用空内容填充。
 *
 *
 * @see TextEventExtra
 * @see ImageEventExtra
 * @see VideoEventExtra
 * @see KMarkdownEventExtra
 * @see CardEventExtra
 *
 */
@Serializable
@SerialName("text")
public sealed class TextExtra : EventExtra() {
    /**
     * 类型。同 [Event.type][Event.typeValue]
     */
    public abstract val type: Int

    /**
     * 服务器 id
     */
    public abstract val guildId: String?

    /**
     * 频道名
     */
    public abstract val channelName: String?

    /**
     * 提及到的用户 id 的列表
     */
    public abstract val mention: List<String>

    /**
     * 是否 mention 所有用户
     */
    public abstract val isMentionAll: Boolean

    /**
     * mention 用户角色的数组
     */
    public abstract val mentionRoles: List<String>

    /**
     * 是否 mention 在线用户
     */
    public abstract val isMentionHere: Boolean

    /**
     * 用户信息
     */
    public abstract val author: SimpleUser
}

/**
 * [文字消息事件 extra](https://developer.kookapp.cn/doc/event/message#%E6%96%87%E5%AD%97%E6%B6%88%E6%81%AF)
 *
 * @author ForteScarlet
 */
@Serializable
public data class TextEventExtra(
    override val type: Int,
    @SerialName("guild_id")
    override val guildId: String? = null,
    @SerialName("channel_name")
    override val channelName: String? = null,
    override val mention: List<String> = emptyList(),
    @SerialName("mention_all")
    override val isMentionAll: Boolean = false,
    @SerialName("mention_roles")
    override val mentionRoles: List<String> = emptyList(),
    @SerialName("mention_here")
    override val isMentionHere: Boolean = false,
    override val author: SimpleUser
) : TextExtra()

/**
 * [图片消息事件 extra](https://developer.kookapp.cn/doc/event/message#%E5%9B%BE%E7%89%87%E6%B6%88%E6%81%AF)
 *
 * @author ForteScarlet
 */
@Serializable
public data class ImageEventExtra(
    override val type: Int,
    @SerialName("guild_id")
    override val guildId: String? = null,
    @SerialName("channel_name")
    override val channelName: String? = null,
    override val mention: List<String> = emptyList(),
    @SerialName("mention_all")
    override val isMentionAll: Boolean = false,
    @SerialName("mention_roles")
    override val mentionRoles: List<String> = emptyList(),
    @SerialName("mention_here")
    override val isMentionHere: Boolean = false,
    override val author: SimpleUser,

    /**
     * 附件
     */
    public val attachments: SimpleAttachments
) : TextExtra()

/**
 * [视频消息事件 extra](https://developer.kookapp.cn/doc/event/message#%E8%A7%86%E9%A2%91%E6%B6%88%E6%81%AF)
 *
 * @author ForteScarlet
 */
@Serializable
public data class VideoEventExtra(
    override val type: Int,
    @SerialName("guild_id")
    override val guildId: String? = null,
    @SerialName("channel_name")
    override val channelName: String? = null,
    override val mention: List<String> = emptyList(),
    @SerialName("mention_all")
    override val isMentionAll: Boolean = false,
    @SerialName("mention_roles")
    override val mentionRoles: List<String> = emptyList(),
    @SerialName("mention_here")
    override val isMentionHere: Boolean = false,
    override val author: SimpleUser,

    /**
     * 附件
     */
    public val attachments: SimpleAttachments
) : TextExtra()

/**
 * [KMarkdown 消息事件 extra](https://developer.kookapp.cn/doc/event/message#KMarkdown%20%E6%B6%88%E6%81%AF)
 *
 * @author ForteScarlet
 */
@OptIn(ExperimentalSimbotApi::class)
@Serializable
public data class KMarkdownEventExtra(
    override val type: Int,
    @SerialName("guild_id")
    override val guildId: String? = null,
    @SerialName("channel_name")
    override val channelName: String? = null,
    override val mention: List<String> = emptyList(),
    @SerialName("mention_all")
    override val isMentionAll: Boolean = false,
    @SerialName("mention_roles")
    override val mentionRoles: List<String> = emptyList(),
    @SerialName("mention_here")
    override val isMentionHere: Boolean = false,
    override val author: SimpleUser,

    /**
     * `nav_channels`
     */
    @ExperimentalSimbotApi
    public val navChannels: List<String>? = null,

    /**
     * `code`
     */
    @ExperimentalSimbotApi
    public val code: String? = null,

    /**
     * KMarkdown data
     */
    @ExperimentalSimbotApi
    public val kmarkdown: RawValueKMarkdown
) : TextExtra()

/**
 * [Card 消息事件 extra](https://developer.kookapp.cn/doc/event/message#Card%20%E6%B6%88%E6%81%AF)
 *
 * @author ForteScarlet
 */
@Serializable
public data class CardEventExtra(
    override val type: Int,
    @SerialName("guild_id")
    override val guildId: String? = null,
    @SerialName("channel_name")
    override val channelName: String? = null,
    override val mention: List<String> = emptyList(),
    @SerialName("mention_all")
    override val isMentionAll: Boolean = false,
    @SerialName("mention_roles")
    override val mentionRoles: List<String> = emptyList(),
    @SerialName("mention_here")
    override val isMentionHere: Boolean = false,
    override val author: SimpleUser,

    /**
     * `nav_channels`
     */
    @ExperimentalSimbotApi
    public val navChannels: List<String>? = null,

    /**
     * `code`
     */
    @ExperimentalSimbotApi
    public val code: String? = null,
) : TextExtra()


/**
 * 系统事件消息 `extra`
 * > 当 [`type`][Event.typeValue] 为系统消息(255)时
 */
@Serializable
@SerialName("sys")
public sealed class SystemExtra : EventExtra() {
//    /**
//     * 标识该事件的类型
//     */
//    public  val type: String,

    /**
     * 该事件关联的具体数据
     */
    @Contextual
    public abstract val body: Any?
}
