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

@file:JvmName("BotRequestUtil")

package love.forte.simbot.kook

import love.forte.simbot.Api4J
import love.forte.simbot.kook.api.ApiResult
import love.forte.simbot.kook.api.KookApiRequest
import love.forte.simbot.utils.runInNoScopeBlocking


/**
 * 通过指定Bot，利用其token和client发起请求并得到对应的[ApiResult]。
 *
 * 如果你希望自动检测 [ApiResult] 响应类型并直接得到 [ApiResult.data] 的反序列化结果，参考 [requestData]、[requestDataBy].
 *
 * @see requestData
 * @see requestDataBy
 */
@JvmSynthetic
public suspend inline fun KookApiRequest<*>.requestForResultBy(bot: KookBot): ApiResult {
    return request(
        bot.httpClient,
        bot.ticket.authorization,
    )
}

/**
 * 通过指定Bot，利用其token和client发起请求并得到对应的data响应值。
 */
@JvmSynthetic
public suspend inline fun <T> KookApiRequest<T>.requestDataBy(bot: KookBot): T {
    return requestData(
        bot.httpClient,
        bot.ticket.authorization,
        bot.configuration.decoder,
    )
}

/**
 * 通过指定Bot，利用其token和client发起请求并得到对应的[ApiResult]。
 *
 * 如果你希望自动检测 [ApiResult] 响应类型并直接得到 [ApiResult.data] 的反序列化结果，参考 [requestData]、[requestDataBy].
 *
 * @see requestData
 * @see requestDataBy
 *
 */
@JvmSynthetic
public suspend inline fun KookBot.request(api: KookApiRequest<*>): ApiResult {
    return api.requestForResultBy(this)
}

/**
 * 通过指定Bot，利用其token和client发起请求并得到对应的data响应值。
 */
@JvmSynthetic
public suspend inline fun <T> KookBot.requestData(api: KookApiRequest<T>): T {
    return api.requestDataBy(this)
}



/**
 * 通过指定Bot，利用其token和client发起请求并得到对应的[ApiResult]。
 *
 * 如果你希望自动检测 [ApiResult] 响应类型并直接得到 [ApiResult.data] 的反序列化结果，参考 [requestDataBlocking]
 *
 * @see requestDataBlocking
 *
 */
@Api4J
public fun KookBot.requestBlocking(api: KookApiRequest<*>): ApiResult {
    return runInNoScopeBlocking(coroutineContext) { api.requestForResultBy(this@requestBlocking) }
}

/**
 * 通过指定Bot，利用其token和client发起请求并得到对应的data响应值。
 */
@Api4J
public fun <T> KookBot.requestDataBlocking(api: KookApiRequest<T>): T {
    return runInNoScopeBlocking(coroutineContext) { api.requestDataBy(this@requestDataBlocking) }
}


