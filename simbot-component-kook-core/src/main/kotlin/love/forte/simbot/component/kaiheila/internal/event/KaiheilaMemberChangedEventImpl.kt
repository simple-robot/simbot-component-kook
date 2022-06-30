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

package love.forte.simbot.component.kaiheila.internal.event

import love.forte.simbot.Api4J
import love.forte.simbot.component.kaiheila.KaiheilaGuildMember
import love.forte.simbot.component.kaiheila.event.*
import love.forte.simbot.component.kaiheila.internal.KaiheilaChannelImpl
import love.forte.simbot.component.kaiheila.internal.KaiheilaComponentBotImpl
import love.forte.simbot.component.kaiheila.internal.KaiheilaGuildImpl
import love.forte.simbot.component.kaiheila.internal.KaiheilaGuildMemberImpl
import love.forte.simbot.definition.UserInfo
import love.forte.simbot.kaiheila.event.Event
import love.forte.simbot.kaiheila.event.system.guild.member.ExitedGuildEventBody
import love.forte.simbot.kaiheila.event.system.guild.member.GuildMemberOfflineEventBody
import love.forte.simbot.kaiheila.event.system.guild.member.GuildMemberOnlineEventBody
import love.forte.simbot.kaiheila.event.system.guild.member.JoinedGuildEventBody
import love.forte.simbot.kaiheila.event.system.user.SelfExitedGuildEventBody
import love.forte.simbot.kaiheila.event.system.user.SelfJoinedGuildEventBody
import love.forte.simbot.kaiheila.event.system.user.UserExitedChannelEventBody
import love.forte.simbot.kaiheila.event.system.user.UserJoinedChannelEventBody

@OptIn(Api4J::class)
internal data class KaiheilaMemberExitedChannelEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<UserExitedChannelEventBody>>,
    override val source: KaiheilaChannelImpl,
    override val before: KaiheilaGuildMemberImpl,
) : KaiheilaMemberExitedChannelEvent() {
    override val member: KaiheilaGuildMember
        get() = before
}


@OptIn(Api4J::class)
internal data class KaiheilaMemberJoinedChannelEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<UserJoinedChannelEventBody>>,
    override val source: KaiheilaChannelImpl,
    override val after: KaiheilaGuildMemberImpl,
) : KaiheilaMemberJoinedChannelEvent() {
    override val member: KaiheilaGuildMember
        get() = after
}


@OptIn(Api4J::class)
internal data class KaiheilaMemberExitedGuildEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<ExitedGuildEventBody>>,
    override val source: KaiheilaGuildImpl,
    override val before: KaiheilaGuildMemberImpl,
) : KaiheilaMemberExitedGuildEvent() {
    override val member: KaiheilaGuildMember
        get() = before
}


@OptIn(Api4J::class)
internal data class KaiheilaMemberJoinedGuildEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<JoinedGuildEventBody>>,
    override val source: KaiheilaGuildImpl,
    override val after: KaiheilaGuildMemberImpl,
) : KaiheilaMemberJoinedGuildEvent() {
    override val member: KaiheilaGuildMember
        get() = after
}


@OptIn(Api4J::class)
internal data class KaiheilaBotSelfExitedGuildEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<SelfExitedGuildEventBody>>,
    override val source: KaiheilaGuildImpl,
    override val before: KaiheilaGuildMemberImpl,
) : KaiheilaBotSelfExitedGuildEvent() {
    override val member: KaiheilaGuildMember
        get() = before
}


@OptIn(Api4J::class)
internal data class KaiheilaBotSelfJoinedGuildEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<SelfJoinedGuildEventBody>>,
    override val source: KaiheilaGuildImpl,
    override val after: KaiheilaGuildMemberImpl,
) : KaiheilaBotSelfJoinedGuildEvent() {
    override val member: KaiheilaGuildMember
        get() = after
}


internal data class KaiheilaMemberOnlineEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<GuildMemberOnlineEventBody>>,
    override val source: UserInfo,
) : KaiheilaUserOnlineStatusChangedEvent.Online() {
    override val guilds: Sequence<KaiheilaGuildImpl?>
        get() = guildIds.asSequence().map { bot.internalGuild(it) }
}

internal data class KaiheilaMemberOfflineEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<GuildMemberOfflineEventBody>>,
    override val source: UserInfo,
) : KaiheilaUserOnlineStatusChangedEvent.Offline() {
    override val guilds: Sequence<KaiheilaGuildImpl?>
        get() = guildIds.asSequence().map { bot.internalGuild(it) }
}