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
import love.forte.simbot.Timestamp
import love.forte.simbot.kook.api.*
import love.forte.simbot.kook.objects.Channel
import love.forte.simbot.kook.objects.Guild
import love.forte.simbot.kook.objects.Role


/**
 * [获取当前用户加入的服务器列表](https://developer.kaiheila.cn/doc/http/guild#%E8%8E%B7%E5%8F%96%E5%BD%93%E5%89%8D%E7%94%A8%E6%88%B7%E5%8A%A0%E5%85%A5%E7%9A%84%E6%9C%8D%E5%8A%A1%E5%99%A8%E5%88%97%E8%A1%A8).
 *
 * request method: `GET`
 *
 *
 *
 *
 *
 */
public class GuildListRequest internal constructor(
    private val page: Int, private val pageSize: Int, private val sort: String?
) : KookGetRequest<KookApiResult.ListData<Guild>>() {
    public companion object Key : BaseKookApiRequestKey("guild", "list") {
        private val serializer = KookApiResult.ListData.serializer(GuildListElement.serializer())
        
        /**
         * 全部使用默认值的 [GuildListRequest].
         *
         */
        @JvmField
        public val DEFAULT: GuildListRequest = GuildListRequest(-1, -1, null)
        
        /**
         * 构造 [GuildListRequest].
         * @param page 目标页数
         * @param pageSize 每页数据数量
         * @param sort 代表排序的字段, 比如-id代表id按DESC排序，id代表id按ASC排序。不一定有, 如果有，接口中会声明支持的排序字段。
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            page: Int = DEFAULT.page, pageSize: Int = DEFAULT.pageSize, sort: String? = DEFAULT.sort
        ): GuildListRequest {
            if (page == DEFAULT.page && pageSize == DEFAULT.pageSize && sort == DEFAULT.sort) {
                return DEFAULT
            }
            
            return GuildListRequest(page, pageSize, sort)
        }
        
    }
    
    override val apiPaths: List<String>
        get() = apiPathList
    
    override val resultDeserializer: DeserializationStrategy<out KookApiResult.ListData<Guild>>
        get() = serializer
    
    override fun ParametersBuilder.buildParameters() {
        if (page > 0) {
            append("page", page.toString())
        }
        if (pageSize > 0) {
            append("page_size", pageSize.toString())
        }
        appendIfNotnull("sort", sort)
    }
    
}


/**
 *
 * [获取当前用户加入的服务器列表](https://developer.kaiheila.cn/doc/http/guild#%E8%8E%B7%E5%8F%96%E5%BD%93%E5%89%8D%E7%94%A8%E6%88%B7%E5%8A%A0%E5%85%A5%E7%9A%84%E6%9C%8D%E5%8A%A1%E5%99%A8%E5%88%97%E8%A1%A8)
 * api的返回值。
 *
 */
@Serializable
internal data class GuildListElement @ApiResultType constructor(
    /**
     * 服务器id
     */
    @Serializable(ID.AsCharSequenceIDSerializer::class) override val id: ID,
    /**
     * 服务器名称
     */
    override val name: String,
    /**
     * 服务器主题
     */
    override val topic: String,
    /**
     * 服务器主的id
     */
    @SerialName("master_id") @Serializable(ID.AsCharSequenceIDSerializer::class) override val masterId: ID,
    /**
     * 	服务器icon的地址
     */
    override val icon: String,
    
    /**
     * 通知类型,
     * - `0` 代表默认使用服务器通知设置
     * - `1` 代表接收所有通知
     * - `2` 代表仅@被提及
     * - `3` 代表不接收通知
     */
    @SerialName("notify_type") override val notifyType: Int,
    
    /**
     * 服务器默认使用语音区域
     */
    override val region: String,
    /**
     * 是否为公开服务器
     */
    @SerialName("enable_open") override val enableOpen: Boolean,
    /**
     * 公开服务器id
     */
    @SerialName("open_id") @Serializable(ID.AsCharSequenceIDSerializer::class) override val openId: ID,
    /**
     * 	默认频道id
     */
    @SerialName("default_channel_id") @Serializable(ID.AsCharSequenceIDSerializer::class) override val defaultChannelId: ID,
    /**
     * 欢迎频道id
     */
    @SerialName("welcome_channel_id") @Serializable(ID.AsCharSequenceIDSerializer::class) override val welcomeChannelId: ID,
) : Guild {
    override val roles: List<Role>? = null
    override val channels: List<Channel>? = null
    
    // 可选的
    override val maximumChannel: Int = -1
    override val createTime: Timestamp = Timestamp.NotSupport
    override val currentMember: Int = -1
    override val maximumMember: Int = -1
}


@Serializable
public data class GuildApiRespSort(val id: Int)

public inline val GuildApiRespSort.isAsc: Boolean get() = id == 1
public inline val GuildApiRespSort.isDesc: Boolean get() = !isAsc



