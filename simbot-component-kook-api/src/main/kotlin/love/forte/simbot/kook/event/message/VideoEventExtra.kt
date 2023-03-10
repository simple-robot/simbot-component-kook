/*
 * Copyright (c) 2021-2023. ForteScarlet.
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
 *
 * [视频消息事件](https://developer.kaiheila.cn/doc/event/message#%E8%A7%86%E9%A2%91%E6%B6%88%E6%81%AF)
 *
 * @author ForteScarlet
 */
public interface VideoEventExtra : MessageEventExtra, AttachmentsMessageEventExtra<VideoAttachments> {
    override val guildId: ID
    override val channelName: String
    override val mention: List<ID>
    override val isMentionAll: Boolean
    override val mentionRoles: List<ID>
    override val isMentionHere: Boolean
    override val author: User

    /** 附件信息 */
    override val attachments: VideoAttachments
}

/**
 *
 * [视频消息事件](https://developer.kaiheila.cn/doc/event/message#%E8%A7%86%E9%A2%91%E6%B6%88%E6%81%AF)
 *
 * @author ForteScarlet
 */
@Serializable
internal data class VideoEventExtraImpl(
    override val type: Event.Type = Event.Type.VIDEO,
    @SerialName("guild_id")
    override val guildId: CharSequenceID = CharSequenceID.EMPTY,
    @SerialName("channel_name")
    override val channelName: String = "",
    override val mention: List<CharSequenceID> = emptyList(),
    @SerialName("mention_all")
    override val isMentionAll: Boolean = false,
    @SerialName("mention_roles")
    override val mentionRoles: List<LongID> = emptyList(),
    @SerialName("mention_here")
    override val isMentionHere: Boolean = false,
    /** 附件信息 */
    override val attachments: VideoAttachmentsImpl,
    override val author: UserImpl,
) : VideoEventExtra


public interface VideoAttachments : Attachments {
    override val type: String
    override val name: String
    override val url: String

    /** 文件格式 */
    public val fileType: String

    /** 大小 单位（B） */
    override val size: Long

    /** 视频时长（s） */
    public val duration: Int

    /** 视频宽度 */
    public val width: Int

    /** 视频高度 */
    public val height: Int
}


/**
 * 视频消息的资源信息。
 */
@Serializable
internal data class VideoAttachmentsImpl(
    override val type: String,
    override val name: String,
    override val url: String,
    @SerialName("file_type")
    override val fileType: String,
    override val size: Long,
    override val duration: Int,
    override val width: Int,
    override val height: Int,
) : VideoAttachments

