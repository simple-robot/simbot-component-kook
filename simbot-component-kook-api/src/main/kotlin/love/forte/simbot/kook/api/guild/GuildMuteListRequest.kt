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

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.kook.api.ApiResultType
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookGetRequest
import love.forte.simbot.literal


/**
 *
 * [服务器静音闭麦列表](https://developer.kaiheila.cn/doc/http/guild#%E6%9C%8D%E5%8A%A1%E5%99%A8%E9%9D%99%E9%9F%B3%E9%97%AD%E9%BA%A6%E5%88%97%E8%A1%A8)
 *
 * request method: GET
 *
 */
public class GuildMuteListRequest(private val guildId: ID) : KookGetRequest<GuildMuteList>() {
    public companion object Key : BaseKookApiRequestKey("guild-mute", "list") {
    
        /**
         * 构造 [GuildMuteListRequest].
         * @param guildId 频道服务器id
         */
        @JvmStatic
        public fun create(guildId: ID): GuildMuteListRequest =
            GuildMuteListRequest(guildId)
    }

    override val resultDeserializer: DeserializationStrategy<GuildMuteList>
        get() = GuildMuteList.serializer()

    override val apiPaths: List<String>
        get() = apiPathList

    override fun ParametersBuilder.buildParameters() {
        append("guild_id", guildId.literal)
        append("return_type", "detail")
    }


}


/**
 *
 * 通过 [服务器静音列表](https://developer.kaiheila.cn/doc/http/guild#%E6%9C%8D%E5%8A%A1%E5%99%A8%E9%9D%99%E9%9F%B3%E9%97%AD%E9%BA%A6%E5%88%97%E8%A1%A8)
 * 的 `return_type = 'detail'` 得到的结果.
 *
 */
@Serializable
public data class GuildMuteList @ApiResultType constructor(
    /**
     * 麦克风静音信息。
     */
    val mic: Mic,

    /**
     * 耳机静音信息。
     */
    val headset: Headset,
) {

    /**
     * 麦克风静音信息。
     */
    @Serializable
    public data class Mic @ApiResultType constructor(
        override val type: Int = 1,
        @SerialName("user_ids")
        override val userIds: Set<CharSequenceID> = emptySet(),
    ) : GuildMuteResult

    /**
     * 耳机静音信息。
     */
    @Serializable
    public data class Headset @ApiResultType constructor(
        override val type: Int = 2,
        @SerialName("user_ids")
        override val userIds: Set<CharSequenceID> = emptySet(),
    ) : GuildMuteResult

}


/**
 * Mute响应值里有两种属性：禁言类型、对应用户列表。
 */
public interface GuildMuteResult {
    /**
     * `1`代表麦克风闭麦，`2`代表耳机静音。
     */
    public val type: Int

    /**
     * 对应用户列表. 用户列表没必要出现重复。
     */
    public val userIds: Set<ID>
}

