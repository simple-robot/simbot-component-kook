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
 * [添加服务器静音或闭麦](https://developer.kaiheila.cn/doc/http/guild#%E6%B7%BB%E5%8A%A0%E6%9C%8D%E5%8A%A1%E5%99%A8%E9%9D%99%E9%9F%B3%E6%88%96%E9%97%AD%E9%BA%A6)
 *
 * request method: POST
 *
 */
public class GuildMuteCreateRequest internal constructor(
    private val guildId: ID, private val userId: ID, private val type: Int
) : KookPostRequest<Unit>() {
    public companion object Key : BaseKookApiRequestKey("guild-mute", "create") {
        
        /**
         * 构造 [GuildMuteCreateRequest].
         * @param guildId 服务器id
         * @param userId 用户id
         * @param type 静音类型
         * - 麦克风闭麦: 1 ([GuildMuteType.TYPE_MICROPHONE])
         * - 耳机静音: 2 ([GuildMuteType.TYPE_EARPHONE])
         *
         * @see GuildMuteType
         */
        @JvmStatic
        public fun create(guildId: ID, userId: ID, type: Int): GuildMuteCreateRequest =
            GuildMuteCreateRequest(guildId, userId, type)
    }
    
    override val resultDeserializer: DeserializationStrategy<out Unit>
        get() = Unit.serializer()
    override val apiPaths: List<String>
        get() = apiPathList
    
    override fun createBody(): Any = Body(guildId, userId, type)
    
    @Serializable
    private data class Body(
        
        /** 服务器id */
        @SerialName("guild_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val guildId: ID,
        
        /** 用户id */
        @SerialName("user_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val userId: ID,
        
        /** 1代表麦克风闭麦，2代表耳机静音 */
        val type: Int
    )
}