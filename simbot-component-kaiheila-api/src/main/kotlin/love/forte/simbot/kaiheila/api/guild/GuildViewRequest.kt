/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kaiheila 的一部分。
 *
 *  simbot-component-kaiheila 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kaiheila 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

package love.forte.simbot.kaiheila.api.guild

import io.ktor.http.*
import kotlinx.serialization.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.api.*
import love.forte.simbot.kaiheila.objects.*


/**
 * [获取服务器详情](https://developer.kaiheila.cn/doc/http/guild#%E8%8E%B7%E5%8F%96%E6%9C%8D%E5%8A%A1%E5%99%A8%E8%AF%A6%E6%83%85)
 *
 * api: /guild/view
 *
 * request method: GET
 *
 */
public class GuildViewRequest(private val guildId: ID) : KaiheilaGetRequest<Guild>() {
    public companion object : BaseApiRequestKey("guild", "view")

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






