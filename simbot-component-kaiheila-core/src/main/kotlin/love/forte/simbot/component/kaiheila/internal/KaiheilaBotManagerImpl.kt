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

package love.forte.simbot.component.kaiheila.internal

import kotlinx.coroutines.*
import love.forte.simbot.BotAlreadyRegisteredException
import love.forte.simbot.ComponentMismatchException
import love.forte.simbot.ID
import love.forte.simbot.component.kaiheila.*
import love.forte.simbot.component.kaiheila.event.KaiheilaBotRegisteredEvent
import love.forte.simbot.component.kaiheila.internal.event.KaiheilaBotRegisteredEventImpl
import love.forte.simbot.event.pushIfProcessable
import love.forte.simbot.kaiheila.KaiheilaBot
import love.forte.simbot.kaiheila.kaiheilaBot
import love.forte.simbot.literal
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext


/**
 *
 * @author ForteScarlet
 */
internal class KaiheilaBotManagerImpl(
    override val configuration: KaiheilaBotManagerConfiguration
) : KaiheilaBotManager() {
    private val eventProcessor = configuration.eventProcessor
    private val job: CompletableJob
    override val coroutineContext: CoroutineContext
    override val component: KaiheilaComponent =
        eventProcessor.getComponent(KaiheilaComponent.ID_VALUE) as? KaiheilaComponent
            ?: throw ComponentMismatchException("The component['${KaiheilaComponent.ID_VALUE}'] cannot cast to [love.forte.simbot.component.kaiheila.KaiheilaComponent]")

    private val bots = ConcurrentHashMap<String, KaiheilaComponentBotImpl>()

    init {
        val parentContext = configuration.parentCoroutineContext
        val parentJob = parentContext[Job]
        job = SupervisorJob(parentJob)
        coroutineContext = parentContext.minusKey(Job) + job
    }

    override fun register(
        ticket: KaiheilaBot.Ticket,
        configuration: KaiheilaComponentBotConfiguration
    ): KaiheilaComponentBot {
        return bots.compute(ticket.clientId.literal) { key, old ->
            if (old != null) throw BotAlreadyRegisteredException(key)
            val khlBot = kaiheilaBot(ticket, configuration.botConfiguration)
            KaiheilaComponentBotImpl(khlBot, this, eventProcessor, component, configuration)
        }!!.also { bot ->
            launch {
                // push event
                eventProcessor.pushIfProcessable(KaiheilaBotRegisteredEvent) {
                    KaiheilaBotRegisteredEventImpl(bot)
                }
            }

        }
    }


    override fun all(): List<KaiheilaComponentBot> = bots.values.toList()

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

    override fun get(id: ID): KaiheilaComponentBot? = bots[id.literal]

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