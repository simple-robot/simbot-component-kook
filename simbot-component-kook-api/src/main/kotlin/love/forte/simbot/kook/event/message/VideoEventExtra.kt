/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
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

