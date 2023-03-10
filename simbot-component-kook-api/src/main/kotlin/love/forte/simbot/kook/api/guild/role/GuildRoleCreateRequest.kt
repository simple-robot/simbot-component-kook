/*
 * Copyright (c) 2021-2023. ForteScarlet.
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

package love.forte.simbot.kook.api.guild.role

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.ID
import love.forte.simbot.LongID
import love.forte.simbot.definition.Role
import love.forte.simbot.kook.api.ApiResultType
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest
import love.forte.simbot.kook.objects.PermissionType
import love.forte.simbot.kook.objects.Permissions
import love.forte.simbot.literal

/**
 *
 * [创建服务器角色](https://developer.kaiheila.cn/doc/http/guild-role#创建服务器角色)
 *
 * guild-role/create
 *
 * @author ForteScarlet
 */
public class GuildRoleCreateRequest internal constructor(
    private val guildId: ID,
    private val name: String? = null,
) : KookPostRequest<GuildRoleCreated>() {
    public companion object Key : BaseKookApiRequestKey("guild-role", "create") {

        /**
         * 构建 [GuildRoleCreateRequest]
         * @param guildId 目标频道
         * @param name 角色名
         */
        @JvmStatic
        @JvmOverloads
        public fun create(guildId: ID, name: String? = null): GuildRoleCreateRequest =
            GuildRoleCreateRequest(guildId, name)

    }

    override val resultDeserializer: DeserializationStrategy<out GuildRoleCreated>
        get() = GuildRoleCreated.serializer()

    override val apiPaths: List<String>
        get() = apiPathList

    override fun createBody(): Any = Body(guildId.literal, name)

    @Serializable
    private data class Body(
        @SerialName("guild_id")
        val guildId: String,
        val name: String? = null
    )
}


/**
 * API [GuildRoleCreateRequest] 的响应值.
 */
@Suppress("MemberVisibilityCanBePrivate")
@Serializable
public data class GuildRoleCreated @ApiResultType constructor(
    /**
     * 角色的id
     */
    @SerialName("role_id")
    override val roleId: LongID,
    /**
     * 角色的名称
     */
    override val name: String,
    /**
     * 角色的色值0x000000 - 0xFFFFFF
     */
    override val color: Int,
    /**
     * 顺序，值越小载靠前
     */
    override val position: Int,
    /**
     * 只能为0或者1，是否把该角色的用户在用户列表排到前面
     */
    @SerialName("hoist")
    override val hoist: Int,
    /**
     * 只能为0或者1，该角色是否可以被提及
     */
    override val mentionable: Int,

    /**
     * 权限信息。Java中可以通过 [permissionsValue] 得到int类型的字面值。
     */
    @SerialName("permissions")
    public override val permissions: Permissions,
) : Role, love.forte.simbot.kook.objects.Role {

    /**
     * 此处的管理员权限判断为完全的 [管理员][PermissionType], 如果你想要更细致的判断，请通过 [PermissionType] 自行处理。
     */
    override val isAdmin: Boolean
        get() = PermissionType.ADMIN in permissions

    override val id: LongID
        get() = roleId

}
