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


package love.forte.simbot.component.kaiheila

import kotlinx.serialization.Serializable
import love.forte.simbot.*
import love.forte.simbot.application.ApplicationConfiguration
import love.forte.simbot.application.EventProviderAutoRegistrarFactory
import love.forte.simbot.application.EventProviderFactory
import love.forte.simbot.component.kaiheila.internal.KaiheilaBotManagerImpl
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.kaiheila.KaiheilaBot
import love.forte.simbot.kaiheila.KaiheilaBotConfiguration
import love.forte.simbot.kaiheila.SimpleTicket
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 * 在开黑啦组件中对于 [Bot][KaiheilaBot] 的注册函数的常见形式。
 *
 * @see KaiheilaBotManager
 */
public interface KaiheilaBotRegistrar {
    
    /**
     * 通过 [ticket] 和 [configuration] 注册bot。
     */
    public fun register(
        ticket: KaiheilaBot.Ticket,
        configuration: KaiheilaComponentBotConfiguration,
    ): KaiheilaComponentBot
    
    
    /**
     * 通过 [clientId]、 [token] 和 [configuration] 注册bot。
     */
    public fun register(
        clientId: String,
        token: String,
        configuration: KaiheilaComponentBotConfiguration,
    ): KaiheilaComponentBot
    
    /**
     * 通过 [ticket] 和 [block] 注册bot。
     */
    public fun register(
        ticket: KaiheilaBot.Ticket,
        block: KaiheilaComponentBotConfiguration.() -> Unit = {},
    ): KaiheilaComponentBot
    
    
    /**
     * 通过 [clientId]、 [token] 和 [block] 注册bot。
     */
    public fun register(
        clientId: String,
        token: String,
        block: KaiheilaComponentBotConfiguration.() -> Unit = {},
    ): KaiheilaComponentBot
}


/**
 *
 * [KaiheilaComponentBot] 的管理器实现。
 *
 * @author ForteScarlet
 */
@Suppress("MemberVisibilityCanBePrivate")
public abstract class KaiheilaBotManager : BotManager<KaiheilaComponentBot>(), KaiheilaBotRegistrar {
    abstract override val component: KaiheilaComponent
    public abstract val configuration: KaiheilaBotManagerConfiguration
    
    /**
     * 通过 `.bot` 的json配置文件注册一个bot信息。
     */
    override fun register(verifyInfo: BotVerifyInfo): KaiheilaComponentBot {
        val serializer = KaiheilaBotVerifyInfoConfiguration.serializer()
        
        val component = verifyInfo.componentId
        val currentComponent = this.component.id.literal
        
        if (component != currentComponent) {
            logger.debug(
                "[{}] mismatch: [{}] != [{}]", verifyInfo.name, component, currentComponent
            )
            throw ComponentMismatchException("[$component] != [$currentComponent]")
        }
        
        val botInfo = verifyInfo.decode(serializer)
        
        logger.debug("[{}] json element load: {}", verifyInfo.name, botInfo)
        
        // no config
        return register(botInfo.clientId, botInfo.token, botInfo::includeConfig)
        
    }
    
    /**
     * 通过 [ticket] 和 [configuration] 注册bot。
     */
    abstract override fun register(
        ticket: KaiheilaBot.Ticket,
        configuration: KaiheilaComponentBotConfiguration,
    ): KaiheilaComponentBot
    
    
    /**
     * 通过 [clientId]、 [token] 和 [configuration] 注册bot。
     */
    override fun register(
        clientId: String,
        token: String,
        configuration: KaiheilaComponentBotConfiguration,
    ): KaiheilaComponentBot {
        return register(SimpleTicket(clientId, token), configuration)
    }
    
    /**
     * 通过 [ticket] 和 [block] 注册bot。
     */
    override fun register(
        ticket: KaiheilaBot.Ticket,
        block: KaiheilaComponentBotConfiguration.() -> Unit,
    ): KaiheilaComponentBot {
        return register(ticket, KaiheilaComponentBotConfiguration(KaiheilaBotConfiguration()).also(block))
    }
    
    
    /**
     * 通过 [clientId]、 [token] 和 [block] 注册bot。
     */
    override fun register(
        clientId: String,
        token: String,
        block: KaiheilaComponentBotConfiguration.() -> Unit,
    ): KaiheilaComponentBot {
        return register(clientId, token, KaiheilaComponentBotConfiguration(KaiheilaBotConfiguration()).also(block))
    }
    
    
    // TODO Auto registrar
    public companion object Factory : EventProviderFactory<KaiheilaBotManager, KaiheilaBotManagerConfiguration> {
        override val key: Attribute<KaiheilaBotManager> = attribute("simbot.kaiheila")
        private val logger = LoggerFactory.getLogger(KaiheilaBotManager::class.java)
        
        
        /**
         * 通过各项配置构建 [KaiheilaBotManager] 实例。
         */
        override suspend fun create(
            eventProcessor: EventProcessor,
            components: List<Component>,
            applicationConfiguration: ApplicationConfiguration,
            configurator: KaiheilaBotManagerConfiguration.() -> Unit,
        ): KaiheilaBotManager {
            val component = components.find { it.id.literal == KaiheilaComponent.ID_VALUE } as? KaiheilaComponent
                ?: throw NoSuchComponentException("${KaiheilaComponent.ID_VALUE} type of KaiheilaComponent")
            
            val config = KaiheilaBotManagerConfigurationImpl().also {
                val currentContext = applicationConfiguration.coroutineContext
                it.parentCoroutineContext = currentContext
                configurator(it)
            }
            
            
            return KaiheilaBotManagerImpl(eventProcessor, config, component).also {
                config.useBotManager(it)
            }
        }
        
        @Suppress("DeprecatedCallableAddReplaceWith")
        @OptIn(InternalSimbotApi::class)
        @JvmStatic
        @Deprecated("Use Factory in Application.")
        public fun newInstance(eventProcessor: EventProcessor): KaiheilaBotManager {
            return KaiheilaBotManagerImpl(
                eventProcessor,
                KaiheilaBotManagerConfigurationImpl(),
                KaiheilaComponent()
            )
        }
        
        @Suppress("DeprecatedCallableAddReplaceWith")
        @OptIn(InternalSimbotApi::class)
        @JvmStatic
        @Deprecated("Use Factory in Application.")
        public fun newInstance(
            eventProcessor: EventProcessor,
            configuration: KaiheilaBotManagerConfiguration,
        ): KaiheilaBotManager {
            return KaiheilaBotManagerImpl(eventProcessor, configuration, KaiheilaComponent())
        }
        
        
    }
    
    
}


/**
 * 实现 [EventProviderAutoRegistrarFactory] 并通过 `Java SPI`
 * 支持 [KaiheilaBotManager] 的自动安装。
 */
public class KaiheilaBotManagerAutoRegistrarFactory :
    EventProviderAutoRegistrarFactory<KaiheilaBotManager, KaiheilaBotManagerConfiguration> {
    override val registrar: KaiheilaBotManager.Factory
        get() = KaiheilaBotManager
}


// TODO DELETE
/**
 * 配置并构建一个 [KaiheilaBotManager] 实例。
 *
 */
@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated("Use Factory in application")
public fun kaiheilaBotManager(
    eventProcessor: EventProcessor,
    block: KaiheilaBotManagerConfiguration.() -> Unit = {},
): KaiheilaBotManager {
    
    @Suppress("DEPRECATION") return KaiheilaBotManager.newInstance(
        eventProcessor,
        KaiheilaBotManagerConfigurationImpl().also(block)
    )
}


/**
 * [KaiheilaBotManager] 使用的配置类。
 */
public interface KaiheilaBotManagerConfiguration {
    
    /**
     * bot管理器中为所有bot分配的父级协程上下文。
     *
     * 此属性的初始值为 [ApplicationConfiguration] 中的值。
     *
     */
    public var parentCoroutineContext: CoroutineContext
    
    
    /**
     * 通过 [ticket] 和 [configuration] 注册bot。
     */
    public fun register(
        ticket: KaiheilaBot.Ticket,
        configuration: KaiheilaComponentBotConfiguration,
        onBot: suspend (KaiheilaComponentBot) -> Unit,
    )
    
    /**
     * 通过 [clientId]、 [token] 和 [configuration] 注册bot。
     */
    public fun register(
        clientId: String,
        token: String,
        configuration: KaiheilaComponentBotConfiguration,
        onBot: suspend (KaiheilaComponentBot) -> Unit,
    )
    
    /**
     * 通过 [ticket] 和 [block] 注册bot。
     */
    public fun register(
        ticket: KaiheilaBot.Ticket,
        block: KaiheilaComponentBotConfiguration.() -> Unit = {},
        onBot: suspend (KaiheilaComponentBot) -> Unit,
    )
    
    /**
     * 通过 [clientId]、 [token] 和 [block] 注册bot。
     */
    public fun register(
        clientId: String,
        token: String,
        block: KaiheilaComponentBotConfiguration.() -> Unit = {},
        onBot: suspend (KaiheilaComponentBot) -> Unit,
    )
}


private class KaiheilaBotManagerConfigurationImpl : KaiheilaBotManagerConfiguration {
    override var parentCoroutineContext: CoroutineContext = EmptyCoroutineContext
    private var managerConfig: (suspend (KaiheilaBotManager) -> Unit) = {}
    
    private fun addConfig(newConfig: suspend (KaiheilaBotManager) -> Unit) {
        managerConfig.also { old ->
            managerConfig = {
                old(it)
                newConfig(it)
            }
        }
    }
    
    /**
     * 通过 [ticket] 和 [configuration] 注册bot。
     */
    override fun register(
        ticket: KaiheilaBot.Ticket,
        configuration: KaiheilaComponentBotConfiguration,
        onBot: suspend (KaiheilaComponentBot) -> Unit,
    ) {
        addConfig { m ->
            onBot(m.register(ticket, configuration))
        }
    }
    
    /**
     * 通过 [clientId]、 [token] 和 [configuration] 注册bot。
     */
    override fun register(
        clientId: String,
        token: String,
        configuration: KaiheilaComponentBotConfiguration,
        onBot: suspend (KaiheilaComponentBot) -> Unit,
    ) {
        addConfig { m ->
            onBot(m.register(clientId, token, configuration))
        }
    }
    
    /**
     * 通过 [ticket] 和 [block] 注册bot。
     */
    override fun register(
        ticket: KaiheilaBot.Ticket,
        block: KaiheilaComponentBotConfiguration.() -> Unit,
        onBot: suspend (KaiheilaComponentBot) -> Unit,
    ) {
        addConfig { m ->
            onBot(m.register(ticket, block))
        }
    }
    
    /**
     * 通过 [clientId]、 [token] 和 [block] 注册bot。
     */
    override fun register(
        clientId: String,
        token: String,
        block: KaiheilaComponentBotConfiguration.() -> Unit,
        onBot: suspend (KaiheilaComponentBot) -> Unit,
    ) {
        addConfig { m ->
            onBot(m.register(clientId, token, block))
        }
    }
    
    suspend fun useBotManager(botManager: KaiheilaBotManager) {
        managerConfig(botManager)
    }
}


/*
 * 暂且备份
 * `.bot` 配置文件读取的配置信息实体。
 *
 * ```json
 * {
 *  "component": "simbot.kaiheila",
 *  "clientId": "Your Client ID",
 *  "token": "Your ws token",
 *  "isCompress": true,
 *  "syncPeriods": {
 *      "guildSyncPeriod": 60000,
 *      "memberSyncPeriods": 60000
 *  }
 * }
 * ```
 *
 */

/**
 * `.bot` 配置文件读取的配置信息实体, 用于接收从 [BotVerifyInfo] 中的序列化信息。
 *
 * 在 [KaiheilaBotVerifyInfoConfiguration] 中，[clientId] 和 [token] 为必选项，
 * 存在于当前配置属性的最外层。除了必选项以外还存在部分可选项存在于 [KaiheilaBotVerifyInfoConfiguration.Config] 类型中，
 * 作为 [config][KaiheilaBotVerifyInfoConfiguration.config] 属性使用。
 *
 * 简化json e.g.
 * ```json
 * {
 *   "component": "simbot.kaiheila",
 *   "clientId": "Your client ID",
 *   "token": "Your ws token"
 * }
 * ```
 *
 * 完整json e.g.
 * ```json
 * {
 *  "component": "simbot.kaiheila",
 *  "clientId": "Your client ID",
 *  "token": "Your ws token",
 *  "config": {
 *      "isCompress": true,
 *      "syncPeriods": {
 *          "guildSyncPeriod": 60000,
 *          "memberSyncPeriods": 60000
 *          }
 *      }
 * }
 * ```
 *
 */
@Serializable
public data class KaiheilaBotVerifyInfoConfiguration(
    /**
     * client id
     */
    val clientId: String,
    
    /**
     * token
     */
    val token: String,
    
    /**
     * 额外的部分可选配置属性。
     */
    val config: Config = Config.DEFAULT,
) {
    
    /**
     * 在 [KaiheilaBotVerifyInfoConfiguration] 中除了必须的bot信息以外的可选配置信息。
     *
     */
    @Serializable
    public data class Config(
        /**
         * 是否压缩数据。
         *
         * _Note: 尚未使用的属性。_
         */
        val isCompress: Boolean = true,
        
        /**
         * 缓存对象信息的同步周期
         */
        val syncPeriods: KaiheilaComponentBotConfiguration.SyncPeriods = KaiheilaComponentBotConfiguration.SyncPeriods(),
        
        ) {
        public companion object {
            /**
             * [Config] 全默认属性实例。
             *
             */
            @JvmField
            public val DEFAULT: Config = Config()
        }
    }
    
    
    internal fun includeConfig(configuration: KaiheilaComponentBotConfiguration) {
        configuration.botConfiguration.isCompress = config.isCompress
        configuration.syncPeriods = config.syncPeriods
    }
    
}












