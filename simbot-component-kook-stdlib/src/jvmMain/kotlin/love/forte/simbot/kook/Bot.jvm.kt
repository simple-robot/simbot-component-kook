/*
 * Copyright (c) 2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook,
 * If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.await
import love.forte.simbot.Api4J
import love.forte.simbot.kook.api.KookApiRequestor
import love.forte.simbot.kook.event.Event
import love.forte.simbot.kook.event.EventExtra
import java.util.concurrent.CompletableFuture

/**
 * 对一个任意或指定 extra 类型的事件进行处理的函数接口
 */
@Api4J
public fun interface BlockingEventProcessor<EX : EventExtra> {
    /**
     * 接收事件并对其进行处理。
     *
     * @param event 事件本体
     * @param raw 事件订阅中接收到的原始的事件JSON字符串
     */
    public fun accept(event: Event<EX>, raw: String)
}

/**
 * 对一个任意或指定 extra 类型的事件进行处理的函数接口
 */
@Api4J
public fun interface AsyncEventProcessor<EX : EventExtra> {
    /**
     * 接收事件并对其进行处理。
     *
     * @param event 事件本体
     * @param raw 事件订阅中接收到的原始的事件JSON字符串
     */
    public fun accept(event: Event<EX>, raw: String): CompletableFuture<Void?>
}


/**
 * 一个 JVM 平台 KOOK Bot。
 *
 * 针对某个平台的 KOOK BOT 类型，应当由 [Bot] 实现并由
 * Kotlin 多平台决定具体细节。
 *
 * @author ForteScarlet
 */
public actual interface PlatformBot : CoroutineScope, KookApiRequestor {

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。
     *
     * @param processorType 事件处理器类型。默认为 [ProcessorType.NORMAL]。
     * @param processor 事件处理器
     */
    @JvmSynthetic
    public actual fun processor(processorType: ProcessorType, processor: suspend Event<*>.(raw: String) -> Unit)

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。
     *
     * @param processorType 事件处理器类型。默认为 [ProcessorType.NORMAL]。
     * @param processor 事件处理器
     */
    @Api4J
    public fun blockingProcessor(processorType: ProcessorType, processor: BlockingEventProcessor<EventExtra>) {
        processor(processorType) { raw -> processor.accept(this, raw) }
    }

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。
     *
     * @param processorType 事件处理器类型。默认为 [ProcessorType.NORMAL]。
     * @param processor 事件处理器
     */
    @Api4J
    public fun asyncProcessor(
        processorType: ProcessorType,
        processor: AsyncEventProcessor<EventExtra>
    ) {
        processor { raw -> processor.accept(this, raw).await() }
    }

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。
     *
     * @param processor 事件处理器
     */
    @Api4J
    public fun blockingProcessor(processor: BlockingEventProcessor<EventExtra>) {
        blockingProcessor(ProcessorType.NORMAL, processor)
    }

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。
     *
     * @param processor 事件处理器
     */
    @Api4J
    public fun asyncProcessor(processor: AsyncEventProcessor<EventExtra>) {
        asyncProcessor(ProcessorType.NORMAL, processor)
    }

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。
     *
     * @param processorType 事件处理器类型。默认为 [ProcessorType.NORMAL]。
     * @param processor 事件处理器
     * @param extraType [Event.extra] 的类型。
     */
    @Api4J
    public fun <EX : EventExtra> blockingProcessor(
        extraType: Class<EX>,
        processorType: ProcessorType,
        processor: BlockingEventProcessor<EX>
    ) {
        processor { raw ->
            if (extraType.isInstance(extra)) {
                @Suppress("UNCHECKED_CAST")
                processor.accept(this as Event<EX>, raw)
            }
        }
    }

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。
     */
    @Api4J
    public fun <EX : EventExtra> asyncProcessor(
        extraType: Class<EX>,
        processorType: ProcessorType,
        processor: AsyncEventProcessor<EventExtra>
    ) {
        processor { raw ->
            if (extraType.isInstance(extra)) {
                @Suppress("UNCHECKED_CAST")
                processor.accept(this as Event<EX>, raw).await()
            }
        }
    }

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。
     *
     * @param processor 事件处理器
     * @param extraType [Event.extra] 的类型。
     */
    @Api4J
    public fun <EX : EventExtra> blockingProcessor(
        extraType: Class<EX>,
        processor: BlockingEventProcessor<EX>
    ) {
        blockingProcessor(extraType, ProcessorType.NORMAL, processor)
    }

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。
     */
    @Api4J
    public fun <EX : EventExtra> asyncProcessor(
        extraType: Class<EX>,
        processor: AsyncEventProcessor<EventExtra>
    ) {
        asyncProcessor(extraType, ProcessorType.NORMAL, processor)
    }

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。
     *
     * @param eventType [Event.type] 。
     * @param processorType 事件处理器类型。默认为 [ProcessorType.NORMAL]。
     * @param processor 事件处理器
     */
    @Api4J
    public fun blockingProcessor(
        eventType: Event.Type,
        processorType: ProcessorType,
        processor: BlockingEventProcessor<EventExtra>
    ) {
        processor { raw ->
            if (typeValue == eventType.value) {
                processor.accept(this, raw)
            }
        }
    }

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。
     *
     * @param eventType [Event.type] 。
     * @param processorType 事件处理器类型。默认为 [ProcessorType.NORMAL]。
     * @param processor 事件处理器
     */
    @Api4J
    public fun asyncProcessor(
        eventType: Event.Type,
        processorType: ProcessorType,
        processor: AsyncEventProcessor<EventExtra>
    ) {
        processor { raw ->
            if (typeValue == eventType.value) {
                processor.accept(this, raw)
            }
        }
    }

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。默认为 [ProcessorType.NORMAL]。
     *
     * @param eventType [Event.type] 。
     * @param processor 事件处理器
     */
    @Api4J
    public fun blockingProcessor(eventType: Event.Type, processor: BlockingEventProcessor<EventExtra>) {
        blockingProcessor(eventType, ProcessorType.NORMAL, processor)
    }

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。默认为 [ProcessorType.NORMAL]。
     *
     * @param eventType [Event.type] 。
     * @param processor 事件处理器
     */
    @Api4J
    public fun asyncProcessor(
        eventType: Event.Type,
        processor: AsyncEventProcessor<EventExtra>
    ) {
        asyncProcessor(eventType, ProcessorType.NORMAL, processor)
    }


}
