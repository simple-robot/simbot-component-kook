/*
 * Copyright (c) 2023. ForteScarlet.
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

import love.forte.simbot.ability.CompletionPerceivable
import love.forte.simbot.application.Application
import love.forte.simbot.application.ApplicationBuilder
import love.forte.simbot.application.ApplicationBuilderDsl
import love.forte.simbot.component.kook.bot.KookBotManager
import love.forte.simbot.component.kook.bot.KookBotManagerConfiguration


/**
 * 注册 [KOOK组件][KookComponent] 信息。
 *
 * usage:
 * ```kotlin
 * simbotApplication(Foo) {
 *  useKookComponent()
 *  // 或
 *  useKookComponent { ... }
 * }
 * ```
 *
 *
 * 相当于：
 * ```kotlin
 * simbotApplication(Foo) {
 *    install(KookComponent) { ... }
 * }
 * ```
 *
 * @see KookComponent
 */
@ApplicationBuilderDsl
public fun <A : Application> ApplicationBuilder<A>.useKookComponent(configurator: KookComponentConfiguration.(perceivable: CompletionPerceivable<A>) -> Unit = {}) {
    install(KookComponent, configurator)
}


/**
 * 注册使用 [KookBotManager]。
 *
 * usage:
 * ```kotlin
 * simbotApplication(Foo) {
 *    useKookBotManager()
 *    // 或
 *    useKookBotManager { ... }
 * }
 * ```
 *
 * 相当于:
 * ```kotlin
 * simbotApplication(Foo) {
 *    install(KookBotManager) { ... }
 * }
 * ```
 *
 *
 *
 * @see KookBotManager
 */
@ApplicationBuilderDsl
public fun <A : Application> ApplicationBuilder<A>.useKookBotManager(configurator: KookBotManagerConfiguration.(perceivable: CompletionPerceivable<A>) -> Unit = {}) {
    install(KookBotManager, configurator)
}

/**
 * 通过 [KookComponentUsageBuilder] 来同时配置使用
 * [KOOK组件][KookComponent] 和 [KookBotManager]。
 *
 * usage:
 * ```kotlin
 * simbotApplication(Foo) {
 *    useKook()
 *    // 或:
 *    useKook {
 *        // 配置组件
 *        component {
 *            // ...
 *        }
 *        // 配置botManager
 *        botManager {
 *            // ...
 *        }
 *    }
 * }
 * ```
 *
 *
 * 相当于:
 * ```kotlin
 * simbotApplication(Foo) {
 *    install(KookComponent) {
 *        // ...
 *    }
 *
 *    install(KookBotManager) {
 *        // ...
 *    }
 * }
 * ```
 */
@ApplicationBuilderDsl
public fun <A : Application> ApplicationBuilder<A>.useKook(usageBuilder: KookComponentUsageBuilder<A>.() -> Unit = {}) {
    KookComponentUsageBuilderImpl<A>().also(usageBuilder).use(this)
}


/**
 * 为 [KookComponentUsageBuilder] 中的函数提供DSL染色。
 *
 */
@DslMarker
internal annotation class KookComponentUsageBuilderDsl


/**
 * 同时配置 [KOOK组件][KookComponent] 和 [KookBotManager] 的构建器。
 *
 * 应用于 [useKook] 中。
 *
 * @see useKook
 *
 */
@KookComponentUsageBuilderDsl
public interface KookComponentUsageBuilder<A : Application> {

    /**
     * 提供针对 [KOOK组件][KookComponent] 的配置。
     *
     */
    @KookComponentUsageBuilderDsl
    public fun component(configurator: KookComponentConfiguration.(perceivable: CompletionPerceivable<A>) -> Unit)


    /**
     * 提供针对 [KookBotManager] 的配置。
     */
    @KookComponentUsageBuilderDsl
    public fun botManager(configurator: KookBotManagerConfiguration.(perceivable: CompletionPerceivable<A>) -> Unit)

}


private class KookComponentUsageBuilderImpl<A : Application> : KookComponentUsageBuilder<A> {
    private var compConf: (KookComponentConfiguration.(perceivable: CompletionPerceivable<A>) -> Unit) = {}
    private var bmConf: (KookBotManagerConfiguration.(perceivable: CompletionPerceivable<A>) -> Unit) = {}

    override fun component(configurator: KookComponentConfiguration.(perceivable: CompletionPerceivable<A>) -> Unit) {
        compConf.also { old ->
            compConf = {
                old(it)
                configurator(it)
            }
        }
    }


    override fun botManager(configurator: KookBotManagerConfiguration.(perceivable: CompletionPerceivable<A>) -> Unit) {
        bmConf.also { old ->
            bmConf = {
                old(it)
                configurator(it)
            }
        }
    }

    fun use(b: ApplicationBuilder<A>) {
        b.useKookComponent(compConf)
        b.useKookBotManager(bmConf)
    }
}



