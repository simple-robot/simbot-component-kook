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