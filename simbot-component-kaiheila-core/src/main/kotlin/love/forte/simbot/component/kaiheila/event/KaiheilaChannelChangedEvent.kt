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

package love.forte.simbot.component.kaiheila.event

import love.forte.simbot.Api4J
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.component.kaiheila.KaiheilaChannel
import love.forte.simbot.component.kaiheila.KaiheilaGuild
import love.forte.simbot.component.kaiheila.KaiheilaGuildMember
import love.forte.simbot.component.kaiheila.message.KaiheilaChannelMessageDetailsContent.Companion.toContent
import love.forte.simbot.component.kaiheila.util.requestDataBy
import love.forte.simbot.definition.ChannelInfoContainer
import love.forte.simbot.event.*
import love.forte.simbot.kaiheila.api.message.MessageViewRequest
import love.forte.simbot.kaiheila.event.system.channel.*
import love.forte.simbot.message.MessageContent
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.utils.runInBlocking

/**
 *
 * 开黑啦系统事件中与频道变更事件相关的事件针对simbot标准事件的实现基准类。
 *
 * 涉及的相关开黑啦**原始**事件有：
 * - [AddedChannelEvent]
 * - [UpdatedChannelEvent]
 * - [DeletedChannelEvent]
 * - [UnpinnedMessageEvent]
 * - [PinnedMessageEvent]
 *
 *
 * @see ChangedEvent
 *
 * @author ForteScarlet
 */
@BaseEvent
public abstract class KaiheilaChannelChangedEvent<out Body : ChannelEventExtraBody> :
    KaiheilaSystemEvent<Body>(), ChangedEvent {

    /**
     * 此事件涉及的频道所属的频道服务器。
     */
    @OptIn(Api4J::class)
    abstract override val source: KaiheilaGuild

    /**
     * 此事件涉及的频道所属的频道服务器。
     */
    @JvmSynthetic
    override suspend fun source(): KaiheilaGuild = source

    //// Impl


    override val changedTime: Timestamp
        get() = sourceEvent.msgTimestamp

    abstract override val key: Event.Key<out KaiheilaChannelChangedEvent<*>>

    public companion object Key : BaseEventKey<KaiheilaChannelChangedEvent<*>>(
        "kaiheila.channel_changed", KaiheilaSystemEvent, ChangedEvent
    ) {
        override fun safeCast(value: Any): KaiheilaChannelChangedEvent<*>? = doSafeCast(value)
    }
}

/**
 * 某频道服务器中新增了一个频道后的事件。
 *
 * @see IncreaseEvent
 * @see AddedChannelEvent
 */
public abstract class KaiheilaAddedChannelChangedEvent :
    KaiheilaChannelChangedEvent<AddedChannelExtraBody>(),
    IncreaseEvent {

    /**
     * 操作者，即此频道的创建者。
     *
     * 创建者获取自 [AddedChannelExtraBody.masterId],
     * 如果在此事件实例化的过程中此人离开频道服务器导致内置缓存被清理，则可能得到null。
     *
     */
    public abstract val operator: KaiheilaGuildMember?

    /**
     * 增加的频道。
     */
    @OptIn(Api4J::class)
    abstract override val after: KaiheilaChannel

    /**
     * 增加的频道。
     */
    @JvmSynthetic
    override suspend fun after(): KaiheilaChannel = after


    override val key: Event.Key<out KaiheilaAddedChannelChangedEvent>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaAddedChannelChangedEvent>(
        "kaiheila.added_channel_changed", KaiheilaChannelChangedEvent, IncreaseEvent
    ) {
        override fun safeCast(value: Any): KaiheilaAddedChannelChangedEvent? = doSafeCast(value)
    }
}

/**
 * 某频道发生了信息变更。
 *
 * _Note: 无法获取变更前的信息，[before] 恒为null_
 *
 * @see UpdatedChannelEvent
 */
public abstract class KaiheilaUpdatedChannelChangedEvent :
    KaiheilaChannelChangedEvent<UpdatedChannelExtraBody>(),
    ChangedEvent,
    ChannelInfoContainer {


    @OptIn(Api4J::class)
    abstract override val channel: KaiheilaChannel

    /**
     * 频道信息。
     */
    @JvmSynthetic
    override suspend fun channel(): KaiheilaChannel = channel

    /**
     * 无法获取变更前的信息，[before] 恒为null。
     */
    @OptIn(Api4J::class)
    override val before: UpdatedChannelExtraBody?
        get() = null

    /**
     * 无法获取变更前的信息，[before] 恒为null。
     */
    @JvmSynthetic
    override suspend fun before(): UpdatedChannelExtraBody? = before

    /**
     * 信息变更内容。
     */
    @OptIn(Api4J::class)
    override val after: UpdatedChannelExtraBody get() = sourceBody

    /**
     * 信息变更内容。
     */
    @JvmSynthetic
    override suspend fun after(): UpdatedChannelExtraBody = after


    override val key: Event.Key<out KaiheilaUpdatedChannelChangedEvent>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaUpdatedChannelChangedEvent>(
        "kaiheila.updated_channel_changed", KaiheilaChannelChangedEvent, ChangedEvent
    ) {
        override fun safeCast(value: Any): KaiheilaUpdatedChannelChangedEvent? = doSafeCast(value)
    }
}

/**
 * 某频道被删除的事件。
 *
 * @see DeletedChannelEvent
 */
public abstract class KaiheilaDeletedChannelChangedEvent :
    KaiheilaChannelChangedEvent<DeletedChannelExtraBody>(),
    DecreaseEvent {

    /**
     * 被删除的频道。
     */
    @OptIn(Api4J::class)
    abstract override val before: KaiheilaChannel

    /**
     * 被删除的频道。
     */
    @JvmSynthetic
    override suspend fun before(): KaiheilaChannel = before

    /**
     * 始终为null。
     */
    @OptIn(Api4J::class)
    override val after: KaiheilaChannel?
        get() = null

    /**
     * 始终为null。
     */
    @JvmSynthetic
    override suspend fun after(): KaiheilaChannel? = null


    override val key: Event.Key<out KaiheilaDeletedChannelChangedEvent>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaDeletedChannelChangedEvent>(
        "kaiheila.deleted_channel_changed", KaiheilaChannelChangedEvent, DecreaseEvent
    ) {

        override fun safeCast(value: Any): KaiheilaDeletedChannelChangedEvent? = doSafeCast(value)
    }
}


/**
 * 与频道消息置顶相关的事件。
 * 涉及的原始事件有：
 * - [PinnedMessageEvent]
 * - [UnpinnedMessageEvent]
 *
 * 此事件为 [ChangedEvent], 事件源为发生事件的频道服务器，变动主体为被设置为置顶消息 **的ID**。
 * 由于事件无法确定变更前的消息，因此此事件的实现子事件中，[before] 和 [after] 只可能有一个不为null。
 *
 * 如果你只关心相关消息的ID，可以直接使用 [msgId] 属性获取。
 *
 */
@BaseEvent
public abstract class KaiheilaMessagePinEvent<Body : ChannelEventExtraBody> :
    KaiheilaChannelChangedEvent<Body>(),
    ChangedEvent, ChannelInfoContainer {

    /**
     * 此事件涉及的频道信息。
     */
    @OptIn(Api4J::class)
    abstract override val channel: KaiheilaChannel


    /**
     * 此事件涉及的频道信息。
     */
    @JvmSynthetic
    override suspend fun channel(): KaiheilaChannel = channel


    /**
     * 此事件涉及的操作者。会通过 [operatorId] 获取。
     *
     * 假若在此事件触发前的瞬间此人离开频道，则可能造成无法获取的情况。
     */
    public abstract val operator: KaiheilaGuildMember?

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
     * 变更前ID。如果此事件是 [KaiheilaUnpinnedMessageEvent], 则有值，否则为null。
     * 有值时同 [msgId].
     */
    @OptIn(Api4J::class)
    abstract override val before: ID?

    /**
     * 变更前ID。如果此事件是 [KaiheilaUnpinnedMessageEvent], 则有值，否则为null。
     * 有值时同 [msgId].
     */
    @JvmSynthetic
    abstract override suspend fun before(): ID?

    /**
     * 变更后ID。如果此事件是 [KaiheilaUnpinnedMessageEvent], 则有值，否则为null。
     * 有值时同 [msgId].
     */
    @OptIn(Api4J::class)
    abstract override val after: ID?

    /**
     * 变更后ID。如果此事件是 [KaiheilaUnpinnedMessageEvent], 则有值，否则为null。
     * 有值时同 [msgId].
     */
    @JvmSynthetic
    abstract override suspend fun after(): ID?


    //// Api

    /**
     * 通过 [msgId] 查询这条被置顶的消息。
     */
    @JvmSynthetic
    public suspend fun queryMsg(): MessageContent {
        val messageView = MessageViewRequest(msgId).requestDataBy(bot)
        return messageView.toContent()
    }

    /**
     * 通过 [msgId] 查询这条置顶相关的消息。
     */
    @Api4J
    public fun queryMsgBlocking(): MessageContent = runInBlocking { queryMsg() }


    override val key: Event.Key<out KaiheilaMessagePinEvent<*>>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaMessagePinEvent<*>>(
        "kaiheila.message_pin", KaiheilaChannelChangedEvent, ChangedEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMessagePinEvent<*>? = doSafeCast(value)
    }
}


/**
 *
 * 新消息置顶事件。此事件的body也属于 [ChannelEventExtraBody] 类型的自类型，
 * 代表一个新的消息被设置为了目标频道的置顶消息。
 *
 * 此事件为 [ChangedEvent], 事件源为发生事件的频道服务器，变动主体为被设置为置顶消息 **的ID**。
 * 由于事件无法确定变更前的消息，因此只能获取到 **变更后的**，而 [before] 恒为null。
 *
 * @see ChannelEventExtraBody
 * @see PinnedMessageEvent
 */
public abstract class KaiheilaPinnedMessageEvent :
    KaiheilaMessagePinEvent<PinnedMessageExtraBody>() {

    /**
     * 涉及消息的ID。
     *
     */
    override val msgId: ID
        get() = sourceBody.msgId


    /**
     * 操作者ID。
     */
    override val operatorId: ID
        get() = sourceBody.operatorId

    /**
     * 频道ID。
     */
    override val channelId: ID
        get() = sourceBody.channelId

    /**
     * 始终为null。
     */
    override val before: ID?
        get() = null

    /**
     * 始终为null。
     */
    @JvmSynthetic
    override suspend fun before(): ID? = null

    /**
     * 同 [msgId].
     */
    override val after: ID
        get() = msgId

    /**
     * 同 [msgId].
     */
    @JvmSynthetic
    override suspend fun after(): ID = after

    override val key: Event.Key<out KaiheilaPinnedMessageEvent>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaPinnedMessageEvent>(
        "kaiheila.pinned_message", KaiheilaMessagePinEvent
    ) {
        override fun safeCast(value: Any): KaiheilaPinnedMessageEvent? = doSafeCast(value)
    }

}

/**
 *
 * 消息取消置顶事件。此事件的body也属于 [ChannelEventExtraBody] 类型的自类型，
 * 代表一个新的消息被设置为了目标频道的置顶消息。
 *
 * 此事件为 [ChangedEvent], 事件源为发生事件的频道服务器，变动主体为被设置为置顶消息 **的ID**。
 * 由于事件无法确定变更前的消息，因此只能获取到 **删除前的** 消息ID，而 [after] 恒为null。
 *
 * @see ChannelEventExtraBody
 * @see UnpinnedMessageEvent
 */
public abstract class KaiheilaUnpinnedMessageEvent :
    KaiheilaMessagePinEvent<UnpinnedMessageExtraBody>() {

    //// Impl
    override val msgId: ID
        get() = sourceBody.msgId

    override val operatorId: ID
        get() = sourceBody.operatorId

    override val channelId: ID
        get() = sourceBody.channelId

    /**
     * 同 [msgId].
     */

    override val before: ID
        get() = msgId

    /**
     * 同 [msgId].
     */

    @JvmSynthetic
    override suspend fun before(): ID = before

    /**
     * 始终为null。
     */
    override val after: ID?
        get() = null

    /**
     * 始终为null。
     */
    @JvmSynthetic
    override suspend fun after(): ID? = null


    override val key: Event.Key<out KaiheilaUnpinnedMessageEvent>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaUnpinnedMessageEvent>(
        "kaiheila.unpinned_message", KaiheilaMessagePinEvent
    ) {
        override fun safeCast(value: Any): KaiheilaUnpinnedMessageEvent? = doSafeCast(value)
    }

}

