/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.api

import io.ktor.websocket.*
import love.forte.simbot.SimbotIllegalStateException


/**
 * 与 Kook api 相关的异常。
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
