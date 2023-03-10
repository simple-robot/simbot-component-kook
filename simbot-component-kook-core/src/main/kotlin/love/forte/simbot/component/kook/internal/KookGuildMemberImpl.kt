/*
 *  Copyright (c) 2022-2023 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.component.kook.internal

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.Simbot
import love.forte.simbot.component.kook.KookGuild
import love.forte.simbot.component.kook.KookGuildMember
import love.forte.simbot.component.kook.KookUserChat
import love.forte.simbot.component.kook.internal.role.KookGuildRoleImpl
import love.forte.simbot.component.kook.internal.role.KookMemberRoleImpl
import love.forte.simbot.component.kook.message.KookMessageCreatedReceipt
import love.forte.simbot.component.kook.message.KookMessageReceipt
import love.forte.simbot.component.kook.model.UserModel
import love.forte.simbot.component.kook.role.KookMemberRole
import love.forte.simbot.component.kook.util.requestBy
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.kook.api.guild.GuildMuteCreateRequest
import love.forte.simbot.kook.api.guild.GuildMuteDeleteRequest
import love.forte.simbot.kook.api.user.UserViewRequest
import love.forte.simbot.literal
import love.forte.simbot.logger.LoggerFactory
import love.forte.simbot.logger.logger
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.utils.item.Items
import love.forte.simbot.utils.item.effectedItemsByFlow
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

/**
 *
 * @author ForteScarlet
 */
internal class KookGuildMemberImpl(
    override val bot: KookComponentBotImpl,
    internal val guildInternal: KookGuildImpl,
    @Volatile override var source: UserModel,
) : KookGuildMember, CoroutineScope {
    private val job = SupervisorJob(guildInternal.job)
    override val coroutineContext: CoroutineContext = guildInternal.coroutineContext + job

    private val muteLock = Mutex()

    @Volatile
    private var muteJob: Job? = null


    override val nickname: String get() = source.nickname ?: ""
    override val username: String = source.username
    override val avatar: String = source.avatar

    override val id: ID
        get() = source.id


    /**
     * 获取当前角色在频道服务器中的全部角色。
     *
     * _Note: [roles] 尚在实验阶段，可能会在未来做出变更。_
     */
    @OptIn(ExperimentalSimbotApi::class)
    override val roles: Items<KookMemberRole>
        get() = effectedItemsByFlow {
            flow {
                val view = UserViewRequest
                    .create(source.id, guildInternal.id)
                    .requestDataBy(bot)
                val guildRoleMap = ConcurrentHashMap<String, KookGuildRoleImpl>()
                guildInternal.roles.collect { r ->
                    guildRoleMap[r.id.literal] = r
                }

                view.roles.forEach { rid ->
                    val role = guildRoleMap[rid.literal] ?: return@forEach
                    emit(KookMemberRoleImpl(this@KookGuildMemberImpl, role))
                }
            }
        }

    override suspend fun guild(): KookGuild = guildInternal

    // region mute相关
    override suspend fun unmute(type: Int): Boolean {
        // do unmute
        val result = GuildMuteDeleteRequest.create(guildInternal.id, source.id, type).requestBy(bot)
        return result.isSuccess.also { success ->
            if (success) {
                muteLock.withLock {
                    muteJob?.also {
                        muteJob = null
                    }
                }?.cancel()
            }
        }
    }

    override suspend fun mute(durationMillis: Long, type: Int): Boolean {
        Simbot.require(durationMillis > 0) { "Duration millis must > 0, but $durationMillis" }
        // do mute
        val result = GuildMuteCreateRequest.create(guildInternal.id, source.id, type).requestBy(bot)
        return result.isSuccess.also { success ->
            if (durationMillis > 0 && success) {
                val scope: CoroutineScope = this
                muteLock.withLock {
                    muteJob.also { oldJob ->
                        oldJob?.cancel()
                        muteJob = scope.launch(bot.muteDelayJob) {
                            delay(durationMillis)
                            unmute(type)
                        }.also { job ->
                            val memberId = source.id
                            val botId = bot.id
                            job.invokeOnCompletion { e ->
                                if (e is CancellationException) {
                                    logger.debug("Member({}) from Bot({}) unmute job cancelled.", memberId, botId)
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    // endregion


    // region send 相关

    private suspend fun asContact(): KookUserChat = bot.contact(id)

    override suspend fun send(text: String): KookMessageCreatedReceipt {
        return asContact().send(text)
    }

    override suspend fun send(message: Message): KookMessageReceipt {
        return asContact().send(message)
    }

    override suspend fun send(message: MessageContent): KookMessageReceipt {
        return asContact().send(message)
    }
    // endregion


    override fun toString(): String {
        return "KookGuildMemberImpl(id=$id, username=$username, source=$source)"
    }

    companion object {
        private val logger = LoggerFactory.logger<KookGuildMember>()
    }

}
