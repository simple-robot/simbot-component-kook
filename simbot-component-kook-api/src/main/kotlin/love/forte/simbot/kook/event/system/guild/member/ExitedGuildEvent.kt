/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.event.system.guild.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.Timestamp


/**
 * [服务器成员退出](https://developer.kaiheila.cn/doc/event/guild-member#%E6%9C%8D%E5%8A%A1%E5%99%A8%E6%88%90%E5%91%98%E9%80%80%E5%87%BA)
 *
 *
 */
public interface ExitedGuildEventBody : GuildMemberEventExtraBody {
    /**
     * 用户 id
     */
    public val userId: ID

    /**
     * 退出服务器的时间(毫秒)
     */
    public val exitedAt: Timestamp
}


@Serializable
internal data class ExitedGuildEventBodyImpl(
    @SerialName("user_id")
    override val userId: CharSequenceID,
    @SerialName("exited_at")
    override val exitedAt: Timestamp

) : ExitedGuildEventBody
