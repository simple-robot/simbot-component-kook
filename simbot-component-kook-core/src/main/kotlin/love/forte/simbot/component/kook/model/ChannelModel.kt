/*
 * Copyright (c) 2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.component.kook.model

import love.forte.simbot.ID
import love.forte.simbot.kook.objects.Channel
import love.forte.simbot.kook.objects.ChannelPermissionOverwrites

/**
 * 针对于 [Channel] 在核心模块中的可变数据模型。
 */
internal data class ChannelModel(
    override val id: ID,
    override val currentMember: Int,
    override val icon: String,
    override val maximumMember: Int,
    override val name: String,
    override val userId: ID,
    override val guildId: ID,
    override val topic: String,
    override val isCategory: Boolean,
    override val parentId: ID,
    override val level: Int,
    override val slowMode: Int,
    override val type: Int,
    override val permissionOverwrites: List<ChannelPermissionOverwrites>,
    override val permissionUsers: List<ID>,
    override val permissionSync: Int,
) : Channel


/**
 * 将一个 [Channel] 转化为 [ChannelModel].
 */
internal fun Channel.toModel(copy: Boolean = false): ChannelModel {
    if (this is ChannelModel) {
        return if (copy) this.copy() else this
    }
    
    return ChannelModel(
        id = id,
        currentMember = currentMember,
        icon = icon,
        maximumMember = maximumMember,
        name = name,
        userId = userId,
        guildId = guildId,
        topic = topic,
        isCategory = isCategory,
        parentId = parentId,
        level = level,
        slowMode = slowMode,
        type = type,
        permissionOverwrites = permissionOverwrites,
        permissionUsers = permissionUsers,
        permissionSync = permissionSync,
    )
}
