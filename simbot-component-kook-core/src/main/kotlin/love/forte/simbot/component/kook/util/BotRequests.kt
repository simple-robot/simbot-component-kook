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
@file:JvmName("BotRequestUtil")

package love.forte.simbot.component.kook.util

import io.ktor.client.statement.*
import love.forte.simbot.Api4J
import love.forte.simbot.component.kook.bot.KookBot
import love.forte.simbot.kook.api.ApiResult
import love.forte.simbot.kook.api.KookApi
import love.forte.simbot.utils.runInAsync
import love.forte.simbot.utils.runInNoScopeBlocking
import java.util.concurrent.CompletableFuture

//region request
/**
 * 使用 [KookBot] 对 [api] 发起请求。
 *
 * @see KookApi.request
 */
@JvmSynthetic
public suspend fun KookBot.request(api: KookApi<*>): HttpResponse {
    return api.request(sourceBot.apiClient, sourceBot.authorization)
}

/**
 * 阻塞地使用 [KookBot] 对 [api] 发起请求。
 *
 * @see request
 */
@Api4J
public fun KookBot.requestBlocking(api: KookApi<*>): HttpResponse = runInNoScopeBlocking {
    request(api)
}

/**
 * 异步地使用 [KookBot] 对 [api] 发起请求。
 *
 * @see request
 */
@Api4J
public fun KookBot.requestAsync(api: KookApi<*>): CompletableFuture<HttpResponse> = runInAsync(this) {
    request(api)
}

/**
 * 使用 [KookBot] 对 [api] 发起请求。
 *
 * @see KookApi.requestDataBy
 */
@JvmSynthetic
public suspend fun <T> KookBot.requestData(api: KookApi<T>): T {
    return api.requestData(sourceBot.apiClient, sourceBot.authorization)
}

/**
 * 阻塞地使用 [KookBot] 对 [api] 发起请求。
 *
 * @see requestDataBy
 */
@Api4J
public fun <T> KookBot.requestDataBlocking(api: KookApi<T>): T = runInNoScopeBlocking {
    requestData(api)
}

/**
 * 异步地使用 [KookBot] 对 [api] 发起请求。
 *
 * @see requestDataBy
 */
@Api4J
public fun <T> KookBot.requestDataAsync(api: KookApi<T>): CompletableFuture<T> = runInAsync(this) {
    requestData(api)
}

/**
 * 使用 [KookBot] 对 [api] 发起请求。
 *
 * @see KookApi.requestRawBy
 */
@JvmSynthetic
public suspend fun KookBot.requestRaw(api: KookApi<*>): String {
    return api.requestRaw(sourceBot.apiClient, sourceBot.authorization)
}

/**
 * 阻塞地使用 [KookBot] 对 [api] 发起请求。
 *
 * @see requestRawBy
 */
@Api4J
public fun KookBot.requestRawBlocking(api: KookApi<*>): String = runInNoScopeBlocking {
    requestRaw(api)
}

/**
 * 异步地使用 [KookBot] 对 [api] 发起请求。
 *
 * @see requestRawBy
 */
@Api4J
public fun KookBot.requestRawAsync(api: KookApi<*>): CompletableFuture<String> = runInAsync(this) {
    requestRaw(api)
}

/**
 * 使用 [KookBot] 对 [api] 发起请求。
 *
 * @see KookApi.requestResultBy
 */
@JvmSynthetic
public suspend fun KookBot.requestResult(api: KookApi<*>): ApiResult {
    return api.requestResult(sourceBot.apiClient, sourceBot.authorization)
}

/**
 * 阻塞地使用 [KookBot] 对 [api] 发起请求。
 *
 * @see requestResultBy
 */
@Api4J
public fun KookBot.requestResultBlocking(api: KookApi<*>): ApiResult = runInNoScopeBlocking {
    requestResult(api)
}

/**
 * 异步地使用 [KookBot] 对 [api] 发起请求。
 *
 * @see requestResultBy
 */
@Api4J
public fun KookBot.requestResultAsync(api: KookApi<*>): CompletableFuture<ApiResult> = runInAsync(this) {
    requestResult(api)
}
//endregion

//region requestBy
/**
 * 使用 [KookApi] 通过 [bot] 发起请求。
 *
 * @see KookApi.request
 */
@JvmSynthetic
public suspend fun KookApi<*>.requestBy(bot: KookBot): HttpResponse =
    bot.request(this)

/**
 * 使用 [KookApi] 通过 [bot] 发起请求。
 *
 * @see KookApi.requestDataBy
 */
@JvmSynthetic
public suspend fun <T> KookApi<T>.requestDataBy(bot: KookBot): T =
    bot.requestData(this)


/**
 * 使用 [KookApi] 通过 [bot] 发起请求。
 *
 * @see KookApi.requestRawBy
 */
@JvmSynthetic
public suspend fun KookApi<*>.requestRawBy(bot: KookBot): String =
    bot.requestRaw(this)


/**
 * 使用 [KookApi] 通过 [bot] 发起请求。
 *
 * @see KookApi.requestResultBy
 */
@JvmSynthetic
public suspend fun KookApi<*>.requestResultBy(bot: KookBot): ApiResult =
    bot.requestResult(this)
//endregion
