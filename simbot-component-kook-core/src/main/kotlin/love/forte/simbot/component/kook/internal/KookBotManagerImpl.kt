/*
 * Copyright (c) 2022-2023. ForteScarlet.
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

package love.forte.simbot.component.kook.internal

import kotlinx.coroutines.*
import love.forte.simbot.ID
import love.forte.simbot.bot.BotAlreadyRegisteredException
import love.forte.simbot.component.kook.*
import love.forte.simbot.component.kook.event.KookBotRegisteredEvent
import love.forte.simbot.component.kook.internal.event.KookBotRegisteredEventImpl
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.event.pushIfProcessable
import love.forte.simbot.kook.KookBot
import love.forte.simbot.kook.kookBot
import love.forte.simbot.literal
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext


/**
 *
 * 【
 *
 * @author ForteScarlet
 */
internal class KookBotManagerImpl(
    private val eventProcessor: EventProcessor,
    override val configuration: KookBotManagerConfiguration,
    override val component: KookComponent,
) : KookBotManager() {
    override val coroutineContext: CoroutineContext
    private val job: CompletableJob
    
    private val bots = ConcurrentHashMap<String, KookComponentBotImpl>()
    
    init {
        val parentContext = configuration.parentCoroutineContext
        job = SupervisorJob(parentContext[Job])
        coroutineContext = parentContext + job
    }
    
    override fun register(
        ticket: KookBot.Ticket,
        configuration: KookComponentBotConfiguration,
    ): KookComponentBot {
        return bots.compute(ticket.clientId.literal) { key, old ->
            if (old != null) throw BotAlreadyRegisteredException(key)
            val kkBot = kookBot(ticket, configuration.botConfiguration)
            KookComponentBotImpl(kkBot, this, eventProcessor, component, configuration)
        }!!.also { bot ->
            launch {
                // push event
                eventProcessor.pushIfProcessable(KookBotRegisteredEvent) {
                    KookBotRegisteredEventImpl(bot)
                }
            }
            
        }
    }
    
    
    override fun all(): List<KookComponentBot> = bots.values.toList()
    
    override suspend fun doCancel(reason: Throwable?): Boolean {
        if (job.isCancelled) return false
        if (reason == null) {
            job.cancel()
        } else {
            job.cancel(CancellationException(reason.localizedMessage, reason))
        }
        job.join()
        return true
    }
    
    override fun get(id: ID): KookComponentBot? = bots[id.literal]
    
    override fun invokeOnCompletion(handler: CompletionHandler) {
        job.invokeOnCompletion(handler)
    }
    
    override suspend fun join() {
        job.join()
    }
    
    override suspend fun start(): Boolean = true
    
    override val isActive: Boolean get() = job.isActive
    override val isCancelled: Boolean get() = job.isCancelled
    override val isStarted: Boolean get() = true
    
}
