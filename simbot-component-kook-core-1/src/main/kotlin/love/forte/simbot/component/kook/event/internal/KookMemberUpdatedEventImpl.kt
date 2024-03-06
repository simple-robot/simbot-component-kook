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

package love.forte.simbot.component.kook.event.internal

import love.forte.simbot.component.kook.KookGuild
import love.forte.simbot.component.kook.KookMember
import love.forte.simbot.component.kook.bot.internal.KookBotImpl
import love.forte.simbot.component.kook.event.KookMemberUpdatedEvent
import love.forte.simbot.component.kook.internal.KookGuildImpl
import love.forte.simbot.component.kook.internal.KookMemberImpl
import love.forte.simbot.kook.event.Event
import love.forte.simbot.kook.event.UpdatedGuildMemberEventExtra
import love.forte.simbot.kook.objects.User


/**
 *
 * @author ForteScarlet
 */
internal class KookMemberUpdatedEventImpl(
    override val bot: KookBotImpl,
    override val sourceEvent: Event<UpdatedGuildMemberEventExtra>,
    private val sourceGuild: KookGuildImpl,
    private val newMember: KookMemberImpl,
    private val beforeMember: KookMemberImpl,
    override val sourceEventContent: String
) : KookMemberUpdatedEvent() {
    override val beforeSource: User
        get() = beforeMember.source

    override suspend fun guild(): KookGuild = sourceGuild

    override suspend fun member(): KookMember = newMember

    override suspend fun before(): KookMember = beforeMember
}
