/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kook.event.system.channel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.Timestamp


/**
 * 删除频道
 *
 * `deleted_channel`
 *
 * @author ForteScarlet
 */
public interface DeletedChannelExtraBody : ChannelEventExtraBody {
    /**
     * 被删除的频道id
     */
    public val id: ID

    /**
     * 删除操作的时间戳(毫秒)
     */
    @SerialName("deleted_at")
    public val deletedAt: Timestamp
}


/**
 * 删除频道
 *
 * `deleted_channel`
 *
 * @author ForteScarlet
 */
@Serializable
internal data class DeletedChannelExtraBodyImpl(
    /**
     * 被删除的频道id
     */
    override val id: CharSequenceID,
    /**
     * 删除操作的时间戳(毫秒)
     */
    @SerialName("deleted_at")
    override val deletedAt: Timestamp
) : DeletedChannelExtraBody