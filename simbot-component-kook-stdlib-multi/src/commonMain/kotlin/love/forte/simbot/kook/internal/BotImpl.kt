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
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import love.forte.simbot.kook.Bot
import love.forte.simbot.kook.BotConfiguration
import love.forte.simbot.kook.ProcessorType
import love.forte.simbot.kook.Ticket
import love.forte.simbot.kook.api.user.GetMeApi
import love.forte.simbot.kook.api.user.Me
import love.forte.simbot.kook.api.user.OfflineApi
import love.forte.simbot.kook.event.Event
import love.forte.simbot.kook.event.Signal
import love.forte.simbot.logger.LoggerFactory
import love.forte.simbot.util.stageloop.loop
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.Volatile

internal typealias EventProcessor = suspend Event<*>.(raw: String) -> Unit

/**
 *
 * @author ForteScarlet
 */
internal class BotImpl(
    override val ticket: Ticket, override val configuration: BotConfiguration
) : Bot {
    private val botLogger = LoggerFactory.getLogger("love.forte.simbot.kook.bot.${ticket.clickId}")
    internal val eventLogger = LoggerFactory.getLogger("love.forte.simbot.kook.event.${ticket.clickId}")

    override val authorization: String = "${ticket.type.prefix} ${ticket.token}"

    private val queueMap = ActualEnumMap.create<ProcessorType, EventProcessorQueue<EventProcessor>> {
        createEventProcessorQueue(
            16
        )
    }

    override fun processor(processorType: ProcessorType, processor: suspend Event<*>.(raw: String) -> Unit) {
        queueMap[processorType].add(processor)
    }

    private val job = SupervisorJob(configuration.coroutineContext[Job])
    override val coroutineContext: CoroutineContext = job + configuration.coroutineContext

    override val apiClient: HttpClient = resolveHttpClient(
        configuration, configuration.clientEngine, configuration.clientEngineFactory, configuration.clientEngineConfig
    ).also(::closeOnBotClosed)

    internal val wsClient: HttpClient = resolveWsClient(
        configuration.wsEngine, configuration.wsEngineFactory, configuration.wsEngineConfig
    ).also(::closeOnBotClosed)

    private fun resolveHttpClient(
        configuration: BotConfiguration,
        engine: HttpClientEngine?,
        engineFactory: HttpClientEngineFactory<*>?,
        engineConfig: BotConfiguration.EngineConfiguration?,
    ): HttpClient = when {
        engine != null -> HttpClient(engine) {
            configApiHttpClient(configuration, engineConfig)
        }

        engineFactory != null -> HttpClient(engineFactory) {
            configApiHttpClient(configuration, engineConfig)
        }

        else -> HttpClient {
            configApiHttpClient(configuration, engineConfig)
        }
    }

    private fun HttpClientConfig<*>.configApiHttpClient(
        configuration: BotConfiguration, engineConfiguration: BotConfiguration.EngineConfiguration?
    ) {
        install(ContentNegotiation) {
            json(defaultApiDecoder)
        }

        val apiHttpRequestTimeoutMillis = configuration.timeout?.requestTimeoutMillis
        val apiHttpConnectTimeoutMillis = configuration.timeout?.connectTimeoutMillis
        val apiHttpSocketTimeoutMillis = configuration.timeout?.socketTimeoutMillis

        if (apiHttpRequestTimeoutMillis != null || apiHttpConnectTimeoutMillis != null || apiHttpSocketTimeoutMillis != null) {
            install(HttpTimeout) {
                apiHttpRequestTimeoutMillis?.also { requestTimeoutMillis = it }
                apiHttpConnectTimeoutMillis?.also { connectTimeoutMillis = it }
                apiHttpSocketTimeoutMillis?.also { socketTimeoutMillis = it }
            }
        }

        install(HttpRequestRetry) {
            maxRetries = 3
        }

        engineConfiguration?.also { ec ->
            engine {
                ec.pipelining?.also { pipelining = it }
                ec.threadsCount?.also { threadsCount = it }
            }
        }
    }

    private fun resolveWsClient(
        engine: HttpClientEngine?,
        engineFactory: HttpClientEngineFactory<*>?,
        engineConfig: BotConfiguration.EngineConfiguration?,
    ): HttpClient = when {
        engine != null -> HttpClient(engine) {
            configWsClient(engineConfig)
        }

        engineFactory != null -> HttpClient(engineFactory) {
            configWsClient(engineConfig)
        }

        else -> HttpClient {
            configWsClient(engineConfig)
        }
    }


    private fun HttpClientConfig<*>.configWsClient(
        engineConfiguration: BotConfiguration.EngineConfiguration?
    ) {
        install(ContentNegotiation) {
            json(defaultApiDecoder)
        }

        install(HttpRequestRetry) {
            maxRetries = 3
        }

        WebSockets {
            pingInterval = 30_000L
        }

        engineConfiguration?.also { ec ->
            engine {
                ec.pipelining?.also { pipelining = it }
                ec.threadsCount?.also { threadsCount = it }
            }
        }
    }


    private fun closeOnBotClosed(client: HttpClient) {
        job.invokeOnCompletion { client.close() }
    }

    override val isActive: Boolean
        get() = job.isActive

    override var isStarted: Boolean by atomic(false)

    @Volatile
    private lateinit var _me: Me

    override suspend fun me(): Me {
        return GetMeApi.requestBy(this).also {
            _me = it
        }
    }

    override val botUserInfo: Me
        get() = if (::_me.isInitialized) _me else throw IllegalStateException("Bot is not initialized.")

    override suspend fun offline() {
        OfflineApi.requestBy(this)
    }

    private val startLock = Mutex()

    @Volatile
    private var currentClientJob: Job? = null

    override suspend fun start(closeBotOnFailure: Boolean) {
        try {
            startLock.withLock {
                if (job.isCancelled) {
                    throw CancellationException("Bot has bean cancelled.")
                }
                // close current client if exist
                if (currentClientJob != null) {
                    botLogger.debug("Cancel current client: {}", currentClientJob)
                    currentClientJob?.cancel()
                    currentClientJob = null
                }


                val connect = Connect(this, botLogger, configuration.isCompress)
                botLogger.debug("Create connect: {}", connect)

                var currentState: State? = connect
                while (currentState?.isReceiving == false) {
                    currentState = currentState()
                }

                if (currentState == null) {
                    throw IllegalStateException("Bot start failed.")
                }

                currentClientJob = launch { currentState.loop() }

                isStarted = true

                me() // init bot user info
            }
        } catch (e: Throwable) {
            // close this bot
            if (closeBotOnFailure) {
                botLogger.error("Close bot on start failed", e)
                close()
            }
            throw e
        }
    }


    override suspend fun join() {
        job.join()
    }

    override fun close() {
        job.cancel()
        currentClientJob = null
    }

    internal suspend fun processEvent(event: Signal.Event<*>, raw: String) {
        val prepareProcessors = queueMap[ProcessorType.PREPARE]
        val normalProcessors = queueMap[ProcessorType.NORMAL]
        if (prepareProcessors.isEmpty() && normalProcessors.isEmpty()) {
            eventLogger.trace("prepare processors and normal processors are both empty, skip.")
            return
        }

        val eventData = event.d
        prepareProcessors.forEach { processor ->
            try {
                processor.invoke(eventData, raw)
            } catch (e: Throwable) {
                eventLogger.error("Event prepare process failed. Enable debug level log for more information.", e)
                eventLogger.debug(
                    "Event prepare processor {} invoke failed. Event: {}, event.data: {}",
                    processor,
                    event,
                    eventData,
                    e
                )
            }
        }

        suspend fun doNormalProcess() {
            normalProcessors.forEach { processor ->
                try {
                    processor.invoke(eventData, raw)
                } catch (e: Throwable) {
                    eventLogger.error("Event normal process failed. Enable debug level log for more information.", e)
                    eventLogger.debug(
                        "Event normal processor {} invoke failed. Event: {}, event.data: {}",
                        processor,
                        event,
                        eventData,
                        e
                    )
                }
            }
        }

        if (configuration.isNormalEventProcessAsync) {
            launch {
                doNormalProcess()
            }
        } else {
            doNormalProcess()
        }


    }

    companion object {
        internal val defaultApiDecoder = Json {
            encodeDefaults = true
            isLenient = true
            allowSpecialFloatingPointValues = true
            prettyPrint = false
            useArrayPolymorphism = false
            ignoreUnknownKeys = true
        }
    }
}

