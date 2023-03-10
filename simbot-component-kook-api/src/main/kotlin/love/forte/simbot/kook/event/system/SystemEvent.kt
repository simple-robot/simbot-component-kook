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
package love.forte.simbot.kook.event.system

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.kook.event.Event
import love.forte.simbot.kook.event.EventParser
import love.forte.simbot.kook.event.KookEventParserDefinition
import love.forte.simbot.kook.event.SysEventParser
import love.forte.simbot.kook.objects.Channel
import kotlin.collections.set


@Suppress("RemoveExplicitTypeArguments")
internal inline fun <reified B> sysParser(
    subType: String,
    serializer: KSerializer<out B>
): SysEventParser<B> = SysEventParser<B>(
    Event.Type.SYS,
    subType,
    serializer
)


internal inline fun <reified B> MutableMap<Any, EventParser<*, *>>.registerParsers(
    subType: String,
    parser: SysEventParser<B>
) {
    this[subType] = parser
}

/**
 * 系统事件的 [KookEventParserDefinition] 的基础抽象定义, 使用 [SimpleSystemEventExtra] 作为 extra 数据的解析类型。
 */
public abstract class SystemEventParserDefinition<out B> :
    KookEventParserDefinition<SimpleSystemEventExtra<B>, SystemEvent<B, SimpleSystemEventExtra<B>>> {
    abstract override val parser: SysEventParser<B>
}



/**
 * 系统相关事件接口。
 */
public interface SystemEvent<out B, out EX : Event.Extra.Sys<B>> : Event<EX> {
    override val channelType: Channel.Type
    override val type: Event.Type
    override val targetId: ID
    override val authorId: ID
    override val content: String
    override val msgId: ID
    override val msgTimestamp: Timestamp
    override val nonce: String
    override val extra: EX
}

/**
 * 针对 [Event.Extra.Sys] 的简单实现。
 */
@Serializable
public data class SimpleSystemEventExtra<out B>(override val type: String, override val body: B) : Event.Extra.Sys<B>


@Serializable
internal data class SystemEventImpl<B>(
    @SerialName("channel_type")
    override val channelType: Channel.Type,
    override val type: Event.Type,
    @SerialName("target_id")
    override val targetId: CharSequenceID,
    @SerialName("author_id")
    override val authorId: CharSequenceID,
    override val content: String,
    @SerialName("msg_id")
    override val msgId: CharSequenceID,
    @SerialName("msg_timestamp")
    override val msgTimestamp: Timestamp,
    override val nonce: String,
    override val extra: SimpleSystemEventExtra<B>
) : SystemEvent<B, SimpleSystemEventExtra<B>>

