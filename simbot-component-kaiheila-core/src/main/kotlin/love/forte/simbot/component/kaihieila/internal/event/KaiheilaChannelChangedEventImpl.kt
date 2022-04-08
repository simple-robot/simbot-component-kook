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

package love.forte.simbot.component.kaihieila.internal.event

import love.forte.simbot.component.kaihieila.event.*
import love.forte.simbot.component.kaihieila.internal.KaiheilaChannelImpl
import love.forte.simbot.component.kaihieila.internal.KaiheilaComponentBotImpl
import love.forte.simbot.component.kaihieila.internal.KaiheilaGuildImpl
import love.forte.simbot.component.kaihieila.internal.KaiheilaGuildMemberImpl
import love.forte.simbot.event.IncreaseEvent
import love.forte.simbot.kaiheila.event.Event.Extra.Sys
import love.forte.simbot.kaiheila.event.system.channel.*
import love.forte.simbot.kaiheila.event.Event as KhlEvent

/**
 * 某频道服务器中新增了一个频道后的事件。
 *
 * @see IncreaseEvent
 * @see AddedChannelEvent
 */
internal data class KaiheilaAddedChannelChangedEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: KhlEvent<Sys<AddedChannelExtraBody>>,
    override val source: KaiheilaGuildImpl,
    override val target: KaiheilaChannelImpl,
) : KaiheilaAddedChannelChangedEvent() {
    override val operator: KaiheilaGuildMemberImpl? = source.internalMember(sourceBody.masterId)
}

/**
 * 频道更新事件impl
 */
internal data class KaiheilaUpdatedChannelChangedEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: KhlEvent<Sys<UpdatedChannelExtraBody>>,
    override val source: KaiheilaGuildImpl,
    override val channel: KaiheilaChannelImpl,
) : KaiheilaUpdatedChannelChangedEvent()


/**
 * 频道删除事件impl
 */
internal data class KaiheilaDeletedChannelChangedEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: KhlEvent<Sys<DeletedChannelExtraBody>>,
    override val source: KaiheilaGuildImpl,
    override val target: KaiheilaChannelImpl,
) : KaiheilaDeletedChannelChangedEvent()

/**
 * 消息置顶事件impl。
 */
internal data class KaiheilaPinnedMessageEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: KhlEvent<Sys<PinnedMessageExtraBody>>,
    override val source: KaiheilaGuildImpl,
    override val channel: KaiheilaChannelImpl,
    override val operator: KaiheilaGuildMemberImpl?
) : KaiheilaPinnedMessageEvent()

/**
 * 消息取消置顶事件impl。
 */
internal data class KaiheilaUnpinnedMessageEventImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val sourceEvent: KhlEvent<Sys<UnpinnedMessageExtraBody>>,
    override val source: KaiheilaGuildImpl,
    override val channel: KaiheilaChannelImpl,
    override val operator: KaiheilaGuildMemberImpl?
) : KaiheilaUnpinnedMessageEvent()