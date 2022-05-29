package love.forte.simbot.kaiheila.api.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.Timestamp
import love.forte.simbot.kaiheila.api.ApiResultType

/**
 * [发送频道消息][MessageCreateRequest] 和 [发送私聊消息][DirectMessageCreateRequest]的响应值。
 *
 * @author ForteScarlet
 */
@Serializable
public data class MessageCreated @ApiResultType constructor(
    /**
     * 服务端生成的消息 id
     */
    @SerialName("msg_id")
    val msgId: CharSequenceID,

    /**
     * 消息发送时间(服务器时间戳)
     */
    @SerialName("msg_timestamp")
    val msgTimestamp: Timestamp,

    /**
     * 随机字符串，见参数列表
     */
    val nonce: String? = null,
)