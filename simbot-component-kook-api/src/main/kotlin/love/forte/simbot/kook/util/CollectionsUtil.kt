/*
 * Copyright (c) 2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.util

import java.util.*


/**
 * 将目标转化为不可变列表。
 */
public inline fun <reified T> List<T>.unmodifiable(): List<T> = when {
    isEmpty() -> emptyList()
    size == 1 -> listOf(first())
    else -> Collections.unmodifiableList(toTypedArray().asList())
}

/**
 * 构建不可变列表。
 */
public inline fun <reified T> unmodifiableListOf(vararg values: T): List<T> = when {
    values.isEmpty() -> emptyList()
    values.size == 1 -> listOf(values[0])
    else -> listOf(*values).unmodifiable()
}
