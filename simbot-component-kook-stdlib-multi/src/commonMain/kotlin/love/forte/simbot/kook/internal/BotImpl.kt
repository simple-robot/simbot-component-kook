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
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.atomicfu.AtomicLong
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.updateAndGet
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import love.forte.simbot.kook.Bot
import love.forte.simbot.kook.BotConfiguration
import love.forte.simbot.kook.ProcessorType
import love.forte.simbot.kook.Ticket
import love.forte.simbot.kook.api.Gateway
import love.forte.simbot.kook.api.user.GetMeApi
import love.forte.simbot.kook.api.user.Me
import love.forte.simbot.kook.api.user.OfflineApi
import love.forte.simbot.kook.event.Event
import love.forte.simbot.kook.event.Signal
import love.forte.simbot.logger.LoggerFactory
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.Volatile

internal typealias EventProcessor = suspend Signal.Event<*>.(Event<*>) -> Unit

/**
 *
 * @author ForteScarlet
 */
internal class BotImpl(
    override val ticket: Ticket,
    override val configuration: BotConfiguration
) : Bot {
    private val botLogger = LoggerFactory.getLogger("love.forte.simbot.kook.bot.${ticket.clickId}")

    override val authorization: String = "${ticket.type.prefix} ${ticket.token}"

    private val queueMap =
        ActualEnumMap.create<ProcessorType, EventProcessorQueue<EventProcessor>> {
            createEventProcessorQueue(
                16
            )
        }

    override fun processor(processorType: ProcessorType, processor: suspend Signal.Event<*>.(Event<*>) -> Unit) {
        queueMap[processorType].add(processor)
    }

    private val job = SupervisorJob(configuration.coroutineContext[Job])
    override val coroutineContext: CoroutineContext = job + configuration.coroutineContext

    override val apiClient: HttpClient =
        resolveHttpClient(
            configuration,
            configuration.clientEngine,
            configuration.clientEngineFactory,
            configuration.clientEngineConfig
        )

    private val wsClient: HttpClient =
        resolveHttpClient(
            configuration,
            configuration.wsEngine,
            configuration.wsEngineFactory,
            configuration.wsEngineConfig
        )

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
        configuration: BotConfiguration,
        engineConfiguration: BotConfiguration.EngineConfiguration?
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

    override val isActive: Boolean
        get() = job.isActive

    override val isStarted: Boolean by atomic(false)

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

    override suspend fun start() {
        startLock.withLock {
            if (job.isCancelled) {
                throw IllegalStateException("Bot is already started.")
            }
            // close current client if exist


            val loopJob = SupervisorJob(job)


            // get gateway
            // connect gateway

            TODO("Not yet implemented")
        }
    }





    override suspend fun join() {
        job.join()
    }

    override fun close() {
        job.cancel()
    }

    private inner class Client(
        val gatewayInfo: GatewayInfo,
        val session: DefaultClientWebSocketSession,
        val sn: AtomicLongRef,
        // heart beat job
        // event process job
    )


    companion object {
        private val defaultApiDecoder = Json {
            encodeDefaults = true
            isLenient = true
            allowSpecialFloatingPointValues = true
            prettyPrint = false
            useArrayPolymorphism = false
        }
    }
}


private data class GatewayInfo(val url: String, val urlBuilder: URLBuilder.() -> Unit = {})


internal class AtomicLongRef(initValue: Long = 0) {
    private val atomicValue: AtomicLong = atomic(initValue)
    var value: Long
        get() = atomicValue.value
        set(value) {
            atomicValue.value = value
        }

    fun updateAndGet(function: (Long) -> Long): Long = atomicValue.updateAndGet(function)
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
