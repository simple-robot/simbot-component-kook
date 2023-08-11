/*
 * Copyright (c) 2023. ForteScarlet.
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

package love.forte.simbot.component.kook.bot

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.job
import love.forte.simbot.ID
import love.forte.simbot.JST
import love.forte.simbot.JSTP
import love.forte.simbot.bot.Bot
import love.forte.simbot.component.kook.KookComponent
import love.forte.simbot.component.kook.KookGuild
import love.forte.simbot.component.kook.message.KookAsset
import love.forte.simbot.component.kook.message.KookAssetImage
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.definition.Group
import love.forte.simbot.definition.GuildBot
import love.forte.simbot.definition.SocialRelationsContainer.Companion.COUNT_NOT_SUPPORTED
import love.forte.simbot.kook.Ticket
import love.forte.simbot.kook.api.ApiResultType
import love.forte.simbot.kook.api.asset.Asset
import love.forte.simbot.kook.api.asset.CreateAssetApi
import love.forte.simbot.kook.messages.MessageType
import love.forte.simbot.literal
import love.forte.simbot.message.Image
import love.forte.simbot.utils.item.Items
import love.forte.simbot.utils.item.Items.Companion.emptyItems
import kotlin.coroutines.CoroutineContext
import love.forte.simbot.kook.Bot as KBot


/**
 * simbot组件针对 Kook bot 的 [Bot] 实现。
 *
 * @author ForteScarlet
 */
public interface KookBot : Bot, CoroutineScope {
    /**
     * 源自 [sourceBot] 的 [CoroutineContext]
     */
    override val coroutineContext: CoroutineContext
        get() = sourceBot.coroutineContext

    /**
     * 得到标准库中的 [Kook Bot][KBot] 源对象。
     */
    public val sourceBot: KBot

    /**
     * bot自己。
     */
    override val bot: KookBot
        get() = this

    /**
     * botID。即 [Ticket.clientId].
     */
    override val id: ID
        get() = sourceBot.ticket.clientId.ID


    /**
     * 判断此 ID 是否代表当前 bot。可以代表 bot 的 id 可能是 [clientId][Ticket.clientId],
     * 也有可能是此 bot 在系统中作为 User 时候的 `user id`。
     *
     * 对于 `user id` 的判断，只有当至少执行过一次 [start] 来启动 bot 的时候才会生效匹配，在那之前将只会通过 `clientId` 进行匹配。
     *
     */
    override fun isMe(id: ID): Boolean

    /**
     * 得到对应的组件实例。
     */
    override val component: KookComponent

    /**
     * bot 是否处于活跃状态
     */
    override val isActive: Boolean
        get() = sourceBot.isActive

    /**
     * bot 是否已经被关闭
     */
    override val isCancelled: Boolean
        get() = coroutineContext.job.isCancelled

    /**
     * bot 是否已经被启动过
     */
    override val isStarted: Boolean
        get() = sourceBot.isStarted

    /**
     * 头像信息
     *
     * 需要至少启动过一次（执行过 [start]）后才可获取。
     *
     * @throws IllegalStateException 尚未启动过时
     */
    override val avatar: String
        get() = sourceBot.botUserInfo.avatar

    /**
     * 用户名称
     *
     * 需要至少启动过一次（执行过 [start]）后才可获取。
     *
     * @throws IllegalStateException 尚未启动过时
     */
    override val username: String
        get() = sourceBot.botUserInfo.username

    /**
     * [KookBot] 的所属管理器。
     */
    override val manager: KookBotManager

    /**
     * 根据 "ID" 构建一个 Image. KOOK 中没有一个针对图片的“ID”，
     * 因此这个 [id] 实际上即为通过 [CreateAssetApi] 上传得到的
     * [Asset] 中的 [url][Asset.url]。
     *
     * [resolveImage] 通常用于自行上传图片并得到 [Asset] 后、
     * 或者希望通过已有的 [KookAssetImage] 重新包装为一个新的 [Image] 类型。
     *
     * 如果是前者，可以考虑使用 [uploadAssetImage] 来提供 [CreateAssetApi]
     * 上传图片并直接得到包装后的 [KookAssetImage]；
     *
     * 如果是后者，你并没有必要“重新包装”，直接使用原本的 [KookAssetImage] 即可。
     * [resolveImage] 并不会产生任何网络请求，而仅仅只是使用一个新的对象包装URL而已。
     *
     * @see uploadAsset
     * @see uploadAssetImage
     */
    @OptIn(ApiResultType::class)
    @JvmSynthetic
    override suspend fun resolveImage(id: ID): Image<*> {
        return KookAssetImage(Asset(id.literal))
    }

    /**
     * 提供一个 [CreateAssetApi], 通过此BOT上传将得到的 [Asset] 结果包装为 [KookAsset] .
     *
     * @param api 上传资源的请求
     * @param type 构建的 [KookAsset] 的类型
     */
    @JST
    public suspend fun uploadAsset(api: CreateAssetApi, type: MessageType): KookAsset =
        KookAsset(api.requestDataBy(this), type)

    /**
     * 提供一个 [CreateAssetApi], 通过此BOT上传将得到的 [Asset] 结果包装为 [KookAssetImage] .
     *
     * @param api 上传资源的请求
     */
    @JST
    public suspend fun uploadAssetImage(api: CreateAssetApi): KookAssetImage =
        KookAssetImage(api.requestDataBy(this))

    override suspend fun cancel(reason: Throwable?): Boolean {
        sourceBot.close()
        return true
    }

    override suspend fun join() {
        sourceBot.join()
    }

    //region Guild APIs
    /**
     * KOOK 支持频道相关操作
     */
    override val isGuildsSupported: Boolean
        get() = true

    /**
     * 获取所有的频道服务器序列
     */
    override val guilds: Items<KookGuild>

    /**
     * 根据ID寻找指定频道。如果找不到则返回 null。
     */
    @JST(blockingBaseName = "getGuild", blockingSuffix = "", asyncBaseName = "getGuild")
    override suspend fun guild(id: ID): KookGuild?

    /**
     * 获取当前bot所处的频道服务器数量。
     */
    @JSTP
    override suspend fun guildCount(): Int
    //endregion


    //region Group APIs
    /**
     * KOOK 中没有 "群"，不支持 group  相关操作
     */
    @Deprecated("'Group' is not supported in KOOK", ReplaceWith("null"))
    @JvmSynthetic
    @JST(blockingBaseName = "getGroup", blockingSuffix = "", asyncBaseName = "getGroup")
    override suspend fun group(id: ID): Group? = null

    /**
     * KOOK 中没有 "群"，不支持 group  相关操作
     */
    @Deprecated("'Group' is not supported in KOOK", ReplaceWith("null"))
    override val groups: Items<Group>
        get() = emptyItems()

    /**
     * KOOK 中没有 "群"，不支持 group  相关操作
     */
    override val isGroupsSupported: Boolean
        get() = false

    /**
     *  KOOK 中没有 "群"，不支持 group  相关操作
     */
    @JSTP
    @JvmSynthetic
    @Deprecated("'Group' is not supported in KOOK", ReplaceWith("null"))
    override suspend fun groupCount(): Int = COUNT_NOT_SUPPORTED
    //endregion
}


/**
 * KOOK 组件中针对于 [GuildBot] 的实现类型。
 *
 * 实现 [KookBot] 和 [GuildBot],
 * 代表一个 bot 在某个频道服务器中所扮演的成员。
 *
 * @see KookBot
 * @see GuildBot
 */
public interface KookGuildBot : KookBot, GuildBot {
    // TODO
}
