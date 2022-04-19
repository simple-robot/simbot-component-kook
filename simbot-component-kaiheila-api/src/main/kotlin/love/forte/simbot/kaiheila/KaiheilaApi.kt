package love.forte.simbot.kaiheila

import io.ktor.http.*
import love.forte.simbot.kaiheila.util.buildUrl

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
     * 得到一个开黑啦API下的BaseUrl, 相当于 `"https://$HOST/api/v$VERSION"`.
     */
    public val baseUrl: Url = buildApiUrl()

    /**
     * 得到一个开黑啦API下的BaseUrl, 但是不携带版本信息。 相当于 `"https://$HOST/api"`.
     */
    public val baseUrlWithoutVersion: Url = buildApiUrl(withVersion = false)

    /**
     * 得到一个完整的路径前置。
     */
    public fun pathPrefix(withVersion: Boolean): String = if (withVersion) "/api/v$VERSION/" else "/api/"


    /**
     * 通过参数构建器 [parameterBuilder] 和 [paths] 构建一个开黑啦api的标准 [Url] 实例。
     * @param paths api路径的 `/api/vn` 后的真正api路径。
     * @param withVersion 是否携带 `/api/` 后面的版本信息。
     * @param parameterBuilder 可以构建参数。
     */
    public inline fun buildApiUrl(
        vararg paths: String,
        withVersion: Boolean = true,
        parameterBuilder: ParametersBuilder.() -> Unit = {},
    ): Url {
        return buildUrl {
            protocol = URLProtocol.HTTPS
            host = HOST
            port = DEFAULT_PORT
            parameters.parameterBuilder()
            encodedPath = paths.joinToString("/", prefix = pathPrefix(withVersion)) { it.encodeURLPath() }
        }
    }

    /**
     * 通过参数构建器 [parameterBuilder] 和 [paths] 构建一个开黑啦api的标准 [Url] 实例。
     * @param paths api路径的 `/api/vn` 后的真正api路径。
     * @param withVersion 是否携带 `/api/` 后面的版本信息。
     * @param parameterBuilder 可以构建参数。
     */
    public inline fun buildApiUrl(
        paths: List<String>,
        withVersion: Boolean = true,
        parameterBuilder: ParametersBuilder.() -> Unit = {},
    ): Url {
        return buildUrl {
            protocol = URLProtocol.HTTPS
            host = HOST
            port = DEFAULT_PORT
            parameters.parameterBuilder()
            encodedPath = paths.joinToString("/", prefix = pathPrefix(withVersion)) { it.encodeURLPath() }
        }
    }

}


