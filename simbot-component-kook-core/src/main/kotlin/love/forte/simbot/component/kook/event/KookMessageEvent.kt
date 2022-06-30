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

package love.forte.simbot.component.kook.event

import love.forte.simbot.Api4J
import love.forte.simbot.component.kook.KookChannel
import love.forte.simbot.component.kook.KookGuildMember
import love.forte.simbot.component.kook.KookUserChat
import love.forte.simbot.component.kook.message.KookMessageReceipt
import love.forte.simbot.component.kook.message.KookReceiveMessageContent
import love.forte.simbot.event.*
import love.forte.simbot.kook.event.message.MessageEventExtra
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.utils.runInBlocking
import love.forte.simbot.kook.event.Event as KkEvent
import love.forte.simbot.kook.event.message.MessageEvent as KkMessageEvent
import love.forte.simbot.kook.objects.Channel as KkChannel


/**
 * Kook 与消息相关的事件, 即当 [KkEvent.extra] 类型为 [KkEvent.Extra.Text] 时所触发的事件。
 *
 * 大部分消息事件都可能由同一个格式衍生为两种类型：私聊与群聊（频道消息），
 * 这由 [KkEvent.channelType] 所决定。当 [KkEvent.channelType]
 * 值为 [KkChannel.Type.GROUP] 时则代表为 [频道消息][ChannelMessageEvent]，
 * 而如果为  [KkChannel.Type.PERSON] 则代表为
 * [联系人消息][ContactMessageEvent] (并非 [好友消息][FriendMessageEvent])。
 *
 * ## 来源
 * Kook 的消息推送同样会推送bot自己所发送的消息。在stdlib模块下，
 * 你可能需要自己手动处理对于消息来自于bot自身的情况。但是在当前组件下，[KookMessageEvent]
 *
 * 来自其他人的事件：[KookChannelMessageEvent]、[KookContactMessageEvent]；
 * 来自bot自己的事件：[KookBotSelfChannelMessageEvent]、[KookBotSelfMessageEvent]。
 *
 *
 *
 * @author ForteScarlet
 */
@BaseEvent
public sealed class KookMessageEvent :
    KookEvent<KkEvent.Extra.Text, KkMessageEvent<MessageEventExtra>>(), MessageEvent {
    override val key: Event.Key<out KookMessageEvent>
        get() = Key

    /**
     * 接收到的消息体。
     */
    abstract override val messageContent: KookReceiveMessageContent
    
    /**
     * 回复此事件。
     *
     * 即向此消息事件的发送者进行**针对性的**消息回复。
     */
    @JvmSynthetic
    abstract override suspend fun reply(message: Message): KookMessageReceipt
    
    /**
     * 回复此事件。
     *
     * 即向此消息事件的发送者进行**针对性的**消息回复。
     */
    @JvmSynthetic
    abstract override suspend fun reply(text: String): KookMessageReceipt
    
    /**
     * 回复此事件。
     *
     * 即向此消息事件的发送者进行**针对性的**消息回复。
     */
    @JvmSynthetic
    abstract override suspend fun reply(message: MessageContent): KookMessageReceipt
    
    /**
     * 回复此事件。
     *
     * 即向此消息事件的发送者进行**针对性的**消息回复。
     */
    @Api4J
    override fun replyBlocking(text: String): KookMessageReceipt = runInBlocking { reply(text) }
    
    /**
     * 回复此事件。
     *
     * 即向此消息事件的发送者进行**针对性的**消息回复。
     */
    @Api4J
    override fun replyBlocking(message: Message): KookMessageReceipt = runInBlocking { reply(message) }
    
    /**
     * 回复此事件。
     *
     * 即向此消息事件的发送者进行**针对性的**消息回复。
     */
    @Api4J
    override fun replyBlocking(message: MessageContent): KookMessageReceipt = runInBlocking { reply(message) }
    
    
    /**
     * 频道消息事件。
     *
     * 此类型可能是 [KookChannelMessageEvent], 则代表为一个普通的频道成员发送的消息事件；
     * 或者是 [KookBotSelfChannelMessageEvent], 则代表为bot自己所发出的消息。
     *
     * [普通成员消息][KookChannelMessageEvent] 会实现 [ChannelMessageEvent],
     * 但是 [bot频道消息][KookBotSelfChannelMessageEvent] 只会实现基础的 [MessageEvent]、[ChannelEvent]、[MemberEvent].
     *
     * @see KookChannelMessageEvent
     * @see KookBotSelfChannelMessageEvent
     *
     */
    public abstract class Channel : KookMessageEvent(), MessageEvent {


        /**
         * 消息事件发生的频道。
         */
        @Api4J
        abstract override val source: KookChannel

        /**
         * 消息事件发生的频道。
         */
        @JvmSynthetic
        abstract override suspend fun source(): KookChannel


        /**
         * Event Key.
         */
        override val key: Event.Key<out Channel>
            get() = Key


        public companion object Key :
            BaseEventKey<Channel>("kook.message_group", KookMessageEvent, MessageEvent) {
            override fun safeCast(value: Any): Channel? = doSafeCast(value)
        }
    }

    /**
     * 私聊消息事件。
     *
     * 此类型可能是 [KookContactMessageEvent], 则代表为一个普通的联系人发送的私聊消息事件；
     * 或者是 [KookBotSelfMessageEvent], 则代表为bot自己所发出的私聊消息。
     *
     * [普通联系人私聊消息][KookContactMessageEvent] 会实现 [ContactMessageEvent],
     * 但是 [bot私聊消息][KookBotSelfMessageEvent] 只会实现基础的 [MessageEvent]
     *
     * @see KookContactMessageEvent
     * @see KookBotSelfMessageEvent
     */
    public abstract class Person : KookMessageEvent(), MessageEvent {

        /**
         * 消息事件发生的对话。
         */
        @Api4J
        abstract override val source: KookUserChat

        /**
         * 消息事件发生的对话。
         */
        @JvmSynthetic
        abstract override suspend fun source(): KookUserChat


        override val key: Event.Key<out Person>
            get() = Key

        public companion object Key :
            BaseEventKey<Person>("kook.message_person", KookMessageEvent, MessageEvent) {
            override fun safeCast(value: Any): Person? = doSafeCast(value)
        }
    }

    public companion object Key : BaseEventKey<KookMessageEvent>(
        "kook.message", MessageEvent
    ) {
        override fun safeCast(value: Any): KookMessageEvent? = doSafeCast(value)
    }
}


/**
 * Kook 普通频道消息事件。即来自bot以外的人发送的消息的类型。
 *
 * 此事件只会由 bot 自身以外的人触发。
 */
public abstract class KookChannelMessageEvent : KookMessageEvent.Channel(), ChannelMessageEvent {

    /**
     * 消息的发送者。不会是bot自己。
     */
    @OptIn(Api4J::class)
    abstract override val author: KookGuildMember

    /**
     * 消息的发送者。不会是bot自己。
     */
    @JvmSynthetic
    override suspend fun author(): KookGuildMember = author

    /**
     * 消息产生的频道。同 [source].
     */
    @OptIn(Api4J::class)
    abstract override val channel: KookChannel

    /**
     * 消息产生的频道。同 [source].
     */
    @JvmSynthetic
    override suspend fun channel(): KookChannel = channel

    /**
     * 消息产生的频道。同 [channel].
     */
    @OptIn(Api4J::class)
    override val source: KookChannel
        get() = channel

    /**
     * 消息产生的频道。同 [channel].
     */
    @JvmSynthetic
    override suspend fun source(): KookChannel = channel

    /**
     * 消息产生的频道。同 [channel].
     */
    @OptIn(Api4J::class)
    override val organization: KookChannel
        get() = channel

    /**
     * 消息产生的频道。同 [channel].
     */
    @JvmSynthetic
    override suspend fun organization(): KookChannel = channel

    /**
     * Event Key.
     */
    override val key: Event.Key<out Channel>
        get() = Key

    public companion object Key :
        BaseEventKey<Channel>("kook.normal_group_message", Channel, ChannelMessageEvent) {
        override fun safeCast(value: Any): Channel? = doSafeCast(value)
    }
}

/**
 * Kook 普通私聊消息事件。即来自bot以外的人发送的消息的类型。
 *
 * 此事件只会由 bot 以外的人触发。
 */
public abstract class KookContactMessageEvent : KookMessageEvent.Person(), ContactMessageEvent {

    /**
     * 私聊消息所来自的聊天会话。
     *
     * 会在获取的时候通过api进行查询，没有内部缓存。
     */
    @Api4J
    override val user: KookUserChat
        get() = runInBlocking { user() }

    /**
     * 私聊消息所来自的聊天会话。
     *
     * 会在获取的时候通过api进行查询，没有内部缓存。
     */
    @JvmSynthetic
    abstract override suspend fun user(): KookUserChat

    /**
     * 私聊消息所来自的聊天会话。同 [user]。
     *
     * 会在获取的时候通过api进行查询，没有内部缓存。
     */
    @Api4J
    override val source: KookUserChat
        get() = user


    /**
     * 私聊消息所来自的聊天会话。同 [user]。
     *
     * 会在获取的时候通过api进行查询，没有内部缓存。
     */
    @JvmSynthetic
    override suspend fun source(): KookUserChat = user()


    override val key: Event.Key<out Person>
        get() = Key
    //endregion

    public companion object Key :
        BaseEventKey<Person>(
            "kook.normal_person_message",
            Person, ContactMessageEvent
        ) {
        override fun safeCast(value: Any): Person? = doSafeCast(value)
    }
}


/**
 * Kook bot频道消息事件。即来自bot自身发送的消息的类型。
 *
 * 此事件只会由 bot 自身触发。
 */
public abstract class KookBotSelfChannelMessageEvent : KookMessageEvent.Channel(), ChannelEvent, MemberEvent {


    /**
     * 发生事件的频道。
     */
    @OptIn(Api4J::class)
    abstract override val channel: KookChannel


    /**
     * 发生事件的频道。
     */
    @JvmSynthetic
    override suspend fun channel(): KookChannel = channel


    /**
     * 发生事件的频道。同 [channel]。
     */
    @OptIn(Api4J::class)
    override val source: KookChannel
        get() = channel

    /**
     * 发生事件的频道。同 [channel]。
     */
    @JvmSynthetic
    override suspend fun source(): KookChannel = source


    /**
     * 发生事件的频道。同 [channel]。
     */
    @OptIn(Api4J::class)
    override val organization: KookChannel
        get() = channel

    /**
     * 发生事件的频道。同 [channel]。
     */
    @JvmSynthetic
    override suspend fun organization(): KookChannel = channel

    /**
     * 消息发送者，也就是bot自身的信息。
     */
    @OptIn(Api4J::class)
    abstract override val member: KookGuildMember

    /**
     * 消息发送者，也就是bot自身的信息。
     */
    @JvmSynthetic
    override suspend fun member(): KookGuildMember = member

    /**
     * 消息发送者，也就是bot自身的信息。同 [member].
     */
    @OptIn(Api4J::class)
    override val user: KookGuildMember get() = member

    /**
     * 消息发送者，也就是bot自身的信息。同 [member].
     */
    @JvmSynthetic
    override suspend fun user(): KookGuildMember = member


    override val key: Event.Key<out Channel>
        get() = Key

    public companion object Key :
        BaseEventKey<Channel>("kook.bot_self_group_message", Channel, ChannelEvent, MemberEvent) {
        override fun safeCast(value: Any): Channel? = doSafeCast(value)
    }
}

/**
 * 私聊消息事件。
 *
 * 此事件只会由 bot 自身触发，代表bot在私聊会话中发出的消息。
 */
public abstract class KookBotSelfMessageEvent : KookMessageEvent.Person() {

    /**
     * 发生事件的私聊会话。
     */
    @Api4J
    override val source: KookUserChat
        get() = runInBlocking { source() }

    /**
     * 发生事件的私聊会话。
     */
    @JvmSynthetic
    abstract override suspend fun source(): KookUserChat


    override val key: Event.Key<out Person>
        get() = Key


    public companion object Key :
        BaseEventKey<Person>(
            "kook.bot_self_person_message",
            Person
        ) {
        override fun safeCast(value: Any): Person? = doSafeCast(value)
    }
}