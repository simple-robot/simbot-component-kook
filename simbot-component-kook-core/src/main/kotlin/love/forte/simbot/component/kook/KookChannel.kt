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

package love.forte.simbot.component.kook

import love.forte.plugin.suspendtrans.annotation.JvmAsync
import love.forte.plugin.suspendtrans.annotation.JvmBlocking
import love.forte.simbot.ID
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
    
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun owner(): KookGuildMember
    
    override val ownerId: ID
    
    /**
     * 获取当前频道中的成员列表。相当于获取 guild 的成员列表。
     */
    override val members: Items<KookGuildMember>
    
    /**
     * 寻找当前频道中指定ID的成员。相当于在 guild 中寻找。
     */
    @JvmBlocking(baseName = "getMember", suffix = "")
    @JvmAsync(baseName = "getMember")
    override suspend fun member(id: ID): KookGuildMember?
    
    
    // region guild api
    /**
     * 子频道所属频道服务器
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun guild(): KookGuild
    
    /**
     * 子频道所属频道服务器
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun previous(): KookGuild = guild()
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
    @JvmBlocking
    @JvmAsync
    public suspend fun send(request: MessageCreateRequest): KookMessageReceipt {
        return request.requestDataBy(bot).asReceipt(false, bot)
    }
    
    /**
     * 根据 [MessageCreateRequest] api 构建并发送消息。
     */
    @JvmBlocking
    @JvmAsync
    public suspend fun send(
        type: Int,
        content: String,
        quote: ID?,
        nonce: String?,
        tempTargetId: ID?,
    ): KookMessageReceipt {
        val request = MessageCreateRequest.create(type, source.id, content, quote, nonce, tempTargetId)
        return send(request)
    }
    
    
    /**
     * 发送纯文本消息，并指定 [tempTargetId].
     *
     * @see MessageCreateRequest.tempTargetId
     */
    @JvmBlocking
    @JvmAsync
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
    @JvmBlocking
    @JvmAsync
    public suspend fun send(message: Message, quote: ID? = null, tempTargetId: ID? = null): KookMessageReceipt
    
    /**
     * 发送消息，并可选的指定 [quote] 和 [tempTargetId].
     *
     * @see MessageCreateRequest.tempTargetId
     * @see MessageCreateRequest.quote
     */
    @JvmBlocking
    @JvmAsync
    public suspend fun send(
        message: MessageContent,
        quote: ID? = null,
        tempTargetId: ID? = null,
    ): KookMessageReceipt
    
    
    /**
     * 发送纯文本消息。
     */
    @JvmBlocking
    @JvmAsync
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
    @JvmBlocking
    @JvmAsync
    override suspend fun send(message: Message): KookMessageReceipt = send(message, null)
    
    /**
     * 发送消息。
     */
    @JvmBlocking
    @JvmAsync
    override suspend fun send(message: MessageContent): KookMessageReceipt = send(message, null)
    
    // endregion
    
    // region Invalid api
    @Deprecated("Channel mute is not supported", ReplaceWith("false"))
    override suspend fun mute(duration: Duration): Boolean = false
    
    @Deprecated("Channel mute is not supported", ReplaceWith("false"))
    override suspend fun unmute(): Boolean = false
    // endregion
}


