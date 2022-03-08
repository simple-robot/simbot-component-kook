/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kaiheila.event.system.user

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


