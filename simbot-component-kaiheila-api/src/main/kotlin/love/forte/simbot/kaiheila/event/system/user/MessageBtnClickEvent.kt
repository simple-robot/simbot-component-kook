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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.kaiheila.objects.User
import love.forte.simbot.kaiheila.objects.UserImpl


/**
 *
 * [Card消息中的Button点击事件](https://developer.kaiheila.cn/doc/event/user#Card%E6%B6%88%E6%81%AF%E4%B8%AD%E7%9A%84Button%E7%82%B9%E5%87%BB%E4%BA%8B%E4%BB%B6)
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
     *   "avatar": "https://img.kaiheila.cn/avatars/2020-02/xxxx.jpg/icon",
     *   "vip_avatar": "https://img.kaiheila.cn/avatars/2020-02/xxxx.jpg/icon",
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


