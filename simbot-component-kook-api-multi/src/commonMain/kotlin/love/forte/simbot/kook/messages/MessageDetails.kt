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

package love.forte.simbot.kook.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.kook.api.ApiResultType
import love.forte.simbot.kook.objects.*


/**
 * 对消息的统一描述。
 */
public interface MessageDetails {
    /**
     * ID
     */
    public val id: String

    /**
     * 消息类型
     *
     * @see MessageType
     */
    public val type: Int

    /**
     * 作者ID
     */
    public val authorId: String

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
    public val attachments: Attachments?

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
@Serializable
public data class ChannelMessageDetails(

    /**
     * 	消息 id
     */
    override val id: String,

    /**
     * 消息类型
     * @see MessageType
     */
    override val type: Int,

    /**
     * 	作者的用户信息
     */
    public val author: User,

    /**
     * 消息内容
     *
     * _（为了保障消息正常发出，请不要超过 8000 字符）_
     */
    override val content: String,

    /**
     * at特定用户 的用户ID数组，与 mention_info 中的数据对应
     */
    public val mention: List<String>,

    /**
     * 是否含有 @全体人员
     */
    @SerialName("mention_all") public val isMentionAll: Boolean,

    /**
     * at特定角色 的角色ID数组，与 [mentionInfo] 中的数据对应
     */
    @SerialName("mention_roles") public val mentionRoles: List<Int> = emptyList(),

    /**
     * 是否含有 @在线人员
     */
    @SerialName("mention_here") public val isMentionHere: Boolean = false,

    /**
     * 超链接解析数据
     */
    override val embeds: List<Map<String, String>> = emptyList(),

    /**
     * 附加的多媒体数据
     */
    override val attachments: SimpleAttachments? = null,

    /**
     * 回应信息
     */
    override val reactions: List<Reaction> = emptyList(),

    /**
     * 引用消息
     */
    override val quote: Quote? = null,

    /**
     * 通知信息
     */
    @SerialName("mention_info") public val mentionInfo: MentionInfo

) : MessageDetails {
    override val authorId: String
        get() = author.id
}


/**
 * 私聊消息的内容详情
 *
 * 更多参考 [文档](https://developer.kookapp.cn/doc/http/direct-message#%E6%B6%88%E6%81%AF%E8%AF%A6%E6%83%85%E5%8F%82%E6%95%B0%E8%AF%B4%E6%98%8E)
 *
 */
@Serializable
public data class DirectMessageDetails(
    override val id: String,
    override val type: Int,
    @SerialName("author_id") override val authorId: String,
    override val content: String,
    override val embeds: List<Map<String, String>> = emptyList(),
    override val attachments: Attachments? = null,
    override val reactions: List<Reaction> = emptyList(),
    override val quote: Quote? = null,
    @SerialName("read_status") public val readStatus: Boolean = false,
) : MessageDetails


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
@Serializable
public data class Reaction(
    public val emoji: Emoji, public val count: Int, public val me: Boolean
)


/**
 * 一个 `emoji`.
 */
@Serializable
public data class Emoji @ApiResultType constructor(
    val id: String, val name: String
)


/**
 * 提及（at）信息。
 */
@Serializable
public data class MentionInfo @ApiResultType constructor(
    @SerialName("mention_part") val mentionPart: List<MentionPart>,

    @SerialName("mention_role_part") val mentionRolePart: List<Role>,
)


/**
 * Mention part info.
 */
@Serializable
public data class MentionPart @ApiResultType constructor(
    val id: String,
    val username: String,
    @SerialName("full_name") val fullName: String,
    val avatar: String,
)
