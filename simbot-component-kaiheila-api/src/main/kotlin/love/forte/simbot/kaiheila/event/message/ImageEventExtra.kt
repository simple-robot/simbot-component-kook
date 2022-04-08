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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.LongID
import love.forte.simbot.kaiheila.event.Event
import love.forte.simbot.kaiheila.objects.Attachments
import love.forte.simbot.kaiheila.objects.User
import love.forte.simbot.kaiheila.objects.UserImpl
import love.forte.simbot.kaiheila.objects.impl.AttachmentsImpl

/**
 * [图片消息事件](https://developer.kaiheila.cn/doc/event/message#%E5%9B%BE%E7%89%87%E6%B6%88%E6%81%AF)
 *
 * @author ForteScarlet
 */
public interface ImageEventExtra : MessageEventExtra, AttachmentsMessageEventExtra<Attachments> {
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
    override val attachments: Attachments

}

/**
 * [图片消息事件](https://developer.kaiheila.cn/doc/event/message#%E5%9B%BE%E7%89%87%E6%B6%88%E6%81%AF)
 *
 * @author ForteScarlet
 */
@Serializable
internal data class ImageEventExtraImpl(
    override val type: Event.Type = Event.Type.IMAGE,
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
    /**
     * 附件
     */
    override val attachments: AttachmentsImpl,
    override val author: UserImpl,
) : ImageEventExtra

//
// /**
//  * 图片事件。
//  */
// @Serializable
// internal sealed class ImageEventImpl : AbstractMessageEvent<ImageEventExtra>(), ImageEvent {
//     override val type: Event.Type
//         get() = Event.Type.IMAGE
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
//         override val extra: ImageEventExtra,
//     ) : ImageEventImpl(), GroupMsg, ImageEvent.Group {
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
//             override val type: Event.Type get() = Event.Type.IMAGE
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
//         override val extra: ImageEventExtra,
//     ) : ImageEventImpl(), PrivateMsg, ImageEvent.Person {
//         override val channelType: Channel.Type
//             get() = Channel.Type.PERSON
//
//         override val privateMsgType: PrivateMsg.Type
//             get() = PrivateMsg.Type.FRIEND
//
//         override val flag: MessageGet.MessageFlag<PrivateMsg.FlagContent> = MessageFlag(PrivateMsgIdFlagContent(msgId))
//
//         companion object : EventLocatorRegistrarCoordinate<Person> {
//             override val type: Event.Type get() = Event.Type.IMAGE
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
//     protected override fun initMessageContent(): MessageContent = attachmentsEventMessageContent("image", extra)
//
//     internal companion object {
//         internal fun EventLocator.registerCoordinates() {
//             registerCoordinate(Group)
//             registerCoordinate(Person)
//         }
//     }
//
// }