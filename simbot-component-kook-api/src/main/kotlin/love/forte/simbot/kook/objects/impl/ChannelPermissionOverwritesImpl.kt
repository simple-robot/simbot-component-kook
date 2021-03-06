package love.forte.simbot.kook.objects.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.kook.objects.ChannelPermissionOverwrites

@Serializable
internal data class ChannelPermissionOverwritesImpl(
    @SerialName("role_id")
    override val roleId: Int,
    override val allow: Int,
    override val deny: Int,
) : ChannelPermissionOverwrites