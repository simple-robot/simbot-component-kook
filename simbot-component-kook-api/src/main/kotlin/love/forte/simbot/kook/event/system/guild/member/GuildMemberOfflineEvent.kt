/*
 * Copyright (c) 2022-2023. ForteScarlet.
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

package love.forte.simbot.kook.event.system.guild.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.Timestamp

/**
 *
 *
 *
 * `guild_member_offline`
 *
 * @author ForteScarlet
 */
public interface GuildMemberOfflineEventBody : GuildMemberEventExtraBody {

    /**
     *用户id
     */
    public val userId: ID

    /**
     *用户下线时间(毫秒)
     */
    public val eventTime: Timestamp

    /**
     *服务器id组成的数组, 代表与该用户所在的共同的服务器
     */
    public val guilds: List<ID>
}

/**
 *
 *
 *
 * `guild_member_offline`
 *
 * @author ForteScarlet
 */
@Serializable
internal data class GuildMemberOfflineEventBodyImpl(
    /**
     *用户id
     */
    @SerialName("user_id")
    override val userId: CharSequenceID,
    /**
     *用户下线时间(毫秒)
     */
    @SerialName("event_time")
    override val eventTime: Timestamp,
    /**
     *服务器id组成的数组, 代表与该用户所在的共同的服务器
     */
    override val guilds: List<CharSequenceID>,
) : GuildMemberOfflineEventBody

