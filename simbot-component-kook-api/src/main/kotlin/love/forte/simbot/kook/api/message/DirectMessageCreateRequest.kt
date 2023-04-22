/*
 * Copyright (c) 2022-2023. ForteScarlet.
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

package love.forte.simbot.kook.api.message

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.ID
import love.forte.simbot.Simbot
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest


/**
 * [发送私信聊天消息](https://developer.kaiheila.cn/doc/http/direct-message#%E5%8F%91%E9%80%81%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF)
 *
 * @author ForteScarlet
 */
public class DirectMessageCreateRequest internal constructor(
    /**
     * 消息类型, 不传默认为 1, 代表文本类型。2 图片消息，3 视频消息，4 文件消息，9 代表 kmarkdown 消息, 10 代表卡片消息。
     * 默认为 [MessageType.TEXT]
     * @see MessageType
     */
    private val type: Int, // = MessageType.TEXT.type,

    /**
     * 目标用户 id，后端会自动创建会话。有此参数之后可不传 chat_code参数
     */
    private val targetId: ID?,

    /**
     * 目标会话 Code。chat_code 与 target_id 必须传一个
     */
    private val chatCode: ID?,

    /**
     * 	消息内容
     */
    private val content: String,

    /**
     * 回复某条消息的 msgId
     */
    private val quote: ID? = null,

    /**
     * nonce, 服务端不做处理, 原样返回
     */
    private val nonce: String? = null,
) : KookPostRequest<MessageCreated>() {
    init {
        Simbot.require(targetId != null || chatCode != null) {
            "At least one of target Id, chat Code, and quote must exist"
        }
    }

    public companion object Key : BaseKookApiRequestKey("direct-message", "create") {

        /**
         * 通过 [chatCode] 构建一个 [DirectMessageCreateRequest] api实例。
         *
         */
        @JvmStatic
        @JvmOverloads
        public fun byChatCode(
            chatCode: ID,
            content: String,
            type: MessageType = MessageType.TEXT,
            quote: ID? = null,
            nonce: String? = null,
        ): DirectMessageCreateRequest = byChatCode(chatCode, content, type.type, quote, nonce)

        /**
         * 通过 [targetId] 构建一个 [DirectMessageCreateRequest] api实例。
         *
         */
        @JvmStatic
        @JvmOverloads
        public fun byTargetId(
            targetId: ID,
            content: String,
            type: MessageType = MessageType.TEXT,
            quote: ID? = null,
            nonce: String? = null,
        ): DirectMessageCreateRequest = byTargetId(targetId, content, type.type, quote, nonce)

        /**
         * 通过 [chatCode] 构建一个 [DirectMessageCreateRequest] api实例。
         *
         */
        @JvmStatic
        @JvmOverloads
        public fun byChatCode(
            chatCode: ID,
            content: String,
            type: Int,
            quote: ID? = null,
            nonce: String? = null,
        ): DirectMessageCreateRequest = DirectMessageCreateRequest(
            type = type, chatCode = chatCode, targetId = null, content = content, quote = quote, nonce = nonce
        )

        /**
         * 通过 [targetId] 构建一个 [DirectMessageCreateRequest] api实例。
         *
         */
        @JvmStatic
        @JvmOverloads
        public fun byTargetId(
            targetId: ID,
            content: String,
            type: Int,
            quote: ID? = null,
            nonce: String? = null,
        ): DirectMessageCreateRequest = DirectMessageCreateRequest(
            type = type, chatCode = null, targetId = targetId, content = content, quote = quote, nonce = nonce
        )
    }

    override val resultDeserializer: DeserializationStrategy<MessageCreated> get() = MessageCreated.serializer()
    override val apiPaths: List<String> get() = apiPathList
    override fun createBody(): Any =
        Body(type, targetId, chatCode, content, quote, nonce)

    @Serializable
    private data class Body(
        private val type: Int,
        @SerialName("target_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        private val targetId: ID?,
        @SerialName("chat_code")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        private val chatCode: ID?,
        private val content: String,
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        private val quote: ID?,
        private val nonce: String?,
    )
}

/**
 * 通过 [targetId] 构建一个 [DirectMessageCreateRequest] api实例。
 *
 * @see DirectMessageCreateRequest.byTargetId
 */
public fun directMessageCreateRequestByTargetId(
    targetId: ID,
    content: String,
    type: MessageType = MessageType.TEXT,
    quote: ID? = null,
    nonce: String? = null,
): DirectMessageCreateRequest = DirectMessageCreateRequest.byTargetId(
    targetId = targetId, content = content, type = type, quote = quote, nonce = nonce
)

/**
 * 通过 [chatCode] 构建一个 [DirectMessageCreateRequest] api实例。
 *
 * @see DirectMessageCreateRequest.byChatCode
 */
public fun directMessageCreateRequestByChatCode(
    chatCode: ID,
    content: String,
    type: MessageType = MessageType.TEXT,
    quote: ID? = null,
    nonce: String? = null,
): DirectMessageCreateRequest = DirectMessageCreateRequest.byChatCode(
    chatCode = chatCode, content = content, type = type, quote = quote, nonce = nonce
)
