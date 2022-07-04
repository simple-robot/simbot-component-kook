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

package love.forte.simbot.kook.objects

import kotlinx.serialization.Serializable

/**
 * ## 权限
 *
 * 权限是一个unsigned int值，由比特位代表是否拥有对应的权限。
 * 权限值与对应比特位进行按位与操作，判断是否拥有该权限。
 *
 * ### 判断是否有某权限
 * 其中: permissions代表权限值，bitValue代表某权限比特位，1 << bitValue 代表某权限值。
 * `permissions & (1 << bitValue)  == (1 << bitValue);`
 *
 * @param perm 权限具体的值。
 * @author ForteScarlet
 */
@Suppress("MemberVisibilityCanBePrivate")
@JvmInline
@Serializable
public value class Permissions(public val perm: UInt) {

    public constructor(permissionType: PermissionType) : this(permissionType.value)
    public constructor(vararg permissionTypes: PermissionType) : this(permissionTypes.combine())

    /**
     * 判断提供的权限类型全部存在于当前值中。
     */
    public operator fun contains(permissionType: PermissionType): Boolean {
        return with(permissionType.value) {
            this == perm and this
        }
    }

    /**
     * 判断提供的权限类型任意一个存在于当前值中。
     */
    public fun anyContains(permissionType: PermissionType): Boolean {
        return (perm and permissionType.value) > 0u


    }
}