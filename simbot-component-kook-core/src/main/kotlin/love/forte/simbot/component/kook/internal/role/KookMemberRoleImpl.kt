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

package love.forte.simbot.component.kook.internal.role

import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.component.kook.internal.KookGuildMemberImpl
import love.forte.simbot.component.kook.role.KookGuildRole
import love.forte.simbot.component.kook.role.KookMemberRole
import love.forte.simbot.component.kook.util.requestBy
import love.forte.simbot.kook.api.guild.role.GuildRoleRevokeRequest
import love.forte.simbot.kook.objects.Permissions
import love.forte.simbot.kook.objects.Role

/**
 *
 * @author ForteScarlet
 */
@OptIn(ExperimentalSimbotApi::class)
internal class KookMemberRoleImpl(
    override val member: KookGuildMemberImpl,
    private val _role: KookGuildRoleImpl,
) : KookMemberRole {
    override val sourceRole: Role get() = _role.sourceRole
    override val id: ID get() = sourceRole.roleId
    override val name: String get() = sourceRole.name
    override val permissions: Permissions get() = sourceRole.permissions
    override val guildRole: KookGuildRole get() = _role

    override suspend fun delete(): Boolean {
        val api = GuildRoleRevokeRequest.create(
            member.guildInternal.id,
            sourceRole.roleId,
            member.id
        )

        val deleted = api.requestBy(member.bot)

        if (deleted.isHttpSuccess || deleted.isSuccess) {
            return true
        }

        if (deleted.httpStatusCode == 404) {
            return false
        }

        // try to deserialize
        deleted.parseDataOrThrow(member.bot.sourceBot.configuration.decoder, api.resultDeserializer)
        return true
    }
}
