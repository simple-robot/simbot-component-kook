/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook,
 * If not, see <https://www.gnu.org/licenses/>.
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
