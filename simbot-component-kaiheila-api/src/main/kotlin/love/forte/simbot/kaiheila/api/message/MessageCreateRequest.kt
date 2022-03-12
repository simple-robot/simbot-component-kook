/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
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
package love.forte.simbot.kaiheila.api.message

import kotlinx.serialization.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.api.*

/**
 * [发送频道聊天消息](https://developer.kaiheila.cn/doc/http/message#%E5%8F%91%E9%80%81%E9%A2%91%E9%81%93%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF)
 *
 * @author ForteScarlet
 */
public class MessageCreateRequest(
    /**
     * 消息类型, 见[type], 不传默认为1, 代表文本类型。9代表 kmarkdown 消息, 10代表卡片消息。
     *
     *
     * *Note: 经测试，似乎也支持 [MessageType.IMAGE]、[MessageType.FILE]等格式。*
     *
     * 默认为 [MessageType.TEXT]
     * @see MessageType
     */
    private val type: Int = MessageType.TEXT.type,

    /**
     * 	目标频道 id
     */
    private val targetId: ID,

    /**
     * 	消息内容
     */
    private val content: String,

    /**
     * 回复某条消息的 msgId
     */
    private val quote: ID? = null,

    /**
     * nonce, 服务端不做处理, 原样返回
     */
    private val nonce: String? = null,

    /**
     * 用户id,如果传了，代表该消息是临时消息，该消息不会存数据库，但是会在频道内只给该用户推送临时消息。用于在频道内针对用户的操作进行单独的回应通知等。
     */
    private val tempTargetId: ID? = null,
) : KaiheilaPostRequest<MessageCreated>() {
    public companion object Key : BaseApiRequestKey("message", "create")

    override val resultDeserializer: DeserializationStrategy<out MessageCreated> get() = MessageCreated.serializer()

    override val apiPaths: List<String> get() = apiPathList

    override fun createBody(): Any = Body(type, targetId, content, quote, nonce, tempTargetId)


    @Serializable
    private data class Body(
        val type: Int,
        @SerialName("target_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val targetId: ID,
        val content: String,
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val quote: ID? = null,
        val nonce: String? = null,
        @SerialName("temp_target_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val tempTargetId: ID? = null,
    )
}