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
import love.forte.simbot.kook.objects.User
import love.forte.simbot.kook.objects.UserImpl


/**
 *
 * [Card消息中的Button点击事件](https://developer.kookapp.cn/doc/event/user#Card%20%E6%B6%88%E6%81%AF%E4%B8%AD%E7%9A%84%20Button%20%E7%82%B9%E5%87%BB%E4%BA%8B%E4%BB%B6)
 *
 *
 * type: [UserEvents.MESSAGE_BTN_CLICK]
 *
 */
public interface MessageBtnClickEventBody {
    /**
     * 用户id
     */
    public val userId: ID

    /**
     * 消息id
     */
    public val msgId: ID

    /**
     * 按钮return-val的返回值
     */
    public val value: String

    /**
     * 消息所在频道的id
     */
    public val targetId: ID

    /**
     * 用户对象.
     *
     * example:
     * ```json
     * "user_info": {
     *   "id": "2418200000",
     *   "username": "tz-un",
     *   "identify_num": "5618",
     *   "online": false,
     *   "status": 0,
     *   "bot": false,
     *   "avatar": "https://img.kook.cn/avatars/2020-02/xxxx.jpg/icon",
     *   "vip_avatar": "https://img.kook.cn/avatars/2020-02/xxxx.jpg/icon",
     *   "mobile_verified": true,
     *   "nickname": "tz-unn",
     *   "roles": [
     *     702
     *   ],
     *   "joined_at": 1621338425026,
     *   "active_time": 1628688607719,
     *   "is_master": false
     * }
     * ```
     */
    public val userInfo: User

}


/**
 *
 * [Card消息中的Button点击事件](https://developer.kaiheila.cn/doc/event/user#Card%E6%B6%88%E6%81%AF%E4%B8%AD%E7%9A%84Button%E7%82%B9%E5%87%BB%E4%BA%8B%E4%BB%B6)
 *
 *
 */
@Serializable
internal data class MessageBtnClickEventBodyImpl(
    @SerialName("user_id")
    override val userId: CharSequenceID,
    @SerialName("msg_id")
    override val msgId: CharSequenceID,
    override val value: String,
    @SerialName("target_id")
    override val targetId: CharSequenceID,
    @SerialName("user_info")
    override val userInfo: UserImpl
) : MessageBtnClickEventBody


