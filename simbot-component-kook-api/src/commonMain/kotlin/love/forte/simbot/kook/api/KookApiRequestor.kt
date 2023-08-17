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

package love.forte.simbot.kook.api

import io.ktor.client.*
import love.forte.simbot.util.api.requestor.Requestor


/**
 * 用于提供给 [KookApi] 作为请求器使用。
 * @author ForteScarlet
 */
public interface KookApiRequestor : Requestor {
    /**
     * 用于进行请求的 [HttpClient]
     */
    public val client: HttpClient
    
    /**
     * 使用的鉴权值。
     *
     * 注意，这里是完整的 `Authorization` 请求头中应当存在的内容，
     * 例如 `Bot aaaabbbbccccdddd`
     */
    public val authorization: String
}
