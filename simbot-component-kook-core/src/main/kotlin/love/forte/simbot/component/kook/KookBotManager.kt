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


package love.forte.simbot.component.kook

import kotlinx.serialization.Serializable
import love.forte.simbot.*
import love.forte.simbot.application.ApplicationConfiguration
import love.forte.simbot.application.EventProviderAutoRegistrarFactory
import love.forte.simbot.application.EventProviderFactory
import love.forte.simbot.bot.BotManager
import love.forte.simbot.bot.BotVerifyInfo
import love.forte.simbot.bot.ComponentMismatchException
import love.forte.simbot.component.kook.internal.KookBotManagerImpl
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.kook.KookBot
import love.forte.simbot.kook.KookBotConfiguration
import love.forte.simbot.kook.SimpleTicket
import love.forte.simbot.logger.LoggerFactory
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 * 在 Kook 组件中对于 [Bot][KookBot] 的注册函数的常见形式。
 *
 * @see KookBotManager
 */
public interface KookBotRegistrar {
    
    /**
     * 通过 [ticket] 和 [configuration] 注册bot。
     */
    public fun register(
        ticket: KookBot.Ticket,
        configuration: KookComponentBotConfiguration,
    ): KookComponentBot
    
    
    /**
     * 通过 [clientId]、 [token] 和 [configuration] 注册bot。
     */
    public fun register(
        clientId: String,
        token: String,
        configuration: KookComponentBotConfiguration,
    ): KookComponentBot
    
    /**
     * 通过 [ticket] 和 [block] 注册bot。
     */
    public fun register(
        ticket: KookBot.Ticket,
        block: KookComponentBotConfiguration.() -> Unit = {},
    ): KookComponentBot
    
    
    /**
     * 通过 [clientId]、 [token] 和 [block] 注册bot。
     */
    public fun register(
        clientId: String,
        token: String,
        block: KookComponentBotConfiguration.() -> Unit = {},
    ): KookComponentBot
}


/**
 *
 * [KookComponentBot] 的管理器实现。
 *
 * @author ForteScarlet
 */
@Suppress("MemberVisibilityCanBePrivate")
public abstract class KookBotManager : BotManager<KookComponentBot>(), KookBotRegistrar {
    abstract override val component: KookComponent
    public abstract val configuration: KookBotManagerConfiguration
    
    /**
     * 通过 `.bot` 的json配置文件注册一个bot信息。
     */
    override fun register(verifyInfo: BotVerifyInfo): KookComponentBot {
        val serializer = KookBotVerifyInfoConfiguration.serializer()
        
        val component = verifyInfo.componentId
        val currentComponent = this.component.id
        
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
        ticket: KookBot.Ticket,
        configuration: KookComponentBotConfiguration,
    ): KookComponentBot
    
    
    /**
     * 通过 [clientId]、 [token] 和 [configuration] 注册bot。
     */
    override fun register(
        clientId: String,
        token: String,
        configuration: KookComponentBotConfiguration,
    ): KookComponentBot {
        return register(SimpleTicket(clientId, token), configuration)
    }
    
    /**
     * 通过 [ticket] 和 [block] 注册bot。
     */
    override fun register(
        ticket: KookBot.Ticket,
        block: KookComponentBotConfiguration.() -> Unit,
    ): KookComponentBot {
        return register(ticket, KookComponentBotConfiguration(KookBotConfiguration()).also(block))
    }
    
    
    /**
     * 通过 [clientId]、 [token] 和 [block] 注册bot。
     */
    override fun register(
        clientId: String,
        token: String,
        block: KookComponentBotConfiguration.() -> Unit,
    ): KookComponentBot {
        return register(clientId, token, KookComponentBotConfiguration(KookBotConfiguration()).also(block))
    }
    
    
    public companion object Factory : EventProviderFactory<KookBotManager, KookBotManagerConfiguration> {
        override val key: Attribute<KookBotManager> = attribute("simbot.kook")
        private val logger = LoggerFactory.getLogger(KookBotManager::class.java)
    
        /**
         * 通过各项配置构建 [KookBotManager] 实例。
         */
        override suspend fun create(
            eventProcessor: EventProcessor,
            components: List<Component>,
            applicationConfiguration: ApplicationConfiguration,
            configurator: KookBotManagerConfiguration.() -> Unit,
        ): KookBotManager {
            val component = components.find { it.id == KookComponent.ID_VALUE } as? KookComponent
                ?: throw NoSuchComponentException("${KookComponent.ID_VALUE} type of KookComponent")
            
            val config = KookBotManagerConfigurationImpl().also {
                val currentContext = applicationConfiguration.coroutineContext
                it.parentCoroutineContext = currentContext
                configurator(it)
            }
            
            
            return KookBotManagerImpl(eventProcessor, config, component).also {
                config.useBotManager(it)
            }
        }
        
        @Suppress("DeprecatedCallableAddReplaceWith")
        @OptIn(InternalSimbotApi::class)
        @JvmStatic
        @Deprecated("Use Factory in Application.")
        public fun newInstance(eventProcessor: EventProcessor): KookBotManager {
            return KookBotManagerImpl(
                eventProcessor,
                KookBotManagerConfigurationImpl(),
                KookComponent()
            )
        }
        
        @Suppress("DeprecatedCallableAddReplaceWith")
        @OptIn(InternalSimbotApi::class)
        @JvmStatic
        @Deprecated("Use Factory in Application.")
        public fun newInstance(
            eventProcessor: EventProcessor,
            configuration: KookBotManagerConfiguration,
        ): KookBotManager {
            return KookBotManagerImpl(eventProcessor, configuration, KookComponent())
        }
        
        
    }
    
    
}


/**
 * 实现 [EventProviderAutoRegistrarFactory] 并通过 `Java SPI`
 * 支持 [KookBotManager] 的自动安装。
 */
public class KookBotManagerAutoRegistrarFactory :
    EventProviderAutoRegistrarFactory<KookBotManager, KookBotManagerConfiguration> {
    override val registrar: KookBotManager.Factory
        get() = KookBotManager
}


/**
 * 配置并构建一个 [KookBotManager] 实例。
 *
 */
@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated("Use Factory in application")
public fun kookBotManager(
    eventProcessor: EventProcessor,
    block: KookBotManagerConfiguration.() -> Unit = {},
): KookBotManager {
    
    @Suppress("DEPRECATION") return KookBotManager.newInstance(
        eventProcessor,
        KookBotManagerConfigurationImpl().also(block)
    )
}


/**
 * [KookBotManager] 使用的配置类。
 */
public interface KookBotManagerConfiguration {
    
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
    @Deprecated("Use ApplicationBuilder.kookBots {...} or BotRegistrar.kook { ... }")
    public fun register(
        ticket: KookBot.Ticket,
        configuration: KookComponentBotConfiguration,
        onBot: suspend (KookComponentBot) -> Unit,
    )
    
    /**
     * 通过 [clientId]、 [token] 和 [configuration] 注册bot。
     */
    @Deprecated("Use ApplicationBuilder.kookBots {...} or BotRegistrar.kook { ... }")
    public fun register(
        clientId: String,
        token: String,
        configuration: KookComponentBotConfiguration,
        onBot: suspend (KookComponentBot) -> Unit,
    )
    
    /**
     * 通过 [ticket] 和 [block] 注册bot。
     */
    @Deprecated("Use ApplicationBuilder.kookBots {...} or BotRegistrar.kook { ... }")
    public fun register(
        ticket: KookBot.Ticket,
        block: KookComponentBotConfiguration.() -> Unit = {},
        onBot: suspend (KookComponentBot) -> Unit,
    )
    
    /**
     * 通过 [clientId]、 [token] 和 [block] 注册bot。
     */
    @Deprecated("Use ApplicationBuilder.kookBots {...} or BotRegistrar.kook { ... }")
    public fun register(
        clientId: String,
        token: String,
        block: KookComponentBotConfiguration.() -> Unit = {},
        onBot: suspend (KookComponentBot) -> Unit,
    )
}


private class KookBotManagerConfigurationImpl : KookBotManagerConfiguration {
    override var parentCoroutineContext: CoroutineContext = EmptyCoroutineContext
    private var managerConfig: (suspend (KookBotManager) -> Unit) = {}
    
    private fun addConfig(newConfig: suspend (KookBotManager) -> Unit) {
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
    @Suppress("OVERRIDE_DEPRECATION", "OverridingDeprecatedMember")
    override fun register(
        ticket: KookBot.Ticket,
        configuration: KookComponentBotConfiguration,
        onBot: suspend (KookComponentBot) -> Unit,
    ) {
        addConfig { m ->
            onBot(m.register(ticket, configuration))
        }
    }
    
    /**
     * 通过 [clientId]、 [token] 和 [configuration] 注册bot。
     */
    @Suppress("OVERRIDE_DEPRECATION", "OverridingDeprecatedMember")
    override fun register(
        clientId: String,
        token: String,
        configuration: KookComponentBotConfiguration,
        onBot: suspend (KookComponentBot) -> Unit,
    ) {
        addConfig { m ->
            onBot(m.register(clientId, token, configuration))
        }
    }
    
    /**
     * 通过 [ticket] 和 [block] 注册bot。
     */
    @Suppress("OVERRIDE_DEPRECATION", "OverridingDeprecatedMember")
    override fun register(
        ticket: KookBot.Ticket,
        block: KookComponentBotConfiguration.() -> Unit,
        onBot: suspend (KookComponentBot) -> Unit,
    ) {
        addConfig { m ->
            onBot(m.register(ticket, block))
        }
    }
    
    /**
     * 通过 [clientId]、 [token] 和 [block] 注册bot。
     */
    @Suppress("OVERRIDE_DEPRECATION", "OverridingDeprecatedMember")
    override fun register(
        clientId: String,
        token: String,
        block: KookComponentBotConfiguration.() -> Unit,
        onBot: suspend (KookComponentBot) -> Unit,
    ) {
        addConfig { m ->
            onBot(m.register(clientId, token, block))
        }
    }
    
    suspend fun useBotManager(botManager: KookBotManager) {
        managerConfig(botManager)
    }
}


/**
 * `.bot` 配置文件读取的配置信息实体, 用于接收从 [BotVerifyInfo] 中的序列化信息。
 *
 * 在 [KookBotVerifyInfoConfiguration] 中，[clientId] 和 [token] 为必选项，
 * 存在于当前配置属性的最外层。除了必选项以外还存在部分可选项存在于 [KookBotVerifyInfoConfiguration.Config] 类型中，
 * 作为 [config][KookBotVerifyInfoConfiguration.config] 属性使用。
 *
 * 简化json e.g.
 * ```json
 * {
 *   "component": "simbot.kook",
 *   "clientId": "Your client ID",
 *   "token": "Your ws token"
 * }
 * ```
 *
 * 完整json e.g.
 * ```json
 * {
 *  "component": "simbot.kook",
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
public data class KookBotVerifyInfoConfiguration(
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
     * 在 [KookBotVerifyInfoConfiguration] 中除了必须的bot信息以外的可选配置信息。
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
        val syncPeriods: KookComponentBotConfiguration.SyncPeriods = KookComponentBotConfiguration.SyncPeriods(),
        
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
    
    
    internal fun includeConfig(configuration: KookComponentBotConfiguration) {
        configuration.botConfiguration.isCompress = config.isCompress
        configuration.syncPeriods = config.syncPeriods
    }
    
}












