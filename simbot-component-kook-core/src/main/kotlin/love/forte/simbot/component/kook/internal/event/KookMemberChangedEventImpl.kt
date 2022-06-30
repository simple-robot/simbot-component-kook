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

@file:Suppress("UnnecessaryOptInAnnotation")

package love.forte.simbot.component.kook.internal.event

import love.forte.simbot.Api4J
import love.forte.simbot.component.kook.KookGuildMember
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

@OptIn(Api4J::class)
internal data class KookMemberExitedChannelEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<UserExitedChannelEventBody>>,
    override val source: KookChannelImpl,
    override val before: KookGuildMemberImpl,
) : KookMemberExitedChannelEvent() {
    override val member: KookGuildMember
        get() = before
}


@OptIn(Api4J::class)
internal data class KookMemberJoinedChannelEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<UserJoinedChannelEventBody>>,
    override val source: KookChannelImpl,
    override val after: KookGuildMemberImpl,
) : KookMemberJoinedChannelEvent() {
    override val member: KookGuildMember
        get() = after
}


@OptIn(Api4J::class)
internal data class KookMemberExitedGuildEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<ExitedGuildEventBody>>,
    override val source: KookGuildImpl,
    override val before: KookGuildMemberImpl,
) : KookMemberExitedGuildEvent() {
    override val member: KookGuildMember
        get() = before
}


@OptIn(Api4J::class)
internal data class KookMemberJoinedGuildEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<JoinedGuildEventBody>>,
    override val source: KookGuildImpl,
    override val after: KookGuildMemberImpl,
) : KookMemberJoinedGuildEvent() {
    override val member: KookGuildMember
        get() = after
}


@OptIn(Api4J::class)
internal data class KookBotSelfExitedGuildEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<SelfExitedGuildEventBody>>,
    override val source: KookGuildImpl,
    override val before: KookGuildMemberImpl,
) : KookBotSelfExitedGuildEvent() {
    override val member: KookGuildMember
        get() = before
}


@OptIn(Api4J::class)
internal data class KookBotSelfJoinedGuildEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<SelfJoinedGuildEventBody>>,
    override val source: KookGuildImpl,
    override val after: KookGuildMemberImpl,
) : KookBotSelfJoinedGuildEvent() {
    override val member: KookGuildMember
        get() = after
}


internal data class KookMemberOnlineEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<GuildMemberOnlineEventBody>>,
    override val source: UserInfo,
) : KookUserOnlineStatusChangedEvent.Online() {
    override val guilds: Sequence<KookGuildImpl?>
        get() = guildIds.asSequence().map { bot.internalGuild(it) }
}

internal data class KookMemberOfflineEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<GuildMemberOfflineEventBody>>,
    override val source: UserInfo,
) : KookUserOnlineStatusChangedEvent.Offline() {
    override val guilds: Sequence<KookGuildImpl?>
        get() = guildIds.asSequence().map { bot.internalGuild(it) }
}