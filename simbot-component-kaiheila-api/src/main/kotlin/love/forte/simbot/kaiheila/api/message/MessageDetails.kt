/*
 *
 *  * Copyright (c) 2021. ForteScarlet All rights reserved.
 *  * Project  simple-robot
 *  * File     MiraiAvatar.kt
 *  *
 *  * You can contact the author through the following channels:
 *  * github https://github.com/ForteScarlet
 *  * gitee  https://gitee.com/ForteScarlet
 *  * email  ForteScarlet@163.com
 *  * QQ     1149159218
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
    public val id: CharSequenceID

    /**
     * 消息类型
     *
     * @see MessageType
     */
    public val type: Int

    /**
     * 作者ID
     */
    public val authorId: CharSequenceID

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
    public val quote: Map<String, String>?

}


/**
 *
 * 频道的[消息详情](https://developer.kaiheila.cn/doc/http/message#%E6%B6%88%E6%81%AF%E8%AF%A6%E6%83%85%E5%8F%82%E6%95%B0%E8%AF%B4%E6%98%8E)
 *
 */
@Serializable
public data class ChannelMessageDetails @ApiResultType constructor(
    /**
     * 	消息 id
     */
    override val id: CharSequenceID,

    /**
     * 消息类型
     * @see MessageType
     */
    override val type: Int,

    /**
     * 	作者的用户信息
     */
    val author: Author,

    /**
     * 消息内容
     */
    override val content: String,

    /**
     * at特定用户 的用户ID数组，与 mention_info 中的数据对应
     */
    val mention: List<String> = emptyList(),

    /**
     * 是否含有 @全体人员
     */
    @SerialName("mention_all")
    val isMentionAll: Boolean,

    /**
     * at特定角色 的角色ID数组，与 mention_info 中的数据对应
     */
    @SerialName("mention_roles")
    val mentionRoles: List<CharSequenceID> = emptyList(),

    /**
     * 是否含有 @在线人员
     */
    @SerialName("mention_here")
    val isMentionHere: Boolean,

    /**
     * 超链接解析数据
     */
    override val embeds: List<Map<String, String>>,

    /**
     * 附加的多媒体数据
     */
    override val attachments: List<Map<String, String>>,


    /**
     * 回应信息
     */
    override val reactions: List<Reaction>,


    /**
     * 引用消息
     */
    override val quote: Map<String, String>? = null,


    @SerialName("mention_info")
    val mentionInfo: MentionInfo,


    ) : MessageDetails {

    override val authorId: CharSequenceID
        get() = author.id
}


/**
 * [ChannelMessageDetails] 中的 [作者信息][ChannelMessageDetails.author]
 */
@Serializable
public data class Author @ApiResultType constructor(
    override val id: CharSequenceID,
    override val username: String,
    @SerialName("online")
    override val isOnline: Boolean,
    override val avatar: String,

    @SerialName("vip_avatar")
    override val vipAvatar: String? = null,
    // maybe miss
    @SerialName("identify_num")
    override val identifyNum: String = "",
    override val status: Int = 0,
    @SerialName("bot")
    override val isBot: Boolean = false,
    @SerialName("mobile_verified")
    override val mobileVerified: Boolean = false,
    override val nickname: String = "",
    override val roles: List<Int> = emptyList(),
) : User


/**
 * 私聊消息的内容详情
 */
@Serializable
public data class DirectMessageDetails @ApiResultType constructor(
    override val id: CharSequenceID,
    override val type: Int,
    @SerialName("author_id")
    override val authorId: CharSequenceID,
    override val content: String,
    override val embeds: List<Map<String, String>>,
    override val attachments: List<Map<String, String>>,
    override val reactions: List<Reaction>,
    override val quote: Map<String, String>?,
    @SerialName("read_status")
    val readStatus: Boolean,
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
public data class Reaction @ApiResultType constructor(
    val emoji: Emoji,
    val count: Int,
    val me: Boolean,
)


/**
 * 一个 `emoji`.
 */
@Serializable
public data class Emoji @ApiResultType constructor(val id: String, val name: String)


@Serializable
public data class MentionInfo @ApiResultType constructor(
    @SerialName("mention_part")
    val mentionPart: List<MentionPart>,

    @SerialName("mention_role_part")
    val mentionRolePart: List<Role>,
)
