package love.forte.simbot.kaiheila.api.userchat

import io.ktor.http.*
import kotlinx.serialization.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.api.*


/**
 * [获取私信聊天会话详情](https://developer.kaiheila.cn/doc/http/user-chat#%E8%8E%B7%E5%8F%96%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E4%BC%9A%E8%AF%9D%E8%AF%A6%E6%83%85)
 *
 * @param chatCode 会话ID
 * @author ForteScarlet
 */
public class UserChatViewRequest(private val chatCode: ID) : KaiheilaGetRequest<UserChatView>() {
    public companion object : BaseApiRequestKey("user-chat", "view")

    override val resultDeserializer: DeserializationStrategy<out UserChatView>
        get() = UserChatView.serializer()

    override val apiPaths: List<String> get() = apiPathList

    override fun ParametersBuilder.buildParameters() {
        append("chat_code", chatCode.literal)
    }

}


/**
 * 私信聊天会话详情信息。
 *
 */
@Serializable
public class UserChatView @ApiResultType constructor(
    /**
     * 私信会话 Code
     */
    public val code: CharSequenceID,

    /**
     * 上次阅读消息的时间
     */
    @SerialName("last_read_time")
    public val lastReadTime: Timestamp,
    /**
     * 最新消息时间
     */
    @SerialName("latest_msg_time")
    public val latestMsgTime: Timestamp,
    /**
     * 未读消息数
     */
    @SerialName("unread_count")
    public val unreadCount: Int,

    /**
     * 目标用户信息
     */
    @SerialName("target_info")
    public val targetInfo: UserChatTargetInfo,
) {
}