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

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.utils.io.charsets.*
import io.ktor.utils.io.core.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import love.forte.simbot.kook.KookBotClientCloseException
import love.forte.simbot.kook.KookBotConnectException
import love.forte.simbot.kook.api.Gateway
import love.forte.simbot.kook.api.GetGatewayApi
import love.forte.simbot.kook.event.Signal
import love.forte.simbot.logger.Logger
import kotlin.time.Duration.Companion.seconds


internal abstract class State : love.forte.simbot.util.stageloop.State<State>() {
    internal abstract val bot: BotImpl
    internal abstract val botLogger: Logger
}

internal data class ReconnectInfo(val sn: Long, val sessionId: String)

internal data class RetryInfo(val time: Int = 1, val exceptions: List<Throwable> = emptyList())

private fun RetryInfo?.incr(exception: Throwable? = null): RetryInfo {
    if (this == null) {
        return RetryInfo(time = 1, exceptions = exception?.let { listOf(it) } ?: emptyList())
    }

    return copy(time = time + 1, exceptions = buildList {
        addAll(exceptions)
        exception?.also(::add)
    })
}

/**
 * state: 获取网关信息并创建连接（前的准备工作）
 *
 * @param isCompress 是否压缩
 * @param retryInfo 重连信息，如果为null则是第一次连接。如果重联次数 time > 3 则会整合异常并抛出。
 */
internal class Connect(
    override val bot: BotImpl,
    override val botLogger: Logger,
    val isCompress: Boolean,
    /**
     * 重连信息，如果为null则是第一次连接
     */
    val retryInfo: RetryInfo? = null,
    /**
     * 重连信息，如果存在则为重连。
     */
    private val reconnect: ReconnectInfo? = null,
) : State() {

    /**
     * @throws KookBotConnectException 连接失败
     */
    override suspend fun invoke(): State {
        // 获取 gateway
        botLogger.debug("Requesting for gateway info")

        if (retryInfo != null) {
            botLogger.debug("Requesting for gateway info, Retry info: {}", retryInfo)
            if (retryInfo.time > 3) {
                // throw it.
                throw KookBotConnectException("Connect failed. Retry times: ${retryInfo.time}").also { e ->
                    retryInfo.exceptions.forEach(e::addSuppressed)
                }
            }
        }

        val gateway = GetGatewayApi.create(isCompress).requestBy(bot).info {
            if (reconnect != null) {
                botLogger.debug("Reconnect: sn: {}, sessionId: {}", reconnect.sn, reconnect.sessionId)
                parameters["session_id"] = reconnect.sessionId
                parameters["sn"] = reconnect.sn.toString()
                parameters["resume"] = "1"
            }
        }

        botLogger.debug("Gateway info: {}, next state: CreateWsSession", gateway)
        return CreateWsSession(bot, botLogger, RequestedGateway(this, gateway))
    }
}

private data class GatewayInfo0(val url: String, val urlBuilder: URLBuilder.() -> Unit = {})

private fun Gateway.info(urlBuilder: URLBuilder.() -> Unit = {}): GatewayInfo0 = GatewayInfo0(url, urlBuilder)

private class RequestedGateway(val connect: Connect, val gateway: GatewayInfo0)

/**
 * 根据 gateway 信息创建 ws 会话
 */
private class CreateWsSession(
    override val bot: BotImpl,
    override val botLogger: Logger,
    val gateway: RequestedGateway
) : State() {
    override suspend fun invoke(): State {
        botLogger.debug("Creating ws session... gateway: {}", gateway)

        val session = try {
            val session = connectWs()
            botLogger.debug("Ws session created: {}", session)
            session
        } catch (e: Throwable) {
            botLogger.error("Connect to gateway {} failed, msg: {}, retry.", gateway, e.message)
            botLogger.debug("Connect to gateway {} failed, msg: {}, retry.", gateway, e.message, e)

            return Connect(
                bot = bot,
                botLogger = botLogger,
                isCompress = gateway.connect.isCompress,
                retryInfo = gateway.connect.retryInfo.incr(e)
            )
        }

        return WaitingHello(bot, botLogger, gateway, session)
    }

    private suspend fun connectWs(): DefaultClientWebSocketSession {
        return bot.wsClient.ws(gateway.gateway)
    }

    /**
     * 通过 [Gateway] 连接bot信息。
     */
    private suspend fun HttpClient.ws(gateway: GatewayInfo0): DefaultClientWebSocketSession {
        return webSocketSession {
            url {
                takeFrom(gateway.url)
                gateway.apply {
                    urlBuilder()
                }
            }
        }
    }
}

/**
 * 等待 hello 消息
 */
private class WaitingHello(
    override val bot: BotImpl,
    override val botLogger: Logger,
    val gateway: RequestedGateway,
    val wsSession: DefaultClientWebSocketSession
) : State() {
    override suspend fun invoke(): State {
        val wsConnectTimeout = bot.configuration.wsConnectTimeout

        val hello = try {
            withTimeout(wsConnectTimeout) {
                wsSession.waitHello()
            }
        } catch (timeout: TimeoutCancellationException) {
            // TODO: timeout
            botLogger.error("Connect to gateway for waiting hello timeout in {}ms. retry.", wsConnectTimeout)
            wsSession.cancel("Connect to gateway for waiting hello timeout in ${wsConnectTimeout}ms.")
            // 回退到第一步
            return Connect(
                bot = bot,
                botLogger = botLogger,
                isCompress = gateway.connect.isCompress,
                retryInfo = gateway.connect.retryInfo.incr(timeout)
            )
        }

        botLogger.debug("Hello received: {}", hello)

        return CreateHeartbeatJob(bot, botLogger, gateway, wsSession, hello)
    }

    private suspend inline fun DefaultClientWebSocketSession.waitHello(): Signal.Hello {
        // receive Hello
        var hello: Signal.Hello? = null

        while (isActive) {
            bot
            val h = waitForHello(BotImpl.defaultApiDecoder, incoming.receive())
            if (h != null) {
                hello = h
                break
            }
        }

        if (hello == null) {
            val reason = closeReason.await() // .err()
            throw KookBotClientCloseException(reason, "Receive hello signal failed: $reason")
        }

        return hello
    }

    private fun waitForHello(decoder: Json, frame: Frame): Signal.Hello? {
        var hello: Signal.Hello? = null
        // for hello

        if (frame is Frame.Text || frame is Frame.Binary) {
            val json = decoder.parseToJsonElement(frame.readToText())
            if (json.jsonObject["s"]?.jsonPrimitive?.intOrNull == Signal.S_HELLO) {
                hello = decoder.decodeFromJsonElement(Signal.Hello.serializer(), json)
            }
        }

        return hello
    }
}

/**
 * 接收到 Hello 后，创建心跳任务
 */
private class CreateHeartbeatJob(
    override val bot: BotImpl,
    override val botLogger: Logger,
    val gateway: RequestedGateway,
    val session: DefaultClientWebSocketSession,
    val hello: Signal.Hello,
) : State() {
    override suspend fun invoke(): State? {
        val sn = AtomicLongRef()
        botLogger.debug("Creating heartbeat job for session: {}", session)

        val heartbeatJob = session.heartbeatJob(sn)

        botLogger.debug("Heartbeat job created: {}", heartbeatJob)


        TODO("Not yet implemented")
    }

    private suspend fun DefaultClientWebSocketSession.heartbeatJob(sn: AtomicLongRef): HeartbeatJob {
        fun helloInterval(): Long {
            val r = kotlin.random.Random.nextLong(5000)
            return if (kotlin.random.Random.nextBoolean()) 30_000 + r else 30_000 - r
        }

        val pongChannel = Channel<Signal.Pong>(1, BufferOverflow.DROP_OLDEST)

        // heartbeat Job
        val heartbeatLaunchJob = launch {
            suspend fun waitForPong(timeout: Long): Signal.Pong {
                return withTimeout(timeout) {
                    pongChannel.receive()
                }
            }

            val pongTimeout = 6.seconds.inWholeMilliseconds

            hb@ while (isActive) {
                val ping = Signal.Ping(sn.value.toInt())
                botLogger.trace("Send 'Ping' {}", ping)
                send(ping.jsonValue())

                // wait for pong
                try {
                    // 如果超时:
                    // 在连接中，每隔 30 秒发一次心跳 ping 包，如果 6 秒内，没有收到心跳 pong 包，则超时。进入到指数回退，重试。
                    botLogger.trace("Waiting for 'Pong'")
                    val pong = waitForPong(pongTimeout)
                    botLogger.debug("Received pong {} in 6s", pong)
                } catch (timeout: TimeoutCancellationException) {
                    // timeout for waiting pong!
                    botLogger.trace("pong receive timeout: {}", timeout.message, timeout)

                    // retry twice
                    for (i in 1..2) {
                        try {
                            val pong = waitForPong(pongTimeout)
                            botLogger.debug("Received pong {} in 6s (timeout retry {})", pong, i)
                            continue@hb
                        } catch (timeout0: TimeoutCancellationException) {
                            botLogger.debug(
                                "Received pong timeout (timeout retry {}): {}",
                                i,
                                timeout0.message,
                                timeout0
                            )
                            delay((i * 2).seconds)
                            continue
                        }
                    }

                    botLogger.error("pong receive timeout: {}, Cancel session", timeout.message, timeout)
                    // close exceptionally
                    // closeExceptionally()
                    close()
                    // cancel("pong receive timeout: ${timeout.localizedMessage}", timeout)
                    break
                }

                delay(helloInterval())
            }
        }

        heartbeatLaunchJob.invokeOnCompletion {
            pongChannel.close()
        }

        return HeartbeatJob(heartbeatLaunchJob, pongChannel)
    }
}

private class HeartbeatJob(val job: Job, val pongChannel: Channel<Signal.Pong>) {
    fun pong(pong: Signal.Pong) {
        pongChannel.trySend(pong)
    }

    override fun toString(): String {
        return "HeartbeatJob(job=$job)"
    }
}

/**
 * 创建事件处理链接
 */
private class CreateClient(
    override val bot: BotImpl,
    override val botLogger: Logger,
    private val hello: Signal.Hello,
    private val gateway: RequestedGateway,
    private val sn: AtomicLongRef,
    private val heartbeatJob: HeartbeatJob
) : State() {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun invoke(): State? {
        val eventProcessChannel = Channel<Signal.Event<*>>(capacity = Channel.BUFFERED)
        eventProcessChannel.invokeOnClose { cause ->
            botLogger.debug("Event process closed, cause: {}", cause?.message, cause)
        }
        botLogger.trace("Creating event process channel: {}", eventProcessChannel)


        TODO("Not yet implemented")
    }

    private fun DefaultClientWebSocketSession.eventProcessJob(channel: Channel<Signal.Event<*>>) {

        TODO("event process job")
    }
}


private fun Frame.readToText(): String {
    return when (this) {
        is Frame.Text -> readText()
        is Frame.Binary -> Charsets.UTF_8.newDecoder().decode(buildPacket { writeFully(data) })
        else -> throw IllegalArgumentException("Frame is not text or binary type.")
    }
}
