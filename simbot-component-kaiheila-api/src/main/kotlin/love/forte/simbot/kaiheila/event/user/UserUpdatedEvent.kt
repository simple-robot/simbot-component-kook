/*
 *
 *  * Copyright (c) 2021. ForteScarlet All rights reserved.
 *  * Project  simple-robot
 *  * File     MiraiAvatar.kt
 *  *
 *  * You can contact the author through the following channels:
 *  * github https://github.com/ForteScarlet
 *  * gitee  https://gitee.com/ForteScarlet
 *  * email  ForteScarlet@163.com
 *  * QQ     1149159218
 *
 */

package love.forte.simbot.kaiheila.event.user

import kotlinx.serialization.*
import love.forte.simbot.*


/**
 * [用户信息更新](https://developer.kaiheila.cn/doc/event/user#%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF%E6%9B%B4%E6%96%B0)
 *
 *
 *  该事件与服务器无关, 遵循以下条件
 *   1. 仅当用户的 用户名 或 头像 变更时;
 *   2. 仅通知与该用户存在关联的用户或Bot: a. 存在聊天会话 b. 双方好友关系
 *
 * type: [UserEventSubTypeConstants.USER_UPDATED]
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


