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
import love.forte.simbot.JST
import love.forte.simbot.JSTP
import love.forte.simbot.component.kook.KookChannel
import love.forte.simbot.component.kook.KookMember
import love.forte.simbot.definition.ChannelInfoContainer
import love.forte.simbot.event.BaseEvent
import love.forte.simbot.event.BaseEventKey
import love.forte.simbot.event.ChangedEvent
import love.forte.simbot.event.Event
import love.forte.simbot.kook.event.PingEventExtraBody
import love.forte.simbot.kook.event.PinnedMessageEventExtra
import love.forte.simbot.kook.event.UnpinnedMessageEventExtra
import love.forte.simbot.message.MessageContent
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.kook.event.Event as KEvent


/**
 * 与频道消息置顶相关的事件。
 * 涉及的原始事件有：
 * - [PinnedMessageEventExtra]
 * - [UnpinnedMessageEventExtra]
 *
 * 此事件为 [ChangedEvent], 事件源为发生事件的频道服务器，变动主体为被设置为置顶消息 **的ID**。
 * 由于事件无法确定变更前的消息，因此此事件的实现子事件中，[before] 和 [after] 只可能有一个不为null。
 *
 * 如果你只关心相关消息的ID，可以直接使用 [msgId] 属性获取。
 *
 */
@BaseEvent
public abstract class KookMessagePinEvent : KookChannelChangedEvent(),
    ChangedEvent, ChannelInfoContainer {

    /**
     * 此事件涉及的频道信息。
     */
    @JSTP
    abstract override suspend fun channel(): KookChannel


    /**
     * 此事件涉及的操作者。会通过 [operatorId] 获取。
     *
     * 假若在此事件触发前的瞬间此人离开频道，则可能造成无法获取的情况。
     */
    public abstract val operator: KookMember?

    /**
     * 涉及消息的ID
     */
    public abstract val msgId: ID

    /**
     * 操作人ID
     */
    public abstract val operatorId: ID

    /**
     * 涉及频道ID
     */
    public abstract val channelId: ID

    /**
     * 变更前ID。如果此事件是 [KookUnpinnedMessageEvent], 则有值，否则为null。
     * 有值时同 [msgId].
     */
    @JSTP
    abstract override suspend fun before(): ID?

    /**
     * 变更后ID。如果此事件是 [KookUnpinnedMessageEvent], 则有值，否则为null。
     * 有值时同 [msgId].
     */
    @JSTP
    abstract override suspend fun after(): ID?


    //// API

    /**
     * 通过 [msgId] 查询这条被置顶的消息。
     */
    @JST
    public abstract suspend fun queryMsg(): MessageContent
//        val messageView = GetChannelMessageViewApi.create(msgId).requestDataBy(bot)
//        return messageView.toContent(bot)


    override val key: Event.Key<out KookMessagePinEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookMessagePinEvent>(
        "kook.message_pin", KookChannelChangedEvent, ChangedEvent
    ) {
        override fun safeCast(value: Any): KookMessagePinEvent? = doSafeCast(value)
    }
}


/**
 *
 * 新消息置顶事件。
 * 代表一个新的消息被设置为了目标频道的置顶消息。
 *
 * 此事件为 [ChangedEvent], 事件源为发生事件的频道服务器，变动主体为被设置为置顶消息 **的ID**。
 * 由于事件无法确定变更前的消息，因此只能获取到 **变更后的**，而 [before] 恒为null。
 *
 * @see PinnedMessageEventExtra
 */
public abstract class KookPinnedMessageEvent : KookMessagePinEvent() {

    abstract override val sourceEvent: KEvent<PinnedMessageEventExtra>

    override val sourceBody: PingEventExtraBody
        get() = sourceEvent.extra.body

    /**
     * 涉及消息的ID。
     */
    override val msgId: ID get() = sourceBody.msgId.ID


    /**
     * 操作者ID。
     */
    override val operatorId: ID get() = sourceBody.operatorId.ID

    /**
     * 频道ID。
     */
    override val channelId: ID get() = sourceBody.channelId.ID

    /**
     * 始终为null。
     */
    @JvmSynthetic
    override suspend fun before(): ID? = null

    /**
     * 同 [msgId].
     */
    @JvmSynthetic
    override suspend fun after(): ID = msgId

    override val key: Event.Key<out KookPinnedMessageEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookPinnedMessageEvent>(
        "kook.pinned_message", KookMessagePinEvent
    ) {
        override fun safeCast(value: Any): KookPinnedMessageEvent? = doSafeCast(value)
    }

}

/**
 *
 * 消息取消置顶事件。代表一个新的消息被设置为了目标频道的置顶消息。
 *
 * 此事件为 [ChangedEvent], 事件源为发生事件的频道服务器，变动主体为被设置为置顶消息 **的ID**。
 * 由于事件无法确定变更前的消息，因此只能获取到 **删除前的** 消息ID，而 [after] 恒为null。
 *
 * @see UnpinnedMessageEventExtra
 */
public abstract class KookUnpinnedMessageEvent : KookMessagePinEvent() {

    abstract override val sourceEvent: KEvent<UnpinnedMessageEventExtra>

    override val sourceBody: PingEventExtraBody
        get() = sourceEvent.extra.body

    override val msgId: ID get() = sourceBody.msgId.ID

    override val operatorId: ID get() = sourceBody.operatorId.ID

    override val channelId: ID get() = sourceBody.channelId.ID

    /**
     * 同 [msgId].
     */
    @JvmSynthetic
    override suspend fun before(): ID = msgId

    /**
     * 始终为null。
     */
    @JvmSynthetic
    override suspend fun after(): ID? = null


    override val key: Event.Key<out KookUnpinnedMessageEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookUnpinnedMessageEvent>(
        "kook.unpinned_message", KookMessagePinEvent
    ) {
        override fun safeCast(value: Any): KookUnpinnedMessageEvent? = doSafeCast(value)
    }

}

