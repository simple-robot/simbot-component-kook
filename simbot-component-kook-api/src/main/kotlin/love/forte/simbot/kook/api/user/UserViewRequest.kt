/*
 * Copyright (c) 2022-2023. ForteScarlet.
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

package love.forte.simbot.kook.api.user

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import love.forte.simbot.ID
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookGetRequest
import love.forte.simbot.kook.objects.User
import love.forte.simbot.kook.objects.UserImpl
import love.forte.simbot.literal

/**
 * 查询用户信息
 * @author ForteScarlet
 */
public class UserViewRequest internal constructor(
    private val userId: ID,
    private val guildId: ID,
) : KookGetRequest<User>() {
    public companion object Key : BaseKookApiRequestKey("user", "view") {
    
        /**
         * 构造 [UserViewRequest].
         *
         * @param userId 用户id
         * @param guildId 频道服务器id
         *
         */
        @JvmStatic
        public fun create(userId: ID, guildId: ID): UserViewRequest = UserViewRequest(userId, guildId)
        
    }

    override val resultDeserializer: DeserializationStrategy<User>
        get() = UserImpl.serializer()

    override val apiPaths: List<String>
        get() = apiPathList

    override fun ParametersBuilder.buildParameters() {
        append("user_id", userId.literal)
        append("guild_id", guildId.literal)
    }
}
