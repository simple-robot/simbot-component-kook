/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.api.userchat

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookGetRequest
import love.forte.simbot.literal


/**
 * [获取私信聊天会话详情](https://developer.kaiheila.cn/doc/http/user-chat#%E8%8E%B7%E5%8F%96%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E4%BC%9A%E8%AF%9D%E8%AF%A6%E6%83%85)
 *
 * @param chatCode 会话ID
 * @author ForteScarlet
 */
public class UserChatViewRequest internal constructor(private val chatCode: ID) : KookGetRequest<UserChatView>() {
    public companion object Key : BaseKookApiRequestKey("user-chat", "view") {
    
        /**
         * 构造 [UserChatViewRequest].
         *
         * @param chatCode 目标会话id
         *
         */
        @JvmStatic
        public fun create(chatCode: ID): UserChatViewRequest = UserChatViewRequest(chatCode)
    }
    
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
