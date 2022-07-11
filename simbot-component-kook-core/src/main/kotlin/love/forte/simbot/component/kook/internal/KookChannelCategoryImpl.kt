/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
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

