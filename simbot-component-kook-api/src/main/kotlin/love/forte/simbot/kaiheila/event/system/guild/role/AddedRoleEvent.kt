/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kaiheila.event.system.guild.role

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.ID
import love.forte.simbot.LongID
import love.forte.simbot.kaiheila.objects.Permissions
import love.forte.simbot.kaiheila.util.BooleanToIntSerializer

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
