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

package love.forte.simbot.component.kook

import love.forte.simbot.FragileSimbotApi
import love.forte.simbot.application.Application
import love.forte.simbot.application.ApplicationBuilder
import love.forte.simbot.application.BotRegistrar
import love.forte.simbot.application.EventProvider
import love.forte.simbot.bot.OriginBotManager


/**
 * 在 [ApplicationBuilder.bots] 作用域中寻找并使用 [KookBotManager].
 *
 * ```kotlin
 * simpleApplication {
 *     kookBots {
 *         val bot = register("client id", "token") {
 *             // config...
 *         }
 *         bot.start()
 * }
 * ```
 *
 * 如果当前环境中不存在任何 [KookBotManager], 则会抛出 [NoSuchElementException]。
 *
 * @throws NoSuchElementException 如果当前环境中不存在任何 [KookBotManager]
 *
 */
public inline fun ApplicationBuilder<*>.kookBots(
    crossinline block: suspend KookBotManager.(BotRegistrar) -> Unit,
) {
    bots {
        val manager = providers.firstKookBotManagerOrNull()
            ?: throw NoSuchElementException("No event provider of type [KookBotManager] in providers: $providers")
        
        manager.block(this)
    }
}

/**
 * 在 [ApplicationBuilder.bots] 作用域中寻找并使用 [KookBotManager].
 *
 * ```kotlin
 * simpleApplication {
 *    kookBots {
 *        val bot = register("client id", "token") {
 *            // config...
 *        }
 *        bot.start()
 *    }
 * }
 * ```
 *
 * 如果当前环境中不存在任何 [KookBotManager], 则不会执行 [block]。
 *
 */
public inline fun ApplicationBuilder<*>.kookBotsIfSupport(
    crossinline block: suspend KookBotManager.(BotRegistrar) -> Unit,
) {
    bots {
        val manager = providers.firstKookBotManagerOrNull() ?: return@bots
        manager.block(this)
    }
}

/**
 * 在 [ApplicationBuilder.bots] 作用域中寻找并使用 [KookBotManager].
 *
 * ```kotlin
 * simpleApplication {
 *     bots {
 *        Kook {
 *             val bot = register("client id", "token") {
 *                 // config...
 *             }
 *             bot.start()
 *         }
 *     }
 * }
 * ```
 *
 * 如果当前环境中不存在任何 [KookBotManager], 则会抛出 [NoSuchElementException]。
 *
 * @throws NoSuchElementException 如果当前环境中不存在任何 [KookBotManager]
 *
 */
public suspend inline fun BotRegistrar.kook(
    crossinline block: suspend KookBotManager.() -> Unit,
) {
    val manager = providers.firstKookBotManagerOrNull()
        ?: throw NoSuchElementException("No event provider of type [KookBotManager] in providers: $providers")
    manager.block()
}

/**
 * 在 [ApplicationBuilder.bots] 作用域中寻找并使用 [KookBotManager].
 *
 * ```kotlin
 * simpleApplication {
 *     bots {
 *        Kook {
 *             val bot = register("client id", "token") {
 *                 // config...
 *             }
 *             bot.start()
 *         }
 *     }
 * }
 * ```
 *
 * 如果当前环境中不存在任何 [KookBotManager], 则不会执行 [block]。
 *
 */
public suspend inline fun BotRegistrar.kookIfSupport(
    crossinline block: suspend KookBotManager.() -> Unit,
) {
    val manager = providers.firstKookBotManagerOrNull() ?: return
    manager.block()
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

