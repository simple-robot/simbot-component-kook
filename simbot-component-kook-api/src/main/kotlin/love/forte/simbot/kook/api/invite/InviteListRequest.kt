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

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import love.forte.simbot.ID
import love.forte.simbot.Simbot
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookApiResult
import love.forte.simbot.kook.api.KookGetRequest
import love.forte.simbot.kook.api.appendIfNotnull
import love.forte.simbot.literal

/**
 *
 * [获取邀请列表](https://developer.kaiheila.cn/doc/http/invite#%E8%8E%B7%E5%8F%96%E9%82%80%E8%AF%B7%E5%88%97%E8%A1%A8)
 *
 * `/api/v3/invite/list`
 *
 * method: GET
 *
 */
public class InviteListRequest internal constructor(
    private val guildId: ID?,
    private val channelId: ID?,
    private val page: Int = -1,
    private val pageSize: Int = -1,
) : KookGetRequest<KookApiResult.ListData<InviteInfo>>() {
    public companion object Key : BaseKookApiRequestKey("invite", "list") {
        private val serializer = KookApiResult.ListData.serializer(InviteInfoImpl.serializer())
        
        /**
         * 构造 [InviteListRequest]
         * @param guildId 服务器 id. 服务器 id 或者频道 id 必须填一个
         * @param channelId 频道 id. 服务器 id 或者频道 id 必须填一个
         * @param page 目标页数. 不小于0时有效。
         * @param pageSize 每页数据数量. 不小于0时有效。
         */
        @JvmStatic
        @JvmOverloads
        public fun create(guildId: ID?, channelId: ID?, page: Int = -1, pageSize: Int = -1): InviteListRequest =
            InviteListRequest(guildId, channelId, page, pageSize)
    }
    
    init {
        Simbot.require(guildId != null || channelId != null) {
            "A guild id or channel id must exist"
        }
    }
    
    override val resultDeserializer: DeserializationStrategy<out KookApiResult.ListData<InviteInfo>>
        get() = serializer
    
    override val apiPaths: List<String>
        get() = apiPathList
    
    override fun ParametersBuilder.buildParameters() {
        appendIfNotnull("guild_id", guildId) { it.literal }
        appendIfNotnull("channel_id", channelId) { it.literal }
        if (page > 0) {
            append("page", page.toString())
        }
        if (pageSize > 0) {
            append("page_size", pageSize.toString())
        }
    }
}

