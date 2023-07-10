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
import love.forte.simbot.kook.event.Signal
import java.util.concurrent.CompletableFuture


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
    public actual fun processor(processorType: ProcessorType, processor: suspend Signal.Event<*>.(Event<*>) -> Unit)

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。
     *
     * @param processorType 事件处理器类型。默认为 [ProcessorType.NORMAL]。
     * @param processor 事件处理器
     */
    @Api4J
    public fun blockingProcessor(processorType: ProcessorType, processor: Signal.Event<*>.(Event<*>) -> Unit) {
        processor { event -> processor(event) }
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
        processor: Signal.Event<*>.(Event<*>) -> CompletableFuture<Unit?>
    ) {
        processor { event -> processor(event).await() }
    }

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。
     *
     * @param processor 事件处理器
     */
    @Api4J
    public fun blockingProcessor(processor: Signal.Event<*>.(Event<*>) -> Unit) {
        blockingProcessor(ProcessorType.NORMAL, processor)
    }

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。
     *
     * @param processor 事件处理器
     */
    @Api4J
    public fun asyncProcessor(processor: Signal.Event<*>.(Event<*>) -> CompletableFuture<Unit?>) {
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
        processor: Signal.Event<EX>.(Event<EX>) -> Unit
    ) {
        processor { event ->
            if (extraType.isInstance(event.extra)) {
                @Suppress("UNCHECKED_CAST")
                processor(this as Signal.Event<EX>, event as Event<EX>)
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
        processor: Signal.Event<EX>.(Event<EX>) -> CompletableFuture<Unit?>
    ) {
        processor { event ->
            if (extraType.isInstance(event.extra)) {
                @Suppress("UNCHECKED_CAST")
                processor(this as Signal.Event<EX>, event as Event<EX>).await()
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
        processor: Signal.Event<EX>.(Event<EX>) -> Unit
    ) {
        blockingProcessor(extraType, ProcessorType.NORMAL, processor)
    }

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。
     */
    @Api4J
    public fun <EX : EventExtra> asyncProcessor(
        extraType: Class<EX>,
        processor: Signal.Event<EX>.(Event<EX>) -> CompletableFuture<Unit?>
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
        processor: Signal.Event<*>.(Event<*>) -> Unit
    ) {
        processor { event ->
            if (event.typeValue == eventType.value) {
                processor(event)
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
        processor: Signal.Event<*>.(Event<*>) -> CompletableFuture<Unit?>
    ) {
        processor { event ->
            if (event.typeValue == eventType.value) {
                processor(event)
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
    public fun blockingProcessor(eventType: Event.Type, processor: Signal.Event<*>.(Event<*>) -> Unit) {
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
        processor: Signal.Event<*>.(Event<*>) -> CompletableFuture<Unit?>
    ) {
        asyncProcessor(eventType, ProcessorType.NORMAL, processor)
    }


}
