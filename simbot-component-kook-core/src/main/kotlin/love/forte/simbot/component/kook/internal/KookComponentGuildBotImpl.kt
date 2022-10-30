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
import love.forte.simbot.component.kook.*
import love.forte.simbot.component.kook.message.KookAssetImage
import love.forte.simbot.component.kook.message.KookSimpleAssetMessage
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.kook.KookBot
import love.forte.simbot.kook.api.message.MessageType
import love.forte.simbot.resources.Resource
import love.forte.simbot.utils.item.Items
import org.slf4j.Logger
import kotlin.coroutines.CoroutineContext


/**
 *
 * @author ForteScarlet
 */
internal class KookComponentGuildBotImpl private constructor(
    override val bot: KookComponentBot,
    private val member: KookGuildMember,
) : KookComponentGuildBot() {
    override suspend fun asMember(): KookGuildMember = member
    
    override suspend fun guild(id: ID): KookGuild? = bot.guild(id)
    
    override suspend fun contact(id: ID): KookUserChat = bot.contact(id)
    
    override suspend fun resolveImage(id: ID): KookAssetImage = bot.resolveImage(id)
    
    override suspend fun uploadAsset(resource: Resource, type: Int): KookSimpleAssetMessage =
        bot.uploadAsset(resource, type)
    
    override suspend fun uploadAsset(resource: Resource, type: MessageType): KookSimpleAssetMessage =
        bot.uploadAsset(resource, type)
    
    override suspend fun uploadAssetImage(resource: Resource): KookAssetImage = bot.uploadAssetImage(resource)
    
    override fun isMe(id: ID): Boolean = bot.isMe(id)
    
    override val sourceBot: KookBot
        get() = bot.sourceBot
    override val component: KookComponent
        get() = bot.component
    override val avatar: String
        get() = bot.avatar
    override val coroutineContext: CoroutineContext
        get() = bot.coroutineContext
    override val eventProcessor: EventProcessor
        get() = bot.eventProcessor
    override val isActive: Boolean
        get() = bot.isActive
    override val isCancelled: Boolean
        get() = bot.isCancelled
    override val isStarted: Boolean
        get() = bot.isStarted
    override val logger: Logger
        get() = bot.logger
    override val manager: KookBotManager
        get() = bot.manager
    override val username: String
        get() = bot.username
    override val guilds: Items<KookGuild>
        get() = bot.guilds
    override val guildList: List<KookGuild>
        get() = bot.guildList
    override val contacts: Items<KookUserChat>
        get() = bot.contacts
    
    override suspend fun cancel(reason: Throwable?): Boolean {
        return bot.cancel(reason)
    }
    
    override suspend fun join() {
        bot.join()
    }
    
    override suspend fun start(): Boolean {
        return bot.start()
    }
    
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


