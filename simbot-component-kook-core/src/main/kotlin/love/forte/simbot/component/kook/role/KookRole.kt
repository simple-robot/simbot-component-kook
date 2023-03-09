/*
 *  Copyright (c) 2023-2023 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.component.kook.role

import love.forte.simbot.Api4J
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.action.DeleteSupport
import love.forte.simbot.definition.Role
import love.forte.simbot.kook.api.KookApiException
import love.forte.simbot.kook.objects.PermissionType
import love.forte.simbot.kook.objects.Permissions

/**
 * 一个 [服务器角色](https://developer.kookapp.cn/doc/http/guild-role) 。
 *
 * ### 可删除
 *
 * 实现 [DeleteSupport], 权限满足时可以对指定Role进行删除。
 * 根据实现类型的不同分别代表删除服务器中的角色与删除某用户当前拥有的角色。
 *
 * [KookRole] 是 [KookGuildRole] 和 [KookMemberRole] 的标准接口，更多内容参考它们的说明。

 * @see KookGuildRole
 * @see KookMemberRole
 *
 * _**Note: 尚在实验阶段，未来可能会产生变更或被移除。**_
 *
 * _目前阶段Role相关的API内部不会构建缓存，而是直接使用相关API，直到 [#82](https://github.com/simple-robot/simbot-component-kook/issues/82) 的过程中逐步改善_
 *
 *
 * @author ForteScarlet
 */
@ExperimentalSimbotApi
public sealed interface KookRole : Role, DeleteSupport {

    /**
     * 得到当前Role对应的原始API返回类型 [Role][love.forte.simbot.kook.objects.Role].
     */
    public val sourceRole: love.forte.simbot.kook.objects.Role

    /**
     * 角色ID
     */
    override val id: ID

    /**
     * 角色名
     */
    override val name: String

    /**
     * 是否拥有管理权限。
     *
     * 当存在以下任意权限时会被判定为 `isAdmin = true`：
     *
     * | 比特位 | 权限 | 说明 |
     * |:----:|:----:|----|
     * | `0` | 管理员 | 拥有此权限会获得完整的管理权，包括绕开所有其他权限（包括频道权限）限制，属于危险权限。 |
     * | `1` | 管理服务器 | 拥有此权限的成员可以修改服务器名称和更换区域。 |
     * | `2` | 查看管理日志 | 拥有此权限的成员可以查看服务器的管理日志。 |
     * | `4` | 管理邀请 | 拥有该权限可以管理服务器的邀请 |
     * | `5` | 频道管理 | 拥有此权限的成员可以创建新的频道以及编辑或删除已存在的频道 |
     * | `6` | 踢出用户 |  |
     * | `7` | 封禁用户 |  |
     * | `8` | 管理自定义表情 |  |
     * | `9` | 修改服务器昵称 | 拥有此权限的用户可以更改他们的昵称。 |
     * | `10` | 管理角色权限 | 拥有此权限成员可以创建新的角色和编辑删除低于该角色的身份。 |
     * | `13` | 管理消息 | 拥有此权限的成员可以删除其他成员发出的消息和置顶消息。 |
     * | `16` | 语音管理 | 拥有此权限的成员可以把其他成员移动和踢出频道；但此类移动仅限于在该成员和被移动成员均有权限的频道之间进行。 |
     * | `26` | 修改他人昵称 | 拥有此权限的用户可以更改他人的昵称 |
     *
     * > 参考 [PermissionType] 中 [PermissionType.isAdmin] 为 `true` 的类型。
     *
     */
    override val isAdmin: Boolean get() = permissions.contains(ADMIN_PERMISSIONS, false)

    /**
     * 角色的权限值。
     *
     * Java中使用 [permissionsValue] 获取对应的[Int]类型权限值。
     */
    @get:JvmSynthetic
    public val permissions: Permissions

    /**
     * 角色的权限值。
     */
    @Api4J
    public val permissionsValue: Int get() = permissions.perm.toInt()

    /**
     * 根据实现类型分别代表删除当前服务器中的角色或删除某用户对应的角色。
     *
     * @throws KookApiException 可能在API请求过程中产生的任何异常，包括权限验证等
     */
    @JvmSynthetic
    override suspend fun delete(): Boolean


    public companion object {
        private val ADMIN_PERMISSIONS: Permissions = PermissionType.values().mapNotNull { pt ->
            pt.takeIf { it.isAdmin }?.let { Permissions(it) }
        }.reduce { p1, p2 -> p1 + p2 }
    }
}
