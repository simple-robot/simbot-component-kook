package love.forte.simbot.kaiheila.api.message

import kotlinx.serialization.*
import love.forte.simbot.kaiheila.api.*


/**
 * [发送私信聊天消息](https://developer.kaiheila.cn/doc/http/direct-message#%E5%8F%91%E9%80%81%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF)
 *
 * @author ForteScarlet
 */
public class DirectMessageCreateRequest internal constructor(
    /**
     * 消息类型, 不传默认为 1, 代表文本类型。2 图片消息，3 视频消息，4 文件消息，9 代表 kmarkdown 消息, 10 代表卡片消息。
     * 默认为 [MessageType.TEXT]
     * @see MessageType
     */
    private val type: Int, // = MessageType.TEXT.type,

    /**
     * 目标用户 id，后端会自动创建会话。有此参数之后可不传 chat_code参数
     */
    private val targetId: String?,

    /**
     * 目标会话 Code，chat_code 与 target_id 必须传一个
     */
    private val chatCode: String?,

    /**
     * 	消息内容
     */
    private val content: String,

    /**
     * 回复某条消息的 msgId
     */
    private val quote: String? = null,

    /**
     * nonce, 服务端不做处理, 原样返回
     */
    private val nonce: String? = null,
) : KaiheilaPostRequest<MessageCreated>() {
    public companion object : BaseApiRequestKey("direct-message", "create")

    override val resultDeserializer: DeserializationStrategy<out MessageCreated> get() = MessageCreated.serializer()
    override val apiPaths: List<String> get() = apiPathList
    override fun createBody(): Any = Body(type, targetId, chatCode, content, quote, nonce)

    @Serializable
    private data class Body(
        private val type: Int,
        @SerialName("target_id")
        private val targetId: String?,
        @SerialName("chat_code")
        private val chatCode: String?,
        private val content: String,
        private val quote: String?,
        private val nonce: String?,
    )
}