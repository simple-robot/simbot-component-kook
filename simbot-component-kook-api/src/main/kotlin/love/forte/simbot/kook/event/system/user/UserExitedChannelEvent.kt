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
import love.forte.simbot.Timestamp


/**
 * [用户退出语音频道](https://developer.kaiheila.cn/doc/event/user#%E7%94%A8%E6%88%B7%E9%80%80%E5%87%BA%E8%AF%AD%E9%9F%B3%E9%A2%91%E9%81%93)
 *
 * type: [UserEvents.EXITED_CHANNEL]
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





