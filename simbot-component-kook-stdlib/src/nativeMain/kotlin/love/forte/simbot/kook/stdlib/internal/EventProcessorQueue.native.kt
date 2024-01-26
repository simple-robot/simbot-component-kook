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

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

/**
 * 事件处理器队列。
 *
 * native 平台内基于 [ArrayList] 配合 [SynchronizedObject] 实现线程安全。
 */
public actual class EventProcessorQueue<T>(initialCapacity: Int) : SynchronizedObject() {
    // TODO copyOnWrite?
    private val list = ArrayList<T>(initialCapacity)

    /**
     * 向此队列添加一个元素。
     */
    public actual fun add(element: T): Boolean {
        return synchronized(this) { list.add(element) }
    }

    /**
     * 移除指定目标。
     */
    public actual fun remove(element: T): Boolean {
        return synchronized(this) { list.remove(element) }
    }

    /**
     * 判断当前队列内容是否为空
     */
    public actual fun isEmpty(): Boolean {
        return synchronized(this) { list.isEmpty() }
    }

    /**
     * 得到当前队列的迭代器。
     */
    public val iterator: MutableIterator<T> get() = synchronized(this) { list.iterator() }
}

/**
 * 创建一个 [EventProcessorQueue]。
 *
 * @param initialCapacity 初始容量。如果 [EventProcessorQueue] 的实现支持，那么应当在创建时指定初始容量。
 */
public actual fun <T> createEventProcessorQueue(initialCapacity: Int): EventProcessorQueue<T> =
    love.forte.simbot.kook.stdlib.internal.EventProcessorQueue(initialCapacity)

/**
 * foreach [EventProcessorQueue] values.
 */
public actual inline fun <T> EventProcessorQueue<T>.forEach(block: (T) -> Unit) {
    iterator.forEach(block)
}
