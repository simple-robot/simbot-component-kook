/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.api.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.LongID
import love.forte.simbot.kook.api.ApiResultType
import love.forte.simbot.kook.objects.impl.AttachmentsImpl
import love.forte.simbot.kook.objects.impl.QuoteImpl


/**
 *
 * 频道的[消息详情](https://developer.kaiheila.cn/doc/http/message#%E6%B6%88%E6%81%AF%E8%AF%A6%E6%83%85%E5%8F%82%E6%95%B0%E8%AF%B4%E6%98%8E)
 *
 */
@Serializable
internal data class ChannelMessageDetailsImpl @ApiResultType constructor(
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
    override val author: AuthorImpl,
    
    /**
     * 消息内容
     */
    override val content: String,
    
    /**
     * at特定用户 的用户ID数组，与 mention_info 中的数据对应
     */
    override val mention: List<CharSequenceID> = emptyList(),
    
    /**
     * 是否含有 @全体人员
     */
    @SerialName("mention_all")
    override val isMentionAll: Boolean,
    
    /**
     * at特定角色 的角色ID数组，与 mention_info 中的数据对应
     */
    @SerialName("mention_roles")
    override val mentionRoles: List<CharSequenceID> = emptyList(),
    
    /**
     * 是否含有 @在线人员
     */
    @SerialName("mention_here")
    override val isMentionHere: Boolean,
    
    /**
     * 超链接解析数据
     */
    override val embeds: List<Map<String, String>>,
    
    /**
     * 附加的多媒体数据
     */
    override val attachments: AttachmentsImpl,
    
    
    /**
     * 回应信息
     */
    override val reactions: List<ReactionImpl>,
    
    
    /**
     * 引用消息
     */
    override val quote: QuoteImpl? = null,
    
    @SerialName("mention_info")
    override val mentionInfo: MentionInfo,
) : ChannelMessageDetails {
    override val authorId: CharSequenceID
        get() = author.id
}


@Serializable
internal data class DirectMessageDetailsImpl(
    override val id: CharSequenceID,
    override val type: Int,
    @SerialName("author_id")
    override val authorId: CharSequenceID,
    override val content: String,
    override val embeds: List<Map<String, String>>,
    override val attachments: AttachmentsImpl,
    override val reactions: List<ReactionImpl>,
    override val quote: QuoteImpl?,
    @SerialName("read_status")
    override val readStatus: Boolean,
) : DirectMessageDetails


/**
 * [ChannelMessageDetails] 中的 [作者信息][ChannelMessageDetails.author]
 */
@Serializable
internal data class AuthorImpl @ApiResultType constructor(
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
    override val roles: List<LongID> = emptyList(),
) : Author


@Serializable
internal data class ReactionImpl @ApiResultType constructor(
    override val emoji: Emoji,
    override val count: Int,
    override val me: Boolean,
) : Reaction
