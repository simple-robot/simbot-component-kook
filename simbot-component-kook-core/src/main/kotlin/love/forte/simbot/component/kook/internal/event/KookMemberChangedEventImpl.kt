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

package love.forte.simbot.component.kook.internal.event

import love.forte.simbot.component.kook.event.*
import love.forte.simbot.component.kook.internal.KookChannelImpl
import love.forte.simbot.component.kook.internal.KookComponentBotImpl
import love.forte.simbot.component.kook.internal.KookGuildImpl
import love.forte.simbot.component.kook.internal.KookGuildMemberImpl
import love.forte.simbot.definition.UserInfo
import love.forte.simbot.kook.event.Event
import love.forte.simbot.kook.event.system.guild.member.ExitedGuildEventBody
import love.forte.simbot.kook.event.system.guild.member.GuildMemberOfflineEventBody
import love.forte.simbot.kook.event.system.guild.member.GuildMemberOnlineEventBody
import love.forte.simbot.kook.event.system.guild.member.JoinedGuildEventBody
import love.forte.simbot.kook.event.system.user.SelfExitedGuildEventBody
import love.forte.simbot.kook.event.system.user.SelfJoinedGuildEventBody
import love.forte.simbot.kook.event.system.user.UserExitedChannelEventBody
import love.forte.simbot.kook.event.system.user.UserJoinedChannelEventBody

internal data class KookMemberExitedChannelEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<UserExitedChannelEventBody>>,
    private val _source: KookChannelImpl,
    private val _before: KookGuildMemberImpl,
) : KookMemberExitedChannelEvent() {
    override suspend fun source() = _source
    override suspend fun member() = _before
    override suspend fun before() = _before
}


internal data class KookMemberJoinedChannelEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<UserJoinedChannelEventBody>>,
    private val _source: KookChannelImpl,
    private val _after: KookGuildMemberImpl,
) : KookMemberJoinedChannelEvent() {
    override suspend fun source() = _source
    override suspend fun member() = _after
    override suspend fun after() = _after
}


internal data class KookMemberExitedGuildEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<ExitedGuildEventBody>>,
    private val _source: KookGuildImpl,
    private val _before: KookGuildMemberImpl,
) : KookMemberExitedGuildEvent() {
    override suspend fun guild() = _source
    override suspend fun member() = _before
}


internal data class KookMemberJoinedGuildEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<JoinedGuildEventBody>>,
    private val _source: KookGuildImpl,
    private val _after: KookGuildMemberImpl,
) : KookMemberJoinedGuildEvent() {
    override suspend fun guild() = _source
    override suspend fun member() = _after
}


internal data class KookBotSelfExitedGuildEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<SelfExitedGuildEventBody>>,
    private val _source: KookGuildImpl,
    private val _before: KookGuildMemberImpl,
) : KookBotSelfExitedGuildEvent() {
    override suspend fun member() = _before
    override suspend fun source() = _source
    override suspend fun before() = _before
}


internal data class KookBotSelfJoinedGuildEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<SelfJoinedGuildEventBody>>,
    private val _source: KookGuildImpl,
    private val _after: KookGuildMemberImpl,
) : KookBotSelfJoinedGuildEvent() {
    override suspend fun member() = _after
    override suspend fun source() = _source
    override suspend fun after() = _after
}


internal data class KookMemberOnlineEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<GuildMemberOnlineEventBody>>,
    private val _source: UserInfo,
) : KookUserOnlineStatusChangedEvent.Online() {
    override val guilds: Sequence<KookGuildImpl?>
        get() = guildIds.asSequence().map { bot.internalGuild(it) }
    
    override suspend fun source() = _source
}

internal data class KookMemberOfflineEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<GuildMemberOfflineEventBody>>,
    private val _source: UserInfo,
) : KookUserOnlineStatusChangedEvent.Offline() {
    override val guilds: Sequence<KookGuildImpl?>
        get() = guildIds.asSequence().map { bot.internalGuild(it) }
    
    override suspend fun source() = _source
}
