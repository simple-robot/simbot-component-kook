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
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.JST
import love.forte.simbot.component.kook.bot.KookGuildBot
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
public interface KookGuild : Guild, CoroutineScope {
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

    // TODO channels

    /**
     * 获取当前频道服务器下的子频道序列。
     *
     * 子频道列表**不包含**分组类型的频道，这类"分类频道"请参考 [categories]。
     */
    override val channels: Items<KookChannel>
        get() = TODO("Not yet implemented")

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

    // TODO categories


    /**
     * 得到当前频道下所有的分组型频道的**列表快照**。
     */
    @ExperimentalSimbotApi
    public val categories: List<KookChannelCategory>


    /**
     * 尝试根据ID获取匹配的分类对象。
     */
    @ExperimentalSimbotApi
    public fun getCategory(id: ID): KookChannelCategory?

    // TODO members
    // TODO owner
    // TODO roles

    // region mute api
    /**
     * 频道服务器不支持整体禁言
     */
    @JvmSynthetic
    @Deprecated("Guild mute is not supported", ReplaceWith("false"))
    override suspend fun mute(duration: Duration): Boolean = false

    /**
     * 频道服务器不支持整体禁言
     */
    @JvmSynthetic
    @Deprecated("Guild mute is not supported", ReplaceWith("false"))
    override suspend fun unmute(): Boolean = false
    // endregion

    /**
     * 频道服务器没有上层。
     */
    @JvmSynthetic
    @Deprecated("The guild does not have previous", ReplaceWith("null"))
    override suspend fun previous(): Organization? = null
}
