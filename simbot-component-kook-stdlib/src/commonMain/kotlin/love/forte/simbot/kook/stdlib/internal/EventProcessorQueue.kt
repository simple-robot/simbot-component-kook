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

package love.forte.simbot.kook.stdlib.internal

import love.forte.simbot.InternalSimbotApi

/**
 * 事件处理器队列。
 *
 * [EventProcessorQueue] 由平台实现，对其中的各项操作应当是线程安全的。
 */
@InternalSimbotApi
public expect class EventProcessorQueue<T> {

    /**
     * 向此队列添加一个元素。
     */
    public fun add(element: T): Boolean

    /**
     * 移除指定目标。
     */
    public fun remove(element: T): Boolean

    /**
     * 判断当前队列内容是否为空
     */
    public fun isEmpty(): Boolean
}

/**
 * 创建一个 [EventProcessorQueue]。
 *
 * @param initialCapacity 初始容量。如果 [EventProcessorQueue] 的实现支持，那么应当在创建时指定初始容量。
 */
public expect fun <T> createEventProcessorQueue(initialCapacity: Int): EventProcessorQueue<T>

/**
 * foreach [EventProcessorQueue] values.
 */
public expect inline fun <T> EventProcessorQueue<T>.forEach(block: (T) -> Unit)

/**
 * 判断当前队列内容是否不为空
 *
 * @see EventProcessorQueue.isEmpty
 */
public fun <T> EventProcessorQueue<T>.isNotEmpty(): Boolean = !isEmpty()
