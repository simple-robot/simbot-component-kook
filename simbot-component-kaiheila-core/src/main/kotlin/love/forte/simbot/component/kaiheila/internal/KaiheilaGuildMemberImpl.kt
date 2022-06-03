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
import love.forte.simbot.Api4J
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.component.kaiheila.KaiheilaComponent.Factory.botUserStatus
import love.forte.simbot.component.kaiheila.KaiheilaComponent.Factory.normalUserStatus
import love.forte.simbot.component.kaiheila.KaiheilaGuildMember
import love.forte.simbot.component.kaiheila.KaiheilaUserChat
import love.forte.simbot.component.kaiheila.message.KaiheilaMessageCreatedReceipt
import love.forte.simbot.component.kaiheila.message.KaiheilaMessageReceipt
import love.forte.simbot.component.kaiheila.model.UserModel
import love.forte.simbot.component.kaiheila.util.requestBy
import love.forte.simbot.component.kaiheila.util.update
import love.forte.simbot.definition.UserStatus
import love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest
import love.forte.simbot.kaiheila.api.guild.GuildMuteDeleteRequest
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration

/**
 *
 * @author ForteScarlet
 */
internal class KaiheilaGuildMemberImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val guild: KaiheilaGuildImpl,
    @Volatile override var source: UserModel,
) : KaiheilaGuildMember, CoroutineScope {
    private val job = SupervisorJob(guild.job)
    override val coroutineContext: CoroutineContext = guild.coroutineContext + job
    
    @Volatile
    @Suppress("unused")
    private var _muteJob: Job? = null
    
    
    override val nickname: String get() = source.nickname ?: ""
    
    override val username: String = source.username
    override val avatar: String = source.avatar
    override val status: UserStatus = if (source.isBot) botUserStatus else normalUserStatus
    
    override val id: ID
        get() = source.id
    
    // region mute相关
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
    // endregion
    
    
    // region send 相关
    // 暂时等同于向好友发送消息
    
    @OptIn(ExperimentalSimbotApi::class)
    private suspend fun asFriend(): KaiheilaUserChat = bot.friend(id)
    
    @Api4J
    @OptIn(ExperimentalSimbotApi::class)
    private fun asFriendBlocking(): KaiheilaUserChat = bot.getFriend(id)
    
    
    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun send(text: String): KaiheilaMessageCreatedReceipt {
        return asFriend().send(text)
    }
    
    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun send(message: Message): KaiheilaMessageReceipt {
        return asFriend().send(message)
    }
    
    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun send(message: MessageContent): KaiheilaMessageReceipt {
        return asFriend().send(message)
    }
    
    @Api4J
    @OptIn(ExperimentalSimbotApi::class)
    override fun sendBlocking(text: String): KaiheilaMessageCreatedReceipt {
        return asFriendBlocking().sendBlocking(text)
    }
    
    @Api4J
    @OptIn(ExperimentalSimbotApi::class)
    override fun sendBlocking(message: Message): KaiheilaMessageReceipt {
        return asFriendBlocking().sendBlocking(message)
    }
    
    @Api4J
    @OptIn(ExperimentalSimbotApi::class)
    override fun sendBlocking(message: MessageContent): KaiheilaMessageReceipt {
        return asFriendBlocking().sendBlocking(message)
    }
    // endregion
    
    
    
    override fun toString(): String {
        return "KaiheilaMember(source=$source)"
    }
    
    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger("love.forte.simbot.component.kaiheila.KaiheilaMember")
        private val MUTE_JOB_ATOMIC =
            AtomicReferenceFieldUpdater.newUpdater(KaiheilaGuildMemberImpl::class.java, Job::class.java, "_muteJob")
    }
    
}
