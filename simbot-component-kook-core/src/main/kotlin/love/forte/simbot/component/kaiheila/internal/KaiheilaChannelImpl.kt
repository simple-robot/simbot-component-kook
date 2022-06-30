/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.component.kaiheila.internal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import love.forte.simbot.ID
import love.forte.simbot.SimbotIllegalArgumentException
import love.forte.simbot.component.kaiheila.KaiheilaChannel
import love.forte.simbot.component.kaiheila.KaiheilaComponentGuildBot
import love.forte.simbot.component.kaiheila.KaiheilaGuildMember
import love.forte.simbot.component.kaiheila.message.*
import love.forte.simbot.component.kaiheila.message.KaiheilaMessageCreatedReceipt.Companion.asReceipt
import love.forte.simbot.component.kaiheila.model.ChannelModel
import love.forte.simbot.component.kaiheila.util.requestDataBy
import love.forte.simbot.kook.api.message.MessageCreateRequest
import love.forte.simbot.kook.api.message.MessageCreated
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.utils.item.Items
import kotlin.coroutines.CoroutineContext


/**
 *
 * @author ForteScarlet
 */
internal class KaiheilaChannelImpl(
    private val baseBot: KaiheilaComponentBotImpl,
    override val guild: KaiheilaGuildImpl,
    @Volatile override var source: ChannelModel,
) : KaiheilaChannel, CoroutineScope {
    
    
    override val bot: KaiheilaComponentGuildBot
        get() = guild.bot
    
    private val job = SupervisorJob(guild.job)
    override val coroutineContext: CoroutineContext = guild.coroutineContext + job
    
    override val guildId: ID
        get() = guild.id
    
    override val currentMember: Int
        get() = guild.currentMember
    
    override val maximumMember: Int
        get() = guild.maximumMember
    
    override val ownerId: ID
        get() = guild.ownerId
    
    override val owner: KaiheilaGuildMember
        get() = guild.owner
    
    override val members: Items<KaiheilaGuildMember>
        get() = guild.members
    
    override fun getMember(id: ID): KaiheilaGuildMember? = guild.getMember(id)
    override suspend fun member(id: ID): KaiheilaGuildMember? = guild.member(id)
    
    
    override suspend fun send(message: Message, quote: ID?, tempTargetId: ID?): KaiheilaMessageReceipt {
        val request = message.toRequest(bot, targetId = source.id, quote = quote, tempTargetId = tempTargetId)
            ?: throw SimbotIllegalArgumentException("Valid messages must not be empty.")
        
        val result = request.requestDataBy(baseBot)
        
        return if (result is MessageCreated) {
            result.asReceipt(false, baseBot)
        } else {
            KaiheilaApiRequestedReceipt(result, false, baseBot)
        }
    }
    
    override suspend fun send(message: MessageContent, quote: ID?, tempTargetId: ID?): KaiheilaMessageReceipt {
        return when (message) {
            is KaiheilaReceiveMessageContent -> {
                val source = message.source
                MessageCreateRequest(
                    type = source.type.type,
                    targetId = this.id,
                    content = source.content,
                    quote = quote,
                    nonce = null,
                    tempTargetId = tempTargetId,
                ).requestDataBy(baseBot).asReceipt(false, baseBot)
            }
            is KaiheilaChannelMessageDetailsContent -> {
                val details = message.details
                MessageCreateRequest(
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
    
    override fun toString(): String {
        return "KaiheilaChannel(source=$source)"
    }
    
}