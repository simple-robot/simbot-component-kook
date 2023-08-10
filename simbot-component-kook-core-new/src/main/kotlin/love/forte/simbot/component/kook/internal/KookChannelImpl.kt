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
import love.forte.simbot.component.kook.KookChannel
import love.forte.simbot.component.kook.KookChannelCategory
import love.forte.simbot.component.kook.bot.KookGuildBot
import love.forte.simbot.component.kook.bot.internal.KookBotImpl
import love.forte.simbot.component.kook.kookGuildNotExistsException
import love.forte.simbot.definition.Guild
import love.forte.simbot.definition.GuildMember
import love.forte.simbot.definition.Organization
import love.forte.simbot.definition.Role
import love.forte.simbot.delegate.getValue
import love.forte.simbot.delegate.stringID
import love.forte.simbot.kook.api.channel.ChannelInfo
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageReceipt
import love.forte.simbot.utils.item.Items
import kotlin.time.Duration


/**
 *
 * @author ForteScarlet
 */
internal class KookChannelImpl(
    private val baseBot: KookBotImpl,
    override val source: ChannelInfo,
    internal val _guildId: String
) : KookChannel {
    override val bot: KookGuildBot
        get() = baseBot.internalGuild(_guildId)?.bot
            ?: throw kookGuildNotExistsException(_guildId)

    override val guildId: ID by stringID { _guildId }

    override suspend fun send(message: Message): MessageReceipt {
        TODO("Not yet implemented")
    }

    override val currentMember: Int
        get() = TODO("Not yet implemented")

    override suspend fun guild(): Guild {
        TODO("Not yet implemented")
    }

    override val maximumMember: Int
        get() = TODO("Not yet implemented")

    override val members: Items<GuildMember>
        get() = TODO("Not yet implemented")

    override suspend fun owner(): GuildMember {
        TODO("Not yet implemented")
    }

    override suspend fun member(id: ID): GuildMember? {
        TODO("Not yet implemented")
    }

    override suspend fun previous(): Organization? {
        TODO("Not yet implemented")
    }

    override val roles: Items<Role>
        get() = TODO("Not yet implemented")

    override suspend fun mute(duration: Duration): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun unmute(): Boolean {
        TODO("Not yet implemented")
    }

    override val category: KookChannelCategory?
        get() = TODO("Not yet implemented")

    override fun toString(): String {
        return "KookChannel(id=${source.id}, name=${source.name}, guildId=${_guildId})"
    }
}
