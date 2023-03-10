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

package love.forte.simbot.kook.objects.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.kook.objects.Channel

@Serializable
internal data class ChannelImpl(
    override val id: CharSequenceID,
    override val name: String,
    @SerialName("user_id")
    override val userId: CharSequenceID,
    @SerialName("guild_id")
    override val guildId: CharSequenceID,
    override val topic: String,
    @SerialName("is_category")
    override val isCategory: Boolean,
    @SerialName("parent_id")
    override val parentId: CharSequenceID,
    override val level: Int,
    @SerialName("slow_mode")
    override val slowMode: Int,
    override val type: Int,
    @SerialName("permission_overwrites")
    override val permissionOverwrites: List<ChannelPermissionOverwritesImpl> = emptyList(),
    @SerialName("permission_users")
    override val permissionUsers: List<CharSequenceID> = emptyList(),
    @SerialName("permission_sync")
    override val permissionSync: Int,

    // 可选的
    override val currentMember: Int = -1,
    override val icon: String = "",
    override val maximumMember: Int = -1
) : Channel

