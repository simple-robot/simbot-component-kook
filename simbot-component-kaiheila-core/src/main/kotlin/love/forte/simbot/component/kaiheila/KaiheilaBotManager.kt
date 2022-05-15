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
import love.forte.simbot.application.EventProviderFactory
import love.forte.simbot.component.kaiheila.internal.KaiheilaBotManagerImpl
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.kaiheila.KaiheilaBot
import love.forte.simbot.kaiheila.KaiheilaBotConfiguration
import love.forte.simbot.kaiheila.SimpleTicket
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 *
 * [KaiheilaComponentBot] 的管理器实现。
 *
 * @author ForteScarlet
 */
@Suppress("MemberVisibilityCanBePrivate")
public abstract class KaiheilaBotManager : BotManager<KaiheilaComponentBot>() {
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
                "[{}] mismatch: [{}] != [{}]",
                verifyInfo.name,
                component,
                currentComponent
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
    public abstract fun register(
        ticket: KaiheilaBot.Ticket,
        configuration: KaiheilaComponentBotConfiguration,
    ): KaiheilaComponentBot
    
    
    /**
     * 通过 [clientId]、 [token] 和 [configuration] 注册bot。
     */
    public fun register(
        clientId: ID,
        token: String,
        configuration: KaiheilaComponentBotConfiguration,
    ): KaiheilaComponentBot {
        return register(SimpleTicket(clientId, token), configuration)
    }
    
    /**
     * 通过 [ticket] 和 [block] 注册bot。
     */
    public fun register(
        ticket: KaiheilaBot.Ticket,
        block: KaiheilaComponentBotConfiguration.() -> Unit = {},
    ): KaiheilaComponentBot {
        return register(ticket, KaiheilaComponentBotConfiguration(KaiheilaBotConfiguration()).also(block))
    }
    
    
    /**
     * 通过 [clientId]、 [token] 和 [block] 注册bot。
     */
    public fun register(
        clientId: ID,
        token: String,
        block: KaiheilaComponentBotConfiguration.() -> Unit = {},
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
            
            val config = KaiheilaBotManagerConfigurationImpl().also(configurator)
            
            return KaiheilaBotManagerImpl(eventProcessor, config, component).also {
                config.useBotManager(it)
            }
        }
        
        /**
         * 通过 [EventProcessor] 构建bot管理器并使用默认配置。
         */
        @Suppress("DeprecatedCallableAddReplaceWith")
        @OptIn(InternalSimbotApi::class)
        @JvmStatic
        @Deprecated("Use Factory.")
        public fun newInstance(eventProcessor: EventProcessor): KaiheilaBotManager {
            return KaiheilaBotManagerImpl(eventProcessor, KaiheilaBotManagerConfigurationImpl(), KaiheilaComponent())
        }
        
        /**
         * 提供配置类并构建bot管理器实例。
         */
        @Suppress("DeprecatedCallableAddReplaceWith")
        @OptIn(InternalSimbotApi::class)
        @JvmStatic
        @Deprecated("Use Factory.")
        public fun newInstance(
            eventProcessor: EventProcessor,
            configuration: KaiheilaBotManagerConfiguration,
        ): KaiheilaBotManager {
            return KaiheilaBotManagerImpl(eventProcessor, configuration, KaiheilaComponent())
        }
        
        
    }
    
    
}

/**
 * 配置并构建一个 [KaiheilaBotManager] 实例。
 *
 */
@Deprecated("Use Factory in application")
public fun kaiheilaBotManager(
    eventProcessor: EventProcessor,
    block: KaiheilaBotManagerConfiguration.() -> Unit = {},
): KaiheilaBotManager {
    
    return KaiheilaBotManager.newInstance(eventProcessor, KaiheilaBotManagerConfigurationImpl().also(block))
}


/**
 * [KaiheilaBotManager] 使用的配置类。
 */
public interface KaiheilaBotManagerConfiguration {
    /**
     * bot管理器中为所有bot分配的父级协程上下文。
     *
     * 如果其中存在 [kotlinx.coroutines.Job], 则会被作为parentJob使用。
     */
    public var parentCoroutineContext: CoroutineContext
    
    // TODO register bot
    
}


private class KaiheilaBotManagerConfigurationImpl : KaiheilaBotManagerConfiguration {
    override var parentCoroutineContext: CoroutineContext = EmptyCoroutineContext
    
    
    fun useBotManager(botManager: KaiheilaBotManager) {
        // TODO
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
    val clientId: CharSequenceID,
    
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
        configuration.syncPeriods = config.syncPeriods
    }

}












