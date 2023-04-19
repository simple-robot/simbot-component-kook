/*
 * Copyright (c) 2021-2023. ForteScarlet.
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

@file:JvmName("ApiDataUtil")

package love.forte.simbot.kook.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import love.forte.simbot.Api4J
import love.forte.simbot.InternalSimbotApi
import love.forte.simbot.kook.Kook
import love.forte.simbot.logger.LoggerFactory
import love.forte.simbot.util.api.requestor.API
import love.forte.simbot.utils.runInAsync
import love.forte.simbot.utils.runInNoScopeBlocking
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

internal val apiLogger = LoggerFactory.getLogger("love.forte.simbot.kook.api")

/**
 * 代表、包装了一个 Kook api的请求。
 *
 * ### 反序列化
 * [KookApiRequest] 面向 ktor, 并基于 `kotlinx.serialization` 进行反序列化。
 *
 * ### Url内容
 * 最终进行请求的 [url] 中部分参数（例如host）来自于 [Kook].
 *
 * ### 不可变
 * 此接口的实现类应当是不可变、可复用的。
 */
public abstract class KookApiRequest<T> : API<KookApiRequestor, T> {

    /**
     * 此请求最终对应的url。最终拼接的URL中部分参数（例如host）来自于 [Kook].
     */
    public abstract val url: Url


    /**
     * 此请求的 method.
     */
    public abstract val method: HttpMethod


    /**
     * 得到响应值的反序列化器.
     */
    public abstract val resultDeserializer: DeserializationStrategy<out T>


    /**
     * 可以为 [request] 提供更多行为的函数，例如提供body、重置contentType等。
     */
    protected open fun HttpRequestBuilder.requestFinishingAction() {
        headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
    }

    /**
     * 根据 [requestor] 中的信息请求当前API。
     */
    @JvmSynthetic
    override suspend fun requestBy(requestor: KookApiRequestor): T {
        return requestData(
            requestor.client,
            requestor.authorization,
            DEFAULT_JSON
        )
    }

    /**
     * @suppress Deprecated
     */
    @Api4J
    @Deprecated("Use 'requestByBlocking'", ReplaceWith("requestByBlocking(requester)"))
    public fun requestBlockingBy(requestor: KookApiRequestor): T = requestByBlocking(requestor)

    /**
     * 阻塞的请求当前API。
     *
     * @see requestBy
     */
    @Api4J
    public fun requestByBlocking(requestor: KookApiRequestor): T = runInNoScopeBlocking { requestBy(requestor) }

    /**
     * 异步的请求当前API。如果 [requestor] 是 [CoroutineScope] 类型则会使用其作用域。
     *
     * @see requestBy
     */
    @OptIn(InternalSimbotApi::class)
    @Api4J
    public fun requestByAsync(requestor: KookApiRequestor): CompletableFuture<out T> {
        return if (requestor is CoroutineScope) {
            runInAsync(requestor) { requestBy(requestor) }
        } else {
            runInAsync { requestBy(requestor) }
        }
    }

    /**
     * 通过 [client] 执行网络请求并尝试得到结果。
     *
     * @param client 使用的 [HttpClient] 实例。
     * @param authorization 使用的鉴权值。注意，这里是完整的 `Authorization` 请求头中应当存在的内容，例如 `Bot aaaabbbbccccdddd`. 请参考 <https://developer.kaiheila.cn/doc/reference#%E5%B8%B8%E8%A7%84%20http%20%E6%8E%A5%E5%8F%A3%E8%A7%84%E8%8C%83>.
     *
     * 可以通过重写 [requestFinishingAction] 来实现提供额外的收尾操作，例如为请求提供 body 等。
     *
     * @param postChecker 当得到了 http response 之后的后置检查，可以用于提供部分自定义的响应值检查函数，例如进行速率限制检查。
     *
     * @throws ApiRateLimitException 当API速度达到上限的时候。检查需要通过 [postChecker] 进行实现支持。
     *
     */
    @JvmSynthetic
    public open suspend fun request(
        client: HttpClient,
        authorization: String,
        postChecker: suspend (HttpResponse) -> Unit = {}
    ): ApiResult {
        val apiId: String? = if (apiLogger.isDebugEnabled) {
            "${method.value} ${url.encodedPath}"
        } else null

        if (this is KookPostRequest) {
            apiLogger.debug("API[{}] ======> query: {}, body: {}", apiId, url.encodedQuery.ifEmpty { "<EMPTY>" }, body)
        } else {
            apiLogger.debug("API[{}] ======> query: {}", apiId, url.encodedQuery.ifEmpty { "<EMPTY>" })
        }

        val response = requestForResponse(client, authorization) {
            requestFinishingAction()
        }

        apiLogger.debug("API[{}] <====== status: {}, response: {}", apiId, response.status, response)

        postChecker(response)

        apiLogger.debug("API[{}] <====== post checker", apiId)

        val rawText = response.bodyAsText()

        apiLogger.debug("API[{}] <====== result: {}", apiId, rawText)

        val result = DEFAULT_JSON.decodeFromString(ApiResult.serializer(), rawText)

        result.httpStatusCode = response.status.value
        result.httpStatusDescription = response.status.description

        // init rate limit info.
        val headers = response.headers

        val limit = headers.rateLimit
        val remaining = headers.rateRemaining
        val reset = headers.rateReset
        val bucket = headers.rateBucket
        val global = headers.isRateGlobal

        val rateLimit = if (limit != null || remaining != null || reset != null || bucket != null || global) {
            RateLimit(
                limit ?: RateLimit.DEFAULT.limit,
                remaining ?: RateLimit.DEFAULT.remaining,
                reset ?: RateLimit.DEFAULT.reset,
                bucket ?: RateLimit.DEFAULT.bucket,
                global
            ).also {
                apiLogger.debug("API[{}] <====== rate limit: {}", apiId, it)
            }
        } else {
            RateLimit.DEFAULT.also {
                apiLogger.debug("API[{}] <====== rate limit: {} (DEFAULT)", apiId, it)
            }
        }


        result.rateLimit = rateLimit

        return result
    }


    /**
     * 阻塞地请求当前 API。
     *
     * @see request
     */
    @Api4J
    @JvmOverloads
    public open fun requestBlocking(
        client: HttpClient,
        authorization: String,
        postChecker: Consumer<HttpResponse> = defaultRequestPostChecker
    ): ApiResult = runInNoScopeBlocking {
        request(client, authorization) { resp ->
            postChecker.accept(resp)
        }
    }

    /**
     * 异步地请求当前 API。
     *
     * @see request
     */
    @OptIn(InternalSimbotApi::class)
    @Api4J
    @JvmOverloads
    public open fun requestAsync(
        client: HttpClient,
        authorization: String,
        postChecker: Consumer<HttpResponse> = defaultRequestPostChecker
    ): CompletableFuture<out ApiResult> = runInAsync {
        request(client, authorization) { resp ->
            postChecker.accept(resp)
        }
    }


    /**
     * 通过 [client] 执行网络请求并尝试得到结果。
     *
     * @param client 使用的 [HttpClient] 实例。
     * @param authorization 使用的鉴权值。注意，这里是完整的 `Authorization` 请求头中应当存在的内容，例如 `Bot aaaabbbbccccdddd`. 请参考 <https://developer.kaiheila.cn/doc/reference#%E5%B8%B8%E8%A7%84%20http%20%E6%8E%A5%E5%8F%A3%E8%A7%84%E8%8C%83>.
     * @param decoder 用于反序列化的 [Json] 实例。如果不提供则使用内部的默认值:
     * ```kotlin
     * Json {
     *      isLenient = true
     *      ignoreUnknownKeys = true
     *      encodeDefaults = true
     *  }
     * ```
     *
     */
    @JvmSynthetic
    public open suspend fun requestData(
        client: HttpClient,
        authorization: String,
        decoder: Json = DEFAULT_JSON
    ): T {
        val result = request(client, authorization)
        return result.parseDataOrThrow(decoder, resultDeserializer)
    }


    /**
     * 阻塞地请求当前API并得到结果。
     *
     * @see requestData
     */
    @Api4J
    @JvmOverloads
    public open fun requestDataBlocking(
        client: HttpClient, authorization: String, decoder: Json = DEFAULT_JSON
    ): T = runInNoScopeBlocking { requestData(client, authorization, decoder) }

    /**
     * 异步地请求当前API并得到结果。
     *
     * @see requestData
     */
    @OptIn(InternalSimbotApi::class)
    @Api4J
    @JvmOverloads
    public open fun requestDataAsync(
        client: HttpClient, authorization: String, decoder: Json = DEFAULT_JSON
    ): CompletableFuture<out T> = runInAsync { requestData(client, authorization, decoder) }


    public companion object {
        internal val DEFAULT_JSON = Json {
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

        internal val defaultRequestPostChecker: Consumer<HttpResponse> = Consumer {}
    }

}

/**
 * Do request for [HttpResponse].
 */
private suspend inline fun KookApiRequest<*>.requestForResponse(
    client: HttpClient,
    authorization: String,
    finishingAction: HttpRequestBuilder.() -> Unit = {} // more, like content type, body, etc.
): HttpResponse {
    return client.request {
        this.method = this@requestForResponse.method

        // set url
        url(this@requestForResponse.url)

        headers[HttpHeaders.Authorization] = authorization

        // 收尾动作
        finishingAction()
    }
}

/**
 * [KookApiRequest] 的基础抽象类。
 */
public abstract class BaseKookApiRequest<T> : KookApiRequest<T>() {
    /**
     * api 路径。
     */
    protected abstract val apiPaths: List<String>

    /**
     * 参数构建器。
     */
    protected open fun ParametersBuilder.buildParameters() {}

    private lateinit var _url: Url

    /**
     * 通过 [apiPaths] 和 [buildParameters] 懒构建 [url] 属性。
     */
    override val url: Url
        get() {
            return if (::_url.isInitialized) {
                _url
            } else {
                val buildUrl = Kook.buildApiUrl(apiPaths, true) {
                    buildParameters()
                }
                buildUrl.also { _url = it }
            }
        }

}


/**
 * 使用 Get 请求的 [KookApiRequest] 基础实现。
 */
public abstract class KookGetRequest<T> : BaseKookApiRequest<T>() {
    override val method: HttpMethod
        get() = HttpMethod.Get
}


/**
 * 使用 Get 请求的 [KookApiRequest] 基础实现。
 */
public abstract class KookPostRequest<T>(
    /**
     * 是否缓存Body实例。如果开启缓存，且没有重写 [body] 或者 [createBody], 则会通过 [createBody] 懒初始化 [body] 实例。
     * 如果不缓存body, 且没有重写 [body] 或者 [createBody], 则 [body] 每次都会通过 [createBody] 构建新的实例。
     *
     * 默认开启。
     */
    private val cacheBody: Boolean = true,
) : BaseKookApiRequest<T>() {
    override val method: HttpMethod
        get() = HttpMethod.Post

    private lateinit var _body: Any


    /**
     * 可以提供一个body实例。
     */
    public open val body: Any?
        get() {
            return if (cacheBody) {
                if (::_body.isInitialized) {
                    // initialized.
                    val b = _body
                    if (b is NULL) null else b
                } else {
                    synchronized(this) {
                        if (::_body.isInitialized) {
                            // initialized.
                            val b = _body
                            if (b is NULL) null else b
                        } else {
                            createBody().also {
                                _body = it ?: NULL
                            }
                        }
                    }
                }

            } else createBody()
        }

    /**
     * 构建一个新的 [body] 所使用的函数。用于简化懒加载逻辑。
     */
    protected open fun createBody(): Any? = null

    /**
     * 通过 [body] 构建提供 body 属性。
     */
    override fun HttpRequestBuilder.requestFinishingAction() {
        headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        setBody(this@KookPostRequest.body ?: EmptyContent)
    }


    private object NULL
}

/**
 * 当目标值 [value] 不为null并且通过 [check] 检测的时候。
 */
public inline fun <reified T : Any?> ParametersBuilder.appendIfNotnullAnd(
    name: String,
    value: T?,
    check: (T) -> Boolean,
    toStringBlock: (T) -> String = { it.toString() },
) {
    value?.takeIf(check)?.let { v ->
        append(name, toStringBlock(v))
    }
}

/**
 * 当目标值 [value] 不为null时拼接。
 */
public inline fun <reified T> ParametersBuilder.appendIfNotnull(
    name: String,
    value: T?,
    toStringBlock: (T) -> String = { it.toString() },
) {
    value?.let { v ->
        append(name, toStringBlock(v))
    }
}
