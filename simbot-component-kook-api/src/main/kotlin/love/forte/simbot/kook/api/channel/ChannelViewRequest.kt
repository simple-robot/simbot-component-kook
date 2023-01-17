/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kook 的一部分。
 *
 *  simbot-component-kook 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kook 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
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

    override val resultDeserializer: DeserializationStrategy<out ChannelView>
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
    override val slowMode: Int,
    /** 人数限制 */
    @SerialName("limit_amount")
    override val maximumMember: Int,
    /** 是否为分组类型 */
    @SerialName("is_category")
    override val isCategory: Boolean,
    /** 语音服务器地址，HOST:PORT的格式 */
    @SerialName("server_url")
    val serverUrl: String,
    // maybe miss
    @SerialName("permission_overwrites")
    override val permissionOverwrites: List<ChannelPermissionOverwrites> = emptyList(),
    @SerialName("permission_users")
    override val permissionUsers: List<CharSequenceID> = emptyList(),
    @SerialName("permission_sync")
    override val permissionSync: Int = 0,
) : Channel {
    override val currentMember: Int
        get() = -1

    override val icon: String
        get() = ""
}
