/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kaiheila 的一部分。
 *
 *  simbot-component-kaiheila 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kaiheila 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

package love.forte.simbot.kaiheila.api.guild.role

import kotlinx.serialization.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.api.*


/**
 *
 * [删除用户角色](https://developer.kaiheila.cn/doc/http/guild-role#删除用户角色)
 *
 * method `POST`
 *
 * `/api/v3/guild-role/revoke`
 *
 */
public class GuildRoleRevokeRequest(
    /** 服务器ID */
    private val guildId: ID,
    /** 角色ID */
    private val roleId: ID,
    /** 用户ID */
    private val userId: ID,
) : KaiheilaPostRequest<UserRoleOperated>() {
    public companion object Key : BaseApiRequestKey("guild-role", "revoke")

    override val resultDeserializer: DeserializationStrategy<out UserRoleOperated>
        get() = UserRoleOperated.serializer()

    override val apiPaths: List<String>
        get() = apiPathList

    override fun createBody(): Any = Body(guildId, roleId, userId)

    @Serializable
    private data class Body(
        @SerialName("guild_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val guildId: ID,
        @SerialName("role_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val roleId: ID,
        @SerialName("user_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val userId: ID
    )
}



