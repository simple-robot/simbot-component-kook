package love.forte.simbot.kaiheila.objects.impl

import kotlinx.serialization.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.objects.*

@Serializable
public data class RoleImpl(
    @SerialName("role_id")
    override val roleId: IntID,
    override val name: String,
    override val color: Int,
    override val position: Int,
    override val hoist: Int,
    override val mentionable: Int,
    override val permissions: Permissions,
) : Role