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
import love.forte.simbot.Timestamp
import love.forte.simbot.component.kook.KookComponentBot
import love.forte.simbot.event.internal.BaseInternalKey
import love.forte.simbot.event.internal.BotRegisteredEvent
import love.forte.simbot.event.internal.InternalEvent
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.randomID

/**
 *
 * @author ForteScarlet
 */
public abstract class KookBotRegisteredEvent : BotRegisteredEvent() {

    override val id: ID = randomID()
    override val timestamp: Timestamp = Timestamp.now()
    abstract override val bot: KookComponentBot
    override val key: InternalEvent.Key<KookBotRegisteredEvent>
        get() = Key

    public companion object Key : BaseInternalKey<KookBotRegisteredEvent>(
        "kook.registered", BotRegisteredEvent
    ) {
        override fun safeCast(value: Any): KookBotRegisteredEvent? = doSafeCast(value)
    }
}
