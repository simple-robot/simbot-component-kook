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
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.component.kaihieila.KaiheilaGuild
import love.forte.simbot.component.kaihieila.KaiheilaGuildMember
import love.forte.simbot.event.BaseEventKey
import love.forte.simbot.event.MemberChangedEvent
import love.forte.simbot.event.MemberDecreaseEvent
import love.forte.simbot.event.MemberIncreaseEvent
import love.forte.simbot.kaiheila.event.Event
import love.forte.simbot.kaiheila.event.system.user.*
import love.forte.simbot.message.doSafeCast

/**
 * 开黑啦的频道成员变更事件。
 *
 * 对应的开黑啦基础事件类型有：
 * - [UserExitedChannelEvent]
 * - [UserJoinedChannelEvent]
 * - [SelfExitedGuildEvent]
 * - [SelfJoinedGuildEvent]
 *
 * 其中，[SelfExitedGuildEvent] 和 [SelfJoinedGuildEvent]
 * 代表为 BOT 自身作为成员的变动，
 * 因此会额外提供相对应的 [bot成员变动][KaiheilaBotMemberChangedEvent]
 * 事件类型来进行更精准的事件监听。
 *
 * @see KaiheilaBotMemberChangedEvent
 * @see UserExitedChannelEvent
 * @see UserJoinedChannelEvent
 * @see SelfExitedGuildEvent
 * @see SelfJoinedGuildEvent
 * @author forte
 */
public abstract class KaiheilaMemberChangedEvent<out B, Before : KaiheilaGuildMember?, After : KaiheilaGuildMember?> :
    KaiheilaEvent<Event.Extra.Sys<B>, Event<Event.Extra.Sys<B>>>(),
    MemberChangedEvent<KaiheilaGuild, Before, After> {


    abstract override val before: Before
    abstract override val after: After
    abstract override val source: KaiheilaGuild

    //// impl
    override suspend fun before(): Before = before
    override suspend fun after(): After = after
    override suspend fun source(): KaiheilaGuild = source

    @OptIn(Api4J::class)
    override val organization: KaiheilaGuild
        get() = source

    override suspend fun organization(): KaiheilaGuild = source

    override val id: ID
        get() = sourceEvent.msgId

    override val changedTime: Timestamp
        get() = sourceEvent.msgTimestamp

    override val visibleScope: love.forte.simbot.event.Event.VisibleScope
        get() = love.forte.simbot.event.Event.VisibleScope.PUBLIC


    @OptIn(Api4J::class)
    abstract override val operator: KaiheilaGuildMember?
    override suspend fun operator(): KaiheilaGuildMember? = operator

    public companion object Key : BaseEventKey<KaiheilaMemberChangedEvent<*, *, *>>(
        "kaiheila.member_changed", KaiheilaEvent, MemberChangedEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMemberChangedEvent<*, *, *>? = doSafeCast(value)
    }

}

/**
 * 开黑啦成员离开(频道)事件。
 *
 * @see UserExitedChannelEvent
 * @see SelfExitedGuildEvent
 * @author forte
 */
public abstract class KaiheilaMemberExitedEvent :
    KaiheilaMemberChangedEvent<UserExitedChannelEventBody, KaiheilaGuildMember, KaiheilaGuildMember?>(),
    MemberDecreaseEvent<KaiheilaGuild, KaiheilaGuildMember> {

    abstract override val target: KaiheilaGuildMember


    //// Impl props
    override suspend fun target(): KaiheilaGuildMember = target
    override suspend fun after(): KaiheilaGuildMember? = after
    override suspend fun before(): KaiheilaGuildMember = before

    override val key: love.forte.simbot.event.Event.Key<out KaiheilaMemberExitedEvent>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaMemberExitedEvent>(
        "kaiheila.member_exited", KaiheilaMemberChangedEvent, MemberDecreaseEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMemberExitedEvent? = doSafeCast(value)
    }
}

/**
 * 开黑啦成员加入(频道)事件。
 *
 * @see UserJoinedChannelEvent
 * @see UserExitedChannelEvent
 * @author forte
 */
public abstract class KaiheilaMemberJoinedEvent :
    KaiheilaMemberChangedEvent<UserJoinedChannelEventBody, KaiheilaGuildMember?, KaiheilaGuildMember>(),
    MemberIncreaseEvent<KaiheilaGuild, KaiheilaGuildMember> {

    abstract override val target: KaiheilaGuildMember

    //// Impl props
    override suspend fun target(): KaiheilaGuildMember = target
    override suspend fun after(): KaiheilaGuildMember = after
    override suspend fun before(): KaiheilaGuildMember? = before

    override val key: love.forte.simbot.event.Event.Key<out KaiheilaMemberJoinedEvent>
        get() = Key

    public companion object Key : BaseEventKey<KaiheilaMemberJoinedEvent>(
        "kaiheila.member_joined", KaiheilaMemberChangedEvent, MemberIncreaseEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMemberJoinedEvent? = doSafeCast(value)
    }
}


/**
 * 频道成员的变动事件中，变动本体为bot自身时的事件。对应开黑啦基础事件的 [SelfExitedGuildEvent] 和 [SelfJoinedGuildEvent]。
 *
 * @see KaiheilaBotMemberChangedEvent
 * @see SelfExitedGuildEvent
 * @see SelfJoinedGuildEvent
 *
 * @author forte
 */
public abstract class KaiheilaBotMemberChangedEvent<out B, Before : KaiheilaGuildMember?, After : KaiheilaGuildMember?> :
    KaiheilaMemberChangedEvent<B, Before, After>() {
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
public abstract class KaiheilaBotSelfExitedEvent<out B> :
    KaiheilaBotMemberChangedEvent<B, KaiheilaGuildMember, KaiheilaGuildMember>() {
    // TODO

    abstract override val before: KaiheilaGuildMember
    abstract override val after: KaiheilaGuildMember

    //// Impl props
    override suspend fun after(): KaiheilaGuildMember = before
    override suspend fun before(): KaiheilaGuildMember = after

    public companion object Key : BaseEventKey<KaiheilaBotSelfExitedEvent<*>>(
        "kaiheila.bot_self_exited", KaiheilaMemberExitedEvent
    ) {
        override fun safeCast(value: Any): KaiheilaBotSelfExitedEvent<*>? = doSafeCast(value)
    }
}

/**
 * 开黑啦BOT自身加入(频道)事件。
 *
 * @see UserExitedChannelEvent
 * @author forte
 */
public abstract class KaiheilaBotSelfJoinedEvent<out B> :
    KaiheilaBotMemberChangedEvent<B, KaiheilaGuildMember, KaiheilaGuildMember>() {
    // TODO

    abstract override val before: KaiheilaGuildMember
    abstract override val after: KaiheilaGuildMember

    //// Impl props
    override suspend fun after(): KaiheilaGuildMember = before
    override suspend fun before(): KaiheilaGuildMember = after

    public companion object Key : BaseEventKey<KaiheilaBotSelfExitedEvent<*>>(
        "kaiheila.bot_self_joined", KaiheilaMemberExitedEvent
    ) {
        override fun safeCast(value: Any): KaiheilaBotSelfExitedEvent<*>? = doSafeCast(value)
    }
}
