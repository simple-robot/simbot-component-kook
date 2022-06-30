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

@file:Suppress("unused")

package love.forte.simbot.kaiheila.objects

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.kaiheila.objects.impl.RoleImpl


/**
 *
 *  Kook objects - [角色Role](https://developer.kaiheila.cn/doc/objects#%E8%A7%92%E8%89%B2Role)
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
        public val serializer: KSerializer<out Role> = RoleImpl.serializer()
    }

}


/**
 * 提及角色权限组时候使用的 `mention_role_part` 字段值。
 */
@Serializable
public data class MentionRolePart(@SerialName("role_id") val id: CharSequenceID, val name: String)