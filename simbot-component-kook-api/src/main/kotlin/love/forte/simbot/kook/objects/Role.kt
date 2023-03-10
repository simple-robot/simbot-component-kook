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

@file:Suppress("unused")

package love.forte.simbot.kook.objects

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.kook.objects.impl.RoleImpl


/**
 *
 * Kook objects - [角色Role](https://developer.kaiheila.cn/doc/objects#%E8%A7%92%E8%89%B2Role)
 *
 * 官方示例：
 * ```json
 * {
 *     "role_id": 11111,
 *     "name": "新角色",
 *     "color": 0,
 *     "position": 5,
 *     "hoist": 0,
 *     "mentionable": 0,
 *     "permissions": 142924296
 * }
 *
 * ```
 * @see RoleImpl
 * @author ForteScarlet
 */
public interface Role : KookObjects, Comparable<Role> {

    /** 角色id */
    public val roleId: ID

    /** 角色名称 */
    public val name: String

    /** 颜色色值 */
    public val color: Int

    /** 顺序位置 */
    public val position: Int

    /** 是否为角色设定(与普通成员分开显示) */
    public val hoist: Int

    /** 是否允许任何人@提及此角色 */
    public val mentionable: Int

    /** 权限码 */
    public val permissions: Permissions

    /**
     * 权限码的数字值
     */
    public val permissionsValue: Int get() = permissions.perm.toInt()

    override fun compareTo(other: Role): Int = position.compareTo(other.position)

    public companion object {
        //public val serializer: KSerializer<out Role> = RoleImpl.serializer()
    }

}


/**
 * 提及角色权限组时候使用的 `mention_role_part` 字段值。
 */
@Serializable
public data class MentionRolePart(@SerialName("role_id") val id: CharSequenceID, val name: String)
