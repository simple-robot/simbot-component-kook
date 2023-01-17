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
package love.forte.simbot.kook.api.message

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.ID
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest

/**
 * [删除消息的某个回应](https://developer.kaiheila.cn/doc/http/message#%E5%88%A0%E9%99%A4%E6%B6%88%E6%81%AF%E7%9A%84%E6%9F%90%E4%B8%AA%E5%9B%9E%E5%BA%94)
 *
 */
public class MessageDeleteReactionRequest internal constructor(
    private val msgId: ID,
    private val emoji: ID,
    private val userId: ID,
) : KookPostRequest<Unit>() {
    @Deprecated(
        "Use MessageDeleteReactionRequest.create(...)",
        ReplaceWith("MessageDeleteReactionRequest.create(msgId. emoji, userId)")
    )
    public constructor(msgId: ID, emoji: love.forte.simbot.message.Emoji, userId: ID) : this(msgId, emoji.id, userId)
    
    @Deprecated(
        "Use MessageDeleteReactionRequest.create(...)",
        ReplaceWith("MessageDeleteReactionRequest.create(msgId. emoji, userId)")
    )
    public constructor(msgId: ID, emoji: Emoji, userId: ID) : this(msgId, emoji.id, userId)
    
    public companion object Key : BaseKookApiRequestKey("message", "delete-reaction") {
        
        /**
         * 构造 [MessageDeleteReactionRequest].
         *
         * @param msgId 消息id
         * @param emoji emoji表情id
         * @param userId 用户id
         *
         */
        @JvmStatic
        public fun create(msgId: ID, emoji: ID, userId: ID): MessageDeleteReactionRequest =
            MessageDeleteReactionRequest(msgId, emoji, userId)
        
        /**
         * 构造 [MessageDeleteReactionRequest].
         *
         * @param msgId 消息id
         * @param emoji emoji表情
         * @param userId 用户id
         *
         */
        @JvmStatic
        public fun create(msgId: ID, emoji: love.forte.simbot.message.Emoji, userId: ID): MessageDeleteReactionRequest =
            create(msgId, emoji.id, userId)
        
        /**
         * 构造 [MessageDeleteReactionRequest].
         *
         * @param msgId 消息id
         * @param emoji emoji表情
         * @param userId 用户id
         *
         */
        @JvmStatic
        public fun create(msgId: ID, emoji: Emoji, userId: ID): MessageDeleteReactionRequest =
            create(msgId, emoji.id, userId)
    }
    
    override val resultDeserializer: DeserializationStrategy<out Unit> get() = Unit.serializer()
    override val apiPaths: List<String> get() = apiPathList
    override fun createBody(): Any = Body(msgId, emoji, userId)
    
    @Serializable
    private data class Body(
        @SerialName("msg_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val msgId: ID,
        @Serializable(ID.AsCharSequenceIDSerializer::class) val emoji: ID,
        @SerialName("user_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val userId: ID,
    )
}
