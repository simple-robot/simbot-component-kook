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

package love.forte.simbot.component.kook.internal.event

import love.forte.simbot.Api4J
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.component.kook.KookChannel
import love.forte.simbot.component.kook.KookGuild
import love.forte.simbot.component.kook.event.*
import love.forte.simbot.component.kook.internal.KookChannelImpl
import love.forte.simbot.component.kook.internal.KookComponentBotImpl
import love.forte.simbot.component.kook.internal.KookGuildImpl
import love.forte.simbot.component.kook.internal.KookGuildMemberImpl
import love.forte.simbot.event.IncreaseEvent
import love.forte.simbot.kook.event.Event.Extra.Sys
import love.forte.simbot.kook.event.system.channel.*
import love.forte.simbot.kook.event.Event as KkEvent

/**
 * 某频道服务器中新增了一个频道后的事件。
 *
 * @see IncreaseEvent
 * @see AddedChannelEvent
 */
@OptIn(Api4J::class)
internal data class KookAddedChannelChangedEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: KkEvent<Sys<AddedChannelExtraBody>>,
    private val _source: KookGuildImpl,
    private val _after: KookChannelImpl,
) : KookAddedChannelChangedEvent() {
    override val operator: KookGuildMemberImpl? = _source.internalMember(sourceBody.masterId)
    override suspend fun source(): KookGuild = _source
    override suspend fun after(): KookChannel = _after
}

/**
 * 频道更新事件impl
 */
@Suppress("UnnecessaryOptInAnnotation")
@OptIn(Api4J::class)
internal data class KookUpdatedChannelChangedEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: KkEvent<Sys<UpdatedChannelExtraBody>>,
    private val _source: KookGuildImpl,
    private val _channel: KookChannelImpl,
) : KookUpdatedChannelChangedEvent() {
    override suspend fun source(): KookGuild = _source
    override suspend fun channel(): KookChannel = _channel
}


/**
 * 频道删除事件impl
 */
@Suppress("UnnecessaryOptInAnnotation")
@OptIn(Api4J::class, ExperimentalSimbotApi::class)
internal data class KookDeletedChannelChangedEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: KkEvent<Sys<DeletedChannelExtraBody>>,
    private val _source: KookGuildImpl,
    private val _before: KookChannelImpl,
) : KookDeletedChannelChangedEvent() {
    override suspend fun source(): KookGuild = _source
    override suspend fun before(): KookChannel = _before
}

/**
 * 消息置顶事件impl。
 */
@Suppress("UnnecessaryOptInAnnotation")
@OptIn(Api4J::class)
internal data class KookPinnedMessageEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: KkEvent<Sys<PinnedMessageExtraBody>>,
    private val _source: KookGuildImpl,
    private val _channel: KookChannelImpl,
    override val operator: KookGuildMemberImpl?,
) : KookPinnedMessageEvent() {
    override suspend fun source(): KookGuild = _source
    override suspend fun channel(): KookChannel = _channel
}

/**
 * 消息取消置顶事件impl。
 */
@Suppress("UnnecessaryOptInAnnotation")
@OptIn(Api4J::class)
internal data class KookUnpinnedMessageEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: KkEvent<Sys<UnpinnedMessageExtraBody>>,
    private val _source: KookGuildImpl,
    private val _channel: KookChannelImpl,
    override val operator: KookGuildMemberImpl?,
) : KookUnpinnedMessageEvent() {
    override suspend fun source(): KookGuild = _source
    override suspend fun channel(): KookChannel = _channel
}
