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
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.ID
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest

/**
 *
 * [给某个消息添加回应](https://developer.kaiheila.cn/doc/http/message#%E7%BB%99%E6%9F%90%E4%B8%AA%E6%B6%88%E6%81%AF%E6%B7%BB%E5%8A%A0%E5%9B%9E%E5%BA%94)
 *
 */
public class MessageAddReactionRequest internal constructor(
    /**
     * 	频道消息的id
     */
    private val msgId: ID,
    /**
     * emoji的id. 可以为 `Guild Emoji` 或者 `Emoji`
     */
    private val emoji: ID,
) : KookPostRequest<Unit>() {
    @Deprecated("Use create(...)", ReplaceWith("MessageAddReactionRequest.create(msgId, emoji)"))
    public constructor(msgId: ID, emoji: love.forte.simbot.message.Emoji) : this(msgId, emoji.id)
    
    @Deprecated("Use create(...)", ReplaceWith("MessageAddReactionRequest.create(msgId, emoji)"))
    public constructor(msgId: ID, emoji: Emoji) : this(msgId, emoji.id)
    
    public companion object Key : BaseKookApiRequestKey("message", "add-reaction") {
        
        /**
         * 构造 [MessageAddReactionRequest]
         * @param msgId 频道消息的id
         * @param emoji emoji的id
         */
        @JvmStatic
        public fun create(msgId: ID, emoji: ID): MessageAddReactionRequest = MessageAddReactionRequest(msgId, emoji)
        
        /**
         * 构造 [MessageAddReactionRequest]
         * @param msgId 频道消息的id
         * @param emoji emoji
         */
        @JvmStatic
        public fun create(msgId: ID, emoji: love.forte.simbot.message.Emoji): MessageAddReactionRequest =
            create(msgId, emoji.id)
        
        /**
         * 构造 [MessageAddReactionRequest]
         * @param msgId 频道消息的id
         * @param emoji emoji
         */
        @JvmStatic
        public fun create(msgId: ID, emoji: Emoji): MessageAddReactionRequest = create(msgId, emoji.id)
    }
    
    override val resultDeserializer: DeserializationStrategy<Unit> get() = Unit.serializer()
    override val apiPaths: List<String> get() = apiPathList
    override fun createBody(): Any = Body(msgId, emoji)
    
    @Serializable
    private data class Body(
        @SerialName("msg_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val msgId: ID,
        @Serializable(ID.AsCharSequenceIDSerializer::class) val emoji: ID
    )
    
    
}
