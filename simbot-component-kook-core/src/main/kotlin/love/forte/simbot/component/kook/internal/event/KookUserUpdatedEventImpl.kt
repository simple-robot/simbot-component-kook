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

package love.forte.simbot.component.kook.internal.event

import love.forte.simbot.component.kook.KookComponentBot
import love.forte.simbot.component.kook.event.KookUserUpdatedEvent
import love.forte.simbot.event.ChangedEvent
import love.forte.simbot.kook.event.Event.Extra.Sys
import love.forte.simbot.kook.event.system.user.UserUpdatedEvent
import love.forte.simbot.kook.event.system.user.UserUpdatedEventBody
import love.forte.simbot.kook.event.Event as KkEvent

/**
 * Kook 用户信息更新事件。
 * 此事件属于一个 [ChangedEvent],
 * 变更的[源][ChangedEvent.source]为发送变更的用户 **的ID**
 * （因为此事件不一定是某个具体频道服务器中的用户，只要有好友关系即会推送），
 * 变更的 [前][ChangedEvent.before]由于无法获取而始终为null，
 * [后][ChangedEvent.after] 为用户变更事件的内容本体，即 [UserUpdatedEventBody] 。
 *
 * @see UserUpdatedEvent
 */
internal data class KookUserUpdatedEventImpl(
    override val bot: KookComponentBot,
    override val sourceEvent: KkEvent<Sys<UserUpdatedEventBody>>
) : KookUserUpdatedEvent()