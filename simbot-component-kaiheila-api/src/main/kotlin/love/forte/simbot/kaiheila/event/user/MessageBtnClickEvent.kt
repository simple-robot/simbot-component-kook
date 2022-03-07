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
import love.forte.simbot.kaiheila.objects.*


/**
 *
 * [Card消息中的Button点击事件](https://developer.kaiheila.cn/doc/event/user#Card%E6%B6%88%E6%81%AF%E4%B8%AD%E7%9A%84Button%E7%82%B9%E5%87%BB%E4%BA%8B%E4%BB%B6)
 *
 *
 * type: [UserEventSubTypeConstants.MESSAGE_BTN_CLICK]
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


