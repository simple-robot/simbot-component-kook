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

import io.ktor.http.*
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.component.kook.KookGuildMember
import love.forte.simbot.component.kook.internal.KookComponentGuildBotImpl
import love.forte.simbot.component.kook.internal.KookGuildImpl
import love.forte.simbot.component.kook.internal.KookGuildMemberImpl
import love.forte.simbot.component.kook.role.KookGuildRole
import love.forte.simbot.component.kook.role.KookGuildRoleUpdater
import love.forte.simbot.component.kook.role.KookMemberRole
import love.forte.simbot.component.kook.util.requestBy
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.kook.api.guild.role.GuildRoleDeleteRequest
import love.forte.simbot.kook.api.guild.role.GuildRoleGrantRequest
import love.forte.simbot.kook.api.guild.role.GuildRoleUpdateRequest
import love.forte.simbot.kook.objects.Permissions
import love.forte.simbot.kook.objects.Role

/**
 *
 * @author ForteScarlet
 */
@OptIn(ExperimentalSimbotApi::class)
internal class KookGuildRoleImpl(
    override val guild: KookGuildImpl,
    @Volatile override var sourceRole: Role
) : KookGuildRole {
    private val _bot: KookComponentGuildBotImpl get() = guild.bot

    override val id: ID
        get() = sourceRole.roleId

    override val name: String
        get() = sourceRole.name

    override val permissions: Permissions
        get() = sourceRole.permissions


    override fun toString(): String = "KookGuildRoleImpl(guild=$guild, source=$sourceRole)"

    /**
     * 赋予某用户角色权限
     */
    override suspend fun grantTo(memberId: ID): KookMemberRole {
        // 寻找此成员
        val guildId = guild.id
        val member =
            guild.internalMember(memberId) ?: throw NoSuchElementException("member(id=$memberId) in guild(id=$guildId)")

        return grantTo(member)
    }

    override suspend fun grantTo(member: KookGuildMember): KookMemberRole {
        val guildId = guild.id
        val impl = member as? KookGuildMemberImpl ?: kotlin.run {
            val memberId = member.id
            guild.internalMember(memberId) ?: throw NoSuchElementException("member(id=$memberId) in guild(id=$guildId)")
        }
        val role = sourceRole
        GuildRoleGrantRequest.create(
            guildId,
            impl.id,
            role.roleId
        ).requestDataBy(_bot)

        return KookMemberRoleImpl(impl, this)
    }

    /**
     * 删除当前频道服务器中的角色
     */
    override suspend fun delete(): Boolean {
        val api = GuildRoleDeleteRequest.create(guild.id, sourceRole.roleId)
        val deleted = api
            .requestBy(_bot)

        if (deleted.isHttpSuccess || deleted.isSuccess) {
            return true
        }

        // 妹找着啊
        if (deleted.httpStatusCode == 404) {
            return false
        }

        // throw.
        deleted.parseDataOrThrow(_bot.sourceBot.configuration.decoder, api.resultDeserializer)
        return true
    }

    override fun updater(): KookGuildRoleUpdater = KookGuildRoleUpdaterImpl(_bot, this)

    internal fun updateRole(role: Role) {
        sourceRole = role
    }

}


@OptIn(ExperimentalSimbotApi::class)
private class KookGuildRoleUpdaterImpl(
    private val _bot: KookComponentGuildBotImpl,
    private val role: KookGuildRoleImpl
) : KookGuildRoleUpdater {
    override var name: String? = null
    override var color: Int? = null
    override var isHoist: Boolean? = null
    override var isMentionable: Boolean? = null
    override var permissions: Permissions? = null

    override suspend fun update() {
        fun ifAllNull(vararg values: Any?): Boolean {
            return values.all { it == null }
        }
        if (ifAllNull(name, color, isHoist, isMentionable, permissions)) {
            // all null
            return
        }
        val updated = GuildRoleUpdateRequest.create(
            guildId = role.guild.id,
            roleId = role.id,
            name = name,
            color = color,
            isHoist = isHoist,
            isMentionable = isMentionable,
            permissions = permissions
        ).requestDataBy(_bot)

        role.updateRole(updated)
    }
}
