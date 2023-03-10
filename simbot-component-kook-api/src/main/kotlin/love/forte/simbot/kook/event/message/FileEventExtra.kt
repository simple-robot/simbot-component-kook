/*
 * Copyright (c) 2021-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.event.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.LongID
import love.forte.simbot.kook.event.Event
import love.forte.simbot.kook.objects.Attachments
import love.forte.simbot.kook.objects.User
import love.forte.simbot.kook.objects.UserImpl


/**
 * [文件消息](https://developer.kaiheila.cn/doc/event/message#%E6%96%87%E4%BB%B6%E6%B6%88%E6%81%AF)
 * @author ForteScarlet
 */
public interface FileEventExtra : MessageEventExtra, AttachmentsMessageEventExtra<FileAttachments> {
    override val type: Event.Type
    override val guildId: ID
    override val channelName: String

    override val mention: List<ID>
    override val isMentionAll: Boolean
    override val mentionRoles: List<ID>
    override val isMentionHere: Boolean
    override val author: User

    /**
     * 附件
     */
    override val attachments: FileAttachments
}

/**
 * [文件消息](https://developer.kaiheila.cn/doc/event/message#%E6%96%87%E4%BB%B6%E6%B6%88%E6%81%AF)
 * @author ForteScarlet
 */
@Serializable
internal data class FileEventExtraImpl(
    override val type: Event.Type = Event.Type.FILE,
    @SerialName("guild_id")
    override val guildId: CharSequenceID = CharSequenceID.EMPTY,
    @SerialName("channel_name")
    override val channelName: String = "",

    override val mention: List<CharSequenceID> = emptyList(),
    override val isMentionAll: Boolean = false,
    override val mentionRoles: List<LongID> = emptyList(),
    override val isMentionHere: Boolean = false,
    /**
     * 附件
     */
    override val attachments: FileAttachmentsImpl,
    override val author: UserImpl,
) : FileEventExtra



/**
 * 文件的信息。
 */
public interface FileAttachments : Attachments {
    override val type: String
    override val url: String
    override val name: String
    override val size: Long
    public val fileType: String
}


@Serializable
internal data class FileAttachmentsImpl(
    override val type: String,
    override val url: String,
    override val name: String,
    override val size: Long,
    @SerialName("file_type")
    override val fileType: String
) : FileAttachments

