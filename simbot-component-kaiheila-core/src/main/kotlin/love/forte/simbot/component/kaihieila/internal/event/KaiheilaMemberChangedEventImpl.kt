/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kaiheila 的一部分。
 *
 *  simbot-component-kaiheila 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kaiheila 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

package love.forte.simbot.component.kaihieila.internal.event

import love.forte.simbot.component.kaihieila.event.*
import love.forte.simbot.component.kaihieila.internal.KaiheilaChannelImpl
import love.forte.simbot.component.kaihieila.internal.KaiheilaComponentBotImpl
import love.forte.simbot.component.kaihieila.internal.KaiheilaGuildImpl
import love.forte.simbot.component.kaihieila.internal.KaiheilaGuildMemberImpl
import love.forte.simbot.kaiheila.event.Event
import love.forte.simbot.kaiheila.event.system.guild.member.ExitedGuildEventBody
import love.forte.simbot.kaiheila.event.system.guild.member.JoinedGuildEventBody
import love.forte.simbot.kaiheila.event.system.user.SelfExitedGuildEventBody
import love.forte.simbot.kaiheila.event.system.user.SelfJoinedGuildEventBody
import love.forte.simbot.kaiheila.event.system.user.UserExitedChannelEventBody
import love.forte.simbot.kaiheila.event.system.user.UserJoinedChannelEventBody

internal data class KaiheilaMemberExitedChannelEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<UserExitedChannelEventBody>>,
    override val source: KaiheilaChannelImpl,
    override val target: KaiheilaGuildMemberImpl
) : KaiheilaMemberExitedChannelEvent()


internal data class KaiheilaMemberJoinedChannelEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<UserJoinedChannelEventBody>>,
    override val source: KaiheilaChannelImpl,
    override val target: KaiheilaGuildMemberImpl
) : KaiheilaMemberJoinedChannelEvent()


internal data class KaiheilaMemberExitedGuildEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<ExitedGuildEventBody>>,
    override val source: KaiheilaGuildImpl,
    override val target: KaiheilaGuildMemberImpl
) : KaiheilaMemberExitedGuildEvent()


internal data class KaiheilaMemberJoinedGuildEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<JoinedGuildEventBody>>,
    override val source: KaiheilaGuildImpl,
    override val target: KaiheilaGuildMemberImpl
) : KaiheilaMemberJoinedGuildEvent()


internal data class KaiheilaBotSelfExitedGuildEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<SelfExitedGuildEventBody>>,
    override val source: KaiheilaGuildImpl,
    override val target: KaiheilaGuildMemberImpl
) : KaiheilaBotSelfExitedGuildEvent()


internal data class KaiheilaBotSelfJoinedGuildEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<SelfJoinedGuildEventBody>>,
    override val source: KaiheilaGuildImpl,
    override val target: KaiheilaGuildMemberImpl
) : KaiheilaBotSelfJoinedGuildEvent()

