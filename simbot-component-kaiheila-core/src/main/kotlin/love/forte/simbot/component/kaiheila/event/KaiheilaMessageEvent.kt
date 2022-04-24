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
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.action.DeleteSupport
import love.forte.simbot.component.kaiheila.KaiheilaChannel
import love.forte.simbot.component.kaiheila.KaiheilaGuildMember
import love.forte.simbot.component.kaiheila.KaiheilaUserChat
import love.forte.simbot.component.kaiheila.message.KaiheilaReceiveMessageContent
import love.forte.simbot.event.*
import love.forte.simbot.kaiheila.event.message.MessageEventExtra
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.utils.runInBlocking
import love.forte.simbot.kaiheila.event.Event as KhlEvent
import love.forte.simbot.kaiheila.event.message.MessageEvent as KhlMessageEvent
import love.forte.simbot.kaiheila.objects.Channel as KhlChannel


/**
 * 开黑啦与消息相关的事件, 即当 [KhlEvent.extra] 类型为 [KhlEvent.Extra.Text] 时所触发的事件。
 *
 * 大部分消息事件都可能由同一个格式衍生为两种类型：私聊与群聊（频道消息），
 * 这由 [KhlEvent.channelType] 所决定。当 [KhlEvent.channelType]
 * 值为 [KhlChannel.Type.GROUP] 时则代表为 [频道消息][ChannelMessageEvent]，
 * 而如果为  [KhlChannel.Type.PERSON] 则代表为
 * [联系人消息][ContactMessageEvent] (并非 [好友消息][FriendMessageEvent])。
 *
 * ## 来源
 * 开黑啦的消息推送同样会推送bot自己所发送的消息。在stdlib模块下，
 * 你可能需要自己手动处理对于消息来自于bot自身的情况。但是在当前组件下，[KaiheilaMessageEvent]
 *
 * 来自其他人的事件：[KaiheilaNormalGroupMessageEvent]、[KaiheilaNormalPersonMessageEvent]；
 * 来自bot自己的事件：[KaiheilaBotSelfGroupMessageEvent]、[KaiheilaBotSelfPersonMessageEvent]。
 *
 *
 *
 * @author ForteScarlet
 */
@BaseEvent
public sealed class KaiheilaMessageEvent :
    KaiheilaEvent<KhlEvent.Extra.Text, KhlMessageEvent<MessageEventExtra>>(), MessageEvent {
    override val key: Event.Key<out KaiheilaMessageEvent>
        get() = Key

    /**
     * 接收到的消息体。
     */
    abstract override val messageContent: KaiheilaReceiveMessageContent


    /**
     * 频道消息事件。
     *
     * 此类型可能是 [KaiheilaNormalGroupMessageEvent], 则代表为一个普通的频道成员发送的消息事件；
     * 或者是 [KaiheilaBotSelfGroupMessageEvent], 则代表为bot自己所发出的消息。
     *
     * [普通成员消息][KaiheilaNormalGroupMessageEvent] 会实现 [ChannelMessageEvent],
     * 但是 [bot频道消息][KaiheilaBotSelfGroupMessageEvent] 只会实现基础的 [MessageEvent]、[ChannelEvent]、[MemberEvent].
     *
     * @see KaiheilaNormalGroupMessageEvent
     * @see KaiheilaBotSelfGroupMessageEvent
     *
     */
    public abstract class Group : KaiheilaMessageEvent(), MessageEvent, DeleteSupport {


        /**
         * 消息事件发生的频道。
         */
        @Api4J
        abstract override val source: KaiheilaChannel

        /**
         * 消息事件发生的频道。
         */
        @JvmSynthetic
        abstract override suspend fun source(): KaiheilaChannel


        /**
         * Event Key.
         */
        override val key: Event.Key<out Group>
            get() = Key


        public companion object Key :
            BaseEventKey<Group>("kaiheila.message_group", KaiheilaMessageEvent, MessageEvent) {
            override fun safeCast(value: Any): Group? = doSafeCast(value)
        }
    }

    /**
     * 私聊消息事件。
     *
     * 此类型可能是 [KaiheilaNormalPersonMessageEvent], 则代表为一个普通的联系人发送的私聊消息事件；
     * 或者是 [KaiheilaBotSelfPersonMessageEvent], 则代表为bot自己所发出的私聊消息。
     *
     * [普通联系人私聊消息][KaiheilaNormalPersonMessageEvent] 会实现 [ContactMessageEvent],
     * 但是 [bot私聊消息][KaiheilaBotSelfPersonMessageEvent] 只会实现基础的 [MessageEvent]
     *
     * @see KaiheilaNormalPersonMessageEvent
     * @see KaiheilaBotSelfPersonMessageEvent
     */
    public abstract class Person : KaiheilaMessageEvent(), MessageEvent {

        /**
         * 消息事件发生的对话。
         */
        @Api4J
        @OptIn(ExperimentalSimbotApi::class)
        abstract override val source: KaiheilaUserChat

        /**
         * 消息事件发生的对话。
         */
        @JvmSynthetic
        @OptIn(ExperimentalSimbotApi::class)
        abstract override suspend fun source(): KaiheilaUserChat


        override val key: Event.Key<out Person>
            get() = Key

        public companion object Key :
            BaseEventKey<Person>("kaiheila.message_person", KaiheilaMessageEvent, MessageEvent) {
            override fun safeCast(value: Any): Person? = doSafeCast(value)
        }
    }

    public companion object Key : BaseEventKey<KaiheilaMessageEvent>(
        "kaiheila.message", MessageEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMessageEvent? = doSafeCast(value)
    }
}


/**
 * 开黑啦普通频道消息事件。即来自bot以外的人发送的消息的类型。
 *
 * 此事件只会由 bot 自身以外的人触发。
 */
public abstract class KaiheilaNormalGroupMessageEvent : KaiheilaMessageEvent.Group(), ChannelMessageEvent {

    /**
     * 消息的发送者。不会是bot自己。
     */
    @OptIn(Api4J::class)
    abstract override val author: KaiheilaGuildMember

    /**
     * 消息的发送者。不会是bot自己。
     */
    @JvmSynthetic
    override suspend fun author(): KaiheilaGuildMember = author

    /**
     * 消息产生的频道。同 [source].
     */
    @OptIn(Api4J::class)
    abstract override val channel: KaiheilaChannel

    /**
     * 消息产生的频道。同 [source].
     */
    @JvmSynthetic
    override suspend fun channel(): KaiheilaChannel = channel

    /**
     * 消息产生的频道。同 [channel].
     */
    @OptIn(Api4J::class)
    override val source: KaiheilaChannel
        get() = channel

    /**
     * 消息产生的频道。同 [channel].
     */
    @JvmSynthetic
    override suspend fun source(): KaiheilaChannel = channel

    /**
     * 消息产生的频道。同 [channel].
     */
    @OptIn(Api4J::class)
    override val organization: KaiheilaChannel
        get() = channel

    /**
     * 消息产生的频道。同 [channel].
     */
    @JvmSynthetic
    override suspend fun organization(): KaiheilaChannel = channel

    /**
     * Event Key.
     */
    override val key: Event.Key<out Group>
        get() = Key

    public companion object Key :
        BaseEventKey<Group>("kaiheila.normal_group_message", Group, ChannelMessageEvent) {
        override fun safeCast(value: Any): Group? = doSafeCast(value)
    }
}

/**
 * 开黑啦普通私聊消息事件。即来自bot以外的人发送的消息的类型。
 *
 * 此事件只会由 bot 以外的人触发。
 */
public abstract class KaiheilaNormalPersonMessageEvent : KaiheilaMessageEvent.Person(), ContactMessageEvent {

    /**
     * 私聊消息所来自的聊天会话。
     *
     * 会在获取的时候通过api进行查询，没有内部缓存。
     */
    @Api4J
    @OptIn(ExperimentalSimbotApi::class)
    override val user: KaiheilaUserChat
        get() = runInBlocking { user() }

    /**
     * 私聊消息所来自的聊天会话。
     *
     * 会在获取的时候通过api进行查询，没有内部缓存。
     */
    @JvmSynthetic
    @OptIn(ExperimentalSimbotApi::class)
    abstract override suspend fun user(): KaiheilaUserChat

    /**
     * 私聊消息所来自的聊天会话。同 [user]。
     *
     * 会在获取的时候通过api进行查询，没有内部缓存。
     */
    @Api4J
    @OptIn(ExperimentalSimbotApi::class)
    override val source: KaiheilaUserChat
        get() = user


    /**
     * 私聊消息所来自的聊天会话。同 [user]。
     *
     * 会在获取的时候通过api进行查询，没有内部缓存。
     */
    @JvmSynthetic
    @OptIn(ExperimentalSimbotApi::class)
    override suspend fun source(): KaiheilaUserChat = user()


    override val key: Event.Key<out Person>
        get() = Key
    //endregion

    public companion object Key :
        BaseEventKey<Person>(
            "kaiheila.normal_person_message",
            Person, ContactMessageEvent
        ) {
        override fun safeCast(value: Any): Person? = doSafeCast(value)
    }
}


/**
 * 开黑啦bot频道消息事件。即来自bot自身发送的消息的类型。
 *
 * 此事件只会由 bot 自身触发。
 */
public abstract class KaiheilaBotSelfGroupMessageEvent : KaiheilaMessageEvent.Group(), ChannelEvent, MemberEvent {


    /**
     * 发生事件的频道。
     */
    @OptIn(Api4J::class)
    abstract override val channel: KaiheilaChannel


    /**
     * 发生事件的频道。
     */
    @JvmSynthetic
    override suspend fun channel(): KaiheilaChannel = channel


    /**
     * 发生事件的频道。同 [channel]。
     */
    @OptIn(Api4J::class)
    override val source: KaiheilaChannel
        get() = channel

    /**
     * 发生事件的频道。同 [channel]。
     */
    @JvmSynthetic
    override suspend fun source(): KaiheilaChannel = source


    /**
     * 发生事件的频道。同 [channel]。
     */
    @OptIn(Api4J::class)
    override val organization: KaiheilaChannel
        get() = channel

    /**
     * 发生事件的频道。同 [channel]。
     */
    @JvmSynthetic
    override suspend fun organization(): KaiheilaChannel = channel

    /**
     * 消息发送者，也就是bot自身的信息。
     */
    @OptIn(Api4J::class)
    abstract override val member: KaiheilaGuildMember

    /**
     * 消息发送者，也就是bot自身的信息。
     */
    @JvmSynthetic
    override suspend fun member(): KaiheilaGuildMember = member

    /**
     * 消息发送者，也就是bot自身的信息。同 [member].
     */
    @OptIn(Api4J::class)
    override val user: KaiheilaGuildMember get() = member

    /**
     * 消息发送者，也就是bot自身的信息。同 [member].
     */
    @JvmSynthetic
    override suspend fun user(): KaiheilaGuildMember = member


    override val key: Event.Key<out Group>
        get() = Key

    public companion object Key :
        BaseEventKey<Group>("kaiheila.bot_self_group_message", Group, ChannelEvent, MemberEvent) {
        override fun safeCast(value: Any): Group? = doSafeCast(value)
    }
}

/**
 * 私聊消息事件。
 *
 * 此事件只会由 bot 自身触发，代表bot在私聊会话中发出的消息。
 */
public abstract class KaiheilaBotSelfPersonMessageEvent : KaiheilaMessageEvent.Person() {

    /**
     * 发生事件的私聊会话。
     */
    @Api4J
    @OptIn(ExperimentalSimbotApi::class)
    override val source: KaiheilaUserChat
        get() = runInBlocking { source() }

    /**
     * 发生事件的私聊会话。
     */
    @JvmSynthetic
    @OptIn(ExperimentalSimbotApi::class)
    abstract override suspend fun source(): KaiheilaUserChat


    override val key: Event.Key<out Person>
        get() = Key


    public companion object Key :
        BaseEventKey<Person>(
            "kaiheila.bot_self_person_message",
            Person
        ) {
        override fun safeCast(value: Any): Person? = doSafeCast(value)
    }
}