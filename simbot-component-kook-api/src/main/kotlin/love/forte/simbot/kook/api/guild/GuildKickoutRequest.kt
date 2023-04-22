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

package love.forte.simbot.kook.api.guild

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.ID
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest


/**
 * [踢出服务器](https://developer.kaiheila.cn/doc/http/guild#%E8%B8%A2%E5%87%BA%E6%9C%8D%E5%8A%A1%E5%99%A8)
 *
 * request method: POST
 *
 */
public class GuildKickoutRequest internal constructor(
    guildId: ID,
    targetId: ID,
) : KookPostRequest<Unit>() {
    public companion object Key : BaseKookApiRequestKey("guild", "kickout") {
        
        /**
         * 构造 [GuildKickoutRequest]
         *
         * @param guildId 频道服务器ID
         * @param targetId 目标用户ID
         */
        @JvmStatic
        public fun create(guildId: ID, targetId: ID): GuildKickoutRequest = GuildKickoutRequest(guildId, targetId)
    }
    
    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()
    override val apiPaths: List<String>
        get() = apiPathList
    
    override val body: Any = Body(guildId, targetId)
    
    @Serializable
    private data class Body(
        @SerialName("guild_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val guildId: ID,
        @SerialName("target_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val targetId: ID,
    )
    
}
