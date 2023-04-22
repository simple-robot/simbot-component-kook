/*
 * Copyright (c) 2021-2023. ForteScarlet.
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

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import love.forte.simbot.ID
import love.forte.simbot.kook.api.*
import love.forte.simbot.kook.objects.Role
import love.forte.simbot.kook.objects.impl.RoleImpl
import love.forte.simbot.literal


/**
 *
 * [获取服务器角色列表](https://developer.kaiheila.cn/doc/http/guild-role#获取服务器角色列表)
 *
 * method: `GET`
 *
 * `/api/v3/guild-role/list`
 *
 * @param guildId 服务器的id
 */
public class GuildRoleListRequest internal constructor(
    public val guildId: ID,
    private val pageRequest: PageRequestParameters? = null,
) : KookGetRequest<KookApiResult.ListData<Role>>() {
    public companion object Key : BaseKookApiRequestKey("guild-role", "list") {
        private val serializer = KookApiResult.ListData.serializer(RoleImpl.serializer())
        
        /**
         * 构建 [GuildRoleListRequest]
         * @param guildId 频道服务器ID
         * @param pageRequest 分页查询参数
         *
         */
        @JvmStatic
        @JvmOverloads
        public fun create(guildId: ID, pageRequest: PageRequestParameters? = null): GuildRoleListRequest =
            GuildRoleListRequest(guildId, pageRequest)
    }
    
    override val resultDeserializer: DeserializationStrategy<KookApiResult.ListData<Role>>
        get() = serializer
    
    override val apiPaths: List<String>
        get() = apiPathList
    
    override fun ParametersBuilder.buildParameters() {
        append("guild_id", guildId.literal)
        pageRequest?.appendTo(this)
    }
    
}


