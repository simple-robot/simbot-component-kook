/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kaiheila.event.system.guild

import kotlinx.serialization.*
import love.forte.simbot.*


/**
 * [服务器信息更新](https://developer.kaiheila.cn/doc/event/guild#%E6%9C%8D%E5%8A%A1%E5%99%A8%E4%BF%A1%E6%81%AF%E6%9B%B4%E6%96%B0)
 *
 * `updated_guild`
 *
 * @author ForteScarlet
 */
public interface UpdatedGuildExtraBody : GuildEventExtraBody {

    /**
     * 服务器id
     */
    public val id: ID
    /**
     * 服务器名称
     */
    public val name: String
    /**
     * 服务器主id
     */
    public val userId: ID
    /**
     * 服务器icon的地址
     */
    public val icon: String
    /**
     * 通知类型, 0代表默认使用服务器通知设置，1代表接收所有通知, 2代表仅@被提及，3代表不接收通知
     */
    public val notifyType: Int
    /**
     * 服务器默认使用语音区域
     */
    public val region: String
    /**
     * 是否为公开服务器
     */
    public val enableOpen: Boolean
    /**
     * 公开服务器id
     */
    public val openId: ID
    /**
     * 默认频道id
     */
    public val defaultChannelId: ID
    /**
     * 欢迎频道id
     */
    public val welcomeChannelId: ID
}



/**
 * 服务器信息更新
 *
 * `updated_guild`
 *
 * @author ForteScarlet
 */
@Serializable
internal data class UpdatedGuildExtraBodyImpl(
    /**
     * 服务器id
     */
    override val id: CharSequenceID,
    override val name: String,
    @SerialName("user_id")
    override val userId: CharSequenceID,
    /**
     * 服务器icon的地址
     */
    override val icon: String,
    /**
     * 通知类型, 0代表默认使用服务器通知设置，1代表接收所有通知, 2代表仅@被提及，3代表不接收通知
     */
    @SerialName("notify_type")
    override val notifyType: Int,
    /**
     * 服务器默认使用语音区域
     */
    override val region: String,
    /**
     * 是否为公开服务器
     */
    @SerialName("enable_open")
   override val enableOpen: Boolean,
    /**
     * 公开服务器id
     */
    @SerialName("open_id")
    override val openId: CharSequenceID,
    /**
     * 默认频道id
     */
    @SerialName("default_channel_id")
    override val defaultChannelId: CharSequenceID,
    /**
     * 欢迎频道id
     */
    @SerialName("welcome_channel_id")
    override val welcomeChannelId: CharSequenceID,
) : UpdatedGuildExtraBody {
    init {
        check(notifyType in 0..3) { "Parameter notify_type must be 0|1|2|3, but $notifyType" }
    }
}

