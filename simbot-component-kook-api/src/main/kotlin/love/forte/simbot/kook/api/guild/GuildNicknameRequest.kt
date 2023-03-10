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
 *
 * [修改服务器中用户的昵称](https://developer.kaiheila.cn/doc/http/guild#%E4%BF%AE%E6%94%B9%E6%9C%8D%E5%8A%A1%E5%99%A8%E4%B8%AD%E7%94%A8%E6%88%B7%E7%9A%84%E6%98%B5%E7%A7%B0)
 *
 * request method: POST
 *
 * @author ForteScarlet
 */
public class GuildNicknameRequest internal constructor(
    guildId: ID,
    userId: ID? = null,
    nickname: String? = null,
) : KookPostRequest<Unit>() {
    public companion object Key : BaseKookApiRequestKey("guild", "nickname") {
    
        /**
         * 构造 [GuildNicknameRequest].
         * @param guildId 频道服务器ID
         * @param userId 要修改昵称的目标用户 ID，不传则修改当前登陆用户的昵称
         * @param nickname 昵称，2 - 64 长度，不传则清空昵称
         *
         */
        @JvmStatic
        @JvmOverloads
        public fun create(guildId: ID, userId: ID? = null, nickname: String? = null): GuildNicknameRequest =
            GuildNicknameRequest(guildId, userId, nickname)
        
        /**
         * 构造 [GuildNicknameRequest], 修改当前登录用户的昵称
         * @param guildId 频道服务器ID
         * @param nickname 昵称，2 - 64 长度，不传则清空昵称
         *
         */
        @JvmStatic
        public fun create(guildId: ID, nickname: String? = null): GuildNicknameRequest =
            GuildNicknameRequest(guildId, null, nickname)
    }

    init {
        if (nickname != null) {
            require(nickname.length in 2..64) { "The length of 'nickname' must be between 2 and 64" }
        }
    }

    override val resultDeserializer: DeserializationStrategy<out Unit>
        get() = Unit.serializer()
    override val apiPaths: List<String>
        get() = apiPathList

    override val body: Any = Body(guildId, nickname, userId)


    /** Request Body */
    @Serializable
    private data class Body(
        @SerialName("guild_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val guildId: ID,
        val nickname: String?,
        @SerialName("user_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val userId: ID?,
    )

}


