package love.forte.simbot.kaiheila.api.message

import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.api.*

/**
 * [删除私信聊天消息](https://developer.kaiheila.cn/doc/http/direct-message#%E5%88%A0%E9%99%A4%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF)
 *
 * @author ForteScarlet
 */
public class DirectMessageDeleteRequest(private val msgId: ID) : KaiheilaPostRequest<Unit>() {
    public companion object : BaseApiRequestKey("direct-message", "delete")

    override val resultDeserializer: DeserializationStrategy<out Unit> get() = Unit.serializer()
    override val apiPaths: List<String> get() = apiPathList

    override fun createBody(): Any = Body(msgId)

    @Serializable
    private data class Body(@SerialName("msg_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val msgId: ID)

}