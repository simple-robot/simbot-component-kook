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
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import love.forte.simbot.util.api.requestor.API
import kotlin.jvm.JvmSynthetic

/**
 * 面向平台实现的 [KookApi] 的抽象类。
 *
 * 用于面向平台提供额外支持的能力。仅用于平台实现，不应被直接使用，请直接使用 [KookApi]。
 */
public expect abstract class PlatformKookApi<T>() : API<KookApiRequestor, T> {

    /**
     * 通过一个 [HttpClient] 和校验信息 [authorization] 对当前API发起请求，并得到一个 [ApiResult]。
     *
     * 得到原始的 [HttpResponse] 而不对结果有任何处理。
     */
    @JvmSynthetic
    public abstract suspend fun request(client: HttpClient, authorization: String): HttpResponse

    /**
     * 通过一个 [HttpClient] 和校验信息 [authorization] 对当前API发起请求，并得到一个结果字符串。
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     */
    @JvmSynthetic
    public abstract suspend fun requestRaw(client: HttpClient, authorization: String): String

    /**
     * 通过一个 [HttpClient] 和校验信息 [authorization] 对当前API发起请求，并得到一个具体结果。
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     * @throws ApiResultException 请求结果的 [ApiResult.code] 校验失败
     */
    @JvmSynthetic
    public abstract suspend fun requestData(client: HttpClient, authorization: String): T

    /**
     * 通过一个 [requestor] 对当前API发起请求，并得到一个具体结果。
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     * @throws ApiResultException 请求结果的 [ApiResult.code] 校验失败
     */
    @JvmSynthetic
    abstract override suspend fun requestBy(requestor: KookApiRequestor): T
}

/**
 * 一个用于表示 KOOK API 封装的抽象类。
 *
 * [KookApi] 面向 `Ktor` 并基于 `kotlinx.serialization` 进行反序列化。
 *
 * @param T 此API预期的响应结果数据类型
 *
 * @author ForteScarlet
 */
public abstract class KookApi<T> : API<KookApiRequestor, T>, PlatformKookApi<T>() {

    /**
     * 此请求最终的url。
     */
    public abstract val url: Url

    /**
     * 此请求使用的 [HttpMethod]。
     */
    public abstract val method: HttpMethod

    /**
     * 预期结果类型的反序列化策略。
     */
    public abstract val resultDeserializer: DeserializationStrategy<T>

    /**
     * 可以为 [request] 提供更多行为的函数，例如提供body、重置contentType等。
     */
    protected open fun HttpRequestBuilder.requestFinishingAction() {
        headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
    }


    /**
     * 通过一个 [HttpClient] 和校验信息 [authorization] 对当前API发起请求，并得到一个 [ApiResult]。
     *
     * 得到原始的 [HttpResponse] 而不对结果有任何处理。
     */
    @JvmSynthetic
    override suspend fun request(client: HttpClient, authorization: String): HttpResponse {
        TODO()
    }

    /**
     * 通过一个 [HttpClient] 和校验信息 [authorization] 对当前API发起请求，并得到一个结果字符串。
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     */
    @JvmSynthetic
    override suspend fun requestRaw(client: HttpClient, authorization: String): String {
        TODO()
    }

    /**
     * 通过一个 [HttpClient] 和校验信息 [authorization] 对当前API发起请求，并得到一个具体结果。
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     * @throws ApiResultException 请求结果的 [ApiResult.code] 校验失败
     */
    @JvmSynthetic
    override suspend fun requestData(client: HttpClient, authorization: String): T {
        TODO()
    }

    /**
     * 通过一个 [requestor] 对当前API发起请求，并得到一个具体结果。
     *
     * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
     * @throws ApiResultException 请求结果的 [ApiResult.code] 校验失败
     */
    @JvmSynthetic
    override suspend fun requestBy(requestor: KookApiRequestor): T =
        requestData(requestor.client, requestor.authorization)


    public companion object {
        internal val DEFAULT_JSON = Json {
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }
}
