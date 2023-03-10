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
import love.forte.simbot.kook.api.ApiResultType
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookGetRequest
import love.forte.simbot.kook.objects.Channel
import love.forte.simbot.kook.objects.Guild
import love.forte.simbot.kook.objects.Role
import love.forte.simbot.literal


/**
 * [获取服务器详情](https://developer.kaiheila.cn/doc/http/guild#%E8%8E%B7%E5%8F%96%E6%9C%8D%E5%8A%A1%E5%99%A8%E8%AF%A6%E6%83%85)
 *
 * api: /guild/view
 *
 * request method: GET
 *
 */
public class GuildViewRequest internal constructor(private val guildId: ID) : KookGetRequest<Guild>() {
    public companion object Key : BaseKookApiRequestKey("guild", "view") {
        /**
         * 构造 [GuildViewRequest].
         *
         * @param guildId 频道服务器ID
         */
        @JvmStatic
        public fun create(guildId: ID): GuildViewRequest = GuildViewRequest(guildId)
    }

    override val apiPaths: List<String>
        get() = apiPathList

    override val resultDeserializer: DeserializationStrategy<out Guild>
        get() = GuildView.serializer()

    override fun ParametersBuilder.buildParameters() {
        append("guild_id", guildId.literal)
    }
}


/*
{
    "code": 0,
    "message": "操作成功",
    "data": {
        "id": "91686000000",
        "name": "Hello",
        "topic": "",
        "master_id": "17000000",
        "is_master": false,
        "icon": "",
        "invite_enabled": true,
        "notify_type": 2,
        "region": "beijing",
        "enable_open": true,
        "open_id": "1600000",
        "default_channel_id": "2710000000",
        "welcome_channel_id": "0",
        "features": [],
        "roles": [
            {
                "role_id": 0,
                "name": "@全体成员",
                "color": 0,
                "position": 999,
                "hoist": 0,
                "mentionable": 0,
                "permissions": 148691464
            }
        ],
        "channels": [
            {
                "id": "37090000000",
                "user_id": "1780000000",
                "parent_id": "0",
                "name": "Hello World",
                "type": 1,
                "level": 100,
                "limit_amount": 0,
                "is_category": false,
                "is_readonly": false,
                "is_private": false
            }
        ],
        "emojis": [
            {
                "name": "ceeb65XXXXXXX0j60jpwfu",
                "id": "9168XXXXX53/4c43fcb7XXXXX0c80ck"
            }
        ],
        "user_config": {
            "notify_type": null,
            "nickname": "XX",
            "role_ids": [
                702
            ],
            "chat_setting": "1"
        }
    }
}

 */

@Serializable
internal data class GuildView @ApiResultType constructor(
    override val id: ID,
    override val name: String,
    override val topic: String,
    @SerialName("master_id")
    override val masterId: ID,
    override val icon: String,
    @SerialName("notify_type")
    override val notifyType: Int,
    override val region: String,
    @SerialName("enable_open")
    override val enableOpen: Boolean,
    @SerialName("open_id")
    override val openId: ID,
    @SerialName("default_channel_id")
    override val defaultChannelId: ID,
    @SerialName("welcome_channel_id")
    override val welcomeChannelId: ID,
    /**
     * 服务器助力数量
     */
    @SerialName("boost_num")
    val boostNum: Int,
    /**
     * 服务器等级
     */
    val level: Int,
    override val roles: List<Role>,
    override val channels: List<Channel>,

    ////
    override val maximumChannel: Int = -1,
    override val maximumMember: Int = -1,

    ) : Guild {
    override val createTime: Timestamp get() = Timestamp.NotSupport

    override val currentMember: Int
        get() =
            channels.sumOf { c -> c.currentMember.takeIf { it > 0 } ?: 0 }
                .takeIf { it > 0 } ?: -1
}






