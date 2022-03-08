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

package love.forte.simbot.kaiheila

import io.ktor.client.*
import io.ktor.client.engine.*
import kotlinx.serialization.json.*
import kotlin.coroutines.*


@Retention(AnnotationRetention.BINARY)
@DslMarker
public annotation class KaiheilaBotConfigurationDSL


/**
 * [KaiheilaBot] 需要使用的配置类。
 *
 * 配置类不应在bot构建完成之后再做修改。
 *
 * @author ForteScarlet
 */
public class KaiheilaBotConfiguration {


    /**
     * 设置bot进行连接的时候使用要使用压缩数据。
     */
    @KaiheilaBotConfigurationDSL
    public val isCompress: Boolean = true

    /**
     * Bot用于解析api请求或其他用途的解析器。
     */
    @KaiheilaBotConfigurationDSL
    public var decoder: Json = defaultDecoder

    /**
     * 为bot提供一个 [CoroutineContext]. 如果其中存在 [kotlinx.coroutines.Job], 则会作为parent job。
     */
    @KaiheilaBotConfigurationDSL
    public var coroutineContext: CoroutineContext = EmptyCoroutineContext


    /**
     * 配置bot内部要使用的client Engine。
     */
    @KaiheilaBotConfigurationDSL
    public var clientEngine: HttpClientEngine? = null

    /**
     * 配置bot内部要使用的client Engine factory。
     *
     * 如果 [clientEngine] 存在，则优先使用 [clientEngine].
     */
    @KaiheilaBotConfigurationDSL
    public var clientEngineFactory: HttpClientEngineFactory<*>? = null

    /**
     * 配置bot内部要使用的httpclient。
     * 默认情况下的client中已经install了[io.ktor.client.features.json.JsonFeature]和[io.ktor.client.features.websocket.WebSockets],
     * 如果有必要可重新加载以覆盖。
     */
    @KaiheilaBotConfigurationDSL
    public var httpClientConfig: HttpClientConfig<*>.() -> Unit = {}


    /**
     * 配置bot内部要使用的httpclient。
     * 默认情况下的client中已经install了[io.ktor.client.features.json.JsonFeature]和[io.ktor.client.features.websocket.WebSockets],
     * 如果有必要可重新加载以覆盖。
     */
    @KaiheilaBotConfigurationDSL
    public fun httpClientConfig(block: HttpClientConfig<*>.() -> Unit) {
        this.httpClientConfig = block
    }


    public companion object {
        /**
         * [KaiheilaBotConfiguration] 默认使用的解析器。
         */
        public val defaultDecoder: Json = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
    }
}