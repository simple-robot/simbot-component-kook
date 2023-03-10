/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.event.system.guild.role

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.ID
import love.forte.simbot.LongID
import love.forte.simbot.kook.objects.Permissions
import love.forte.simbot.kook.util.BooleanToIntSerializer

/**
 * 服务器角色增加
 *
 * `added_role`
 *
 * @author ForteScarlet
 */
public interface AddedRoleEventBody : GuildRoleEventExtraBody {

    /**
     * 角色id
     */
    public val roleId: ID

    /**
     * 角色名称
     */
    public val name: String

    /**
     * 颜色
     * Constraints:  Min 0┃Max 16777215
     */
    public val color: Int

    /**
     * 顺序，值越小载靠前
     */
    public val position: Int

    /**
     * 只能为0或者1，是否把该角色的用户在用户列表排到前面.
     * Allowed: 0┃1
     *
     * 0: false, 1: true.
     */
    public val isHoist: Boolean

    /**
     * 只能为0或者1，该角色是否可以被提及.
     * Allowed: 0┃1
     *
     * 0: false, 1: true.
     */
    public val isMentionable: Boolean

    /**
     * 允许的权限.
     * @see Permissions
     */
    public val permissions: Permissions

    /**
     * 权限的 [Int] 类型值。
     */
    public val permissionValue: Int get() = permissions.perm.toInt()

}

/**
 * 服务器角色增加
 *
 * `added_role`
 *
 * @author ForteScarlet
 */
@Serializable
internal data class AddedRoleEventBodyImpl(
    /**
     * 角色id
     */
    @SerialName("role_id")
    override val roleId: LongID,
    /**
     * 角色名称
     */
    override val name: String,
    /**
     * 颜色
     * Constraints:  Min 0┃Max 16777215
     */
    override val color: Int,
    /**
     * 顺序，值越小载靠前
     */
    override val position: Int,
    /**
     * 只能为0或者1，是否把该角色的用户在用户列表排到前面
     * Allowed: 0┃1
     */
    @SerialName("hoist")
    @Serializable(BooleanToIntSerializer::class)
    override val isHoist: Boolean,
    /**
     * 只能为0或者1，该角色是否可以被提及
     * Allowed: 0┃1
     */
    @SerialName("mentionable")
    @Serializable(BooleanToIntSerializer::class)
    override val isMentionable: Boolean,
    /**
     * 允许的权限.
     * @see Permissions
     */
    override val permissions: Permissions,
) : AddedRoleEventBody {
    init {
        check(color in 0..0xffffff) { "Color must be in 0..16777215(0xffffff), but $color" }
    }

}
