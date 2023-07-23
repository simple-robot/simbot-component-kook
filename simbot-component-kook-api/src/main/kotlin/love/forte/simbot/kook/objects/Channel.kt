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

package love.forte.simbot.kook.objects

import kotlinx.serialization.Serializable
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.definition.ChannelInfo


/**
 * Kook objects - [频道Channel](https://developer.kaiheila.cn/doc/objects#%E9%A2%91%E9%81%93Channel)
 *
 * #### 示例
 *
 * ```json
 * {
 *     "id": "53002000000000",
 *     "name": "新的频道",
 *     "user_id": "2418239356",
 *     "guild_id": "6016389000000",
 *     "is_category": 0,
 *     "parent_id": "6016400000000000",
 *     "level": null,
 *     "slow_mode": 0,
 *     "topic": "新的频道的说明",
 *     "type": 1,
 *     "permission_overwrites": [
 *         {
 *             "role_id": 0,
 *             "allow": 0,
 *             "deny": 0
 *         }
 *     ],
 *     "permission_users": [],
 *     "permission_sync": 1
 * }
 * ```
 *
 *
 * @author ForteScarlet
 */
public interface Channel : KookObjects, ChannelInfo {

    /** 频道id */
    override val id: ID

    /** 频道名称 */
    override val name: String


    /** 创建者id */
    public val userId: ID

    /** 服务器id */
    override val guildId: ID

    /** 频道简介 */
    public val topic: String

    /** 是否为分组 */
    public val isCategory: Boolean

    /** 上级分组的id */
    public val parentId: ID

    /** 排序level */
    public val level: Int

    /** 慢速模式下限制发言的最短时间间隔, 单位为秒(s) */
    public val slowMode: Int

    /** 频道类型: 1 文字频道, 2 语音频道 */
    public val type: Int

    /** 针对角色在该频道的权限覆写规则组成的列表 */
    public val permissionOverwrites: List<ChannelPermissionOverwrites>

    /**
     * 针对用户在该频道的权限覆写规则组成的列表
     *
     * 在一些官方文档未提及的地方（例如 /guild/view API），permissionUsers 可能会默认得到空。
     *
     */
    public val permissionUsers: List<ID>

    /** 权限设置是否与分组同步, 1 or 0 */
    public val permissionSync: Int


    /**
     * [Channel] 的类型，一般出现在事件中。
     */
    @Serializable
    public enum class Type {
        GROUP, PERSON,
    }

    // impls

    override val ownerId: ID get() = userId
    override val description: String get() = topic
    override val createTime: Timestamp get() = Timestamp.NotSupport

}


/**
 * 判断频道类型是否为 [Channel.Type.PERSON].
 */
public inline val Channel.Type.isPrivate: Boolean get() = this == Channel.Type.PERSON

/**
 * 判断频道类型是否为 [Channel.Type.PERSON].
 */
public inline val Channel.Type.isPerson: Boolean get() = isPrivate

/**
 * 判断频道类型是否为 [Channel.Type.GROUP].
 */
public inline val Channel.Type.isGroup: Boolean get() = this == Channel.Type.GROUP


/**
 * 针对角色在该频道的权限覆写规则组成的列表.
 * ```json
 *     "permission_overwrites": [
 *         {
 *             "role_id": 0,
 *             "allow": 0,
 *             "deny": 0
 *         }
 *     ],
 *
 * ```
 *
 */
public interface ChannelPermissionOverwrites {
    public val roleId: Int
    public val allow: Int
    public val deny: Int
}

