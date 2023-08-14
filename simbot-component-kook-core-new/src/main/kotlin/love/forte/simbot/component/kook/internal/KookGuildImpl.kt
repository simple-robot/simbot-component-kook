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

import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.component.kook.*
import love.forte.simbot.component.kook.bot.internal.KookBotImpl
import love.forte.simbot.component.kook.bot.internal.KookGuildBotImpl
import love.forte.simbot.definition.Role
import love.forte.simbot.delegate.getValue
import love.forte.simbot.delegate.stringID
import love.forte.simbot.kook.objects.Guild
import love.forte.simbot.literal
import love.forte.simbot.utils.item.Items
import love.forte.simbot.utils.item.Items.Companion.emptyItems
import love.forte.simbot.utils.item.effectedItemsBySequence


/**
 *
 * @author ForteScarlet
 */
internal class KookGuildImpl(
    private val baseBot: KookBotImpl,
    override val source: Guild,
) : KookGuild {

    internal var _bot: KookGuildBotImpl? = null

    override val bot: KookGuildBotImpl
        get() {
            return _bot ?: (baseBot.internalMember(source.id, baseBot.sourceBot.botUserInfo.id)
                ?: throw kookGuildNotExistsException(source.id)).let {
                KookGuildBotImpl(baseBot, it).also { b -> _bot = b }
            }
        }

    override val currentMember: Int
        get() = baseBot.internalGuildMemberCount(source.id)

    override val channels: Items<KookChannel>
        get() = effectedItemsBySequence { baseBot.internalChannels(source.id) }

    override suspend fun channel(id: ID): KookChannel? =
        baseBot.internalChannel(id.literal)

    override val currentChannel: Int
        get() = baseBot.internalGuildChannelCount(source.id)

    override val members: Items<KookMember>
        get() = effectedItemsBySequence { baseBot.internalMembers(source.id) }

    override suspend fun owner(): KookMember {
        return baseBot.internalMember(source.id, id.literal)
            ?: throw kookMemberNotExistsException(source.id)
    }

    override val ownerId: ID by stringID { source.userId }

    override suspend fun member(id: ID): KookMember? =
        baseBot.internalMember(source.id, id.literal)

    override val roles: Items<Role>
        get() = emptyItems() // TODO("Not yet implemented")


    @ExperimentalSimbotApi
    override val categories: Items<KookChannelCategory>
        get() = effectedItemsBySequence { baseBot.internalCategories(source.id) }

    @ExperimentalSimbotApi
    override fun getCategory(id: ID): KookChannelCategory? =
        baseBot.internalCategory(id.literal)

    override fun toString(): String {
        return "KookGuild(id=${source.id}, name=${source.name})"
    }
}
