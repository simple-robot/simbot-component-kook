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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import love.forte.simbot.ID
import love.forte.simbot.Simbot
import love.forte.simbot.component.kook.*
import love.forte.simbot.component.kook.event.KookBotStartedEvent
import love.forte.simbot.component.kook.internal.KookGuildImpl.Companion.toKookGuild
import love.forte.simbot.component.kook.internal.event.KookBotStartedEventImpl
import love.forte.simbot.component.kook.message.KookAssetImage
import love.forte.simbot.component.kook.message.KookAssetMessage
import love.forte.simbot.component.kook.message.KookAssetMessage.Key.asImage
import love.forte.simbot.component.kook.message.KookAssetMessage.Key.asMessage
import love.forte.simbot.component.kook.message.KookSimpleAssetMessage
import love.forte.simbot.component.kook.model.toModel
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.event.pushIfProcessable
import love.forte.simbot.kook.Kook
import love.forte.simbot.kook.KookBot
import love.forte.simbot.kook.api.ApiResultType
import love.forte.simbot.kook.api.asset.AssetCreateRequest
import love.forte.simbot.kook.api.asset.AssetCreated
import love.forte.simbot.kook.api.channel.ChannelViewRequest
import love.forte.simbot.kook.api.guild.GuildListRequest
import love.forte.simbot.kook.api.guild.GuildViewRequest
import love.forte.simbot.kook.api.message.MessageType
import love.forte.simbot.kook.api.user.Me
import love.forte.simbot.kook.api.user.UserViewRequest
import love.forte.simbot.kook.api.userchat.UserChatCreateRequest
import love.forte.simbot.kook.api.userchat.UserChatListRequest
import love.forte.simbot.kook.event.Event.Extra.Sys
import love.forte.simbot.kook.event.Event.Extra.Text
import love.forte.simbot.kook.event.system.channel.AddedChannelExtraBody
import love.forte.simbot.kook.event.system.channel.DeletedChannelExtraBody
import love.forte.simbot.kook.event.system.channel.UpdatedChannelExtraBody
import love.forte.simbot.kook.event.system.guild.DeletedGuildExtraBody
import love.forte.simbot.kook.event.system.guild.UpdatedGuildExtraBody
import love.forte.simbot.kook.event.system.guild.member.JoinedGuildEventBody
import love.forte.simbot.kook.event.system.guild.member.UpdatedGuildMemberEventBody
import love.forte.simbot.kook.event.system.user.SelfExitedGuildEventBody
import love.forte.simbot.kook.event.system.user.SelfJoinedGuildEventBody
import love.forte.simbot.kook.event.system.user.UserUpdatedEventBody
import love.forte.simbot.literal
import love.forte.simbot.resources.Resource
import love.forte.simbot.utils.item.Items
import love.forte.simbot.utils.item.Items.Companion.asItems
import love.forte.simbot.utils.item.effectOn
import love.forte.simbot.utils.item.itemsByFlow
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.set
import kotlin.coroutines.CoroutineContext
import love.forte.simbot.kook.event.Event as KkEvent

/**
 *
 * @author ForteScarlet
 */
internal class KookComponentBotImpl(
    override val sourceBot: KookBot,
    override val manager: KookBotManager,
    override val eventProcessor: EventProcessor,
    override val component: KookComponent,
    private val configuration: KookComponentBotConfiguration,
) : KookComponentBot {
    internal val job = SupervisorJob(sourceBot.coroutineContext[Job]!!)
    override val coroutineContext: CoroutineContext = sourceBot.coroutineContext + job
    
    internal val isEventProcessAsync = sourceBot.configuration.isEventProcessAsync
    
    override val logger: Logger =
        LoggerFactory.getLogger("love.forte.simbot.component.kook.bot.${sourceBot.ticket.clientId}")
    
    internal val muteDelayJob = SupervisorJob(job)
    
    // internal val muteDelayCoroutineDispatcher get() = muteDelayCoroutineDispatcherContainer.muteDelayDispatcher
    
    private lateinit var internalGuilds: ConcurrentHashMap<String, KookGuildImpl>
    
    @JvmSynthetic
    internal fun internalGuild(id: ID): KookGuildImpl? = internalGuild(id.literal)
    
    @JvmSynthetic
    internal fun internalGuild(id: String): KookGuildImpl? = internalGuilds[id]
    
    
    init {
        sourceBot.preProcessor { _, decoded ->
            val decodedEvent = decoded()
            
            // register some event processors
            decodedEvent.internalPreProcessor()
            
            /*
             *  事件的验证、准备是(协程下)同步的（借preProcessor的特性），
             *  但是事件的触发是异步的。
             *
             *  存在一小部分事件的准备处理是在 internalProcessor 中进行而不是 internalProProcessor 中。
             *  这部分事件通常与"删除"有关。
             */
            decodedEvent.internalProcessor()
        }
        
    }
    
    
    // invoke with initLock
    private suspend fun init() {
        updateMe(sourceBot.me())
        initGuilds()
        initSyncJob()
    }
    
    /**
     * 以 flow 的方式查询guild列表
     */
    private fun requestGuilds(): Flow<love.forte.simbot.kook.objects.Guild> {
        return flow {
            var page = 1
            do {
                bot.logger.debug("Sync guild data ... page {}", page)
                val guildsResult = GuildListRequest.create(page = page).requestDataBy(this@KookComponentBotImpl)
                val guilds = guildsResult.items
                bot.logger.debug("{} guild data synchronized in page {}", guilds.size, page)
                guilds.forEach {
                    emit(it)
                }
                page = guildsResult.meta.page + 1
            } while (guilds.isNotEmpty() && guildsResult.meta.page < guildsResult.meta.pageTotal)
        }
    }
    
    private suspend fun initGuilds() {
        val guildsMap = ConcurrentHashMap<String, KookGuildImpl>()
        requestGuilds()
            .buffer(100)
            .map { guild -> guild.toModel() }
            .collect { model ->
                guildsMap[model.id.literal] = model.toKookGuild(this)
            }
        
        bot.logger.debug("Sync guild data, {} guild data have been cached.", guildsMap.size)
        
        this.internalGuilds = guildsMap
    }
    
    @Synchronized
    private fun initSyncJob() {
        initGuildSyncJob()
    }
    
    @Volatile
    private var guildSyncJob: Job? = null
    
    @Synchronized
    private fun initGuildSyncJob() {
        guildSyncJob?.cancel()
        guildSyncJob = null
        
        val guildSyncPeriod = configuration.syncPeriods.guildSyncPeriod
        val batchDelay = configuration.syncPeriods.batchDelay
        if (guildSyncPeriod > 0) {
            // 同步guild信息。目前只同步 model 信息
            suspend fun syncGuild() {
                requestGuilds().collect { guild ->
                    val guildId = guild.id.literal
                    val guildModel = guild.toModel()
                    
                    val curr = guild(guild.id)
                    val syncNeedGuild = if (curr == null) {
                        // compute it.
                        val guildImpl = guildModel.toKookGuild(this)
                        internalGuilds.compute(guildId) { _, cur ->
                            if (cur == null) {
                                // 不存在旧的，直接添加
                                guildImpl
                            } else {
                                // 存在旧的，尝试替换model
                                // 但是无论如何, 新的guildImpl都是没有必要的了
                                guildImpl.cancel()
                                // 如果当前旧的完成初始化的事件比自己晚，抛弃自己
                                // // 否则，自己为最新数据，替换。
                                if (cur.initTimestamp < guildImpl.initTimestamp) {
                                    cur.source = guildModel
                                }
                                cur
                            }
                        }!!
                    } else {
                        // update source data
                        curr.source = guildModel
                        curr
                    }
                    
                    syncNeedGuild.sync(batchDelay)
                    
                }
            }
            
            guildSyncJob = launch {
                while (job.isActive) {
                    delay(guildSyncPeriod)
                    syncGuild()
                }
            }
        }
    }
    
    
    override val isActive: Boolean
        get() = job.isActive
    
    override val isCancelled: Boolean
        get() = job.isCancelled
    
    override val isStarted: Boolean
        get() = sourceBot.isStarted
    
    
    override suspend fun join() {
        sourceBot.join()
    }
    
    override suspend fun cancel(reason: Throwable?): Boolean {
        if (job.isCancelled) return false
        sourceBot.cancel(reason)
        return true
    }
    
    // region me and isMe
    @Volatile
    internal lateinit var me: Me
    
    private val meLock = Mutex()
    
    private suspend fun updateMe(newMe: Me) = meLock.withLock {
        me = newMe
    }
    
    override fun isMe(id: ID): Boolean {
        if (id == this.id) return true
        if (::me.isInitialized && me.id == id) return true
        return false
    }
    // endregion
    
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
            eventProcessor.pushIfProcessable(KookBotStartedEvent) {
                KookBotStartedEventImpl(this)
            }
        }
    }
    
    
    override suspend fun guild(id: ID): KookGuildImpl? = internalGuilds[id.literal]
    
    override val guildList: List<KookGuild>
        get() = internalGuilds.values.toList()
    
    override val guilds: Items<KookGuild>
        get() = internalGuilds.values.asItems()
    
    
    // region friend api
    override suspend fun contact(id: ID): KookUserChatImpl {
        val chat = UserChatCreateRequest.create(id).requestDataBy(bot)
        return KookUserChatImpl(this, chat.toModel())
    }
    
    override val contacts: Items<KookUserChatImpl>
        get() {
            return itemsByFlow { prop ->
                val flow = flow {
                    val items = UserChatListRequest.requestDataBy(this@KookComponentBotImpl).items
                    items.forEach { emit(KookUserChatImpl(this@KookComponentBotImpl, it.toModel())) }
                }
                
                prop.effectOn(flow)
            }
        }
    
    // endregion
    
    
    // region image api / assert api
    
    /**
     * 上传一个资源并得到一个 [KookAssetMessage].
     *
     * @param resource 需要上传的资源
     * @param type 在发送时所需要使用的消息类型。通常选择为 [MessageType.IMAGE]、[MessageType.FILE] 中的值，
     * 即 `2`、`3`、`4`。
     */
    @JvmSynthetic
    override suspend fun uploadAsset(resource: Resource, type: Int): KookSimpleAssetMessage {
        val asset = AssetCreateRequest.create(resource).requestDataBy(this)
        return asset.asMessage(type)
    }
    
    
    /**
     * 由于 Kook 中的资源不存在id，因此会直接将 [id] 视为 url 进行转化。
     */
    @OptIn(ApiResultType::class)
    @JvmSynthetic
    override suspend fun resolveImage(id: ID): KookAssetImage {
        Simbot.require(id.literal.startsWith(ASSET_PREFIX)) {
            "The id must be the resource id of the kook and must start with $ASSET_PREFIX"
        }
        return KookAssetImage(AssetCreated(id.literal))
    }

    
    override suspend fun uploadAssetImage(resource: Resource): KookAssetImage {
        val asset = AssetCreateRequest.create(resource).requestDataBy(this)
        return asset.asImage()
    }
    
    // endregion
    
    
    // region internal event process
    private suspend fun KkEvent<*>.internalPreProcessor() {
        when (val ex = extra) {
            // 系统事件
            is Sys<*> -> {
                when (val body = ex.body) {
                    
                    // region guild members
                    // 某人退出频道服务器
                    // 在标准事件处理中进行
                    // is ExitedGuildEventBody -> {
                    // val guild = internalGuild(this.targetId) ?: return
                    // guild.internalMembers.remove(body.userId.literal)?.also { it.cancel() }
                    // }
                    
                    // 某人加入频道服务器
                    is JoinedGuildEventBody -> {
                        // query user info.
                        val guild = internalGuild(this.targetId) ?: return
                        val userInfo =
                            UserViewRequest.create(guild.id, body.userId).requestDataBy(this@KookComponentBotImpl)
                        val userModel = userInfo.toModel()
                        
                        guild.internalMembers.compute(body.userId.literal) { _, current ->
                            current?.also {
                                it.source = userModel
                            } ?: KookGuildMemberImpl(this@KookComponentBotImpl, guild, userModel)
                        }
                    }
                    // 信息变更 （昵称变更）
                    is UpdatedGuildMemberEventBody -> {
                        val guild = internalGuild(this.targetId) ?: return
                        val member = guild.internalMembers[body.userId.literal] ?: return
                        member.source.also { old ->
                            member.source = old.copy(nickname = body.nickname)
                        }
                    }
                    // endregion
                    // region guilds
                    // 服务器被删除
                    is DeletedGuildExtraBody -> {
                        internalGuilds.remove(body.id.literal)?.also { it.cancel() }
                    }
                    // 服务器更新
                    is UpdatedGuildExtraBody -> {
                        val guild = internalGuild(body.id) ?: return
                        guild.source.also { old ->
                            guild.source = old.copy(
                                name = body.name,
                                icon = body.icon,
                                masterId = body.userId,
                                notifyType = body.notifyType,
                                region = body.region,
                                enableOpen = body.enableOpen,
                                openId = body.openId,
                                defaultChannelId = body.defaultChannelId,
                                welcomeChannelId = body.welcomeChannelId,
                            )
                        }
                        
                    }
                    // endregion
                    // region channels
                    // 某服务器新增频道
                    is AddedChannelExtraBody -> {
                        val channelId = body.id
                        val guildId = body.guildId.literal
                        
                        internalGuilds[guildId]?.also { guild ->
                            // query channel info.
                            val channelView = ChannelViewRequest.create(channelId).requestDataBy(this@KookComponentBotImpl)
                            val channelModel = channelView.toModel()
                            guild.computeMergeChannelModel(channelModel)
                        }
                    }
                    
                    // 某服务器更新频道信息
                    is UpdatedChannelExtraBody -> {
                        guild(body.guildId)?.also { guild ->
                            val channelId = body.id.literal
                            val mutableChannelModelContainer: MutableChannelModelContainer = if (body.isCategory) {
                                guild.getInternalCategory(channelId)
                            } else {
                                guild.getInternalChannel(channelId)
                            } ?: return@also
                            
                            val oldSource = mutableChannelModelContainer.source
                            
                            val newSource = oldSource.copy(
                                userId = body.masterId,
                                parentId = body.parentId, // update parent -> update category
                                name = body.name,
                                topic = body.topic,
                                type = body.type,
                                level = body.level,
                                slowMode = body.slowMode,
                                // isCategory = body.isCategory, // changeable?
                            )
                            
                            mutableChannelModelContainer.source = newSource
                            if (mutableChannelModelContainer is KookChannelImpl) {
                                val newCategoryIdValue = newSource.parentId.literal
                                if (newCategoryIdValue.isEmpty()) {
                                    // set null
                                    mutableChannelModelContainer.category = null
                                } else if (oldSource.parentId.literal != newCategoryIdValue) {
                                    val newCategory =
                                        guild.findOrQueryAndComputeCategory(newSource.parentId.literal) ?: return@also
                                    // set new category
                                    mutableChannelModelContainer.category = newCategory
                                }
                            }
                            
                        }
                    }
                    // 某服务器频道被删除
                    is DeletedChannelExtraBody -> {
                        // TODO check isCategory?
                        guild(targetId)?.also { guild ->
                            val removedId = body.id.literal
                            val removed: Any? =
                                guild.removeInternalChannel(removedId) ?: guild.removeInternalCategory(removedId)
                            logger.debug("Channel(or category) {} is be Deleted", removed)
                        }
                    }
                    // endregion
                    
                    // region bot self
                    // 用户信息更新
                    is UserUpdatedEventBody -> {
                        val curMe = me
                        if (body.userId == curMe.id) {
                            updateMe(curMe.copy(username = body.username, avatar = body.avatar))
                        }
                    }
                    // bot退出了某个服务器
                    is SelfExitedGuildEventBody -> {
                        internalGuilds.remove(body.guildId.literal)?.also { it.cancel() }
                    }
                    // bot加入了某服务器
                    is SelfJoinedGuildEventBody -> {
                        val guildInfo = GuildViewRequest.create(body.guildId).requestDataBy(this@KookComponentBotImpl)
                        val guildModel = guildInfo.toModel()
                        val newGuild = guildModel.toKookGuild(this@KookComponentBotImpl)
                        internalGuilds.merge(guildInfo.id.literal, newGuild) { old, cur ->
                            old.cancel()
                            cur
                        }
                    }
                    // endregion
                    
                }
            }
            
            // Text..?
            is Text -> {
            }
        }
    }
    
    private suspend fun KkEvent<*>.internalProcessor() {
        register(this@KookComponentBotImpl)
    }
    
    // endregion
    
    companion object {
        const val ASSET_PREFIX = Kook.HOST
    }
}


