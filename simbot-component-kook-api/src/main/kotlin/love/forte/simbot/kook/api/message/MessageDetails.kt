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

package love.forte.simbot.kook.api.message

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.ID
import love.forte.simbot.LongID
import love.forte.simbot.kook.api.ApiResultType
import love.forte.simbot.kook.objects.*


/**
 * 对消息的统一描述。
 */
public interface MessageDetails {
    /**
     * ID
     */
    public val id: ID

    /**
     * 消息类型
     *
     * @see MessageType
     */
    public val type: Int

    /**
     * 作者ID
     */
    public val authorId: ID

    /**
     * 消息内容
     */
    public val content: String

    /**
     * 超链接解析数据
     */
    public val embeds: List<Map<String, String>>

    /**
     * 附加的多媒体数据
     */
    public val attachments: Attachments

    /**
     * 消息回应数据
     */
    public val reactions: List<Reaction>

    /**
     * 引用消息
     */
    public val quote: Quote?

}


/**
 *
 * 频道的[消息详情](https://developer.kaiheila.cn/doc/http/message#%E6%B6%88%E6%81%AF%E8%AF%A6%E6%83%85%E5%8F%82%E6%95%B0%E8%AF%B4%E6%98%8E)
 *
 */
public interface ChannelMessageDetails : MessageDetails {
    /**
     * 	消息 id
     */
    override val id: ID

    /**
     * 消息类型
     * @see MessageType
     */
    override val type: Int

    /**
     * 	作者的用户信息
     */
    public val author: Author

    /**
     * 消息内容
     */
    override val content: String

    /**
     * at特定用户 的用户ID数组，与 mention_info 中的数据对应
     */
    public val mention: List<ID>

    /**
     * 是否含有 @全体人员
     */
    public val isMentionAll: Boolean

    /**
     * at特定角色 的角色ID数组，与 mention_info 中的数据对应
     */
    public val mentionRoles: List<ID>

    /**
     * 是否含有 @在线人员
     */
    public val isMentionHere: Boolean

    /**
     * 超链接解析数据
     */
    override val embeds: List<Map<String, String>>

    /**
     * 附加的多媒体数据
     */
    override val attachments: Attachments


    /**
     * 回应信息
     */
    override val reactions: List<Reaction>


    /**
     * 引用消息
     */
    override val quote: Quote?

    /**
     * 通知信息
     */
    public val mentionInfo: MentionInfo

}


/**
 * [ChannelMessageDetails] 中的 [作者信息][ChannelMessageDetails.author], 是 [User] 的实现之一。
 */
public interface Author : User {
    override val id: ID
    override val username: String
    override val isOnline: Boolean
    override val avatar: String
    override val vipAvatar: String?
    override val identifyNum: String
    override val status: Int
    override val isBot: Boolean
    override val mobileVerified: Boolean
    override val nickname: String
    override val roles: List<LongID>
}


/**
 * 私聊消息的内容详情
 */
public interface DirectMessageDetails : MessageDetails {
    override val id: ID
    override val type: Int
    override val authorId: ID
    override val content: String
    override val embeds: List<Map<String, String>>
    override val attachments: Attachments
    override val reactions: List<Reaction>
    override val quote: Quote?
    public val readStatus: Boolean

    public companion object {
        public val serializer: KSerializer<out DirectMessageDetails> = DirectMessageDetailsImpl.serializer()
    }
}


/**
 * 回应信息
 * ```json
 *  "reactions": [
 *      {
 *          "emoji": {
 *              "id": "[#129315;]",
 *              "name": "[#129315;]"
 *           },
 *          "count": 1,
 *          "me": true
 *      }
 *  ],
 * ```
 */
public interface Reaction {
    public val emoji: Emoji
    public val count: Int
    public val me: Boolean
}


/**
 * 一个 `emoji`.
 */
@Serializable
public data class Emoji @ApiResultType constructor(
    @Serializable(ID.AsCharSequenceIDSerializer::class) val id: ID,
    val name: String
)


/**
 * 提及（at）信息。
 */
@Serializable
public data class MentionInfo @ApiResultType constructor(
    @SerialName("mention_part")
    val mentionPart: List<MentionPart>,

    @SerialName("mention_role_part")
    val mentionRolePart: List<Role>,
)
