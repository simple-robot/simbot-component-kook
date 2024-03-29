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

package love.forte.simbot.component.kook

import love.forte.simbot.FragileSimbotApi
import love.forte.simbot.application.Application
import love.forte.simbot.application.BotManagers
import love.forte.simbot.application.EventProvider
import love.forte.simbot.application.bots
import love.forte.simbot.bot.OriginBotManager
import love.forte.simbot.component.kook.bot.KookBotManager


/**
 * 在 [Application.bots] 作用域中寻找并使用 [KookBotManager].
 *
 * ```kotlin
 * val app = createSimpleApplication {
 *     useKook()
 *     // ...
 * }
 *
 * app.kookBots {
 *   // ...
 * }
 * ```
 *
 * 如果当前环境中不存在任何 [KookBotManager], 则会抛出 [NoSuchElementException]。
 *
 * @throws NoSuchElementException 如果当前环境中不存在任何 [KookBotManager]
 *
 */
public inline fun Application.kookBots(
    block: KookBotManager.(BotManagers) -> Unit,
) {
    bots {
        val manager = providers.firstKookBotManagerOrNull()
            ?: throw NoSuchElementException("No event provider of type [KookBotManager] in providers: $providers")

        manager.block(this)
    }
}

/**
 * 在 [Application.bots] 作用域中寻找并使用 [KookBotManager].
 *
 * ```kotlin
 * val app = createSimpleApplication {
 *    useKook()
 *    // ...
 * }
 *
 * app.kookBotsIfSupport {
 *    // ...
 * }
 * ```
 *
 * 如果当前环境中不存在任何 [KookBotManager], 则不会执行 [block]。
 *
 */
public inline fun Application.kookBotsIfSupport(
    block: KookBotManager.(BotManagers) -> Unit,
) {
    bots {
        val manager = providers.firstKookBotManagerOrNull() ?: return@bots
        manager.block(this)
    }
}

// region bot manager 获取扩展
/**
 * 从一个 [EventProvider] 序列中过滤寻找所有的 [KookBotManager]。
 */
@Suppress("NOTHING_TO_INLINE")
public inline fun Sequence<EventProvider>.filterIsKookBotManagers(): Sequence<KookBotManager> =
    filterIsInstance<KookBotManager>()


/**
 * 从一个 [EventProvider] 列表中过滤寻找所有的 [KookBotManager]。
 */
@Suppress("NOTHING_TO_INLINE")
public inline fun Iterable<EventProvider>.filterIsKookBotManagers(): List<KookBotManager> =
    filterIsInstance<KookBotManager>()


/**
 * 从一个 [EventProvider] 序列中寻找第一个的 [KookBotManager]。
 *
 * @throws NoSuchElementException 当找不到时
 */
@Suppress("NOTHING_TO_INLINE")
public inline fun Sequence<EventProvider>.firstKookBotManager(): KookBotManager =
    first { it is KookBotManager } as KookBotManager


/**
 * 从一个 [EventProvider] 列表中过滤寻找第一个的 [KookBotManager]。
 *
 * @throws NoSuchElementException 当找不到时
 */
@Suppress("NOTHING_TO_INLINE")
public inline fun Iterable<EventProvider>.firstKookBotManager(): KookBotManager =
    first { it is KookBotManager } as KookBotManager


/**
 * 从一个 [EventProvider] 序列中寻找第一个的 [KookBotManager]。
 * 当找不到时得到null。
 */
@Suppress("NOTHING_TO_INLINE")
public inline fun Sequence<EventProvider>.firstKookBotManagerOrNull(): KookBotManager? =
    firstOrNull { it is KookBotManager } as KookBotManager?


/**
 * 从一个 [EventProvider] 列表中过滤寻找第一个的 [KookBotManager]。
 * 当找不到时得到null。
 */
@Suppress("NOTHING_TO_INLINE")
public inline fun Iterable<EventProvider>.firstKookBotManagerOrNull(): KookBotManager? =
    firstOrNull { it is KookBotManager } as KookBotManager?


/**
 * 通过 [OriginBotManager] 获取所有的 [KookBotManager]。
 *
 * @see OriginBotManager
 */
@FragileSimbotApi
@Suppress("NOTHING_TO_INLINE")
public inline fun kookBotManagers(): List<KookBotManager> = OriginBotManager.kookBotManagers

/**
 * 从 [OriginBotManager] 获取第一个 [KookBotManager]。
 *
 * @throws [NoSuchElementException] 如果找不到的话
 */
@FragileSimbotApi
@Suppress("NOTHING_TO_INLINE")
public inline fun firstKookBotManager(): KookBotManager =
    OriginBotManager.first { it is KookBotManager } as KookBotManager

/**
 * 从 [OriginBotManager] 获取第一个 [KookBotManager]。
 *
 * 如果找不到则返回null。
 */
@FragileSimbotApi
@Suppress("NOTHING_TO_INLINE")
public inline fun firstKookBotManagerOrNull(): KookBotManager? =
    OriginBotManager.firstOrNull { it is KookBotManager } as KookBotManager?


/**
 * 得到 [OriginBotManager] 中的所有 Kook 组件。
 *
 * @see OriginBotManager
 */
@FragileSimbotApi
public inline val OriginBotManager.kookBotManagers: List<KookBotManager> get() = filterIsKookBotManagers()


/**
 * 从 [Application.providers] 中寻找所有的 [KookBotManager].
 *
 */
public inline val Application.kookBotManagers: List<KookBotManager> get() = this.providers.filterIsKookBotManagers()
// endregion

