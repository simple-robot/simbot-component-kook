/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kook 的一部分。
 *
 *  simbot-component-kook 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kook 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

package love.forte.simbot.kaiheila.api

import io.ktor.websocket.*
import love.forte.simbot.SimbotIllegalStateException


/**
 * 与  Kook api 相关的异常。
 * @author ForteScarlet
 */
@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
public open class KookApiException(
    public val code: Int,
    public val msg: String?,
    cause: Throwable? = null,
) : SimbotIllegalStateException("code: $code, message: $msg", cause) {
    public companion object {
        public const val NOT_FOUNT: Int = 404
    }
    
}


@Suppress("NOTHING_TO_INLINE")
public inline fun CloseReason?.err(e: Throwable? = null): Nothing {
    if (this == null) {
        if (e != null) {
            throw KookApiException(-1, "No reason", e)
        } else {
            throw KookApiException(-1, "No reason")
        }
    }
    val known = knownReason
    val message = message
    if (known != null) {
        if (e != null) {
            throw KookApiException(
                known.code.toInt(),
                "${known.name}: $message",
                e
            )
        } else {
            throw KookApiException(known.code.toInt(), "${known.name}: $message")
        }
    } else {
        if (e != null) {
            throw KookApiException(code.toInt(), message, e)
        } else {
            throw KookApiException(code.toInt(), message)
        }
    }
}