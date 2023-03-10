/*
 * Copyright (c) 2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.api.guild.emoji

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.kook.api.*
import love.forte.simbot.kook.objects.User
import love.forte.simbot.kook.objects.UserImpl
import love.forte.simbot.literal


/**
 *
 * [获取服务器表情列表](https://developer.kookapp.cn/doc/http/guild-emoji#%E8%8E%B7%E5%8F%96%E6%9C%8D%E5%8A%A1%E5%99%A8%E8%A1%A8%E6%83%85%E5%88%97%E8%A1%A8)
 *
 * @author ForteScarlet
 */
public class GuildEmojiListRequest internal constructor(
    private val guildId: ID,
    private val pageRequest: PageRequestParameters?,
) : KookGetRequest<KookApiResult.ListData<GuildEmojiData>>() {
    public companion object Key : BaseKookApiRequestKey("guild-emoji", "list") {
        private val serializer = KookApiResult.ListData.serializer(GuildEmojiDataImpl.serializer())
    
        /**
         * 构建 [GuildEmojiListRequest].
         * @param guildId 频道服务器ID
         * @param pageRequest 分页信息
         */
        @JvmStatic
        @JvmOverloads
        public fun create(guildId: ID, pageRequest: PageRequestParameters? = null): GuildEmojiListRequest
            = GuildEmojiListRequest(guildId, pageRequest)
    }
    
    override val resultDeserializer: DeserializationStrategy<out KookApiResult.ListData<GuildEmojiData>>
        get() = serializer
    
    override val apiPaths: List<String>
        get() = apiPathList
    
    override fun ParametersBuilder.buildParameters() {
        append("guild_id", guildId.literal)
        pageRequest?.appendTo(this)
    }
    
}


public interface GuildEmojiData {
    
    /**
     * 表情的 ID
     */
    public val id: ID
    
    /**
     * 表情的名称
     */
    public val name: String
    
    /**
     * 上传用户。
     */
    public val userInfo: User/*
    "user_info": {
          "id": "10000",
          "username": "用户名",
          "identify_num": "0123",
          "online": true,
          "os": "Websocket",
          "status": 1,
          "avatar": "https://XXXXXXXXXXX"
        }
     */
}

@Serializable
internal data class GuildEmojiDataImpl(
    override val id: CharSequenceID,
    override val name: String,
    @SerialName("user_info")
    override val userInfo: UserImpl,
) : GuildEmojiData
