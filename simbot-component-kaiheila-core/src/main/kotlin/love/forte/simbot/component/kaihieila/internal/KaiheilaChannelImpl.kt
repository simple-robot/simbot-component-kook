/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kaiheila 的一部分。
 *
 *  simbot-component-kaiheila 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kaiheila 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

package love.forte.simbot.component.kaihieila.internal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import love.forte.simbot.Api4J
import love.forte.simbot.ID
import love.forte.simbot.Limiter
import love.forte.simbot.SimbotIllegalArgumentException
import love.forte.simbot.component.kaihieila.KaiheilaChannel
import love.forte.simbot.component.kaihieila.KaiheilaGuildMember
import love.forte.simbot.component.kaihieila.message.KaiheilaApiRequestedReceipt
import love.forte.simbot.component.kaihieila.message.KaiheilaChannelMessageDetailsContent
import love.forte.simbot.component.kaihieila.message.KaiheilaMessageCreatedReceipt.Companion.asReceipt
import love.forte.simbot.component.kaihieila.message.KaiheilaReceiveMessageContent
import love.forte.simbot.component.kaihieila.message.toRequest
import love.forte.simbot.component.kaihieila.util.requestDataBy
import love.forte.simbot.definition.Role
import love.forte.simbot.kaiheila.api.message.MessageCreateRequest
import love.forte.simbot.kaiheila.api.message.MessageCreated
import love.forte.simbot.kaiheila.objects.Channel
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.message.MessageReceipt
import java.util.stream.Stream
import kotlin.coroutines.CoroutineContext


/**
 *
 * @author ForteScarlet
 */
internal class KaiheilaChannelImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val guild: KaiheilaGuildImpl,
    override val source: Channel
) : KaiheilaChannel, CoroutineScope {
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

    override fun getMember(id: ID): KaiheilaGuildMember? = guild.getMember(id)
    override suspend fun member(id: ID): KaiheilaGuildMember? = guild.member(id)
    override fun getMembers(): Stream<out KaiheilaGuildMember> = guild.getMembers()
    override fun getMembers(groupingId: ID?): Stream<out KaiheilaGuildMember> = guild.getMembers(groupingId)
    override fun getMembers(groupingId: ID?, limiter: Limiter): Stream<out KaiheilaGuildMember> =
        guild.getMembers(groupingId, limiter)

    override fun getMembers(limiter: Limiter): Stream<out KaiheilaGuildMember> = guild.getMembers(limiter)
    override suspend fun members(groupingId: ID?, limiter: Limiter): Flow<KaiheilaGuildMember> =
        guild.members(groupingId, limiter)


    override suspend fun send(message: Message, tempTargetId: ID?): MessageReceipt {
        val request = message.toRequest(targetId = source.id, tempTargetId = tempTargetId)
            ?: throw SimbotIllegalArgumentException("Valid messages must not be empty.")

        val result = request.requestDataBy(bot)

        return if (result is MessageCreated) {
            result.asReceipt(false, bot)
        } else {
            KaiheilaApiRequestedReceipt(result, false)
        }
    }

    override suspend fun send(message: MessageContent, tempTargetId: ID?): MessageReceipt {
        return when (message) {
            is KaiheilaReceiveMessageContent -> {
                val source = message.source
                MessageCreateRequest(
                    type = source.type.type,
                    targetId = this.id,
                    content = source.content,
                    quote = null,
                    nonce = null,
                    tempTargetId = tempTargetId,
                ).requestDataBy(bot).asReceipt(false, bot)
            }
            is KaiheilaChannelMessageDetailsContent -> {
                val details = message.details
                MessageCreateRequest(
                    type = details.type,
                    targetId = this.id,
                    content = details.content,
                    quote = details.quote?.id,
                    nonce = null,
                    tempTargetId = tempTargetId,
                ).requestDataBy(bot).asReceipt(false, bot)
            }
            else -> {
                send(message.messages)
            }
        }
    }

    @Api4J
    override fun getRoles(groupingId: ID?, limiter: Limiter): Stream<out Role> {
        return Stream.empty()
        // TODO("Not yet implemented")
    }

    override suspend fun roles(groupingId: ID?, limiter: Limiter): Flow<Role> {
        return emptyFlow()
        // TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "KaiheilaChannel(source=$source)"
    }

}