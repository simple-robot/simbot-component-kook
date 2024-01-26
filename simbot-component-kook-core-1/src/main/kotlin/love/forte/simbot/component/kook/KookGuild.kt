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

package love.forte.simbot.component.kook

import kotlinx.coroutines.CoroutineScope
import love.forte.simbot.*
import love.forte.simbot.component.kook.bot.KookGuildBot
import love.forte.simbot.component.kook.role.KookGuildRole
import love.forte.simbot.component.kook.role.KookGuildRoleCreator
import love.forte.simbot.component.kook.role.KookRole
import love.forte.simbot.component.kook.role.KookRoleOperator
import love.forte.simbot.definition.Guild
import love.forte.simbot.definition.Organization
import love.forte.simbot.utils.item.Items
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import love.forte.simbot.kook.objects.Guild as KGuild


/**
 * 一个 KOOK 中的频道服务器。
 *
 * @author ForteScarlet
 */
public interface KookGuild : Guild, CoroutineScope, KookRoleOperator {
    /**
     * 源于 [bot] 的上下文。
     */
    override val coroutineContext: CoroutineContext
        get() = bot.coroutineContext

    /**
     * 得到此 Guild 内对应的 api 模块下的原始 guild 信息。
     *
     * @see KGuild
     */
    public val source: KGuild

    /**
     * 得到对应所属的 bot
     *
     * @throws KookGuildNotExistsException 当频道已经不存在时
     */
    override val bot: KookGuildBot

    /**
     * 频道服务器ID
     */
    override val id: ID
        get() = source.id.ID

    /**
     * 频道服务器名称
     */
    override val name: String
        get() = source.name

    /**
     * 服务器主题
     */
    override val description: String
        get() = source.topic

    /**
     * 服务器icon的地址
     */
    override val icon: String
        get() = source.icon

    /**
     * KOOK Guild 不支持获取创建时间。始终得到 [Timestamp]。
     */
    override val createTime: Timestamp
        get() = Timestamp.notSupport()

    /**
     * KOOK Guild 不支持获取最大频道上限。始终得到 `-1`。
     */
    override val maximumChannel: Int
        get() = -1

    /**
     * KOOK Guild 不支持获取最大成员上限。始终得到 `-1`。
     */
    override val maximumMember: Int
        get() = -1

    /**
     * 频道当前成员数量
     */
    override val currentMember: Int

    /**
     * 频道当前子频道数量
     */
    override val currentChannel: Int

    /**
     * 获取当前频道服务器下的子频道序列。
     *
     * 子频道列表**不包含**分组类型的频道，这类"分类频道"请参考 [categories]。
     */
    override val channels: Items<KookChannel>

    /**
     * 尝试根据指定ID获取匹配的 [KookChannel]。未找到时得到null。
     */
    @JST(blockingBaseName = "getChannel", blockingSuffix = "", asyncBaseName = "getChannel")
    override suspend fun channel(id: ID): KookChannel?

    /**
     * 获取当前频道服务器下的子频道序列。
     *
     * @see channels
     */
    override val children: Items<KookChannel>
        get() = channels

    /**
     * 尝试根据指定ID获取匹配的 [KookChannel]。未找到时得到null。
     *
     * @see channel
     */
    @JST(blockingBaseName = "getChild", blockingSuffix = "", asyncBaseName = "getChild")
    override suspend fun child(id: ID): KookChannel? = channel(id)

    // categories

    /**
     * 得到当前频道下所有的分组型频道。
     */
    @ExperimentalSimbotApi
    public val categories: Items<KookChannelCategory>


    /**
     * 尝试根据ID获取匹配的分类对象。
     */
    @ExperimentalSimbotApi
    public fun getCategory(id: ID): KookChannelCategory?

    // members

    /**
     * 此频道下的成员序列。
     */
    override val members: Items<KookMember>

    /**
     * 根据ID寻找一个此频道下的成员。
     */
    @JST(blockingBaseName = "getMember", blockingSuffix = "", asyncBaseName = "getMember")
    override suspend fun member(id: ID): KookMember?

    /**
     * 获取当前频道的创建人。
     *
     * @throws KookMemberNotExistsException 如果无法寻找到此成员时
     */
    @JSTP
    override suspend fun owner(): KookMember


    //region roles API

    /**
     * 获取当前频道服务器中配置的所有角色信息。
     *
     * _Note: [roles] 尚在实验阶段，可能会在未来做出变更。_
     *
     * @see KookRole
     * @see KookGuildRole
     */
    @ExperimentalSimbotApi
    override val roles: Items<KookGuildRole>


    @ExperimentalSimbotApi
    override fun roleCreator(): KookGuildRoleCreator
    //endregion

    // region mute api
    /**
     * 频道服务器不支持整体禁言
     */
    @JvmSynthetic
    @Deprecated("Guild mute is not supported in KOOK", ReplaceWith("false"))
    override suspend fun mute(duration: Duration): Boolean = false

    /**
     * 频道服务器不支持整体禁言
     */
    @JvmSynthetic
    @Deprecated("Guild mute is not supported in KOOK", ReplaceWith("false"))
    override suspend fun unmute(): Boolean = false
    // endregion

    /**
     * 频道服务器没有上层。
     */
    @JvmSynthetic
    @Deprecated("The guild does not have previous", ReplaceWith("null"))
    override suspend fun previous(): Organization? = null
}
