package love.forte.simbot.component.kook.internal

import love.forte.simbot.component.kook.KaiheilaComponentBot
import love.forte.simbot.component.kook.KaiheilaComponentGuildBot
import love.forte.simbot.component.kook.KaiheilaGuildMember


/**
 *
 * @author ForteScarlet
 */
internal class KaiheilaComponentGuildBotImpl(
    override val bot: KaiheilaComponentBot,
    private val member: KaiheilaGuildMember,
) : KaiheilaComponentGuildBot(), KaiheilaComponentBot by bot {
    override suspend fun asMember(): KaiheilaGuildMember = member
    
    override fun toMember(): KaiheilaGuildMember = member
}

internal fun KaiheilaComponentBot.toMemberBot(member: KaiheilaGuildMember): KaiheilaComponentGuildBotImpl = KaiheilaComponentGuildBotImpl(this, member)
