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

package love.forte.simbot.component.kaiheila.event

import love.forte.simbot.Api4J
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.action.ActionType
import love.forte.simbot.component.kaiheila.KaiheilaChannel
import love.forte.simbot.component.kaiheila.KaiheilaGuild
import love.forte.simbot.component.kaiheila.KaiheilaGuildMember
import love.forte.simbot.component.kaiheila.event.KaiheilaUserOnlineStatusChangedEvent.Offline
import love.forte.simbot.component.kaiheila.event.KaiheilaUserOnlineStatusChangedEvent.Online
import love.forte.simbot.definition.Organization
import love.forte.simbot.definition.UserInfo
import love.forte.simbot.event.*
import love.forte.simbot.kook.event.Event.Extra.Sys
import love.forte.simbot.kook.event.system.guild.member.*
import love.forte.simbot.kook.event.system.user.*
import love.forte.simbot.message.doSafeCast
import java.util.stream.Stream
import kotlin.streams.asStream
import love.forte.simbot.kook.event.Event as KhlEvent

/**
 *  Kook 的频道成员变更事件。
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
 * 因此会额外提供相对应的 [bot成员变动][KaiheilaBotMemberChangedEvent]
 * 事件类型来进行更精准的事件监听。
 *
 * ## 相关事件
 * ### 频道成员变更事件
 * [KaiheilaMemberChannelChangedEvent] 事件及其子类型
 * [KaiheilaMemberJoinedChannelEvent]、[KaiheilaMemberExitedChannelEvent]
 * 代表了一个频道服务器中的某个群成员加入、离开某一个频道（通常为语音频道）的事件。
 *
 * ### 频道服务器成员变更事件
 * [KaiheilaMemberGuildChangedEvent] 事件及其子类型
 * [KaiheilaMemberJoinedGuildEvent]、[KaiheilaMemberExitedGuildEvent]
 * 代表了一个频道服务器中有新群成员加入、旧成员离开此服务器的事件。
 *
 * ### Bot频道服务器事件
 * [KaiheilaBotMemberChangedEvent] 事件及其子类型
 * [KaiheilaBotSelfJoinedGuildEvent]、[KaiheilaBotSelfExitedGuildEvent]
 * 代表了当前bot加入新频道服务器、离开旧频道服务器的事件。
 *
 * @author forte
 */
@BaseEvent
public abstract class KaiheilaMemberChangedEvent<out Body> :
    KaiheilaSystemEvent<Body>(),
    MemberChangedEvent {

    /**
     * 本次变更涉及的频道成员信息。同 [user]
     */
    @OptIn(Api4J::class)
    abstract override val member: KaiheilaGuildMember

    /**
     * 本次变更涉及的频道成员信息。同 [user]
     */
    @JvmSynthetic
    override suspend fun member(): KaiheilaGuildMember = member

    /**
     * 本次变更涉及的频道成员信息。同 [member]
     */
    @OptIn(Api4J::class)
    override val user: KaiheilaGuildMember get() = member

    /**
     * 本次变更涉及的频道成员信息。同 [member]
     */
    @JvmSynthetic
    override suspend fun user(): KaiheilaGuildMember = user

    /**
     * 可能存在的变更前成员信息。
     */
    @OptIn(Api4J::class)
    abstract override val before: KaiheilaGuildMember?

    /**
     * 可能存在的变更前成员信息。
     */
    @JvmSynthetic
    abstract override suspend fun before(): KaiheilaGuildMember?

    /**
     * 可能存在的变更后成员信息。
     */
    @OptIn(Api4J::class)
    abstract override val after: KaiheilaGuildMember?

    /**
     * 可能存在的变更后成员信息。
     */
    @JvmSynthetic
    abstract override suspend fun after(): KaiheilaGuildMember?

    /**
     * 变更成员所处组织。
     */
    @OptIn(Api4J::class)
    abstract override val source: Organization

    /**
     * 变更成员所处组织。
     */
    @JvmSynthetic
    abstract override suspend fun source(): Organization


    /**
     * 变更成员所处组织。同 [source].
     */
    @OptIn(Api4J::class)
    abstract override val organization: Organization

    /**
     * 变更成员所处组织。同 [source].
     */
    @JvmSynthetic
    abstract override suspend fun organization(): Organization

    /**
     * 变更时间。
     */
    override val changedTime: Timestamp
        get() = sourceEvent.msgTimestamp


    /**
     * 可能存在的变更操作者。
     */
    @OptIn(Api4J::class)
    abstract override val operator: KaiheilaGuildMember?

    /**
     * 可能存在的变更操作者。
     */
    @JvmSynthetic
    override suspend fun operator(): KaiheilaGuildMember? = operator

    public companion object Key : BaseEventKey<KaiheilaMemberChangedEvent<*>>(
        "kook.member_changed", KaiheilaEvent, MemberChangedEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMemberChangedEvent<*>? = doSafeCast(value)
    }

}

//region member相关

//region 频道进出相关
/**
 *  Kook  [成员变更事件][KaiheilaMemberChangedEvent] 中与**频道进出**相关的变更事件。
 * 这类事件代表某人进入、离开某个频道（通常为语音频道），而不代表成员进入、离开了当前的频道服务器（`guild`）。
 */
@BaseEvent
public abstract class KaiheilaMemberChannelChangedEvent<out Body> : KaiheilaMemberChangedEvent<Body>() {

    /**
     * 事件涉及的频道信息。同 [organization].
     */
    abstract override val source: KaiheilaChannel

    /**
     * 事件涉及的频道信息。同 [organization].
     */
    @JvmSynthetic
    override suspend fun source(): KaiheilaChannel = source


    /**
     * 事件涉及的频道信息。同 [source].
     */
    override val organization: KaiheilaChannel get() = source

    /**
     * 事件涉及的频道信息。同 [source].
     */
    @JvmSynthetic
    override suspend fun organization(): KaiheilaChannel = organization


    public companion object Key : BaseEventKey<KaiheilaMemberChannelChangedEvent<*>>(
        "kook.member_channel_changed", KaiheilaMemberChangedEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMemberChannelChangedEvent<*>? = doSafeCast(value)
    }

}


/**
 *  Kook 成员离开(频道)事件。
 *
 * @see UserExitedChannelEvent
 * @author forte
 */
public abstract class KaiheilaMemberExitedChannelEvent :
    KaiheilaMemberChannelChangedEvent<UserExitedChannelEventBody>(),
    MemberDecreaseEvent {

    /**
     * 离开的成员。
     */
    @OptIn(Api4J::class)
    abstract override val before: KaiheilaGuildMember

    /**
     * 离开的成员。
     */
    @JvmSynthetic
    override suspend fun before(): KaiheilaGuildMember = before

    /**
     * 成员离开后。始终为null。
     */
    @OptIn(Api4J::class)
    override val after: KaiheilaGuildMember? get() = null

    /**
     * 成员离开后。始终为null。
     */
    @JvmSynthetic
    override suspend fun after(): KaiheilaGuildMember? = null


    /**
     *  Kook 群员离开频道事件的行为类型始终为 [主动的][ActionType.PROACTIVE]。
     */
    override val actionType: ActionType get() = ActionType.PROACTIVE

    /**
     *  Kook 群员离开频道事件的操作者始终为null （无法确定操作者）。
     */
    @Suppress("UnnecessaryOptInAnnotation")
    @OptIn(Api4J::class)
    override val operator: KaiheilaGuildMember? get() = null

    /**
     *  Kook 群员离开频道事件的操作者始终为null （无法确定操作者）。
     */
    @JvmSynthetic
    override suspend fun operator(): KaiheilaGuildMember? = null


    override val key: Event.Key<out KaiheilaMemberExitedChannelEvent>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaMemberExitedChannelEvent>(
        "kook.member_exited_channel", KaiheilaMemberChannelChangedEvent, MemberDecreaseEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMemberExitedChannelEvent? = doSafeCast(value)
    }
}

/**
 *  Kook 成员加入(频道)事件。
 *
 * @see UserJoinedChannelEvent
 * @author forte
 */
public abstract class KaiheilaMemberJoinedChannelEvent :
    KaiheilaMemberChannelChangedEvent<UserJoinedChannelEventBody>(),
    MemberIncreaseEvent {

    /**
     * 增加的成员。
     */
    @OptIn(Api4J::class)
    abstract override val after: KaiheilaGuildMember

    /**
     * 增加的成员。
     */
    @JvmSynthetic
    override suspend fun after(): KaiheilaGuildMember = after


    /**
     * 始终为null。
     */
    @OptIn(Api4J::class)
    override val before: KaiheilaGuildMember? get() = null

    /**
     * 始终为null。
     */
    @JvmSynthetic
    override suspend fun before(): KaiheilaGuildMember? = null


    /**
     *  Kook 群员离开频道事件的行为类型始终为 [主动的][ActionType.PROACTIVE]。
     */
    override val actionType: ActionType get() = ActionType.PROACTIVE


    /**
     *  Kook 群员进入频道事件的操作者始终为null （无法确定操作者）。
     */
    @Suppress("UnnecessaryOptInAnnotation")
    @OptIn(Api4J::class)
    override val operator: KaiheilaGuildMember? get() = null

    /**
     *  Kook 群员进入频道事件的操作者始终为null （无法确定操作者）。
     */
    @JvmSynthetic
    override suspend fun operator(): KaiheilaGuildMember? = null


    override val key: Event.Key<out KaiheilaMemberJoinedChannelEvent>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaMemberJoinedChannelEvent>(
        "kook.member_joined_channel", KaiheilaMemberChannelChangedEvent, MemberIncreaseEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMemberJoinedChannelEvent? = doSafeCast(value)
    }
}
//endregion


//region 频道服务器进出
/**
 *  Kook  [成员变更事件][KaiheilaMemberChangedEvent] 中与**频道服务器进出**相关的变更事件。
 * 这类事件代表某人加入、离开某个频道服务器。
 */
@BaseEvent
public abstract class KaiheilaMemberGuildChangedEvent<out Body> :
    KaiheilaMemberChangedEvent<Body>() {

    /**
     * 涉及的相关频道服务器。同 [organization].
     */
    abstract override val source: KaiheilaGuild

    /**
     * 涉及的相关频道服务器。同 [organization].
     */
    @JvmSynthetic
    override suspend fun source(): KaiheilaGuild = source

    /**
     * 涉及的相关频道服务器。同 [source].
     */
    override val organization: KaiheilaGuild get() = source

    /**
     * 涉及的相关频道服务器。同 [source].
     */
    @JvmSynthetic
    override suspend fun organization(): KaiheilaGuild = organization


    public companion object Key : BaseEventKey<KaiheilaMemberGuildChangedEvent<*>>(
        "kook.member_guild_changed", KaiheilaMemberChangedEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMemberGuildChangedEvent<*>? = doSafeCast(value)
    }
}


/**
 *  Kook 成员离开(频道)事件。
 *
 * @see ExitedGuildEvent
 * @author forte
 */
public abstract class KaiheilaMemberExitedGuildEvent :
    KaiheilaMemberGuildChangedEvent<ExitedGuildEventBody>(),
    MemberDecreaseEvent {


    /**
     * 离开的成员。
     */
    @OptIn(Api4J::class)
    abstract override val before: KaiheilaGuildMember

    /**
     * 离开的成员。
     */
    @JvmSynthetic
    override suspend fun before(): KaiheilaGuildMember = before


    /**
     * 成员离开后，始终为null。
     */
    @OptIn(Api4J::class)
    override val after: KaiheilaGuildMember? get() = null

    /**
     * 成员离开后，始终为null。
     */
    @JvmSynthetic
    override suspend fun after(): KaiheilaGuildMember? = null

    /**
     *  Kook 群员离开频道事件的行为类型始终为 [主动的][ActionType.PROACTIVE]。
     */
    override val actionType: ActionType get() = ActionType.PROACTIVE

    /**
     *  Kook 群员离开频道事件的操作者始终为null （无法确定操作者）。
     */
    @Suppress("UnnecessaryOptInAnnotation")
    @OptIn(Api4J::class)
    override val operator: KaiheilaGuildMember?
        get() = null

    /**
     *  Kook 群员离开频道事件的操作者始终为null （无法确定操作者）。
     */
    @JvmSynthetic
    override suspend fun operator(): KaiheilaGuildMember? = null


    override val key: Event.Key<out KaiheilaMemberExitedChannelEvent>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaMemberExitedChannelEvent>(
        "kook.member_exited_channel", KaiheilaMemberChannelChangedEvent, MemberDecreaseEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMemberExitedChannelEvent? = doSafeCast(value)
    }
}

/**
 *  Kook 成员加入(频道)事件。
 *
 * @see JoinedGuildEvent
 * @author forte
 */
public abstract class KaiheilaMemberJoinedGuildEvent :
    KaiheilaMemberGuildChangedEvent<JoinedGuildEventBody>(),
    MemberIncreaseEvent {

    /**
     * 加入的成员。
     */
    @OptIn(Api4J::class)
    abstract override val after: KaiheilaGuildMember

    /**
     * 加入的成员。
     */
    @JvmSynthetic
    override suspend fun after(): KaiheilaGuildMember = after


    /**
     * 成员加入前，始终为null。
     */
    @OptIn(Api4J::class)
    override val before: KaiheilaGuildMember?
        get() = null

    /**
     * 成员加入前，始终为null。
     */
    @JvmSynthetic
    override suspend fun before(): KaiheilaGuildMember? = null

    /**
     *  Kook 群员离开频道事件的行为类型始终为 [主动的][ActionType.PROACTIVE]。
     */
    override val actionType: ActionType get() = ActionType.PROACTIVE


    /**
     *  Kook 群员进入频道事件的操作者始终为null （无法确定操作者）。
     */
    @Suppress("UnnecessaryOptInAnnotation")
    @OptIn(Api4J::class)
    override val operator: KaiheilaGuildMember? get() = null

    /**
     *  Kook 群员进入频道事件的操作者始终为null （无法确定操作者）。
     */
    @JvmSynthetic
    override suspend fun operator(): KaiheilaGuildMember? = null

    override val key: Event.Key<out KaiheilaMemberJoinedGuildEvent>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaMemberJoinedGuildEvent>(
        "kook.member_joined_guild", KaiheilaMemberGuildChangedEvent, MemberIncreaseEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMemberJoinedGuildEvent? = doSafeCast(value)
    }
}

//endregion


//endregion


//region bot相关
/**
 * 频道成员的变动事件中，变动本体为bot自身时的事件。对应 Kook 原始事件的 [SelfExitedGuildEvent] 和 [SelfJoinedGuildEvent]。
 *
 * @see KaiheilaBotMemberChangedEvent
 *
 * @author forte
 */
@BaseEvent
public abstract class KaiheilaBotMemberChangedEvent<out Body> :
    KaiheilaMemberChangedEvent<Body>() {


    /**
     * 涉及的相关频道服务器。同 [organization].
     */
    abstract override val source: KaiheilaGuild

    /**
     * 涉及的相关频道服务器。同 [organization].
     */
    @JvmSynthetic
    override suspend fun source(): KaiheilaGuild = source

    /**
     * 涉及的相关频道服务器。同 [source].
     */
    override val organization: KaiheilaGuild get() = source

    /**
     * 涉及的相关频道服务器。同 [source].
     */
    @JvmSynthetic
    override suspend fun organization(): KaiheilaGuild = organization


    public companion object Key : BaseEventKey<KaiheilaBotMemberChangedEvent<*>>(
        "kook.bot_member_changed", KaiheilaMemberChangedEvent
    ) {
        override fun safeCast(value: Any): KaiheilaBotMemberChangedEvent<*>? = doSafeCast(value)
    }
}


/**
 *  Kook BOT自身离开(频道)事件。
 *
 * @see SelfExitedGuildEvent
 * @author forte
 */
public abstract class KaiheilaBotSelfExitedGuildEvent :
    KaiheilaBotMemberChangedEvent<SelfExitedGuildEventBody>(),
    MemberDecreaseEvent {

    /**
     * 即bot自身在频道服务器内的信息。
     */
    @OptIn(Api4J::class)
    abstract override val before: KaiheilaGuildMember

    /**
     * 即bot自身在频道服务器内的信息。
     */
    @JvmSynthetic
    override suspend fun before(): KaiheilaGuildMember = before

    /**
     * 始终为null。
     */
    @OptIn(Api4J::class)
    override val after: KaiheilaGuildMember? get() = null

    /**
     * 始终为null。
     */
    @JvmSynthetic
    override suspend fun after(): KaiheilaGuildMember? = after

    /**
     *  Kook bot离开频道事件的操作者始终为null （无法确定操作者）。
     */
    @Suppress("UnnecessaryOptInAnnotation")
    @OptIn(Api4J::class)
    override val operator: KaiheilaGuildMember? get() = null

    /**
     *  Kook bot离开频道事件的操作者始终为null （无法确定操作者）。
     */
    @JvmSynthetic
    override suspend fun operator(): KaiheilaGuildMember? = null


    /**
     *  Kook 群员离开频道事件的行为类型始终为[主动的][ActionType.PROACTIVE]。
     */
    override val actionType: ActionType get() = ActionType.PROACTIVE


    override val key: Event.Key<out KaiheilaBotSelfExitedGuildEvent>
        get() = Key


    public companion object Key : BaseEventKey<KaiheilaBotSelfExitedGuildEvent>(
        "kook.bot_self_exited", KaiheilaBotMemberChangedEvent, MemberDecreaseEvent
    ) {
        override fun safeCast(value: Any): KaiheilaBotSelfExitedGuildEvent? = doSafeCast(value)
    }
}

/**
 *  Kook BOT自身加入(频道)事件。
 *
 * @see SelfJoinedGuildEvent
 * @author forte
 */
public abstract class KaiheilaBotSelfJoinedGuildEvent :
    KaiheilaBotMemberChangedEvent<SelfJoinedGuildEventBody>(),
    MemberIncreaseEvent {
    /**
     * 即bot自身在频道服务器内的信息。
     */
    @OptIn(Api4J::class)
    abstract override val after: KaiheilaGuildMember

    /**
     * 即bot自身在频道服务器内的信息。
     */
    @JvmSynthetic
    override suspend fun after(): KaiheilaGuildMember = after

    /**
     * 始终为null。
     */
    @OptIn(Api4J::class)
    override val before: KaiheilaGuildMember? get() = null

    /**
     * 始终为null。
     */
    @JvmSynthetic
    override suspend fun before(): KaiheilaGuildMember? = before

    /**
     *  Kook bot进入频道事件的操作者始终为null （无法确定操作者）。
     */
    @Suppress("UnnecessaryOptInAnnotation")
    @OptIn(Api4J::class)
    override val operator: KaiheilaGuildMember? get() = null

    /**
     *  Kook bot进入频道事件的操作者始终为null （无法确定操作者）。
     */
    @JvmSynthetic
    override
    suspend fun operator(): KaiheilaGuildMember? = null


    /**
     *  Kook 群员离开频道事件的行为类型始终为主动的。
     */
    override val actionType: ActionType
        get() = ActionType.PROACTIVE

    override val key: Event.Key<out KaiheilaBotSelfJoinedGuildEvent>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaBotSelfJoinedGuildEvent>(
        "kook.bot_self_joined", KaiheilaBotMemberChangedEvent, MemberIncreaseEvent
    ) {
        override fun safeCast(value: Any): KaiheilaBotSelfJoinedGuildEvent? = doSafeCast(value)
    }
}
//endregion

/**
 *  Kook 用户在线状态变更相关事件的抽象父类。
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
public sealed class KaiheilaUserOnlineStatusChangedEvent :
    KaiheilaSystemEvent<GuildMemberEventExtraBody>(),
    ChangedEvent {

    /**
     * 发生变化的用户信息。
     */
    @OptIn(Api4J::class)
    abstract override val source: UserInfo

    /**
     * 发生变化的用户信息。
     */
    @JvmSynthetic
    override suspend fun source(): UserInfo = source


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

    /**
     * 通过 [guildIds] 信息获取各个ID对应的 [KaiheilaGuild] 实例。
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
    public abstract val guilds: Sequence<KaiheilaGuild?>


    /**
     * 通过 [guildIds] 信息获取各个ID对应的 [KaiheilaGuild] 实例。
     *
     * 可能存在获取不到的情况，因此 [guilds] 序列中的元素 **可能为null**。
     *
     * 可以考虑过滤空值：
     * ```java
     * final Stream<KaiheilaGuild> guilds = event.getGuilds().filter(Objects::nonNull)
     * ```
     *
     * 来相对安全的使用流。
     *
     */
    @Api4J
    @get:JvmName("getGuilds")
    public val guildStream: Stream<KaiheilaGuild?> get() = guilds.asStream()


    /**
     * 变更前的在线状态。相当于 `!isOnline`.
     */
    @OptIn(Api4J::class)
    override val before: Boolean get() = !isOnline

    /**
     * 变更前的在线状态。相当于 `!isOnline`.
     */
    @JvmSynthetic
    override suspend fun before(): Boolean = before

    /**
     * 变更后的在线状态。同 [isOnline].
     */
    @OptIn(Api4J::class)
    override val after: Boolean get() = isOnline

    /**
     * 变更后的在线状态。同 [isOnline].
     */
    @JvmSynthetic
    override suspend fun after(): Boolean = after

    /**
     * 变更时间。
     */
    override val changedTime: Timestamp
        get() = sourceEvent.msgTimestamp


    override val key: Event.Key<out KaiheilaUserOnlineStatusChangedEvent>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaUserOnlineStatusChangedEvent>(
        "kook.guild_member_online_status_changed", KaiheilaSystemEvent, ChangedEvent
    ) {
        override fun safeCast(value: Any): KaiheilaUserOnlineStatusChangedEvent? = doSafeCast(value)
    }


    /**
     * [KaiheilaUserOnlineStatusChangedEvent] 对于用户上线的事件子类型。
     *
     */
    public abstract class Online : KaiheilaUserOnlineStatusChangedEvent() {
        abstract override val sourceEvent: KhlEvent<Sys<GuildMemberOnlineEventBody>>

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
            BaseEventKey<Online>("kook.member_online", KaiheilaUserOnlineStatusChangedEvent) {
            override fun safeCast(value: Any): Online? = doSafeCast(value)
        }
    }

    /**
     * [KaiheilaUserOnlineStatusChangedEvent] 对于用户离线的事件子类型。
     *
     */
    public abstract class Offline : KaiheilaUserOnlineStatusChangedEvent() {
        abstract override val sourceEvent: KhlEvent<Sys<GuildMemberOfflineEventBody>>

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
            BaseEventKey<Offline>("kook.member_offline", KaiheilaUserOnlineStatusChangedEvent) {
            override fun safeCast(value: Any): Offline? = doSafeCast(value)
        }
    }


}