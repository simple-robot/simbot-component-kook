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
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.component.kook.bot.KookGuildBot
import love.forte.simbot.definition.Channel
import kotlin.coroutines.CoroutineContext


/**
 * 一个 KOOK 中的子频道。
 *
 * @author ForteScarlet
 */
public interface KookChannel : KookChannelBased, Channel, CoroutineScope {
    /**
     * 源于 [bot] 的上下文。
     */
    override val coroutineContext: CoroutineContext
        get() = bot.coroutineContext

    /**
     * 此频道的分组。
     *
     * 如果当前频道是属于“顶层分类”的频道（即没有分类、 [source.parentId][love.forte.simbot.kook.objects.Channel.parentId] 为空），
     * 则 [category] 结果为null。
     *
     * @see KookChannelCategory
     */
    override val category: KookChannelCategory?

    /**
     * 得到所属 bot。
     *
     * @throws KookGuildNotExistsException 如果频道已经不存在时
     */
    override val bot: KookGuildBot

    /**
     * 频道ID
     */
    override val id: ID get() = source.id.ID

    /**
     * 频道名称
     */
    override val name: String get() = source.name

    /**
     * 频道简介。始终为空字符串 `""`
     */
    override val description: String
        get() = ""

    /**
     * 所属频道ID
     */
    override val guildId: ID

    /**
     * 创建者ID
     */
    override val ownerId: ID
        get() = source.userId.ID

    /**
     * KOOK 中不存在“频道图标”，始终得到空字符串 （`""`）。
     */
    @Deprecated("'Channel icon' does not exist in KOOK", ReplaceWith("\"\""))
    override val icon: String get() = ""

    /**
     * KOOK 中不支持获取子频道的创建时间。
     */
    @Deprecated(
        "'Channel createTime' does not supported in KOOK",
        ReplaceWith("Timestamp.notSupport()", "love.forte.simbot.Timestamp")
    )
    override val createTime: Timestamp get() = Timestamp.notSupport()


    // TODO guild
    // TODO members
    // TODO roles
    // TODO send
    // TODO mute


}
