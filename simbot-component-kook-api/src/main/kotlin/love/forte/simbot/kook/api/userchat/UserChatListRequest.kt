/*
 * Copyright (c) 2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.api.userchat

import kotlinx.serialization.DeserializationStrategy
import love.forte.simbot.kook.api.KookApiResult
import love.forte.simbot.kook.api.KookGetRequest
import love.forte.simbot.kook.util.unmodifiableListOf


/**
 * [获取私信聊天会话列表](https://developer.kaiheila.cn/doc/http/user-chat#%E8%8E%B7%E5%8F%96%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E4%BC%9A%E8%AF%9D%E5%88%97%E8%A1%A8)
 *
 * @author ForteScarlet
 */
public object UserChatListRequest : KookGetRequest<KookApiResult.ListData<UserChatView>>() {
    override val resultDeserializer: DeserializationStrategy<out KookApiResult.ListData<UserChatView>>
        get() = KookApiResult.ListData.serializer(
            UserChatViewImpl.serializer()
        )
    override val apiPaths: List<String> = unmodifiableListOf("user-chat", "list")
}
