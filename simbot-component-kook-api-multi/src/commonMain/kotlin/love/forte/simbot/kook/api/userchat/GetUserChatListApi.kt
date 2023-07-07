/*
 * Copyright (c) 2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook,
 * If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.api.userchat

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.kook.api.ApiResultType
import love.forte.simbot.kook.api.KookGetApi
import love.forte.simbot.kook.util.appendIfNotNull
import love.forte.simbot.kook.util.parameters
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic


/**
 * [获取私信聊天会话列表](https://developer.kookapp.cn/doc/http/user-chat#%E8%8E%B7%E5%8F%96%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E4%BC%9A%E8%AF%9D%E5%88%97%E8%A1%A8)
 *
 * @author ForteScarlet
 */
public class GetUserChatListApi private constructor(
    /**
     * 目标页数
     */
    private val page: Int? = null,
    /**
     * 每页数据数量
     */
    private val pageSize: Int? = null,
) : KookGetApi<UserChatListView>() {
    public companion object Factory {
        private val PATH = ApiPath.create("user-chat", "list")

        private val EMPTY = GetUserChatListApi()

        /**
         * 构造 [获取私信聊天会话列表][GetUserChatListApi] 请求。
         *
         * @param page 目标页数
         * @param pageSize 每页数据数量
         */
        @JvmStatic
        @JvmOverloads
        public fun create(page: Int? = null, pageSize: Int? = null): GetUserChatListApi =
            if (page == null && pageSize == null) EMPTY else GetUserChatListApi(page, pageSize)
    }

    override val apiPath: ApiPath
        get() = PATH

    override val resultDeserializer: DeserializationStrategy<UserChatListView>
        get() = UserChatListView.serializer()

    override fun urlBuild(builder: URLBuilder) {
        builder.parameters {
            appendIfNotNull("page", page) { it.toString() }
            appendIfNotNull("page_size", pageSize) { it.toString() }
        }
    }
}


/**
 * [GetUserChatListApi] 的响应结果。
 */
@Serializable
public data class UserChatListView @ApiResultType constructor(
    val code: String,
    /**
     * 上次阅读消息的时间 (毫秒)
     */
    @SerialName("last_read_time") val lastReadTime: Long,
    /**
     * 最新消息时间 (毫秒)
     */
    @SerialName("latest_msg_time") val latestMsgTime: Long,
    /**
     * 未读消息数
     */
    @SerialName("unread_count") val unreadCount: Int,
    /**
     * 目标用户信息
     */
    @SerialName("target_info") val targetInfo: TargetInfo,
)

