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

package love.forte.simbot.kaiheila.api.message

import kotlinx.serialization.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.api.*
import love.forte.simbot.kaiheila.objects.*


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
    public val attachments: List<Map<String, String>>

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
internal interface ChannelMessageDetails : MessageDetails {
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
    val author: AuthorImpl

    /**
     * 消息内容
     */
    override val content: String

    /**
     * at特定用户 的用户ID数组，与 mention_info 中的数据对应
     */
    val mention: List<ID>

    /**
     * 是否含有 @全体人员
     */
    val isMentionAll: Boolean

    /**
     * at特定角色 的角色ID数组，与 mention_info 中的数据对应
     */
    val mentionRoles: List<ID>

    /**
     * 是否含有 @在线人员
     */
    val isMentionHere: Boolean

    /**
     * 超链接解析数据
     */
    override val embeds: List<Map<String, String>>

    /**
     * 附加的多媒体数据
     */
    override val attachments: List<Map<String, String>>


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
    val mentionInfo: MentionInfo

    override val authorId: CharSequenceID
        get() = author.id
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
    override val roles: List<IntID>
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
    override val attachments: List<Map<String, String>>
    override val reactions: List<Reaction>
    override val quote: Quote?
    public val readStatus: Boolean
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
