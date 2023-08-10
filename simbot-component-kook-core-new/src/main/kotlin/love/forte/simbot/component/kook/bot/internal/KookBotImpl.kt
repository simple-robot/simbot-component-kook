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

package love.forte.simbot.component.kook.bot.internal

import love.forte.simbot.ID
import love.forte.simbot.component.kook.KookComponent
import love.forte.simbot.component.kook.bot.KookBot
import love.forte.simbot.component.kook.bot.KookBotManager
import love.forte.simbot.definition.Contact
import love.forte.simbot.definition.Group
import love.forte.simbot.definition.Guild
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.kook.Bot
import love.forte.simbot.logger.LoggerFactory
import love.forte.simbot.message.Image
import love.forte.simbot.utils.item.Items
import org.slf4j.Logger

/**
 *
 * @author ForteScarlet
 */
internal class KookBotImpl(
    override val eventProcessor: EventProcessor,
    override val sourceBot: Bot,
    override val component: KookComponent,
    override val manager: KookBotManager
) : KookBot {


    override val logger: Logger =
        LoggerFactory.getLogger("love.forte.simbot.component.bot.${sourceBot.ticket.clientId}")


    override suspend fun resolveImage(id: ID): Image<*> {
        TODO("Not yet implemented")
    }

    override suspend fun start(): Boolean {
        sourceBot.start()
        TODO("Sync data ")
    }

    override val contacts: Items<Contact>
        get() = TODO("Not yet implemented")

    override suspend fun contact(id: ID): Contact? {
        TODO("Not yet implemented")
    }

    override val groups: Items<Group>
        get() = TODO("Not yet implemented")

    override suspend fun group(id: ID): Group? {
        TODO("Not yet implemented")
    }

    override val guilds: Items<Guild>
        get() = TODO("Not yet implemented")

    override suspend fun guild(id: ID): Guild? {
        TODO("Not yet implemented")
    }

    override fun isMe(id: ID): Boolean {
        TODO("Not yet implemented")
    }

    override val avatar: String
        get() = TODO("Not yet implemented")

    override val username: String
        get() = TODO("Not yet implemented")

}
