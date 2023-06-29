/*
 * Copyright (c) 2023. ForteScarlet.
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
import love.forte.simbot.Timestamp
import love.forte.simbot.event.BaseEventKey
import love.forte.simbot.event.Event
import love.forte.simbot.kook.event.system.user.MessageBtnClickEventBody
import love.forte.simbot.message.doSafeCast


/**
 * 一个 `Card` 中的按钮被按下的事件。
 *
 * @see MessageBtnClickEventBody
 *
 * @author ForteScarlet
 */
public abstract class KookMessageBtnClickEvent : KookSystemEvent<MessageBtnClickEventBody>() {
    override val timestamp: Timestamp
        get() = sourceEvent.msgTimestamp

    /**
     * 按钮 `return-val` 时的返回值
     *
     * @see MessageBtnClickEventBody.value
     */
    public val value: String get() = sourceBody.value


    /**
     * 点击的用户ID
     *
     * @see MessageBtnClickEventBody.userId
     */
    public val userId: ID get() = sourceBody.userId


    override val key: Event.Key<out KookMessageBtnClickEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookMessageBtnClickEvent>("kook.message_btn_click", KookSystemEvent) {
        override fun safeCast(value: Any): KookMessageBtnClickEvent? = doSafeCast(value)
    }
}



