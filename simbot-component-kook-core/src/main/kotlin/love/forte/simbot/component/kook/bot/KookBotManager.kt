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

package love.forte.simbot.component.kook.bot

import love.forte.simbot.Attribute
import love.forte.simbot.Component
import love.forte.simbot.NoSuchComponentException
import love.forte.simbot.application.ApplicationConfiguration
import love.forte.simbot.application.EventProviderAutoRegistrarFactory
import love.forte.simbot.application.EventProviderFactory
import love.forte.simbot.attribute
import love.forte.simbot.bot.*
import love.forte.simbot.component.kook.KookComponent
import love.forte.simbot.component.kook.bot.internal.KookBotManagerImpl
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.kook.BotConfiguration
import love.forte.simbot.kook.Ticket
import love.forte.simbot.logger.LoggerFactory
import kotlin.coroutines.CoroutineContext

/**
 * [KookBot] 注册器。主要由 [KookBotManager] 实现。
 *
 * @author ForteScarlet
 */
public interface KookBotRegistrar {

    /**
     * 通过 [ticket] 和 [configuration] 注册bot。
     *
     * @throws BotAlreadyRegisteredException 已经存在注册的相同 `clientId` 的 bot 时
     */
    public fun register(
        ticket: Ticket,
        configuration: KookBotConfiguration,
    ): KookBot

    /**
     * 通过 [clientId]、 [token] 和 [configuration] 注册 ws bot。
     *
     * @throws BotAlreadyRegisteredException 已经存在注册的相同 `clientId` 的 bot 时
     */
    public fun registerWs(
        clientId: String,
        token: String,
        configuration: KookBotConfiguration,
    ): KookBot = register(Ticket.botWsTicket(clientId, token), configuration)

    /**
     * 通过 [ticket] 和 [block] 注册bot。
     *
     * @throws BotAlreadyRegisteredException 已经存在注册的相同 `clientId` 的 bot 时
     */
    public fun register(
        ticket: Ticket,
        block: KookBotConfiguration.() -> Unit = {},
    ): KookBot

    /**
     * 通过 [clientId]、 [token] 和 [block] 注册 ws bot。
     *
     * @throws BotAlreadyRegisteredException 已经存在注册的相同 `clientId` 的 bot 时
     */
    public fun registerWs(
        clientId: String,
        token: String,
        block: KookBotConfiguration.() -> Unit = {},
    ): KookBot = register(Ticket.botWsTicket(clientId, token), block)


}


/**
 * [KookBot] 的 [BotManager] 实现。
 *
 * @author ForteScarlet
 */
public abstract class KookBotManager : BotManager<KookBot>(), KookBotRegistrar {
    abstract override val component: Component
    abstract override val coroutineContext: CoroutineContext
    public abstract val configuration: KookBotManagerConfiguration

    override fun register(verifyInfo: BotVerifyInfo): Bot {
        val serializer = KookBotVerifyInfoConfiguration.serializer()

        val componentId = verifyInfo.componentId
        val currentComponent = this.component.id

        if (componentId != currentComponent) {
            logger.debug(
                "[{}] mismatch: [{}] != [{}]", verifyInfo.name, component, currentComponent
            )
            throw ComponentMismatchException("[$component] != [$currentComponent]")
        }

        val botInfo = verifyInfo.decode(serializer)

        logger.debug("[{}] config loaded: {}", verifyInfo.name, botInfo)

        return register(botInfo.ticket, botInfo::includeConfig)
    }


    override fun register(ticket: Ticket, block: KookBotConfiguration.() -> Unit): KookBot =
        register(ticket, KookBotConfiguration(BotConfiguration()).also(block))


    public companion object Factory : EventProviderFactory<KookBotManager, KookBotManagerConfiguration> {
        private const val FACTORY_KEY_NAME: String = "simbot.kook"
        override val key: Attribute<KookBotManager> = attribute(FACTORY_KEY_NAME)
        private val logger = LoggerFactory.getLogger(KookBotManager::class.java)

        /**
         * 通过各属性构建 [KookBotManager] 实例。
         */
        override suspend fun create(
            eventProcessor: EventProcessor,
            components: List<Component>,
            applicationConfiguration: ApplicationConfiguration,
            configurator: KookBotManagerConfiguration.() -> Unit
        ): KookBotManager {
            val component = components.find { it.id == KookComponent.ID_VALUE && it is KookComponent } as? KookComponent
                ?: throw NoSuchComponentException("Component(id=${KookComponent.ID_VALUE}) type of KookComponent")

            val config = KookBotManagerConfiguration().also {
                it.coroutineContext = applicationConfiguration.coroutineContext
                configurator(it)
            }

            return KookBotManagerImpl(eventProcessor, config, component)
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
