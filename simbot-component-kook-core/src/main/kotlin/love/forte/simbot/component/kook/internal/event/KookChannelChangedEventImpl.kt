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
    override val source: KookGuildImpl,
    override val after: KookChannelImpl,
) : KookAddedChannelChangedEvent() {
    override val operator: KookGuildMemberImpl? = source.internalMember(sourceBody.masterId)
}

/**
 * 频道更新事件impl
 */
@Suppress("UnnecessaryOptInAnnotation")
@OptIn(Api4J::class)
internal data class KookUpdatedChannelChangedEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: KkEvent<Sys<UpdatedChannelExtraBody>>,
    override val source: KookGuildImpl,
    override val channel: KookChannelImpl,
) : KookUpdatedChannelChangedEvent()


/**
 * 频道删除事件impl
 */
@Suppress("UnnecessaryOptInAnnotation")
@OptIn(Api4J::class)
internal data class KookDeletedChannelChangedEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: KkEvent<Sys<DeletedChannelExtraBody>>,
    override val source: KookGuildImpl,
    override val before: KookChannelImpl,
) : KookDeletedChannelChangedEvent()

/**
 * 消息置顶事件impl。
 */
@Suppress("UnnecessaryOptInAnnotation")
@OptIn(Api4J::class)
internal data class KookPinnedMessageEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: KkEvent<Sys<PinnedMessageExtraBody>>,
    override val source: KookGuildImpl,
    override val channel: KookChannelImpl,
    override val operator: KookGuildMemberImpl?,
) : KookPinnedMessageEvent()

/**
 * 消息取消置顶事件impl。
 */
@Suppress("UnnecessaryOptInAnnotation")
@OptIn(Api4J::class)
internal data class KookUnpinnedMessageEventImpl(
    override val bot: KookComponentBotImpl,
    override val sourceEvent: KkEvent<Sys<UnpinnedMessageExtraBody>>,
    override val source: KookGuildImpl,
    override val channel: KookChannelImpl,
    override val operator: KookGuildMemberImpl?,
) : KookUnpinnedMessageEvent()