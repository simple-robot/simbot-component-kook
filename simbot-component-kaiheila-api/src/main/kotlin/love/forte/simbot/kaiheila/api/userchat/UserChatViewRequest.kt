/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kaiheila.api.userchat

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.kaiheila.api.BaseApiRequestKey
import love.forte.simbot.kaiheila.api.KaiheilaGetRequest
import love.forte.simbot.literal


/**
 * [获取私信聊天会话详情](https://developer.kaiheila.cn/doc/http/user-chat#%E8%8E%B7%E5%8F%96%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E4%BC%9A%E8%AF%9D%E8%AF%A6%E6%83%85)
 *
 * @param chatCode 会话ID
 * @author ForteScarlet
 */
public class UserChatViewRequest(private val chatCode: ID) : KaiheilaGetRequest<UserChatView>() {
    public companion object Key : BaseApiRequestKey("user-chat", "view")
    
    override val resultDeserializer: DeserializationStrategy<out UserChatView>
        get() = UserChatViewImpl.serializer()
    
    override val apiPaths: List<String> get() = apiPathList
    
    override fun ParametersBuilder.buildParameters() {
        append("chat_code", chatCode.literal)
    }
    
}


/**
 * 私信聊天会话详情信息。
 *
 */
public interface UserChatView {
    
    /**
     * 私信会话 Code
     */
    public val code: ID
    
    /**
     * 上次阅读消息的时间
     */
    public val lastReadTime: Timestamp
    
    /**
     * 最新消息时间
     */
    public val latestMsgTime: Timestamp
    
    /**
     * 未读消息数
     */
    public val unreadCount: Int
    
    /**
     * 是否为好友
     */
    public val isFriend: Boolean
    
    /**
     * 是否已屏蔽对方
     */
    public val isBlocked: Boolean
    
    
    /**
     * 是否已被对方屏蔽
     */
    public val isTargetBlocked: Boolean
    
    
    /**
     * 目标用户信息
     */
    public val targetInfo: UserChatTargetInfo
}


@Serializable
internal data class UserChatViewImpl(
    /**
     * 私信会话 Code
     */
    override val code: CharSequenceID,
    
    /**
     * 上次阅读消息的时间
     */
    @SerialName("last_read_time")
    override val lastReadTime: Timestamp,
    /**
     * 最新消息时间
     */
    @SerialName("latest_msg_time")
    override val latestMsgTime: Timestamp,
    /**
     * 未读消息数
     */
    @SerialName("unread_count")
    override val unreadCount: Int,
    
    /**
     * 是否为好友
     */
    @SerialName("is_friend")
    override val isFriend: Boolean,
    
    /**
     * 是否已屏蔽对方
     */
    @SerialName("is_blocked")
    override val isBlocked: Boolean,
    
    
    /**
     * 是否已被对方屏蔽
     */
    @SerialName("is_target_blocked")
    override val isTargetBlocked: Boolean,
    
    
    /**
     * 目标用户信息
     */
    @SerialName("target_info")
    override val targetInfo: UserChatTargetInfoImpl,
) : UserChatView