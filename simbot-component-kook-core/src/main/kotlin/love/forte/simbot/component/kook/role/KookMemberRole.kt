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

import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.component.kook.KookGuildMember
import love.forte.simbot.kook.api.KookApiException

/**
 * 代表一个频道中某个成员所拥有的某个角色。
 *
 * ## 查询
 *
 * [KookMemberRole] 是通过 [KookGuildMember.roles] 进行列表查询得到的，
 * 或者通过下述的**赋予**来 _"构建"_ 实例。
 *
 * ## 移除
 *
 * [KookMemberRole] 是一个附着于某个成员上的角色，
 * 当bot权限足够时可以将其从当前成员中 [移除][delete]。
 *
 * ## 赋予
 *
 * 可以将一个 [KookGuildRole] 赋予([KookGuildRole.grantTo]) 给一个指定的成员。
 * 当赋予成功后将会得到一个 [KookMemberRole] 实例。这也是一种 _"构建"_ [KookMemberRole] 实例的方式。
 *
 * <hr />
 *
 * > 统一性说明参考 [KookRole]
 *
 * @see KookRole
 *
 * @author ForteScarlet
 */
@ExperimentalSimbotApi
public interface KookMemberRole : KookRole {

    /**
     * 此角色所属用户
     */
    public val member: KookGuildMember

    /**
     * 得到当前角色所代表的服务器角色信息。
     */
    public val guildRole: KookGuildRole

    /**
     * 将当前角色从当前用户上移除。
     *
     * 移除后不应再使用其他API，可能会产生任何预期外的问题。
     *
     * @throws KookApiException API请求过程中产生的任何异常
     */
    @JvmSynthetic
    override suspend fun delete(): Boolean

}
