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

