/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kook.api.invite

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.ID
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest


/**
 * [删除邀请链接](https://developer.kaiheila.cn/doc/http/invite#删除邀请链接)
 *
 * `/api/v3/invite/delete`
 *
 * method POST
 */
public class InviteDeleteRequest(
    /**
     * 邀请码
     */
    private val urlCode: String,

    /**
     * 服务器 id
     */
    private val guildId: ID? = null,

    /**
     * 	服务器频道 ID
     */
    private val channelId: ID? = null,
) : KookPostRequest<Unit>() {
    public companion object Key : BaseKookApiRequestKey("invite", "delete")

    override val resultDeserializer: DeserializationStrategy<out Unit> get() = Unit.serializer()
    override val apiPaths: List<String> get() = apiPathList

    override fun createBody(): Any = Body(urlCode, guildId, channelId)

    @Serializable
    private data class Body(
        @SerialName("url_code") val urlCode: String,
        @SerialName("guild_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val guildId: ID?,
        @SerialName("channel_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val channelId: ID?,
    )

}