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

package love.forte.simbot.component.kaiheila.event

import love.forte.simbot.ID
import love.forte.simbot.component.kaiheila.KaiheilaComponentBot
import love.forte.simbot.definition.BotContainer
import love.forte.simbot.event.*
import love.forte.simbot.kaiheila.event.Event.Extra.Sys
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.kaiheila.event.Event as KhlEvent


/**
 *
 * 开黑啦组件在simbot中的所有事件总类。
 *
 *
 * @see UnsupportedKaiheilaEvent
 *
 * @param E 开黑啦api模块中所定义的原始开黑啦事件对象 [KhlEvent].
 * @param EX 开黑啦api模块中所定义的原始开黑啦事件对象的 [extra][KhlEvent.Extra] 属性类型。
 *
 * @author ForteScarlet
 */
@BaseEvent
public abstract class KaiheilaEvent<out EX : KhlEvent.Extra, out E : KhlEvent<EX>> : BotContainer, Event {
    /**
     * 此事件对应的bot示例。
     */
    abstract override val bot: KaiheilaComponentBot

    /**
     * 当前事件内部对应的原始事件实体。
     */
    public abstract val sourceEvent: E


    override fun toString(): String {
        return "KaiheilaEvent(sourceEvent=$sourceEvent)"
    }

    abstract override val key: Event.Key<out KaiheilaEvent<*, *>>


    public companion object Key : BaseEventKey<KaiheilaEvent<*, *>>(
        "kaiheila.event", Event
    ) {
        override fun safeCast(value: Any): KaiheilaEvent<*, *>? = doSafeCast(value)
    }

}


/**
 * 开黑啦组件在simbot中的**系统事件**相关的事件总类。
 *
 * @param Body 事件消息的 [extra][KhlEvent.Extra] 作为 [Sys] 时，其 [Sys.body] 的类型。
 */
@BaseEvent
public abstract class KaiheilaSystemEvent<out Body> :
    KaiheilaEvent<Sys<Body>, KhlEvent<Sys<Body>>>() {

    override val id: ID get() = sourceEvent.msgId

    /**
     * [sourceEvent] 中的 `extra.body` 信息。
     */
    public open val sourceBody: Body get() = sourceEvent.extra.body


    abstract override val key: Event.Key<out KaiheilaSystemEvent<*>>

    public companion object Key : BaseEventKey<KaiheilaSystemEvent<*>>(
        "kaiheila.system_event", KaiheilaEvent
    ) {
        override fun safeCast(value: Any): KaiheilaSystemEvent<*>? = doSafeCast(value)
    }
}