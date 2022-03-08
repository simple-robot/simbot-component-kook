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

package love.forte.simbot.kaiheila.event.system

import kotlinx.serialization.*
import love.forte.simbot.kaiheila.event.*
import love.forte.simbot.kaiheila.event.message.*


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
 * 系统事件的 [KaiheilaEventParserDefinition] 的基础抽象定义, 使用 [SimpleSystemEventExtra] 作为 extra 数据的解析类型。
 */
public abstract class SystemEventParserDefinition<out B> :
    KaiheilaEventParserDefinition<SimpleSystemEventExtra<B>, SystemEvent<B, SimpleSystemEventExtra<B>>> {
    abstract override val parser: SysEventParser<B>
}
