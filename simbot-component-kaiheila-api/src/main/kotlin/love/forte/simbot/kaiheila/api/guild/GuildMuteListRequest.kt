/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kaiheila.api.guild

import io.ktor.http.*
import kotlinx.serialization.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.api.*


/**
 *
 * [服务器静音闭麦列表](https://developer.kaiheila.cn/doc/http/guild#%E6%9C%8D%E5%8A%A1%E5%99%A8%E9%9D%99%E9%9F%B3%E9%97%AD%E9%BA%A6%E5%88%97%E8%A1%A8)
 *
 * request method: GET
 *
 */
public sealed class GuildMuteListRequest(
    private val guildId: ID
) : KaiheilaGetRequest<GuildMuteList>() {
    public companion object Key : BaseApiRequestKey("guild-mute", "list")

    override val resultDeserializer: DeserializationStrategy<out GuildMuteList>
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
    val mic: Mic,
    val headset: Headset,
) {

    @Serializable
    public data class Mic @ApiResultType constructor(
        override val type: Int = 1,
        @SerialName("user_ids")
        override val userIds: List<CharSequenceID> = emptyList(),
    ) : GuildMuteResult

    @Serializable
    public data class Headset @ApiResultType constructor(
        override val type: Int = 2,
        @SerialName("user_ids")
        override val userIds: List<CharSequenceID> = emptyList(),
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
     * 对应用户列表
     */
    public val userIds: List<ID>
}
