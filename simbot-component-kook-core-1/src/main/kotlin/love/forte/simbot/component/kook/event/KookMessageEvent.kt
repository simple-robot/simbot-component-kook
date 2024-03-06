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
import love.forte.simbot.JST
import love.forte.simbot.JSTP
import love.forte.simbot.Timestamp
import love.forte.simbot.component.kook.KookChannel
import love.forte.simbot.component.kook.KookMember
import love.forte.simbot.component.kook.KookUserChat
import love.forte.simbot.component.kook.message.KookMessageReceipt
import love.forte.simbot.component.kook.message.KookReceiveMessageContent
import love.forte.simbot.event.*
import love.forte.simbot.kook.event.TextExtra
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.kook.event.Event as KEvent

/**
 * KOOK 中与消息相关的事件, 即当 [KEvent.extra] 类型为 [TextExtra] 时所触发的事件。
 *
 * 大部分消息事件都可能由同一个格式衍生为两种类型：私聊与群聊（频道消息），
 * 这由 [KEvent.channelType] 所决定。当 [KEvent.channelType]
 * 值为 [KEvent.ChannelType.GROUP] 时则代表为 [频道消息][ChannelMessageEvent]，
 * 而如果为  [KEvent.ChannelType.PERSON] 则代表为
 * [联系人消息][ContactMessageEvent] (并非 [好友消息][FriendMessageEvent])。
 *
 * ## 来源
 * KOOK 的消息推送同样会推送bot自己所发送的消息。在stdlib模块下，
 * 你可能需要自己手动处理对于消息来自bot自身的情况。
 * 但是在当前组件下，[KookMessageEvent] 中:
 * - 来自其他人的事件: [KookChannelMessageEvent], [KookContactMessageEvent]
 * - 来自bot自己的事件: [KookBotSelfChannelMessageEvent], [KookBotSelfMessageEvent]
 *
 * @author ForteScarlet
 */
@BaseEvent
public sealed class KookMessageEvent : KookEvent<TextExtra, KEvent<TextExtra>>(), MessageEvent {
    override val key: Event.Key<out KookMessageEvent>
        get() = Key

    override val id: ID get() = sourceEvent.msgId.ID

    override val time: Timestamp get() = Timestamp.ofMilliseconds(sourceEvent.msgTimestamp)

    /**
     * 接收到的消息体。
     */
    abstract override val messageContent: KookReceiveMessageContent

    /**
     * 回复此事件。
     *
     * 即向此消息事件的发送者进行**针对性的**消息回复。
     */
    @ST
    abstract override suspend fun reply(message: Message): KookMessageReceipt

    /**
     * 回复此事件。
     *
     * 即向此消息事件的发送者进行**针对性的**消息回复。
     */
    @ST
    abstract override suspend fun reply(text: String): KookMessageReceipt

    /**
     * 回复此事件。
     *
     * 即向此消息事件的发送者进行**针对性的**消息回复。
     */
    @ST
    abstract override suspend fun reply(message: MessageContent): KookMessageReceipt

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
        @STP
        abstract override suspend fun source(): KookChannel


        /**
         * Event Key.
         */
        override val key: Event.Key<out Channel>
            get() = Key


        public companion object Key :
            BaseEventKey<Channel>("kook.base_message_channel", KookMessageEvent, MessageEvent) {
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
        @STP
        abstract override suspend fun source(): KookUserChat


        override val key: Event.Key<out Person>
            get() = Key

        public companion object Key :
            BaseEventKey<Person>("kook.base_message_person", KookMessageEvent, MessageEvent) {
            override fun safeCast(value: Any): Person? = doSafeCast(value)
        }
    }

    public companion object Key : BaseEventKey<KookMessageEvent>(
        "kook.message", KookEvent, MessageEvent
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
    @STP
    abstract override suspend fun author(): KookMember

    /**
     * 消息产生的频道。
     */
    @STP
    abstract override suspend fun channel(): KookChannel

    /**
     * 消息产生的频道。同 [channel].
     */
    @STP
    override suspend fun source(): KookChannel = channel()

    /**
     * 消息产生的频道。同 [channel].
     */
    @STP
    override suspend fun organization(): KookChannel = channel()

    /**
     * Event Key.
     */
    override val key: Event.Key<out KookChannelMessageEvent>
        get() = Key

    public companion object Key :
        BaseEventKey<KookChannelMessageEvent>("kook.channel_message", Channel, ChannelMessageEvent) {
        override fun safeCast(value: Any): KookChannelMessageEvent? = doSafeCast(value)
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
    @STP
    abstract override suspend fun user(): KookUserChat

    /**
     * 私聊消息所来自的聊天会话。同 [user]。
     *
     * 会在获取的时候通过api进行查询，没有内部缓存。
     */
    @STP
    override suspend fun source(): KookUserChat = user()


    override val key: Event.Key<out KookContactMessageEvent>
        get() = Key
    // endregion

    public companion object Key :
        BaseEventKey<KookContactMessageEvent>(
            "kook.contact_message",
            Person, ContactMessageEvent
        ) {
        override fun safeCast(value: Any): KookContactMessageEvent? = doSafeCast(value)
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
    @STP
    abstract override suspend fun channel(): KookChannel


    @STP
    override suspend fun source(): KookChannel = channel()

    /**
     * 发生事件的频道。同 [channel]。
     */
    @STP
    override suspend fun organization(): KookChannel = channel()

    /**
     * 消息发送者，也就是bot自身的信息。
     */
    @STP
    abstract override suspend fun member(): KookMember

    /**
     * 消息发送者，也就是bot自身的信息。同 [member].
     */
    @STP
    override suspend fun user(): KookMember = member()


    override val key: Event.Key<out KookBotSelfChannelMessageEvent>
        get() = Key

    public companion object Key :
        BaseEventKey<KookBotSelfChannelMessageEvent>(
            "kook.bot_self_channel_message",
            Channel,
            ChannelEvent,
            MemberEvent
        ) {
        override fun safeCast(value: Any): KookBotSelfChannelMessageEvent? = doSafeCast(value)
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
    @STP
    abstract override suspend fun source(): KookUserChat


    override val key: Event.Key<out KookBotSelfMessageEvent>
        get() = Key


    public companion object Key :
        BaseEventKey<KookBotSelfMessageEvent>(
            "kook.bot_self_person_message",
            Person
        ) {
        override fun safeCast(value: Any): KookBotSelfMessageEvent? = doSafeCast(value)
    }
}


// TODO 消息更新、消息删除
