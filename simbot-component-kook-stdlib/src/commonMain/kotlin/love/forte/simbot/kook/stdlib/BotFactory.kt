/*
 * Copyright (c) 2023-2024. ForteScarlet.
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

package love.forte.simbot.kook.stdlib

import love.forte.simbot.kook.stdlib.internal.BotImpl
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * 构建 [Bot] 的工厂。
 *
 * @see Bot
 */
public object BotFactory {

    /**
     * 构建一个尚未启动的 [Bot] 对象。
     *
     * @param ticket bot启动所需的票据信息
     */
    @JvmOverloads
    @JvmStatic
    public fun create(ticket: Ticket, configuration: BotConfiguration = BotConfiguration()): Bot =
        BotImpl(ticket, configuration)

}


/**
 * 构建一个尚未启动的 [Bot] 对象。
 *
 * @param ticket bot启动所需的票据信息
 */
public inline fun BotFactory.create(ticket: Ticket, config: BotConfiguration.() -> Unit): Bot {
    return create(ticket, BotConfiguration().also(config))
}
