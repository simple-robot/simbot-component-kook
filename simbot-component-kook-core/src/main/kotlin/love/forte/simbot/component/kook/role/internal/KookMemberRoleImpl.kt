/*
 * Copyright (c) 2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook,
 * If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.component.kook.role.internal

import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.component.kook.KookMember
import love.forte.simbot.component.kook.bot.internal.KookBotImpl
import love.forte.simbot.component.kook.role.KookMemberRole
import love.forte.simbot.component.kook.util.requestResultBy
import love.forte.simbot.kook.api.role.RevokeGuildRoleApi
import love.forte.simbot.kook.objects.Permissions
import love.forte.simbot.kook.objects.Role

/**
 *
 * @author ForteScarlet
 */
@OptIn(ExperimentalSimbotApi::class)
internal class KookMemberRoleImpl(
    private val baseBot: KookBotImpl,
    override val member: KookMember,
    private val guildId: String,
    override val guildRole: KookGuildRoleImpl,
) : KookMemberRole {
    override val id: ID get() = source.roleId.ID
    override val source: Role get() = guildRole.source
    override val name: String get() = source.name
    override val permissions: Permissions get() = source.permissions

    override suspend fun delete(): Boolean {
        val api = RevokeGuildRoleApi.create(
            guildId = guildId,
            userId = member.source.id,
            roleId = source.roleId,
        )

        val deleted = api.requestResultBy(baseBot)

        if (deleted.isHttpSuccess || deleted.isSuccess) {
            return true
        }

        if (deleted.httpStatusCode == 404) {
            return false
        }


        // try to deserialize
//        deleted.parseDataOrThrow(KookBotImpl.DEFAULT_JSON, api.resultDeserializer)
        return true
    }
}
