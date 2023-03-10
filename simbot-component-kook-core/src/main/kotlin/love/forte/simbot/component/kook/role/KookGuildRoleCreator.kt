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
import love.forte.simbot.component.kook.KookGuild
import love.forte.simbot.kook.api.KookApiException

/**
 * 服务器频道角色创建器。提供 Kotlin DSL风格的API和Java的惯用API。
 *
 * 通过 [KookGuild.roleCreator] 构建。
 *
 * 对于创建角色来说，[API](https://developer.kookapp.cn/doc/http/guild-role#%E5%88%9B%E5%BB%BA%E6%9C%8D%E5%8A%A1%E5%99%A8%E8%A7%92%E8%89%B2)
 * 仅提供了 `name` 属性。如果希望指定其他属性，考虑在创建后使用 [KookGuildRole.updater] 进行属性更新。
 *
 */
@ExperimentalSimbotApi
public interface KookGuildRoleCreator {

    //region DSL API
    /**
     * 角色名称。如果不写，则为"新角色"
     */
    public var name: String?
    //endregion


    /**
     * 角色名称。如果不写，则为"新角色"
     * @see name
     */
    public fun name(value: String): KookGuildRoleCreator = also { name = value }

    /**
     * 创建一个新角色。
     *
     * @throws KookApiException 任何在API请求过程中产生的异常
     *
     * @return 创建的角色
     */
    @JvmBlocking
    @JvmAsync
    public suspend fun create(): KookGuildRole
}



/**
 * 使用 DSL 风格API创建一个 [KookGuildRole].
 *
 * ```kotlin
 * val newRole = guild.createRole {
 *    name = "武旦"
 * }
 * ```
 *
 * @see KookGuild.roleCreator
 */
@ExperimentalSimbotApi
public suspend inline fun KookGuild.createRole(block: KookGuildRoleCreator.() -> Unit): KookGuildRole {
    return roleCreator().also(block).create()
}
