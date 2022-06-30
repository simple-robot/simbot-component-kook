/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
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
import love.forte.simbot.kaiheila.api.BaseKookApiRequestKey
import love.forte.simbot.kaiheila.api.KookPostRequest

/**
 *
 * [给某个消息添加回应](https://developer.kaiheila.cn/doc/http/message#%E7%BB%99%E6%9F%90%E4%B8%AA%E6%B6%88%E6%81%AF%E6%B7%BB%E5%8A%A0%E5%9B%9E%E5%BA%94)
 *
 */
public class MessageAddReactionRequest(
    /**
     * 	频道消息的id
     */
    private val msgId: ID,
    /**
     *	emoji的id, 可以为 `Guild Emoji` 或者 `Emoji`
     */
    private val emoji: ID,
) : KookPostRequest<Unit>() {
    public constructor(msgId: ID, emoji: love.forte.simbot.message.Emoji) : this(msgId, emoji.id)
    public constructor(msgId: ID, emoji: Emoji) : this(msgId, emoji.id)

    public companion object Key : BaseKookApiRequestKey("message", "add-reaction")

    override val resultDeserializer: DeserializationStrategy<out Unit> get() = Unit.serializer()
    override val apiPaths: List<String> get() = apiPathList
    override fun createBody(): Any = Body(msgId, emoji)

    @Serializable
    private data class Body(
        @SerialName("msg_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val msgId: ID,
        @Serializable(ID.AsCharSequenceIDSerializer::class) val emoji: ID
    )


}