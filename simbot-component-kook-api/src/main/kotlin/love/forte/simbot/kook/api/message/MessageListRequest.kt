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
import love.forte.simbot.kook.api.KookApiResult
import love.forte.simbot.kook.api.KookGetRequest
import love.forte.simbot.kook.api.appendIfNotnull

/**
 * [获取频道聊天消息列表](https://developer.kaiheila.cn/doc/http/message#%E8%8E%B7%E5%8F%96%E9%A2%91%E9%81%93%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF%E5%88%97%E8%A1%A8)
 *
 * @author ForteScarlet
 */
public class MessageListRequest internal constructor(
    /**
     * 频道 id
     */
    private val targetId: ID,
    
    /**
     * 参考消息 id，不传则默认为最新的消息 id
     */
    private val msgId: ID? = null,
    
    /**
     * 是否查询置顶消息（置顶消息只支持查询最新的消息）。
     */
    private val pin: Boolean = false,
    
    /**
     * 查询模式，有三种模式可以选择。不传则默认查询最新的消息.
     *
     * @see MessageListFlag
     */
    private val flag: MessageListFlag? = null,
    
    /**
     * 当前分页消息数量, 如果小于等于零则不提供此参数。服务器此参数默认 50
     */
    private val pageSize: Int = -1
) : KookGetRequest<KookApiResult.ListData<ChannelMessageDetails>>() {
    public companion object Key : BaseKookApiRequestKey("message", "list") {
        private val serializer = KookApiResult.ListData.serializer(ChannelMessageDetailsImpl.serializer())
        
        /**
         * 构造 [MessageListRequest]
         * @param targetId 频道 id
         * @param msgId 参考消息 id，不传则默认为最新的消息 id
         * @param pin 是否查询置顶消息（置顶消息只支持查询最新的消息）。
         * @param flag 查询模式，有三种模式可以选择。不传则默认查询最新的消息.
         * @param pageSize 当前分页消息数量, 如果小于等于零则不提供此参数。服务器此参数默认 50
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            targetId: ID,
            msgId: ID? = null,
            pin: Boolean = false,
            flag: MessageListFlag? = null,
            pageSize: Int = -1
        ): MessageListRequest =
            MessageListRequest(targetId, msgId, pin, flag, pageSize)
    }
    
    override val apiPaths: List<String>
        get() = apiPathList
    
    override val resultDeserializer: DeserializationStrategy<KookApiResult.ListData<ChannelMessageDetails>>
        get() = serializer
    
    override fun ParametersBuilder.buildParameters() {
        append("target_id", targetId.toString())
        appendIfNotnull("msgId", msgId)
        appendIfNotnull("pin", if (pin) 1 else 0)
        appendIfNotnull("flag", flag) { it.name.lowercase() }
        appendIfNotnull("msg_id", msgId)
        if (pageSize > 0) {
            append("page_size", pageSize.toString())
        }
    }
}
