/*
 * Copyright (c) 2021-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.objects

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.definition.GuildInfo
import love.forte.simbot.kook.objects.impl.ChannelImpl
import love.forte.simbot.kook.objects.impl.RoleImpl


/**
 *
 * Kook objects - [服务器Guild](https://developer.kaiheila.cn/doc/objects#%E6%9C%8D%E5%8A%A1%E5%99%A8Guild)
 *
 * 官方数据结构示例：
 * ```json
 * {
 *   "id": "2405000000000",
 *   "name": "工具",
 *   "topic": "",
 *   "master_id": "9200000000",
 *   "icon": "",
 *   "notify_type": 1,
 *   "region": "beijing",
 *   "enable_open": false,
 *   "open_id": "0",
 *   "default_channel_id": "2369000000000",
 *   "welcome_channel_id": "0",
 *   "roles": [
 *       {
 *           "role_id": 109472,
 *           "name": "管理员",
 *           "color": 0,
 *           "position": 1,
 *           "hoist": 0,
 *           "mentionable": 0,
 *           "permissions": 1
 *       }
 *   ],
 *   "channels": [
 *       {
 *           "id": "2369000000000",
 *           "master_id": "9200000000",
 *           "parent_id": "",
 *           "name": "你好",
 *           "type": 1,
 *           "level": 1,
 *           "limit_amount": 0,
 *           "is_category": false
 *       },
 *   ]
 * }
 * ```
 *
 *
 * @author ForteScarlet
 */
public interface Guild : KookObjects, GuildInfo {

    /** 服务器id */
    override val id: ID

    /** 服务器名称 */
    override val name: String

    /** 服务器icon的地址 */
    override val icon: String

    /** 服务器主题 */
    public val topic: String

    /** 服务器主的id */
    public val masterId: ID


    /** 通知类型, 0代表默认使用服务器通知设置，1代表接收所有通知, 2代表仅@被提及，3代表不接收通知 */
    public val notifyType: Int

    /** 服务器默认使用语音区域 */
    public val region: String

    /** 是否为公开服务器 */
    public val enableOpen: Boolean

    /** 公开服务器id */
    public val openId: ID

    /** 默认频道id */
    public val defaultChannelId: ID

    /** 欢迎频道id */
    public val welcomeChannelId: ID

    /** 角色列表 */
    public val roles: List<Role>?

    /** 频道列表 */
    public val channels: List<Channel>?

    //// impls
    override val ownerId: ID get() = masterId
    override val description: String get() = topic
    override val currentChannel: Int get() = channels?.size ?: 0


    public companion object {
        internal val serializer: KSerializer<out Guild> = GuildImpl.serializer()
    }

}


@Serializable
internal data class GuildImpl(
    override val id: CharSequenceID,
    override val name: String,
    override val topic: String,
    @SerialName("master_id")
    override val masterId: CharSequenceID,
    override val icon: String,
    @SerialName("notify_type")
    override val notifyType: Int,
    override val region: String,
    @SerialName("enable_open")
    override val enableOpen: Boolean,
    @SerialName("open_id")
    override val openId: CharSequenceID,
    @SerialName("default_channel_id")
    override val defaultChannelId: CharSequenceID,
    @SerialName("welcome_channel_id")
    override val welcomeChannelId: CharSequenceID,
    override val roles: List<RoleImpl>,
    override val channels: List<ChannelImpl>,

    // 可选的
    override val maximumChannel: Int = -1,
    override val createTime: Timestamp = Timestamp.NotSupport,
    override val currentMember: Int = -1,
    override val maximumMember: Int = -1,
) : Guild
