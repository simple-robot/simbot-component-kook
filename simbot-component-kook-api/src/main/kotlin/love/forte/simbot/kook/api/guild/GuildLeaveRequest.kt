/*
 * Copyright (c) 2021-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
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
 * [离开服务器](https://developer.kaiheila.cn/doc/http/guild#%E7%A6%BB%E5%BC%80%E6%9C%8D%E5%8A%A1%E5%99%A8)
 *
 * request method: POST
 *
 */
public class GuildLeaveRequest internal constructor(private val guildId: ID) : KookPostRequest<Unit>() {
    public companion object Key : BaseKookApiRequestKey("guild", "leave") {
    
        /**
         * 构造 [GuildLeaveRequest].
         * @param guildId 目标频道服务器
         */
        @JvmStatic
        public fun create(guildId: ID): GuildLeaveRequest = GuildLeaveRequest(guildId)
    }

    override val resultDeserializer: DeserializationStrategy<out Unit>
        get() = Unit.serializer()

    override val apiPaths: List<String>
        get() = apiPathList

    override val body: Any = Body(guildId)

    override fun createBody(): Any = Body(guildId)

    @Serializable
    private data class Body(@SerialName("guild_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val guildId: ID)

}







