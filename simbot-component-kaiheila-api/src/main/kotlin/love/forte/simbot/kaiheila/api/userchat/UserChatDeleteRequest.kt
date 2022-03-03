package love.forte.simbot.kaiheila.api.userchat

import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.api.*


/**
 * [删除私信聊天会话](https://developer.kaiheila.cn/doc/http/user-chat#%E5%88%A0%E9%99%A4%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E4%BC%9A%E8%AF%9D)
 *
 * @param chatCode 删除目标会话的ID
 * @author ForteScarlet
 */
public class UserChatDeleteRequest(private val chatCode: ID) : KaiheilaPostRequest<Unit>() {
    public companion object : BaseApiRequestKey("user-chat", "delete")

    override val resultDeserializer: DeserializationStrategy<out Unit> get() = Unit.serializer()
    override val apiPaths: List<String> get() = apiPathList
    override fun createBody(): Any = Body(chatCode)

    @Serializable
    private data class Body(@SerialName("chat_code") @Serializable(ID.AsCharSequenceIDSerializer::class) val chatCode: ID)
}