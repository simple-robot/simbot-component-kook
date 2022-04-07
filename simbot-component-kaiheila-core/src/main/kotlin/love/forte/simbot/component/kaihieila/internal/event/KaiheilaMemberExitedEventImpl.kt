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

import love.forte.simbot.Timestamp
import love.forte.simbot.action.ActionType
import love.forte.simbot.component.kaihieila.KaiheilaGuild
import love.forte.simbot.component.kaihieila.KaiheilaGuildMember
import love.forte.simbot.component.kaihieila.event.KaiheilaMemberExitedEvent
import love.forte.simbot.component.kaihieila.internal.KaiheilaComponentBotImpl
import love.forte.simbot.kaiheila.event.Event
import love.forte.simbot.kaiheila.event.system.user.UserExitedChannelEventBody

/**
 *
 * @author ForteScarlet
 */
internal class KaiheilaMemberExitedEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: Event<Event.Extra.Sys<UserExitedChannelEventBody>>,
) : KaiheilaMemberExitedEvent() {

    override val timestamp: Timestamp = sourceEvent.extra.body.exitedAt

    /**
     * 开黑啦事件无法区分用户的离开是否为被踢。
     * 因此统一视为 [主动][ActionType.PROACTIVE] 离开。
     */
    override val actionType: ActionType get() = ActionType.PROACTIVE


    override val source: KaiheilaGuild = TODO()

    override val operator: KaiheilaGuildMember?
        get() = TODO("Not yet implemented")
    override val target: KaiheilaGuildMember
        get() = TODO("Not yet implemented")
}