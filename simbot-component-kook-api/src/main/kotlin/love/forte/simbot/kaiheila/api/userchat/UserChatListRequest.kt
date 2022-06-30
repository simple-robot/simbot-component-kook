package love.forte.simbot.kaiheila.api.userchat

import kotlinx.serialization.DeserializationStrategy
import love.forte.simbot.kaiheila.api.KookApiResult
import love.forte.simbot.kaiheila.api.KookGetRequest
import love.forte.simbot.kaiheila.util.unmodifiableListOf


/**
 * [获取私信聊天会话列表](https://developer.kaiheila.cn/doc/http/user-chat#%E8%8E%B7%E5%8F%96%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E4%BC%9A%E8%AF%9D%E5%88%97%E8%A1%A8)
 *
 * @author ForteScarlet
 */
public object UserChatListRequest : KookGetRequest<KookApiResult.ListData<UserChatView>>() {
    override val resultDeserializer: DeserializationStrategy<out KookApiResult.ListData<UserChatView>>
        get() = KookApiResult.ListData.serializer(
            UserChatViewImpl.serializer()
        )
    override val apiPaths: List<String> = unmodifiableListOf("user-chat", "list")
}