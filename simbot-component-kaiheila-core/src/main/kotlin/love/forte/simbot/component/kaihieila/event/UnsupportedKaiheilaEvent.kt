package love.forte.simbot.component.kaihieila.event

import love.forte.simbot.*
import love.forte.simbot.component.kaihieila.*
import love.forte.simbot.event.*
import love.forte.simbot.kaiheila.event.Event
import love.forte.simbot.message.*


/**
 * 所有未提供针对性实现的其他开黑啦事件。
 * [UnsupportedKaiheilaEvent] 不实现任何其他事件类型，
 * 仅实现开黑啦组件中的事件父类型 [KaiheilaEvent]，是一个完全独立的事件类型。
 *
 * [UnsupportedKaiheilaEvent] 会将所有未支持或难以支持的事件都会通过此类型进行推送，
 * 但是如果要监听 [UnsupportedKaiheilaEvent], 你需要谨慎处理其中的一切，
 * 因为 [UnsupportedKaiheilaEvent] 能够提供的事件会随着当前组件实现的特定事件的增多而减少，
 * 这种减少可能会伴随着版本更新而产生，且可能不会有任何说明或错误提示。
 *
 * 因此你应当首先查看 [KaiheilaEvent] 下是否有所需的已经实现的事件类型，并且不应当过分依赖 [UnsupportedKaiheilaEvent].
 *
 *
 *
 * @author ForteScarlet
 */
@DiscreetSimbotApi
public class UnsupportedKaiheilaEvent(
    override val bot: KaiheilaComponentBot,
    override val sourceEvent: Event<Event.Extra>
) : KaiheilaEvent<Event.Extra, Event<Event.Extra>>() {
    override fun toString(): String {
        return "UnsupportedKaiheilaEvent(sourceEvent=$sourceEvent)"
    }

    override val id: ID get() = sourceEvent.msgId
    override val timestamp: Timestamp
        get() = sourceEvent.msgTimestamp

    override val visibleScope: love.forte.simbot.event.Event.VisibleScope
        get() = love.forte.simbot.event.Event.VisibleScope.PRIVATE


    override val key: love.forte.simbot.event.Event.Key<UnsupportedKaiheilaEvent>
        get() = Key

    public companion object Key : BaseEventKey<UnsupportedKaiheilaEvent>("kaiheila.unsupported", KaiheilaEvent) {
        override fun safeCast(value: Any): UnsupportedKaiheilaEvent? = doSafeCast(value)
    }

}