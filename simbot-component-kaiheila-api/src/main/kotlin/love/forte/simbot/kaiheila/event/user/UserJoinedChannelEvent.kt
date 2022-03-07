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
 *
 * [用户加入语音频道](https://developer.kaiheila.cn/doc/event/user#%E7%94%A8%E6%88%B7%E5%8A%A0%E5%85%A5%E8%AF%AD%E9%9F%B3%E9%A2%91%E9%81%93)
 *
 * type: [UserEventSubTypeConstants.JOINED_CHANNEL]
 *
 * @author ForteScarlet
 */
public interface UserJoinedChannelEventBody {
    /**
     * 用户ID
     */
    public val userId: ID

    /**
     * 频道ID
     */
    public val channelId: ID

    /**
     * 加入时间
     */
    public val joinedAt: Timestamp
}

/**
 *
 * [用户加入语音频道](https://developer.kaiheila.cn/doc/event/user#%E7%94%A8%E6%88%B7%E5%8A%A0%E5%85%A5%E8%AF%AD%E9%9F%B3%E9%A2%91%E9%81%93)
 *
 * type: `joined_channel`
 *
 * @author ForteScarlet
 */
@Serializable
public data class UserJoinedChannelEventBodyImpl(
    @SerialName("user_id")
    override val userId: CharSequenceID,
    @SerialName("channel_id")
    override val channelId: CharSequenceID,
    @SerialName("joined_at")
    override val joinedAt: Timestamp,
) : UserJoinedChannelEventBody

