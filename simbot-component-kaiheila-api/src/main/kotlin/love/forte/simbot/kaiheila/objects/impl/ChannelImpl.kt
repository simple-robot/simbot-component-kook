/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kaiheila 的一部分。
 *
 *  simbot-component-kaiheila 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kaiheila 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

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
) : Channel {

}

