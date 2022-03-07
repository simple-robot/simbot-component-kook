/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kaiheila.event.message

import kotlinx.serialization.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.event.*
import love.forte.simbot.kaiheila.objects.*


/**
 * [文件消息](https://developer.kaiheila.cn/doc/event/message#%E6%96%87%E4%BB%B6%E6%B6%88%E6%81%AF)
 * @author ForteScarlet
 */
public interface FileEventExtra : MessageEventExtra, AttachmentsMessageEventExtra<FileAttachments> {
    override val type: Event.Type
    override val guildId: ID
    override val channelName: String

    override val mention: List<ID>
    override val mentionAll: Boolean
    override val mentionRoles: List<ID>
    override val mentionHere: Boolean
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
    override val mentionAll: Boolean = false,
    override val mentionRoles: List<LongID> = emptyList(),
    override val mentionHere: Boolean = false,
    /**
     * 附件
     */
    override val attachments: FileAttachments,
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

//
// @Serializable
// internal sealed class FileEventImpl : AbstractMessageEvent<FileEventExtra>(), FileEvent {
//     override val type: Event.Type
//         get() = Event.Type.FILE
//
//     /**
//      * 群消息.
//      */
//     @Serializable
//     public data class Group(
//         @SerialName("target_id")
//         override val targetId: String,
//         @SerialName("author_id")
//         override val authorId: String,
//         override val content: String,
//         @SerialName("msg_id")
//         override val msgId: String,
//         @SerialName("msg_timestamp")
//         override val msgTimestamp: Long,
//         override val nonce: String,
//         override val extra: FileEventExtra,
//     ) : FileEventImpl(), GroupMsg, FileEvent.Group {
//
//
//         override val channelType: Channel.Type get() = Channel.Type.GROUP
//         override val groupMsgType: GroupMsg.Type = if (authorId == "1") GroupMsg.Type.SYS else GroupMsg.Type.NORMAL
//
//         @Transient
//         override val flag: MessageGet.MessageFlag<GroupMsg.FlagContent> =
//             MessageFlag(GroupMsgIdFlagContent(msgId))
//
//         //region GroupAccountInfo Ins
//         private inner class ImageEventGroupAccountInfo : GroupAccountInfo, GroupInfo, GroupBotInfo {
//             override val accountCode: String get() = extra.author.accountCode
//             override val accountNickname: String get() = extra.author.accountNickname
//             override val accountRemark: String? get() = extra.author.accountRemark
//             override val accountAvatar: String get() = extra.author.accountAvatar
//
//             @Suppress("DEPRECATION")
//             override val accountTitle: String?
//                 get() = extra.author.accountTitle
//
//             override val botCode: String get() = bot.botCode
//             override val botCodeNumber: Long get() = bot.botCodeNumber
//             override val botName: String get() = bot.botName
//             override val botAvatar: String? get() = bot.botAvatar
//
//             @Suppress("DEPRECATION")
//             override val permission: Permissions
//                 get() = extra.author.permission
//
//             override val groupAvatar: String?
//                 get() = null // TODO("Not yet implemented")
//
//             override val parentCode: String get() = extra.guildId
//             override val groupCode: String get() = targetId
//             override val groupName: String get() = extra.channelName
//         }
//
//         @Transient
//         private val textEventGroupAccountInfo = ImageEventGroupAccountInfo()
//
//         override val permission: Permissions get() = textEventGroupAccountInfo.permission
//         override val accountInfo: GroupAccountInfo get() = textEventGroupAccountInfo
//         override val groupInfo: GroupInfo get() = textEventGroupAccountInfo
//         override val botInfo: GroupBotInfo get() = textEventGroupAccountInfo
//         //endregion
//
//         /**
//          * Event coordinate.
//          */
//         companion object Coordinate : EventLocatorRegistrarCoordinate<Group> {
//             override val type: Event.Type get() = Event.Type.FILE
//
//             override val channelType: Channel.Type get() = Channel.Type.GROUP
//
//             override val extraType: String
//                 get() = type.type.toString()
//
//             override fun coordinateSerializer(): KSerializer<Group> = serializer()
//         }
//     }
//
//     /**
//      * 私聊消息.
//      */
//     @Serializable
//     public data class Person(
//         @SerialName("target_id")
//         override val targetId: String,
//         @SerialName("author_id")
//         override val authorId: String,
//         override val content: String,
//         @SerialName("msg_id")
//         override val msgId: String,
//         @SerialName("msg_timestamp")
//         override val msgTimestamp: Long,
//         override val nonce: String,
//         override val extra: FileEventExtra,
//     ) : FileEventImpl(), PrivateMsg, FileEvent.Person {
//         override val channelType: Channel.Type
//             get() = Channel.Type.PERSON
//
//         override val privateMsgType: PrivateMsg.Type
//             get() = PrivateMsg.Type.FRIEND
//
//         override val flag: MessageGet.MessageFlag<PrivateMsg.FlagContent> = MessageFlag(PrivateMsgIdFlagContent(msgId))
//
//         companion object : EventLocatorRegistrarCoordinate<Person> {
//             override val type: Event.Type get() = Event.Type.FILE
//
//             override val channelType: Channel.Type get() = Channel.Type.PERSON
//
//             override val extraType: String
//                 get() = type.type.toString()
//
//             override fun coordinateSerializer(): KSerializer<Person> = serializer()
//         }
//     }
//
//
//     protected override fun initMessageContent(): MessageContent = attachmentsEventMessageContent("file", extra)
//
//     internal companion object {
//         internal fun EventLocator.registerCoordinates() {
//             registerCoordinate(Group)
//             registerCoordinate(Person)
//         }
//     }
// }
//
//
//