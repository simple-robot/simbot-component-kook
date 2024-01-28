/*
 * Copyright (c) 2024. ForteScarlet.
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

@file:JvmName("EventProcessors")
@file:JvmMultifileClass

package love.forte.simbot.kook.stdlib

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runInterruptible
import love.forte.simbot.kook.event.Event
import love.forte.simbot.kook.event.EventExtra
import org.jetbrains.annotations.Blocking
import org.jetbrains.annotations.NonBlocking
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

/**
 * 一个阻塞式的 [EventProcessor]，是提供给 Java 的友好类型。
 * 可直接使用 `EventProcessors.block((event, raw) -> { ... })` 构建。
 *
 * [block] 最终会在 [Dispatchers.IO] 的调度器下使用 [runInterruptible] 执行。
 */
public fun interface JBlockEventProcessor : EventProcessor {
    /**
     * 处理事件。
     */
    @Blocking
    @Throws(Exception::class)
    public fun block(event: Event<*>, raw: String)

    @JvmSynthetic
    override suspend fun Event<*>.invoke(raw: String) {
        runInterruptible(Dispatchers.IO) { block(this, raw) }
    }
}

/**
 * 以阻塞的形式实现 [EventProcessor]，是提供给 Java 的友好类型。
 * 可直接使用 `EventProcessors.block((event, raw) -> { ... })` 构建。
 *
 *  [block] 最终会在 [Dispatchers.IO] 的调度器下使用 [runInterruptible] 执行。
 */
public fun interface TypedJBlockEventProcessor<E : EventExtra> {
    /**
     * 处理事件。
     */
    @Blocking
    @Throws(Exception::class)
    public fun block(event: Event<E>, raw: String)
}

/**
 * 构建一个阻塞式的 [EventProcessor]
 */
public fun block(function: JBlockEventProcessor): EventProcessor = function

/**
 * 构建一个处理目标类型 [E] 的阻塞式 [EventProcessor]
 */
@Suppress("UNCHECKED_CAST")
public fun <E : EventExtra> block(type: Class<E>, function: TypedJBlockEventProcessor<E>): EventProcessor =
    block { event, raw ->
        if (type.isInstance(event.extra)) {
            function.block(event as Event<E>, raw)
        }
    }

/**
 * 一个异步式的 [EventProcessor]，是提供给 Java 的友好类型。
 * 可直接使用 `EventProcessors.async((event, raw) -> { ... })` 构建。
 *
 * [block] 最终会在 [Dispatchers.IO] 的调度器下使用 [runInterruptible] 执行。
 */
public fun interface JAsyncEventProcessor : EventProcessor {
    /**
     * 处理事件。
     */
    @NonBlocking
    @Throws(Exception::class)
    public fun async(event: Event<*>, raw: String): CompletionStage<Void?>

    @JvmSynthetic
    override suspend fun Event<*>.invoke(raw: String) {
        async(this, raw).await()
    }
}

/**
 * 以异步的形式实现 [EventProcessor]，是提供给 Java 的友好类型。
 * 可直接使用 `EventProcessors.async((event, raw) -> { ... })` 构建。
 *
 *  [block] 最终会在 [Dispatchers.IO] 的调度器下使用 [runInterruptible] 执行。
 */
public fun interface TypedJAsyncEventProcessor<E : EventExtra> {
    /**
     * 处理事件。
     */
    @NonBlocking
    @Throws(Exception::class)
    public fun async(event: Event<E>, raw: String): CompletionStage<Void?>
}

/**
 * 构建一个异步式的 [EventProcessor]
 */
public fun async(function: JAsyncEventProcessor): EventProcessor = function

/**
 * 构建一个处理目标类型 [E] 的异步式 [EventProcessor]
 */
@Suppress("UNCHECKED_CAST")
public fun <E : EventExtra> async(type: Class<E>, function: TypedJAsyncEventProcessor<E>): EventProcessor =
    async { event, raw ->
        if (type.isInstance(event.extra)) {
            function.async(event as Event<E>, raw)
        } else {
            CompletableFuture.completedStage(null)
        }
    }
