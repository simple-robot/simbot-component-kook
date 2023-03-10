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

package love.forte.simbot.kook.api.guild.role

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.ID
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest


/**
 *
 * [删除服务器角色](https://developer.kaiheila.cn/doc/http/guild-role#删除服务器角色)
 *
 * method `POST`
 *
 * `/api/v3/guild-role/delete`
 *
 */
public class GuildRoleDeleteRequest internal constructor(
    private val guildId: ID,
    private val roleId: ID,
) : KookPostRequest<Unit>() {
    public companion object Key : BaseKookApiRequestKey("guild-role", "delete") {
        
        /**
         * 构建 [GuildRoleDeleteRequest]
         * @param guildId 频道ID
         * @param roleId 角色ID
         */
        @JvmStatic
        public fun create(guildId: ID, roleId: ID): GuildRoleDeleteRequest = GuildRoleDeleteRequest(guildId, roleId)
        
    }
    
    override val resultDeserializer: DeserializationStrategy<out Unit>
        get() = Unit.serializer()
    
    override val apiPaths: List<String>
        get() = apiPathList
    
    override fun createBody(): Any = Body(guildId, roleId)
    
    @Serializable
    private data class Body(
        @SerialName("guild_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val guildId: ID,
        @SerialName("role_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val roleId: ID,
    )
    
}
