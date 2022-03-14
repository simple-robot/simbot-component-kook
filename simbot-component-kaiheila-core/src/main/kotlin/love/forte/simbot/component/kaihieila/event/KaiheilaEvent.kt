package love.forte.simbot.component.kaihieila.event

import love.forte.simbot.component.kaihieila.*
import love.forte.simbot.definition.*
import love.forte.simbot.event.*
import love.forte.simbot.message.*
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