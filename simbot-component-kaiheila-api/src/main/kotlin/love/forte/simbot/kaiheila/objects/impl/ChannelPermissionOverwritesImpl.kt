package love.forte.simbot.kaiheila.objects.impl

import kotlinx.serialization.*
import love.forte.simbot.kaiheila.objects.*

@Serializable
internal data class ChannelPermissionOverwritesImpl(
    @SerialName("role_id")
    override val roleId: Int,
    override val allow: Int,
    override val deny: Int,
) : ChannelPermissionOverwrites