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

import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import love.forte.simbot.annotations.ExperimentalSimbotAPI
import love.forte.simbot.annotations.ExperimentalSimbotAPI
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.literal
import love.forte.simbot.component.kook.*
import love.forte.simbot.component.kook.bot.internal.KookBotImpl
import love.forte.simbot.component.kook.bot.internal.KookGuildBotImpl
import love.forte.simbot.component.kook.role.KookGuildRole
import love.forte.simbot.component.kook.role.KookGuildRoleCreator
import love.forte.simbot.component.kook.role.internal.KookGuildRoleImpl
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.delegate.getValue
import love.forte.simbot.delegate.stringID
import love.forte.simbot.kook.api.KookApi
import love.forte.simbot.kook.api.role.CreateGuildRoleApi
import love.forte.simbot.kook.api.role.GetGuildRoleListApi
import love.forte.simbot.kook.objects.Guild
import love.forte.simbot.utils.item.Items
import love.forte.simbot.utils.item.effectedItemsBySequence
import love.forte.simbot.utils.item.itemsByFlow


/**
 *
 * @author ForteScarlet
 */
internal class KookGuildImpl(
    private val baseBot: KookBotImpl,
    override val source: Guild,
) : KookGuild {
    internal var internalBot: KookGuildBotImpl? = null

    override val bot: KookGuildBotImpl
        get() {
            return internalBot ?: (baseBot.internalMember(source.id, baseBot.sourceBot.botUserInfo.id)
                ?: throw kookGuildNotExistsException(source.id)).let {
                KookGuildBotImpl(baseBot, it).also { b -> internalBot = b }
            }
        }

    override val currentMember: Int
        get() = baseBot.internalGuildMemberCount(source.id)

    override val channels: Items<KookChatChannel>
        get() = effectedItemsBySequence { baseBot.internalChannels(source.id) }

    override suspend fun channel(id: ID): KookChatChannel? =
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

    @ExperimentalSimbotAPI
    override val roles: Items<KookGuildRoleImpl>
        get() = itemsByFlow { prop ->
            val pageSize = prop.batch.takeIf { it > 0 } ?: KookApi.DEFAULT_MAX_PAGE_SIZE
            val limit = prop.limit.takeIf { it > 0 }
            val offset = prop.offset.takeIf { it > 0 }
            val startPage = offset?.div(pageSize) ?: KookApi.DEFAULT_START_PAGE
            val drop = offset?.mod(pageSize) ?: 0

            flow {
                var page = startPage
                do {
                    val result = GetGuildRoleListApi
                        .create(source.id, page = page, pageSize = pageSize)
                        .requestDataBy(bot)
                    val items = result.items
                    items.forEach {
                        emit(it)
                    }
                    page = result.meta.page + 1

                } while (items.isNotEmpty() && result.meta.page < result.meta.pageTotal)
            }.drop(drop).let { flow ->
                if (limit != null) {
                    flow.take(limit)
                } else {
                    flow
                }
            }.map { r ->
                KookGuildRoleImpl(baseBot, this, r)
            }
        }

    @ExperimentalSimbotAPI
    override fun roleCreator(): KookGuildRoleCreator = KookGuildRoleCreatorImpl(baseBot, this)

    @ExperimentalSimbotAPI
    override val categories: Items<KookCategoryChannel>
        get() = effectedItemsBySequence { baseBot.internalCategories(source.id) }

    @ExperimentalSimbotAPI
    override fun getCategory(id: ID): KookCategoryChannel? =
        baseBot.internalCategory(id.literal)

    override fun toString(): String {
        return "KookGuild(id=${source.id}, name=${source.name})"
    }
}


@OptIn(ExperimentalSimbotAPI::class)
private class KookGuildRoleCreatorImpl(
    private val baseBot: KookBotImpl,
    private val guild: KookGuildImpl,
) : KookGuildRoleCreator {
    override var name: String? = null

    override suspend fun create(): KookGuildRole {
        val role = CreateGuildRoleApi.create(guild.source.id, name).requestDataBy(guild.bot)
        return KookGuildRoleImpl(baseBot, guild, role)
    }
}
