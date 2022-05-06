/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kaiheila.event.system.channel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.kaiheila.util.BooleanToIntSerializer


/**
 * 新增频道
 *
 * added_channel
 *
 * @author ForteScarlet
 */
public interface AddedChannelExtraBody : ChannelEventExtraBody, Comparable<AddedChannelExtraBody> {

    /**
     * 服务器频道ID
     */
    public val id: ID

    /**
     * 服务器id
     */
    public val guildId: ID

    /**
     * 频道创建者id
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

    override fun compareTo(other: AddedChannelExtraBody): Int = level.compareTo(other.level)
}

/**
 * 新增频道
 *
 * added_channel
 *
 * @author ForteScarlet
 */
@Serializable
internal data class AddedChannelExtraBodyImpl(
    /**
     * 服务器频道ID
     */
    override val id: CharSequenceID,
    /**
     * 服务器id
     */
    @SerialName("guild_id")
    override val guildId: CharSequenceID,
    
    /**
     * 频道创建者id
     */
    @SerialName("user_id")
    override val masterId: CharSequenceID,

    /**
     * 父分组频道id
     */
    @SerialName("parent_id")
    override val parentId: CharSequenceID,

    /**
     * 频道名称
     */
    override val name: String,

    /**
     * 频道简介
     */
    override val topic: String,

    /**
     * 频道类型，1为文字频道，2为语音频道
     *
     */
    override val type: Int,

    /**
     * 频道排序
     */
    @SerialName("level")
    private val _level: Int? = null,

    /**
     * 慢速限制，单位秒。用户发送消息之后再次发送消息的等待时间。
     */
    @SerialName("slow_mode")
    override val slowMode: Int = -1,

    /**
     * 人数限制（如果为语音频道）
     */
    @SerialName("limit_amount")
    override val limitAmount: Int = -1,

    /**
     * 是否为分组类型
     */
    @SerialName("is_category")
    @Serializable(BooleanToIntSerializer::class)
    override val isCategory: Boolean = false,

    /**
     * 语音服务器地址，HOST:PORT的格式
     */
    @SerialName("server_url")
    override val serverUrl: String = ""
) : AddedChannelExtraBody {
    init {
        check(type in 1..2) { "Parameter type must be 1 or 2, but $type" }
    }

    override val level: Int
        get() = _level ?: -1

}
