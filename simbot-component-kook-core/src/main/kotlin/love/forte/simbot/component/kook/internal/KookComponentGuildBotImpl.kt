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

import love.forte.simbot.component.kook.KookComponentBot
import love.forte.simbot.component.kook.KookComponentGuildBot
import love.forte.simbot.component.kook.KookGuildMember


/**
 *
 * @author ForteScarlet
 */
internal class KookComponentGuildBotImpl private constructor(
    override val bot: KookComponentBot,
    private val member: KookGuildMember,
) : KookComponentGuildBot(), KookComponentBot by bot {
    override suspend fun asMember(): KookGuildMember = member
    
    override fun toMember(): KookGuildMember = member
    
    
    override fun toString(): String {
        return "KookComponentGuildBotImpl(bot=$bot, member=$member)"
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is KookComponentGuildBotImpl) return false
        
        return bot == other.bot && member == other.member
    }
    
    override fun hashCode(): Int {
        var result = bot.hashCode()
        result = 31 * result + member.hashCode()
        return result
    }
    
    companion object {
        internal fun KookComponentBot.toMemberBot(member: KookGuildMember): KookComponentGuildBotImpl =
            KookComponentGuildBotImpl(this, member)
    }
}


