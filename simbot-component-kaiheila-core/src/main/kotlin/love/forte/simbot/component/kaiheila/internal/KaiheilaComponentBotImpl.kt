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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import love.forte.simbot.*
import love.forte.simbot.component.kaiheila.KaiheilaBotManager
import love.forte.simbot.component.kaiheila.KaiheilaComponent
import love.forte.simbot.component.kaiheila.KaiheilaComponentBot
import love.forte.simbot.component.kaiheila.KaiheilaComponentBotConfiguration
import love.forte.simbot.component.kaiheila.event.KaiheilaBotStartedEvent
import love.forte.simbot.component.kaiheila.internal.event.KaiheilaBotStartedEventImpl
import love.forte.simbot.component.kaiheila.message.KaiheilaAssetImage
import love.forte.simbot.component.kaiheila.message.KaiheilaAssetMessage
import love.forte.simbot.component.kaiheila.message.KaiheilaAssetMessage.Key.asImage
import love.forte.simbot.component.kaiheila.message.KaiheilaAssetMessage.Key.asMessage
import love.forte.simbot.component.kaiheila.message.KaiheilaSimpleAssetMessage
import love.forte.simbot.component.kaiheila.util.requestDataBy
import love.forte.simbot.definition.UserStatus
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.event.pushIfProcessable
import love.forte.simbot.kaiheila.KaiheilaBot
import love.forte.simbot.kaiheila.api.ApiResultType
import love.forte.simbot.kaiheila.api.asset.AssetCreateRequest
import love.forte.simbot.kaiheila.api.asset.AssetCreated
import love.forte.simbot.kaiheila.api.guild.GuildListRequest
import love.forte.simbot.kaiheila.api.guild.GuildViewRequest
import love.forte.simbot.kaiheila.api.message.MessageType
import love.forte.simbot.kaiheila.api.user.Me
import love.forte.simbot.kaiheila.api.user.UserViewRequest
import love.forte.simbot.kaiheila.api.userchat.UserChatCreateRequest
import love.forte.simbot.kaiheila.api.userchat.UserChatListRequest
import love.forte.simbot.kaiheila.event.Event.Extra.Sys
import love.forte.simbot.kaiheila.event.Event.Extra.Text
import love.forte.simbot.kaiheila.event.system.guild.DeletedGuildExtraBody
import love.forte.simbot.kaiheila.event.system.guild.UpdatedGuildExtraBody
import love.forte.simbot.kaiheila.event.system.guild.member.ExitedGuildEventBody
import love.forte.simbot.kaiheila.event.system.guild.member.JoinedGuildEventBody
import love.forte.simbot.kaiheila.event.system.guild.member.UpdatedGuildMemberEventBody
import love.forte.simbot.kaiheila.event.system.user.SelfExitedGuildEventBody
import love.forte.simbot.kaiheila.event.system.user.SelfJoinedGuildEventBody
import love.forte.simbot.kaiheila.event.system.user.UserUpdatedEventBody
import love.forte.simbot.resources.Resource
import love.forte.simbot.utils.runInBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Stream
import kotlin.coroutines.CoroutineContext
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
    private val configuration: KaiheilaComponentBotConfiguration
) : KaiheilaComponentBot() {
    internal val job = SupervisorJob(sourceBot.coroutineContext[Job]!!)
    override val coroutineContext: CoroutineContext = sourceBot.coroutineContext + job
    override val logger: Logger =
        LoggerFactory.getLogger("love.forte.simbot.component.kaiheila.bot.${sourceBot.ticket.clientId}")

    internal lateinit var guilds: ConcurrentHashMap<String, KaiheilaGuildImpl>
    private set


    @JvmSynthetic
    internal fun internalGuild(id: ID): KaiheilaGuildImpl? = internalGuild(id.literal)

    @JvmSynthetic
    internal fun internalGuild(id: String): KaiheilaGuildImpl? = guilds[id]


    init {
        sourceBot.preProcessor { _, decoded ->
            val decodedEvent = decoded()

            // register some event processors
            decodedEvent.internalPreProcessor()

            // register standard event processors
            /*
                事件的验证、准备是(协程下)同步的（借preProcessor的特性），
                但是事件的触发是异步的。
             */
            decodedEvent.internalProcessor()
        }

    }


    // invoke with initLock
    private suspend fun init() {
        updateMe(sourceBot.me())
        initGuilds()
        initSyncJob()
        // clearFriendCache()
    }

    /**
     * 以 flow 的方式查询guild列表
     */
    private fun requestGuilds(): Flow<love.forte.simbot.kaiheila.objects.Guild> {
        return flow {
            var page = 1
            do {
                bot.logger.debug("Sync guild data ... page {}", page)
                val guildsResult = GuildListRequest(page = page).requestDataBy(this@KaiheilaComponentBotImpl)
                page = guildsResult.meta.page + 1
                val guilds = guildsResult.items
                bot.logger.debug("{} guild data synchronized in page {}", guilds.size, page - 1)
                guilds.forEach {
                    emit(it)
                }
            } while (guilds.isNotEmpty() && guildsResult.meta.page < guildsResult.meta.pageTotal)
        }
    }

    private suspend fun initGuilds() {
        val guildsMap = ConcurrentHashMap<String, KaiheilaGuildImpl>()
        requestGuilds()
            .buffer(100)
            .map { guild ->
                KaiheilaGuildImpl(this, guild)
            }.collect {
                guildsMap[it.id.literal] = it.also { it.init() }
            }

        bot.logger.debug("Sync guild data, {} guild data have been cached.", guildsMap.size)

        this.guilds = guildsMap
    }


    private fun initSyncJob() {
        initGuildSyncJob()
        initMemberSyncJob()
    }

    @Volatile
    private var guildSyncJob: Job? = null

    private fun initGuildSyncJob() {
        guildSyncJob?.cancel()
        guildSyncJob = null

        val guildSyncPeriod = configuration.syncPeriods.guildSyncPeriod
        if (guildSyncPeriod > 0) {
            // 同步guild信息。
            suspend fun syncGuild() {
                requestGuilds().collect { guild ->
                    if (!guilds.containsKey(guild.id.literal)) {
                        val guildImpl = KaiheilaGuildImpl(this, guild).also { it.init() }
                        guilds.computeIfPresent(guild.id.literal) { _, cur ->
                            if (cur.initTimestamp >= guildImpl.initTimestamp) {
                                guildImpl.cancel()
                                cur
                            } else {
                                cur.cancel()
                                guildImpl
                            }
                        }
                    }
                }
            }

            guildSyncJob = launch {
                while (isActive) {
                    delay(guildSyncPeriod)
                    syncGuild()
                }
            }
        }
    }


    @Volatile
    private var memberSyncJob: Job? = null

    private fun initMemberSyncJob() {
        memberSyncJob?.cancel()
        memberSyncJob = null
    }


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

    private val initLock = Mutex()

    /**
     * 启动bot。
     */
    override suspend fun start(): Boolean = sourceBot.start().also {
        if (!it) {
            return@also
        }
        initLock.withLock {
            init()
            // push event
            eventProcessor.pushIfProcessable(KaiheilaBotStartedEvent) {
                KaiheilaBotStartedEventImpl(this)
            }
        }
    }


    //region guild api
    override suspend fun guild(id: ID): KaiheilaGuildImpl? = guilds[id.literal]

    override suspend fun guilds(grouping: Grouping, limiter: Limiter): Flow<KaiheilaGuildImpl> =
        guilds.values.asFlow().withLimiter(limiter)

    override fun getGuild(id: ID): KaiheilaGuildImpl? = guilds[id.literal]

    override fun getGuilds(grouping: Grouping, limiter: Limiter): Stream<out KaiheilaGuildImpl> =
        guilds.values.stream().withLimiter(limiter)
    //endregion


    //region friend api
    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun friend(id: ID): KaiheilaUserChatImpl {
        val chat = UserChatCreateRequest(id).requestDataBy(bot)
        return KaiheilaUserChatImpl(this, chat)
    }

    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun friends(grouping: Grouping, limiter: Limiter): Flow<KaiheilaUserChatImpl> {
        return UserChatListRequest.requestDataBy(this).items.asFlow().map { chat ->
            KaiheilaUserChatImpl(this, chat)
        }
    }

    @Api4J
    @OptIn(ExperimentalSimbotApi::class)
    override fun getFriends(grouping: Grouping, limiter: Limiter): Stream<out KaiheilaUserChatImpl> {
        return runInBlocking { UserChatListRequest.requestDataBy(this@KaiheilaComponentBotImpl) }
            .items.stream().map { chat -> KaiheilaUserChatImpl(this, chat) }
    }
    //endregion


    //region image api / assert api

    /**
     * 上传一个资源并得到一个 [KaiheilaAssetMessage].
     *
     * @param resource 需要上传的资源
     * @param type 在发送时所需要使用的消息类型。通常选择为 [MessageType.IMAGE]、[MessageType.FILE] 中的值，
     * 即 `2`、`3`、`4`。
     */
    @JvmSynthetic
    override suspend fun uploadAsset(resource: Resource, type: Int): KaiheilaSimpleAssetMessage {
        val asset = AssetCreateRequest(resource).requestDataBy(this)
        return asset.asMessage(type)
    }


    /**
     * 由于开黑啦中的资源不存在id，因此会直接将 [id] 视为 url 进行转化。
     */
    @OptIn(ApiResultType::class)
    @JvmSynthetic
    override suspend fun resolveImage(id: ID): KaiheilaAssetImage {
        Simbot.require(id.literal.startsWith(ASSET_PREFIX)) {
            "The id must be the resource id of the kaiheila and must start with $ASSET_PREFIX"
        }
        return KaiheilaAssetImage(AssetCreated(id.literal))
    }

    override suspend fun uploadImage(resource: Resource): KaiheilaAssetImage {
        val asset = AssetCreateRequest(resource).requestDataBy(this)
        return asset.asImage()
    }

    //endregion


    //region internal event process
    private suspend fun KhlEvent<*>.internalPreProcessor() {
        when (val ex = extra) {
            // 系统事件
            is Sys<*> -> {
                when (val body = ex.body) {

                    //region guild members
                    // 某人退出频道服务器
                    is ExitedGuildEventBody -> {
                        val guild = internalGuild(this.targetId) ?: return
                        guild.members.remove(body.userId.literal)?.also { it.cancel() }
                    }
                    // 某人加入频道服务器
                    is JoinedGuildEventBody -> {
                        // query user info.
                        val guild = internalGuild(this.targetId) ?: return
                        val userInfo =
                            UserViewRequest(guild.id, body.userId).requestDataBy(this@KaiheilaComponentBotImpl)
                        val member = KaiheilaGuildMemberImpl(this@KaiheilaComponentBotImpl, guild, userInfo)
                        guild.members.merge(body.userId.literal, member) { old, now ->
                            old.cancel()
                            now
                        }
                    }
                    // 信息变更 （昵称变更）
                    is UpdatedGuildMemberEventBody -> {
                        val guild = internalGuild(this.targetId) ?: return
                        val member = guild.members[body.userId.literal] ?: return
                        member.nickname = body.nickname
                    }
                    //endregion
                    //region guilds
                    // 服务器被删除
                    is DeletedGuildExtraBody -> {
                        guilds.remove(body.id.literal)?.also { it.cancel() }
                    }
                    // 服务器更新
                    is UpdatedGuildExtraBody -> {
                        val guild = internalGuild(body.id) ?: return
                        guild.name = body.name
                        guild.icon = body.icon
                        guild.ownerId = body.openId
                    }
                    //endregion
                    //region bot self
                    // 用户信息更新
                    is UserUpdatedEventBody -> {
                        if (body.userId == me.id) {
                            updateMe(me.copy(username = body.username, avatar = body.avatar))
                        }
                    }
                    // bot退出了某个服务器
                    is SelfExitedGuildEventBody -> {
                        guilds.remove(body.guildId.literal)?.also { it.cancel() }
                    }
                    // bot加入了某服务器
                    is SelfJoinedGuildEventBody -> {
                        val guildInfo = GuildViewRequest(body.guildId).requestDataBy(this@KaiheilaComponentBotImpl)
                        val guild = KaiheilaGuildImpl(this@KaiheilaComponentBotImpl, guildInfo)
                        guilds.merge(guildInfo.id.literal, guild) { old, now ->
                            old.cancel()
                            now
                        }
                        guild.init()
                    }
                    //endregion

                }
            }

            // Text.
            is Text -> {
            }
        }
    }

    private suspend fun KhlEvent<*>.internalProcessor() {
        register(this@KaiheilaComponentBotImpl)
    }

    //endregion

    companion object {
        val botStatus = UserStatus.builder().bot().fakeUser().build()
        const val ASSET_PREFIX = "https://www.kaiheila.cn"
    }
}


