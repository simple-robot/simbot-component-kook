package love.forte.simbot.component.kaiheila.internal

import love.forte.simbot.Api4J
import love.forte.simbot.ID
import love.forte.simbot.component.kaiheila.KaiheilaComponentBot
import love.forte.simbot.component.kaiheila.KaiheilaComponentGuildMemberBot
import love.forte.simbot.component.kaiheila.KaiheilaGuildMember
import love.forte.simbot.component.kaiheila.message.KaiheilaMessageCreatedReceipt
import love.forte.simbot.definition.UserStatus
import love.forte.simbot.message.Message
import love.forte.simbot.utils.runInBlocking


/**
 *
 * @author ForteScarlet
 */
internal class KaiheilaComponentGuildMemberBotImpl(
    override val bot: KaiheilaComponentBot,
    private val member: KaiheilaGuildMember,
) : KaiheilaComponentGuildMemberBot(), KaiheilaComponentBot by bot, KaiheilaGuildMember by member {
    override val avatar: String
        get() = member.avatar
    
    override val id: ID
        get() = member.id
    
    override val status: UserStatus = member.status.copyAndAsBot()
    
    override val username: String
        get() = member.username
    
    @Api4J
    override fun sendIfSupportBlocking(message: Message): KaiheilaMessageCreatedReceipt {
        return runInBlocking { member.send(message) }
    }
    
}

internal fun KaiheilaComponentBot.toMemberBot(member: KaiheilaGuildMember): KaiheilaComponentGuildMemberBotImpl = KaiheilaComponentGuildMemberBotImpl(this, member)

private fun UserStatus.copyAndAsBot(): UserStatus {
    return UserStatus.builder().also {
        it.bot()
        if (isAnonymous) {
            it.anonymous()
        }
        if (isFake) {
            it.fakeUser()
        }
        if (isNormal) {
            it.normal()
        }
        if (isOfficial) {
            it.official()
        }
    }.build()
}