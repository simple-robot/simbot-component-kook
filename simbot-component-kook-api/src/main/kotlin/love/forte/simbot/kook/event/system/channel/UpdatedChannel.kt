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

package love.forte.simbot.kook.event.system.channel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.kook.util.BooleanToIntSerializer

/**
 * 频道信息更新
 *
 * `updated_channel`
 *
 * @author ForteScarlet
 */
public interface UpdatedChannelExtraBody : ChannelEventExtraBody, Comparable<UpdatedChannelExtraBody> {
    
    /**
     * 服务器频道ID
     */
    public val id: ID
    
    /**
     * 服务器id
     */
    public val guildId: ID
    
    /**
     * 频道创建者id. 事件体中通常为 `user_id` 字段。
     */
    public val masterId: ID
    
    /**
     * 父分组频道id
     */
    public val parentId: ID
    
    /**
     * 频道名称
     */
    public val name: String
    
    /**
     * 频道简介
     */
    public val topic: String
    
    /**
     * 频道类型，1为文字频道，2为语音频道
     *
     */
    public val type: Int
    
    /**
     * 频道排序
     */
    public val level: Int
    
    /**
     * 慢速限制，单位秒。用户发送消息之后再次发送消息的等待时间。
     */
    public val slowMode: Int
    
    /**
     * 人数限制（如果为语音频道）
     */
    public val limitAmount: Int
    
    /**
     * 是否为分组类型
     */
    public val isCategory: Boolean
    
    /**
     * 语音服务器地址，HOST:PORT的格式
     */
    public val serverUrl: String
    
    override fun compareTo(other: UpdatedChannelExtraBody): Int = level.compareTo(other.level)
}


@Serializable
internal data class UpdatedChannelExtraBodyImpl(
    override val id: CharSequenceID,
    @SerialName("guild_id") override val guildId: CharSequenceID,
    @SerialName("user_id") override val masterId: CharSequenceID,
    @SerialName("parent_id") override val parentId: CharSequenceID,
    override val name: String,
    override val topic: String,
    override val type: Int,
    @SerialName("level")
    private val _level: Int? = null,
    @SerialName("slow_mode") override val slowMode: Int = -1,
    @SerialName("limit_amount") override val limitAmount: Int = -1,
    @SerialName("is_category") @Serializable(BooleanToIntSerializer::class) override val isCategory: Boolean,
    @SerialName("server_url") override val serverUrl: String = "",
) : UpdatedChannelExtraBody {
    override val level: Int
        get() = _level ?: -1
}
