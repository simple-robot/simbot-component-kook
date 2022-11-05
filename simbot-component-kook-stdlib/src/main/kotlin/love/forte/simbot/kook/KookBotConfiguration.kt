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

package love.forte.simbot.kook

import io.ktor.client.*
import io.ktor.client.engine.*
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


@Retention(AnnotationRetention.BINARY)
@DslMarker
public annotation class KookBotConfigurationDSL


/**
 * [KookBot] 需要使用的配置类。
 *
 * 配置类不应在bot构建完成之后再做修改。
 *
 * @author ForteScarlet
 */
public class KookBotConfiguration {
    
    /**
     * 设置bot进行连接的时候使用要使用压缩数据。
     */
    @KookBotConfigurationDSL
    public var isCompress: Boolean = true
    
    /**
     * Bot用于解析api请求或其他用途的解析器。
     */
    @KookBotConfigurationDSL
    public var decoder: Json = defaultDecoder
    
    /**
     * 为bot提供一个 [CoroutineContext]. 如果其中存在 [kotlinx.coroutines.Job], 则会作为parent job。
     */
    @KookBotConfigurationDSL
    public var coroutineContext: CoroutineContext = EmptyCoroutineContext
    
    
    // region request client
    /**
     * 配置bot内部要使用的client Engine。
     */
    @KookBotConfigurationDSL
    public var clientEngine: HttpClientEngine? = null
    
    /**
     * 配置bot内部要使用的client Engine factory。
     *
     * 如果 [clientEngine] 存在，则优先使用 [clientEngine].
     */
    @KookBotConfigurationDSL
    public var clientEngineFactory: HttpClientEngineFactory<*>? = null
    
    /**
     * 配置bot内部要使用的httpclient。
     *
     * 默认情况下的client中已经install了
     * [ContentNegotiation][io.ktor.client.plugins.contentnegotiation.ContentNegotiation.Plugin] 中的 `json`
     */
    @KookBotConfigurationDSL
    public var httpClientConfig: HttpClientConfig<*>.() -> Unit = {}
    
    
    /**
     * 配置bot内部要使用的httpclient。
     *
     * 默认情况下的client中已经install了
     * [ContentNegotiation][io.ktor.client.plugins.contentnegotiation.ContentNegotiation.Plugin] 中的 `json`
     */
    @KookBotConfigurationDSL
    public fun httpClientConfig(block: HttpClientConfig<*>.() -> Unit) {
        this.httpClientConfig = block
    }
    // endregion
    
    // region api request timeout
    /**
     * api请求的超时配置。
     * 如果为null则不会在 `httpClient` 中安装 [io.ktor.client.plugins.HttpTimeout]。
     */
    @KookBotConfigurationDSL
    public var timeout: TimeoutConfiguration? = TimeoutConfiguration(
        connectTimeoutMillis = 5000L,
        requestTimeoutMillis = 5000L,
        socketTimeoutMillis = null,
    )
    
    /**
     * 禁用超时配置。
     */
    @KookBotConfigurationDSL
    public fun disableTimeout() {
        timeout = null
    }
    
    /**
     * api请求的连接超时时间。
     */
    @KookBotConfigurationDSL
    public var connectTimeoutMillis: Long?
        get() = timeout?.connectTimeoutMillis
        set(value) {
            timeout?.let { t -> t.connectTimeoutMillis = value } ?: apply {
                timeout = TimeoutConfiguration(connectTimeoutMillis = value)
            }
        }
    
    /**
     * api请求的请求超时时间。
     */
    @KookBotConfigurationDSL
    public var requestTimeoutMillis: Long?
        get() = timeout?.requestTimeoutMillis
        set(value) {
            timeout?.let { t -> t.requestTimeoutMillis = value } ?: apply {
                timeout = TimeoutConfiguration(requestTimeoutMillis = value)
            }
        }
    
    /**
     * api请求的请求超时时间。
     */
    @KookBotConfigurationDSL
    public var socketTimeoutMillis: Long?
        get() = timeout?.socketTimeoutMillis
        set(value) {
            timeout?.let { t -> t.socketTimeoutMillis = value } ?: apply {
                timeout = TimeoutConfiguration(socketTimeoutMillis = value)
            }
        }
    // endregion
    
    /**
     * ws连接超时时间。默认为 [DEFAULT_WS_CONNECT_TIMEOUT].
     */
    @KookBotConfigurationDSL
    public var wsConnectTimeout: Long = DEFAULT_WS_CONNECT_TIMEOUT
    
    /**
     * 在进行**事件处理**时是否进行异步处理（使用 `launch { ... }` 调度）。
     *
     * 此处指的是 **事件处理** 而不是 **事件预处理**。预处理逻辑将始终不是异步的（是'同步'非阻塞的）。
     *
     * 默认为 `true`，如果为 `false` 则会根据接收的事件依序调度。在事件频繁而事件调度逻辑较为耗时时这可能会导致事件积压。
     *
     */
    @KookBotConfigurationDSL
    public var isEventProcessAsync: Boolean = true
    
    
    /**
     * 禁用事件的异步调度。即 set [isEventProcessAsync] = `false`
      */
    @KookBotConfigurationDSL
    public fun disableEventProcessAsync() {
        isEventProcessAsync = false
    }
    
    
    /**
     * 在执行 [KookBot.start] 建立连接成功后、进行事件处理之前执行此函数。
     */
    @KookBotConfigurationDSL
    public var preEventProcessor: suspend (bot: KookBot, sessionId: String) -> Unit = { _, _ -> }
    
    
    /**
     * 在执行 [KookBot.start] 建立连接成功后、进行事件处理之前执行此函数。
     */
    @KookBotConfigurationDSL
    public fun preEventProcessor(block: suspend (bot: KookBot, sessionId: String) -> Unit) {
        val old = this.preEventProcessor
        this.preEventProcessor = { b, s ->
            old(b, s)
            block(b, s)
        }
    }
    
    
    public companion object {
        /**
         * 默认的连接超时时间。
         */
        public const val DEFAULT_WS_CONNECT_TIMEOUT: Long = 6000L
    
        
        /**
         * [KookBotConfiguration] 默认使用的解析器。
         */
        public val defaultDecoder: Json = Json {
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }
    
    /**
     * api请求的统一超时时间配置。
     */
    public data class TimeoutConfiguration(
        var connectTimeoutMillis: Long? = null,
        var requestTimeoutMillis: Long? = null,
        var socketTimeoutMillis: Long? = null,
    ) {
        public constructor() : this(null, null, null)
    }
}


/**
 * 配置 [KookBotConfiguration.timeout]。
 *
 * 只有当 [timeout][KookBotConfiguration.timeout] 不为null时或者 [init] == true 时才会触发。
 *
 * @param init 如果 timeout 为null且 [init] 为true，则会初始化一个 [timeout][KookBotConfiguration.timeout]
 *
 */
public inline fun KookBotConfiguration.timeout(
    init: Boolean = true,
    block: KookBotConfiguration.TimeoutConfiguration.() -> Unit,
) {
    val timeout = this.timeout
    if (timeout == null && init) {
        this.timeout = KookBotConfiguration.TimeoutConfiguration().also(block)
    } else {
        timeout?.block()
    }
}