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

import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope
import love.forte.simbot.kook.event.Event
import love.forte.simbot.kook.event.Signal
import kotlin.coroutines.CoroutineContext


/**
 * 一个 KOOK Bot。
 *
 * [Bot] 承载了通过 `WebSocket` 的方式与 KOOK 服务器连接并订阅事件的能力，
 * 以及通过各种 [KOOK HTTP API][love.forte.simbot.kook.api.KookApi] 进行功能交互的能力。
 *
 * @author ForteScarlet
 */
public interface Bot : CoroutineScope {
    override val coroutineContext: CoroutineContext

    /**
     * 此 bot 的票据信息。
     */
    public val ticket: Ticket

    /**
     * 当前 bot 所使用的配置信息。
     */
    public val configuration: BotConfiguration

    /**
     * 当前bot所使用的 [HttpClient] 实例。
     *
     * 此实例代表的用于进行API请求（xxxRequest）的http client。
     */
    public val apiClient: HttpClient

    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候按照添加顺序依次进行处理。
     */
    public fun processor(processor: EventProcessor)

    /**
     * 添加一个事件的前置处理器。
     *
     * 与 [processor] 不同的是，前置处理器会在每次事件被触发时，优先于 [processor] 中的事件并**顺序**执行。
     *
     */
    public fun preProcessor(processor: EventProcessor)

    // TODO Blocking and Async for Java

    /**
     * Bot 是否处于活跃状态。
     */
    public val isActive: Boolean
}

/**
 * 事件处理器。
 */
public typealias EventProcessor = suspend Signal.Event.(Event<*>) -> Unit

/**
 * Bot 所使用的部分权限"票据"。
 *
 * 这些信息可以在 [KOOK Bot 应用](https://developer.kookapp.cn/app/index)
 * 中选择某个具体的 Bot 进行查看、配置。
 *
 * @author ForteScarlet
 */
public interface Ticket {
    /**
     * Client Id.
     */
    public val clickId: String

    /**
     * Token.
     */
    public val token: String
}
