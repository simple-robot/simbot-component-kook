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
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.*
import kotlinx.serialization.json.*
import love.forte.simbot.*
import love.forte.simbot.LoggerFactory
import love.forte.simbot.kaiheila.*
import love.forte.simbot.kaiheila.api.*
import love.forte.simbot.kaiheila.api.user.*
import love.forte.simbot.kaiheila.event.*
import org.slf4j.*
import java.util.concurrent.*
import java.util.concurrent.atomic.*
import java.util.zip.*
import kotlin.coroutines.*
import kotlin.math.*

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
    private val logger = LoggerFactory.getLogger("love.forte.simbot.kaiheila.bot.${ticket.clientId}")
    private val clientLogger = LoggerFactory.getLogger("love.forte.simbot.kaiheila.bot.client.${ticket.clientId}")
    private val processorQueue: ConcurrentLinkedQueue<suspend Signal_0.(Json, () -> Any) -> Unit> =
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
            install(JsonFeature) {
                serializer = KotlinxSerializer(decoder)
            }
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

    override fun processor(processor: suspend Signal_0.(decoder: Json, decoded: () -> Any) -> Unit) {
        processorQueue.add(processor)
    }

    private val lifeLock = Mutex()

    @Volatile
    private var client: ClientImpl? = null

    override suspend fun start(): Boolean = lifeLock.withLock {
        if (job.isCancelled) {
            throw kotlinx.coroutines.CancellationException("Job has been cancelled")
        }


        val gateway = gatewayRequest.requestDataBy(this)

        val clientJob = SupervisorJob(job)

        client = createClient(clientJob, gateway)

        true
    }


    /**
     * 创建并启动一个连接。
     */
    private suspend fun createClient(clientJob: Job, gateway: Gateway): ClientImpl {
        val sessionInfo = createSession(clientJob, gateway)
        val (session, sn, heartbeatJob, sessionData) = sessionInfo

        val processingJob = processEvent(clientJob, sessionInfo)

        return ClientImpl(clientJob, gateway.url, sessionData, heartbeatJob, processingJob, sn, session)
    }


    /**
     * 创建一个连接。
     */
    private suspend fun createSession(clientJob: Job, gateway: Gateway): SessionInfo {
        val sn = AtomicLong(0)

        val session = httpClient.ws(gateway)

        return withContext(clientJob) {
            kotlin.runCatching {
                val hello: Signal.Hello = session.waitHello().check()

                clientLogger.debug("Received Hello: {}", hello)
                // receive events

                val heartbeatJob = session.heartbeatJob(sn)


                return@withContext SessionInfo(session, sn, heartbeatJob, hello)

            }.getOrElse {
                session.closeReason.await().err(it)
            }
        }

    }


    private suspend fun resumeSession(
        gateway: Gateway,
        sessionData: Signal.HelloPack,
        seq: AtomicLong,
    ): SessionInfo {
        val requestToken = ticket.botToken

        val resume = Signal.Resume(
            Signal.Resume.Data(
                token = requestToken,
                sessionId = sessionData.sessionId,
                seq = seq.get()
            )
        )

        val session = httpClient.ws(gateway)

        val hello = session.waitHello()

        clientLogger.debug("Received Hello: {}", hello)

        // 重连
        // see https://bot.q.qq.com/wiki/develop/api/gateway/reference.html#%E9%89%B4%E6%9D%83ß
        session.send(decoder.encodeToString(Signal.Resume.serializer(), resume))

        val heartbeatJob = session.heartbeatJob(seq)

        return SessionInfo(session, seq, heartbeatJob, logger, sessionData)
    }


    private suspend fun processEvent(clientJob: Job, sessionInfo: SessionInfo): Job {
        val eventSerializer = Signal.Event.serializer()
        val sn = sessionInfo.sn

        fun processEventString(eventString: String): Signal.Event? {
            val jsonElement = decoder.parseToJsonElement(eventString)
            // maybe op 11: heartbeat ack
            val s = jsonElement.jsonObject["s"]?.jsonPrimitive?.intOrNull ?: return null
            return when (s) {
                // Pong.
                Signal.Pong.S_CODE -> {
                    // TODO 6s timeout
                    this.clientLogger.debug("Pong signal received.")

                    null
                }
                Signal.Reconnect.S_CODE -> {
                    // TODO reconnect
                    this.clientLogger.error("Reconnect signal received.")

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
            if (processorQueue.isNotEmpty()) {
                clientLogger.debug("On event: $event")
                val eventType = event.type
                val eventextraType = event.extraType

                val parser = EventSignals[eventType, eventextraType] ?: run {
                    val e = SimbotIllegalStateException("Unknown event type: $eventType. data: $event")
                    this.clientLogger.error(e.localizedMessage, e)
                    // e.process(logger) { "Event receiving" } // TODO process exception
                    return@onEach
                }

                val lazy = lazy(LazyThreadSafetyMode.PUBLICATION) {
                    parser.deserialize(decoder, event.d)
                }


                val lazyDecoded = lazy::value
                launch {
                    processorQueue.forEach { p ->
                        try {
                            p(event, decoder, lazyDecoded)
                        } catch (e: Throwable) {
                            clientLogger.error("Event process failed.", e)
                        }
                    }
                }
            }

            // 留下最大的值。
            sn.updateAndGet { prev -> max(prev, nowSn) }
        }
            .flowOn(clientJob) // on client job
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
            .launchIn(this)
    }


    /**
     * 通过 [Gateway] 连接bot信息。
     */
    private suspend fun HttpClient.ws(gateway: Gateway): DefaultClientWebSocketSession {
        return webSocketSession {
            url(gateway.url)
            // headers {
            //     this[HttpHeaders.Authorization] = ticket.authorization
            // }
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
        private val job: Job,
        override val url: String,
        private val sessionData: Signal.Hello,
        private var heartbeatJob: Job,
        private var processingJob: Job,
        private val _sn: AtomicLong,
        private var session: DefaultClientWebSocketSession,
    ) : KaiheilaBot.Client {
        override val sn: Long get() = _sn.get()
        private val _resuming = AtomicBoolean(false)

        override val isActive: Boolean get() = session.isActive
        override val isResuming: Boolean get() = _resuming.get()

        override val bot: KaiheilaBot
            get() = this@KaiheilaBotImpl

        override val isCompress: Boolean
            get() = this@KaiheilaBotImpl.isCompress

        private var resumeJob: Job = launch {
            val closed = session.closeReason.await()
            resume(closed)
        }

        suspend fun cancel(reason: Throwable? = null) {
            val cancel = reason?.let { CancellationException(it.localizedMessage, it) }

            job.cancel(cancel)
            val sessionJob = session.coroutineContext[Job]
            sessionJob?.cancel(cancel)
            resumeJob.cancel(cancel)
            heartbeatJob.cancel(cancel)

            sessionJob?.join()
            heartbeatJob.join()
            processingJob.join()
        }


        /**
         * 重新连接。
         * // TODO 增加日志
         */
        private suspend fun resume(closeReason: CloseReason?) {
            if (closeReason == null) {
                clientLogger.warn(
                    "Client {} was closed, but no reason. stop this client without any action, including resuming.",
                    this
                )
                return
            }

            // val code = closeReason.code
            // TODO reason check
            // if (!checkResumeCode(code)) {
            //     logger.debug("Not resume code: {}, close and restart.", code)
            //     launch { start() }
            //     return
            // }

            while (!_resuming.compareAndSet(false, true)) {
                logger.info("In resuming now, delay 100ms")
                delay(100)
            }

            logger.info("Resume. reason: $closeReason")

            try {
                heartbeatJob.cancel()
                processingJob.cancel()
                heartbeatJob.join()
                processingJob.join()

                val gatewayInfo = gatewayRequest.resume(sn, sessionData.d.sessionId).requestDataBy(this@KaiheilaBotImpl)

                // val gatewayInfo = GatewayApis.Normal.request(
                //     this@KaiheilaBotImpl.httpClient,
                //     server = this@KaiheilaBotImpl.url,
                //     token = this@KaiheilaBotImpl.ticket.botToken,
                //     decoder = this@KaiheilaBotImpl.decoder,
                // )

                val resumeSession = resumeSession(gatewayInfo, sessionData, _sn)
                val (session, _, heartbeatJob, _, _) = resumeSession
                this.session = session
                this.heartbeatJob = heartbeatJob
                val processingJob = processEvent(resumeSession)
                this.processingJob = processingJob
                this.resumeJob = launch {
                    val closed = session.closeReason.await()
                    resume(closed)
                }
            } finally {
                _resuming.compareAndSet(true, false)
            }

        }


    }


    override suspend fun me(): Me {
        return MeRequest.requestDataBy(this)
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