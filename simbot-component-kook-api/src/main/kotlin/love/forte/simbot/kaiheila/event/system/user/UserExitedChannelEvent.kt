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





