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

package love.forte.simbot.component.kook.event

import love.forte.simbot.ID
import love.forte.simbot.JSTP
import love.forte.simbot.Timestamp
import love.forte.simbot.action.ActionType
import love.forte.simbot.component.kook.KookChannel
import love.forte.simbot.component.kook.KookGuild
import love.forte.simbot.component.kook.KookMember
import love.forte.simbot.definition.Organization
import love.forte.simbot.definition.UserInfo
import love.forte.simbot.event.*
import love.forte.simbot.kook.event.*
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.utils.item.Items
import love.forte.simbot.kook.event.Event as KkEvent

/**
 * KOOK 的频道成员变更事件。
 *
 * 相关的 KOOK **原始**事件类型有：
 * - [ExitedChannelEventExtra]
 * - [JoinedChannelEventExtra]
 * - [JoinedGuildEventExtra]
 * - [ExitedGuildEventExtra]
 * - [SelfExitedGuildEventExtra]
 * - [SelfJoinedGuildEventExtra]
 *
 * 其中，[SelfExitedGuildEventExtra] 和 [SelfJoinedGuildEventExtra]
 * 代表为 BOT 自身作为成员的变动，
 * 因此会额外提供相对应的 [bot成员变动][KookBotMemberChangedEvent]
 * 事件类型来进行更精准的事件监听。
 *
 * ## 相关事件
 * ### 频道成员变更事件
 * [KookMemberChannelChangedEvent] 事件及其子类型
 * [KookMemberJoinedChannelEvent]、[KookMemberExitedChannelEvent]
 * 代表了一个频道服务器中的某个群成员加入、离开某一个频道（通常为语音频道）的事件。
 *
 * ### 频道服务器成员变更事件
 * [KookMemberGuildChangedEvent] 事件及其子类型
 * [KookMemberJoinedGuildEvent]、[KookMemberExitedGuildEvent]
 * 代表了一个频道服务器中有新群成员加入、旧成员离开此服务器的事件。
 *
 * ### Bot频道服务器事件
 * [KookBotMemberChangedEvent] 事件及其子类型
 * [KookBotSelfJoinedGuildEvent]、[KookBotSelfExitedGuildEvent]
 * 代表了当前bot加入新频道服务器、离开旧频道服务器的事件。
 *
 * @author forte
 */
@BaseEvent
public abstract class KookMemberChangedEvent : KookSystemEvent(), MemberChangedEvent {
    /**
     * 变更时间。
     */
    override val changedTime: Timestamp get() = Timestamp.byMillisecond(sourceEvent.msgTimestamp)

    /**
     * 本次变更涉及的频道成员信息。同 [user]
     */
    @JSTP
    abstract override suspend fun member(): KookMember

    /**
     * 本次变更涉及的频道成员信息。同 [member]
     */
    @JSTP
    override suspend fun user(): KookMember = member()

    /**
     * 可能存在的变更前成员信息。
     */
    @JSTP
    abstract override suspend fun before(): KookMember?

    /**
     * 可能存在的变更后成员信息。
     */
    @JSTP
    abstract override suspend fun after(): KookMember?

    /**
     * 变更成员所处组织。
     */
    @JSTP
    abstract override suspend fun source(): Organization

    /**
     * 变更成员所处组织。同 [source].
     */
    @JSTP
    override suspend fun organization(): Organization = source()

    /**
     * 可能存在的变更操作者。
     */
    @JSTP
    abstract override suspend fun operator(): KookMember?

    public companion object Key : BaseEventKey<KookMemberChangedEvent>(
        "kook.member_changed", KookEvent, MemberChangedEvent
    ) {
        override fun safeCast(value: Any): KookMemberChangedEvent? = doSafeCast(value)
    }

}

// region member相关

// region 频道进出相关
/**
 * KOOK [成员变更事件][KookMemberChangedEvent] 中与**频道进出**相关的变更事件。
 * 这类事件代表某人进入、离开某个频道（通常为语音频道），而不代表成员进入、离开了当前的频道服务器（`guild`）。
 */
@BaseEvent
public abstract class KookMemberChannelChangedEvent : KookMemberChangedEvent() {

    /**
     * 事件涉及的频道信息。同 [organization].
     */
    @JSTP
    abstract override suspend fun source(): KookChannel


    /**
     * 事件涉及的频道信息。同 [source].
     */
    @JSTP
    override suspend fun organization(): KookChannel = source()


    public companion object Key : BaseEventKey<KookMemberChannelChangedEvent>(
        "kook.member_channel_changed", KookMemberChangedEvent
    ) {
        override fun safeCast(value: Any): KookMemberChannelChangedEvent? = doSafeCast(value)
    }

}


/**
 * KOOK 成员离开(频道)事件。
 *
 * 此事件被触发时，相关成员**已经**被移除自对应频道，且终止了内置的所有任务。
 * 因此 [before] 不可用于执行禁言等操作，[before] 也不会存在于当前频道成员中。
 *
 * @see JoinedChannelEventExtra
 * @author forte
 */
public abstract class KookMemberExitedChannelEvent : KookMemberChannelChangedEvent(), MemberDecreaseEvent {
    abstract override val sourceEvent: love.forte.simbot.kook.event.Event<JoinedChannelEventExtra>

    override val sourceBody: JoinedChannelEventBody
        get() = sourceEvent.extra.body

    /**
     * 离开的成员。
     *
     * 此成员已经被移除自频道，因此 [before] 不可用于执行禁言等操作，
     * 也不会存在于当前频道成员中。
     */
    @JSTP
    override suspend fun before(): KookMember = member()

    /**
     * 成员离开后。始终为null。
     */
    @JSTP
    override suspend fun after(): KookMember? = null

    /**
     * 成员离开频道事件的操作者始终为null （无法确定操作者）。
     */
    @JSTP
    override suspend fun operator(): KookMember? = null

    /**
     * KOOK 群员离开频道事件的行为类型始终为 [主动的][ActionType.PROACTIVE] （无法确定真正的类型）。
     */
    override val actionType: ActionType get() = ActionType.PROACTIVE


    override val key: Event.Key<out KookMemberExitedChannelEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookMemberExitedChannelEvent>(
        "kook.member_exited_channel", KookMemberChannelChangedEvent, MemberDecreaseEvent
    ) {
        override fun safeCast(value: Any): KookMemberExitedChannelEvent? = doSafeCast(value)
    }
}

/**
 * KOOK 成员加入(频道)事件。
 *
 * @see ExitedChannelEventExtra
 * @author forte
 */
public abstract class KookMemberJoinedChannelEvent : KookMemberChannelChangedEvent(), MemberIncreaseEvent {

    abstract override val sourceEvent: love.forte.simbot.kook.event.Event<ExitedChannelEventExtra>

    override val sourceBody: ExitedChannelEventBody
        get() = sourceEvent.extra.body

    /**
     * 事件涉及的频道信息。
     */
    @JSTP
    abstract override suspend fun source(): KookChannel

    /**
     * 增加的成员。
     */
    @JSTP
    override suspend fun after(): KookMember = member()

    /**
     * 始终为null。
     */
    @JSTP
    override suspend fun before(): KookMember? = null

    /**
     * KOOK 群员进入频道事件的操作者始终为null （无法确定操作者）。
     */
    @JSTP
    override suspend fun operator(): KookMember? = null

    /**
     * KOOK 群员离开频道事件的行为类型始终为 [主动的][ActionType.PROACTIVE]。
     */
    override val actionType: ActionType get() = ActionType.PROACTIVE


    override val key: Event.Key<out KookMemberJoinedChannelEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookMemberJoinedChannelEvent>(
        "kook.member_joined_channel", KookMemberChannelChangedEvent, MemberIncreaseEvent
    ) {
        override fun safeCast(value: Any): KookMemberJoinedChannelEvent? = doSafeCast(value)
    }
}
// endregion


// region 频道服务器进出
/**
 * KOOK [成员变更事件][KookMemberChangedEvent] 中与**频道服务器进出**相关的变更事件。
 * 这类事件代表某人加入、离开某个频道服务器。
 */
@BaseEvent
public abstract class KookMemberGuildChangedEvent : KookMemberChangedEvent() {

    /**
     * 涉及的相关频道服务器。同 [organization].
     */
    @JSTP
    abstract override suspend fun source(): KookGuild

    /**
     * 涉及的相关频道服务器。同 [source].
     */
    @JSTP
    override suspend fun organization(): KookGuild = source()

    abstract override val key: Event.Key<out KookMemberGuildChangedEvent>

    public companion object Key : BaseEventKey<KookMemberGuildChangedEvent>(
        "kook.member_guild_changed", KookMemberChangedEvent
    ) {
        override fun safeCast(value: Any): KookMemberGuildChangedEvent? = doSafeCast(value)
    }
}


/**
 * KOOK 成员离开(频道)事件。
 *
 * @see ExitedGuildEventExtra
 * @author forte
 */
public abstract class KookMemberExitedGuildEvent : KookMemberGuildChangedEvent(), GuildMemberDecreaseEvent {

    abstract override val sourceEvent: love.forte.simbot.kook.event.Event<ExitedGuildEventExtra>

    override val sourceBody: ExitedGuildEventBody
        get() = sourceEvent.extra.body

    /**
     * 离开的成员。
     */
    @JSTP
    abstract override suspend fun member(): KookMember

    /**
     * 离开的成员。同 [member].
     *
     * @see member
     */
    @JSTP
    override suspend fun before(): KookMember = member()

    /**
     * 涉及的频道服务器。
     */
    @JSTP
    abstract override suspend fun guild(): KookGuild

    /**
     * 涉及的相关频道服务器。同 [source].
     */
    @JSTP
    override suspend fun organization(): KookGuild = guild()

    /**
     * 成员离开后，始终为null。
     */
    @JSTP
    override suspend fun after(): KookMember? = null

    /**
     * KOOK 群员离开频道事件的操作者始终为null （无法确定操作者）。
     */
    @JSTP
    override suspend fun operator(): KookMember? = null

    /**
     * 涉及的频道服务器。同 [guild]
     * @see guild
     */
    @JSTP
    override suspend fun source(): KookGuild = guild()

    /**
     * KOOK 群员离开频道事件的行为类型始终为 [主动的][ActionType.PROACTIVE]。
     */
    override val actionType: ActionType get() = ActionType.PROACTIVE


    override val key: Event.Key<out KookMemberExitedGuildEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookMemberExitedGuildEvent>(
        "kook.member_exited_guild", KookMemberGuildChangedEvent, GuildMemberDecreaseEvent
    ) {
        override fun safeCast(value: Any): KookMemberExitedGuildEvent? = doSafeCast(value)
    }
}

/**
 * KOOK 成员加入(频道)事件。
 *
 * @see JoinedGuildEventExtra
 * @author forte
 */
public abstract class KookMemberJoinedGuildEvent : KookMemberGuildChangedEvent(), GuildMemberIncreaseEvent {

    abstract override val sourceEvent: love.forte.simbot.kook.event.Event<JoinedGuildEventExtra>

    override val sourceBody: JoinedGuildEventBody
        get() = sourceEvent.extra.body

    /**
     * 涉及的相关频道服务器。
     */
    @JSTP
    abstract override suspend fun guild(): KookGuild

    /**
     * 涉及的相关频道服务器。
     */
    @JSTP
    override suspend fun organization(): KookGuild = guild()

    /**
     * 涉及的相关频道服务器。同 [guild]
     */
    @JSTP
    override suspend fun source(): KookGuild = guild()

    /**
     * 加入的成员。
     */
    @JSTP
    abstract override suspend fun member(): KookMember

    /**
     * 加入的成员。同 [member].
     *
     * @see member
     */
    @JSTP
    override suspend fun after(): KookMember = member()

    /**
     * 成员加入前，始终为null。
     */
    @JSTP
    override suspend fun before(): KookMember? = null

    /**
     * KOOK 群员进入频道事件的操作者始终为null （无法确定操作者）。
     */
    @JSTP
    override suspend fun operator(): KookMember? = null

    /**
     * KOOK 群员离开频道事件的行为类型始终为 [主动的][ActionType.PROACTIVE]。
     */
    override val actionType: ActionType get() = ActionType.PROACTIVE

    override val key: Event.Key<out KookMemberJoinedGuildEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookMemberJoinedGuildEvent>(
        "kook.member_joined_guild", KookMemberGuildChangedEvent, GuildMemberIncreaseEvent
    ) {
        override fun safeCast(value: Any): KookMemberJoinedGuildEvent? = doSafeCast(value)
    }
}

// endregion


// endregion


// region bot相关
/**
 * 频道成员的变动事件中，变动本体为bot自身时的事件。对应 KOOK 原始事件的 [SelfExitedGuildEventExtra] 和 [SelfJoinedGuildEventExtra]。
 *
 * @see KookBotMemberChangedEvent
 *
 * @author forte
 */
@BaseEvent
public abstract class KookBotMemberChangedEvent : KookMemberChangedEvent() {
    abstract override val sourceBody: BotSelfGuildEventBody

    /**
     * 涉及的相关频道服务器。同 [organization].
     */
    @JSTP
    abstract override suspend fun source(): KookGuild

    /**
     * 涉及的相关频道服务器。同 [source].
     */
    @JSTP
    override suspend fun organization(): KookGuild = source()


    public companion object Key : BaseEventKey<KookBotMemberChangedEvent>(
        "kook.bot_member_changed", KookMemberChangedEvent
    ) {
        override fun safeCast(value: Any): KookBotMemberChangedEvent? = doSafeCast(value)
    }
}


/**
 * KOOK BOT自身离开(频道)事件。
 *
 * @see SelfExitedGuildEventExtra
 * @author forte
 */
public abstract class KookBotSelfExitedGuildEvent : KookBotMemberChangedEvent(), MemberDecreaseEvent {
    // TODO impl GuildEvent or GuildInfoContainer?
    abstract override val sourceEvent: love.forte.simbot.kook.event.Event<SelfExitedGuildEventExtra>

    override val sourceBody: BotSelfGuildEventBody
        get() = sourceEvent.extra.body

    /**
     * 即bot自身在频道服务器内的信息。
     */
    @JSTP
    abstract override suspend fun before(): KookMember

    /**
     * 始终为null。
     */
    @JSTP
    override suspend fun after(): KookMember? = null

    /**
     * KOOK bot离开频道事件的操作者始终为null （无法确定操作者）。
     */
    @JSTP
    override suspend fun operator(): KookMember? = null


    /**
     * KOOK 群员离开频道事件的行为类型始终为[主动的][ActionType.PROACTIVE]。
     */
    override val actionType: ActionType get() = ActionType.PROACTIVE


    override val key: Event.Key<out KookBotSelfExitedGuildEvent>
        get() = Key


    public companion object Key : BaseEventKey<KookBotSelfExitedGuildEvent>(
        "kook.bot_self_exited", KookBotMemberChangedEvent, MemberDecreaseEvent
    ) {
        override fun safeCast(value: Any): KookBotSelfExitedGuildEvent? = doSafeCast(value)
    }
}

/**
 * KOOK BOT自身加入(频道)事件。
 *
 * @see SelfJoinedGuildEventExtra
 * @author forte
 */
public abstract class KookBotSelfJoinedGuildEvent : KookBotMemberChangedEvent(), MemberIncreaseEvent {

    abstract override val sourceEvent: love.forte.simbot.kook.event.Event<SelfJoinedGuildEventExtra>

    override val sourceBody: BotSelfGuildEventBody
        get() = sourceEvent.extra.body

    /**
     * 涉及的相关频道服务器。
     */
    @JSTP
    abstract override suspend fun source(): KookGuild

    /**
     * 即bot自身在频道服务器内的信息。
     */
    @JSTP
    abstract override suspend fun after(): KookMember

    /**
     * 始终为null。
     */
    @JSTP
    override suspend fun before(): KookMember? = null

    /**
     * KOOK bot进入频道事件的操作者始终为null （无法确定操作者）。
     */
    @JSTP
    override suspend fun operator(): KookMember? = null


    /**
     * KOOK 群员离开频道事件的行为类型始终为主动的。
     */
    override val actionType: ActionType
        get() = ActionType.PROACTIVE

    override val key: Event.Key<out KookBotSelfJoinedGuildEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookBotSelfJoinedGuildEvent>(
        "kook.bot_self_joined", KookBotMemberChangedEvent, MemberIncreaseEvent
    ) {
        override fun safeCast(value: Any): KookBotSelfJoinedGuildEvent? = doSafeCast(value)
    }
}
// endregion

/**
 * KOOK 用户在线状态变更相关事件的抽象父类。
 *
 * 涉及到的原始事件有：
 * - [GuildMemberOfflineEventExtra]
 * - [GuildMemberOnlineEventExtra]
 *
 * 此事件及相关事件属于 [ChangedEvent] 事件，[变化主体][source] 为相对应的[基础用户信息][UserInfo]，[变更前][before] [变更后][after] 为其上下线的状态。
 * 其中变更前后的值满足 [before] == ![after], 且 [after] 永远代表此事件发生后此用户的在线状态。
 * 也因此而满足 [after] == [isOnline].
 *
 * 当 [after] == `true` 时，代表此人由离线状态变为在线状态，反之同理。
 *
 * ## 变化主体
 * 此事件主体是事件中的 [用户ID][GuildMemberOnlineStatusChangedEventBody.userId]
 *
 * ## 子类型
 * 此事件是密封的，如果你只想监听某人的上线或下线中的其中一种事件，则考虑监听此事件类的具体子类型。
 *
 * @see KookMemberOnlineEvent
 * @see KookMemberOfflineEvent
 *
 * @author forte
 */
public sealed class KookUserOnlineStatusChangedEvent : KookSystemEvent(), ChangedEvent {
    abstract override val sourceBody: GuildMemberOnlineStatusChangedEventBody

    /**
     * 用户ID。
     *
     * @see GuildMemberOnlineStatusChangedEventBody.userId
     */
    @JSTP
    override suspend fun source(): String = sourceBody.userId


    /**
     * 状态变化后，此用户是否为_在线_状态。
     */
    public abstract val isOnline: Boolean

    /**
     * 此用户与当前bot所同处的频道服务器的id列表。
     *
     * @see GuildMemberOnlineStatusChangedEventBody.guilds
     */
    public abstract val guildIds: List<ID>


    /**
     * 通过 [guildIds] 信息获取各个ID对应的 [KookGuild] 实例。
     *
     * [guilds] 的数据不是根据 [guildIds] 立即生效的，而是在每次获取的时候读取缓存信息。
     * 因此可能存在获取不到的情况（例如在获取时某 guild 已经被删除），
     * [guilds] 序列中的元素可能无法与 [guildIds] 完全对应。
     *
     */
    public abstract val guilds: Items<KookGuild>

    /**
     * 变更前的在线状态。相当于 `!isOnline`.
     */
    @JSTP
    override suspend fun before(): Boolean = !isOnline

    /**
     * 变更后的在线状态。同 [isOnline].
     */
    @JSTP
    override suspend fun after(): Boolean = isOnline

    /**
     * 变更时间。
     */
    override val changedTime: Timestamp get() = Timestamp.byMillisecond(sourceEvent.msgTimestamp)


    override val key: Event.Key<out KookUserOnlineStatusChangedEvent>
        get() = Key

    public companion object Key : BaseEventKey<KookUserOnlineStatusChangedEvent>(
        "kook.guild_member_online_status_changed", KookSystemEvent, ChangedEvent
    ) {
        override fun safeCast(value: Any): KookUserOnlineStatusChangedEvent? = doSafeCast(value)
    }


}


/**
 * [KookUserOnlineStatusChangedEvent] 对于用户上线的事件子类型。
 *
 */
public abstract class KookMemberOnlineEvent : KookUserOnlineStatusChangedEvent() {
    abstract override val sourceEvent: KkEvent<GuildMemberOnlineEventExtra>

    /**
     * 此事件代表上线，[isOnline] == true.
     */
    override val isOnline: Boolean
        get() = true

    override val sourceBody: GuildMemberOnlineStatusChangedEventBody
        get() = sourceEvent.extra.body

    public val userId: ID get() = sourceBody.userId.ID

    override val guildIds: List<ID> get() = sourceBody.guilds.map { it.ID }


    public companion object Key :
        BaseEventKey<KookMemberOnlineEvent>("kook.member_online", KookUserOnlineStatusChangedEvent) {
        override fun safeCast(value: Any): KookMemberOnlineEvent? = doSafeCast(value)
    }
}

/**
 * [KookUserOnlineStatusChangedEvent] 对于用户离线的事件子类型。
 *
 */
public abstract class KookMemberOfflineEvent : KookUserOnlineStatusChangedEvent() {
    abstract override val sourceEvent: KkEvent<GuildMemberOfflineEventExtra>

    /**
     * 此事件代表下线，[isOnline] == false.
     */
    override val isOnline: Boolean
        get() = false

    override val sourceBody: GuildMemberOnlineStatusChangedEventBody
        get() = sourceEvent.extra.body

    public val userId: ID get() = sourceBody.userId.ID

    override val guildIds: List<ID> get() = sourceBody.guilds.map { it.ID }

    public companion object Key :
        BaseEventKey<KookMemberOfflineEvent>("kook.member_offline", KookUserOnlineStatusChangedEvent) {
        override fun safeCast(value: Any): KookMemberOfflineEvent? = doSafeCast(value)
    }
}
