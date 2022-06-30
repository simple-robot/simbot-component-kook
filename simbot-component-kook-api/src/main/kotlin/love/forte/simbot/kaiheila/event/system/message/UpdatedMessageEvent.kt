/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kaiheila.event.system.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.LongID
import love.forte.simbot.Timestamp

/**
 * 频道消息更新
 *
 * type: [MessageEvents.UPDATED_MESSAGE].
 */
public interface UpdatedMessageEventBody : MessageEventExtraBody {

    /**
     * 消息id
     */
    public val msgId: ID

    /**
     * 消息所在的频道id
     */
    public val channelId: ID

    /**
     * 消息内容
     */
    public val content: String

    /**
     * At特定用户 的用户ID数组，与mention_info中的数据对应
     */
    public val mention: List<ID>

    /**
     * 是否含有@全体人员
     */
    public val mentionAll: String

    /**
     * At特定角色 的角色ID数组，与mention_info中的数据对应
     */
    public val mentionRoles: List<ID>

    /** */
    public val mentionHere: Boolean

    /**
     * 更新时间戳(毫秒)
     */
    public val updatedAt: Timestamp
}


/**
 * 频道消息更新
 *
 *
 */
@Serializable
internal data class UpdatedMessageEventBodyImpl(
    /**
     * 消息id
     */
    @SerialName("msg_id")
    override val msgId: CharSequenceID,
    /**
     * 消息所在的频道id
     */
    @SerialName("channel_id")
    override val channelId: CharSequenceID,
    /**
     * 消息内容
     */
    override val content: String,
    /**
     * At特定用户 的用户ID数组，与mention_info中的数据对应
     */
    override val mention: List<CharSequenceID>,
    /**
     * 是否含有@全体人员
     */
    @SerialName("mention_all")
    override val mentionAll: String,
    /**
     * At特定角色 的角色ID数组，与mention_info中的数据对应
     */
    @SerialName("metnion_roles")
    override val mentionRoles: List<LongID>,
    /** */
    @SerialName("mention_here")
    override val mentionHere: Boolean,
    /**
     * 更新时间戳(毫秒)
     */
    @SerialName("updated_at")
    override val updatedAt: Timestamp,
) : UpdatedMessageEventBody


