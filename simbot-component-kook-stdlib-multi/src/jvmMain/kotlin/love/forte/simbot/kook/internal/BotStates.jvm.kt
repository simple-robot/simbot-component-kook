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
import java.util.zip.InflaterInputStream

/**
 * 由平台实现对二进制 `deflate` 压缩数据进行解压缩并转为字符串数据。
 * JVM 中会使用 [InflaterInputStream] 对 [Frame.Binary.data] 进行解码
 *
 * @throws UnsupportedOperationException 当不支持解析二进制数据时
 */
@InternalSimbotApi
public actual fun Frame.Binary.readToTextWithDeflated(): String {
    return InflaterInputStream(data.inputStream()).reader().use { it.readText() }
}
