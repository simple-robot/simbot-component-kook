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
import love.forte.simbot.component.kook.KookGuild
import love.forte.simbot.component.kook.KookMember
import love.forte.simbot.component.kook.bot.KookBotManager
import love.forte.simbot.component.kook.bot.KookGuildBot
import love.forte.simbot.definition.Contact
import love.forte.simbot.definition.GuildMember
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.kook.Bot
import love.forte.simbot.message.Image
import love.forte.simbot.utils.item.Items
import org.slf4j.Logger


/**
 *
 * @author ForteScarlet
 */
internal class KookGuildBotImpl(
    override val bot: KookBotImpl,
    internal val member: KookMember
) : KookGuildBot {
    override val eventProcessor: EventProcessor
        get() = bot.eventProcessor

    override val logger: Logger
        get() = bot.logger

    override suspend fun resolveImage(id: ID): Image<*> =
        bot.resolveImage(id)

    override suspend fun start(): Boolean =
        bot.start()

    override val contacts: Items<Contact>
        get() = TODO("Not yet implemented")

    override suspend fun contact(id: ID): Contact? {
        TODO("Not yet implemented")
    }

    override suspend fun asMember(): GuildMember = member

    override val sourceBot: Bot
        get() = bot.sourceBot

    override fun isMe(id: ID): Boolean =
        bot.isMe(id)

    override val component: KookComponent
        get() = bot.component
    override val avatar: String
        get() = bot.avatar
    override val username: String
        get() = bot.username
    override val manager: KookBotManager
        get() = bot.manager

    override val guilds: Items<KookGuild>
        get() = bot.guilds

    override suspend fun guild(id: ID): KookGuild? = bot.guild(id)

    override suspend fun guildCount(): Int = bot.guildCount()
}