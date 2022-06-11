package love.forte.simbot.component.kaiheila.internal

import love.forte.simbot.component.kaiheila.KaiheilaComponentBot
import love.forte.simbot.component.kaiheila.KaiheilaComponentGuildBot
import love.forte.simbot.component.kaiheila.KaiheilaGuildMember


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
