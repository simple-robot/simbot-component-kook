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

package love.forte.simbot.kook.api.invite

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.kook.objects.UserImpl

@Serializable
internal data class InviteInfoImpl(
    /**
     * 服务器id
     *
     */
    @SerialName("guild_id")
    override val guildId: CharSequenceID,
    /**
     * 频道id
     *
     */
    @SerialName("channel_id")
    override val channelId: CharSequenceID,
    /**
     * url code
     *
     */
    @SerialName("url_code")
    override val urlCode: String,
    /**
     * 地址
     *
     */
    override val url: String,
    /**
     * 用户
     *
     */
    override val user: UserImpl,
) : InviteInfo
