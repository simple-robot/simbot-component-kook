package love.forte.simbot.component.kaiheila

import love.forte.simbot.ability.CompletionPerceivable
import love.forte.simbot.application.Application
import love.forte.simbot.application.ApplicationBuilder
import love.forte.simbot.application.ApplicationBuilderDsl


/**
 * 注册 [ Kook 组件][KaiheilaComponent] 信息。
 *
 * usage:
 * ```kotlin
 * simbotApplication(Foo) {
 *  useKaiheilaComponent()
 *  // 或
 *  useKaiheilaComponent { ... }
 * }
 * ```
 *
 *
 * 相当于：
 * ```kotlin
 * simbotApplication(Foo) {
 *    install(KaiheilaComponent) { ... }
 * }
 * ```
 *
 * @see KaiheilaComponent
 */
@ApplicationBuilderDsl
public fun <A : Application> ApplicationBuilder<A>.useKaiheilaComponent(configurator: KaiheilaComponentConfiguration.(perceivable: CompletionPerceivable<A>) -> Unit = {}) {
    install(KaiheilaComponent, configurator)
}


/**
 * 注册使用 [ Kook botManager][KaiheilaBotManager]。
 *
 * usage:
 * ```kotlin
 * simbotApplication(Foo) {
 *    useKaiheilaBotManager()
 *    // 或
 *    useKaiheilaBotManager { ... }
 * }
 * ```
 *
 * 相当于:
 * ```kotlin
 * simbotApplication(Foo) {
 *    install(KaiheilaBotManager) { ... }
 * }
 * ```
 *
 *
 *
 * @see KaiheilaBotManager
 */
@ApplicationBuilderDsl
public fun <A : Application> ApplicationBuilder<A>.useKaiheilaBotManager(configurator: KaiheilaBotManagerConfiguration.(perceivable: CompletionPerceivable<A>) -> Unit = {}) {
    install(KaiheilaBotManager, configurator)
}


/**
 * 通过 [KaiheilaComponentUsageBuilder] 来同时配置使用
 * [ Kook 组件][KaiheilaComponent] 和
 * [ Kook botManager][KaiheilaBotManager]。
 *
 * usage:
 * ```kotlin
 * simbotApplication(Foo) {
 *    useKaiheila()
 *    // 或
 *    useKaiheila {
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
 *    install(KaiheilaComponent) {
 *        // ...
 *    }
 *
 *    install(KaiheilaBotManager) {
 *        // ...
 *    }
 * }
 * ```
 *
 *
 *
 *
 */
@ApplicationBuilderDsl
public fun <A : Application> ApplicationBuilder<A>.useKaiheila(usageBuilder: KaiheilaComponentUsageBuilder<A>.() -> Unit = {}) {
    KaiheilaComponentUsageBuilderImpl<A>().also(usageBuilder).use(this)
}


/**
 * 为 [KaiheilaComponentUsageBuilder] 中的函数提供DSL染色。
 *
 */
@DslMarker
internal annotation class KaiheilaComponentUsageBuilderDsl


/**
 * 同时配置 [ Kook 组件][KaiheilaComponent] 和 [ Kook botManager][KaiheilaBotManager]
 * 的构建器。
 *
 * 应用于 [useKaiheila] 中。
 *
 * @see useKaiheila
 *
 */
@KaiheilaComponentUsageBuilderDsl
public interface KaiheilaComponentUsageBuilder<A : Application> {
    
    /**
     * 提供针对 [ Kook 组件][KaiheilaComponent] 的配置。
     *
     */
    @KaiheilaComponentUsageBuilderDsl
    public fun component(configurator: KaiheilaComponentConfiguration.(perceivable: CompletionPerceivable<A>) -> Unit)
    
    
    /**
     * 提供针对 [ Kook BotManager][KaiheilaBotManager] 的配置。
     */
    @KaiheilaComponentUsageBuilderDsl
    public fun botManager(configurator: KaiheilaBotManagerConfiguration.(perceivable: CompletionPerceivable<A>) -> Unit)
    
}


private class KaiheilaComponentUsageBuilderImpl<A : Application> : KaiheilaComponentUsageBuilder<A> {
    private var compConf: (KaiheilaComponentConfiguration.(perceivable: CompletionPerceivable<A>) -> Unit) = {}
    private var bmConf: (KaiheilaBotManagerConfiguration.(perceivable: CompletionPerceivable<A>) -> Unit) = {}
    
    override fun component(configurator: KaiheilaComponentConfiguration.(perceivable: CompletionPerceivable<A>) -> Unit) {
        compConf.also { old ->
            compConf = {
                old(it)
                configurator(it)
            }
        }
    }
    
    
    override fun botManager(configurator: KaiheilaBotManagerConfiguration.(perceivable: CompletionPerceivable<A>) -> Unit) {
        bmConf.also { old ->
            bmConf = {
                old(it)
                configurator(it)
            }
        }
    }
    
    fun use(b: ApplicationBuilder<A>) {
        b.useKaiheilaComponent(compConf)
        b.useKaiheilaBotManager(bmConf)
    }
}