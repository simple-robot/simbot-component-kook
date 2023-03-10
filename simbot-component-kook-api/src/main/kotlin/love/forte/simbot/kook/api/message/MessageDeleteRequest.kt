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
 * [删除频道聊天消息](https://developer.kaiheila.cn/doc/http/message#%E5%88%A0%E9%99%A4%E9%A2%91%E9%81%93%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF)
 *
 * @param msgId 消息 id
 */
public class MessageDeleteRequest internal constructor(private val msgId: ID) : KookPostRequest<Unit>() {
    public companion object Key : BaseKookApiRequestKey("message", "delete") {
    
        /**
         * 构建 [MessageDeleteRequest]
         * @param msgId 消息ID
         */
        @JvmStatic
        public fun create(msgId: ID): MessageDeleteRequest = MessageDeleteRequest(msgId)
    }

    override val resultDeserializer: DeserializationStrategy<out Unit> get() = Unit.serializer()

    override val apiPaths: List<String> get() = apiPathList
    override fun createBody(): Any = Body(msgId)

    @Serializable
    private data class Body(
        @SerialName("msg_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val msgId: ID
    )
}
