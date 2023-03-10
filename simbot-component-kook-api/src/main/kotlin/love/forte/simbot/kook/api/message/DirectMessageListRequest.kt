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

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import love.forte.simbot.ID
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookApiResult
import love.forte.simbot.kook.api.KookGetRequest
import love.forte.simbot.kook.api.appendIfNotnull
import love.forte.simbot.kook.api.message.DirectMessageListRequest.Key.byChatCode
import love.forte.simbot.kook.api.message.DirectMessageListRequest.Key.byTargetId

/**
 * [获取私信聊天消息列表](https://developer.kaiheila.cn/doc/http/direct-message#%E8%8E%B7%E5%8F%96%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF%E5%88%97%E8%A1%A8)
 *
 * method: GET
 *
 * @see byChatCode
 * @see byTargetId
 */
public class DirectMessageListRequest private constructor(
    /**
     * 私信会话 Code。chat_code 与 target_id 必须传一个
     */
    private val chatCode: ID?,
    /**
     * 目标用户 id，后端会自动创建会话。有此参数之后可不传 chat_code参数
     */
    private val targetId: ID?,
    /**
     * 参考消息 id，不传则默认为最新的消息 id
     */
    private val msgId: ID? = null,
    /**
     * 查询模式，有三种模式可以选择。不传则默认查询最新的消息
     */
    private val flag: MessageListFlag? = null,
) : KookGetRequest<KookApiResult.ListData<DirectMessageDetails>>() {
    public companion object Key : BaseKookApiRequestKey("direct-message", "list") {
        /**
         * 构造 [DirectMessageListRequest]
         * @param chatCode 私信会话 Code。
         * @param msgId 参考消息 id，不传则默认为最新的消息 id
         * @param flag 查询模式，有三种模式可以选择。不传则默认查询最新的消息
         */
        @JvmStatic
        @JvmOverloads
        public fun byChatCode(
            chatCode: ID,
            msgId: ID? = null,
            flag: MessageListFlag? = null,
        ): DirectMessageListRequest {
            return DirectMessageListRequest(
                chatCode = chatCode, targetId = null, msgId = msgId, flag = flag
            )
        }
        
        /**
         * @suppress just use [byChatCode]
         * @see byChatCode
         */
        @Deprecated(
            "Just use byChatCode(...)", ReplaceWith(
                "byChatCode(chatCode, msgId, flag)",
                "love.forte.simbot.kook.api.message.DirectMessageListRequest.Key.byChatCode"
            )
        )
        @JvmStatic
        @JvmOverloads
        public fun getInstanceByChatCode(
            chatCode: ID, msgId: ID? = null, flag: MessageListFlag? = null
        ): DirectMessageListRequest = byChatCode(chatCode, msgId, flag)
    
        /**
         * 构造 [DirectMessageListRequest]
         *
         * @param targetId 目标用户 id，后端会自动创建会话。
         * @param msgId 参考消息 id，不传则默认为最新的消息 id
         * @param flag 查询模式，有三种模式可以选择。不传则默认查询最新的消息
         */
        @JvmStatic
        @JvmOverloads
        public fun byTargetId(
            targetId: ID,
            msgId: ID? = null,
            flag: MessageListFlag? = null,
        ): DirectMessageListRequest {
            return DirectMessageListRequest(
                chatCode = null, targetId = targetId, msgId = msgId, flag = flag
            )
        }
    
        /**
         * @suppress just use [byTargetId]
         * @see byTargetId
         */
        @Deprecated(
            "Just use byTargetId(...)",
            ReplaceWith(
                "byTargetId(targetId, msgId, flag)",
                "love.forte.simbot.kook.api.message.DirectMessageListRequest.Key.byTargetId"
            ),
        )
        @JvmStatic
        @JvmOverloads
        public fun getInstanceByTargetId(
            targetId: ID,
            msgId: ID? = null,
            flag: MessageListFlag? = null,
        ): DirectMessageListRequest = byTargetId(targetId, msgId, flag)
        
    }
    
    override val resultDeserializer: DeserializationStrategy<out KookApiResult.ListData<DirectMessageDetails>>
        get() = KookApiResult.ListData.serializer(DirectMessageDetails.serializer)
    
    override val apiPaths: List<String> get() = apiPathList
    
    override fun ParametersBuilder.buildParameters() {
        appendIfNotnull("chat_code", chatCode)
        appendIfNotnull("target_id", targetId)
        appendIfNotnull("msg_id", msgId)
        appendIfNotnull("flag", flag) { it.name.lowercase() }
    }
}
