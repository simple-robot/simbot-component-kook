/*
 *
 *  * Copyright (c) 2021. ForteScarlet All rights reserved.
 *  * Project  simple-robot
 *  * File     MiraiAvatar.kt
 *  *
 *  * You can contact the author through the following channels:
 *  * github https://github.com/ForteScarlet
 *  * gitee  https://gitee.com/ForteScarlet
 *  * email  ForteScarlet@163.com
 *  * QQ     1149159218
 *
 */

@file:JvmName("ApiDataUtil")

package love.forte.simbot.kaiheila.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.*
import io.ktor.http.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import love.forte.simbot.kaiheila.*


/**
 * 代表、包装了一个开黑啦api的请求。
 *
 * ### 反序列化
 * [KaiheilaApiRequest] 面向 ktor, 并基于 `kotlinx.serialization` 进行反序列化。
 *
 * ### Url内容
 * 最终进行请求的 [url] 中部分参数（例如host）来自于 [KaiheilaApi].
 *
 * ### 不可变
 * 此接口的实现类应当是不可变、可复用的。
 */
public abstract class KaiheilaApiRequest<T> {

    /**
     * 此请求最终对应的url。最终拼接的URL中部分参数（例如host）来自于 [KaiheilaApi].
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
     *  }
     * ```
     *
     * 可以通过重写 [requestFinishingAction] 来实现提供额外的收尾操作，例如为请求提供 body 等。
     *
     */
    @JvmOverloads
    public open suspend fun request(
        client: HttpClient,
        authorization: String,
        decoder: Json = DEFAULT_JSON
    ): ApiResult {
        val response = requestForResponse(client, authorization) {
            requestFinishingAction()
        }

        return response.receive()
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
     *  }
     * ```
     *
     */
    @JvmOverloads
    public open suspend fun requestData(
        client: HttpClient,
        authorization: String,
        decoder: Json = DEFAULT_JSON
    ): T {
        val result = request(client, authorization, decoder)
        return result.parseDataOrThrow(decoder, resultDeserializer)
    }


    public companion object {
        internal val DEFAULT_JSON = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

}

/**
 * Do request for [HttpResponse].
 */
private suspend inline fun KaiheilaApiRequest<*>.requestForResponse(
    client: HttpClient,
    authorization: String,
    finishingAction: HttpRequestBuilder.() -> Unit = {} // more, like content type, body, etc.
): HttpResponse {
    return client.request {
        this.method = this@requestForResponse.method

        // set url
        url(this@requestForResponse.url)

        headers {
            this[HttpHeaders.Authorization] = authorization
            this[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }

        // 收尾动作
        finishingAction()
    }
}


public abstract class BaseKaiheilaApiRequest<T> : KaiheilaApiRequest<T>() {
    /**
     * api 路径。
     */
    protected abstract val apiPaths: List<String>

    /**
     * 参数构建器。
     */
    protected abstract fun ParametersBuilder.buildParameters()

    private lateinit var _url: Url

    /**
     * 通过 [apiPaths] 和 [buildParameters] 懒构建 [url] 属性。
     */
    override val url: Url
        get() {
            return if (::_url.isInitialized) {
                _url
            } else {
                val buildUrl = KaiheilaApi.buildApiUrl(apiPaths, true) {
                    buildParameters()
                }
                buildUrl.also { _url = it }
            }
        }

}


/**
 * 使用 Get 请求的 [KaiheilaApiRequest] 基础实现。
 */
public abstract class KaiheilaGetRequest<T> : BaseKaiheilaApiRequest<T>() {
    override val method: HttpMethod
        get() = HttpMethod.Get
}


/**
 * 使用 Get 请求的 [KaiheilaApiRequest] 基础实现。
 */
public abstract class KaiheilaPostRequest<T> : BaseKaiheilaApiRequest<T>() {
    override val method: HttpMethod
        get() = HttpMethod.Post

    /**
     * 可以提供一个body实例。
     */
    public abstract val body: Any?

    /**
     * 通过 [body] 构建提供 body 属性。
     */
    override fun HttpRequestBuilder.requestFinishingAction() {
        body = this@KaiheilaPostRequest.body ?: EmptyContent
    }


}

