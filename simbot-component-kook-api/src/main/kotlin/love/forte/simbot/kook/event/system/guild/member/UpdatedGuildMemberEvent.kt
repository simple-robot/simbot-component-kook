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

/**
 * [服务器成员信息更新](https://developer.kaiheila.cn/doc/event/guild-member#%E6%9C%8D%E5%8A%A1%E5%99%A8%E6%88%90%E5%91%98%E4%BF%A1%E6%81%AF%E6%9B%B4%E6%96%B0)
 *
 * `updated_guild_member`
 *
 * @author ForteScarlet
 */
public interface UpdatedGuildMemberEventBody : GuildMemberEventExtraBody {

    /**
     * User ID
     */
    public val userId: ID

    /**
     * 昵称
     */
    public val nickname: String
}

/**
 * 服务器成员信息更新
 *
 * `updated_guild_member`
 *
 * @author ForteScarlet
 */
@Serializable
internal data class UpdatedGuildMemberEventBodyImpl(
    /**
     * User ID
     */
    @SerialName("user_id")
    override val userId: CharSequenceID,
    /**
     * 昵称
     */
    override val nickname: String,
) : UpdatedGuildMemberEventBody


