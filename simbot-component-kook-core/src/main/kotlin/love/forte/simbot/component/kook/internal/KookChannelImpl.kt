/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook,
 * If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.component.kook.internal

import kotlinx.coroutines.CoroutineScope
import love.forte.simbot.ID
import love.forte.simbot.SimbotIllegalArgumentException
import love.forte.simbot.component.kook.KookChannel
import love.forte.simbot.component.kook.KookComponentGuildBot
import love.forte.simbot.component.kook.KookGuild
import love.forte.simbot.component.kook.KookGuildMember
import love.forte.simbot.component.kook.message.KookChannelMessageDetailsContent
import love.forte.simbot.component.kook.message.KookMessageCreatedReceipt.Companion.asReceipt
import love.forte.simbot.component.kook.message.KookMessageReceipt
import love.forte.simbot.component.kook.message.KookReceiveMessageContent
import love.forte.simbot.component.kook.message.sendToChannel
import love.forte.simbot.component.kook.model.ChannelModel
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.kook.api.message.MessageCreateRequest
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.utils.item.Items
import kotlin.coroutines.CoroutineContext


internal interface MutableChannelModelContainer {
    var source: ChannelModel
}


/**
 *
 * @author ForteScarlet
 */
internal class KookChannelImpl private constructor(
    private val baseBot: KookComponentBotImpl,
    private val _guild: KookGuildImpl,
    @Volatile
    override var category: KookChannelCategoryImpl?,
    @Volatile override var source: ChannelModel,
) : KookChannel, CoroutineScope, MutableChannelModelContainer {
    
    override val bot: KookComponentGuildBot
        get() = _guild.bot
    
    override val coroutineContext: CoroutineContext = _guild.newSupervisorCoroutineContext()
    
    override val guildId: ID
        get() = _guild.id
    
    override val currentMember: Int
        get() = _guild.currentMember
    
    override val maximumMember: Int
        get() = _guild.maximumMember
    
    override val ownerId: ID
        get() = _guild.ownerId
    
    override val members: Items<KookGuildMember>
        get() = _guild.members
    
    override suspend fun owner(): KookGuildMember = _guild.owner()
    
    override suspend fun guild(): KookGuild = _guild
    
    override suspend fun member(id: ID): KookGuildMember? = _guild.member(id)
    
    override suspend fun send(message: Message, quote: ID?, tempTargetId: ID?): KookMessageReceipt {
        return send0(message, quote, tempTargetId, null)
//        return message.sendToChannel(bot, targetId = source.id, quote = quote, tempTargetId = tempTargetId)
//            ?: throw SimbotIllegalArgumentException("Valid messages must not be empty.")
    }

    override suspend fun send(message: MessageContent, quote: ID?, tempTargetId: ID?): KookMessageReceipt {
        return when (message) {
            is KookReceiveMessageContent -> {
                val source = message.source
                MessageCreateRequest.create(
                    type = source.type.type,
                    targetId = this.id,
                    content = source.content,
                    quote = quote,
                    nonce = null,
                    tempTargetId = tempTargetId,
                ).requestDataBy(baseBot).asReceipt(false, baseBot)
            }
            
            is KookChannelMessageDetailsContent -> {
                val details = message.details
                MessageCreateRequest.create(
                    type = details.type,
                    targetId = this.id,
                    content = details.content,
                    quote = quote ?: details.quote?.id,
                    nonce = null,
                    tempTargetId = tempTargetId,
                ).requestDataBy(baseBot).asReceipt(false, baseBot)
            }
            
            else -> {
                send(message.messages)
            }
        }
    }


    internal suspend fun send0(message: Message, quote: ID?, tempTargetId: ID?, defaultTempTargetId: ID?): KookMessageReceipt {
        return message.sendToChannel(bot, targetId = source.id, quote = quote, tempTargetId = tempTargetId, defaultTempTargetId = defaultTempTargetId)
            ?: throw SimbotIllegalArgumentException("Valid messages must not be empty.")
    }
    
    override fun toString(): String {
        return "KookChannelImpl(id=$id, name=$name, source=$source, category=$category)"
    }
    
    
    companion object {
        internal fun ChannelModel.toKookChannel(
            baseBot: KookComponentBotImpl,
            guild: KookGuildImpl,
            category: KookChannelCategoryImpl?,
        ): KookChannelImpl {
            return KookChannelImpl(baseBot, guild, category, this)
        }
    }
}




