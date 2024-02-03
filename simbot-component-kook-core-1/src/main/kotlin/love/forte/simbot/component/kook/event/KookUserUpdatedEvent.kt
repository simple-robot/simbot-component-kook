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

package love.forte.simbot.component.kook.event

import love.forte.simbot.ID
import love.forte.simbot.JSTP
import love.forte.simbot.Timestamp
import love.forte.simbot.event.BaseEventKey
import love.forte.simbot.event.ChangedEvent
import love.forte.simbot.event.Event
import love.forte.simbot.kook.event.UserUpdatedEventBody
import love.forte.simbot.kook.event.UserUpdatedEventExtra
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.kook.event.Event as KEvent


/**
 * Kook 用户信息更新事件。
 * 此事件属于一个 [ChangedEvent],
 * [变更源][ChangedEvent.source] 为发送变更的用户 **的ID**
 * （因为此事件不一定是某个具体频道服务器中的用户，只要有好友关系即会推送），
 * [变更前][ChangedEvent.before] 由于无法获取而始终为null，
 * [变更后][ChangedEvent.after] 为用户变更事件的内容本体，即 [sourceBody]。
 *
 * @see UserUpdatedEventExtra
 */
public abstract class KookUserUpdatedEvent : KookSystemEvent(), ChangedEvent {
    override val changedTime: Timestamp get() = Timestamp.ofMilliseconds(sourceEvent.msgTimestamp)

    abstract override val sourceEvent: KEvent<UserUpdatedEventExtra>

    override val sourceBody: UserUpdatedEventBody
        get() = sourceEvent.extra.body

    /**
     * 变化源。为发生变更的用户的id。
     */
    @STP
    override suspend fun source(): ID = sourceBody.userId.ID

    /**
     * before 无法确定，始终为null。
     */
    @STP
    override suspend fun before(): Any? = null

    /**
     * 变化事件的主要内容。
     */
    @STP
    override suspend fun after(): UserUpdatedEventBody = sourceBody


    override val key: Event.Key<out KookUserUpdatedEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookUserUpdatedEvent>(
        "kook.user_updated", KookSystemEvent, ChangedEvent
    ) {
        override fun safeCast(value: Any): KookUserUpdatedEvent? = doSafeCast(value)
    }
}
