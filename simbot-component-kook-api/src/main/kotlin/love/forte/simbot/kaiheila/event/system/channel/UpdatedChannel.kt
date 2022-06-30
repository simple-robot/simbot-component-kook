/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kaiheila.event.system.channel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.kaiheila.util.BooleanToIntSerializer

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