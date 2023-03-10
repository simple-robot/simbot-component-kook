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
package love.forte.simbot.kook.api.message

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.ID
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest

/**
 * [删除消息的某个回应](https://developer.kaiheila.cn/doc/http/message#%E5%88%A0%E9%99%A4%E6%B6%88%E6%81%AF%E7%9A%84%E6%9F%90%E4%B8%AA%E5%9B%9E%E5%BA%94)
 *
 */
public class MessageDeleteReactionRequest internal constructor(
    private val msgId: ID,
    private val emoji: ID,
    private val userId: ID,
) : KookPostRequest<Unit>() {
    @Deprecated(
        "Use MessageDeleteReactionRequest.create(...)",
        ReplaceWith("MessageDeleteReactionRequest.create(msgId. emoji, userId)")
    )
    public constructor(msgId: ID, emoji: love.forte.simbot.message.Emoji, userId: ID) : this(msgId, emoji.id, userId)
    
    @Deprecated(
        "Use MessageDeleteReactionRequest.create(...)",
        ReplaceWith("MessageDeleteReactionRequest.create(msgId. emoji, userId)")
    )
    public constructor(msgId: ID, emoji: Emoji, userId: ID) : this(msgId, emoji.id, userId)
    
    public companion object Key : BaseKookApiRequestKey("message", "delete-reaction") {
        
        /**
         * 构造 [MessageDeleteReactionRequest].
         *
         * @param msgId 消息id
         * @param emoji emoji表情id
         * @param userId 用户id
         *
         */
        @JvmStatic
        public fun create(msgId: ID, emoji: ID, userId: ID): MessageDeleteReactionRequest =
            MessageDeleteReactionRequest(msgId, emoji, userId)
        
        /**
         * 构造 [MessageDeleteReactionRequest].
         *
         * @param msgId 消息id
         * @param emoji emoji表情
         * @param userId 用户id
         *
         */
        @JvmStatic
        public fun create(msgId: ID, emoji: love.forte.simbot.message.Emoji, userId: ID): MessageDeleteReactionRequest =
            create(msgId, emoji.id, userId)
        
        /**
         * 构造 [MessageDeleteReactionRequest].
         *
         * @param msgId 消息id
         * @param emoji emoji表情
         * @param userId 用户id
         *
         */
        @JvmStatic
        public fun create(msgId: ID, emoji: Emoji, userId: ID): MessageDeleteReactionRequest =
            create(msgId, emoji.id, userId)
    }
    
    override val resultDeserializer: DeserializationStrategy<out Unit> get() = Unit.serializer()
    override val apiPaths: List<String> get() = apiPathList
    override fun createBody(): Any = Body(msgId, emoji, userId)
    
    @Serializable
    private data class Body(
        @SerialName("msg_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val msgId: ID,
        @Serializable(ID.AsCharSequenceIDSerializer::class) val emoji: ID,
        @SerialName("user_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val userId: ID,
    )
}
