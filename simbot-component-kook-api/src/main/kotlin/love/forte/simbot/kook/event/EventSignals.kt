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

package love.forte.simbot.kook.event

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.*
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.kook.event.message.*
import love.forte.simbot.kook.event.system.SimpleSystemEventExtra
import love.forte.simbot.kook.event.system.SystemEvent
import love.forte.simbot.kook.event.system.SystemEventImpl
import love.forte.simbot.kook.event.system.channel.channelEventParsers
import love.forte.simbot.kook.event.system.guild.guildEventParsers
import love.forte.simbot.kook.event.system.guild.member.guildMemberEventParsers
import love.forte.simbot.kook.event.system.guild.role.guildRoleEventParsers
import love.forte.simbot.kook.event.system.message.messageEventParsers
import love.forte.simbot.kook.event.system.user.userEventParsers

/**
 * 事件原始数据处理器，提供一个从事件得到的原始 [JsonElement]，将其反序列化为一个 [Event] 实例。
 */
public abstract class EventParser<out EX : Event.Extra, out E : Event<EX>> {

    /**
     * 通过 [eventType] 和 [subType] 来判断此事件是否可以由自身处理。
     *
     * @param eventType 事件外层的 [type][Event.type] 属性，通常为数字类型。
     * @param subType 事件的 [extra][Event.extra] 中的 type 属性，
     * 当为消息事件时通常为数字类型，
     * 当为系统事件的时候通常为字符串类型。
     */
    public abstract fun check(eventType: Event.Type, subType: JsonPrimitive): Boolean

    /**
     * 提供原始数据，并将其转化为对应的具体事件类型。
     *
     * @throws SerializationException 序列化过程中可能会遇到任何序列化相关异常
     */
    public abstract fun deserialize(decoder: Json, rawData: JsonElement): E
}


/**
 * 针对于消息事件的事件处理器。
 */
public class MessageEventParser<out EX : MessageEventExtra>(
    private val type: Event.Type,
    extraSerializer: KSerializer<out EX>
) : EventParser<EX, MessageEvent<EX>>() {
    private val eventSerializer: KSerializer<out MessageEvent<EX>> = MessageEventImpl.serializer(extraSerializer)
    override fun check(eventType: Event.Type, subType: JsonPrimitive): Boolean {
        return type == eventType && type.type == subType.intOrNull
    }

    override fun deserialize(decoder: Json, rawData: JsonElement): MessageEvent<EX> {
        return decoder.decodeFromJsonElement(eventSerializer, rawData)
    }

    override fun toString(): String {
        return "MessageEventParser(type=$type, serializer=$eventSerializer)"
    }
}

/**
 * 使用 [SimpleSystemEventExtra] 作为 extra 的类型的事件解析器。
 */
public class SysEventParser<out B>(
    private val type: Event.Type = Event.Type.SYS,
    private val subType: String,
    extraBodySerializer: KSerializer<out B>
) : EventParser<SimpleSystemEventExtra<B>, SystemEvent<B, SimpleSystemEventExtra<B>>>() {
    private val eventSerializer: KSerializer<out SystemEvent<B, SimpleSystemEventExtra<B>>> =
        SystemEventImpl.serializer(extraBodySerializer)

    override fun check(eventType: Event.Type, subType: JsonPrimitive): Boolean {
        return eventType == type && subType.isString && this.subType == subType.contentOrNull
    }

    override fun deserialize(decoder: Json, rawData: JsonElement): SystemEvent<B, SimpleSystemEventExtra<B>> {
        return decoder.decodeFromJsonElement(eventSerializer, rawData)
    }

}


/**
 * 所有事件以及其对应的定位器。
 *
 */
public object EventSignals {
    private val eventParsers: Array<Map<Any, EventParser<*, *>>> = Array(Event.Type.values().size) { emptyMap() }

    @OptIn(ExperimentalSimbotApi::class)
    private val MESSAGE_EVENT_STANDARD_PARSERS: Map<Any, MessageEventParser<MessageEventExtra>> = mapOf(
        Event.Type.TEXT to TextEventParser,
        Event.Type.IMAGE to ImageEventParser,
        Event.Type.VIDEO to VideoEventParser,
        Event.Type.FILE to FileEventParser,
        Event.Type.KMD to KMarkdownEventParser,
        Event.Type.CARD to CardEventParser
    )

    init {
        eventParsers[Event.Type.TEXT.ordinal] = MESSAGE_EVENT_STANDARD_PARSERS
        eventParsers[Event.Type.IMAGE.ordinal] = MESSAGE_EVENT_STANDARD_PARSERS
        eventParsers[Event.Type.VIDEO.ordinal] = MESSAGE_EVENT_STANDARD_PARSERS
        eventParsers[Event.Type.FILE.ordinal] = MESSAGE_EVENT_STANDARD_PARSERS
        // Voice?
        eventParsers[Event.Type.VOICE.ordinal] = MESSAGE_EVENT_STANDARD_PARSERS
        eventParsers[Event.Type.KMD.ordinal] = MESSAGE_EVENT_STANDARD_PARSERS
        eventParsers[Event.Type.CARD.ordinal] = MESSAGE_EVENT_STANDARD_PARSERS
        eventParsers[Event.Type.SYS.ordinal] = buildMap {
            userEventParsers()
            messageEventParsers()
            channelEventParsers()
            guildEventParsers()
            guildRoleEventParsers()
            guildMemberEventParsers()

            // other sys events..?
        }
    }


    /**
     * 根据 [type] 和 [subType] 尝试定位一个事件解析器。
     */
    public operator fun get(type: Event.Type, subType: Any): EventParser<*, *>? = eventParsers[type.ordinal][subType]

    /**
     * 获取消息事件解析器。如果使用了 [Event.Type.SYS] 或者 [Event.Type.VIDEO], 则得到null。
     */
    public operator fun get(type: Event.Type): MessageEventParser<*>? {
        val subMap = eventParsers[type.ordinal].takeIf { it.size == 1 } ?: return null
        val (key, value) = subMap.entries.first()
        if (key != type) return null

        return value as? MessageEventParser<*>
    }

}

/**
 * 描述一个事件的解析定义, 使用对象或伴生对象实现此接口，提供于用户做事件监听的目标参数。
 * ```kotlin
 * bot.processor(XxxEvent) { event ->
 *      // do..
 * }
 * ```
 *
 */
public interface KookEventParserDefinition<out EX : Event.Extra, out E : Event<EX>> {

    /**
     * 此事件的解析器。
     */
    public val parser: EventParser<EX, E>
}


