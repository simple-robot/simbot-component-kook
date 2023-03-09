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

import love.forte.plugin.suspendtrans.annotation.JvmAsync
import love.forte.plugin.suspendtrans.annotation.JvmBlocking
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.component.kook.KookGuild
import love.forte.simbot.kook.api.KookApiException
import love.forte.simbot.kook.objects.PermissionType
import love.forte.simbot.kook.objects.Permissions

/**
 * 代表一个频道服务器所拥有的角色。
 *
 * @see KookRole
 *
 * @author ForteScarlet
 */
@ExperimentalSimbotApi
public interface KookGuildRole : KookRole {

    /**
     * 当前角色所属的频道服务器。
     */
    public val guild: KookGuild

    /**
     * 将当前角色赋予给指定 [memberId] 的用户。
     *
     * @throws KookApiException API请求过程中产生的任何异常
     */
    public suspend fun grantTo(memberId: ID): KookMemberRole

    /**
     * 删除当前频道中对应的角色。
     *
     * 删除一个 [KookGuildRole] 后不应再进行其他操作。组件目前不会进行过多判断，但是可能会造成任何预期外的异常。
     *
     * @throws KookApiException API请求过程中产生的任何异常
     */
    @JvmSynthetic
    override suspend fun delete(): Boolean

    /**
     * 构建一个用于更新当前角色信息的 [KookGuildRoleUpdater].
     */
    public fun updater(): KookGuildRoleUpdater

}


/**
 * 用于修改 [KookGuildRole] 的更新器，提供Kotlin DSL风格的API和Java的惯用API。
 *
 * @author ForteScarlet
 *
 */
@ExperimentalSimbotApi
public interface KookGuildRoleUpdater {
    //region DSL API
    /**
     * 名称
     */
    public var name: String?

    /**
     * 颜色.
     */
    public var color: Int?

    /**
     * 是否把该角色的用户在用户列表排到前面
     */
    public var isHoist: Boolean?

    /**
     * 该角色是否可以被提及
     */
    public var isMentionable: Boolean?

    /**
     * 权限
     */
    @get:JvmSynthetic
    @set:JvmSynthetic
    public var permissions: Permissions?
    //endregion


    //region Java API
    /**
     * 名称
     */
    public fun name(value: String): KookGuildRoleUpdater = also { name = value }

    /**
     * 颜色.
     */
    public fun color(value: Int): KookGuildRoleUpdater = also { color = value }

    /**
     * 是否把该角色的用户在用户列表排到前面
     */
    public fun isHoist(value: Boolean): KookGuildRoleUpdater = also { isHoist = value }

    /**
     * 该角色是否可以被提及
     */
    public fun isMentionable(value: Boolean): KookGuildRoleUpdater = also { isMentionable = value }

    /**
     * 权限
     */
    public fun permissions(value: PermissionType): KookGuildRoleUpdater = also { permissions = Permissions(value) }

    /**
     * 权限
     *
     * _Note: [permissionsValue] 会被转化为 [Kotlin UInt][UInt]_
     */
    public fun permissions(permissionsValue: Int): KookGuildRoleUpdater = also { permissions = Permissions(permissionsValue.toUInt()) }
    //endregion

    /**
     * 根据当前配置的内容发起一次 Guild Role 更新。
     *
     * 如果更新成功，更新后的信息将会生效并刷新到当前对象中。
     *
     */
    @JvmAsync
    @JvmBlocking
    public suspend fun update()

}
