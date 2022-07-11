/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
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
        roles = roles,
        channels = channels.map { it.toModel(channelCopy) },
    )
}