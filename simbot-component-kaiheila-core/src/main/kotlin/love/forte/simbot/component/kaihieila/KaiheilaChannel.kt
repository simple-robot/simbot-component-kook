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

package love.forte.simbot.component.kaihieila

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import love.forte.simbot.Api4J
import love.forte.simbot.ID
import love.forte.simbot.Limiter
import love.forte.simbot.Timestamp
import love.forte.simbot.component.kaihieila.message.KaiheilaMessageCreatedReceipt
import love.forte.simbot.component.kaihieila.message.KaiheilaMessageCreatedReceipt.Companion.asReceipt
import love.forte.simbot.component.kaihieila.util.requestDataBy
import love.forte.simbot.definition.*
import love.forte.simbot.kaiheila.api.message.MessageCreateRequest
import love.forte.simbot.kaiheila.api.message.MessageType
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.message.MessageReceipt
import love.forte.simbot.utils.runInBlocking
import java.util.concurrent.TimeUnit
import java.util.stream.Stream
import kotlin.time.Duration
import love.forte.simbot.kaiheila.objects.Channel as KhlChannel


/**
 *
 * 开黑啦组件的子频道类型定义。
 *
 * @author ForteScarlet
 */
public interface KaiheilaChannel : Channel, KaiheilaComponentDefinition<KhlChannel> {

    /**
     * 得到当前频道所对应的api模块下的频道对象。
     */
    override val source: KhlChannel


    override val bot: KaiheilaComponentBot
    override val id: ID get() = source.id
    override val icon: String get() = source.icon
    override val name: String get() = source.name
    override val createTime: Timestamp get() = Timestamp.notSupport()
    override val description: String get() = source.description

    override val guildId: ID
    override val currentMember: Int
    override val maximumMember: Int

    @OptIn(Api4J::class)
    override val owner: KaiheilaGuildMember
    override suspend fun owner(): Member = owner
    override val ownerId: ID

    //region members api
    override fun getMember(id: ID): KaiheilaGuildMember?

    override suspend fun member(id: ID): KaiheilaGuildMember?

    @OptIn(Api4J::class)
    override fun getMembers(): Stream<out KaiheilaGuildMember>

    @OptIn(Api4J::class)
    override fun getMembers(groupingId: ID?): Stream<out KaiheilaGuildMember>

    @OptIn(Api4J::class)
    override fun getMembers(groupingId: ID?, limiter: Limiter): Stream<out KaiheilaGuildMember>

    @OptIn(Api4J::class)
    override fun getMembers(limiter: Limiter): Stream<out KaiheilaGuildMember>


    override suspend fun members(groupingId: ID?, limiter: Limiter): Flow<KaiheilaGuildMember>
    //endregion


    //region guild api
    @OptIn(Api4J::class)
    override val guild: KaiheilaGuild

    @OptIn(Api4J::class)
    override val previous: KaiheilaGuild?
        get() = guild

    override suspend fun guild(): KaiheilaGuild = guild
    override suspend fun previous(): KaiheilaGuild = guild
    //endregion


    //region roles api
    @Api4J
    override fun getRoles(groupingId: ID?, limiter: Limiter): Stream<out Role>
    override suspend fun roles(groupingId: ID?, limiter: Limiter): Flow<Role>
    //endregion


    //region send api
    /**
     * 根据 [MessageCreateRequest] api 构建并发送消息。
     */
    @JvmSynthetic
    public suspend fun send(request: MessageCreateRequest): KaiheilaMessageCreatedReceipt {
        return request.requestDataBy(bot).asReceipt(false, bot)
    }

    /**
     * 根据 [MessageCreateRequest] api 构建并发送消息。
     */
    @JvmSynthetic
    public suspend fun send(
        type: Int,
        content: String,
        quote: ID?,
        nonce: String?,
        tempTargetId: ID?
    ): KaiheilaMessageCreatedReceipt {
        val request = MessageCreateRequest(type, source.id, content, quote, nonce, tempTargetId)
        return send(request)
    }


    /**
     * 发送纯文本消息，并指定 [tempTargetId].
     *
     * @see MessageCreateRequest.tempTargetId
     */
    @JvmSynthetic
    public suspend fun send(text: String, tempTargetId: ID?): KaiheilaMessageCreatedReceipt {
        return send(
            MessageType.TEXT.type,
            text,
            null, null, tempTargetId
        )
    }

    /**
     * 发送消息，并指定 [tempTargetId].
     *
     * @see MessageCreateRequest.tempTargetId
     */
    @JvmSynthetic
    public suspend fun send(message: Message, tempTargetId: ID?): MessageReceipt

    /**
     * 发送消息，并指定 [tempTargetId].
     *
     * @see MessageCreateRequest.tempTargetId
     */
    @JvmSynthetic
    public suspend fun send(message: MessageContent, tempTargetId: ID?): MessageReceipt


    /**
     * 发送纯文本消息。
     */
    @JvmSynthetic
    override suspend fun send(text: String): KaiheilaMessageCreatedReceipt {
        return send(
            MessageType.TEXT.type,
            text,
            null, null, null
        )
    }
    /**
     * 发送消息。
     */
    @JvmSynthetic
    override suspend fun send(message: Message): MessageReceipt = send(message, null)

    /**
     * 发送消息。
     */
    @JvmSynthetic
    override suspend fun send(message: MessageContent): MessageReceipt = send(message, null)



    /**
     * 发送纯文本消息，并指定 [tempTargetId].
     *
     * @see MessageCreateRequest.tempTargetId
     */
    @Api4J
    public fun sendBlocking(text: String, tempTargetId: ID?): KaiheilaMessageCreatedReceipt = runInBlocking {
        send(text, tempTargetId)
    }

    /**
     * 发送消息，并指定 [tempTargetId].
     *
     * @see MessageCreateRequest.tempTargetId
     */
    @Api4J
    public fun sendBlocking(message: Message, tempTargetId: ID?): MessageReceipt = runInBlocking { send(message, tempTargetId) }

    /**
     * 发送消息，并指定 [tempTargetId].
     *
     * @see MessageCreateRequest.tempTargetId
     */
    @Api4J
    public fun sendBlocking(message: MessageContent, tempTargetId: ID?): MessageReceipt = runInBlocking { send(message, tempTargetId) }

    /**
     * 发送纯文本消息。
     */
    @Api4J
    override fun sendBlocking(text: String): KaiheilaMessageCreatedReceipt = sendBlocking(text, null)

    /**
     * 发送消息。
     */
    @Api4J
    override fun sendBlocking(message: Message): MessageReceipt = sendBlocking(message, null)

    /**
     * 发送消息。
     */
    @Api4J
    override fun sendBlocking(message: MessageContent): MessageReceipt = sendBlocking(message, null)

    //endregion

    //region Invalid api
    @Deprecated("Channel mute is not supported", ReplaceWith("false"))
    override suspend fun mute(duration: Duration): Boolean = false

    @Deprecated("Channel mute is not supported", ReplaceWith("false"))
    override suspend fun unmute(): Boolean = false

    @OptIn(Api4J::class)
    @Deprecated("Channel mute is not supported", ReplaceWith("false"))
    override fun muteBlocking(time: Long, unit: TimeUnit): Boolean = false

    @OptIn(Api4J::class)
    @Deprecated("Channel mute is not supported", ReplaceWith("false"))
    override fun unmuteBlocking(): Boolean = false

    @Deprecated("Kaiheila channel has no children", ReplaceWith("emptyFlow()", "kotlinx.coroutines.flow.emptyFlow"))
    override suspend fun children(groupingId: ID?): Flow<Organization> = emptyFlow()

    @Deprecated("Kaiheila channel has no children", ReplaceWith("emptyFlow()", "kotlinx.coroutines.flow.emptyFlow"))
    override suspend fun children(groupingId: ID?, limiter: Limiter): Flow<Organization> = emptyFlow()

    @OptIn(Api4J::class)
    @Deprecated("Kaiheila channel has no children", ReplaceWith("Stream.empty()", "java.util.stream.Stream"))
    override fun getChildren(groupingId: ID?, limiter: Limiter): Stream<Organization> = Stream.empty()

    @OptIn(Api4J::class)
    @Deprecated("Kaiheila channel has no children", ReplaceWith("Stream.empty()", "java.util.stream.Stream"))
    override fun getChildren(): Stream<out Organization> = Stream.empty()

    @OptIn(Api4J::class)
    @Deprecated("Kaiheila channel has no children", ReplaceWith("Stream.empty()", "java.util.stream.Stream"))
    override fun getChildren(groupingId: ID?): Stream<out Organization> = Stream.empty()
    //endregion
}