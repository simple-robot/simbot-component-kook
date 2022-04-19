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

package love.forte.simbot.kaiheila.internal

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import love.forte.simbot.SimbotIllegalArgumentException
import love.forte.simbot.SimbotIllegalStateException
import love.forte.simbot.kaiheila.KaiheilaBot
import love.forte.simbot.kaiheila.KaiheilaBotConfiguration
import love.forte.simbot.kaiheila.api.Gateway
import love.forte.simbot.kaiheila.api.GatewayRequest
import love.forte.simbot.kaiheila.api.KaiheilaApiException
import love.forte.simbot.kaiheila.api.err
import love.forte.simbot.kaiheila.api.user.Me
import love.forte.simbot.kaiheila.api.user.MeRequest
import love.forte.simbot.kaiheila.api.user.OfflineRequest
import love.forte.simbot.kaiheila.event.*
import love.forte.simbot.kaiheila.requestDataBy
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import java.util.zip.InflaterInputStream
import kotlin.coroutines.CoroutineContext
import kotlin.math.max

/**
 *
 * [KaiheilaBot] 基础实现。
 *
 * @author ForteScarlet
 */
internal class KaiheilaBotImpl(
    override val ticket: KaiheilaBot.Ticket,
    override val configuration: KaiheilaBotConfiguration
) : KaiheilaBot {
    override val logger: Logger = LoggerFactory.getLogger("love.forte.simbot.kaiheila.bot.${ticket.clientId}")
    private val clientLogger = LoggerFactory.getLogger("love.forte.simbot.kaiheila.bot.client.${ticket.clientId}")
    private val processorQueue: ConcurrentLinkedQueue<suspend Signal_0.(Json, () -> Event<*>) -> Unit> =
        ConcurrentLinkedQueue()

    private val preProcessorQueue: ConcurrentLinkedQueue<suspend Signal_0.(Json, () -> Event<*>) -> Unit> =
        ConcurrentLinkedQueue()


    private val decoder = configuration.decoder

    private val job: Job
    override val coroutineContext: CoroutineContext

    override val httpClient: HttpClient

    private val isCompress = configuration.isCompress
    private val gatewayRequest: GatewayRequest = if (isCompress) GatewayRequest.Compress else GatewayRequest.NotCompress


    init {
        val parentJob = configuration.coroutineContext[Job]
        this.job = SupervisorJob(parentJob)
        this.coroutineContext = configuration.coroutineContext + job + CoroutineName("KaiheilaBot.${ticket.clientId}")

        val engine = configuration.clientEngine
        val engineFactory = configuration.clientEngineFactory

        fun HttpClientConfig<*>.configClient() {
            install(ContentNegotiation) {
                json(decoder)
            }
            // install(JsonFeature) {
            //     serializer = KotlinxSerializer(decoder)
            // }
            // install ws
            install(WebSockets)


            // config it.
            configuration.httpClientConfig(this)
        }

        val client = when {
            engine != null -> HttpClient(engine) {
                configClient()
            }
            engineFactory != null -> HttpClient(engineFactory) {
                configClient()
            }
            else -> HttpClient() {
                configClient()
            }
        }

        httpClient = client


    }

    override fun preProcessor(processor: suspend Signal.Event.(decoder: Json, decoded: () -> Event<*>) -> Unit) {
        preProcessorQueue.add(processor)
    }

    override fun processor(processor: suspend Signal_0.(decoder: Json, decoded: () -> Event<*>) -> Unit) {
        processorQueue.add(processor)
    }

    private val lifeLock = Mutex()

    @Volatile
    private var client: ClientImpl? = null

    private val _isStarted: AtomicBoolean = AtomicBoolean(false)

    override val isStarted: Boolean
        get() = _isStarted.get()

    override suspend fun start(): Boolean = start { null }


    private suspend inline fun start(reason: () -> Throwable? = { null }): Boolean = lifeLock.withLock {
        if (job.isCancelled) {
            throw kotlinx.coroutines.CancellationException("Bot has been cancelled.")
        }

        val c = client
        if (c != null) {
            clientLogger.debug("Closing the current client: {}", c)
            c.cancel(reason())
            clientLogger.debug("Current client closed.")
            client = null
        }

        clientLogger.debug("Requesting for gateway info...")
        val gateway = gatewayRequest.requestDataBy(this)

        clientLogger.debug("Creating client by gateway {}", gateway)
        client = createClient(gateway, DEFAULT_CONNECT_TIMEOUT)
        clientLogger.debug("Client created. client: {}", client)

        _isStarted.compareAndSet(false, true)

        true
    }


    /**
     * 创建并启动一个连接。
     */
    private suspend fun createClient(gateway: Gateway, connectTimeout: Long): ClientImpl {
        clientLogger.debug("Creating session...")
        val sessionInfo: SessionInfo = createSession(gateway, connectTimeout)
        clientLogger.debug("Session created: {}", sessionInfo)
        val (session, sn, _, sessionData) = sessionInfo

        // pre process
        val preProcessor = configuration.preEventProcessor
        preProcessor(this, sessionData.data.sessionId)

        processEvent(sessionInfo)

        return ClientImpl(gateway.url, sessionData, sn, session)
    }


    /**
     * 创建一个连接。
     */
    private suspend fun createSession(gateway: Gateway, connectTimeout: Long): SessionInfo {
        val sn = AtomicLong(0)

        val session = httpClient.ws(gateway)

        kotlin.runCatching {
            val timeoutJob = launch {
                delay(connectTimeout)
                val message = "Hello receive timeout: $connectTimeout ms"
                session.cancel(message, TimeoutException(message))
            }

            val hello: Signal.Hello = session.waitHello().check()
            timeoutJob.cancel()

            clientLogger.debug("Received Hello: {}", hello)
            // receive events

            val heartbeatJob = session.heartbeatJob(sn)


            return SessionInfo(session, sn, heartbeatJob, hello)
        }.getOrElse {
            session.closeReason.await().err(it)
        }

    }


    private suspend fun processEvent(sessionInfo: SessionInfo): Job {
        val eventSerializer = Signal.Event.serializer()
        val sn = sessionInfo.sn

        fun processEventString(eventString: String): Signal.Event? {
            val jsonElement = decoder.parseToJsonElement(eventString)
            // maybe op 11: heartbeat ack
            val s = jsonElement.jsonObject["s"]?.jsonPrimitive?.intOrNull ?: return null
            return when (s) {
                // Pong.
                Signal.Pong.S_CODE -> {
                    // TODO 6s timeout?
                    this.clientLogger.debug("Pong signal received.")

                    null
                }
                Signal.Reconnect.S_CODE -> {
                    this.clientLogger.debug("Reconnect signal received: {}", eventString)
                    val reconnect = decoder.decodeFromJsonElement(Signal.Reconnect.serializer(), jsonElement)
                    val reconnectData = reconnect.data
                    this.launch {
                        start { ReconnectException(reconnectData.code, reconnectData.err.toString()) }
                    }
                    null
                }
                // Event
                Signal.Event.S_CODE -> {
                    // is dispatch
                    decoder.decodeFromJsonElement(eventSerializer, jsonElement)
                }
                else -> null
            }
        }

        val processJob = SupervisorJob(sessionInfo.session.coroutineContext[Job])

        return sessionInfo.session.incoming.receiveAsFlow().mapNotNull {
            when (it) {
                is Frame.Text -> {
                    val eventString = it.readToText()
                    processEventString(eventString)

                }
                is Frame.Binary -> {
                    val eventString = it.readToText()
                    processEventString(eventString)
                }
                else -> {
                    clientLogger.trace("Other frame: {}", it)
                    null
                }
            }
        }.onEach { event ->
            val nowSn = event.sn
            val currPreProcessorQueue = preProcessorQueue
            val currProcessorQueue = processorQueue
            if (currPreProcessorQueue.isNotEmpty() || currProcessorQueue.isNotEmpty()) {
                clientLogger.debug("On event: $event")
                val eventType = event.type
                val eventExtraType = event.extraType

                val parser = EventSignals[eventType, eventExtraType] ?: run {
                    val e = SimbotIllegalStateException("Unknown event type: $eventType. data: $event")
                    this.clientLogger.error(e.localizedMessage, e)
                    // e.process(logger) { "Event receiving" } // TODO process exception?
                    return@onEach
                }

                val lazy = lazy(LazyThreadSafetyMode.PUBLICATION) {
                    parser.deserialize(decoder, event.d)
                }


                val lazyDecoded = lazy::value

                // pre process
                currPreProcessorQueue.forEach { pre ->
                    try {
                        pre(event, decoder, lazyDecoded)
                    } catch (e: Throwable) {
                        clientLogger.error("Event pre precess failed.", e)
                    }
                }

                if (currProcessorQueue.isNotEmpty()) {
                    launch {
                        currProcessorQueue.forEach { p ->
                            try {
                                p(event, decoder, lazyDecoded)
                            } catch (e: Throwable) {
                                clientLogger.error("Event process failed.", e)
                            }
                        }
                    }
                }
            }

            // 留下最大的值。
            sn.updateAndGet { prev -> max(prev, nowSn) }
        }
            .onCompletion { cause ->
                clientLogger.debug(
                    "Session flow completion. cause: ${cause?.localizedMessage}",
                    cause
                )
            }.catch { cause ->
                clientLogger.error(
                    "Session flow on catch: ${cause.localizedMessage}",
                    cause
                )
            }
            .launchIn(this + processJob)
    }


    /**
     * 通过 [Gateway] 连接bot信息。
     */
    private suspend fun HttpClient.ws(gateway: Gateway): DefaultClientWebSocketSession {
        return webSocketSession {
            url(gateway.url)
        }
    }


    private suspend inline fun DefaultClientWebSocketSession.waitHello(): Signal.Hello {
        // receive Hello
        var hello: Signal.Hello? = null
        while (isActive) {
            val h = waitForHello(decoder, incoming.receive())
            if (h != null) {
                hello = h
                break
            }
        }
        if (hello == null) {
            closeReason.await().err()
        }
        return hello
    }


    private suspend inline fun DefaultClientWebSocketSession.heartbeatJob(sn: AtomicLong): Job {
        val heartbeatInterval = 30
        val helloIntervalFactory: () -> Long = {
            val r = kotlin.random.Random.nextLong(5000)
            if (kotlin.random.Random.nextBoolean()) heartbeatInterval + r else heartbeatInterval - r
        }

        // heartbeat Job
        val heartbeatJob = launch {
            val serializer = Signal.Ping.serializer()
            while (isActive) {
                delay(helloIntervalFactory())
                val hb = Signal.Ping(sn.get())
                send(decoder.encodeToString(serializer, hb))
            }
        }

        return heartbeatJob
    }

    override suspend fun join() {
        job.join()
    }

    override suspend fun cancel(reason: Throwable?) = lifeLock.withLock {
        if (job.isCancelled) {
            return@withLock
        }

        if (reason == null) {
            job.cancel()
        } else {
            job.cancel(reason.localizedMessage, reason)
        }
        job.join()
    }

    private inner class ClientImpl(
        //private val job: Job,
        override val url: String,
        private val sessionData: Signal.Hello,
        private val _sn: AtomicLong,
        private var session: DefaultClientWebSocketSession,
    ) : KaiheilaBot.Client {
        override val sn: Long get() = _sn.get()

        override val isActive: Boolean get() = session.isActive
        // override val isResuming: Boolean get() = _resuming.get()

        override val bot: KaiheilaBot
            get() = this@KaiheilaBotImpl

        override val isCompress: Boolean
            get() = this@KaiheilaBotImpl.isCompress


        suspend fun cancel(reason: Throwable? = null) = lifeLock.withLock {
            val cancel = reason?.let { CancellationException(it.localizedMessage, it) }

            val sessionJob = session.coroutineContext[Job]!!
            sessionJob.cancel(cancel)
            sessionJob.join()
        }

        override fun toString(): String {
            return "Client(url=$url, sn=$sn, sessionId=${sessionData.d.sessionId})"
        }

    }


    override suspend fun me(): Me {
        return MeRequest.requestDataBy(this)
    }

    override suspend fun offline() {
        return OfflineRequest.requestDataBy(this)
    }


    companion object {
        private const val DEFAULT_CONNECT_TIMEOUT: Long = 6000L
    }

}


private data class SessionInfo(
    val session: DefaultClientWebSocketSession,
    val sn: AtomicLong,
    val heartbeatJob: Job,
    val sessionData: Signal.Hello
)

/**
 * 将 [Frame.Text] 读取为文字字符串。
 */
private fun Frame.Text.readToText(): String {
    return readText()
}

/**
 * 将 [Frame.Binary] 读取为文字字符串。
 */
private fun Frame.Binary.readToText(): String {
    return InflaterInputStream(readBytes().inputStream()).bufferedReader().use { it.readText() }
}

private fun Frame.readToText(): String {
    return when (this) {
        is Frame.Text -> readToText()
        is Frame.Binary -> readToText()
        else -> throw SimbotIllegalArgumentException("Frame is not text or binary type.")
    }
}


private fun waitForHello(decoder: Json, frame: Frame): Signal.Hello? {
    var hello: Signal.Hello? = null
    // for hello

    if (frame is Frame.Text || frame is Frame.Binary) {
        val json = decoder.parseToJsonElement(frame.readToText())
        if (json.jsonObject["s"]?.jsonPrimitive?.intOrNull == Signal.Hello.S_CODE) {
            hello = decoder.decodeFromJsonElement(Signal.Hello.serializer(), json)
        }
    }

    return hello
}


/**
 * 检测 [Signal.Hello] 是否成功。
 */
private fun Signal.Hello.check(): Signal.Hello {
    if (d.code != Signal.Hello.SUCCESS_CODE) {
        val info = Signal.Hello.getErrorInfo(d.code)
        throw KaiheilaApiException(d.code.toInt(), info)
    }

    return this
}


internal class ReconnectException(code: Int, message: String, cause: Throwable? = null) :
    KaiheilaApiException(code, message, cause)