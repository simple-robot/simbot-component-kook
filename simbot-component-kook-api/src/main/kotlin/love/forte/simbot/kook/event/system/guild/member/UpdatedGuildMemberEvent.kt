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

package love.forte.simbot.kook.event.system.guild.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID

/**
 * [服务器成员信息更新](https://developer.kook.cn/doc/event/guild-member#%E6%9C%8D%E5%8A%A1%E5%99%A8%E6%88%90%E5%91%98%E4%BF%A1%E6%81%AF%E6%9B%B4%E6%96%B0)
 *
 * `updated_guild_member`
 *
 * @author ForteScarlet
 */
public interface UpdatedGuildMemberEventBody : GuildMemberEventExtraBody {

    /**
     * User ID
     */
    public val userId: ID

    /**
     * 昵称
     */
    public val nickname: String
}

/**
 * 服务器成员信息更新
 *
 * `updated_guild_member`
 *
 * @author ForteScarlet
 */
@Serializable
internal data class UpdatedGuildMemberEventBodyImpl(
    /**
     * User ID
     */
    @SerialName("user_id")
    override val userId: CharSequenceID,
    /**
     * 昵称
     */
    override val nickname: String,
) : UpdatedGuildMemberEventBody


