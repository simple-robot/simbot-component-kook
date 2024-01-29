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

import love.forte.simbot.Timestamp
import love.forte.simbot.common.time.Timestamp
import love.forte.simbot.component.kook.KookCategoryChannel
import love.forte.simbot.component.kook.KookChatChannel
import love.forte.simbot.component.kook.KookGuild
import love.forte.simbot.component.kook.KookMember
import love.forte.simbot.definition.Channel
import love.forte.simbot.event.*
import love.forte.simbot.kook.event.*
import love.forte.simbot.kook.objects.SimpleChannel
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.suspendrunner.STP
import love.forte.simbot.kook.event.Event as KEvent

/**
 * KOOK 系统事件中与 _频道变更_ 相关的事件的simbot事件基准类。
 *
 * 涉及的 KOOK 原始事件 (的 [SystemExtra] 子类型) 有：
 * - [AddedChannelEventExtra]
 * - [UpdatedChannelEventExtra]
 * - [DeletedChannelEventExtra]
 *
 * @author ForteScarlet
 *
 * @see KookSystemEvent
 * @see ChangedEvent
 */
public abstract class KookChannelChangedEvent : KookSystemEvent(), ChangedEvent {
    /**
     * 事件涉及子频道所属的频道服务器。
     */
    @STP
    abstract override suspend fun source(): KookGuild

    /**
     * @see KEvent.msgTimestamp
     */
    override val changedTime: Timestamp get() = Timestamp.byMillisecond(sourceEvent.msgTimestamp)

    abstract override val key: Event.Key<out KookChannelChangedEvent>

    public companion object Key : BaseEventKey<KookChannelChangedEvent>(
        "kook.channel_changed", KookSystemEvent, ChangedEvent
    ) {
        override fun safeCast(value: Any): KookChannelChangedEvent? = doSafeCast(value)
    }
}

/**
 * 某频道服务器中新增了一个频道后的事件。
 *
 * @see IncreaseEvent
 * @see AddedChannelEventExtra
 */
public abstract class KookAddedChannelEvent : KookChannelChangedEvent(), IncreaseEvent, ChannelEvent {

    /**
     * 原事件对象
     *
     * @see AddedChannelEventExtra
     */
    abstract override val sourceEvent: KEvent<AddedChannelEventExtra>

    /**
     * @see AddedChannelEventExtra.body
     */
    override val sourceBody: SimpleChannel
        get() = sourceEvent.extra.body

    /**
     * 操作者，即此频道的创建者。
     *
     * 创建者获取自 [sourceBody.userId][SimpleChannel.userId],
     * 如果在此事件实例化的过程中此人离开频道服务器导致内置缓存被清理，则可能得到null。
     *
     */
    public abstract val operator: KookMember?

    /**
     * 增加的频道。
     */
    @STP
    abstract override suspend fun channel(): KookChatChannel

    /**
     * 增加的频道。
     *
     * @see channel
     */
    @STP
    override suspend fun after(): KookChatChannel = channel()

    /**
     * 增加的频道。
     *
     * @see channel
     */
    @STP
    override suspend fun organization(): KookChatChannel = channel()


    override val key: Event.Key<out KookAddedChannelEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookAddedChannelEvent>(
        "kook.added_channel", KookChannelChangedEvent, IncreaseEvent, ChannelEvent
    ) {
        override fun safeCast(value: Any): KookAddedChannelEvent? = doSafeCast(value)
    }
}

/**
 * 某频道发生了信息变更。
 *
 * _Note: 无法获取变更前的信息，[before] 恒为null_
 *
 * @see UpdatedChannelEventExtra
 */
public abstract class KookUpdatedChannelEvent : KookChannelChangedEvent(), ChangedEvent, ChannelEvent {

    /**
     * @see UpdatedChannelEventExtra.body
     */
    override val sourceBody: SimpleChannel
        get() = sourceEvent.extra.body

    /**
     * @see UpdatedChannelEventExtra
     */
    abstract override val sourceEvent: KEvent<UpdatedChannelEventExtra>

    /**
     * 变更后的频道信息
     */
    @STP
    abstract override suspend fun channel(): KookChatChannel


    /**
     * 变更后的频道信息
     *
     * @see channel
     */
    @STP
    override suspend fun organization(): Channel = channel()

    /**
     * 变更后的频道信息
     *
     * @see channel
     */
    @STP
    override suspend fun after(): KookChatChannel = channel()

    /**
     * 恒为null
     */
    @STP
    override suspend fun before(): Any? = null

    override val key: Event.Key<out KookUpdatedChannelEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookUpdatedChannelEvent>(
        "kook.updated_channel", KookChannelChangedEvent, ChangedEvent, ChannelEvent
    ) {
        override fun safeCast(value: Any): KookUpdatedChannelEvent? = doSafeCast(value)
    }
}

/**
 * 某频道被删除的事件。
 *
 * @see DeletedChannelEventExtra
 */
public abstract class KookDeletedChannelEvent : KookChannelChangedEvent(), DecreaseEvent {
    abstract override val sourceEvent: love.forte.simbot.kook.event.Event<DeletedChannelEventExtra>

    override val sourceBody: DeletedChannelEventBody
        get() = sourceEvent.extra.body

    /**
     * 已经被删除的频道。
     */
    @STP
    abstract override suspend fun before(): KookChatChannel

    /**
     * 始终为 null
     */
    @STP
    override suspend fun after(): Any? = null

    override val key: Event.Key<out KookDeletedChannelEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookDeletedChannelEvent>(
        "kook.deleted_channel", KookChannelChangedEvent, DecreaseEvent
    ) {

        override fun safeCast(value: Any): KookDeletedChannelEvent? = doSafeCast(value)
    }
}

/**
 * KOOK 系统事件中与 _频道分组变更_ 相关的事件的simbot事件基准类。
 *
 * 涉及的 KOOK 原始事件 (的 [SystemExtra] 子类型) 有：
 * - [AddedChannelEventExtra]
 * - [UpdatedChannelEventExtra]
 * - [DeletedChannelEventExtra]
 *
 * @author ForteScarlet
 *
 * @see KookSystemEvent
 * @see ChangedEvent
 */
public abstract class KookCategoryChangedEvent : KookSystemEvent(), ChangedEvent {
    /**
     * 事件涉及子频道所属的频道服务器。
     */
    @STP
    abstract override suspend fun source(): KookGuild

    /**
     * @see KEvent.msgTimestamp
     */
    override val changedTime: Timestamp get() = Timestamp.byMillisecond(sourceEvent.msgTimestamp)

    abstract override val key: Event.Key<out KookCategoryChangedEvent>

    public companion object Key : BaseEventKey<KookCategoryChangedEvent>(
        "kook.category_changed", KookSystemEvent, ChangedEvent
    ) {
        override fun safeCast(value: Any): KookCategoryChangedEvent? = doSafeCast(value)
    }
}

/**
 * 某频道服务器中新增了一个频道分组后的事件。
 *
 * @see IncreaseEvent
 * @see AddedChannelEventExtra
 */
public abstract class KookAddedCategoryEvent : KookCategoryChangedEvent(), IncreaseEvent {
    /**
     * 原事件对象
     *
     * @see AddedChannelEventExtra
     */
    abstract override val sourceEvent: KEvent<AddedChannelEventExtra>

    /**
     * @see AddedChannelEventExtra.body
     */
    override val sourceBody: SimpleChannel
        get() = sourceEvent.extra.body

    /**
     * 操作者，即此频道的创建者。
     *
     * 创建者获取自 [sourceBody.userId][SimpleChannel.userId],
     * 如果在此事件实例化的过程中此人离开频道服务器导致内置缓存被清理，则可能得到null。
     *
     */
    public abstract val operator: KookMember?

    /**
     * 增加的频道分组。
     */
    @STP
    public abstract suspend fun category(): KookCategoryChannel

    /**
     * 增加的频道分组。
     *
     * @see category
     */
    @STP
    override suspend fun after(): KookCategoryChannel = category()


    override val key: Event.Key<out KookAddedCategoryEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookAddedCategoryEvent>(
        "kook.added_category", KookCategoryChangedEvent, IncreaseEvent
    ) {
        override fun safeCast(value: Any): KookAddedCategoryEvent? = doSafeCast(value)
    }
}

/**
 * 某频道分组发生了信息变更。
 *
 * _Note: 无法获取变更前的信息，[before] 恒为null_
 *
 * @see UpdatedChannelEventExtra
 */
public abstract class KookUpdatedCategoryEvent : KookCategoryChangedEvent(), ChangedEvent {

    /**
     * @see UpdatedChannelEventExtra.body
     */
    override val sourceBody: SimpleChannel
        get() = sourceEvent.extra.body

    /**
     * @see UpdatedChannelEventExtra
     */
    abstract override val sourceEvent: KEvent<UpdatedChannelEventExtra>

    /**
     * 变更后的频道分组信息
     */
    @STP
    public abstract suspend fun category(): KookCategoryChannel

    /**
     * 变更后的频道分组信息
     *
     * @see category
     */
    @STP
    override suspend fun after(): KookCategoryChannel = category()

    /**
     * 恒为null
     */
    @STP
    override suspend fun before(): Any? = null

    override val key: Event.Key<out KookUpdatedCategoryEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookUpdatedCategoryEvent>(
        "kook.updated_category", KookCategoryChangedEvent, ChangedEvent
    ) {
        override fun safeCast(value: Any): KookUpdatedCategoryEvent? = doSafeCast(value)
    }
}

/**
 * 某频道分组被删除的事件。
 *
 * @see DeletedChannelEventExtra
 */
public abstract class KookDeletedCategoryEvent : KookCategoryChangedEvent(), DecreaseEvent {
    abstract override val sourceEvent: love.forte.simbot.kook.event.Event<DeletedChannelEventExtra>

    override val sourceBody: DeletedChannelEventBody
        get() = sourceEvent.extra.body

    /**
     * 已经被删除的频道分组。
     */
    @STP
    abstract override suspend fun before(): KookCategoryChannel

    /**
     * 始终为 null
     */
    @STP
    override suspend fun after(): Any? = null

    override val key: Event.Key<out KookDeletedCategoryEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookDeletedCategoryEvent>(
        "kook.deleted_category", KookCategoryChangedEvent, DecreaseEvent
    ) {

        override fun safeCast(value: Any): KookDeletedCategoryEvent? = doSafeCast(value)
    }
}








