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

package love.forte.simbot.component.kook.model

import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.kook.objects.Guild
import love.forte.simbot.kook.objects.Role

/**
 * 针对于 [Guild] 在核心模块中的可变数据模型。
 */
internal data class GuildModel(
    override val id: ID,
    override val maximumChannel: Int,
    override val createTime: Timestamp,
    override val currentMember: Int,
    override val maximumMember: Int,
    override val name: String,
    override val icon: String,
    override val topic: String,
    override val masterId: ID,
    override val notifyType: Int,
    override val region: String,
    override val enableOpen: Boolean,
    override val openId: ID,
    override val defaultChannelId: ID,
    override val welcomeChannelId: ID,
    override val roles: List<Role>,
    override val channels: List<ChannelModel>,
) : Guild


/**
 * 将一个 [Guild] 转化为 [GuildModel].
 */
internal fun Guild.toModel(copy: Boolean = false, channelCopy: Boolean = false): GuildModel {
    if (this is GuildModel) {
        return if (copy) this.copy() else this
    }
    
    return GuildModel(
        id = id,
        maximumChannel = maximumChannel,
        createTime = createTime,
        currentMember = currentMember,
        maximumMember = maximumMember,
        name = name,
        icon = icon,
        topic = topic,
        masterId = masterId,
        notifyType = notifyType,
        region = region,
        enableOpen = enableOpen,
        openId = openId,
        defaultChannelId = defaultChannelId,
        welcomeChannelId = welcomeChannelId,
        roles = roles ?: emptyList(),
        channels = channels?.map { it.toModel(channelCopy) } ?: emptyList(),
    )
}
