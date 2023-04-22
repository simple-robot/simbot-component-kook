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
import love.forte.simbot.ID
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest


/**
 * [创建私信聊天会话](https://developer.kaiheila.cn/doc/http/user-chat#%E5%88%9B%E5%BB%BA%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E4%BC%9A%E8%AF%9D)
 *
 * @author ForteScarlet
 */
public class UserChatCreateRequest internal constructor(private val targetId: ID) : KookPostRequest<UserChatView>() {
    public companion object Key : BaseKookApiRequestKey("user-chat", "create") {
    
        /**
         * 构造 [UserChatCreateRequest].
         *
         * @param targetId 目标id
         */
        @JvmStatic
        public fun create(targetId: ID): UserChatCreateRequest = UserChatCreateRequest(targetId)
    }

    override val resultDeserializer: DeserializationStrategy<UserChatView> get() = UserChatViewImpl.serializer()
    override val apiPaths: List<String> get() = apiPathList
    override fun createBody(): Any = Body(targetId)

    @Serializable
    private data class Body(@SerialName("target_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val targetId: ID)
}
