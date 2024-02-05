/*
 * Copyright (c) 2022-2023. ForteScarlet.
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

@file:JvmName("BotRequests")
@file:JvmMultifileClass

package love.forte.simbot.kook.stdlib

import io.ktor.client.statement.*
import love.forte.simbot.kook.api.*
import love.forte.simbot.kook.stdlib.internal.BotImpl
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

/**
 * 使用 [Bot] 向 [KookApi] 发起请求。
 */
@JvmSynthetic
public suspend fun KookApi<*>.requestBy(bot: Bot): HttpResponse {
    val authorization = (bot as? BotImpl)?.authorization ?: bot.calculateAuthorization()
    return request(bot.apiClient, authorization)
}

/**
 * 使用 [Bot] 向 [KookApi] 发起请求。
 */
@JvmSynthetic
public suspend fun KookApi<*>.requestTextBy(bot: Bot): String {
    val authorization = (bot as? BotImpl)?.authorization ?: bot.calculateAuthorization()
    return requestText(bot.apiClient, authorization)
}

/**
 * 使用 [Bot] 向 [KookApi] 发起请求。
 */
@JvmSynthetic
public suspend fun KookApi<*>.requestResultBy(bot: Bot): ApiResult {
    val authorization = (bot as? BotImpl)?.authorization ?: bot.calculateAuthorization()
    return requestResult(bot.apiClient, authorization)
}

/**
 * 使用 [Bot] 向 [KookApi] 发起请求。
 */
@JvmSynthetic
public suspend fun <T : Any> KookApi<T>.requestDataBy(bot: Bot): T {
    val authorization = (bot as? BotImpl)?.authorization ?: bot.calculateAuthorization()
    return requestData(bot.apiClient, authorization)
}

private fun Bot.calculateAuthorization(): String = with(ticket) { "${type.prefix} $token" }
