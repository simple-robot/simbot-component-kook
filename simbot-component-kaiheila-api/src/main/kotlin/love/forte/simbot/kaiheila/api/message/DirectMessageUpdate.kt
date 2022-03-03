package love.forte.simbot.kaiheila.api.message

import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import love.forte.simbot.kaiheila.api.*

/**
 * [更新私信聊天消息](https://developer.kaiheila.cn/doc/http/direct-message#%E6%9B%B4%E6%96%B0%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF)
 *
 * @author ForteScarlet
 */
public class DirectMessageUpdateRequest(

    /**
     * 消息 id
     */
    private val msgId: String,

    /**
     * 消息内容
     */
    private val content: String,

    /**
     * 回复某条消息的 msgId。如果为空，则代表删除回复，不传则无影响。
     */
    private val quote: String?,
) : KaiheilaPostRequest<Unit>() {
    public companion object : BaseApiRequestKey("direct-message", "update")

    override val resultDeserializer: DeserializationStrategy<out Unit> get() = Unit.serializer()
    override val apiPaths: List<String> get() = apiPathList

    protected override fun createBody(): Any = Body(msgId, content, quote)

    @Serializable
    private data class Body(
        @SerialName("msg_id")
        val msgId: String,
        val content: String,
        val quote: String?,
    )
}


/**
 * 通过 [MessageCreateResp] 构建 [DirectMessageUpdateReq] 实例。
 * TODO
 */
// public fun MessageCreateResp.createDirectMessageUpdateReq(content: String, quote: String? = null): DirectMessageUpdateReq =
//     DirectMessageUpdateReq(msgId, content, quote)
