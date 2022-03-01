package love.forte.simbot.kaiheila

import io.ktor.http.*

/**
 * 部分当前开黑啦api的信息常量，例如api版本信息。
 *
 * 相关内容参考 [开黑啦文档](https://developer.kaiheila.cn/doc/reference)
 */
@Suppress("MemberVisibilityCanBePrivate")
public object KaiheilaApi {

    /**
     * 当前的开黑啦api的版本。
     *
     * 参考 <https://developer.kaiheila.cn/doc/reference>。
     */
    public const val VERSION: String = "3"

    /**
     * 开黑啦api的url host.
     *
     * 参考 <https://developer.kaiheila.cn/doc/reference>。
     */
    public const val HOST: String = "www.kaiheila.cn"

    /**
     * 得到一个开黑啦API下的BaseUrl, 也就相当于 `https://HOST/api`.
     *
     *
     */
    public val baseUrl: Url = buildUrl {
        protocol = URLProtocol.HTTPS
        host = HOST
        port = DEFAULT_PORT
        path("api")
    }

    /**
     * 通过参数构建器 [parameterBuilder] 和 [paths] 构建一个开黑啦api的标准 [Url] 实例。
     */
    public inline fun buildApiUrl(
        vararg paths: String,
        parameterBuilder: ParametersBuilder.() -> Unit = {},
    ): Url {
        return buildUrl {
            protocol = URLProtocol.HTTPS
            host = HOST
            port = DEFAULT_PORT
            parameters.parameterBuilder()
            encodedPath = paths.joinToString("/", prefix = "/api/") { it.encodeURLPath() }
        }
    }

}

/**
 * 通过 [URLBuilder] lambda 构建 [Url] 实例。
 */
public inline fun buildUrl(builder: URLBuilder.() -> Unit): Url {
    return URLBuilder().apply(builder).build()
}

