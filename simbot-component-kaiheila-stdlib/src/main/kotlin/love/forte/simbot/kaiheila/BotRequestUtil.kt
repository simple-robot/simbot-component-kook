/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kaiheila 的一部分。
 *
 *  simbot-component-kaiheila 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kaiheila 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

@file:JvmName("BotRequestUtil")

package love.forte.simbot.kaiheila

import love.forte.simbot.kaiheila.api.*


/**
 * 通过指定Bot，利用其token和client发起请求并得到对应的[ApiResult]。
 */
public suspend fun KaiheilaApiRequest<*>.requestBy(bot: KaiheilaBot): ApiResult {
    return request(
        bot.httpClient,
        bot.ticket.authorization,
        bot.configuration.decoder,
    )
}

/**
 * 通过指定Bot，利用其token和client发起请求并得到对应的data响应值。
 */
public suspend fun <T> KaiheilaApiRequest<T>.requestDataBy(bot: KaiheilaBot): T {
    return requestData(
        bot.httpClient,
        bot.ticket.authorization,
        bot.configuration.decoder,
    )
}

/**
 * 通过指定Bot，利用其token和client发起请求并得到对应的[ApiResult]。
 */
public suspend fun KaiheilaBot.request(api: KaiheilaApiRequest<*>): ApiResult {
    return api.requestBy(this)
}

/**
 * 通过指定Bot，利用其token和client发起请求并得到对应的data响应值。
 */
public suspend fun <T> KaiheilaBot.requestData(api: KaiheilaApiRequest<T>): T {
    return api.requestDataBy(this)
}


