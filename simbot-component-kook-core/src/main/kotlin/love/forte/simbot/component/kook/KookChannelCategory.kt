/*
 * Copyright (c) 2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.component.kook

import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.definition.Category
import love.forte.simbot.kook.objects.Channel


/**
 * Kook 组件中对于 [频道类型][Category] 的定义。
 *
 */
public interface KookChannelCategory : Category, KookComponentDefinition<Channel> {
    
    /**
     * 这个分类对应的原本的 [Channel] 类型对象。
     * 可能会会随着事件而发生变化与更新。
     */
    override val source: Channel
    
    /**
     * 此分类所述的频道服务器。
     */
    public val guild: KookGuild
    
    /**
     * 此分类ID。
     */
    override val id: ID
    
    /**
     * 此分类名称。
     */
    override val name: String
}


/**
 * 根据当前分组信息，寻找频道服务器下所有相同分类的子频道序列。
 *
 */
@ExperimentalSimbotApi
public val KookChannelCategory.channels: Sequence<KookChannel>
    get() = guild.children.asSequence().filter { it.category?.id == id }
