/*
 * Copyright (c) 2021-2023. ForteScarlet.
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

package love.forte.simbot.kook.event.system.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID


/**
 * [用户信息更新](https://developer.kaiheila.cn/doc/event/user#%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF%E6%9B%B4%E6%96%B0)
 *
 *
 *  该事件与服务器无关, 遵循以下条件
 *   1. 仅当用户的 用户名 或 头像 变更时;
 *   2. 仅通知与该用户存在关联的用户或Bot: a. 存在聊天会话 b. 双方好友关系
 *
 * type: [UserEvents.USER_UPDATED]
 *
 */
public interface UserUpdatedEventBody {
    /**
     * 用户ID
     */
    public val userId: ID

    /**
     * 用户名
     */
    public val username: String

    /**
     * 头像
     */
    public val avatar: String

}


/**
 * [用户信息更新](https://developer.kaiheila.cn/doc/event/user#%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF%E6%9B%B4%E6%96%B0)
 *
 *
 *  该事件与服务器无关, 遵循以下条件
 *   1. 仅当用户的 用户名 或 头像 变更时;
 *   2. 仅通知与该用户存在关联的用户或Bot: a. 存在聊天会话 b. 双方好友关系
 *
 * type: `user_updated`
 *
 */
@Serializable
internal data class UserUpdatedEventBodyImpl(
    @SerialName("user_id")
    override val userId: CharSequenceID,
    override val username: String,
    override val avatar: String,
) : UserUpdatedEventBody


