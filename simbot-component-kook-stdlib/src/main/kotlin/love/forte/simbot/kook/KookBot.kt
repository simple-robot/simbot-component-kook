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

package love.forte.simbot.kook

import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import love.forte.simbot.Api4J
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.LoggerContainer
import love.forte.simbot.kook.api.KookApiRequestor
import love.forte.simbot.kook.api.user.Me
import love.forte.simbot.kook.api.user.MeRequest
import love.forte.simbot.kook.api.user.OfflineRequest
import love.forte.simbot.kook.event.*
import org.slf4j.Logger
import kotlin.coroutines.CoroutineContext

/**
 * Kook Bot对应的bot类型。
 *
 * [KookBot] 提供标准定义，但是不实现 simple-robot-api 中的 [love.forte.simbot.Bot] 接口。
 *
 * @author ForteScarlet
 */
public interface KookBot : CoroutineScope, LoggerContainer, KookApiRequestor {
    override val coroutineContext: CoroutineContext
    override val logger: Logger

    /**
     * 当前bot所使用的配置类。
     *
     */
    public val configuration: KookBotConfiguration


    /**
     * 当前bot的[票据][Ticket]信息。
     */
    public val ticket: Ticket


    /**
     * 当前bot所使用的 [HttpClient] 实例。
     *
     * 此实例代表的用于进行API请求（xxxRequest）的http client。
     */
    public val httpClient: HttpClient
    
    /**
     * @see httpClient
     */
    override val client: HttpClient
        get() = httpClient
    
    /**
     * @see ticket
     */
    override val authorization: String
        get() = ticket.authorization
    
    /**
     * 添加一个事件的前置处理器。
     *
     * 与 [processor] 不同的是，前置处理器会在每次事件被触发时，优先于 [processor] 中的事件并**顺序**执行。
     *
     */
    @JvmSynthetic
    public fun preProcessor(processor: suspend Signal.Event.(decoder: Json, decoded: () -> Event<*>) -> Unit)


    /**
     * 添加一个事件处理器。所有事件处理器会在每次触发的时候异步的按照添加顺序依次进行处理。
     */
    @JvmSynthetic
    public fun processor(processor: suspend Signal.Event.(decoder: Json, decoded: () -> Event<*>) -> Unit)

    /**
     * process for java
     */
    @Api4J
    public fun process(processor: (rawEvent: Signal.Event, decoder: Json, decoded: () -> Event<*>) -> Unit) {
        processor { decoder, decoded -> processor(this, decoder, decoded) }
    }

    /**
     * process for java
     */
    @Api4J
    public fun <EX : Event.Extra, E : Event<EX>> process(eventParser: EventParser<EX, E>, processor: (E) -> Unit) {
        processor { _, decoded ->

            if (eventParser.check(type, extraTypePrimitive)) {
                // val eventData: R = decoder.decodeFromJsonElement(eventType.decoder, data)
                @Suppress("UNCHECKED_CAST")
                processor(decoded() as E)
            }
        }
    }


    /**
     * 当前bot所使用的部分权限"票据"。
     *
     * [Ticket.equals] 默认情况下将会只基于 [clientId] 进行匹配。如果你想同时比较 [token], 使用 [exactlyEquals].
     *
     * @see SimpleTicket
     */
    public interface Ticket {

        /**
         * 得到当前bot的ID.（Client ID）
         *
         * [clientId] 作为bot在程序中的ID使用，例如在 simbot 整合中，会作为bot注册的唯一键。
         *
         */
        public val clientId: ID


        /**
         * 当前bot所使用的 `Token`. 从 [Bot](https://developer.kook.cn/bot/) 中 websocket链接模式中得到。
         *
         * 鉴于此token可以重新生成，当前ticket中的token也可以随时修改，并且立即生效。
         *
         * 如果需要进行API请求，请直接使用 [authorization].
         *
         */
        public var token: String


        /**
         * 当前bot使用的 [token] 拼接了 `Bot ` 前缀的结果，用于通过API进行请求，会随着 [token] 的变化儿变化。
         */
        public val authorization: String

        /**
         * 同时使用 [clientId] 和 [token] 进行比较。
         */
        public fun exactlyEquals(other: Any?): Boolean

    }


    /**
     * 启动此bot的连接。
     *
     * @throws kotlinx.coroutines.CancellationException 如果已经被关闭。
     * @return 如果正常运行中，则尝试重新启动；如果尚未开始，则启动。如果已经被关闭，抛出 [kotlinx.coroutines.CancellationException].
     */
    @JvmSynthetic
    public suspend fun start(): Boolean


    /**
     * 等待直到当前bot被取消。
     */
    public suspend fun join()


    /**
     * 终止此bot并关闭所有连接。
     */
    @JvmSynthetic
    public suspend fun cancel(reason: Throwable? = null)


    /**
     * 此bot是否启动过了。
     */
    public val isStarted: Boolean


    /**
     * Bot内部持有的连接信息。
     */
    public interface Client {
        /**
         * 此连接是否为压缩连接。
         */
        public val isCompress: Boolean

        /**
         * 此连接所属Bot。
         */
        public val bot: KookBot


        /**
         * 此连接的目标url。
         */
        public val url: String


        /**
         * 此连接内部所持有的 `sn` 。
         */
        public val sn: Long


        /**
         * 当前 client 是否处于运行状态。
         */
        public val isActive: Boolean


    }


    //// some self func


    /**
     * 查询bot当前信息。
     *
     * @see MeRequest
     */
    public suspend fun me(): Me

    /**
     * 让 bot 下线。
     *
     * @see OfflineRequest
     */
    public suspend fun offline()

}

/**
 * 用于Java的事件处理函数接口。
 */
@Api4J
public fun interface EventProcessor4J<out EX : Event.Extra, E : Event<EX>> {
    /**
     * 事件处理器。
     */
    @Api4J
    public fun process(event: E)

}


/**
 * [KookBot.Ticket] 的基础实现。
 *
 */
public class SimpleTicket(
    clientId: String,
    token: String,
) : KookBot.Ticket {
    override val clientId: CharSequenceID = clientId.ID
    
    // Bot xxx
    @Volatile private var _authToken = "Bot $token"

    override var token: String
        get() = _authToken.substring(4)
        set(value) {
            _authToken = "Bot $value"
        }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is SimpleTicket) return false

        return clientId == other.clientId
    }

    override fun exactlyEquals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is SimpleTicket) return false

        return clientId == other.clientId && _authToken == other._authToken
    }

    override fun hashCode(): Int {
        return clientId.hashCode()
    }

    override val authorization: String
        get() = _authToken

    override fun toString(): String {
        return "SimpleTicket(clientId=$clientId, token=${token.hide()})"
    }

}

private fun String.hide(size: Int = 3, hide: String = "********"): String {
    return if (length <= size) hide
    else "${substring(0, 3)}$hide${substring(lastIndex - 2, length)}"

}


