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

package love.forte.simbot.kook.api.invite

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.ID
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest


/**
 * [删除邀请链接](https://developer.kaiheila.cn/doc/http/invite#删除邀请链接)
 *
 * `/api/v3/invite/delete`
 *
 * method POST
 */
public class InviteDeleteRequest internal constructor(
    private val urlCode: String,
    private val guildId: ID? = null,
    private val channelId: ID? = null,
) : KookPostRequest<Unit>() {
    public companion object Key : BaseKookApiRequestKey("invite", "delete") {
        
        /**
         * 构造 [InviteDeleteRequest].
         *
         * @param urlCode 邀请码
         * @param guildId 服务器 id
         * @param channelId 服务器频道 id
         *
         */
        @JvmStatic
        @JvmOverloads
        public fun create(urlCode: String, guildId: ID? = null, channelId: ID? = null): InviteDeleteRequest =
            InviteDeleteRequest(urlCode, guildId, channelId)
        
    }
    
    override val resultDeserializer: DeserializationStrategy<out Unit> get() = Unit.serializer()
    override val apiPaths: List<String> get() = apiPathList
    
    override fun createBody(): Any = Body(urlCode, guildId, channelId)
    
    @Serializable
    private data class Body(
        @SerialName("url_code") val urlCode: String,
        @SerialName("guild_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val guildId: ID?,
        @SerialName("channel_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val channelId: ID?,
    )
    
}
