/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.component.kook

import love.forte.simbot.Api4J
import love.forte.simbot.ID
import love.forte.simbot.bot.Bot
import love.forte.simbot.component.kook.message.KookAssetImage
import love.forte.simbot.component.kook.message.KookAssetMessage
import love.forte.simbot.component.kook.message.KookSimpleAssetMessage
import love.forte.simbot.definition.Group
import love.forte.simbot.definition.GuildBot
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.kook.KookBot
import love.forte.simbot.kook.api.message.MessageType
import love.forte.simbot.kook.api.userchat.UserChatCreateRequest
import love.forte.simbot.kook.api.userchat.UserChatListRequest
import love.forte.simbot.resources.Resource
import love.forte.simbot.utils.item.Items
import love.forte.simbot.utils.item.Items.Companion.emptyItems
import love.forte.simbot.utils.runInBlocking
import org.slf4j.Logger
import kotlin.coroutines.CoroutineContext


/**
 *
 * Kook 组件在simbot下的组件 [Bot] 实现。
 *
 * @author ForteScarlet
 */
public interface KookComponentBot : Bot {
    /**
     * botID。此id通常代表 client id, 即 [KookBot.Ticket.clientId].
     */
    override val id: ID get() = sourceBot.ticket.clientId
    
    /**
     * bot自身。
     */
    override val bot: KookComponentBot get() = this
    
    /**
     * 判断此ID是否代表当前bot。可能代表bot的id有可能是 [clientId][KookBot.Ticket.clientId],
     * 也有可能是此bot在系统中作为User时候的 user id.
     *
     * 对于 user id 的判断，只有当至少执行过一次 [start] 来启动bot的时候才会生效匹配，在那之前将只会通过 [id] 进行匹配。
     *
     */
    override fun isMe(id: ID): Boolean
    
    /**
     * 得到在stdlib标准库模块下所提供的 Kook bot实例。
     */
    public val sourceBot: KookBot
    
    /**
     * 得到对应的组件实例。
     */
    public override val component: KookComponent
    
    public override val avatar: String
    public override val coroutineContext: CoroutineContext
    public override val eventProcessor: EventProcessor
    public override val isActive: Boolean
    public override val isCancelled: Boolean
    public override val isStarted: Boolean
    public override val logger: Logger
    public override val manager: KookBotManager
    public override val username: String
    
    /**
     * 获取当前bot存在的频道服务器序列。
     */
    override val guilds: Items<KookGuild>
    
    /**
     * 根据指定ID寻找频道服务器。
     */
    @JvmSynthetic
    public override suspend fun guild(id: ID): KookGuild?
    
    
    /**
     * 根据指定ID寻找频道服务器。
     */
    @OptIn(Api4J::class)
    public override fun getGuild(id: ID): KookGuild?
    
    
    // region image api
    /**
     * 上传一个资源并得到一个 [KookAssetMessage].
     *
     * @param resource 需要上传的资源
     * @param type 在发送时所需要使用的消息类型。通常选择为 [MessageType.IMAGE]、[MessageType.FILE] 中的值，即 `2`、`3`、`4`。
     */
    @JvmSynthetic
    public suspend fun uploadAsset(resource: Resource, type: Int): KookSimpleAssetMessage
    
    /**
     * 上传一个资源并得到一个 [KookAssetMessage].
     * @param resource 需要上传的资源
     * @param type 在发送时所需要使用的消息类型。通常选择为 [MessageType.IMAGE]、[MessageType.FILE] 中的值.
     */
    @JvmSynthetic
    public suspend fun uploadAsset(resource: Resource, type: MessageType): KookSimpleAssetMessage =
        uploadAsset(resource, type.type)
    
    
    /**
     * 提供一个资源类型并将其上传后作为 [KookAssetImage] 使用。
     */
    @JvmSynthetic
    public suspend fun uploadAssetImage(resource: Resource): KookAssetImage
    
    /**
     * 提供一个资源类型并将其上传后作为 [KookAssetImage] 使用。
     */
    @Api4J
    public fun uploadAssetImageBlocking(resource: Resource): KookAssetImage = runInBlocking { uploadAssetImage(resource) }
    
    
    /**
     * 由于 Kook 中的资源不存在id，因此会直接将 [id] 视为 url 进行转化。
     *
     * 但是需要验证此 [id] 是否为 `https://www.kook.cn` 开头，即是否为 kook 的资源。
     *
     */
    @JvmSynthetic
    public override suspend fun resolveImage(id: ID): KookAssetImage
    
    /**
     * 由于 Kook 中的资源不存在id，因此会直接将 [id] 视为 url 进行转化。
     *
     * 但是需要验证此 [id] 是否为 `https://www.kook.cn` 开头，即是否为 kook 的资源。
     *
     */
    @OptIn(Api4J::class)
    override fun resolveImageBlocking(id: ID): KookAssetImage
    // endregion
    
    /**
     * 通过指定ID **构建** 一个目标用户的[聊天会话][KookUserChat]对象。
     *
     * [聊天会话][KookUserChat] 目前不会进行缓存，每次获取都会通过 [UserChatCreateRequest] 请求并构建新的实例。
     * 由于每次都会通过api请求，因此不存在 "没找到" 的情况，[contact] 将不会返回非null值。
     *
     * 但是会抛出任何可能由 [UserChatCreateRequest] 抛出的或者请求过程中产生的任何异常。
     *
     */
    @JvmSynthetic
    override suspend fun contact(id: ID): KookUserChat
    
    /**
     * 通过指定ID **构建** 一个目标用户的[聊天会话][KookUserChat]对象。
     *
     * [聊天会话][KookUserChat] 目前不会进行缓存，每次获取都会通过 [UserChatCreateRequest] 请求并构建新的实例。
     * 由于每次都会通过api请求，因此不存在 "没找到" 的情况，[getContact] 将不会返回非null值。
     *
     * 但是会抛出任何可能由 [UserChatCreateRequest] 抛出的或者请求过程中产生的任何异常。
     *
     */
    @Api4J
    override fun getContact(id: ID): KookUserChat = runInBlocking { contact(id) }
    
    
    /**
     * 查询当前存在的所有 [聊天会话][KookUserChat]。
     *
     * 会通过 [UserChatListRequest] 直接进行查询，不会进行缓存。
     */
    override val contacts: Items<KookUserChat>
    
    
    /**
     * 终止当前bot。
     */
    @JvmSynthetic
    public override suspend fun cancel(reason: Throwable?): Boolean
    
    /**
     * 挂起直到当前bot被关闭。
     */
    @JvmSynthetic
    public override suspend fun join()
    
    /**
     * 启动此bot。
     */
    @JvmSynthetic
    public override suspend fun start(): Boolean
    
    
    @Deprecated("Does not support the 'group'", ReplaceWith("null"))
    override suspend fun group(id: ID): Group? = null
    
    @Deprecated(
        "Does not support the 'group'",
        ReplaceWith("emptyItems()", "love.forte.simbot.utils.item.Items.Companion.emptyItems")
    )
    override val groups: Items<Group>
        get() = emptyItems()
    
    @OptIn(Api4J::class)
    @Deprecated("Does not support the 'group'", ReplaceWith("null"))
    override fun getGroup(id: ID): Group? = null
    
    
    // more ..?
}


/**
 * Kook 组件中针对于 [GuildBot] 的实现类型。
 *
 * 实现 [KookComponentBot] 和 [GuildBot],
 * 代表一个bot在某个频道服务器中所扮演的成员。
 *
 * @see KookComponentBot
 * @see GuildBot
 */
public abstract class KookComponentGuildBot : KookComponentBot, GuildBot {
    
    /**
     * 得到当前组织中的 Kook bot在当前组织中所扮演的成员对象。
     */
    abstract override suspend fun asMember(): KookGuildMember
    
    /**
     * 得到当前组织中的 Kook bot在当前组织中所扮演的成员对象。
     */
    @OptIn(Api4J::class)
    abstract override fun toMember(): KookGuildMember
}