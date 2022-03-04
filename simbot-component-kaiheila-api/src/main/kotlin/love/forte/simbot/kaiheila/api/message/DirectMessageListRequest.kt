package love.forte.simbot.kaiheila.api.message

import com.sun.xml.internal.bind.v2.model.core.*
import io.ktor.http.*
import kotlinx.serialization.*
import love.forte.simbot.kaiheila.api.*
import love.forte.simbot.kaiheila.api.message.DirectMessageListRequest.Companion.byChatCode
import love.forte.simbot.kaiheila.api.message.DirectMessageListRequest.Companion.byTargetId

/**
 * [获取私信聊天消息列表](https://developer.kaiheila.cn/doc/http/direct-message#%E8%8E%B7%E5%8F%96%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF%E5%88%97%E8%A1%A8)
 *
 * method: GET
 *
 * @see byChatCode
 * @see byTargetId
 */
public class DirectMessageListRequest private constructor(
    /**
     * 私信会话 Code。chat_code 与 target_id 必须传一个
     */
    private val chatCode: ID?,
    /**
     * 目标用户 id，后端会自动创建会话。有此参数之后可不传 chat_code参数
     */
    private val targetId: ID?,
    /**
     * 参考消息 id，不传则默认为最新的消息 id
     */
    private val msgId: ID? = null,
    /**
     * 查询模式，有三种模式可以选择。不传则默认查询最新的消息
     */
    private val flag: String? = null,
) : KaiheilaGetRequest<KaiheilaApiResult.ListData<DirectMessageDetails>>() {
    public companion object : BaseApiRequestKey("direct-message", "list") {
        @JvmStatic
        @JvmOverloads
        @JvmName("getInstanceByChatCode")
        public fun byChatCode(
            chatCode: ID,
            msgId: ID? = null,
            flag: MessageReqFlag? = null,
        ): DirectMessageListRequest {
            return DirectMessageListRequest(
                chatCode = chatCode, targetId = null, msgId = msgId, flag = flag?.flag
            )
        }

        @JvmStatic
        @JvmOverloads
        @JvmName("getInstanceByTargetId")
        public fun byTargetId(
            targetId: ID,
            msgId: ID? = null,
            flag: MessageReqFlag? = null,
        ): DirectMessageListRequest {
            return DirectMessageListRequest(
                chatCode = null, targetId = targetId, msgId = msgId, flag = flag?.flag
            )
        }
    }

    override val resultDeserializer: DeserializationStrategy<out KaiheilaApiResult.ListData<DirectMessageDetails>>
        get() = KaiheilaApiResult.ListData.serializer(DirectMessageDetails.serializer())

    override val apiPaths: List<String> get() = apiPathList

    override fun ParametersBuilder.buildParameters() {
        appendIfNotnull("chat_code", chatCode)
        appendIfNotnull("target_id", targetId)
        appendIfNotnull("msg_id", msgId)
        appendIfNotnull("flag", flag)
    }
}


/**
 *
 * 对消息进行查询时的 `flag` 参数范围。
 *
 * @author ForteScarlet
 */
public enum class MessageReqFlag(public val flag: String) {

    /**
     * 查询参考消息之前的消息，不包括参考消息。
     */
    BEFORE("before"),

    /**
     * 查询以参考消息为中心，前后一定数量的消息。
     */
    AROUND("around"),

    /**
     * 查询参考消息之后的消息，不包括参考消息。
     */
    AFTER("after"),
}