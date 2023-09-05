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

package love.forte.simbot.kook.internal

import io.ktor.websocket.*
import love.forte.simbot.InternalSimbotApi

/**
 * Native 平台中不支持, 会抛出 [UnsupportedOperationException]
 *
 * @throws UnsupportedOperationException 当不支持解析二进制数据时
 */
@InternalSimbotApi
public actual suspend fun Frame.Binary.readToTextWithDeflated(): String {
    throw UnsupportedOperationException("Parsing binary compressed data on native platforms is not yet supported")
}
