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

package love.forte.simbot.kook

import kotlinx.serialization.json.Json
import love.forte.simbot.kook.event.*

/**
 * 提供一个事件类型，
 * 并且可选的提供一个extra的子类型，监听这个事件。
 */
public inline fun KaiheilaBot.processor(
    eventType: Event.Type,
    extraType: String,
    crossinline block: suspend Signal.Event.(decoder: Json, decoded: () -> Any) -> Unit
) {
    processor0(eventType, extraType, block)
    // processor { decoder, decoded ->
    //     if (type == eventType) {
    //         this.block(decoder, decoded)
    //     }
    // }
}

/**
 * 提供一个事件类型，
 * 并且可选的提供一个extra的子类型，监听这个事件。
 */
public inline fun KaiheilaBot.processor(
    eventType: Event.Type,
    extraType: Event.Type?,
    crossinline block: suspend Signal.Event.(decoder: Json, decoded: () -> Any) -> Unit
) {
    processor0(eventType, extraType, block)
}

/**
 * 提供一个事件类型，
 * 并且可选的提供一个extra的子类型，监听这个事件。
 */
@PublishedApi
internal inline fun KaiheilaBot.processor0(
    eventType: Event.Type,
    extraType: Any?,
    crossinline block: suspend Signal.Event.(decoder: Json, decoded: () -> Any) -> Unit
) {
    processor { decoder, decoded ->
        if (type == eventType && (extraType == null || extraType == this.extraType)) {
            this.block(decoder, decoded)
        }
    }
}

/**
 * 提供一个 [EventParser] 进行事件验证并在验证通过时进行事件处理。
 *
 * @see love.forte.simbot.kook.event.message.MessageEventDefinition
 * @see love.forte.simbot.kook.event.system.user.UserEvents
 * @see love.forte.simbot.kook.event.system.message.MessageEvents
 * @see love.forte.simbot.kook.event.system.guild.GuildEvents
 * @see love.forte.simbot.kook.event.system.guild.role.GuildRoleEvents
 * @see love.forte.simbot.kook.event.system.guild.member.GuildMemberEvents
 * @see love.forte.simbot.kook.event.system.channel.ChannelEvents
 *
 */
public inline fun <reified EX : Event.Extra, reified E : Event<EX>> KaiheilaBot.processor(
    eventParser: EventParser<EX, E>,
    crossinline block: suspend (E) -> Unit
) {
    processor { _, decoded ->
        if (eventParser.check(type, extraTypePrimitive)) {
            val decodedEvent = decoded()
            block(
                decoded() as? E
                    ?: throw EventParserException("Event decoded as ${E::class} failed. decoded: $decodedEvent, target event parser: $eventParser")
            )
        }
    }
}

/**
 * 提供一个 [KaiheilaEventParserDefinition] 进行事件验证并在验证通过时进行事件处理。
 *
 * @see love.forte.simbot.kook.event.message.MessageEventDefinition
 * @see love.forte.simbot.kook.event.system.user.UserEvents
 * @see love.forte.simbot.kook.event.system.message.MessageEvents
 * @see love.forte.simbot.kook.event.system.guild.GuildEvents
 * @see love.forte.simbot.kook.event.system.guild.role.GuildRoleEvents
 * @see love.forte.simbot.kook.event.system.guild.member.GuildMemberEvents
 * @see love.forte.simbot.kook.event.system.channel.ChannelEvents
 *
 */
public inline fun <reified EX : Event.Extra, reified E : Event<EX>> KaiheilaBot.processor(
    eventDefinition: KaiheilaEventParserDefinition<EX, E>,
    crossinline block: suspend (E) -> Unit
) {
    processor(eventDefinition.parser, block)
}

/**
 * 提供一个 [EventParser] 进行事件验证并在验证通过时进行事件处理。
 *
 * 由于 Kook 事件可能会触发bot自身发的消息，通过 [processorExcludeSelf]
 *
 * @see love.forte.simbot.kook.event.message.MessageEventDefinition
 * @see love.forte.simbot.kook.event.system.user.UserEvents
 * @see love.forte.simbot.kook.event.system.message.MessageEvents
 * @see love.forte.simbot.kook.event.system.guild.GuildEvents
 * @see love.forte.simbot.kook.event.system.guild.role.GuildRoleEvents
 * @see love.forte.simbot.kook.event.system.guild.member.GuildMemberEvents
 * @see love.forte.simbot.kook.event.system.channel.ChannelEvents
 *
 */
public inline fun <reified EX : Event.Extra, reified E : Event<EX>> KaiheilaBot.processorExcludeSelf(
    eventParser: EventParser<EX, E>,
    crossinline block: suspend (E) -> Unit
) {
    processor { _, decoded ->
        if (eventParser.check(type, extraTypePrimitive)) {
            val decodedEvent = decoded()
            block(
                decoded() as? E
                    ?: throw EventParserException("Event decoded as ${E::class} failed. decoded: $decodedEvent, target event parser: $eventParser")
            )
        }
    }
}

/**
 * 提供一个 [KaiheilaEventParserDefinition] 进行事件验证并在验证通过时进行事件处理。
 *
 * @see love.forte.simbot.kook.event.message.MessageEventDefinition
 * @see love.forte.simbot.kook.event.system.user.UserEvents
 * @see love.forte.simbot.kook.event.system.message.MessageEvents
 * @see love.forte.simbot.kook.event.system.guild.GuildEvents
 * @see love.forte.simbot.kook.event.system.guild.role.GuildRoleEvents
 * @see love.forte.simbot.kook.event.system.guild.member.GuildMemberEvents
 * @see love.forte.simbot.kook.event.system.channel.ChannelEvents
 *
 */
public inline fun <reified EX : Event.Extra, reified E : Event<EX>> KaiheilaBot.processorExcludeSelf(
    eventDefinition: KaiheilaEventParserDefinition<EX, E>,
    crossinline block: suspend (E) -> Unit
) {
    processor(eventDefinition.parser, block)
}


