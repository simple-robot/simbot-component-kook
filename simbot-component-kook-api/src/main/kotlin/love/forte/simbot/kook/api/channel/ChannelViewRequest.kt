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
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.kook.api.ApiResultType
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookGetRequest
import love.forte.simbot.kook.objects.Channel
import love.forte.simbot.kook.objects.ChannelPermissionOverwrites
import love.forte.simbot.literal


/**
 *
 * [获取频道详情](https://developer.kaiheila.cn/doc/http/channel#%E8%8E%B7%E5%8F%96%E9%A2%91%E9%81%93%E8%AF%A6%E6%83%85)
 *
 * request method: GET
 *
 * @author ForteScarlet
 */
public class ChannelViewRequest internal constructor(private val targetId: ID) : KookGetRequest<ChannelView>() {

    public companion object Key : BaseKookApiRequestKey("channel", "view") {

        /**
         * 构建 [ChannelViewRequest]
         * @param targetId 目标频道ID
         */
        @JvmStatic
        public fun create(targetId: ID): ChannelViewRequest = ChannelViewRequest(targetId)
    }

    override val resultDeserializer: DeserializationStrategy<ChannelView>
        get() = ChannelView.serializer()

    override val apiPaths: List<String>
        get() = apiPathList

    override fun ParametersBuilder.buildParameters() {
        append("target_id", targetId.literal)
    }
}


/**
 * [频道详情](https://developer.kaiheila.cn/doc/http/channel#%E8%8E%B7%E5%8F%96%E9%A2%91%E9%81%93%E8%AF%A6%E6%83%85)
 */
@Serializable
public data class ChannelView @ApiResultType constructor(
    /** 频道id */
    override val id: CharSequenceID,
    /** 服务器id */
    @SerialName("guild_id")
    override val guildId: CharSequenceID,
    /** 频道创建者id */
    @SerialName("user_id")
    override val userId: CharSequenceID,
    /** 父分组频道id */
    @SerialName("parent_id")
    override val parentId: CharSequenceID,
    /** 频道名称 */
    override val name: String,
    /** 频道简介 */
    override val topic: String,
    /** 频道类型，1 文字，2 语音 */
    override val type: Int,
    /** 频道排序 */
    override val level: Int,
    /** 慢速限制，单位秒。用户发送消息之后再次发送消息的等待时间。 */
    @SerialName("slow_mode")
    override val slowMode: Int = -1,
    /** 人数限制 */
    @SerialName("limit_amount")
    override val maximumMember: Int = -1,
    /** 是否为分组类型 */
    @SerialName("is_category")
    override val isCategory: Boolean = false,
    // maybe miss
    @SerialName("permission_overwrites")
    override val permissionOverwrites: List<ChannelPermissionOverwritesView> = emptyList(),

    @SerialName("permission_sync")
    override val permissionSync: Int = 0,
) : Channel {
    override val currentMember: Int
        get() = -1

    override val icon: String
        get() = ""

    /**
     * 始终得到空集合。
     *
     * 在官方文档中并未提及 `permission_users` 属性，因此不会对其进行解析。
     *
     */
    override val permissionUsers: List<CharSequenceID> get() = emptyList()
}


@Serializable
public data class ChannelPermissionOverwritesView(
    @SerialName("role_id")
    override val roleId: Int,
    override val allow: Int,
    override val deny: Int,
) : ChannelPermissionOverwrites


