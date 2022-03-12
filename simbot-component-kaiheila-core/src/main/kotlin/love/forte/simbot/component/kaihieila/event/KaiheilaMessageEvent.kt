package love.forte.simbot.component.kaihieila.event

import love.forte.simbot.*
import love.forte.simbot.component.kaihieila.*
import love.forte.simbot.definition.*
import love.forte.simbot.definition.Channel
import love.forte.simbot.event.*
import love.forte.simbot.kaiheila.event.message.*
import love.forte.simbot.message.*
import love.forte.simbot.kaiheila.event.Event as KhlEvent
import love.forte.simbot.kaiheila.event.message.MessageEvent as KhlMessageEvent
import love.forte.simbot.kaiheila.objects.Channel as KhlChannel


/**
 * 开黑啦与消息相关的事件, 即当 [KhlEvent.extra] 类型为 [KhlEvent.Extra.Text] 时所触发的事件。
 *
 * 大部分消息事件都可能由同一个格式衍生为两种类型：私聊与群聊（频道消息），这由 [KhlEvent.channelType] 所决定。
 * 当 [KhlEvent.channelType] 值为 [KhlChannel.Type.GROUP] 时则代表为 [频道消息][ChannelMessageEvent]，
 * 而如果为  [KhlChannel.Type.PERSON] 则代表为 [联系人消息][ContactMessageEvent]。
 *
 *
 *
 * @see KhlEvent.Extra.Text
 *
 * @author ForteScarlet
 */
public sealed class KaiheilaMessageEvent<out EX : MessageEventExtra> :
    KaiheilaEvent<KhlEvent.Extra.Text, KhlMessageEvent<EX>>(), MessageEvent {
    override val key: Event.Key<out KaiheilaMessageEvent<*>>
        get() = Key


    override val messageContent: ReceivedMessageContent
        get() = TODO("Not yet implemented")

    /**
     * 频道消息事件
     */
    public abstract class Group<out EX : MessageEventExtra> : KaiheilaMessageEvent<EX>(), ChannelMessageEvent {

        @OptIn(Api4J::class)
        abstract override val author: KaiheilaGuildMember

        @OptIn(Api4J::class)
        abstract override val channel: KaiheilaChannel

        //region Impls

        override val key: Event.Key<out KaiheilaMessageEvent<*>>
            get() = Key

        @OptIn(Api4J::class)
        override val source: Channel
            get() = channel

        override suspend fun author(): KaiheilaGuildMember = author
        override suspend fun channel(): KaiheilaChannel = channel
        override suspend fun source(): KaiheilaChannel = channel

        @OptIn(Api4J::class)
        override val organization: KaiheilaChannel
            get() = channel

        override suspend fun organization(): KaiheilaChannel = channel
        //endregion

        public companion object Key :
            BaseEventKey<Group<*>>("kaiheila.message_event_group", KaiheilaMessageEvent, ChannelMessageEvent) {
            override fun safeCast(value: Any): Group<*>? = doSafeCast(value)
        }
    }

    /**
     * 私聊消息事件
     */
    public abstract class Person<out EX : MessageEventExtra> : KaiheilaMessageEvent<EX>(), ContactMessageEvent {


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