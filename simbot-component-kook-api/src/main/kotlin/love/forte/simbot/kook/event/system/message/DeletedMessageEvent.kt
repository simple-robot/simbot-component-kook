/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook,
 * If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.event.system.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * 频道消息被删除
 *
 * type: [MessageEvents.DELETED_MESSAGE]
 */
public interface DeletedMessageEventBody : MessageEventExtraBody {
    /**
     * 被删除的消息id
     */
    @SerialName("msg_id")
    public val msgId: String

    /**
     * 消息所在频道id
     */
    @SerialName("channel_id")
    public val channelId: String
}


/**
 * 频道消息被删除
 *
 * type: [MessageEvents.DELETED_MESSAGE]
 */
@Serializable
internal data class DeletedMessageEventBodyImpl(
    /**
     * 被删除的消息id
     */
    @SerialName("msg_id")
    override val msgId: String,

    /**
     * 消息所在频道id
     */
    @SerialName("channel_id")
    override val channelId: String,
) : DeletedMessageEventBody


