/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.event.system.message

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


