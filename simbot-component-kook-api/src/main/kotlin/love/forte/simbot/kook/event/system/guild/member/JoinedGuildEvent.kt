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
 * 新成员加入服务器
 *
 * `joined_guild`
 * @author ForteScarlet
 */

public interface JoinedGuildEventBody : GuildMemberEventExtraBody {
    
    /**
     * 用户ID。
     */
    public val userId: ID
    
    /**
     * 加入时间。
     */
    public val joinedAt: Timestamp


}


/**
 * 新成员加入服务器
 *
 * `joined_guild`
 * @author ForteScarlet
 */
@Serializable
internal data class JoinedGuildEventBodyImpl(
    @SerialName("user_id")
    override val userId: CharSequenceID,

    @SerialName("joined_at")
    override val joinedAt: Timestamp,

) : JoinedGuildEventBody

