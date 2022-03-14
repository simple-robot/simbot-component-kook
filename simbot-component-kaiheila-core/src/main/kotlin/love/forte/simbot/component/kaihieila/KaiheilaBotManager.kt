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

import kotlinx.serialization.*
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
@Suppress("MemberVisibilityCanBePrivate")
public abstract class KaiheilaBotManager : BotManager<KaiheilaComponentBot>() {
    abstract override val component: KaiheilaComponent
    public abstract val configuration: KaiheilaBotManagerConfiguration

    /**
     * 通过 `.bot` 的json配置文件注册一个bot信息。
     */
    @OptIn(ExperimentalSerializationApi::class)
    override fun register(verifyInfo: BotVerifyInfo): KaiheilaComponentBot {
        val serializer = KaiheilaBotVerifyInfo.serializer()

        val botInfo = verifyInfo.inputStream().use { inp -> registerJson.decodeFromStream(serializer, inp) }

        val component =
            botInfo.component ?: throw NoSuchComponentException("Component is not found in [${verifyInfo.infoName}]")

        logger.debug("[{}] json element load: {}", verifyInfo.infoName, botInfo)

        this.component.id.literal
        if (component != KaiheilaComponent.ID_VALUE) {
            logger.debug(
                "[{}] mismatch: [{}] != [{}]",
                verifyInfo.infoName,
                component,
                KaiheilaComponent.ID_VALUE
            )
            throw ComponentMismatchException("[$component] != [${KaiheilaComponent.ID_VALUE}]")
        }

        // no config
        return register(botInfo.clientId, botInfo.token, botInfo::includeConfig)

    }

    /**
     * 通过 [ticket] 和 [configuration] 注册bot。
     */
    public abstract fun register(
        ticket: KaiheilaBot.Ticket,
        configuration: KaiheilaComponentBotConfiguration
    ): KaiheilaComponentBot


    /**
     * 通过 [clientId]、 [token] 和 [configuration] 注册bot。
     */
    public fun register(clientId: ID, token: String, configuration: KaiheilaComponentBotConfiguration): KaiheilaComponentBot {
        return register(SimpleTicket(clientId, token), configuration)
    }

    /**
     * 通过 [ticket] 和 [block] 注册bot。
     */
    public fun register(
        ticket: KaiheilaBot.Ticket,
        block: KaiheilaComponentBotConfiguration.() -> Unit = {}
    ): KaiheilaComponentBot {
        return register(ticket, KaiheilaComponentBotConfiguration(KaiheilaBotConfiguration()).also(block))
    }


    /**
     * 通过 [clientId]、 [token] 和 [block] 注册bot。
     */
    public fun register(
        clientId: ID,
        token: String,
        block: KaiheilaComponentBotConfiguration.() -> Unit = {}
    ): KaiheilaComponentBot {
        return register(clientId, token, KaiheilaComponentBotConfiguration(KaiheilaBotConfiguration()).also(block))
    }


    public companion object {
        private val logger = LoggerFactory.getLogger(KaiheilaBotManager::class.java)
        private val registerJson = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }

        /**
         * 通过 [EventProcessor] 构建bot管理器并使用默认配置。
         */
        @JvmStatic
        public fun newInstance(eventProcessor: EventProcessor): KaiheilaBotManager {
            return KaiheilaBotManagerImpl(KaiheilaBotManagerConfiguration(eventProcessor))
        }

        /**
         * 提供配置类并构建bot管理器实例。
         */
        @JvmStatic
        public fun newInstance(configuration: KaiheilaBotManagerConfiguration): KaiheilaBotManager {
            return KaiheilaBotManagerImpl(configuration)
        }


    }


}

/**
 * 配置并构建一个 [KaiheilaBotManager] 实例。
 *
 */
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


/**
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
@Serializable
private data class KaiheilaBotVerifyInfo(
    val component: String? = null,

    /**
     * client id
     */
    val clientId: CharSequenceID,

    /**
     * token
     */
    val token: String,

    /**
     * 是否压缩数据
     */
    val isCompress: Boolean = true,

    /**
     * 缓存对象信息的同步周期
     */
    val syncPeriods: KaiheilaComponentBotConfiguration.SyncPeriods = KaiheilaComponentBotConfiguration.SyncPeriods()

) {
    fun includeConfig(configuration: KaiheilaComponentBotConfiguration) {
        configuration.syncPeriods = syncPeriods
    }
}












