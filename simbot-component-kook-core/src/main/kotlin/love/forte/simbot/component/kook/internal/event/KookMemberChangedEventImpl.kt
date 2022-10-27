/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kook 的一部分。
 *
 *  simbot-component-kook 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kook 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
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
    override suspend fun source() = _source
    override suspend fun member() = _before
    override suspend fun before() = _before
}


internal data class KookMemberJoinedGuildEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<JoinedGuildEventBody>>,
    private val _source: KookGuildImpl,
    private val _after: KookGuildMemberImpl,
) : KookMemberJoinedGuildEvent() {
    override suspend fun source() = _source
    override suspend fun member() = _after
    override suspend fun after() = _after
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