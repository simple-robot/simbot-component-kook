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

package love.forte.simbot.component.kook.internal

import love.forte.simbot.ID
import love.forte.simbot.component.kook.KookMember
import love.forte.simbot.component.kook.bot.internal.KookBotImpl
import love.forte.simbot.component.kook.kookGuildNotExistsException
import love.forte.simbot.definition.Role
import love.forte.simbot.delegate.getValue
import love.forte.simbot.delegate.stringID
import love.forte.simbot.kook.objects.User
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageReceipt
import love.forte.simbot.utils.item.Items
import kotlin.time.Duration


/**
 *
 * @author ForteScarlet
 */
internal class KookMemberImpl(
    override val bot: KookBotImpl,
    override val source: User,
    private val _guildId: String
) : KookMember {
    override val guildId: ID by stringID { _guildId }

    private val guildValue
        get() = bot.internalGuild(_guildId) ?: throw kookGuildNotExistsException(_guildId)


    override suspend fun send(message: Message): MessageReceipt {
        TODO("Not yet implemented")
    }

    override suspend fun guild(): KookGuildImpl = guildValue

    override val roles: Items<Role>
        get() = Items.emptyItems() // TODO roles

    override suspend fun mute(duration: Duration): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun unmute(): Boolean {
        TODO("Not yet implemented")
    }

    private suspend fun asContact(): KookUserChatImpl = bot.contact(id)

}
