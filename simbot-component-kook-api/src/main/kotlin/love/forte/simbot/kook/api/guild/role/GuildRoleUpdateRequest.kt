/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
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
    @Deprecated(
        "Use GuildRoleUpdateRequest.create(...)",
        ReplaceWith("GuildRoleUpdateRequest.create(roleId, name, color, position, isHoist, isMentionable, permissions)")
    )
    public constructor(
        roleId: ID,
        name: String,
        color: Int,
        position: Int,
        isHoist: Boolean,
        isMentionable: Boolean,
        permissions: Int,
    ) : this(Body(roleId, name, color, position, isHoist, isMentionable, permissions))
    
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
         * @param permissions 权限,参见 [权限说明](https://developer.kaiheila.cn/doc/http/guild-role#权限说明)
         *
         *
         */
        @JvmStatic
        public fun create(
            roleId: ID,
            name: String,
            color: Int,
            position: Int,
            isHoist: Boolean,
            isMentionable: Boolean,
            permissions: Int,
        ): GuildRoleUpdateRequest =
            GuildRoleUpdateRequest(Body(roleId, name, color, position, isHoist, isMentionable, permissions))
        
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
            roleId: ID,
            name: String,
            color: Int,
            position: Int,
            isHoist: Boolean,
            isMentionable: Boolean,
            permissions: Permissions,
        ): GuildRoleUpdateRequest =
            create(roleId, name, color, position, isHoist, isMentionable, permissions.perm.toInt())
        
        
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
        /** 角色的id */
        @SerialName("role_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val roleId: ID,
        
        /** 角色的名称 */
        val name: String,
        
        /** 角色的色值0x000000 - 0xFFFFFF */
        val color: Int,
        
        /** 顺序，值越小载靠前 */
        val position: Int,
        
        /** 只能为0或者1，是否把该角色的用户在用户列表排到前面 */
        @SerialName("hoist") @Serializable(BooleanToIntSerializer::class) val isHoist: Boolean,
        
        /** 只能为0或者1，该角色是否可以被提及 */
        @SerialName("mentionable") @Serializable(BooleanToIntSerializer::class) val isMentionable: Boolean,
        /**
         * 权限,参见 [权限说明](https://developer.kaiheila.cn/doc/http/guild-role#权限说明)
         *
         * @see Permissions
         * @see PermissionType
         */
        val permissions: Int,
    )
    
}


