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

package love.forte.simbot.kook.internal

/**
 * typealias [EventProcessorQueue] for jvm.
 */
public actual typealias EventProcessorQueue<T> = java.util.concurrent.ConcurrentLinkedQueue<T>

/**
 * 创建一个 [EventProcessorQueue]。
 *
 * @param initialCapacity 初始容量。如果 [EventProcessorQueue] 的实现支持，那么应当在创建时指定初始容量。
 */
public actual fun <T> createEventProcessorQueue(initialCapacity: Int): EventProcessorQueue<T> = EventProcessorQueue()

/**
 * foreach [EventProcessorQueue] values.
 */
public actual inline fun <T> EventProcessorQueue<T>.forEach(block: (T) -> Unit) {
    (this as Iterable<T>).forEach { block(it) }
}
