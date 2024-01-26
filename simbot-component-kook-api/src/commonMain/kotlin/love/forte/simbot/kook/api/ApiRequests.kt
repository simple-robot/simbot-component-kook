/*
 * Copyright (c) 2024. ForteScarlet.
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
@file:JvmName("ApiRequests")
@file:JvmMultifileClass

package love.forte.simbot.kook.api

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.common.serialization.guessSerializer
import love.forte.simbot.logger.LoggerFactory
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

private val apiLogger = LoggerFactory.getLogger("love.forte.simbot.kook.api")

/**
 * 通过一个 [HttpClient] 和校验信息 [authorization] 对当前API发起请求，并得到一个 [ApiResult]。
 *
 * 得到原始的 [HttpResponse] 而不对结果有任何处理。
 */
@JvmSynthetic
public suspend fun <T : Any> KookApi<T>.request(client: HttpClient, authorization: String): HttpResponse {
    val apiId: String? = if (apiLogger.isDebugEnabled()) {
        "${method.value} ${url.encodedPath}"
    } else null

    coroutineContext[APIContext]?.apiId = apiId

    if (this is KookPostApi) {
        apiLogger.debug(
            "API[{}] ======> query: {}, body: {}",
            apiId,
            url.encodedQuery.ifEmpty { "<EMPTY>" },
            body
        )
    } else {
        apiLogger.debug("API[{}] ======> query: {}", apiId, url.encodedQuery.ifEmpty { "<EMPTY>" })
    }

    val response = reqForResp(client, authorization)

    apiLogger.debug("API[{}] <====== status: {}, response: {}", apiId, response.status, response)

    return response
}

/**
 * 通过一个 [HttpClient] 和校验信息 [authorization] 对当前API发起请求，并得到一个结果字符串。
 *
 * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
 */
@JvmSynthetic
public suspend fun <T : Any> KookApi<T>.requestText(client: HttpClient, authorization: String): String {
    val response = request(client, authorization)
    return response.requireSuccess(this).bodyAsText()
}


/**
 * 通过一个 [HttpClient] 和校验信息 [authorization] 对当前API发起请求，并得到一个具体结果。
 *
 * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
 */
@JvmSynthetic
public suspend fun <T : Any> KookApi<T>.requestResult(client: HttpClient, authorization: String): ApiResult {
    var apiContext: APIContext? = null
    val response = if (apiLogger.isDebugEnabled()) {
        apiContext = coroutineContext[APIContext] ?: APIContext(null)
        withContext(apiContext) {
            request(client, authorization)
        }
    } else {
        request(client, authorization)
    }.requireSuccess(this)

    val raw = response.bodyAsText()

    apiLogger.debug("API[{}] <====== raw result: {}", apiContext?.apiId, raw)

    val result = KookApi.DEFAULT_JSON.decodeFromString(ApiResult.serializer(), raw)
    result.httpStatus = response.status
    result.raw = raw


    val rateLimit = response.headers.createRateLimit().also {
        apiLogger.debug("API[{}] <====== rate limit: {}", apiContext?.apiId, it)
    }
    result.rateLimit = rateLimit

    return result
}

/**
 * 通过一个 [HttpClient] 和校验信息 [authorization] 对当前API发起请求，并得到一个具体结果。
 *
 * @throws ApiResponseException 请求结果的状态码不是 200..300 之间
 * @throws ApiResultException 请求结果的 [ApiResult.code] 校验失败
 */
@JvmSynthetic
public suspend fun <T : Any> KookApi<T>.requestData(client: HttpClient, authorization: String): T {
    val result = requestResult(client, authorization)

    if (!result.isSuccess) {
        throw ApiResultException(result, "result.code is not success ($result)")
    }

    if (resultDeserializationStrategy == Unit.serializer()) {
        @Suppress("UNCHECKED_CAST")
        return Unit as T
    }

    return result.parseDataOrThrow(KookApi.DEFAULT_JSON, resultDeserializationStrategy)
}

private fun HttpResponse.requireSuccess(api: KookApi<*>): HttpResponse {
    if (!status.isSuccess()) {
        throw ApiResponseException(this, "API [$api] response status non-successful: $status")
    }

    return this
}

private suspend inline fun KookApi<*>.reqForResp(
    client: HttpClient,
    authorization: String,
    preAction: HttpRequestBuilder.() -> Unit = {},
    postAction: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = client.request {
    preAction()
    this.method = this@reqForResp.method
    url(this@reqForResp.url)
    headers {
        append(HttpHeaders.Authorization, authorization)
        appendAll(this@reqForResp.headers)
    }
    if (HttpHeaders.ContentType !in headers) {
        headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
    }

    // set Body
    when (val body = this@reqForResp.body) {
        null -> setBody(EmptyContent)
        is OutgoingContent -> setBody(body)
        else -> {
            if (client.pluginOrNull(ContentNegotiation) != null) {
                setBody(body)
            } else {
                try {
                    val ser = guessSerializer(body, KookApi.DEFAULT_JSON.serializersModule)
                    val bodyJson = KookApi.DEFAULT_JSON.encodeToString(ser, body)
                    setBody(bodyJson)
                } catch (e: Throwable) {
                    try {
                        setBody(body)
                    } catch (e0: Throwable) {
                        e0.addSuppressed(e)
                        throw e0
                    }
                }
            }
        }
    }

    postAction()
}

private class APIContext(
    var apiId: String? = null
) : AbstractCoroutineContextElement(APIContext) {
    /**
     * Key for [APIContext] instance in the coroutine context.
     */
    companion object Key : CoroutineContext.Key<APIContext>

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = "APIContext(apiId=$apiId)"
}
