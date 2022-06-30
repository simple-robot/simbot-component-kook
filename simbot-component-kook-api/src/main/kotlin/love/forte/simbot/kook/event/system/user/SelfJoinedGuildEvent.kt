/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kook 的一部分。
 *
 *  simbot-component-kook 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kook 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

package love.forte.simbot.kook.event.system.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID

/**
 * [自己新加入服务器](https://developer.kook.cn/doc/event/user#%E8%87%AA%E5%B7%B1%E6%96%B0%E5%8A%A0%E5%85%A5%E6%9C%8D%E5%8A%A1%E5%99%A8)
 *
 *
 * 当自己被邀请或主动加入新的服务器时, 产生该事件（对于机器人来说，就是机器人被邀请进入新服务器）
 *
 *
 * type: [UserEvents.SELF_JOINED_GUILD]
 *
 */
public interface SelfJoinedGuildEventBody {
    /**
     * 频道ID
     */
    public val guildId: ID
}



@Serializable
internal data class SelfJoinedGuildEventBodyImpl(@SerialName("guild_id") override val guildId: CharSequenceID):
    SelfJoinedGuildEventBody


