/*
 * Copyright (c) 2023. ForteScarlet.
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

import kotlinx.coroutines.CoroutineScope
import love.forte.simbot.*
import love.forte.simbot.component.kook.bot.KookGuildBot
import love.forte.simbot.component.kook.message.KookMessageCreatedReceipt.Companion.asReceipt
import love.forte.simbot.component.kook.message.KookMessageReceipt
import love.forte.simbot.component.kook.role.KookGuildRole
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.definition.Channel
import love.forte.simbot.definition.GuildMember
import love.forte.simbot.kook.api.message.SendChannelMessageApi
import love.forte.simbot.kook.messages.MessageType
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.utils.item.Items
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration


/**
 * 一个 KOOK 中的子频道。
 *
 * @author ForteScarlet
 */
public interface KookChannel : KookChannelBased, Channel, CoroutineScope {
    /**
     * 源于 [bot] 的上下文。
     */
    override val coroutineContext: CoroutineContext
        get() = bot.coroutineContext

    /**
     * 此频道的分组。
     *
     * 如果当前频道是属于“顶层分类”的频道（即没有分类、 [source.parentId][love.forte.simbot.kook.objects.Channel.parentId] 为空），
     * 则 [category] 结果为null。
     *
     * @see KookChannelCategory
     */
    override val category: KookChannelCategory?

    /**
     * 得到所属 bot。
     *
     * @throws KookGuildNotExistsException 如果频道已经不存在时
     */
    override val bot: KookGuildBot

    /**
     * 频道ID
     */
    override val id: ID get() = source.id.ID

    /**
     * 频道名称
     */
    override val name: String get() = source.name

    /**
     * 频道简介。始终为空字符串 `""`
     */
    override val description: String
        get() = ""

    /**
     * 最大成员上限。始终得到 `-1`
     */
    override val maximumMember: Int
        get() = -1

    /**
     * 所属频道ID
     */
    override val guildId: ID

    /**
     * 创建者ID
     */
    override val ownerId: ID
        get() = source.userId.ID

    /**
     * KOOK 中不存在“频道图标”，始终得到空字符串 （`""`）。
     */
    @Deprecated("'Channel icon' does not exist in KOOK", ReplaceWith("\"\""))
    override val icon: String get() = ""

    /**
     * KOOK 中不支持获取子频道的创建时间。
     */
    @Deprecated(
        "'Channel createTime' does not supported in KOOK",
        ReplaceWith("Timestamp.notSupport()", "love.forte.simbot.Timestamp")
    )
    override val createTime: Timestamp get() = Timestamp.notSupport()

    /**
     * 此频道所属服务器
     */
    override suspend fun guild(): KookGuild


    /**
     * 此频道所属服务器
     */
    override suspend fun previous(): KookGuild = guild()

    /**
     * 等同于 [KookGuild.member]
     */
    override suspend fun member(id: ID): GuildMember?

    /**
     * 等同于 [KookGuild.members]
     */
    override val members: Items<GuildMember>


    /**
     * 等同于 [KookGuild.members]
     */
    @ExperimentalSimbotAPI
    override val roles: Items<KookGuildRole>

    // region send api
    /**
     * 根据 [SendChannelMessageApi] api 构建并发送消息。
     */
    @ST
    public suspend fun send(request: SendChannelMessageApi): KookMessageReceipt {
        return request.requestDataBy(bot).asReceipt(false, bot)
    }

    /**
     * 根据 [SendChannelMessageApi] api 构建并发送消息。
     */
    @ST
    public suspend fun send(
        type: Int,
        content: String,
        quote: ID?,
        nonce: String?,
        tempTargetId: ID?,
    ): KookMessageReceipt {
        val request = SendChannelMessageApi.create(type, source.id, content, quote?.literal, nonce, tempTargetId?.literal)
        return send(request)
    }


    /**
     * 发送纯文本消息，并指定 [tempTargetId].
     *
     * @see SendChannelMessageApi.tempTargetId
     */
    @ST
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
     * @see SendChannelMessageApi.tempTargetId
     * @see SendChannelMessageApi.quote
     */
    @ST
    public suspend fun send(message: Message, quote: ID? = null, tempTargetId: ID? = null): KookMessageReceipt

    /**
     * 发送消息，并可选的指定 [quote] 和 [tempTargetId].
     *
     * @see SendChannelMessageApi.tempTargetId
     * @see SendChannelMessageApi.quote
     */
    @ST
    public suspend fun send(
        message: MessageContent,
        quote: ID? = null,
        tempTargetId: ID? = null,
    ): KookMessageReceipt


    /**
     * 发送纯文本消息。
     */
    @ST
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
    @ST
    override suspend fun send(message: Message): KookMessageReceipt = send(message, null)

    /**
     * 发送消息。
     */
    @ST
    override suspend fun send(message: MessageContent): KookMessageReceipt = send(message, null)

    // endregion


    /**
     * Deprecated: KOOK 中不支持 Channel 级别的禁言。
     */
    @Deprecated("Channel mute is not supported in KOOK", ReplaceWith("false"))
    override suspend fun mute(duration: Duration): Boolean = false


    /**
     * Deprecated: KOOK 中不支持 Channel 级别的禁言。
     */
    @Deprecated("Channel mute is not supported in KOOK", ReplaceWith("false"))
    override suspend fun unmute(): Boolean = false


}
