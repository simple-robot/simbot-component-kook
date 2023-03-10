/*
 * Copyright (c) 2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
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
