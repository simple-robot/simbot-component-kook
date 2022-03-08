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
 *
 * [自己退出服务器](https://developer.kaiheila.cn/doc/event/user#%E8%87%AA%E5%B7%B1%E9%80%80%E5%87%BA%E6%9C%8D%E5%8A%A1%E5%99%A8)
 *
 * 当自己被踢出服务器或被拉黑或主动退出服务器时, 产生该事件（对于机器人来说，就是机器人被踢出/拉黑/主动退出新服务器）
 *
 * type: [UserEventSubTypeConstants.SELF_EXITED_GUILD]
 *
 */
public interface SelfExitedGuildEventBody {
    /**
     * 服务器id
     */
    public val guildId: ID
}

/**
 *
 * [自己退出服务器](https://developer.kaiheila.cn/doc/event/user#%E8%87%AA%E5%B7%B1%E9%80%80%E5%87%BA%E6%9C%8D%E5%8A%A1%E5%99%A8)
 *
 * type: `self_exited_guild`
 *
 */
@Serializable
internal data class SelfExitedGuildEventBodyImpl(@SerialName("guild_id") override val guildId: CharSequenceID) :
    SelfExitedGuildEventBody




