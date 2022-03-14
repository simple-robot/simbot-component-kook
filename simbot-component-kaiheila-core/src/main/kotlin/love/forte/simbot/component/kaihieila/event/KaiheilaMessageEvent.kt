package love.forte.simbot.component.kaihieila.event

import love.forte.simbot.*
import love.forte.simbot.component.kaihieila.*
import love.forte.simbot.component.kaihieila.message.*
import love.forte.simbot.definition.Channel
import love.forte.simbot.event.*
import love.forte.simbot.kaiheila.event.message.*
import love.forte.simbot.message.*
import love.forte.simbot.utils.*
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
 * 提供了两个类型用于区别消息是否来自于 bot 自身：[KaiheilaNormalMessageEvent]、[KaiheilaBotSelfMessageEvent].
 *
 * @see KhlEvent.Extra.Text
 *
 * @see KaiheilaNormalMessageEvent
 * @see KaiheilaBotSelfMessageEvent
 *
 * @author ForteScarlet
 */
public sealed class KaiheilaMessageEvent<out EX : MessageEventExtra> :
    KaiheilaEvent<KhlEvent.Extra.Text, KhlMessageEvent<EX>>(), MessageEvent {
    override val key: Event.Key<out KaiheilaMessageEvent<*>>
        get() = Key

    abstract override val messageContent: KaiheilaReceiveMessageContent


    /**
     * 频道消息事件。
     *
     * 此事件只会由 bot 自身以外的人触发。
     */
    public abstract class Group<out EX : MessageEventExtra> : KaiheilaMessageEvent<EX>(), ChannelMessageEvent {

        /**
         * 消息的发送者。不会是bot自己。
         */
        @OptIn(Api4J::class)
        abstract override val author: KaiheilaGuildMember

        /**
         * 消息产生的频道。
         */
        @OptIn(Api4J::class)
        abstract override val channel: KaiheilaChannel

        //region Impls

        /**
         * 可见性。始终为 [Event.VisibleScope.PUBLIC].
         */
        override val visibleScope: Event.VisibleScope
            get() = Event.VisibleScope.PUBLIC

        /**
         * Event Key.
         */
        override val key: Event.Key<out Group<*>>
            get() = Key

        /**
         * 消息产生的频道。
         */
        @OptIn(Api4J::class)
        override val source: Channel
            get() = channel

        /**
         * 消息的发送者。不会是bot自己。
         */
        override suspend fun author(): KaiheilaGuildMember = author

        /**
         * 消息产生的频道。
         */
        override suspend fun channel(): KaiheilaChannel = channel

        /**
         * 消息产生的频道。
         */
        override suspend fun source(): KaiheilaChannel = channel

        /**
         * 消息产生的频道。
         */
        @OptIn(Api4J::class)
        override val organization: KaiheilaChannel
            get() = channel

        /**
         * 消息产生的频道。
         */
        override suspend fun organization(): KaiheilaChannel = channel
        //endregion

        public companion object Key :
            BaseEventKey<Group<*>>("kaiheila.message_event_group", KaiheilaMessageEvent, ChannelMessageEvent) {
            override fun safeCast(value: Any): Group<*>? = doSafeCast(value)
        }
    }

    /**
     * 私聊消息事件。
     *
     * 此事件只会由 bot 自身以外的人触发。
     */
    public abstract class Person<out EX : MessageEventExtra> : KaiheilaMessageEvent<EX>(), ContactMessageEvent {

        @OptIn(ExperimentalSimbotApi::class)
        abstract override suspend fun user(): KaiheilaUserChat

        @OptIn(ExperimentalSimbotApi::class)
        override suspend fun source(): KaiheilaUserChat = user()

        @Api4J
        @OptIn(ExperimentalSimbotApi::class)
        override val user: KaiheilaUserChat
            get() = runInBlocking { user() }

        //region Impls
        @Api4J
        @OptIn(ExperimentalSimbotApi::class)
        override val source: KaiheilaUserChat
            get() = user


        override val key: Event.Key<out Person<*>>
            get() = Key
        //endregion

        public companion object Key :
            BaseEventKey<Person<*>>("kaiheila.message_event_person", KaiheilaMessageEvent, ContactMessageEvent) {
            override fun safeCast(value: Any): Person<*>? = doSafeCast(value)
        }
    }

    public companion object Key : BaseEventKey<KaiheilaMessageEvent<*>>(
        "kaiheila.message_event", MessageEvent
    ) {
        override fun safeCast(value: Any): KaiheilaMessageEvent<*>? = doSafeCast(value)
    }
}


/**
 * 开黑啦普通消息事件类型，即非来自bot自身发送的消息的类型。
 *
 * 此事件下接收的消息不会是bot自己所发的消息。
 *
 *
 */
public sealed class KaiheilaNormalMessageEvent<out EX : MessageEventExtra> : KaiheilaMessageEvent<EX>() {

    /**
     * 频道消息事件。
     *
     * 此事件只会由 bot 自身以外的人触发。
     */
    public abstract class Group<out EX : MessageEventExtra> : KaiheilaMessageEvent.Group<EX>() {

        /**
         * Event Key.
         */
        override val key: Event.Key<out Group<*>>
            get() = Key

        public companion object Key :
            BaseEventKey<Group<*>>("kaiheila.normal_message_event_group", Group) {
            override fun safeCast(value: Any): Group<*>? = doSafeCast(value)
        }
    }

    /**
     * 私聊消息事件。
     *
     * 此事件只会由 bot 自身以外的人触发。
     */
    public abstract class Person<out EX : MessageEventExtra> : KaiheilaMessageEvent.Person<EX>() {

        override val key: Event.Key<out Person<*>>
            get() = Key
        //endregion

        public companion object Key :
            BaseEventKey<Person<*>>("kaiheila.normal_message_event_person", Person) {
            override fun safeCast(value: Any): Person<*>? = doSafeCast(value)
        }
    }
}

/**
 * 开黑啦bot消息事件类型，即来自bot自身发送的消息的类型。
 *
 * 此事件下接收的消息只会是bot自己所发的消息。
 *
 *
 */
public sealed class KaiheilaBotSelfMessageEvent<out EX : MessageEventExtra> : KaiheilaMessageEvent<EX>() {


    /**
     * 频道消息事件。
     *
     * 此事件只会由 bot 自身以外的人触发。
     */
    public abstract class Group<out EX : MessageEventExtra> : KaiheilaMessageEvent.Group<EX>() {
        override val key: Event.Key<out Group<*>>
            get() = Key

        public companion object Key :
            BaseEventKey<Group<*>>("kaiheila.bot_self_message_event_group", Group) {
            override fun safeCast(value: Any): Group<*>? = doSafeCast(value)
        }
    }

    /**
     * 私聊消息事件。
     *
     * 此事件只会由 bot 自身以外的人触发。
     */
    public abstract class Person<out EX : MessageEventExtra> : KaiheilaMessageEvent.Person<EX>() {
        override val key: Event.Key<out Person<*>>
            get() = Key
        //endregion

        public companion object Key :
            BaseEventKey<Person<*>>(
                "kaiheila.bot_self_message_event_person",
                Person
            ) {
            override fun safeCast(value: Any): Person<*>? = doSafeCast(value)
        }
    }
}