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

package love.forte.simbot.component.kook.event.internal

import love.forte.simbot.component.kook.bot.internal.KookBotImpl
import love.forte.simbot.component.kook.event.*
import love.forte.simbot.component.kook.internal.KookChannelImpl
import love.forte.simbot.component.kook.internal.KookGuildImpl
import love.forte.simbot.component.kook.internal.KookMemberImpl
import love.forte.simbot.definition.UserInfo
import love.forte.simbot.kook.event.*
import love.forte.simbot.utils.item.Items
import love.forte.simbot.utils.item.effectedItemsBySequence

internal data class KookMemberExitedChannelEventImpl(
    override val bot: KookBotImpl,
    override val sourceEvent: Event<JoinedChannelEventExtra>,
    private val _source: KookChannelImpl,
    private val _before: KookMemberImpl,
) : KookMemberExitedChannelEvent() {
    override suspend fun source() = _source
    override suspend fun member() = _before
    override suspend fun before() = _before
}


internal data class KookMemberJoinedChannelEventImpl(
    override val bot: KookBotImpl,
    override val sourceEvent: Event<ExitedChannelEventExtra>,
    private val _source: KookChannelImpl,
    private val _after: KookMemberImpl,
) : KookMemberJoinedChannelEvent() {
    override suspend fun source() = _source
    override suspend fun member() = _after
    override suspend fun after() = _after
}


internal data class KookMemberExitedGuildEventImpl(
    override val bot: KookBotImpl,
    override val sourceEvent: Event<ExitedGuildEventExtra>,
    private val _source: KookGuildImpl,
    private val _before: KookMemberImpl,
) : KookMemberExitedGuildEvent() {
    override suspend fun guild() = _source
    override suspend fun member() = _before
}


internal data class KookMemberJoinedGuildEventImpl(
    override val bot: KookBotImpl,
    override val sourceEvent: Event<JoinedGuildEventExtra>,
    private val _source: KookGuildImpl,
    private val _after: KookMemberImpl,
) : KookMemberJoinedGuildEvent() {
    override suspend fun guild() = _source
    override suspend fun member() = _after
}


internal data class KookBotSelfExitedGuildEventImpl(
    override val bot: KookBotImpl,
    override val sourceEvent: Event<SelfExitedGuildEventExtra>,
    private val _source: KookGuildImpl,
    private val _before: KookMemberImpl,
) : KookBotSelfExitedGuildEvent() {
    override suspend fun member() = _before
    override suspend fun source() = _source
    override suspend fun before() = _before
}


internal data class KookBotSelfJoinedGuildEventImpl(
    override val bot: KookBotImpl,
    override val sourceEvent: Event<SelfJoinedGuildEventExtra>,
    private val _source: KookGuildImpl,
    private val _after: KookMemberImpl,
) : KookBotSelfJoinedGuildEvent() {
    override suspend fun member() = _after
    override suspend fun source() = _source
    override suspend fun after() = _after
}


internal data class KookMemberOnlineEventImpl(
    override val bot: KookBotImpl,
    override val sourceEvent: Event<GuildMemberOnlineEventExtra>,
    private val _source: UserInfo,
) : KookUserOnlineEvent() {
    override val guilds: Items<KookGuildImpl>
        get() = effectedItemsBySequence { sourceBody.guilds.asSequence().map { bot.internalGuild(it) }.filterNotNull() }

    override suspend fun source() = _source
}

internal data class KookMemberOfflineEventImpl(
    override val bot: KookBotImpl,
    override val sourceEvent: Event<GuildMemberOfflineEventExtra>,
    private val _source: UserInfo,
) : KookUserOfflineEvent() {
    override val guilds: Items<KookGuildImpl>
        get() = effectedItemsBySequence { sourceBody.guilds.asSequence().map { bot.internalGuild(it) }.filterNotNull() }

    override suspend fun source() = _source
}
