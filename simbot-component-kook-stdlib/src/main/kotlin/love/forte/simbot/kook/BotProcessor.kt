/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook

import kotlinx.serialization.json.Json
import love.forte.simbot.kook.event.*

/**
 * 提供一个事件类型，
 * 并且可选的提供一个extra的子类型，监听这个事件。
 */
public inline fun KookBot.processor(
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
public inline fun KookBot.processor(
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
internal inline fun KookBot.processor0(
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
public inline fun <reified EX : Event.Extra, reified E : Event<EX>> KookBot.processor(
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
 * 提供一个 [KookEventParserDefinition] 进行事件验证并在验证通过时进行事件处理。
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
public inline fun <reified EX : Event.Extra, reified E : Event<EX>> KookBot.processor(
    eventDefinition: KookEventParserDefinition<EX, E>,
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
public inline fun <reified EX : Event.Extra, reified E : Event<EX>> KookBot.processorExcludeSelf(
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
 * 提供一个 [KookEventParserDefinition] 进行事件验证并在验证通过时进行事件处理。
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
public inline fun <reified EX : Event.Extra, reified E : Event<EX>> KookBot.processorExcludeSelf(
    eventDefinition: KookEventParserDefinition<EX, E>,
    crossinline block: suspend (E) -> Unit
) {
    processor(eventDefinition.parser, block)
}


