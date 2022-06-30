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

@file:JvmName("BotRequestUtil")

package love.forte.simbot.kook

import love.forte.simbot.Api4J
import love.forte.simbot.kook.api.ApiResult
import love.forte.simbot.kook.api.KookApiRequest
import love.forte.simbot.utils.runInBlocking


/**
 * 通过指定Bot，利用其token和client发起请求并得到对应的[ApiResult]。
 *
 * 如果你希望自动检测 [ApiResult] 响应类型并直接得到 [ApiResult.data] 的反序列化结果，参考 [requestData]、[requestDataBy].
 *
 * @see requestData
 * @see requestDataBy
 */
@JvmSynthetic
public suspend inline fun KookApiRequest<*>.requestBy(bot: KaiheilaBot): ApiResult {
    return request(
        bot.httpClient,
        bot.ticket.authorization,
        bot.configuration.decoder,
    )
}

/**
 * 通过指定Bot，利用其token和client发起请求并得到对应的data响应值。
 */
@JvmSynthetic
public suspend inline fun <T> KookApiRequest<T>.requestDataBy(bot: KaiheilaBot): T {
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
public suspend inline fun KaiheilaBot.request(api: KookApiRequest<*>): ApiResult {
    return api.requestBy(this)
}

/**
 * 通过指定Bot，利用其token和client发起请求并得到对应的data响应值。
 */
@JvmSynthetic
public suspend inline fun <T> KaiheilaBot.requestData(api: KookApiRequest<T>): T {
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
public fun KaiheilaBot.requestBlocking(api: KookApiRequest<*>): ApiResult {
    return runInBlocking { api.requestBy(this@requestBlocking) }
}

/**
 * 通过指定Bot，利用其token和client发起请求并得到对应的data响应值。
 */
@Api4J
public fun <T> KaiheilaBot.requestDataBlocking(api: KookApiRequest<T>): T {
    return runInBlocking { api.requestDataBy(this@requestDataBlocking) }
}


