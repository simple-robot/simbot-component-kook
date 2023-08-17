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

package love.forte.simbot.component.kook.bot.internal

import kotlinx.coroutines.*
import love.forte.simbot.ID
import love.forte.simbot.bot.BotAlreadyRegisteredException
import love.forte.simbot.component.kook.KookComponent
import love.forte.simbot.component.kook.bot.KookBot
import love.forte.simbot.component.kook.bot.KookBotConfiguration
import love.forte.simbot.component.kook.bot.KookBotManager
import love.forte.simbot.component.kook.bot.KookBotManagerConfiguration
import love.forte.simbot.component.kook.event.KookBotRegisteredEvent
import love.forte.simbot.component.kook.event.internal.KookBotRegisteredEventImpl
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.event.pushIfProcessable
import love.forte.simbot.kook.BotFactory
import love.forte.simbot.kook.Ticket
import love.forte.simbot.literal
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext

/**
 *
 * @author ForteScarlet
 */
internal class KookBotManagerImpl(
    private val eventProcessor: EventProcessor,
    override val configuration: KookBotManagerConfiguration,
    override val component: KookComponent
) : KookBotManager() {
    private val supervisorJob: CompletableJob = SupervisorJob(configuration.coroutineContext[Job])
    override val coroutineContext: CoroutineContext = configuration.coroutineContext + supervisorJob

    /**
     * 记录所有注册进来的 [KookBot] .
     */
    private val botMap = ConcurrentHashMap<String, KookBotImpl>()

    override fun invokeOnCompletion(handler: CompletionHandler) {
        supervisorJob.invokeOnCompletion(handler)
    }

    override suspend fun join() {
        supervisorJob.join()
    }

    override val isActive: Boolean get() = supervisorJob.isActive
    override val isCancelled: Boolean get() = supervisorJob.isCancelled
    override val isStarted: Boolean get() = true
    override suspend fun start(): Boolean = true

    override fun all(): List<KookBot> = botMap.values.toList()

    override suspend fun doCancel(reason: Throwable?): Boolean {
        if (reason == null) {
            supervisorJob.cancel()
        } else {
            supervisorJob.cancel(CancellationException(reason.localizedMessage, reason))
        }
        botMap.clear()
        return true
    }

    override fun get(id: ID): KookBot? = botMap[id.literal]

    override fun register(ticket: Ticket, configuration: KookBotConfiguration): KookBot {
        val clientId = ticket.clientId
        fun createBot(): KookBotImpl {
            val botConfiguration = configuration.botConfiguration
            val botCoroutineContext = botConfiguration.coroutineContext
            var botJob: Job? = botCoroutineContext[Job]

            if (botJob == null) {
                botJob = SupervisorJob(this.supervisorJob)
                botConfiguration.coroutineContext =
                    this.coroutineContext.minusKey(Job) + botCoroutineContext.minusKey(Job) + botJob
            } else {
                // link to current job
                invokeOnCompletion { botJob.cancel() }
                botConfiguration.coroutineContext = this.coroutineContext.minusKey(Job) + botCoroutineContext
            }


            val sourceBot = BotFactory.create(ticket, botConfiguration)
            return KookBotImpl(eventProcessor, sourceBot, component, this, configuration)
        }

        return botMap.compute(clientId) { id, old ->
            if (old != null) {
                throw BotAlreadyRegisteredException("clientId=$id")
            }

            createBot()
        }!!.also { newBot ->
            newBot.invokeOnCompletion {
                botMap.remove(clientId, newBot)
            }

            // Publish BotRegisteredEvent
            launch {
                eventProcessor.pushIfProcessable(KookBotRegisteredEvent) {
                    KookBotRegisteredEventImpl(newBot)
                }
            }
        }
    }

}
