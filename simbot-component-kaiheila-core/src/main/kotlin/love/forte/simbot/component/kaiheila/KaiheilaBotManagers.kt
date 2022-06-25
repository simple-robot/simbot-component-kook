package love.forte.simbot.component.kaiheila

import love.forte.simbot.FragileSimbotApi
import love.forte.simbot.OriginBotManager
import love.forte.simbot.application.Application
import love.forte.simbot.application.ApplicationBuilder
import love.forte.simbot.application.BotRegistrar
import love.forte.simbot.application.EventProvider


/**
 * 在 [ApplicationBuilder.bots] 作用域中寻找并使用 [KaiheilaBotManager].
 *
 * ```kotlin
 * simpleApplication {
 *     kaiheilaBots {
 *         val bot = register("client id", "token") {
 *             // config...
 *         }
 *         bot.start()
 * }
 * ```
 *
 * 如果当前环境中不存在任何 [KaiheilaBotManager], 则会抛出 [NoSuchElementException]。
 *
 * @throws NoSuchElementException 如果当前环境中不存在任何 [KaiheilaBotManager]
 *
 */
public inline fun ApplicationBuilder<*>.kaiheilaBots(
    crossinline block: suspend KaiheilaBotManager.(BotRegistrar) -> Unit,
) {
    bots {
        val manager = providers.firstKaiheilaBotManagerOrNull()
            ?: throw NoSuchElementException("No event provider of type [KaiheilaBotManager] in providers: $providers")
        
        manager.block(this)
    }
}

/**
 * 在 [ApplicationBuilder.bots] 作用域中寻找并使用 [KaiheilaBotManager].
 *
 * ```kotlin
 * simpleApplication {
 *    kaiheilaBots {
 *        val bot = register("client id", "token") {
 *            // config...
 *        }
 *        bot.start()
 *    }
 * }
 * ```
 *
 * 如果当前环境中不存在任何 [KaiheilaBotManager], 则不会执行 [block]。
 *
 */
public inline fun ApplicationBuilder<*>.kaiheilaBotsIfSupport(
    crossinline block: suspend KaiheilaBotManager.(BotRegistrar) -> Unit,
) {
    bots {
        val manager = providers.firstKaiheilaBotManagerOrNull() ?: return@bots
        manager.block(this)
    }
}

/**
 * 在 [ApplicationBuilder.bots] 作用域中寻找并使用 [KaiheilaBotManager].
 *
 * ```kotlin
 * simpleApplication {
 *     bots {
 *         kaiheila {
 *             val bot = register("client id", "token") {
 *                 // config...
 *             }
 *             bot.start()
 *         }
 *     }
 * }
 * ```
 *
 * 如果当前环境中不存在任何 [KaiheilaBotManager], 则会抛出 [NoSuchElementException]。
 *
 * @throws NoSuchElementException 如果当前环境中不存在任何 [KaiheilaBotManager]
 *
 */
public suspend inline fun BotRegistrar.kaiheila(
    crossinline block: suspend KaiheilaBotManager.() -> Unit,
) {
    val manager = providers.firstKaiheilaBotManagerOrNull()
        ?: throw NoSuchElementException("No event provider of type [KaiheilaBotManager] in providers: $providers")
    manager.block()
}

/**
 * 在 [ApplicationBuilder.bots] 作用域中寻找并使用 [KaiheilaBotManager].
 *
 * ```kotlin
 * simpleApplication {
 *     bots {
 *         kaiheila {
 *             val bot = register("client id", "token") {
 *                 // config...
 *             }
 *             bot.start()
 *         }
 *     }
 * }
 * ```
 *
 * 如果当前环境中不存在任何 [KaiheilaBotManager], 则不会执行 [block]。
 *
 */
public suspend inline fun BotRegistrar.kaiheilaIfSupport(
    crossinline block: suspend KaiheilaBotManager.() -> Unit,
) {
    val manager = providers.firstKaiheilaBotManagerOrNull() ?: return
    manager.block()
}


// region bot manager 获取扩展
/**
 * 从一个 [EventProvider] 序列中过滤寻找所有的 [KaiheilaBotManager]。
 */
@Suppress("NOTHING_TO_INLINE")
public inline fun Sequence<EventProvider>.filterIsKaiheilaBotManagers(): Sequence<KaiheilaBotManager> =
    filterIsInstance<KaiheilaBotManager>()


/**
 * 从一个 [EventProvider] 列表中过滤寻找所有的 [KaiheilaBotManager]。
 */
@Suppress("NOTHING_TO_INLINE")
public inline fun Iterable<EventProvider>.filterIsKaiheilaBotManagers(): List<KaiheilaBotManager> =
    filterIsInstance<KaiheilaBotManager>()


/**
 * 从一个 [EventProvider] 序列中寻找第一个的 [KaiheilaBotManager]。
 *
 * @throws NoSuchElementException 当找不到时
 */
@Suppress("NOTHING_TO_INLINE")
public inline fun Sequence<EventProvider>.firstKaiheilaBotManager(): KaiheilaBotManager =
    first { it is KaiheilaBotManager } as KaiheilaBotManager


/**
 * 从一个 [EventProvider] 列表中过滤寻找第一个的 [KaiheilaBotManager]。
 *
 * @throws NoSuchElementException 当找不到时
 */
@Suppress("NOTHING_TO_INLINE")
public inline fun Iterable<EventProvider>.firstKaiheilaBotManager(): KaiheilaBotManager =
    first { it is KaiheilaBotManager } as KaiheilaBotManager


/**
 * 从一个 [EventProvider] 序列中寻找第一个的 [KaiheilaBotManager]。
 * 当找不到时得到null。
 */
@Suppress("NOTHING_TO_INLINE")
public inline fun Sequence<EventProvider>.firstKaiheilaBotManagerOrNull(): KaiheilaBotManager? =
    firstOrNull { it is KaiheilaBotManager } as KaiheilaBotManager?


/**
 * 从一个 [EventProvider] 列表中过滤寻找第一个的 [KaiheilaBotManager]。
 * 当找不到时得到null。
 */
@Suppress("NOTHING_TO_INLINE")
public inline fun Iterable<EventProvider>.firstKaiheilaBotManagerOrNull(): KaiheilaBotManager? =
    firstOrNull { it is KaiheilaBotManager } as KaiheilaBotManager?


/**
 * 通过 [OriginBotManager] 获取所有的 [KaiheilaBotManager]。
 *
 * @see OriginBotManager
 */
@FragileSimbotApi
@Suppress("NOTHING_TO_INLINE")
public inline fun kaiheilaBotManagers(): List<KaiheilaBotManager> = OriginBotManager.kaiheilaBotManagers

/**
 * 从 [OriginBotManager] 获取第一个 [KaiheilaBotManager]。
 *
 * @throws [NoSuchElementException] 如果找不到的话
 */
@FragileSimbotApi
@Suppress("NOTHING_TO_INLINE")
public inline fun firstKaiheilaBotManager(): KaiheilaBotManager =
    OriginBotManager.first { it is KaiheilaBotManager } as KaiheilaBotManager

/**
 * 从 [OriginBotManager] 获取第一个 [KaiheilaBotManager]。
 *
 * 如果找不到则返回null。
 */
@FragileSimbotApi
@Suppress("NOTHING_TO_INLINE")
public inline fun firstKaiheilaBotManagerOrNull(): KaiheilaBotManager? =
    OriginBotManager.firstOrNull { it is KaiheilaBotManager } as KaiheilaBotManager?


/**
 * 得到 [OriginBotManager] 中的所有开黑啦组件。
 *
 * @see OriginBotManager
 */
@FragileSimbotApi
public inline val OriginBotManager.kaiheilaBotManagers: List<KaiheilaBotManager> get() = filterIsKaiheilaBotManagers()


/**
 * 从 [Application.providers] 中寻找所有的 [KaiheilaBotManager].
 *
 */
public inline val Application.kaiheilaBotManagers: List<KaiheilaBotManager> get() = this.providers.filterIsKaiheilaBotManagers()
// endregion

