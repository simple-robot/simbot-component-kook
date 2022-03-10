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
import kotlinx.coroutines.sync.*
import love.forte.simbot.*
import love.forte.simbot.component.kaihieila.*
import love.forte.simbot.definition.*
import love.forte.simbot.event.*
import love.forte.simbot.kaiheila.*
import love.forte.simbot.kaiheila.api.user.*
import love.forte.simbot.kaiheila.event.Event.Extra.Sys
import love.forte.simbot.kaiheila.event.Event.Extra.Text
import love.forte.simbot.kaiheila.event.system.user.*
import love.forte.simbot.message.*
import love.forte.simbot.resources.*
import love.forte.simbot.utils.*
import org.slf4j.*
import org.slf4j.LoggerFactory
import java.util.concurrent.*
import java.util.stream.*
import kotlin.coroutines.*
import love.forte.simbot.kaiheila.event.Event as KhlEvent

/**
 *
 * @author ForteScarlet
 */
internal class KaiheilaComponentBotImpl(
    override val sourceBot: KaiheilaBot,
    override val manager: KaiheilaBotManager,
    override val eventProcessor: EventProcessor,
    override val component: KaiheilaComponent,
) : KaiheilaComponentBot() {
    private val job = SupervisorJob(sourceBot.coroutineContext[Job]!!)
    override val coroutineContext: CoroutineContext = sourceBot.coroutineContext + job
    override val logger: Logger =
        LoggerFactory.getLogger("love.forte.simbot.component.kaiheila.bot.${sourceBot.ticket.clientId}")


    init {
        // register some event processors
        sourceBot.processor { _, decoded ->
            decoded().internalProcessor()
        }

        // register standard event processors

    }


    //region internal event process
    private suspend fun KhlEvent<*>.internalProcessor() {
        when (val ex = extra) {
            // 系统事件
            is Sys<*> -> {
                when (val body = ex.body) {
                    is UserUpdatedEventBody -> {
                        // 用户信息更新
                        if (body.userId == me.id) {
                            updateMe(me.copy(username = body.username, avatar = body.avatar))
                        }

                    }
                }
            }

            // Text.
            is Text -> {
            }
        }
    }
    //endregion


    override val isActive: Boolean
        get() = job.isActive

    override val isCancelled: Boolean
        get() = job.isCancelled

    override val isStarted: Boolean
        get() = sourceBot.isStarted

    override val status: UserStatus
        get() = botStatus

    override suspend fun join() {
        sourceBot.join()
    }

    override suspend fun cancel(reason: Throwable?): Boolean {
        if (job.isCancelled) return false
        sourceBot.cancel(reason)
        return true
    }

    //region me and isMe
    @Volatile
    private lateinit var me: Me

    private val meLock = Mutex()

    private suspend fun updateMe(newMe: Me) = meLock.withLock {
        me = newMe
    }

    override fun isMe(id: ID): Boolean {
        if (id == this.id) return true
        if (::me.isInitialized && me.id == id) return true
        return false
    }
    //endregion

    override val avatar: String
        get() = me.avatar

    override val username: String
        get() = me.username


    /**
     * 启动bot。
     */
    override suspend fun start(): Boolean = sourceBot.start().also {
        updateMe(sourceBot.me())
        if (!it) {
            return@also
        }
    }


    //region guild api
    override suspend fun guild(id: ID): KaiheilaGuild? {
        TODO("Not yet implemented")
    }

    override suspend fun guilds(grouping: Grouping, limiter: Limiter): Flow<KaiheilaGuild> {
        TODO("Not yet implemented")
    }

    @Api4J
    override fun getGuild(id: ID): KaiheilaGuild? {
        TODO("Not yet implemented")
    }

    @Api4J
    override fun getGuilds(grouping: Grouping, limiter: Limiter): Stream<out KaiheilaGuild> {
        TODO("Not yet implemented")
    }
    //endregion


    //region friend api
    override suspend fun friend(id: ID): Friend? {
        TODO("Not yet implemented")
    }

    override suspend fun friends(grouping: Grouping, limiter: Limiter): Flow<Friend> {
        TODO("Not yet implemented")
    }

    @Api4J
    override fun getFriends(grouping: Grouping, limiter: Limiter): Stream<out Friend> {
        TODO("Not yet implemented")
    }
    //endregion


    //region image api
    override suspend fun resolveImage(id: ID): Image<*> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadImage(resource: Resource): Image<*> {
        TODO("Not yet implemented")
    }
    //endregion


    /**
     * 禁言延时任务
     */
    internal val muteJobs = ConcurrentHashMap<String, Job>()


    companion object {
        val botStatus = UserStatus.builder().bot().fakeUser().build()
    }
}

internal fun love.forte.simbot.kaiheila.objects.User.asMember(
    bot: KaiheilaComponentBotImpl,
    guild: LazyValue<KaiheilaGuildImpl>
): KaiheilaGuildMemberImpl {
    return KaiheilaGuildMemberImpl(bot, guild, this)
}

internal fun love.forte.simbot.kaiheila.objects.User.asMember(
    bot: KaiheilaComponentBotImpl,
    guild: KaiheilaGuildImpl
): KaiheilaGuildMemberImpl {
    return KaiheilaGuildMemberImpl(bot, completedLazyValue(guild), this)
}


