package love.forte.simbot.kaiheila.objects.impl

import kotlinx.serialization.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.objects.*

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
    override val parentId: String,
    override val level: Int,
    @SerialName("slow_mode")
    override val slowMode: Int,
    override val type: Int,
    @SerialName("permission_overwrites")
    override val permissionOverwrites: List<ChannelPermissionOverwritesImpl>,
    @SerialName("permission_users")
    override val permissionUsers: List<String>,
    @SerialName("permission_sync")
    override val permissionSync: Int,

    // 可选的
    override val currentMember: Int = -1,
    override val icon: String = "",
    override val maximumMember: Int = -1
) : Channel {

}

