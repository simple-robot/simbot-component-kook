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
import kotlinx.coroutines.promise
import love.forte.simbot.util.api.requestor.API
import kotlin.js.Promise

/**
 * 面向 JS 平台实现的 [KookApi] 的抽象类。
 *
 * 额外拥有 Async 的请求API。
 *
 * 用于面向平台提供额外支持的能力。仅用于平台实现，不应被直接使用，请直接使用 [KookApi]。
 */
public actual abstract class PlatformKookApi<T> actual constructor() : API<KookApiRequestor, T> {

    /**
     * 通过一个 [HttpClient] 和校验信息 [authorization] 对当前API发起请求，并得到一个 [ApiResult]。
     *
     * 得到原始的 [HttpResponse] 而不对结果有任何处理。
     */
    public actual abstract suspend fun request(
        client: HttpClient,
        authorization: String
    ): HttpResponse

    /**
     * 通过一个 [HttpClient] 和校验信息 [authorization] 对当前API发起请求，并得到一个结果字符串。
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     */
    public actual abstract suspend fun requestRaw(
        client: HttpClient,
        authorization: String
    ): String


    /**
     * 通过一个 [HttpClient] 和校验信息 [authorization] 对当前API发起请求，并得到一个 [ApiResult] 结果。
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     * @throws ApiResultException 请求结果的 [ApiResult.code] 校验失败
     */
    public actual abstract suspend fun requestResult(client: HttpClient, authorization: String): ApiResult

    /**
     * 通过一个 [HttpClient] 和校验信息 [authorization] 对当前API发起请求，并得到一个具体结果。
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     * @throws ApiResultException 请求结果的 [ApiResult.code] 校验失败
     */
    public actual abstract suspend fun requestData(client: HttpClient, authorization: String): T

    /**
     * 通过一个 [requestor] 对当前API发起请求，并得到一个具体结果。
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     * @throws ApiResultException 请求结果的 [ApiResult.code] 校验失败
     */
    public actual abstract override suspend fun requestBy(requestor: KookApiRequestor): T

    // TODO Promise

    /**
     * 异步地执行 [request]
     * @see request
     *
     * 得到原始的 [HttpResponse] 而不对结果有任何处理。
     */
    @OptIn(DelicateCoroutinesApi::class)
    public fun requestAsync(client: HttpClient, authorization: String): Promise<HttpResponse> =
        GlobalScope.promise { request(client, authorization) }

    /**
     * 异步地执行 [requestRaw]
     * @see requestRaw
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     */
    @OptIn(DelicateCoroutinesApi::class)
    public fun requestRawAsync(client: HttpClient, authorization: String): Promise<String> =
        GlobalScope.promise { requestRaw(client, authorization) }

    /**
     * 异步地执行 [requestData]
     * @see requestData
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     * @throws ApiResultException 请求结果的 [ApiResult.code] 校验失败
     */
    @OptIn(DelicateCoroutinesApi::class)
    public fun requestDataAsync(client: HttpClient, authorization: String): Promise<T> =
        GlobalScope.promise { requestData(client, authorization) }

    /**
     * 异步地执行 [requestBy]
     *
     * @see requestBy
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     * @throws ApiResultException 请求结果的 [ApiResult.code] 校验失败
     */
    @OptIn(DelicateCoroutinesApi::class)
    public fun requestByAsync(requestor: KookApiRequestor): Promise<T> {
        val scope = requestor as? CoroutineScope ?: GlobalScope
        return scope.promise { requestBy(requestor) }
    }

}
