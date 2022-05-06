package love.forte.simbot.component.kaiheila.model

import love.forte.simbot.ID
import love.forte.simbot.kaiheila.objects.Channel
import love.forte.simbot.kaiheila.objects.ChannelPermissionOverwrites

/**
 * 针对于 [Channel] 在核心模块中的可变数据模型。
 */
internal data class ChannelModel(
    override var id: ID,
    override var currentMember: Int,
    override var icon: String,
    override var maximumMember: Int,
    override var name: String,
    override var userId: ID,
    override var guildId: ID,
    override var topic: String,
    override var isCategory: Boolean,
    override var parentId: ID,
    override var level: Int,
    override var slowMode: Int,
    override var type: Int,
    override var permissionOverwrites: List<ChannelPermissionOverwrites>,
    override var permissionUsers: List<ID>,
    override var permissionSync: Int,
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
