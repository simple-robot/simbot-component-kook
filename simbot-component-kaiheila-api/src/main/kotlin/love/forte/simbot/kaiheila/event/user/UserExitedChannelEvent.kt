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
 * [用户退出语音频道](https://developer.kaiheila.cn/doc/event/user#%E7%94%A8%E6%88%B7%E9%80%80%E5%87%BA%E8%AF%AD%E9%9F%B3%E9%A2%91%E9%81%93)
 *
 * type: [UserEventSubTypeConstants.EXITED_CHANNEL]
 *
 */
public interface UserExitedChannelEventBody {
    /**
     * 用户ID
     */
    public val userId: ID

    /**
     * 加入的频道id
     */
    public val channelId: ID

    /**
     * 退出的时间（ms)
     */
    public val exitedAt: Timestamp
}

/**
 * [用户退出语音频道](https://developer.kaiheila.cn/doc/event/user#%E7%94%A8%E6%88%B7%E9%80%80%E5%87%BA%E8%AF%AD%E9%9F%B3%E9%A2%91%E9%81%93)
 *
 * type: `exited_channel`
 *
 */
@Serializable
public data class UserExitedChannelEventBodyImpl(
    @SerialName("user_id")
    override val userId: CharSequenceID,
    @SerialName("channel_id")
    override val channelId: CharSequenceID,
    @SerialName("exited_at")
    override val exitedAt: Timestamp,
) : UserExitedChannelEventBody





