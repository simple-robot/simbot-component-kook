/*
 * Copyright (c) 2021-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.event.system.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID

/**
 *
 * [自己退出服务器](https://developer.kaiheila.cn/doc/event/user#%E8%87%AA%E5%B7%B1%E9%80%80%E5%87%BA%E6%9C%8D%E5%8A%A1%E5%99%A8)
 *
 * 当自己被踢出服务器或被拉黑或主动退出服务器时, 产生该事件（对于机器人来说，就是机器人被踢出/拉黑/主动退出新服务器）
 *
 * type: [UserEvents.SELF_EXITED_GUILD]
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




