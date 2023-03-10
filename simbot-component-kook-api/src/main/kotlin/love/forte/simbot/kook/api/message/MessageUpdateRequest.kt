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
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest

/**
 * [更新频道聊天消息](https://developer.kaiheila.cn/doc/http/message#%E6%9B%B4%E6%96%B0%E9%A2%91%E9%81%93%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF)
 *
 * *无返回参数*
 */
public class MessageUpdateRequest internal constructor(
    /**
     * 消息 id
     */
    private val msgId: ID,
    
    /**
     * 消息内容
     */
    private val content: String,
    
    /**
     * 回复某条消息的 msgId。如果为空
     * （为空大概指空字符串：`""`，可以使用 [CharSequenceID.EMPTY] 或者 `"".ID` ），
     * 则代表删除回复，不传则无影响。
     */
    private val quote: ID? = null,
    
    /**
     * 用户 id，针对特定用户临时更新消息，必须是正常消息才能更新。与发送临时消息概念不同，但同样不保存数据库。
     */
    private val tempTargetId: ID? = null,
) : KookPostRequest<Unit>() {
    public companion object Key : BaseKookApiRequestKey("message", "update") {
    
        /**
         *
         * @param msgId 消息 id
         * @param content 消息内容
         * @param quote 回复某条消息的 msgId。如果为空
         * （为空大概指空字符串：`""`，可以使用 [CharSequenceID.EMPTY] 或者 `"".ID` ）
         * 则代表删除回复，不传则无影响。
         * @param tempTargetId 用户 id，针对特定用户临时更新消息，必须是正常消息才能更新。与发送临时消息概念不同，但同样不保存数据库。
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            msgId: ID,
            content: String,
            quote: ID? = null,
            tempTargetId: ID? = null,
        ): MessageUpdateRequest =
            MessageUpdateRequest(msgId, content, quote, tempTargetId)
    }
    
    override val resultDeserializer: DeserializationStrategy<out Unit> get() = Unit.serializer()
    override val apiPaths: List<String> get() = apiPathList
    override fun createBody(): Any = Body(msgId, content, quote, tempTargetId)
    
    @Serializable
    private data class Body(
        @SerialName("msg_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val msgId: ID,
        val content: String,
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val quote: ID? = null,
        @SerialName("temp_target_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val tempTargetId: ID? = null,
    )
}


/**
 * 通过 [MessageCreated] 构建 [MessageUpdateRequest] 实例。
 *
 * @see MessageCreated.toDirectUpdate
 */
public fun MessageCreated.toUpdate(
    content: String,
    quote: ID? = null
): MessageUpdateRequest =
    MessageUpdateRequest.create(msgId, content, quote)
