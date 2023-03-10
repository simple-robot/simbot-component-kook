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

package love.forte.simbot.kook.api.userchat

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.ID
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest


/**
 * [删除私信聊天会话](https://developer.kaiheila.cn/doc/http/user-chat#%E5%88%A0%E9%99%A4%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E4%BC%9A%E8%AF%9D)
 *
 * @param chatCode 删除目标会话的ID
 * @author ForteScarlet
 */
public class UserChatDeleteRequest internal constructor(private val chatCode: ID) : KookPostRequest<Unit>() {
    public companion object Key : BaseKookApiRequestKey("user-chat", "delete") {
    
        /**
         * 构造 [UserChatDeleteRequest].
         *
         * @param chatCode 目标会话id
         *
         */
        @JvmStatic
        public fun create(chatCode: ID): UserChatDeleteRequest = UserChatDeleteRequest(chatCode)
    }

    override val resultDeserializer: DeserializationStrategy<out Unit> get() = Unit.serializer()
    override val apiPaths: List<String> get() = apiPathList
    override fun createBody(): Any = Body(chatCode)

    @Serializable
    private data class Body(@SerialName("chat_code") @Serializable(ID.AsCharSequenceIDSerializer::class) val chatCode: ID)
}
