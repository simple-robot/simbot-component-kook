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
