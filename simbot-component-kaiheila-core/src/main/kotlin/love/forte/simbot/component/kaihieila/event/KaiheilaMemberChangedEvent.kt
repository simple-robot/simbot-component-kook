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

package love.forte.simbot.component.kaihieila.event

import love.forte.simbot.Api4J
import love.forte.simbot.component.kaihieila.KaiheilaGuild
import love.forte.simbot.component.kaihieila.KaiheilaGuildMember
import love.forte.simbot.event.BaseEventKey
import love.forte.simbot.event.MemberChangedEvent
import love.forte.simbot.kaiheila.event.Event
import love.forte.simbot.message.doSafeCast


/**
 * 开黑啦的频道成员变更事件。
 *
 */
public abstract class KaiheilaMemberChangedEvent<
        out EX : Event.Extra, out E : Event<EX>,
        Before : KaiheilaGuildMember?, After : KaiheilaGuildMember
        > :
    KaiheilaEvent<EX, E>(), MemberChangedEvent<Before, After> {

    abstract override val source: KaiheilaGuild
    override suspend fun source(): KaiheilaGuild = source

    @OptIn(Api4J::class)
    abstract override val operator: KaiheilaGuildMember?
    override suspend fun operator(): KaiheilaGuildMember? = operator

    public companion object Key : BaseEventKey<KaiheilaMemberChangedEvent<*, *, *, *>>(
        "kaiheila.member_changed", KaiheilaEvent, MemberChangedEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMemberChangedEvent<*, *, *, *>? = doSafeCast(value)
    }

}