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

package love.forte.simbot.component.kook.event

import love.forte.simbot.Api4J
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.event.BaseEventKey
import love.forte.simbot.event.ChangedEvent
import love.forte.simbot.event.Event
import love.forte.simbot.kook.event.system.user.UserUpdatedEvent
import love.forte.simbot.kook.event.system.user.UserUpdatedEventBody
import love.forte.simbot.message.doSafeCast

/**
 * Kook 用户信息更新事件。
 * 此事件属于一个 [ChangedEvent],
 * 变更的[源][ChangedEvent.source]为发送变更的用户 **的ID**
 * （因为此事件不一定是某个具体频道服务器中的用户，只要有好友关系即会推送），
 * 变更的 [前][ChangedEvent.before]由于无法获取而始终为null，
 * [后][ChangedEvent.after] 为用户变更事件的内容本体，即 [UserUpdatedEventBody] 。
 *
 * @see UserUpdatedEvent
 */
public abstract class KookUserUpdatedEvent :
    KookSystemEvent<UserUpdatedEventBody>(),
    ChangedEvent {

    override val changedTime: Timestamp
        get() = sourceEvent.msgTimestamp


    /**
     * 变化源。为发生变更的用户的id。
     */
    @OptIn(Api4J::class)
    override val source: ID
        get() = sourceBody.userId

    /**
     * 变化源。为发生变更的用户的id。
     */
    @JvmSynthetic
    override suspend fun source(): ID = source

    /**
     * before 无法确定，始终为null。
     */
    @OptIn(Api4J::class)
    override val before: Any?
        get() = null

    /**
     * before 无法确定，始终为null。
     */
    @JvmSynthetic
    override suspend fun before(): Any? = null


    /**
     * 变化事件的主要内容。
     */
    @OptIn(Api4J::class)
    override val after: UserUpdatedEventBody
        get() = sourceBody

    /**
     * 变化事件的主要内容。
     */
    @JvmSynthetic
    override suspend fun after(): UserUpdatedEventBody = after


    override val key: Event.Key<out KookUserUpdatedEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookUserUpdatedEvent>(
        "kook.user_updated", KookSystemEvent
    ) {
        override fun safeCast(value: Any): KookUserUpdatedEvent? = doSafeCast(value)
    }
}