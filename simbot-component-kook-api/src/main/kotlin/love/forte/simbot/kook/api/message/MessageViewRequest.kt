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

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import love.forte.simbot.ID
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookGetRequest

/**
 * [获取频道聊天消息详情](https://developer.kaiheila.cn/doc/http/message#%E8%8E%B7%E5%8F%96%E9%A2%91%E9%81%93%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF%E8%AF%A6%E6%83%85)
 * @author ForteScarlet
 */
public class MessageViewRequest internal constructor(private val msgId: ID) : KookGetRequest<ChannelMessageDetails>() {
    public companion object Key : BaseKookApiRequestKey("message", "view") {
    
        /**
         * 构造 [MessageViewRequest]
         * @param msgId 消息ID
         */
        @JvmStatic
        public fun create(msgId: ID): MessageViewRequest = MessageViewRequest(msgId)
    }

    override val resultDeserializer: DeserializationStrategy<out ChannelMessageDetails>
        get() = ChannelMessageDetailsImpl.serializer()

    override val apiPaths: List<String>
        get() = apiPathList

    override fun ParametersBuilder.buildParameters() {
        append("msg_id", msgId.toString())
    }

}
