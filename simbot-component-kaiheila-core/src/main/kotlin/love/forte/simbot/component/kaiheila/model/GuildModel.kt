package love.forte.simbot.component.kaiheila.model

import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.kaiheila.objects.Guild
import love.forte.simbot.kaiheila.objects.Role

/**
 * 针对于 [Guild] 在核心模块中的可变数据模型。
 */
internal data class GuildModel(
    override var id: ID,
    override var maximumChannel: Int,
    override var createTime: Timestamp,
    override var currentMember: Int,
    override var maximumMember: Int,
    override var name: String,
    override var icon: String,
    override var topic: String,
    override var masterId: ID,
    override var notifyType: Int,
    override var region: String,
    override var enableOpen: Boolean,
    override var openId: ID,
    override var defaultChannelId: ID,
    override var welcomeChannelId: ID,
    override var roles: List<Role>,
    override var channels: List<ChannelModel>,
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