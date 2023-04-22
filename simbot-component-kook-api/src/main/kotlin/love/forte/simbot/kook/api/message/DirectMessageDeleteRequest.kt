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
 * [删除私信聊天消息](https://developer.kaiheila.cn/doc/http/direct-message#%E5%88%A0%E9%99%A4%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF)
 *
 * @author ForteScarlet
 */
public class DirectMessageDeleteRequest internal constructor(private val msgId: ID) : KookPostRequest<Unit>() {
    public companion object Key : BaseKookApiRequestKey("direct-message", "delete") {
    
        /**
         * 构造 [DirectMessageDeleteRequest].
         * @param msgId 消息ID
         */
        @JvmStatic
        public fun create(msgId: ID): DirectMessageDeleteRequest =
            DirectMessageDeleteRequest(msgId)
    }

    override val resultDeserializer: DeserializationStrategy<Unit> get() = Unit.serializer()
    override val apiPaths: List<String> get() = apiPathList

    override fun createBody(): Any = Body(msgId)

    @Serializable
    private data class Body(@SerialName("msg_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val msgId: ID)

}
