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

package love.forte.simbot.component.kook.event

import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.event.BaseEventKey
import love.forte.simbot.kook.event.DeletedMessageEventExtra
import love.forte.simbot.kook.event.DeletedPrivateMessageEventExtra
import love.forte.simbot.kook.event.Event
import love.forte.simbot.kook.event.SystemExtra
import love.forte.simbot.message.doSafeCast


/**
 * KOOK 系统事件中与 _消息删除_ 相关的事件的simbot事件基准类。
 *
 * 涉及的 KOOK 原始事件 (的 [SystemExtra] 子类型) 有：
 * - [DeletedMessageEventExtra]
 * - [DeletedPrivateMessageEventExtra]
 *
 * @author ForteScarlet
 *
 * @see KookSystemEvent
 */
public abstract class KookDeletedMessageEvent : KookSystemEvent() {
    /**
     * 源事件
     */
    abstract override val sourceEvent: Event<SystemExtra>

    /**
     * 源事件中的事件体
     */
    abstract override val sourceBody: Any?

    /**
     * 被删除的消息的ID
     */
    public abstract val deletedMsgId: ID

    /**
     * 消息被删除的时间
     */
    abstract override val time: Timestamp

    abstract override val key: love.forte.simbot.event.Event.Key<out KookDeletedMessageEvent>

    public companion object Key : BaseEventKey<KookDeletedMessageEvent>(
        "kook.deleted_message_event", KookSystemEvent
    ) {
        override fun safeCast(value: Any): KookDeletedMessageEvent? = doSafeCast(value)
    }
}


/**
 * KOOK中一个频道消息被删除的事件。
 *
 * @see KookDeletedMessageEvent
 *
 */
public abstract class KookDeletedChannelMessageEvent : KookDeletedMessageEvent() {
    /**
     * 频道消息被删除事件的源事件
     */
    abstract override val sourceEvent: Event<DeletedMessageEventExtra>

    /**
     * [sourceEvent] 的事件体。
     *
     * @see DeletedMessageEventExtra.Body
     */
    override val sourceBody: DeletedMessageEventExtra.Body
        get() = sourceEvent.extra.body

    /**
     * 被删除的消息的ID
     */
    override val deletedMsgId: ID
        get() = sourceBody.msgId.ID

    /**
     * 消息删除时间。来自 [Event.msgTimestamp]
     */
    override val time: Timestamp
        get() = Timestamp.ofMilliseconds(sourceEvent.msgTimestamp)


    override val key: love.forte.simbot.event.Event.Key<KookDeletedChannelMessageEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookDeletedChannelMessageEvent>(
        "kook.deleted_channel_message_event", KookDeletedMessageEvent
    ) {
        override fun safeCast(value: Any): KookDeletedChannelMessageEvent? = doSafeCast(value)
    }
}

/**
 * KOOK中一个私聊消息被删除的事件。
 *
 * @see KookDeletedMessageEvent
 *
 */
public abstract class KookDeletedPrivateMessageEvent : KookDeletedMessageEvent() {
    /**
     * 私聊消息被删除事件的源事件
     */
    abstract override val sourceEvent: Event<DeletedPrivateMessageEventExtra>

    /**
     * [sourceEvent] 的事件体。
     *
     * @see DeletedPrivateMessageEventExtra.Body
     */
    override val sourceBody: DeletedPrivateMessageEventExtra.Body
        get() = sourceEvent.extra.body

    /**
     * 被删除的消息的ID
     */
    override val deletedMsgId: ID
        get() = sourceBody.msgId.ID

    /**
     * 被删除消息的创建者ID
     */
    public val authorId: ID
        get() = sourceBody.authorId.ID

    /**
     * 被删除的消息的目标用户ID
     */
    public val targetId: ID
        get() = sourceBody.targetId.ID

    /**
     * 私聊 code
     */
    public val chatCode: String
        get() = sourceBody.chatCode

    /**
     * 消息删除时间。来自 [DeletedPrivateMessageEventExtra.Body.deletedAt]
     */
    override val time: Timestamp
        get() = Timestamp.ofMilliseconds(sourceBody.deletedAt)


    override val key: love.forte.simbot.event.Event.Key<KookDeletedPrivateMessageEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookDeletedPrivateMessageEvent>(
        "kook.deleted_private_message_event", KookDeletedMessageEvent
    ) {
        override fun safeCast(value: Any): KookDeletedPrivateMessageEvent? = doSafeCast(value)
    }
}


