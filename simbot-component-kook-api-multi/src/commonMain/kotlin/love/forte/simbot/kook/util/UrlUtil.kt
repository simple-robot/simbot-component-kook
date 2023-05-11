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

package love.forte.simbot.kook.util

import io.ktor.http.*


/**
 * 通过 [URLBuilder] lambda 构建 [Url] 实例。
 */
public inline fun buildUrl(url: Url? = null, builder: URLBuilder.() -> Unit): Url {
    val urlBuilder = url?.let { URLBuilder(it) } ?: URLBuilder()
    return urlBuilder.apply(builder).build()
}

/**
 * 通过 [URLBuilder] lambda 构建 [Url] 实例。
 */
public inline fun buildUrl(urlString: String, builder: URLBuilder.() -> Unit): Url {
    return URLBuilder(urlString).apply(builder).build()
}

/**
 * 使用 [URLBuilder.parameters]
 */
public inline fun URLBuilder.parameters(block: ParametersBuilder.() -> Unit) {
    parameters.apply(block)
}
