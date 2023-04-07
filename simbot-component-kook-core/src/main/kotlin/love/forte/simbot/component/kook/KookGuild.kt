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

package love.forte.simbot.component.kook

import kotlinx.coroutines.CoroutineScope
import love.forte.plugin.suspendtrans.annotation.JvmAsync
import love.forte.plugin.suspendtrans.annotation.JvmBlocking
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.component.kook.role.KookGuildRole
import love.forte.simbot.component.kook.role.KookGuildRoleCreator
import love.forte.simbot.component.kook.role.KookRole
import love.forte.simbot.definition.*
import love.forte.simbot.utils.item.Items
import kotlin.time.Duration
import love.forte.simbot.kook.objects.Guild as KkGuild

/**
 *
 * Kook 组件中的频道服务器信息。
 *
 * @author ForteScarlet
 */
public interface KookGuild : Guild, CoroutineScope, KookComponentDefinition<KkGuild> {
    
    /**
     * 得到当前频道服务器所对应的api模块下的服务器对象。
     */
    override val source: KkGuild
    
    override val bot: KookComponentGuildBot
    
    override val currentMember: Int
    override val maximumMember: Int
    
    override val description: String
    override val icon: String
    override val id: ID
    override val name: String
    
    override val currentChannel: Int
    override val maximumChannel: Int
    
    // region owner api
    override val ownerId: ID
    
    /**
     * 频道服务器的创建人。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun owner(): KookGuildMember
    // endregion
    
    
    /**
     * 根据指定ID查询对应用户信息，或得到null。
     */
    @JvmBlocking(baseName = "getMember", suffix = "")
    @JvmAsync(baseName = "getMember")
    override suspend fun member(id: ID): KookGuildMember?
    
    /**
     * 直接获取用户列表的副本。
     */
    public val memberList: List<KookGuildMember>
    
    /**
     * 查询用户列表。
     *
     * @see memberList
     */
    override val members: Items<KookGuildMember>
    
    /**
     * 直接获取当前频道服务器下的子频道列表的副本。
     *
     * 子频道列表不包含分组类型的频道，这类频道请参考 [categories]。
     */
    public val channelList: List<KookChannel>
    
    /**
     * 获取当前频道服务器下的子频道序列。
     *
     * 子频道列表不包含分组类型的频道，这类频道请参考 [categories]。
     *
     * @see channelList
     */
    override val channels: Items<KookChannel>
    
    /**
     * 尝试根据指定ID获取匹配的[子频道][KookChannel]。
     *
     * 未找到时得到null。
     */
    @JvmBlocking(baseName = "getChannel", suffix = "")
    @JvmAsync(baseName = "getChannel")
    override suspend fun channel(id: ID): KookChannel?
    
    /**
     * 获取当前频道服务器下的子频道序列。
     *
     * 子频道列表不包含分组类型的频道，这类"分类频道"请参考 [categories]。
     *
     * @see channels
     */
    override val children: Items<KookChannel>
        get() = channels
    
    /**
     * 尝试根据指定ID获取匹配的[子频道][KookChannel]。
     *
     * 未找到时得到null。
     */
    @JvmBlocking(baseName = "getChild", suffix = "")
    @JvmAsync(baseName = "getChild")
    override suspend fun child(id: ID): KookChannel? = channel(id)
    
    /**
     * 得到当前频道下所有的分组型频道。
     */
    public val categories: List<KookChannelCategory>
    
    
    /**
     * 尝试根据ID获取匹配的分类对象。
     */
    public fun getCategory(id: ID): KookChannelCategory?
    
    // region role api
    /**
     * 获取当前频道服务器中配置的所有角色信息。
     *
     * _Note: [roles] 尚在实验阶段，可能会在未来做出变更。_
     *
     * @see KookRole
     */
    @ExperimentalSimbotApi
    override val roles: Items<KookGuildRole>

    /**
     * 构建一个针对当前频道服务器的角色创建器，用于构建一个新的角色 `Role`。
     *
     * @see KookGuildRoleCreator
     */
    @ExperimentalSimbotApi
    public fun roleCreator(): KookGuildRoleCreator

    // endregion
    
    // region mute api
    
    @Deprecated("Guild mute is not supported", ReplaceWith("false"))
    override suspend fun mute(duration: Duration): Boolean = false
    
    @Deprecated("Guild mute is not supported", ReplaceWith("false"))
    override suspend fun unmute(): Boolean = false
    // endregion
    
    
    /**
     * 频道服务器没有上层。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun previous(): Organization? = null
}

