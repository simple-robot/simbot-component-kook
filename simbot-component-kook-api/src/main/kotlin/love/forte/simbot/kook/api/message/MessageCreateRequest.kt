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
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest

/**
 * [发送频道聊天消息](https://developer.kaiheila.cn/doc/http/message#%E5%8F%91%E9%80%81%E9%A2%91%E9%81%93%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF)
 *
 * @author ForteScarlet
 */
public class MessageCreateRequest internal constructor(
    /**
     * 消息类型, 见[type], 不传默认为1, 代表文本类型。9代表 kmarkdown 消息, 10代表卡片消息。
     *
     *
     * *Note: 经测试，似乎也支持 [MessageType.IMAGE]、[MessageType.FILE]等格式。*
     *
     * 默认为 [MessageType.TEXT]
     * @see MessageType
     */
    private val type: Int = MessageType.TEXT.type,
    
    /**
     * 	目标频道 id
     */
    @Serializable(ID.AsCharSequenceIDSerializer::class) private val targetId: ID,
    
    /**
     * 	消息内容
     */
    private val content: String,
    
    /**
     * 回复某条消息的 msgId
     */
    @Serializable(ID.AsCharSequenceIDSerializer::class) private val quote: ID? = null,
    
    /**
     * 服务端不做处理, 原样返回
     */
    private val nonce: String? = null,
    
    /**
     * 用户id,如果传了，代表该消息是临时消息，该消息不会存数据库，但是会在频道内只给该用户推送临时消息。用于在频道内针对用户的操作进行单独的回应通知等。
     */
    @Serializable(ID.AsCharSequenceIDSerializer::class) private val tempTargetId: ID? = null,
) : KookPostRequest<MessageCreated>() {
    public companion object Key : BaseKookApiRequestKey("message", "create") {
        
        /**
         * 构造 [MessageCreateRequest]
         * @param type 消息类型, 见[type], 不传默认为1, 代表文本类型。9代表 kmarkdown 消息, 10代表卡片消息。
         * @param targetId 目标频道 id
         * @param content 消息内容
         * @param quote 回复某条消息的 msgId
         * @param nonce 服务端不做处理, 原样返回
         * @param tempTargetId 用户id,如果传了，代表该消息是临时消息，该消息不会存数据库，但是会在频道内只给该用户推送临时消息。
         * 用于在频道内针对用户的操作进行单独的回应通知等。
         *
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            type: Int = MessageType.TEXT.type,
            targetId: ID,
            content: String,
            quote: ID? = null,
            nonce: String? = null,
            tempTargetId: ID? = null,
        ): MessageCreateRequest = MessageCreateRequest(type, targetId, content, quote, nonce, tempTargetId)
        
        
    }
    
    override val resultDeserializer: DeserializationStrategy<MessageCreated> get() = MessageCreated.serializer()
    
    override val apiPaths: List<String> get() = apiPathList
    
    override fun createBody(): Any = Body(type, targetId, content, quote, nonce, tempTargetId)
    
    
    @Serializable
    private data class Body(
        val type: Int,
        @SerialName("target_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val targetId: ID,
        val content: String,
        @Serializable(ID.AsCharSequenceIDSerializer::class) val quote: ID? = null,
        val nonce: String? = null,
        @SerialName("temp_target_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val tempTargetId: ID? = null,
    )
    
    /**
     * 将此api转化为 [DirectMessageCreateRequest].
     */
    @JvmOverloads
    public fun toDirect(targetId: ID = this.targetId): DirectMessageCreateRequest {
        return DirectMessageCreateRequest.byTargetId(targetId, content, type, quote, nonce)
    }
    
    /**
     * 将此api转化为 [DirectMessageCreateRequest].
     */
    public fun toDirectByChatCode(chatCode: ID): DirectMessageCreateRequest {
        return DirectMessageCreateRequest.byChatCode(chatCode, content, type, quote, nonce)
    }
    
}
