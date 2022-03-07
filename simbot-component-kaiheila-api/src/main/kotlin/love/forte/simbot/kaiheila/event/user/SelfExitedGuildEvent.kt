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

package love.forte.simbot.kaiheila.event.user

import kotlinx.serialization.*
import love.forte.simbot.*

/**
 *
 * [自己退出服务器](https://developer.kaiheila.cn/doc/event/user#%E8%87%AA%E5%B7%B1%E9%80%80%E5%87%BA%E6%9C%8D%E5%8A%A1%E5%99%A8)
 *
 * 当自己被踢出服务器或被拉黑或主动退出服务器时, 产生该事件（对于机器人来说，就是机器人被踢出/拉黑/主动退出新服务器）
 *
 * type: [UserEventSubTypeConstants.SELF_EXITED_GUILD]
 *
 */
public interface SelfExitedGuildEventBody {
    /**
     * 服务器id
     */
    public val guildId: ID
}

/**
 *
 * [自己退出服务器](https://developer.kaiheila.cn/doc/event/user#%E8%87%AA%E5%B7%B1%E9%80%80%E5%87%BA%E6%9C%8D%E5%8A%A1%E5%99%A8)
 *
 * type: `self_exited_guild`
 *
 */
@Serializable
internal data class SelfExitedGuildEventBodyImpl(@SerialName("guild_id") override val guildId: CharSequenceID) :
    SelfExitedGuildEventBody




