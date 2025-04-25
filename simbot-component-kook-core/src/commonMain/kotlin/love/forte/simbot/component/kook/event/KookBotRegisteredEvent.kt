/*
 *     Copyright (c) 2022-2024. ForteScarlet.
 *
 *     This file is part of simbot-component-kook.
 *
 *     simbot-component-kook is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     simbot-component-kook is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with simbot-component-kook,
 *     If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.component.kook.event

import love.forte.simbot.annotations.ExperimentalSimbotAPI
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.UUID
import love.forte.simbot.common.time.Timestamp
import love.forte.simbot.component.kook.bot.KookBot
import love.forte.simbot.component.kook.bot.KookBotManager
import love.forte.simbot.event.BotRegisteredEvent
import love.forte.simbot.event.FuzzyEventTypeImplementation

/**
 * 当一个 [KookBot] 在 [KookBotManager] 中被_注册_时。
 *
 * @author ForteScarlet
 */
@OptIn(FuzzyEventTypeImplementation::class)
public abstract class KookBotRegisteredEvent : KookEvent(), BotRegisteredEvent {
    override val id: ID = UUID.random()

    @OptIn(ExperimentalSimbotAPI::class)
    override val time: Timestamp = Timestamp.now()
    abstract override val bot: KookBot

    override fun toString(): String {
        return "KookBotRegisteredEvent(bot=$bot)"
    }
}
