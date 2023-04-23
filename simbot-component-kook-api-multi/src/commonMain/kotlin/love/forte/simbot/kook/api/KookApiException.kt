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

import io.ktor.client.statement.*
import love.forte.simbot.kook.KookException


/**
 * 当API请求得到失败响应得到此异常。
 * @author ForteScarlet
 */
public class ApiResponseException(
    /**
     * 响应结果
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public val response: HttpResponse,
    message: String? = null,
    cause: Throwable? = null,
) : KookException(message ?: "API response status is not success. status=${response.status}", cause)


/**
 * 当API请求的状态码成功，但是 [ApiResult] 表现结果为失败时。
 * @author ForteScarlet
 */
public class ApiResultException(
    /**
     * 响应结果
     */
    public val result: ApiResult,
    /**
     * 原始JSON字符串
     */
    public val raw: String,
    message: String? = null,
    cause: Throwable? = null,
) : KookException(message, cause)

