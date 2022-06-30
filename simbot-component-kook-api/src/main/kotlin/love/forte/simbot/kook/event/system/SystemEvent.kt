/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
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

