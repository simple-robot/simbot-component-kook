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
 * [自己新加入服务器](https://developer.kaiheila.cn/doc/event/user#%E8%87%AA%E5%B7%B1%E6%96%B0%E5%8A%A0%E5%85%A5%E6%9C%8D%E5%8A%A1%E5%99%A8)
 *
 *
 * 当自己被邀请或主动加入新的服务器时, 产生该事件（对于机器人来说，就是机器人被邀请进入新服务器）
 *
 *
 * type: [UserEventSubTypeConstants.SELF_JOINED_GUILD]
 *
 */
public interface SelfJoinedGuildEventBody {
    /**
     * 频道ID
     */
    public val guildId: ID
}



@Serializable
internal data class SelfJoinedGuildEventBodyImpl(@SerialName("guild_id") override val guildId: CharSequenceID): SelfJoinedGuildEventBody


