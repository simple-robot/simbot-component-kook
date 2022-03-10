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

package love.forte.simbot.component.kaihieila

import kotlinx.serialization.json.*
import love.forte.simbot.*
import love.forte.simbot.component.kaihieila.internal.*
import love.forte.simbot.event.*
import love.forte.simbot.kaiheila.*
import kotlin.coroutines.*

/**
 *
 * [KaiheilaComponentBot] 的管理器实现。
 *
 * @author ForteScarlet
 */
public abstract class KaiheilaBotManager : BotManager<KaiheilaComponentBot>() {
    abstract override val component: KaiheilaComponent
    public abstract val configuration: KaiheilaBotManagerConfiguration

    override fun register(verifyInfo: BotVerifyInfo): Bot {
        TODO("Not yet implemented")
    }

    /**
     * 通过 [ticket] 和 [configuration] 注册bot。
     */
    public abstract fun register(
        ticket: KaiheilaBot.Ticket,
        configuration: KaiheilaBotConfiguration
    ): KaiheilaComponentBot


    /**
     * 通过 [clientId]、 [token] 和 [configuration] 注册bot。
     */
    public fun register(clientId: ID, token: String, configuration: KaiheilaBotConfiguration): KaiheilaComponentBot {
        return register(SimpleTicket(clientId, token), configuration)
    }

    /**
     * 通过 [ticket] 和 [block] 注册bot。
     */
    public fun register(ticket: KaiheilaBot.Ticket, block: KaiheilaBotConfiguration.() -> Unit = {}): KaiheilaComponentBot {
        return register(ticket, KaiheilaBotConfiguration().also(block))
    }


    /**
     * 通过 [clientId]、 [token] 和 [block] 注册bot。
     */
    public fun register(clientId: ID, token: String, block: KaiheilaBotConfiguration.() -> Unit = {}): KaiheilaComponentBot {
        return register(clientId, token, KaiheilaBotConfiguration().also(block))
    }


    public companion object {
        private val registerJson = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }


        @JvmStatic
        public fun newInstance(eventProcessor: EventProcessor): KaiheilaBotManager {
            return KaiheilaBotManagerImpl(KaiheilaBotManagerConfiguration(eventProcessor))
        }

        @JvmStatic
        public fun newInstance(configuration: KaiheilaBotManagerConfiguration): KaiheilaBotManager {
            return KaiheilaBotManagerImpl(configuration)
        }


    }


}

public fun kaiheilaBotManager(
    eventProcessor: EventProcessor,
    block: KaiheilaBotManagerConfiguration.() -> Unit = {}
): KaiheilaBotManager {
    return KaiheilaBotManager.newInstance(KaiheilaBotManagerConfiguration(eventProcessor).also(block))
}


/**
 * [KaiheilaBotManager] 使用的配置类。
 *
 * @param eventProcessor 当前bot所使用的事件处理器
 */
@Suppress("MemberVisibilityCanBePrivate")
public class KaiheilaBotManagerConfiguration(public var eventProcessor: EventProcessor) {

    /**
     * 如果其中存在 [kotlinx.coroutines.Job], 则会被作为parentJob使用。
     */
    public var parentCoroutineContext: CoroutineContext = EmptyCoroutineContext

}
