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

package love.forte.simbot.kaiheila.api.guild

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.ID
import love.forte.simbot.kaiheila.api.BaseKookApiRequestKey
import love.forte.simbot.kaiheila.api.KookPostRequest


/**
 *
 * [修改服务器中用户的昵称](https://developer.kaiheila.cn/doc/http/guild#%E4%BF%AE%E6%94%B9%E6%9C%8D%E5%8A%A1%E5%99%A8%E4%B8%AD%E7%94%A8%E6%88%B7%E7%9A%84%E6%98%B5%E7%A7%B0)
 *
 * request method: POST
 *
 * @author ForteScarlet
 */
public class GuildNicknameRequest(
    guildId: ID,
    nickname: String? = null,
    userId: ID? = null,
) : KookPostRequest<Unit>() {
    public companion object Key : BaseKookApiRequestKey("guild", "nickname")

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


