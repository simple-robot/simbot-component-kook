package love.forte.simbot.kook.util

import io.ktor.http.*


/**
 * 通过 [URLBuilder] lambda 构建 [Url] 实例。
 */
public inline fun buildUrl(builder: URLBuilder.() -> Unit): Url {
    return URLBuilder().apply(builder).build()
}