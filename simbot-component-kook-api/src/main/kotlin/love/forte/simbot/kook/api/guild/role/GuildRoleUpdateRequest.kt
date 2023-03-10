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
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest
import love.forte.simbot.kook.objects.PermissionType
import love.forte.simbot.kook.objects.Permissions
import love.forte.simbot.kook.objects.Role
import love.forte.simbot.kook.objects.impl.RoleImpl
import love.forte.simbot.kook.util.BooleanToIntSerializer


/**
 *
 * [更新服务器角色](https://developer.kaiheila.cn/doc/http/guild-role#更新服务器角色)
 *
 * method `POST`
 *
 * `/api/v3/guild-role/update`
 *
 */
public class GuildRoleUpdateRequest internal constructor(override val body: Body) : KookPostRequest<Role>() {
    public companion object Key : BaseKookApiRequestKey("guild-role", "update") {

        /**
         * 使用最基础的 [Body] 参数构造 [GuildRoleUpdateRequest].
         *
         * @param body 构造参数
         */
        @JvmStatic
        public fun create(body: Body): GuildRoleUpdateRequest = GuildRoleUpdateRequest(body)

        /**
         *
         * 使用与 [Body] 基本一致的参数构造 [GuildRoleUpdateRequest].
         *
         * @param roleId 角色的id
         * @param name 角色的名称
         * @param color 角色的色值0x000000 - 0xFFFFFF
         * @param position 顺序，值越小载靠前
         * @param isHoist 是否把该角色的用户在用户列表排到前面
         * @param isMentionable 该角色是否可以被提及
         * @param permissionsValue 权限,参见 [权限说明](https://developer.kaiheila.cn/doc/http/guild-role#权限说明)
         *
         *
         */
        @JvmStatic
        public fun create(
            guildId: ID,
            roleId: ID,
            name: String? = null,
            color: Int? = null,
            position: Int? = null,
            isHoist: Boolean? = null,
            isMentionable: Boolean? = null,
            permissionsValue: Int? = null,
        ): GuildRoleUpdateRequest =
            GuildRoleUpdateRequest(
                Body(
                    guildId = guildId,
                    roleId = roleId,
                    name,
                    color,
                    position,
                    isHoist,
                    isMentionable,
                    permissionsValue
                )
            )

        /**
         *
         * 使用与 [Body] 基本一致的参数构造 [GuildRoleUpdateRequest].
         *
         * @param roleId 角色的id
         * @param name 角色的名称
         * @param color 角色的色值0x000000 - 0xFFFFFF
         * @param position 顺序，值越小载靠前
         * @param isHoist 是否把该角色的用户在用户列表排到前面
         * @param isMentionable 该角色是否可以被提及
         * @param permissions 权限,参见 [权限说明](https://developer.kaiheila.cn/doc/http/guild-role#权限说明)
         *
         */
        @JvmStatic
        public fun create(
            guildId: ID,
            roleId: ID,
            name: String? = null,
            color: Int? = null,
            position: Int? = null,
            isHoist: Boolean? = null,
            isMentionable: Boolean? = null,
            permissions: Permissions? = null,
        ): GuildRoleUpdateRequest =
            create(Body(guildId = guildId, roleId = roleId, name, color, position, isHoist, isMentionable, permissions))


    }

    override val resultDeserializer: DeserializationStrategy<out Role>
        get() = RoleImpl.serializer()

    override val apiPaths: List<String>
        get() = apiPathList

    override fun createBody(): Body = body


    /**
     * 用于 [GuildRoleUpdateRequest] 的请求体类型。
     */
    @Serializable
    public data class Body(
        /** 频道服务器的id */
        @SerialName("guild_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val guildId: ID,
        /** 角色的id */
        @SerialName("role_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val roleId: ID,

        /** 角色的名称 */
        val name: String? = null,

        /** 角色的色值0x000000 - 0xFFFFFF */
        val color: Int? = null,

        /** 顺序，值越小载靠前 */
        val position: Int? = null,

        /** 只能为0或者1，是否把该角色的用户在用户列表排到前面 */
        @SerialName("hoist") @Serializable(BooleanToIntSerializer::class) val isHoist: Boolean? = null,

        /** 只能为0或者1，该角色是否可以被提及 */
        @SerialName("mentionable") @Serializable(BooleanToIntSerializer::class) val isMentionable: Boolean? = null,
        /**
         * 权限,参见 [权限说明](https://developer.kaiheila.cn/doc/http/guild-role#权限说明)
         *
         * @see Permissions
         * @see PermissionType
         */
        val permissions: Permissions? = null,
    ) {

        /**
         * @param permissionsValue 权限值
         */
        public constructor(
            guildId: ID,
            roleId: ID,
            name: String? = null,
            color: Int? = null,
            position: Int? = null,
            isHoist: Boolean? = null,
            isMentionable: Boolean? = null,
            permissionsValue: Int? = null,
        ) : this(
            guildId,
            roleId,
            name,
            color,
            position,
            isHoist,
            isMentionable,
            permissionsValue?.let { Permissions(it.toUInt()) }
        )
    }

}


