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

package love.forte.simbot.kook

import io.ktor.websocket.*
import kotlinx.serialization.SerializationException

/**
 * bot启动过程中、连接失败（比如多次重试仍无法连接）时抛出的异常。
 */
public class KookBotConnectException : KookException {
    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(cause: Throwable?) : super(cause)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
}

/**
 * Bot 连接成功后，当连接关闭时抛出的异常。
 */
public class KookBotClientCloseException : KookException {
    public val closeReason: CloseReason?

    public constructor(closeReason: CloseReason?) : super() {
        this.closeReason = closeReason
    }

    public constructor(closeReason: CloseReason?, message: String?) : super(message) {
        this.closeReason = closeReason
    }

    public constructor(closeReason: CloseReason?, cause: Throwable?) : super(cause) {
        this.closeReason = closeReason
    }

    public constructor(closeReason: CloseReason?, message: String?, cause: Throwable?) : super(message, cause) {
        this.closeReason = closeReason
    }
}

/**
 * 事件接收中因为事件无法反序列化而导致的错误。
 *
 */
public class EventDeserializationException(
    /**
     * 接收到的原始消息字符串
     */
    public val source: String,
    message: String, cause: SerializationException
) : KookException(message, cause)
