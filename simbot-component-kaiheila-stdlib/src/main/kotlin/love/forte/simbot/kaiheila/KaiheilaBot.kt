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
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.api.user.*
import love.forte.simbot.kaiheila.event.*
import kotlin.coroutines.*

/**
 * 开黑啦Bot对应的bot类型。
 *
 * [KaiheilaBot] 提供标准定义，但是不实现 simple-robot-api 中的 [love.forte.simbot.Bot] 接口。
 *
 * @author ForteScarlet
 */
public interface KaiheilaBot : CoroutineScope {
    override val coroutineContext: CoroutineContext

    /**
     * 当前bot所使用的配置类。
     *
     */
    public val configuration: KaiheilaBotConfiguration

    /**
     * 当前bot所使用的 [HttpClient] 实例。
     *
     * 此实例代表的用于进行API请求（xxxRequest）的http client。
     */
    public val httpClient: HttpClient


    /**
     * 添加一个事件处理器。
     */
    @JvmSynthetic
    public fun processor(processor: suspend Signal.Event.(decoder: Json, decoded: () -> Any) -> Unit)

    /**
     * process for java
     */
    @Api4J
    public fun process(processor: (rawEvent: Signal.Event, decoder: Json, decoded: () -> Any) -> Unit) {
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
     * 查询bot当前信息。
     */
    public suspend fun me(): Me

}


@Api4J
public fun interface EventProcessor4J<out EX : Event.Extra, E : Event<EX>> {
    /**
     * 事件处理器。
     */
    @Api4J
    public fun process(event: E)

}