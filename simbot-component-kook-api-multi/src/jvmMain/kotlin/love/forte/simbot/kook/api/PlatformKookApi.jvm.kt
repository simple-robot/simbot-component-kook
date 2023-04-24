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

package love.forte.simbot.kook.api

import io.ktor.client.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import kotlinx.coroutines.runBlocking
import love.forte.simbot.Api4J
import love.forte.simbot.util.api.requestor.API
import java.util.concurrent.CompletableFuture

/**
 * 面向 JVM 平台实现的 [KookApi] 的抽象类。
 *
 * 额外拥有 Blocking、Async 的请求API。
 *
 * 仅用于平台实现，不应被直接使用，请直接使用 [KookApi]。
 */
public actual abstract class PlatformKookApi<T> actual constructor() : API<KookApiRequestor, T> {
    /**
     * 通过一个 [HttpClient] 和校验信息 [authorization] 对当前API发起请求，并得到一个 [ApiResult]。
     *
     * 得到原始的 [HttpResponse] 而不对结果有任何处理。
     */
    @JvmSynthetic
    public actual abstract suspend fun request(
        client: HttpClient, authorization: String
    ): HttpResponse

    /**
     * 通过一个 [HttpClient] 和校验信息 [authorization] 对当前API发起请求，并得到一个结果字符串。
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     */
    @JvmSynthetic
    public actual abstract suspend fun requestRaw(
        client: HttpClient, authorization: String
    ): String

    /**
     * 通过一个 [HttpClient] 和校验信息 [authorization] 对当前API发起请求，并得到一个具体结果。
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     * @throws ApiResultException 请求结果的 [ApiResult.code] 校验失败
     */
    @JvmSynthetic
    public actual abstract suspend fun requestData(client: HttpClient, authorization: String): T

    /**
     * 通过一个 [requestor] 对当前API发起请求，并得到一个具体结果。
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     * @throws ApiResultException 请求结果的 [ApiResult.code] 校验失败
     */
    @JvmSynthetic
    public actual abstract override suspend fun requestBy(requestor: KookApiRequestor): T

    /**
     * 阻塞的使用 [request]
     *
     * @see request
     */
    @Api4J
    public fun requestBlocking(client: HttpClient, authorization: String): HttpResponse =
        runBlocking { request(client, authorization) }


    /**
     * 阻塞的使用 [requestRaw]
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     * @see requestRaw
     */
    @Api4J
    public fun requestRawBlocking(client: HttpClient, authorization: String): String =
        runBlocking { requestRaw(client, authorization) }


    /**
     * 阻塞的使用 [requestData]
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     * @throws ApiResultException 请求结果的 [ApiResult.code] 校验失败
     *
     * @see requestData
     */
    @Api4J
    public fun requestDataBlocking(client: HttpClient, authorization: String): T =
        runBlocking { requestData(client, authorization) }


    /**
     * 阻塞的使用 [requestBy]
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     * @throws ApiResultException 请求结果的 [ApiResult.code] 校验失败
     *
     * @see requestBy
     */
    @Api4J
    public fun requestByBlocking(requestor: KookApiRequestor): T = runBlocking { requestBy(requestor) }

    /**
     * 异步地使用 [request]
     *
     * @see request
     */
    @Api4J
    @OptIn(DelicateCoroutinesApi::class)
    public fun requestAsync(client: HttpClient, authorization: String): CompletableFuture<out HttpResponse> =
        GlobalScope.future { request(client, authorization) }


    /**
     * 异步地使用 [requestRaw]
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     * @see requestRaw
     */
    @Api4J
    @OptIn(DelicateCoroutinesApi::class)
    public fun requestRawAsync(client: HttpClient, authorization: String): CompletableFuture<out String> =
        GlobalScope.future { requestRaw(client, authorization) }


    /**
     * 异步地使用 [requestData]
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     * @throws ApiResultException 请求结果的 [ApiResult.code] 校验失败
     *
     * @see requestData
     */
    @Api4J
    @OptIn(DelicateCoroutinesApi::class)
    public fun requestDataAsync(client: HttpClient, authorization: String): CompletableFuture<out T> =
        GlobalScope.future { requestData(client, authorization) }


    /**
     * 异步地使用 [requestBy]
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     * @throws ApiResultException 请求结果的 [ApiResult.code] 校验失败
     *
     * @see requestBy
     */
    @Api4J
    @OptIn(DelicateCoroutinesApi::class)
    public fun requestByAsync(requestor: KookApiRequestor): CompletableFuture<out T> {
        val scope = requestor as? CoroutineScope ?: GlobalScope
        return scope.future { requestBy(requestor) }
    }


}
