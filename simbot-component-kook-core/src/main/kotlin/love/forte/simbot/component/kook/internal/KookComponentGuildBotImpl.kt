package love.forte.simbot.component.kook.internal

import love.forte.simbot.component.kook.KookComponentBot
import love.forte.simbot.component.kook.KookComponentGuildBot
import love.forte.simbot.component.kook.KookGuildMember


/**
 *
 * @author ForteScarlet
 */
internal class KookComponentGuildBotImpl(
    override val bot: KookComponentBot,
    private val member: KookGuildMember,
) : KookComponentGuildBot(), KookComponentBot by bot {
    override suspend fun asMember(): KookGuildMember = member
    
    override fun toMember(): KookGuildMember = member
}

internal fun KookComponentBot.toMemberBot(member: KookGuildMember): KookComponentGuildBotImpl = KookComponentGuildBotImpl(this, member)
