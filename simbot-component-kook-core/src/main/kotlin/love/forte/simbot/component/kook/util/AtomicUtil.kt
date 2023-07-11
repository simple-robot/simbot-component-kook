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

package love.forte.simbot.component.kook.util

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater


internal inline fun <T, V> AtomicReferenceFieldUpdater<T, V>.update(obj: T, updateBlock: (V) -> V): V {
    var prev: V?
    var next: V
    do {
        prev = get(obj)
        next = updateBlock(prev)
    } while (!compareAndSet(obj, prev, next))

    return next
}