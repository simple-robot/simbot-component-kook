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

@file:JvmName("ApiUtil")

package love.forte.simbot.kook.api

import io.ktor.client.*
import io.ktor.client.engine.*
import java.util.function.Consumer


/**
 * 直接构建一个 [HttpClient]. 需要环境中存在可用的引擎。
 *
 * @see HttpClient
 */
public fun createHttpClient(): HttpClient = HttpClient()

/**
 * 构建一个 [HttpClient].
 *
 * @param engine 使用的引擎
 * @param config [HttpClient] 构建的配置
 * @see HttpClient
 */
@JvmOverloads
public fun createHttpClient(engine: HttpClientEngine, config: Consumer<HttpClientConfig<*>>? = null): HttpClient =
    HttpClient(engine) { config?.accept(this) }


/**
 * 直接构建一个 [HttpClient].
 *
 * @param engineFactory 引擎工厂。
 * @param config [HttpClient] 的构建配置
 * @see HttpClient
 */
@JvmOverloads
public fun <T : HttpClientEngineConfig> createHttpClient(
    engineFactory: HttpClientEngineFactory<T>, config: Consumer<HttpClientConfig<T>>? = null
): HttpClient = HttpClient(engineFactory) { config?.accept(this) }
