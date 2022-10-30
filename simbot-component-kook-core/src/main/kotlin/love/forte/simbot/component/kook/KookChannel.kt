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

package love.forte.simbot.component.kook

import love.forte.simbot.Api4J
import love.forte.simbot.ID
import love.forte.simbot.JavaDuration
import love.forte.simbot.Timestamp
import love.forte.simbot.component.kook.message.KookMessageCreatedReceipt.Companion.asReceipt
import love.forte.simbot.component.kook.message.KookMessageReceipt
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.definition.*
import love.forte.simbot.kook.api.message.MessageCreateRequest
import love.forte.simbot.kook.api.message.MessageType
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.utils.item.Items
import love.forte.simbot.utils.item.Items.Companion.emptyItems
import love.forte.simbot.utils.runInBlocking
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import love.forte.simbot.kook.objects.Channel as KkChannel


/**
 *
 * Kook 组件的子频道类型定义。
 *
 * @author ForteScarlet
 */
public interface KookChannel : Channel, KookComponentDefinition<KkChannel> {
    
    /**
     * 得到当前频道所对应的api模块下的频道对象。
     */
    override val source: KkChannel
    
    /**
     * 此频道对应的分组类型。
     *
     * 如果当前频道是属于“顶层分类”的频道（即 [source.parentId][KkChannel.parentId] 为空），则 [category] 结果为null。
     * 如果你希望能够得到“顶层分类”下的所有频道，请参考 [KookGuild.rootCategory]。
     *
     *
     * @see KookChannelCategory
     */
    override val category: KookChannelCategory?
    
    override val bot: KookComponentGuildBot
    override val id: ID get() = source.id
    override val icon: String get() = source.icon
    override val name: String get() = source.name
    override val createTime: Timestamp get() = Timestamp.notSupport()
    override val description: String get() = source.description
    
    override val guildId: ID
    override val currentMember: Int
    override val maximumMember: Int
    
    @OptIn(Api4J::class)
    override val owner: KookGuildMember
    
    @JvmSynthetic
    override suspend fun owner(): GuildMember = owner
    override val ownerId: ID
    
    /**
     * 获取当前频道中的成员列表。相当于获取 guild 的成员列表。
     */
    override val members: Items<KookGuildMember>
    
    /**
     * 寻找当前频道中指定ID的成员。相当于在 guild 中寻找。
     */
    @OptIn(Api4J::class)
    override fun getMember(id: ID): KookGuildMember?
    
    /**
     * 寻找当前频道中指定ID的成员。相当于在 guild 中寻找。
     */
    @JvmSynthetic
    override suspend fun member(id: ID): KookGuildMember?
    
    
    // region guild api
    @OptIn(Api4J::class)
    override val guild: KookGuild
    
    @OptIn(Api4J::class)
    override val previous: KookGuild?
        get() = guild
    
    @JvmSynthetic
    override suspend fun guild(): KookGuild = guild
    
    @JvmSynthetic
    override suspend fun previous(): KookGuild = guild
    // endregion
    
    
    // region roles api
    
    /**
     * 获取当前子频道中的所有角色信息。
     *
     * Deprecated: 尚未支持
     */
    @Deprecated(
        "Not support yet.",
        ReplaceWith("emptyItems()", "love.forte.simbot.utils.item.Items.Companion.emptyItems")
    )
    override val roles: Items<Role> get() = emptyItems()
    
    // endregion
    
    
    // region send api
    /**
     * 根据 [MessageCreateRequest] api 构建并发送消息。
     */
    @JvmSynthetic
    public suspend fun send(request: MessageCreateRequest): KookMessageReceipt {
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
        tempTargetId: ID?,
    ): KookMessageReceipt {
        val request = MessageCreateRequest(type, source.id, content, quote, nonce, tempTargetId)
        return send(request)
    }
    
    
    /**
     * 发送纯文本消息，并指定 [tempTargetId].
     *
     * @see MessageCreateRequest.tempTargetId
     */
    @JvmSynthetic
    public suspend fun send(text: String, quote: ID? = null, tempTargetId: ID? = null): KookMessageReceipt {
        return send(
            MessageType.TEXT.type,
            text,
            quote, null, tempTargetId
        )
    }
    
    /**
     * 发送消息，并可选的指定 [quote] 和 [tempTargetId].
     *
     * @see MessageCreateRequest.tempTargetId
     * @see MessageCreateRequest.quote
     */
    @JvmSynthetic
    public suspend fun send(message: Message, quote: ID? = null, tempTargetId: ID? = null): KookMessageReceipt
    
    /**
     * 发送消息，并可选的指定 [quote] 和 [tempTargetId].
     *
     * @see MessageCreateRequest.tempTargetId
     * @see MessageCreateRequest.quote
     */
    @JvmSynthetic
    public suspend fun send(
        message: MessageContent,
        quote: ID? = null,
        tempTargetId: ID? = null,
    ): KookMessageReceipt
    
    
    /**
     * 发送纯文本消息。
     */
    @JvmSynthetic
    override suspend fun send(text: String): KookMessageReceipt {
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
    override suspend fun send(message: Message): KookMessageReceipt = send(message, null)
    
    /**
     * 发送消息。
     */
    @JvmSynthetic
    override suspend fun send(message: MessageContent): KookMessageReceipt = send(message, null)
    
    
    /**
     * 发送纯文本消息，并指定 [quote].
     *
     * @see MessageCreateRequest.quote
     */
    @Api4J
    public fun sendBlocking(text: String, quote: ID?): KookMessageReceipt = runInBlocking {
        send(text, quote = quote)
    }
    
    /**
     * 发送消息，并指定 [quote].
     *
     * @see MessageCreateRequest.quote
     */
    @Api4J
    public fun sendBlocking(message: Message, quote: ID?): KookMessageReceipt =
        runInBlocking { send(message, quote = quote) }
    
    /**
     * 发送消息，并指定 [quote].
     *
     * @see MessageCreateRequest.quote
     */
    @Api4J
    public fun sendBlocking(message: MessageContent, quote: ID?): KookMessageReceipt =
        runInBlocking { send(message, quote = quote) }
    
    /**
     * 发送纯文本消息，并指定 [quote] 和 [tempTargetId].
     *
     * @see MessageCreateRequest.tempTargetId
     * @see MessageCreateRequest.quote
     */
    @Api4J
    public fun sendBlocking(text: String, quote: ID?, tempTargetId: ID?): KookMessageReceipt = runInBlocking {
        send(text, quote = quote, tempTargetId = tempTargetId)
    }
    
    /**
     * 发送消息，并指定 [quote] 和 [tempTargetId].
     *
     * @see MessageCreateRequest.tempTargetId
     * @see MessageCreateRequest.quote
     */
    @Api4J
    public fun sendBlocking(message: Message, quote: ID?, tempTargetId: ID?): KookMessageReceipt =
        runInBlocking { send(message, quote = quote, tempTargetId = tempTargetId) }
    
    /**
     * 发送消息，并指定 [quote] 和 [tempTargetId].
     *
     * @see MessageCreateRequest.tempTargetId
     * @see MessageCreateRequest.quote
     */
    @Api4J
    public fun sendBlocking(message: MessageContent, quote: ID?, tempTargetId: ID?): KookMessageReceipt =
        runInBlocking { send(message, quote = quote, tempTargetId = tempTargetId) }
    
    /**
     * 发送纯文本消息。
     */
    @Api4J
    override fun sendBlocking(text: String): KookMessageReceipt = sendBlocking(text, null)
    
    /**
     * 发送消息。
     */
    @Api4J
    override fun sendBlocking(message: Message): KookMessageReceipt = sendBlocking(message, null)
    
    /**
     * 发送消息。
     */
    @Api4J
    override fun sendBlocking(message: MessageContent): KookMessageReceipt = sendBlocking(message, null)
    
    // endregion
    
    // region Invalid api
    @Deprecated("Channel mute is not supported", ReplaceWith("false"))
    override suspend fun mute(duration: Duration): Boolean = false
    
    @Deprecated("Channel mute is not supported", ReplaceWith("false"))
    override suspend fun unmute(): Boolean = false
    
    @OptIn(Api4J::class)
    @Deprecated("Channel mute is not supported", ReplaceWith("false"))
    override fun muteBlocking(time: Long, timeUnit: TimeUnit): Boolean = false
    
    @OptIn(Api4J::class)
    @Deprecated("Channel mute is not supported", ReplaceWith("false"))
    override fun unmuteBlocking(): Boolean = false
    
    @OptIn(Api4J::class)
    @Deprecated("Channel mute is not supported", ReplaceWith("false"))
    override fun muteBlocking(): Boolean = false
    
    @OptIn(Api4J::class)
    @Deprecated("Channel mute is not supported", ReplaceWith("false"))
    override fun muteBlocking(duration: JavaDuration): Boolean = false
    
    // endregion
}


