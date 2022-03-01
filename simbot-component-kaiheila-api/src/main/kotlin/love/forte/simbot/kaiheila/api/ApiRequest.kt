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
public abstract class KaiheilaApiRequest<T, out R : KaiheilaApiResult<T>> {

    /**
     * 此请求最终对应的url。最终拼接的URL中部分参数（例如host）来自于 [KaiheilaApi].
     */
    public abstract val url: Url


    /**
     * 得到响应值的反序列化器.
     */
    public abstract val resultDeserializer: DeserializationStrategy<out R>


    /**
     * 通过 [client] 执行网络请求并尝试得到结果。
     * @param client 使用的 [HttpClient] 实例。
     * @param authorization 使用的鉴权值。注意，这里是完整的 `Authorization` 请求头中应当存在的内容，例如 `Bot aaaabbbbccccdddd`. 请参考 <https://developer.kaiheila.cn/doc/reference#%E5%B8%B8%E8%A7%84%20http%20%E6%8E%A5%E5%8F%A3%E8%A7%84%E8%8C%83>.
     * @param decoder 用于反序列化的 [Json] 实例。如果不提供则使用内部的默认值:
     * ```kotlin
     * Json {
     *      isLenient = true
     *      ignoreUnknownKeys = true
     *  }
     * ```
     */
    public open suspend fun request(
        client: HttpClient,
        authorization: String,
        decoder: Json = DEFAULT_JSON
    ) {
        TODO("Impl this.")
    }


    public companion object {
        internal val DEFAULT_JSON = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

}








public interface ParametersAppender {
    public fun append(key: String, value: Any)
}


public interface RouteInfoBuilder {
    /**
     * 可以设置api路径
     */
    public var apiPath: List<String>

    /**
     * 获取parameter的构建器
     */
    public val parametersAppender: ParametersAppender

    /**
     * 请求头中的 [ContentType], 绝大多数情况下，此参数默认为 [ContentType.Application.Json].
     */
    public var contentType: ContentType?


    public companion object {
        @JvmStatic
        public fun getInstance(parametersBuilder: ParametersAppender, contentType: ContentType?): RouteInfoBuilder =
            RouteInfoBuilderImpl(parametersAppender = parametersBuilder, contentType = contentType)
    }
}


public inline fun RouteInfoBuilder.parameters(block: ParametersAppender.() -> Unit) {
    parametersAppender.block()
}

public inline fun <reified T> ParametersAppender.appendIfNotnull(
    name: String,
    value: T?,
    toStringBlock: (T) -> String = { it.toString() },
) {
    value?.let { v ->
        append(name, toStringBlock(v))
    }
}


private data class RouteInfoBuilderImpl(
    override var apiPath: List<String> = emptyList(),
    override val parametersAppender: ParametersAppender,
    override var contentType: ContentType?,
) : RouteInfoBuilder

