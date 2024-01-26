/*
 * Copyright (c) 2023-2024. ForteScarlet.
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

package love.forte.simbot.kook.stdlib

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.await
import love.forte.simbot.kook.api.KookApiRequestor
import love.forte.simbot.kook.event.Event
import kotlin.js.Promise


/**
 * 一个 JS 平台 KOOK Bot。
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
    public actual fun processor(processorType: ProcessorType, processor: suspend Event<*>.(raw: String) -> Unit)

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。
     *
     * @param processorType 事件处理器类型。默认为 [ProcessorType.NORMAL]。
     * @param processor 事件处理器，结果为一个 [Promise] 异步任务。
     */
    public fun asyncProcessor(
        processorType: ProcessorType = ProcessorType.NORMAL,
        processor: Event<*>.(raw: String) -> Promise<Unit?>
    ) {
        processor(processorType) { event -> processor(event).await() }
    }

    /**
     * 添加一个事件处理器。
     *
     * @param processorType 事件处理器类型。默认为 [ProcessorType.NORMAL]。
     * @param processor 事件处理器
     */
    public fun blockingProcessor(
        processorType: ProcessorType = ProcessorType.NORMAL,
        processor: Event<*>.(raw: String) -> Unit
    ) {
        processor(processorType) { event -> processor(event) }
    }
}
