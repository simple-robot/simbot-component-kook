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

package love.forte.simbot.kaiheila.api.message

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.ID
import love.forte.simbot.kaiheila.api.BaseApiRequestKey
import love.forte.simbot.kaiheila.api.KaiheilaPostRequest

/**
 * [更新私信聊天消息](https://developer.kaiheila.cn/doc/http/direct-message#%E6%9B%B4%E6%96%B0%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF)
 *
 * @author ForteScarlet
 */
public class DirectMessageUpdateRequest(

    /**
     * 消息 id
     */
    private val msgId: ID,

    /**
     * 消息内容
     */
    private val content: String,

    /**
     * 回复某条消息的 msgId。如果为空，则代表删除回复，不传则无影响。
     */
    private val quote: ID?,
) : KaiheilaPostRequest<Unit>() {
    public companion object Key : BaseApiRequestKey("direct-message", "update")

    override val resultDeserializer: DeserializationStrategy<out Unit> get() = Unit.serializer()
    override val apiPaths: List<String> get() = apiPathList

    protected override fun createBody(): Any = Body(msgId, content, quote)

    @Serializable
    private data class Body(
        @SerialName("msg_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val msgId: ID,
        val content: String,
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val quote: ID?,
    )
}


/**
 * 通过 [MessageCreated] 构建 [DirectMessageUpdateRequest] 实例。
 *
 * @see MessageCreated.toUpdate
 */
public fun MessageCreated.toDirectUpdate(
    content: String,
    quote: ID? = null
): DirectMessageUpdateRequest =
    DirectMessageUpdateRequest(msgId, content, quote)
