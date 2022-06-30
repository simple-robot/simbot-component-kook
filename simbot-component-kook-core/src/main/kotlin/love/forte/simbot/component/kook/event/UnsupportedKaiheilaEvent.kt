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

package love.forte.simbot.component.kook.event

import love.forte.simbot.DiscreetSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.component.kook.KaiheilaComponentBot
import love.forte.simbot.event.BaseEventKey
import love.forte.simbot.kook.event.Event
import love.forte.simbot.message.doSafeCast


/**
 * 所有未提供针对性实现的其他 Kook 事件。
 *
 * [UnsupportedKaiheilaEvent] 不实现任何其他事件类型，
 * 仅实现 Kook 组件中的事件父类型 [KaiheilaEvent]，是一个完全独立的事件类型。
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
    override val sourceEvent: Event<Event.Extra>,
) : KaiheilaEvent<Event.Extra, Event<Event.Extra>>() {
    /**
     * 事件ID。
     */
    override val id: ID get() = sourceEvent.msgId

    /**
     * 事件时间。
     */
    override val timestamp: Timestamp
        get() = sourceEvent.msgTimestamp



    override fun toString(): String {
        return "UnsupportedKaiheilaEvent(sourceEvent=$sourceEvent)"
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is UnsupportedKaiheilaEvent) return false

        return sourceEvent == other.sourceEvent
    }

    override fun hashCode(): Int {
        return sourceEvent.hashCode()
    }


    override val key: love.forte.simbot.event.Event.Key<UnsupportedKaiheilaEvent>
        get() = Key

    public companion object Key : BaseEventKey<UnsupportedKaiheilaEvent>("kook.unsupported", KaiheilaEvent) {
        override fun safeCast(value: Any): UnsupportedKaiheilaEvent? = doSafeCast(value)
    }

}