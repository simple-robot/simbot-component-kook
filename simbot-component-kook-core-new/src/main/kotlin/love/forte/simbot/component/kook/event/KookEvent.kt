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
import love.forte.simbot.component.kook.bot.KookBot
import love.forte.simbot.definition.BotContainer
import love.forte.simbot.delegate.getValue
import love.forte.simbot.delegate.stringID
import love.forte.simbot.event.*
import love.forte.simbot.kook.event.EventExtra
import love.forte.simbot.kook.event.SystemExtra
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.kook.event.Event as KEvent


/**
 *
 * Kook 组件在simbot中的所有事件总类。
 *
 *
 * @see UnsupportedKookEvent
 *
 * @param E Kook api模块中所定义的原始 Kook 事件对象 [KEvent].
 * @param EX Kook api模块中所定义的原始 Kook 事件对象的 [extra][EventExtra] 属性类型。
 *
 * @author ForteScarlet
 */
@BaseEvent
public abstract class KookEvent<out EX : EventExtra, out E : KEvent<EX>> : BotContainer, Event {
    /**
     * 此事件对应的bot示例。
     */
    abstract override val bot: KookBot

    /**
     * 当前事件内部对应的原始事件实体。
     */
    public abstract val sourceEvent: E

    override fun toString(): String {
        return "KookEvent(sourceEvent=$sourceEvent)"
    }

    abstract override val key: Event.Key<out KookEvent<*, *>>


    public companion object Key : BaseEventKey<KookEvent<*, *>>(
        "kook.event", Event
    ) {
        override fun safeCast(value: Any): KookEvent<*, *>? = doSafeCast(value)
    }

}


/**
 * Kook 组件在simbot中的**系统事件**相关的事件总类。
 */
@BaseEvent
public abstract class KookSystemEvent :
    KookEvent<SystemExtra, KEvent<SystemExtra>>() {

    override val id: ID by stringID { sourceEvent.msgId }

    /**
     * [sourceEvent] 中的 `extra.body` 信息。
     *
     * 不同的系统事件的 body 类型可能是截然不同的。
     * [sourceBody] 的具体类型由具体的实现类重写定义。
     *
     */
    public open val sourceBody: Any? get() = sourceEvent.extra.body


    abstract override val key: Event.Key<out KookSystemEvent>

    public companion object Key : BaseEventKey<KookSystemEvent>(
        "kook.system_event", KookEvent
    ) {
        override fun safeCast(value: Any): KookSystemEvent? = doSafeCast(value)
    }
}
