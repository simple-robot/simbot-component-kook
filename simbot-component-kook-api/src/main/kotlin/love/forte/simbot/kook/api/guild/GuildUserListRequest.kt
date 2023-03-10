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

package love.forte.simbot.kook.api.guild

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.ID
import love.forte.simbot.LongID
import love.forte.simbot.kook.api.*
import love.forte.simbot.kook.objects.User
import love.forte.simbot.literal


/**
 * [获取服务器中的用户列表](https://developer.kaiheila.cn/doc/http/guild#%E8%8E%B7%E5%8F%96%E6%9C%8D%E5%8A%A1%E5%99%A8%E4%B8%AD%E7%9A%84%E7%94%A8%E6%88%B7%E5%88%97%E8%A1%A8)
 *
 *
 * request method: GET
 *
 */
public class GuildUserListRequest internal constructor(
    /**	是 服务器的 ID */
    private val guildId: ID,
    /**	否 服务器中频道的 ID */
    private val channelId: ID? = null,
    /**	否 搜索关键字，在用户名或昵称中搜索 */
    private val search: String? = null,
    /**	否 角色 ID，获取特定角色的用户列表 */
    private val roleId: ID? = null,
    /**	否 只能为0或1，0是未认证，1是已认证 */
    private val mobileVerified: Boolean? = null,
    /**	否 根据活跃时间排序，0是顺序排列，1是倒序排列 */
    private val activeTimeSort: Int? = null,
    /**	否 根据加入时间排序，0是顺序排列，1是倒序排列 */
    private val joinedAtSort: Int? = null,
    /**	否 目标页 */
    private val page: Int = -1,
    /**	否 每页数据数量 */
    private val pageSize: Int = -1,
) : KookGetRequest<GuildUserList>() {
    
    @Deprecated(
        "Use GuildUserListRequest.create(...)",
        ReplaceWith("GuildUserListRequest.create(guildId = guildId, channelId = channelId, page = page, pageSize = pageSize)")
    )
    @JvmOverloads
    public constructor(
        guildId: ID, channelId: ID? = null,
        page: Int = -1, pageSize: Int = -1,
    ) : this(
        guildId = guildId,
        channelId = channelId,
        search = null,
        page = page,
        pageSize = pageSize
    )
    
    public companion object Key : BaseKookApiRequestKey("guild", "user-list") {
        
        /**
         * 构造 [GuildUserListRequest]
         * @param guildId 服务器的 ID
         * @param channelId 服务器中频道的 ID
         * @param search 搜索关键字，在用户名或昵称中搜索
         * @param roleId 角色 ID，获取特定角色的用户列表
         * @param mobileVerified 只能为0或1，0是未认证，1是已认证
         * @param activeTimeSort 根据活跃时间排序，0是顺序排列，1是倒序排列
         * @param joinedAtSort 根据加入时间排序，0是顺序排列，1是倒序排列
         * @param page 目标页
         * @param pageSize 每页数据数量
         *
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            guildId: ID,
            channelId: ID? = null,
            search: String? = null,
            roleId: ID? = null,
            mobileVerified: Boolean? = null,
            activeTimeSort: Int? = null,
            joinedAtSort: Int? = null,
            page: Int = -1,
            pageSize: Int = -1,
        ): GuildUserListRequest =
            GuildUserListRequest(
                guildId,
                channelId,
                search,
                roleId,
                mobileVerified,
                activeTimeSort,
                joinedAtSort,
                page,
                pageSize
            )
        
    }
    
    override val resultDeserializer: DeserializationStrategy<out GuildUserList>
        get() = GuildUserList.serializer()
    
    override val apiPaths: List<String>
        get() = apiPathList
    
    override fun ParametersBuilder.buildParameters() {
        append("guild_id", guildId.literal)
        appendIfNotnull("channelId", channelId) { it.literal }
        appendIfNotnull("search", search)
        appendIfNotnull("roleId", roleId)
        appendIfNotnull("mobileVerified", mobileVerified) { if (it) "1" else "0" }
        appendIfNotnull("activeTime", activeTimeSort)
        appendIfNotnull("joinedAt", joinedAtSort)
        if (page > 0) {
            append("page", page.toString())
        }
        if (pageSize > 0) {
            append("page_size", pageSize.toString())
        }
    }
    
    
}


/**
 * Guild list user 响应数据。
 */
@Serializable
public data class GuildUserList @ApiResultType constructor(
    /**
     * 用户数量
     */
    @SerialName("user_count")
    val userCount: Int,
    /**
     * 在线用户数量
     */
    @SerialName("online_count")
    val onlineCount: Int,
    /**
     * 离线用户数量
     */
    @SerialName("offline_count")
    val offlineCount: Int,
    /**
     * 用户列表
     */
    override val items: List<GuildUser>,
    override val meta: KookApiResult.ListMeta,
    override val sort: Map<String, Int> = emptyMap()
) : KookApiResult.ListDataResponse<GuildUser, Map<String, Int>>()

/**
 * Guild User from [GuildUserListRequest]
 *
 * ```json
 *  {
 *  "id": "444",
 *  "username": "***",
 *  "avatar": "https:// **.jpg",
 *  "online": true,
 *  "nickname": "***",
 *  "joined_at": 1611743334000,
 *  "active_time": 1612691445583,
 *  "roles": [],
 *  "is_master": true,
 *  "abbr": "***",
 *  }
 * ```
 */
@Serializable
public data class GuildUser @ApiResultType constructor(
    @Serializable(ID.AsCharSequenceIDSerializer::class)
    override val id: ID,
    override val username: String,
    override val nickname: String,
    @SerialName("online")
    override val isOnline: Boolean,
    override val status: Int = 0,
    override val avatar: String,
    @SerialName("vip_avatar")
    override val vipAvatar: String? = null,
    @SerialName("bot")
    override val isBot: Boolean = false,
    @SerialName("joined_at")
    val joinedAt: Long,
    @SerialName("active_time")
    val activeTime: Long,
    @SerialName("is_master")
    val master: Boolean,
    @SerialName("mobile_verified")
    override val mobileVerified: Boolean = false,
    @SerialName("identify_num")
    override val identifyNum: String = username.split("#", limit = 2).let { if (it.size < 2) it[0] else "" },
    override val roles: List<LongID> = emptyList(),
) : User
