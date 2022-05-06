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

package love.forte.simbot.component.kaiheila

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import love.forte.simbot.*
import love.forte.simbot.component.kaiheila.message.KaiheilaAssetImage
import love.forte.simbot.component.kaiheila.message.KaiheilaAssetMessage
import love.forte.simbot.component.kaiheila.message.KaiheilaSimpleAssetMessage
import love.forte.simbot.definition.Group
import love.forte.simbot.definition.UserStatus
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.kaiheila.KaiheilaBot
import love.forte.simbot.kaiheila.api.message.MessageType
import love.forte.simbot.resources.Resource
import love.forte.simbot.utils.runInBlocking
import org.slf4j.Logger
import java.util.stream.Stream
import kotlin.coroutines.CoroutineContext


/**
 *
 * 开黑啦组件在simbot下的组件 [Bot] 实现。
 *
 * @author ForteScarlet
 */
public abstract class KaiheilaComponentBot : Bot {
    /**
     * botID。此id通常代表 client id, 即 [KaiheilaBot.Ticket.clientId].
     */
    override val id: ID get() = sourceBot.ticket.clientId

    /**
     * 判断此ID是否代表当前bot。可能代表bot的id有可能是 [clientId][KaiheilaBot.Ticket.clientId],
     * 也有可能是此bot在系统中作为User时候的 user id.
     *
     * 对于 user id 的判断，只有当至少执行过一次 [start] 来启动bot的时候才会生效匹配，在那之前将只会通过 [id] 进行匹配。
     *
     */
    abstract override fun isMe(id: ID): Boolean

    /**
     * 得到在stdlib标准库模块下所提供的开黑啦bot实例。
     */
    public abstract val sourceBot: KaiheilaBot

    /**
     * 得到对应的组件实例。
     */
    abstract override val component: KaiheilaComponent
    abstract override val avatar: String
    abstract override val coroutineContext: CoroutineContext
    abstract override val eventProcessor: EventProcessor
    abstract override val isActive: Boolean
    abstract override val isCancelled: Boolean
    abstract override val isStarted: Boolean
    abstract override val logger: Logger
    abstract override val manager: KaiheilaBotManager
    abstract override val status: UserStatus
    abstract override val username: String


    //region guild api
    abstract override suspend fun guild(id: ID): KaiheilaGuild?

    abstract override suspend fun guilds(grouping: Grouping, limiter: Limiter): Flow<KaiheilaGuild>

    @OptIn(Api4J::class)
    abstract override fun getGuild(id: ID): KaiheilaGuild?

    @OptIn(Api4J::class)
    abstract override fun getGuilds(grouping: Grouping, limiter: Limiter): Stream<out KaiheilaGuild>

    @OptIn(Api4J::class)
    override fun getGuilds(limiter: Limiter): Stream<out KaiheilaGuild> = getGuilds(Grouping.EMPTY, limiter)

    @OptIn(Api4J::class)
    override fun getGuilds(): Stream<out KaiheilaGuild> = getGuilds(Grouping.EMPTY, Limiter)
    //endregion


    //region image api
    /**
     * 上传一个资源并得到一个 [KaiheilaAssetMessage].
     *
     * @param resource 需要上传的资源
     * @param type 在发送时所需要使用的消息类型。通常选择为 [MessageType.IMAGE]、[MessageType.FILE] 中的值，即 `2`、`3`、`4`。
     */
    @JvmSynthetic
    public abstract suspend fun uploadAsset(resource: Resource, type: Int): KaiheilaSimpleAssetMessage

    /**
     * 上传一个资源并得到一个 [KaiheilaAssetMessage].
     * @param resource 需要上传的资源
     * @param type 在发送时所需要使用的消息类型。通常选择为 [MessageType.IMAGE]、[MessageType.FILE] 中的值.
     */
    @JvmSynthetic
    public suspend fun uploadAsset(resource: Resource, type: MessageType): KaiheilaSimpleAssetMessage = uploadAsset(resource, type.type)


    /**
     * 提供一个资源类型并将其上传后作为 [KaiheilaAssetImage] 使用。
     */
    @JvmSynthetic
    abstract override suspend fun uploadImage(resource: Resource): KaiheilaAssetImage

    /**
     * 提供一个资源类型并将其上传后作为 [KaiheilaAssetImage] 使用。
     */
    @Api4J
    override fun uploadImageBlocking(resource: Resource): KaiheilaAssetImage = runInBlocking { uploadImage(resource) }

    /**
     * 由于开黑啦中的资源不存在id，因此会直接将 [id] 视为 url 进行转化。
     *
     * 但是需要验证此 [id] 是否为 `https://www.kaiheila.cn` 开头，即是否为kaiheila的资源。
     *
     */
    @JvmSynthetic
    abstract override suspend fun resolveImage(id: ID): KaiheilaAssetImage

    @OptIn(Api4J::class)
    override fun resolveImageBlocking(id: ID): KaiheilaAssetImage = runInBlocking { resolveImage(id) }
    //endregion

    @OptIn(ExperimentalSimbotApi::class)
    abstract override suspend fun friend(id: ID): KaiheilaUserChat?

    @OptIn(ExperimentalSimbotApi::class)
    abstract override suspend fun friends(grouping: Grouping, limiter: Limiter): Flow<KaiheilaUserChat>

    @OptIn(ExperimentalSimbotApi::class)
    @Api4J
    abstract override fun getFriends(grouping: Grouping, limiter: Limiter): Stream<out KaiheilaUserChat>

    abstract override suspend fun cancel(reason: Throwable?): Boolean

    abstract override suspend fun join()


    abstract override suspend fun start(): Boolean


    //// impl

    override val bot: KaiheilaComponentBot get() = this

    //region group apis
    @Deprecated("Does not support the 'group'", ReplaceWith("null"))
    override suspend fun group(id: ID): Group? = null

    @Deprecated("Does not support the 'group'", ReplaceWith("null"))
    override suspend fun groups(grouping: Grouping, limiter: Limiter): Flow<Group> = emptyFlow()

    @OptIn(Api4J::class)
    @Deprecated("Does not support the 'group'", ReplaceWith("null"))
    override fun getGroups(grouping: Grouping, limiter: Limiter): Stream<out Group> = Stream.empty()

    @OptIn(Api4J::class)
    @Deprecated("Does not support the 'group'", ReplaceWith("null"))
    override fun getGroup(id: ID): Group? = null

    @OptIn(Api4J::class)
    @Deprecated("Does not support the 'group'", ReplaceWith("null"))
    override fun getGroups(): Stream<out Group> = Stream.empty()

    @OptIn(Api4J::class)
    @Deprecated("Does not support the 'group'", ReplaceWith("null"))
    override fun getGroups(limiter: Limiter): Stream<out Group> = Stream.empty()
    //endregion
// todo ..
}