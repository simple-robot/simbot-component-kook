/*
 *
 *  * Copyright (c) 2021. ForteScarlet All rights reserved.
 *  * Project  simple-robot
 *  * File     MiraiAvatar.kt
 *  *
 *  * You can contact the author through the following channels:
 *  * github https://github.com/ForteScarlet
 *  * gitee  https://gitee.com/ForteScarlet
 *  * email  ForteScarlet@163.com
 *  * QQ     1149159218
 *
 */

@file:Suppress("unused")

package love.forte.simbot.kaiheila.objects

import kotlinx.serialization.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.api.*
import love.forte.simbot.kaiheila.objects.impl.*


/**
 *
 * 开黑啦objects - [角色Role](https://developer.kaiheila.cn/doc/objects#%E8%A7%92%E8%89%B2Role)
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
public interface Role : KhlObjects, Comparable<Role> {

    /** 角色id */
    public val roleId: IntID

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
        public val objectSerializer: KSerializer<out KaiheilaApiResult.Obj<out Role>> = KaiheilaApiResult.Obj.serializer(RoleImpl.serializer())
        public val emptySortSerializer: KSerializer<out KaiheilaApiResult.List<out Role>> = KaiheilaApiResult.List.serializer(RoleImpl.serializer())
    }

}






