package love.forte.simbot.component.kaihieila.internal

import kotlinx.coroutines.*
import love.forte.simbot.*
import love.forte.simbot.component.kaihieila.*
import love.forte.simbot.kaiheila.*
import java.util.concurrent.*
import kotlin.coroutines.*


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

    override fun register(ticket: KaiheilaBot.Ticket, configuration: KaiheilaComponentBotConfiguration): KaiheilaComponentBot {
        return bots.compute(ticket.clientId.literal) { key, old ->
            if (old != null) throw BotAlreadyRegisteredException(key)
            val khlBot = kaiheilaBot(ticket, configuration.botConfiguration)
            KaiheilaComponentBotImpl(khlBot, this, eventProcessor, component, configuration)
        }!!
    }


    override fun all(): Sequence<KaiheilaComponentBot> = bots.values.asSequence()

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