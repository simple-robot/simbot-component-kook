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
    override val bot: KookComponentBotImpl,
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
        internal fun KookComponentBotImpl.toMemberBot(member: KookGuildMember): KookComponentGuildBotImpl =
            KookComponentGuildBotImpl(this, member)
    }
}


