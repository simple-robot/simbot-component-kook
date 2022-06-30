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

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import love.forte.simbot.ID
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookApiResult
import love.forte.simbot.kook.api.KookGetRequest
import love.forte.simbot.kook.objects.Role
import love.forte.simbot.kook.objects.impl.RoleImpl
import love.forte.simbot.literal


/**
 *
 * [获取服务器角色列表](https://developer.kook.cn/doc/http/guild-role#获取服务器角色列表)
 *
 * method: `GET`
 *
 * `/api/v3/guild-role/list`
 *
 * @param guildId 服务器的id
 */
public class GuildRoleListRequest(public val guildId: ID) :
    KookGetRequest<KookApiResult.ListData<Role>>() {
    public companion object Key : BaseKookApiRequestKey("guild-role", "list") {
        private val serializer = KookApiResult.ListData.serializer(RoleImpl.serializer())
    }

    override val resultDeserializer: DeserializationStrategy<out KookApiResult.ListData<Role>>
        get() = serializer

    override val apiPaths: List<String>
        get() = apiPathList

    override fun ParametersBuilder.buildParameters() {
        append("guild_id", guildId.literal)
    }

}


