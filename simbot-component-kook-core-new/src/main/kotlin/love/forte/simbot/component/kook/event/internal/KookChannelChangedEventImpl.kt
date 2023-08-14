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

@file:Suppress("UnnecessaryOptInAnnotation")

package love.forte.simbot.component.kook.event.internal

import love.forte.simbot.Api4J
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.component.kook.KookChannel
import love.forte.simbot.component.kook.KookGuild
import love.forte.simbot.component.kook.bot.internal.KookBotImpl
import love.forte.simbot.component.kook.event.*
import love.forte.simbot.component.kook.internal.*
import love.forte.simbot.component.kook.message.KookChannelMessageDetailsContent.Companion.toContent
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.event.IncreaseEvent
import love.forte.simbot.kook.api.message.GetChannelMessageViewApi
import love.forte.simbot.kook.event.*
import love.forte.simbot.message.MessageContent
import love.forte.simbot.kook.event.Event as KEvent

/**
 * 某频道服务器中新增了一个频道后的事件。
 *
 * @see IncreaseEvent
 * @see AddedChannelEvent
 */
internal data class KookAddedChannelEventImpl(
    override val bot: KookBotImpl,
    override val sourceEvent: KEvent<AddedChannelEventExtra>,
    private val _source: KookGuildImpl,
    private val _after: KookChannelImpl,
) : KookAddedChannelEvent() {
    override val operator: KookMemberImpl? = bot.internalMember(sourceBody.guildId, sourceBody.userId)
    override suspend fun source(): KookGuild = _source
    override suspend fun channel(): KookChannel = _after
}

/**
 * 频道更新事件impl
 */
@Suppress("UnnecessaryOptInAnnotation")
internal data class KookUpdatedChannelEventImpl(
    override val bot: KookBotImpl,
    override val sourceEvent: KEvent<UpdatedChannelEventExtra>,
    private val _source: KookGuildImpl,
    private val _channel: KookChannelImpl,
) : KookUpdatedChannelEvent() {
    override suspend fun source(): KookGuild = _source
    override suspend fun channel(): KookChannel = _channel
}


/**
 * 频道删除事件impl
 */
@Suppress("UnnecessaryOptInAnnotation")
@OptIn(Api4J::class, ExperimentalSimbotApi::class)
internal data class KookDeletedChannelEventImpl(
    override val bot: KookBotImpl,
    override val sourceEvent: KEvent<DeletedChannelEventExtra>,
    private val _source: KookGuildImpl,
    private val _before: KookChannelImpl,
) : KookDeletedChannelEvent() {
    override suspend fun source(): KookGuild = _source
    override suspend fun before(): KookChannel = _before
}

/**
 * 消息置顶事件impl。
 */
@Suppress("UnnecessaryOptInAnnotation")
@OptIn(Api4J::class)
internal data class KookPinnedMessageEventImpl(
    override val bot: KookBotImpl,
    override val sourceEvent: KEvent<PinnedMessageEventExtra>,
    private val _source: KookGuildImpl,
    private val _channel: KookChannelImpl,
) : KookPinnedMessageEvent() {
    override val operator: KookMemberImpl? = bot.internalMember(sourceEvent.targetId, sourceBody.operatorId)

    override suspend fun source(): KookGuild = _source
    override suspend fun channel(): KookChannel = _channel

    override suspend fun queryMsg(): MessageContent {
        return GetChannelMessageViewApi.create(sourceBody.msgId).requestDataBy(bot).toContent(bot)
    }
}

/**
 * 消息取消置顶事件impl。
 */
@Suppress("UnnecessaryOptInAnnotation")
@OptIn(Api4J::class)
internal data class KookUnpinnedMessageEventImpl(
    override val bot: KookBotImpl,
    override val sourceEvent: KEvent<UnpinnedMessageEventExtra>,
    private val _source: KookGuildImpl,
    private val _channel: KookChannelImpl,
) : KookUnpinnedMessageEvent() {
    override val operator: KookMemberImpl? = bot.internalMember(sourceEvent.targetId, sourceBody.operatorId)

    override suspend fun source(): KookGuild = _source
    override suspend fun channel(): KookChannel = _channel

    override suspend fun queryMsg(): MessageContent {
        return GetChannelMessageViewApi.create(sourceBody.msgId).requestDataBy(bot).toContent(bot)
    }
}
