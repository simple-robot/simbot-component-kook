package love.forte.simbot.kaiheila.api.userchat

import kotlinx.serialization.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.api.*


/**
 * [创建私信聊天会话](https://developer.kaiheila.cn/doc/http/user-chat#%E5%88%9B%E5%BB%BA%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E4%BC%9A%E8%AF%9D)
 *
 * @author ForteScarlet
 */
public class UserChatCreateRequest(private val targetId: ID) : KaiheilaPostRequest<UserChatView>() {
    public companion object : BaseApiRequestKey("user-chat", "create")

    override val resultDeserializer: DeserializationStrategy<out UserChatView> get() = UserChatView.serializer()
    override val apiPaths: List<String> get() = apiPathList
    override fun createBody(): Any = Body(targetId)

    @Serializable
    private data class Body(@SerialName("target_id") @Serializable(ID.AsCharSequenceIDSerializer::class) val targetId: ID)
}