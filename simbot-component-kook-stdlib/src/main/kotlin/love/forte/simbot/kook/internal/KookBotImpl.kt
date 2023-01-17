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

package love.forte.simbot.kook.internal

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ChannelResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import love.forte.simbot.SimbotIllegalArgumentException
import love.forte.simbot.SimbotIllegalStateException
import love.forte.simbot.kook.KookBot
import love.forte.simbot.kook.KookBotConfiguration
import love.forte.simbot.kook.api.Gateway
import love.forte.simbot.kook.api.GatewayRequest
import love.forte.simbot.kook.api.KookApiException
import love.forte.simbot.kook.api.err
import love.forte.simbot.kook.api.user.Me
import love.forte.simbot.kook.api.user.MeRequest
import love.forte.simbot.kook.api.user.OfflineRequest
import love.forte.simbot.kook.event.*
import love.forte.simbot.kook.requestDataBy
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicLong
import java.util.zip.InflaterInputStream
import kotlin.coroutines.CoroutineContext
import kotlin.math.max
import kotlin.time.Duration.Companion.seconds

/**
 *
 * [KookBot] 基础实现。
 *
 * @author ForteScarlet
 */
internal class KookBotImpl(
    override val ticket: KookBot.Ticket,
    override val configuration: KookBotConfiguration,
) : KookBot {
    override val logger: Logger = LoggerFactory.getLogger("love.forte.simbot.kook.bot.${ticket.clientId}")
    private val clientLogger = LoggerFactory.getLogger("love.forte.simbot.kook.bot.client.${ticket.clientId}")
    
    internal val isEventProcessAsync = configuration.isEventProcessAsync
    private val wsConnectTimeout = configuration.wsConnectTimeout
    
    /**
     * 事件预处理器集。
     */
    private val preProcessorQueue: ConcurrentLinkedQueue<suspend Signal_0.(Json, () -> Event<*>) -> Unit> =
        ConcurrentLinkedQueue()
    
    /**
     * 事件处理器集。
     */
    private val processorQueue: ConcurrentLinkedQueue<suspend Signal_0.(Json, () -> Event<*>) -> Unit> =
        ConcurrentLinkedQueue()
    
    private val decoder = configuration.decoder
    
    private val job: Job
    override val coroutineContext: CoroutineContext
    
    /**
     * api请求的client
     */
    override val httpClient: HttpClient
    
    /**
     * ws连接用的client
     */
    private val wsClient: HttpClient
    
    private val isCompress = configuration.isCompress
    private val gatewayRequest: GatewayRequest = if (isCompress) GatewayRequest.Compress else GatewayRequest.NotCompress
    
    
    init {
        val parentJob = configuration.coroutineContext[Job]
        this.job = SupervisorJob(parentJob)
        this.coroutineContext = configuration.coroutineContext + job + CoroutineName("KookBot.${ticket.clientId}")
        
        val engine = configuration.clientEngine
        val engineFactory = configuration.clientEngineFactory
        
        fun HttpClientConfig<*>.configClient() {
            // json decoder
            install(ContentNegotiation) {
                json(decoder)
            }
            
            // http request timeout
            install(HttpTimeout) {
                this.connectTimeoutMillis = configuration.connectTimeoutMillis
                this.requestTimeoutMillis = configuration.requestTimeoutMillis
            }
            
            // config it.
            configuration.httpClientConfig(this)
        }
        
        fun HttpClientConfig<*>.configWs() {
            WebSockets {
                pingInterval = 30_000L
            }
        }
        
        when {
            engine != null -> {
                httpClient = HttpClient(engine) {
                    configClient()
                }
                wsClient = HttpClient(engine) {
                    configWs()
                }
            }
            
            engineFactory != null -> {
                httpClient = HttpClient(engineFactory) {
                    configClient()
                }
                wsClient = HttpClient(engineFactory) {
                    configWs()
                }
            }
            
            else -> {
                httpClient = HttpClient {
                    configClient()
                }
                wsClient = HttpClient {
                    configWs()
                }
            }
        }
        
        
    }
    
    override fun preProcessor(processor: suspend Signal.Event.(decoder: Json, decoded: () -> Event<*>) -> Unit) {
        preProcessorQueue.add(processor)
    }
    
    override fun processor(processor: suspend Signal_0.(decoder: Json, decoded: () -> Event<*>) -> Unit) {
        processorQueue.add(processor)
    }
    
    @Volatile
    private var _client: ClientImpl? = null
    
    @Volatile
    private var stageLoop: StageLoop? = null
    
    @Volatile
    override var isStarted: Boolean = false
    
    private val clientLock = Mutex()
    
    override suspend fun start(): Boolean = clientLock.withLock {
        if (job.isCancelled) {
            throw kotlinx.coroutines.CancellationException("Bot has been cancelled.")
        }
        
        clientLogger.debug("Closing the current client: {}", _client)
        stageLoop?.job?.cancel()
        _client?.session?.cancel()
        stageLoop = null
        _client = null
        
        val loopJob = SupervisorJob(job)
        
        val loop = StageLoop(loopJob)
        logger.debug("Create stage loop {}", loop)
        
        loop.addLast(RequestGateway())
        
        var next = loop.nextStage()
        while (next != null && next !is Receive) {
            // 还没到receive阶段
            loop.invoke(next)
            next = loop.nextStage()
        }
        
        logger.debug("Loop on stage 'Receive'")
        
        launch(loopJob) {
            loop.invoke(next)
            loop.run()
        }
        
        isStarted = true
        true
    }
    
    
    /**
     * 通过 [Gateway] 连接bot信息。
     */
    private suspend fun HttpClient.ws(gateway: GatewayInfo): DefaultClientWebSocketSession {
        return webSocketSession {
            url {
                takeFrom(gateway.url)
                gateway.apply {
                    urlBuilder()
                }
            }
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
    
    private suspend fun DefaultClientWebSocketSession.heartbeatJob(sn: AtomicLong): HeartbeatJob {
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
                val ping = Signal.Ping(sn.get())
                clientLogger.trace("Send 'Ping' {}", ping)
                send(ping.jsonValue())
                
                // wait for pong
                try {
                    // 如果超时:
                    // 在连接中，每隔 30 秒发一次心跳 ping 包，如果 6 秒内，没有收到心跳 pong 包，则超时。进入到指数回退，重试。
                    clientLogger.trace("Waiting for 'Pong'")
                    val pong = waitForPong(pongTimeout)
                    clientLogger.debug("Received pong {} in 6s", pong)
                } catch (timeout: TimeoutCancellationException) {
                    // timeout for waiting pong!
                    clientLogger.trace("pong receive timeout: {}", timeout.localizedMessage, timeout)
                    
                    // retry twice
                    for (i in 1..2) {
                        try {
                            val pong = waitForPong(pongTimeout)
                            clientLogger.debug("Received pong {} in 6s (timeout retry {})", pong, i)
                            continue@hb
                        } catch (timeout0: TimeoutCancellationException) {
                            clientLogger.debug(
                                "Received pong timeout (timeout retry {}): {}",
                                i,
                                timeout0.localizedMessage,
                                timeout0
                            )
                            delay((i * 2).seconds)
                            continue
                        }
                    }
                    
                    clientLogger.error("pong receive timeout: {}, Cancel session", timeout.localizedMessage, timeout)
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
    
    private class HeartbeatJob(val job: Job, val pongChannel: Channel<Signal.Pong>) {
        fun pong(pong: Signal.Pong) {
            pongChannel.trySend(pong)
        }
        
        override fun toString(): String {
            return "HeartbeatJob(job=$job)"
        }
    }
    
    override suspend fun join() {
        job.join()
    }
    
    override suspend fun cancel(reason: Throwable?) {
        if (job.isCancelled) {
            return
        }
        
        if (reason == null) {
            job.cancel()
        } else {
            job.cancel(reason.localizedMessage, reason)
        }
        
        job.join()
    }
    
    override suspend fun me(): Me {
        return MeRequest.requestDataBy(this)
    }
    
    override suspend fun offline() {
        return OfflineRequest.requestDataBy(this)
    }
    
    private class GatewayInfo(val url: String, val urlBuilder: URLBuilder.() -> Unit = {})
    
    private fun Gateway.info(urlBuilder: URLBuilder.() -> Unit = {}): GatewayInfo = GatewayInfo(url, urlBuilder)
    
    data class ReconnectInfo(val sn: Long, val sessionId: String)
    
    private open inner class Stage {
        open suspend operator fun invoke(loop: StageLoop) {
        }
    }
    
    
    /**
     * 请求并获取gateway信息。
     * @property time 当前获取gateway的次数。大于1时代表本次为某次的重试。
     */
    private inner class RequestGateway(
        val time: Int = 1,
        val reconnectInfo: ReconnectInfo? = null,
    ) : Stage() {
        override suspend fun invoke(loop: StageLoop) {
            clientLogger.debug("Requesting for gateway info... time: {}", time)
            // retry on failure?
            val gateway = gatewayRequest.requestDataBy(this@KookBotImpl)
            
            // next: create session
            if (reconnectInfo != null) {
                loop.addLast(CreateWsSession(RequestedGateway(this, gateway.info {
                    parameters["session_id"] = reconnectInfo.sessionId
                    parameters["sn"] = reconnectInfo.sn.toString()
                    parameters["resume"] = "1"
                })))
            } else {
                loop.addLast(CreateWsSession(RequestedGateway(this, gateway.info())))
            }
        }
    }
    
    private inner class RequestedGateway(val stage: RequestGateway, val gateway: GatewayInfo)
    
    /**
     * ws 连接到 gateway
     *
     * > 2: 连接 Gateway。如果连接失败，回退到第 1 步。
     *
     */
    private inner class CreateWsSession(val gateway: RequestedGateway) : Stage() {
        override suspend fun invoke(loop: StageLoop) {
            clientLogger.debug("Creating websocket session...")
            try {
                val session = connectWs()
                
                // next: waiting hello?
                // next: create client
                loop.addLast(WaitingHello(gateway, session))
            } catch (e: Throwable) {
                if (clientLogger.isDebugEnabled) {
                    clientLogger.error("Connect to gateway [{}] failed, retry.", gateway, e)
                } else {
                    clientLogger.error("Connect to gateway failed, retry.", e)
                }
                // retry
                loop.addLast(
                    RequestGateway(
                        time = gateway.stage.time + 1,
                        reconnectInfo = gateway.stage.reconnectInfo
                    )
                )
            }
        }
        
        private suspend fun connectWs(): DefaultClientWebSocketSession {
            return wsClient.ws(gateway.gateway)
        }
    }
    
    /**
     *
     * [CreateWsSession] 之后，等待 `Hello` 包。期间如果出现其他什么包，抛弃。
     *
     * > 3: 收到 hello 包，如果成功，开始接收事件。如果失败，回退至第 1 步。
     */
    private inner class WaitingHello(val gatewayStage: RequestedGateway, val wsSession: DefaultClientWebSocketSession) :
        Stage() {
        override suspend fun invoke(loop: StageLoop) {
            clientLogger.debug("Waiting for 'Hello'")
            val hello: Signal.Hello = try {
                withTimeout(wsConnectTimeout) {
                    wsSession.waitHello()
                }.apply {
                    // KookApiException()
                    // just throw..?
                    check()
                }
            } catch (timeout: TimeoutCancellationException) {
                clientLogger.error("'Hello' receive timeout: {}. reconnect", timeout.localizedMessage, timeout)
                wsSession.cancel("'Hello' receive timeout, reconnect.", timeout)
                // next: back to gateway
                // 回退到第 1 步
                loop.addLast(
                    RequestGateway(
                        time = gatewayStage.stage.time + 1,
                        reconnectInfo = gatewayStage.stage.reconnectInfo
                    )
                )
                return
            }
            clientLogger.debug("Received 'Hello': {}, ready to process event.", hello)
            
            // create heart beat job
            // event process (create client)
            loop.addLast(CreateHeartbeatJob(gatewayStage, wsSession, hello, AtomicLong(0)))
        }
    }
    
    
    /**
     * 创建心跳收发器
     */
    private inner class CreateHeartbeatJob(
        private val gatewayStage: RequestedGateway,
        private val session: DefaultClientWebSocketSession,
        private val hello: Signal.Hello,
        private val sn: AtomicLong,
    ) : Stage() {
        override suspend fun invoke(loop: StageLoop) {
            clientLogger.debug("Create heartbeat job")
            val heartbeatJob = session.heartbeatJob(sn)
            // next: create client
            loop.addLast(CreateClient(gatewayStage, session, hello, sn, heartbeatJob))
        }
    }
    
    /**
     * 创建一个 [ClientImpl], 并在其中异步的处理事件。
     */
    private inner class CreateClient(
        private val gatewayStage: RequestedGateway,
        private val session: DefaultClientWebSocketSession,
        private val hello: Signal.Hello,
        private val sn: AtomicLong,
        private val heartbeatJob: HeartbeatJob,
    ) : Stage() {
        @OptIn(ExperimentalCoroutinesApi::class)
        override suspend fun invoke(loop: StageLoop) {
            val eventProcessChannel = Channel<Signal.Event>(capacity = Channel.BUFFERED)
            
            eventProcessChannel.invokeOnClose { cause ->
                clientLogger.debug("Event process closed, cause: {}", cause?.localizedMessage, cause)
            }
            
            clientLogger.trace("Create event process channel: {}", eventProcessChannel)
            
            val job = session.eventProcessJob(eventProcessChannel)
            
            clientLogger.trace("Create event process job: {}", job)
            
            val client = ClientImpl(gatewayStage.gateway, session, sn, heartbeatJob, job)
            
            clientLogger.trace("Create client: {}", client)
            
            // next: receive
            loop.addLast(Receive(hello, client))
        }
    }
    
    /**
     * 连接成功后构建client，并开始接收处理信息。
     * client中事件在 flow 中异步处理，而 pong、reconnect等信令优先处理。
     */
    private inner class Receive(
        private val hello: Signal.Hello,
        private val client: ClientImpl,
    ) : Stage() {
        override suspend fun invoke(loop: StageLoop) {
            
            val session = client.session
            if (!session.isActive) {
                val reason = session.closeReason.await()
                // TODO reconnect?
                clientLogger.error("Session is closed. reason: {}.", reason)
                return
            }
            
            try {
                // 接收
                clientLogger.trace("Receiving next frame ...")
                val frame = session.incoming.receive()
                clientLogger.trace("Received frame: {}", frame)
                
                when (frame) {
                    is Frame.Text -> processString(frame.readToText(), loop)
                    is Frame.Binary -> processString(frame.readToText(), loop)
                    else -> {
                        clientLogger.trace("Other frame: {}", frame)
                        // next: self
                        loop.addLast(this)
                    }
                }
            } catch (cancellation: CancellationException) {
                clientLogger.warn("Session is cancelled: {}, try reconnect.", cancellation.localizedMessage, cancellation)
                // next: self
                loop.addLast(this)
            } catch (e: Throwable) {
                clientLogger.error("Session received frame failed: {}, try reconnect.", e.localizedMessage, e)
                // next: self
                loop.addLast(this)
            }
        }
        
        /**
         * 更新SN的值。
         */
        private fun AtomicLong.updateSn(newSn: Long): Long {
            return updateAndGet { prev -> max(prev, newSn) }
        }
        
        private suspend fun processString(eventString: String, loop: StageLoop) {
            clientLogger.trace("On signal: {}", eventString)
            val jsonElement = decoder.parseToJsonElement(eventString)
            
            
            fun <T> decode(strategy: DeserializationStrategy<T>): T {
                return decoder.decodeFromJsonElement(strategy, jsonElement)
            }
            
            // maybe op 11: heartbeat ack
            val s = jsonElement.jsonObject["s"]?.jsonPrimitive?.intOrNull ?: return
            
            when (s) {
                // Pong.
                Signal.Pong.S_CODE -> {
                    client.heartbeatJob.pong(decoder.decodeFromJsonElement(Signal.Pong.serializer(), jsonElement))
                    // next: self
                    loop.addLast(this)
                }
                
                // Reconnect.
                Signal.Reconnect.S_CODE -> {
                    clientLogger.debug("Reconnect signal received: {}", eventString)
                    // next: reconnect
                    loop.addLast(Reconnect(client.atomicSn.get(), hello.data.sessionId))
                    
                    val reconnect = decoder.decodeFromJsonElement(Signal.Reconnect.serializer(), jsonElement)
                    val reconnectData = reconnect.data
                    client.session.closeExceptionally(ReconnectException(reconnectData.code, reconnectData.err ?: ""))
                }
                
                // Event
                Signal.Event.S_CODE -> {
                    // next: self
                    loop.addLast(this)
                    
                    val event = decode(Signal.Event.serializer())
                    
                    clientLogger.trace("On event signal: {}", event)
                    
                    // 如果sn比当前小，忽略此事件。
                    val eventSn = event.sn
                    val updated = client.atomicSn.updateSn(eventSn)
                    if (eventSn < updated) {
                        // just skip.
                        clientLogger.trace("Event sn ({}) < current sn ({}), ignore and skip this event. The event: {}", eventSn, updated, event)
                    } else {
                        // push event
                        try {
                            client.eventProcessJob.apply {
                                var success = false
                                // try to send 3 times
                                for (i in 1..3) {
                                    val sendResult = trySendEvent(event)
                                    if (sendResult.isSuccess) {
                                        success = true
                                        break
                                    }
            
                                    if (sendResult.isFailure && !sendResult.isClosed) {
                                        // retry
                                        continue
                                    }
            
                                    if (sendResult.isClosed) {
                                        return
                                    }
                                }
        
                                if (!success) {
                                    sendEvent(event)
                                }
                            }
                        } catch (e: Throwable) {
                            clientLogger.error("Event push on error: {}", e.localizedMessage, e)
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 重连
     */
    private inner class Reconnect(
        val sn: Long,
        val sessionId: String,
    ) : Stage() {
        override suspend fun invoke(loop: StageLoop) {
            loop.addLast(RequestGateway(reconnectInfo = ReconnectInfo(sn, sessionId)))
        }
    }
    
    // inner class Stop : Stage()
    
    private inner class StageLoop(
        val job: Job,
        val stageDeque: ConcurrentLinkedDeque<Stage> = ConcurrentLinkedDeque(),
    ) {
        @Volatile
        var currentStage: Stage? = null
            private set
        
        fun addLast(stage: Stage) {
            stageDeque.addLast(stage)
        }
        
        suspend fun run() {
            var next: Stage? = nextStage()
            while (job.isActive && next != null) {
                try {
                    invoke(next)
                } catch (e: Throwable) {
                    logger.error("Stage invoke failed: {}", e.localizedMessage, e)
                }
                next = nextStage()
            }
            logger.debug("Stage loop done. last stage: {}", currentStage)
            currentStage = null
        }
        
        suspend fun invoke(stage: Stage?) {
            currentStage = stage
            if (stage != null) {
                stage(this)
            }
        }
        
        fun nextStage(): Stage? = stageDeque.pollFirst()
        
    }
    
    /**
     * 连接成功的client。
     */
    private inner class ClientImpl(
        val gatewayInfo: GatewayInfo,
        
        val session: DefaultClientWebSocketSession,
        
        /**
         * sn
         */
        val atomicSn: AtomicLong,
        
        /**
         * 心跳Job
         */
        val heartbeatJob: HeartbeatJob,
        
        /**
         * 事件通道
         */
        val eventProcessJob: EventProcessJob,
    ) : KookBot.Client {
        override val sn: Long
            get() = atomicSn.get()
        override val isCompress: Boolean
            get() = this@KookBotImpl.isCompress
        override val bot: KookBot
            get() = this@KookBotImpl
        override val url: String
            get() = gatewayInfo.url
        override val isActive: Boolean
            get() = session.isActive
        
        override fun toString(): String {
            return "ClientImpl(sn=$sn, isCompress=$isCompress, heartbeatJob=$heartbeatJob, eventProcessJob=$eventProcessJob, bot=$bot)"
        }
        
    }
    
    private fun DefaultClientWebSocketSession.eventProcessJob(
        eventChannel: Channel<Signal.Event>,
    ): EventProcessJob {
        val launchJob = eventChannel
            .receiveAsFlow()
            .cancellable()
            .buffer()
            .onEach { event ->
            // val currPreProcessorQueue = preProcessorQueue
            // val currProcessorQueue = processorQueue
            if (preProcessorQueue.isNotEmpty() || processorQueue.isNotEmpty()) {
                clientLogger.trace("On event: {}", event)
                val eventType = event.type
                val eventExtraType = event.extraType
                
                // Event(s=0,
                // d={"channel_type":"GROUP","type":9,"target_id":"4587833303764121","author_id":"2371258185","content":"我是RBQ",
                // "extra":{"type":1,
                // "code":"","guild_id":"8582739890554982","channel_name":"查价bot (机器人)","author":{"id":"2371258185","username":"芦苇测试机","identify_num":"5173","online":true,"os":"Websocket","status":0,"avatar":"https://img.kaiheila.cn/assets/bot.png/icon","vip_avatar":"https://img.kaiheila.cn/assets/bot.png/icon","banner":"","nickname":"芦苇测试机","roles":[2842315],"is_vip":false,"is_ai_reduce_noise":false,"bot":true,"tag_info":{"color":"#34A853","text":"机器人"},"client_id":"OPYfwS3t0hPuVZZx"},"mention":[],"mention_all":false,"mention_roles":[],"mention_here":false,"nav_channels":[],"kmarkdown":{"raw_content":"我是RBQ","mention_part":[],"mention_role_part":[]},"last_msg_content":"芦苇测试机：我是RBQ"},"msg_id":"ee8b14c1-22eb-44d3-bb65-a1274ade96db","msg_timestamp":1650354611204,"nonce":"","from_type":1}, sn=2)
                
                val parser = EventSignals[eventType, eventExtraType] ?: run {
                    val e =
                        SimbotIllegalStateException("Unknown event type: $eventType, subType: $eventExtraType. data: $event")
                    clientLogger.error(e.localizedMessage, e)
                    // e.process(logger) { "Event receiving" } // TODO process exception?
                    return@onEach
                }
                
                val lazy = lazy(LazyThreadSafetyMode.PUBLICATION) {
                    parser.deserialize(decoder, event.d)
                }
                
                val lazyDecoded = lazy::value
                
                // pre process
                preProcessorQueue.forEach { pre ->
                    try {
                        pre(event, decoder, lazyDecoded)
                    } catch (e: Throwable) {
                        if (clientLogger.isDebugEnabled) {
                            clientLogger.debug(
                                "Event pre precess failure. Event: {}, event.data: {}", event, event.data
                            )
                        }
                        clientLogger.error("Event pre precess failure.", e)
                    }
                }
                
                if (isEventProcessAsync) {
                    launch {
                        processorQueue.forEach { processor ->
                            try {
                                processor(event, decoder, lazyDecoded)
                            } catch (e: Throwable) {
                                clientLogger.debug(
                                    "Event precess failure. Event: {}, event.data: {}", event, event.data, e
                                )
                            }
                        }
                    }
                } else {
                    processorQueue.forEach { processor ->
                        try {
                            processor(event, decoder, lazyDecoded)
                        } catch (e: Throwable) {
                            clientLogger.debug(
                                "Event precess failure. Event: {}, event.data: {}", event, event.data, e
                            )
                        }
                    }
                }
            }
        }.onCompletion { cause ->
                if (cause == null) {
                    clientLogger.debug("Event process flow is completed. No cause.")
                } else {
                    clientLogger.debug(
                        "Event process flow is completed. Cause: {}", cause.localizedMessage, cause
                    )
                }
            
        }.catch { cause ->
            clientLogger.error(
                "Event process flow on error: {}", cause.localizedMessage, cause
            )
        }.launchIn(this)
        
        return EventProcessJob(launchJob, eventChannel)
    }
    
    private class EventProcessJob(
        val job: Job,
        val eventChannel: Channel<Signal.Event>,
    ) {
        
        suspend fun sendEvent(event: Signal.Event) {
            eventChannel.send(event)
        }
        
        fun trySendEvent(event: Signal.Event): ChannelResult<Unit> {
            return eventChannel.trySend(event)
        }
        
        override fun toString(): String {
            return "EventProcessJob(job=$job, eventChannel=$eventChannel)"
        }
    }
    
    companion object
    
}


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
        throw KookApiException(d.code.toInt(), info)
    }
    
    return this
}

internal class ReconnectException(code: Int, message: String, cause: Throwable? = null) :
    KookApiException(code, message, cause)
