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
 * [删除服务器静音或闭麦](https://developer.kook.cn/doc/http/guild#%E5%88%A0%E9%99%A4%E6%9C%8D%E5%8A%A1%E5%99%A8%E9%9D%99%E9%9F%B3%E6%88%96%E9%97%AD%E9%BA%A6)
 *
 * request method: POST
 *
 */
public class GuildMuteDeleteRequest(
    /** 服务器id */
    guildId: ID,
    /** 用户id */
    userId: ID,
    /**
     * 1代表麦克风闭麦，2代表耳机静音
     *
     * @see GuildMuteType.TYPE_MICROPHONE
     * @see GuildMuteType.TYPE_EARPHONE
     */
    type: Int
) : KookPostRequest<Unit>() {
    public companion object Key : BaseKookApiRequestKey("guild-mute", "delete")

    override val resultDeserializer: DeserializationStrategy<out Unit>
        get() = Unit.serializer()

    override val apiPaths: List<String> get() = apiPathList

    override val body: Any = Body(guildId, userId, type)

    @Serializable
    private data class Body(
        /** 服务器id */
        @SerialName("guild_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val guildId: ID,
        /** 用户id */
        @SerialName("user_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val userId: ID,
        /** 1代表麦克风闭麦，2代表耳机静音 */
        val type: Int
    )
}