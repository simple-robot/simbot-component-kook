/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.component.kook.internal

import love.forte.simbot.ID
import love.forte.simbot.component.kook.KookChannelCategory
import love.forte.simbot.component.kook.model.ChannelModel


internal class KookChannelCategoryImpl private constructor(
    override val guild: KookGuildImpl,
    @Volatile override var source: ChannelModel,
) : KookChannelCategory, MutableChannelModelContainer {
    override val id: ID
        get() = source.id
    override val name: String
        get() = source.name
    
    
    
    override fun toString(): String {
        return "KookChannelCategoryImpl(id=$id, name=$name, guild=$guild, source=$source)"
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is KookChannelCategoryImpl) return false
        
        return guild == other.guild && source == other.source
    }
    
    override fun hashCode(): Int {
        var result = guild.hashCode()
        result = 31 * result + source.hashCode()
        return result
    }
    
    companion object {
        internal fun ChannelModel.toCategory(guild: KookGuildImpl): KookChannelCategoryImpl =
            KookChannelCategoryImpl(guild, this)
    }
}

