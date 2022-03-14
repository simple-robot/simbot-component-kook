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

package love.forte.simbot.component.kaihieila.internal

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import love.forte.simbot.*
import love.forte.simbot.component.kaihieila.*
import love.forte.simbot.component.kaihieila.KaiheilaComponent.Registrar.botUserStatus
import love.forte.simbot.component.kaihieila.KaiheilaComponent.Registrar.normalUserStatus
import love.forte.simbot.component.kaihieila.util.*
import love.forte.simbot.definition.*
import love.forte.simbot.definition.Role
import love.forte.simbot.kaiheila.api.guild.*
import love.forte.simbot.kaiheila.objects.User
import java.util.concurrent.atomic.*
import java.util.stream.*
import kotlin.coroutines.*
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.*

/**
 *
 * @author ForteScarlet
 */
internal class KaiheilaMemberImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val guild: KaiheilaGuildImpl,
    override val source: User
) : KaiheilaGuildMember, CoroutineScope {
    internal val job = SupervisorJob(guild.job)
    override val coroutineContext: CoroutineContext = guild.coroutineContext + job

    @Volatile
    @Suppress("unused")
    private var _muteJob: Job? = null

    // private val muteJob = atomic<Job?>(null)


    // private val muteJobUpdater =
    //     AtomicReferenceFieldUpdater.newUpdater(KaiheilaMemberImpl::class.java, Job::class.java, "muteJob")


    override var nickname: String = source.nickname ?: ""
        internal set
    override val username: String = source.username
    override val avatar: String = source.avatar
    override val status: UserStatus = if (source.isBot) botUserStatus else normalUserStatus

    override val id: ID
        get() = source.id

    override suspend fun unmute(type: Int): Boolean {
        // do unmute
        val result = GuildMuteDeleteRequest(guild.id, source.id, type).requestBy(bot)
        return result.isSuccess.also { success ->
            if (success) {
                // remove delete job
                MUTE_JOB_ATOMIC.update(this) { cur ->
                    cur?.cancel()
                    null
                }
                // muteJob.update { cur ->
                //     cur?.cancel()
                //     null
                // }
            }
        }
    }

    override suspend fun mute(duration: Duration, type: Int): Boolean {
        // do mute
        val milliseconds = duration.inWholeMilliseconds
        if (milliseconds == 0L) {
            return unmute(type)
        }

        val result = GuildMuteCreateRequest(guild.id, source.id, type).requestBy(bot)
        return result.isSuccess.also { success ->
            if (milliseconds > 0 && success) {
                val scope: CoroutineScope = this
                MUTE_JOB_ATOMIC.update(this) { cur ->
                    cur?.cancel()
                    scope.launch {
                        delay(milliseconds)
                        unmute(type)
                    }.also {
                        it.invokeOnCompletion { e ->
                            if (e is CancellationException) {
                                logger.debug("Member({}) from Bot({}) unmute job cancelled.", source.id, bot.id)
                            }
                        }
                    }
                }
            }
        }


    }

    @Api4J
    override val roles: Stream<out Role>
        get() = TODO("Not yet implemented")

    override suspend fun roles(): Flow<Role> {
        TODO("Not yet implemented")
    }


    override fun toString(): String {
        return "KaiheilaMember(source=$source)"
    }

    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger("love.forte.simbot.component.kaihieila.KaiheilaMember")
        private val MUTE_JOB_ATOMIC =
            AtomicReferenceFieldUpdater.newUpdater(KaiheilaMemberImpl::class.java, Job::class.java, "_muteJob")
    }

}