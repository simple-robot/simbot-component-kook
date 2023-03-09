/*
 *  Copyright (c) 2022-2023 ForteScarlet <ForteScarlet@163.com>
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

import love.forte.plugin.suspendtrans.annotation.JvmAsync
import love.forte.plugin.suspendtrans.annotation.JvmBlocking
import love.forte.simbot.Api4J
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.action.ActionType
import love.forte.simbot.component.kook.KookChannel
import love.forte.simbot.component.kook.KookGuild
import love.forte.simbot.component.kook.KookGuildMember
import love.forte.simbot.component.kook.event.KookUserOnlineStatusChangedEvent.Offline
import love.forte.simbot.component.kook.event.KookUserOnlineStatusChangedEvent.Online
import love.forte.simbot.definition.Organization
import love.forte.simbot.definition.UserInfo
import love.forte.simbot.event.*
import love.forte.simbot.kook.event.Event.Extra.Sys
import love.forte.simbot.kook.event.system.guild.member.*
import love.forte.simbot.kook.event.system.user.*
import love.forte.simbot.message.doSafeCast
import java.util.stream.Stream
import kotlin.streams.asStream
import love.forte.simbot.kook.event.Event as KkEvent

/**
 * Kook 的频道成员变更事件。
 *
 * 相关的 Kook **原始**事件类型有：
 * - [UserExitedChannelEvent]
 * - [UserJoinedChannelEvent]
 * - [JoinedGuildEvent]
 * - [ExitedGuildEvent]
 * - [SelfExitedGuildEvent]
 * - [SelfJoinedGuildEvent]
 *
 * 其中，[SelfExitedGuildEvent] 和 [SelfJoinedGuildEvent]
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
public abstract class KookMemberChangedEvent<out Body> :
    KookSystemEvent<Body>(),
    MemberChangedEvent {
    
    /**
     * 本次变更涉及的频道成员信息。同 [user]
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun member(): KookGuildMember
    
    /**
     * 本次变更涉及的频道成员信息。同 [member]
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun user(): KookGuildMember = member()
    
    /**
     * 可能存在的变更前成员信息。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun before(): KookGuildMember?
    
    /**
     * 可能存在的变更后成员信息。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun after(): KookGuildMember?
    
    /**
     * 变更成员所处组织。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun source(): Organization
    
    /**
     * 变更成员所处组织。同 [source].
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun organization(): Organization = source()
    
    /**
     * 可能存在的变更操作者。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun operator(): KookGuildMember?
    
    /**
     * 变更时间。
     */
    override val changedTime: Timestamp
        get() = sourceEvent.msgTimestamp
    
    public companion object Key : BaseEventKey<KookMemberChangedEvent<*>>(
        "kook.member_changed", KookEvent, MemberChangedEvent
    ) {
        override fun safeCast(value: Any): KookMemberChangedEvent<*>? = doSafeCast(value)
    }
    
}

// region member相关

// region 频道进出相关
/**
 * Kook  [成员变更事件][KookMemberChangedEvent] 中与**频道进出**相关的变更事件。
 * 这类事件代表某人进入、离开某个频道（通常为语音频道），而不代表成员进入、离开了当前的频道服务器（`guild`）。
 */
@BaseEvent
public abstract class KookMemberChannelChangedEvent<out Body> : KookMemberChangedEvent<Body>() {
    
    /**
     * 事件涉及的频道信息。同 [organization].
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun source(): KookChannel
    
    
    /**
     * 事件涉及的频道信息。同 [source].
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun organization(): KookChannel = source()
    
    
    public companion object Key : BaseEventKey<KookMemberChannelChangedEvent<*>>(
        "kook.member_channel_changed", KookMemberChangedEvent
    ) {
        override fun safeCast(value: Any): KookMemberChannelChangedEvent<*>? = doSafeCast(value)
    }
    
}


/**
 * Kook 成员离开(频道)事件。
 *
 * 此事件被触发时，相关成员**已经**被移除自对应频道，且终止了内置的所有任务。
 * 因此 [before] 不可用于执行禁言等操作，[before] 也不会存在于当前频道成员中。
 *
 * @see UserExitedChannelEvent
 * @author forte
 */
public abstract class KookMemberExitedChannelEvent :
    KookMemberChannelChangedEvent<UserExitedChannelEventBody>(),
    MemberDecreaseEvent {
    
    /**
     * 离开的成员。
     *
     * 此成员已经被移除自频道，因此 [before] 不可用于执行禁言等操作，
     * 也不会存在于当前频道成员中。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun before(): KookGuildMember
    
    /**
     * 成员离开后。始终为null。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun after(): KookGuildMember? = null
    
    /**
     * Kook 群员离开频道事件的操作者始终为null （无法确定操作者）。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun operator(): KookGuildMember? = null
    
    /**
     * Kook 群员离开频道事件的行为类型始终为 [主动的][ActionType.PROACTIVE]。
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
 * Kook 成员加入(频道)事件。
 *
 * @see UserJoinedChannelEvent
 * @author forte
 */
public abstract class KookMemberJoinedChannelEvent :
    KookMemberChannelChangedEvent<UserJoinedChannelEventBody>(),
    MemberIncreaseEvent {
    
    /**
     * 事件涉及的频道信息。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun source(): KookChannel
    
    /**
     * 增加的成员。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun after(): KookGuildMember
    
    /**
     * 始终为null。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun before(): KookGuildMember? = null
    
    /**
     * Kook 群员进入频道事件的操作者始终为null （无法确定操作者）。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun operator(): KookGuildMember? = null
    
    /**
     * Kook 群员离开频道事件的行为类型始终为 [主动的][ActionType.PROACTIVE]。
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
 * Kook  [成员变更事件][KookMemberChangedEvent] 中与**频道服务器进出**相关的变更事件。
 * 这类事件代表某人加入、离开某个频道服务器。
 */
@BaseEvent
public abstract class KookMemberGuildChangedEvent<out Body> :
    KookMemberChangedEvent<Body>() {
    
    /**
     * 涉及的相关频道服务器。同 [organization].
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun source(): KookGuild
    
    /**
     * 涉及的相关频道服务器。同 [source].
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun organization(): KookGuild = source()
    
    abstract override val key: Event.Key<out KookMemberGuildChangedEvent<*>>
    
    public companion object Key : BaseEventKey<KookMemberGuildChangedEvent<*>>(
        "kook.member_guild_changed", KookMemberChangedEvent
    ) {
        override fun safeCast(value: Any): KookMemberGuildChangedEvent<*>? = doSafeCast(value)
    }
}


/**
 * Kook 成员离开(频道)事件。
 *
 * @see ExitedGuildEvent
 * @author forte
 */
public abstract class KookMemberExitedGuildEvent :
    KookMemberGuildChangedEvent<ExitedGuildEventBody>(),
    GuildMemberDecreaseEvent {

    /**
     * 离开的成员。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun member(): KookGuildMember

    /**
     * 离开的成员。同 [member].
     *
     * @see member
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun before(): KookGuildMember = member()

    /**
     * 涉及的频道服务器。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun guild(): KookGuild

    /**
     * 涉及的相关频道服务器。同 [source].
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun organization(): KookGuild = guild()

    /**
     * 成员离开后，始终为null。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun after(): KookGuildMember? = null
    
    /**
     * Kook 群员离开频道事件的操作者始终为null （无法确定操作者）。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun operator(): KookGuildMember? = null
    
    /**
     * 涉及的频道服务器。同 [guild]
     * @see guild
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun source(): KookGuild = guild()
    
    /**
     * Kook 群员离开频道事件的行为类型始终为 [主动的][ActionType.PROACTIVE]。
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
 * Kook 成员加入(频道)事件。
 *
 * @see JoinedGuildEvent
 * @author forte
 */
public abstract class KookMemberJoinedGuildEvent :
    KookMemberGuildChangedEvent<JoinedGuildEventBody>(),
    GuildMemberIncreaseEvent {

    /**
     * 涉及的相关频道服务器。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun guild(): KookGuild

    /**
     * 涉及的相关频道服务器。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun organization(): KookGuild = guild()

    /**
     * 涉及的相关频道服务器。同 [guild]
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun source(): KookGuild = guild()
    
    /**
     * 加入的成员。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun member(): KookGuildMember
    
    /**
     * 加入的成员。同 [member].
     *
     * @see member
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun after(): KookGuildMember = member()
    
    /**
     * 成员加入前，始终为null。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun before(): KookGuildMember? = null
    
    /**
     * Kook 群员进入频道事件的操作者始终为null （无法确定操作者）。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun operator(): KookGuildMember? = null
    
    /**
     * Kook 群员离开频道事件的行为类型始终为 [主动的][ActionType.PROACTIVE]。
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
 * 频道成员的变动事件中，变动本体为bot自身时的事件。对应 Kook 原始事件的 [SelfExitedGuildEvent] 和 [SelfJoinedGuildEvent]。
 *
 * @see KookBotMemberChangedEvent
 *
 * @author forte
 */
@BaseEvent
public abstract class KookBotMemberChangedEvent<out Body> :
    KookMemberChangedEvent<Body>() {
    
    /**
     * 涉及的相关频道服务器。同 [organization].
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun source(): KookGuild
    
    /**
     * 涉及的相关频道服务器。同 [source].
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun organization(): KookGuild = source()
    
    
    public companion object Key : BaseEventKey<KookBotMemberChangedEvent<*>>(
        "kook.bot_member_changed", KookMemberChangedEvent
    ) {
        override fun safeCast(value: Any): KookBotMemberChangedEvent<*>? = doSafeCast(value)
    }
}


/**
 * Kook BOT自身离开(频道)事件。
 *
 * @see SelfExitedGuildEvent
 * @author forte
 */
public abstract class KookBotSelfExitedGuildEvent :
    KookBotMemberChangedEvent<SelfExitedGuildEventBody>(),
    MemberDecreaseEvent {
    
    /**
     * 即bot自身在频道服务器内的信息。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun before(): KookGuildMember
    
    /**
     * 始终为null。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun after(): KookGuildMember? = null
    
    /**
     * Kook bot离开频道事件的操作者始终为null （无法确定操作者）。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun operator(): KookGuildMember? = null
    
    
    /**
     * Kook 群员离开频道事件的行为类型始终为[主动的][ActionType.PROACTIVE]。
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
 * Kook BOT自身加入(频道)事件。
 *
 * @see SelfJoinedGuildEvent
 * @author forte
 */
public abstract class KookBotSelfJoinedGuildEvent :
    KookBotMemberChangedEvent<SelfJoinedGuildEventBody>(),
    MemberIncreaseEvent {
    
    /**
     * 涉及的相关频道服务器。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun source(): KookGuild
    
    /**
     * 即bot自身在频道服务器内的信息。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun after(): KookGuildMember
    
    /**
     * 始终为null。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun before(): KookGuildMember? = null
    
    /**
     * Kook bot进入频道事件的操作者始终为null （无法确定操作者）。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun operator(): KookGuildMember? = null
    
    
    /**
     * Kook 群员离开频道事件的行为类型始终为主动的。
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
 * Kook 用户在线状态变更相关事件的抽象父类。
 *
 * 涉及到的原始事件有：
 * - [GuildMemberOfflineEvent]
 * - [GuildMemberOnlineEvent]
 *
 * 此事件及相关事件属于 [ChangedEvent] 事件，[变化主体][source] 为相对应的[基础用户信息][UserInfo]，变更[前][before] [后][after] 为其上下线的状态。
 * 其中变更前后的值满足 [before] == ![after], 且 [after] 永远代表此事件发生后此用户的在线状态。
 * 也因此而满足 [after] == [isOnline].
 *
 * 当 [after] == true 时，代表此人由离线状态变为在线状态，反之同理。
 *
 * ## 变化主体
 * 因为用户在线/离线事件所提供的用户并未一个具体的频道用户，因此事件主体是一个基础的 [用户信息][UserInfo]
 *
 * ## 子类型
 * 此事件是密封的，如果你只想监听某人的上线或下线中的其中一种事件，则考虑监听此事件类的具体子类型。
 *
 * @see Online
 * @see Offline
 *
 * @author forte
 */
public sealed class KookUserOnlineStatusChangedEvent :
    KookSystemEvent<GuildMemberEventExtraBody>(),
    ChangedEvent {
    
    /**
     * 发生变化的用户信息。
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    abstract override suspend fun source(): UserInfo
    
    
    /**
     * 状态变化后，此用户是否为_在线_状态。
     */
    public abstract val isOnline: Boolean
    
    /**
     * 此用户与当前bot所同处的频道服务器的id列表。
     *
     * @see GuildMemberOnlineEventBody.guilds
     * @see GuildMemberOfflineEventBody.guilds
     */
    public abstract val guildIds: List<ID>
    
    // TODO guilds 返回值类型变更为 Items<T>
    
    /**
     * 通过 [guildIds] 信息获取各个ID对应的 [KookGuild] 实例。
     *
     * 可能存在获取不到的情况，因此 [guilds] 序列中的元素 **可能为null**。
     *
     * 可以考虑过滤空值：
     * ```kotlin
     * val guilds = event.guilds.filterNotNull()
     * ```
     *
     * 来相对安全的使用序列。
     *
     */
    @get:JvmSynthetic
    public abstract val guilds: Sequence<KookGuild?>
    
    
    /**
     * 通过 [guildIds] 信息获取各个ID对应的 [KookGuild] 实例。
     *
     * 可能存在获取不到的情况，因此 [guilds] 序列中的元素 **可能为null**。
     *
     * 可以考虑过滤空值：
     * ```java
     * final Stream<KookGuild> guilds = event.getGuilds().filter(Objects::nonNull)
     * ```
     *
     * 来相对安全的使用流。
     *
     */
    @Api4J
    @get:JvmName("getGuilds")
    public val guildStream: Stream<KookGuild?> get() = guilds.asStream()
    
    /**
     * 变更前的在线状态。相当于 `!isOnline`.
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun before(): Boolean = !isOnline
    
    /**
     * 变更后的在线状态。同 [isOnline].
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun after(): Boolean = isOnline
    
    /**
     * 变更时间。
     */
    override val changedTime: Timestamp
        get() = sourceEvent.msgTimestamp
    
    
    override val key: Event.Key<out KookUserOnlineStatusChangedEvent>
        get() = Key
    
    public companion object Key : BaseEventKey<KookUserOnlineStatusChangedEvent>(
        "kook.guild_member_online_status_changed", KookSystemEvent, ChangedEvent
    ) {
        override fun safeCast(value: Any): KookUserOnlineStatusChangedEvent? = doSafeCast(value)
    }
    
    
    /**
     * [KookUserOnlineStatusChangedEvent] 对于用户上线的事件子类型。
     *
     */
    public abstract class Online : KookUserOnlineStatusChangedEvent() {
        abstract override val sourceEvent: KkEvent<Sys<GuildMemberOnlineEventBody>>
        
        /**
         * 此事件代表上线，[isOnline] == true.
         */
        override val isOnline: Boolean
            get() = true
        
        override val sourceBody: GuildMemberOnlineEventBody
            get() = sourceEvent.extra.body
        
        public val userId: ID
            get() = sourceBody.userId
        
        override val changedTime: Timestamp
            get() = sourceBody.eventTime
        
        override val guildIds: List<ID>
            get() = sourceBody.guilds
        
        
        public companion object Key :
            BaseEventKey<Online>("kook.member_online", KookUserOnlineStatusChangedEvent) {
            override fun safeCast(value: Any): Online? = doSafeCast(value)
        }
    }
    
    /**
     * [KookUserOnlineStatusChangedEvent] 对于用户离线的事件子类型。
     *
     */
    public abstract class Offline : KookUserOnlineStatusChangedEvent() {
        abstract override val sourceEvent: KkEvent<Sys<GuildMemberOfflineEventBody>>
        
        /**
         * 此事件代表下线，[isOnline] == false.
         */
        override val isOnline: Boolean
            get() = false
        
        override val sourceBody: GuildMemberOfflineEventBody
            get() = sourceEvent.extra.body
        
        public val userId: ID
            get() = sourceBody.userId
        
        override val changedTime: Timestamp
            get() = sourceBody.eventTime
        
        override val guildIds: List<ID>
            get() = sourceBody.guilds
        
        public companion object Key :
            BaseEventKey<Offline>("kook.member_offline", KookUserOnlineStatusChangedEvent) {
            override fun safeCast(value: Any): Offline? = doSafeCast(value)
        }
    }
    
    
}
