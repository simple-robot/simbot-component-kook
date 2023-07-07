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

package love.forte.simbot.kook.api.channel

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.kook.api.ApiResultType
import love.forte.simbot.kook.api.KookGetApi
import love.forte.simbot.kook.api.ListData
import love.forte.simbot.kook.util.parameters
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic


/**
 * [获取频道列表](https://developer.kookapp.cn/doc/http/channel#%E8%8E%B7%E5%8F%96%E9%A2%91%E9%81%93%E5%88%97%E8%A1%A8)
 *
 * @author ForteScarlet
 */
public class GetChannelListApi private constructor(
    private val guildId: String,
    private val page: Int? = null,
    private val pageSize: Int? = null,
    private val type: Int? = null,
) : KookGetApi<ListData<ChannelInfo>>() {
    public companion object Factory {
        private val PATH = ApiPath.create("channel", "list")

        private val serializer = ListData.serializer(ChannelInfo.serializer())

        /**
         * 构建 [GetChannelListApi]
         * @param guildId 频道服务器ID
         * @param page 目标页数
         * @param pageSize 每页数据数量
         * @param type 频道类型, `1`为文字，`2`为语音, 默认为`1`.
         *
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            guildId: String,
            page: Int? = null,
            pageSize: Int? = null,
            type: Int? = null,
        ): GetChannelListApi = GetChannelListApi(guildId, page, pageSize, type)
    }

    override val resultDeserializer: DeserializationStrategy<ListData<ChannelInfo>>
        get() = serializer

    override val apiPath: ApiPath
        get() = PATH

    override fun urlBuild(builder: URLBuilder) {
        builder.parameters {
            append("guild_id", guildId)
            page?.also { append("page", it.toString()) }
            pageSize?.also { append("page_size", it.toString()) }
            type?.also { append("type", it.toString()) }
        }
    }
}

/**
 * Api [GetChannelListApi] 的响应体。
 */
@Serializable
public data class ChannelInfo @ApiResultType constructor(
    /**
     * 频道id
     */
    public val id: String,
    /**
     * 频道名称
     */
    public val name: String,
    /**
     * 是否为分组类型
     */
    @SerialName("is_category") public val isCategory: Boolean,
    /**
     * 频道创建者id
     */
    @SerialName("user_id") public val userId: String,
    /**
     * 父分组频道id
     */
    @SerialName("parent_id") public val parentId: String,
    /**
     * 频道排序
     */
    public val level: Int,
    /**
     * 频道类型
     */
    public val type: Int,
    /**
     * 人数限制
     */
    @SerialName("limit_amount")
    public val limitAmount: Int,
)




