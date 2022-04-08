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

package love.forte.simbot.component.kaihieila.event

import love.forte.simbot.Api4J
import love.forte.simbot.Timestamp
import love.forte.simbot.action.ActionType
import love.forte.simbot.component.kaihieila.KaiheilaChannel
import love.forte.simbot.component.kaihieila.KaiheilaGuild
import love.forte.simbot.component.kaihieila.KaiheilaGuildMember
import love.forte.simbot.definition.Organization
import love.forte.simbot.event.BaseEventKey
import love.forte.simbot.event.MemberChangedEvent
import love.forte.simbot.event.MemberDecreaseEvent
import love.forte.simbot.event.MemberIncreaseEvent
import love.forte.simbot.kaiheila.event.system.guild.member.ExitedGuildEvent
import love.forte.simbot.kaiheila.event.system.guild.member.ExitedGuildEventBody
import love.forte.simbot.kaiheila.event.system.guild.member.JoinedGuildEvent
import love.forte.simbot.kaiheila.event.system.guild.member.JoinedGuildEventBody
import love.forte.simbot.kaiheila.event.system.user.*
import love.forte.simbot.message.doSafeCast

/**
 * 开黑啦的频道成员变更事件。
 *
 * 相关的开黑啦**原始**事件类型有：
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
public abstract class KaiheilaMemberChangedEvent<out Body, Source : Organization, Before : KaiheilaGuildMember?, After : KaiheilaGuildMember?> :
    KaiheilaSystemEvent<Body>(),
    MemberChangedEvent<Source, Before, After> {


    abstract override val before: Before
    abstract override val after: After
    abstract override val source: Source

    //// impl
    override suspend fun before(): Before = before
    override suspend fun after(): After = after
    override suspend fun source(): Source = source

    @OptIn(Api4J::class)
    override val organization: Source
        get() = source

    override suspend fun organization(): Source = source

    override val changedTime: Timestamp
        get() = sourceEvent.msgTimestamp

    override val visibleScope: love.forte.simbot.event.Event.VisibleScope
        get() = love.forte.simbot.event.Event.VisibleScope.PUBLIC


    @OptIn(Api4J::class)
    abstract override val operator: KaiheilaGuildMember?
    override suspend fun operator(): KaiheilaGuildMember? = operator

    public companion object Key : BaseEventKey<KaiheilaMemberChangedEvent<*, *, *, *>>(
        "kaiheila.member_changed", KaiheilaEvent, MemberChangedEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMemberChangedEvent<*, *, *, *>? = doSafeCast(value)
    }

}

//region member相关

//region 频道进出相关
/**
 * 开黑啦 [成员变更事件][KaiheilaMemberChangedEvent] 中与**频道进出**相关的变更事件。
 * 这类事件代表某人进入、离开某个频道（通常为语音频道），而不代表成员进入、离开了当前的频道服务器（`guild`）。
 */
public abstract class KaiheilaMemberChannelChangedEvent<out Body, Before : KaiheilaGuildMember?, After : KaiheilaGuildMember?> :
    KaiheilaMemberChangedEvent<Body, KaiheilaChannel, Before, After>() {

    public companion object Key : BaseEventKey<KaiheilaMemberChannelChangedEvent<*, *, *>>(
        "kaiheila.member_channel_changed", KaiheilaMemberChangedEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMemberChannelChangedEvent<*, *, *>? = doSafeCast(value)
    }

}


/**
 * 开黑啦成员离开(频道)事件。
 *
 * @see UserExitedChannelEvent
 * @author forte
 */
public abstract class KaiheilaMemberExitedChannelEvent :
    KaiheilaMemberChannelChangedEvent<UserExitedChannelEventBody, KaiheilaGuildMember, KaiheilaGuildMember?>(),
    MemberDecreaseEvent<KaiheilaChannel, KaiheilaGuildMember> {

    abstract override val target: KaiheilaGuildMember


    //// Impl props
    override val after: KaiheilaGuildMember?
        get() = target
    override val before: KaiheilaGuildMember
        get() = target

    override suspend fun target(): KaiheilaGuildMember = target
    override suspend fun after(): KaiheilaGuildMember? = after
    override suspend fun before(): KaiheilaGuildMember = before

    /**
     * 开黑啦群员离开频道事件的行为类型始终为主动的。
     */
    override val actionType: ActionType
        get() = ActionType.PROACTIVE

    /**
     * 开黑啦群员离开频道事件的操作者始终为null （无法确定操作者）。
     */
    @Suppress("UnnecessaryOptInAnnotation")
    @OptIn(Api4J::class)
    override val operator: KaiheilaGuildMember?
        get() = null

    override val key: love.forte.simbot.event.Event.Key<out KaiheilaMemberExitedChannelEvent>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaMemberExitedChannelEvent>(
        "kaiheila.member_exited_channel", KaiheilaMemberChannelChangedEvent, MemberDecreaseEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMemberExitedChannelEvent? = doSafeCast(value)
    }
}

/**
 * 开黑啦成员加入(频道)事件。
 *
 * @see UserJoinedChannelEvent
 * @author forte
 */
public abstract class KaiheilaMemberJoinedChannelEvent :
    KaiheilaMemberChannelChangedEvent<UserJoinedChannelEventBody, KaiheilaGuildMember?, KaiheilaGuildMember>(),
    MemberIncreaseEvent<KaiheilaChannel, KaiheilaGuildMember> {

    abstract override val target: KaiheilaGuildMember

    //// Impl props
    override val after: KaiheilaGuildMember
        get() = target
    override val before: KaiheilaGuildMember?
        get() = target

    override suspend fun target(): KaiheilaGuildMember = target
    override suspend fun after(): KaiheilaGuildMember = after
    override suspend fun before(): KaiheilaGuildMember? = before

    /**
     * 开黑啦群员离开频道事件的行为类型始终为主动的。
     */
    override val actionType: ActionType
        get() = ActionType.PROACTIVE


    /**
     * 开黑啦群员进入频道事件的操作者始终为null （无法确定操作者）。
     */
    @Suppress("UnnecessaryOptInAnnotation")
    @OptIn(Api4J::class)
    override val operator: KaiheilaGuildMember?
        get() = null

    override val key: love.forte.simbot.event.Event.Key<out KaiheilaMemberJoinedChannelEvent>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaMemberJoinedChannelEvent>(
        "kaiheila.member_joined_channel", KaiheilaMemberChannelChangedEvent, MemberIncreaseEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMemberJoinedChannelEvent? = doSafeCast(value)
    }
}
//endregion


//region 频道服务器进出
/**
 * 开黑啦 [成员变更事件][KaiheilaMemberChangedEvent] 中与**频道服务器进出**相关的变更事件。
 * 这类事件代表某人加入、离开某个频道服务器。
 */
public abstract class KaiheilaMemberGuildChangedEvent<out Body, Before : KaiheilaGuildMember?, After : KaiheilaGuildMember?> :
    KaiheilaMemberChangedEvent<Body, KaiheilaGuild, Before, After>() {

    public companion object Key : BaseEventKey<KaiheilaMemberGuildChangedEvent<*, *, *>>(
        "kaiheila.member_guild_changed", KaiheilaMemberChangedEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMemberGuildChangedEvent<*, *, *>? = doSafeCast(value)
    }
}


/**
 * 开黑啦成员离开(频道)事件。
 *
 * @see ExitedGuildEvent
 * @author forte
 */
public abstract class KaiheilaMemberExitedGuildEvent :
    KaiheilaMemberGuildChangedEvent<ExitedGuildEventBody, KaiheilaGuildMember, KaiheilaGuildMember?>(),
    MemberDecreaseEvent<KaiheilaGuild, KaiheilaGuildMember> {

    abstract override val target: KaiheilaGuildMember


    //// Impl props
    override val after: KaiheilaGuildMember?
        get() = target
    override val before: KaiheilaGuildMember
        get() = target

    override suspend fun target(): KaiheilaGuildMember = target
    override suspend fun after(): KaiheilaGuildMember? = after
    override suspend fun before(): KaiheilaGuildMember = before

    /**
     * 开黑啦群员离开频道事件的行为类型始终为主动的。
     */
    override val actionType: ActionType
        get() = ActionType.PROACTIVE

    /**
     * 开黑啦群员离开频道事件的操作者始终为null （无法确定操作者）。
     */
    @Suppress("UnnecessaryOptInAnnotation")
    @OptIn(Api4J::class)
    override val operator: KaiheilaGuildMember?
        get() = null

    override val key: love.forte.simbot.event.Event.Key<out KaiheilaMemberExitedChannelEvent>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaMemberExitedChannelEvent>(
        "kaiheila.member_exited_channel", KaiheilaMemberChannelChangedEvent, MemberDecreaseEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMemberExitedChannelEvent? = doSafeCast(value)
    }
}

/**
 * 开黑啦成员加入(频道)事件。
 *
 * @see JoinedGuildEvent
 * @author forte
 */
public abstract class KaiheilaMemberJoinedGuildEvent :
    KaiheilaMemberGuildChangedEvent<JoinedGuildEventBody, KaiheilaGuildMember?, KaiheilaGuildMember>(),
    MemberIncreaseEvent<KaiheilaGuild, KaiheilaGuildMember> {

    abstract override val target: KaiheilaGuildMember

    //// Impl props
    override val after: KaiheilaGuildMember
        get() = target
    override val before: KaiheilaGuildMember?
        get() = target

    override suspend fun target(): KaiheilaGuildMember = target
    override suspend fun after(): KaiheilaGuildMember = after
    override suspend fun before(): KaiheilaGuildMember? = before

    /**
     * 开黑啦群员离开频道事件的行为类型始终为主动的。
     */
    override val actionType: ActionType
        get() = ActionType.PROACTIVE


    /**
     * 开黑啦群员进入频道事件的操作者始终为null （无法确定操作者）。
     */
    @Suppress("UnnecessaryOptInAnnotation")
    @OptIn(Api4J::class)
    override val operator: KaiheilaGuildMember?
        get() = null

    override val key: love.forte.simbot.event.Event.Key<out KaiheilaMemberJoinedGuildEvent>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaMemberJoinedGuildEvent>(
        "kaiheila.member_joined_guild", KaiheilaMemberGuildChangedEvent, MemberIncreaseEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMemberJoinedGuildEvent? = doSafeCast(value)
    }
}

//endregion


//endregion


//region bot相关
/**
 * 频道成员的变动事件中，变动本体为bot自身时的事件。对应开黑啦原始事件的 [SelfExitedGuildEvent] 和 [SelfJoinedGuildEvent]。
 *
 * @see KaiheilaBotMemberChangedEvent
 *
 * @author forte
 */
public abstract class KaiheilaBotMemberChangedEvent<out Body, Before : KaiheilaGuildMember?, After : KaiheilaGuildMember?> :
    KaiheilaMemberChangedEvent<Body, KaiheilaGuild, Before, After>() {
    public companion object Key : BaseEventKey<KaiheilaBotMemberChangedEvent<*, *, *>>(
        "kaiheila.bot_member_changed", KaiheilaMemberChangedEvent
    ) {
        override fun safeCast(value: Any): KaiheilaBotMemberChangedEvent<*, *, *>? = doSafeCast(value)
    }
}


/**
 * 开黑啦BOT自身离开(频道)事件。
 *
 * @see SelfExitedGuildEvent
 * @author forte
 */
public abstract class KaiheilaBotSelfExitedGuildEvent :
    KaiheilaBotMemberChangedEvent<SelfExitedGuildEventBody, KaiheilaGuildMember, KaiheilaGuildMember?>(),
    MemberDecreaseEvent<KaiheilaGuild, KaiheilaGuildMember> {
    /**
     * 即bot自身在频道服务器内的信息。
     */
    abstract override val target: KaiheilaGuildMember

    //// Impl props
    override val before: KaiheilaGuildMember
        get() = target

    /**
     * `after` 恒为null。
     */
    override val after: KaiheilaGuildMember?
        get() = null


    /**
     * 开黑啦bot离开频道事件的操作者始终为null （无法确定操作者）。
     */
    @Suppress("UnnecessaryOptInAnnotation")
    @OptIn(Api4J::class)
    override val operator: KaiheilaGuildMember?
        get() = null

    override suspend fun target(): KaiheilaGuildMember = target
    override suspend fun after(): KaiheilaGuildMember? = after
    override suspend fun before(): KaiheilaGuildMember = before

    /**
     * 开黑啦群员离开频道事件的行为类型始终为主动的。
     */
    override val actionType: ActionType
        get() = ActionType.PROACTIVE

    override val key: love.forte.simbot.event.Event.Key<out KaiheilaBotSelfExitedGuildEvent>
        get() = Key


    public companion object Key : BaseEventKey<KaiheilaBotSelfExitedGuildEvent>(
        "kaiheila.bot_self_exited", KaiheilaBotMemberChangedEvent, MemberDecreaseEvent
    ) {
        override fun safeCast(value: Any): KaiheilaBotSelfExitedGuildEvent? = doSafeCast(value)
    }
}

/**
 * 开黑啦BOT自身加入(频道)事件。
 *
 * @see SelfJoinedGuildEvent
 * @author forte
 */
public abstract class KaiheilaBotSelfJoinedGuildEvent :
    KaiheilaBotMemberChangedEvent<SelfJoinedGuildEventBody, KaiheilaGuildMember?, KaiheilaGuildMember>(),
    MemberIncreaseEvent<KaiheilaGuild, KaiheilaGuildMember> {
    /**
     * 即bot自身在频道服务器内的信息。
     */
    abstract override val target: KaiheilaGuildMember

    //// Impl props
    override val after: KaiheilaGuildMember
        get() = target

    /**
     * `before` 恒为null。
     */
    override val before: KaiheilaGuildMember?
        get() = null

    /**
     * 开黑啦bot进入频道事件的操作者始终为null （无法确定操作者）。
     */
    @Suppress("UnnecessaryOptInAnnotation")
    @OptIn(Api4J::class)
    override val operator: KaiheilaGuildMember?
        get() = null


    override suspend fun target(): KaiheilaGuildMember = target
    override suspend fun after(): KaiheilaGuildMember = after
    override suspend fun before(): KaiheilaGuildMember? = before

    /**
     * 开黑啦群员离开频道事件的行为类型始终为主动的。
     */
    override val actionType: ActionType
        get() = ActionType.PROACTIVE

    override val key: love.forte.simbot.event.Event.Key<out KaiheilaBotSelfJoinedGuildEvent>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaBotSelfJoinedGuildEvent>(
        "kaiheila.bot_self_joined", KaiheilaBotMemberChangedEvent, MemberIncreaseEvent
    ) {
        override fun safeCast(value: Any): KaiheilaBotSelfJoinedGuildEvent? = doSafeCast(value)
    }
}
//endregion

