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

package love.forte.simbot.kaiheila.objects

import love.forte.simbot.kaiheila.objects.CardModule.ActionGroup


@DslMarker
@Retention(AnnotationRetention.BINARY)
public annotation class CardMsgBuildDsl


/**
 * 通过 [CardMessageBuilder] 以 DSL 的方式构建一个 [CardMessage].
 */
@CardMsgBuildDsl
public fun buildCardMessage(action: CardMessageBuilder.() -> Unit): CardMessage {
    return CardMessageBuilder().also(action).build()
}


/**
 * 用于构建 [CardMessage] 的构建器。
 */
@CardMsgBuildDsl
public class CardMessageBuilder @JvmOverloads constructor(private val collect: MutableCollection<Card> = mutableListOf()) {

    private fun addCard0(card: Card): CardMessageBuilder = also {
        collect.add(card)
    }

    /**
     * DSL构建一个 [Card].
     */
    @CardBuildDsl
    public fun card(action: CardBuilder.() -> Unit): CardMessageBuilder {
        val card = CardBuilder().also(action).build()
        return addCard0(card)
    }

    /**
     * 构建一个 [Card] 并添加到当前builder。
     */
    @JvmOverloads
    public fun card(
        theme: Theme = Theme.Default,
        color: String? = null,
        size: Size = Size.Default,
        modules: List<CardModule>
    ): CardMessageBuilder {
        val card = Card(theme, color, size, modules)
        return addCard0(card)
    }


    /**
     * 直接添加一个 [Card]。
     */
    @JvmName("card")
    public operator fun Card.unaryPlus(): CardMessageBuilder {
        return addCard0(this)
    }

    /**
     * 得到最终的 [CardMessage] 结果。
     */
    public fun build(): CardMessage = CardMessage(collect.toList())
}


@DslMarker
@Retention(AnnotationRetention.BINARY)
public annotation class CardBuildDsl


/**
 * 针对 [Card] 的构建器。
 */
@CardBuildDsl
public class CardBuilder @JvmOverloads constructor(private val collect: MutableCollection<CardModule> = mutableListOf()) {
    /**
     * 卡片风格.
     *
     * @see Card.theme
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public var theme: Theme = Theme.PRIMARY

    /**
     * 色值
     *
     * @see Card.color
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public var color: String? = null

    /**
     * 大小。
     *
     * @see Card.size
     */
    public var size: Size = Size.Default


    private fun addModule0(module: CardModule): CardBuilder = also {
        collect.add(module)
    }

    /**
     * 直接增加一个 [CardModule] 实例。
     */
    @JvmName("addModule")
    public operator fun CardModule.unaryPlus(): CardBuilder {
        return addModule0(this)
    }

    /**
     * 构建多个modules.
     */
    @CardModuleBuildDsl
    public fun modules(action: CardModulesBuilder.() -> Unit): CardBuilder {
        // only action, no build.
        CardModulesBuilder(collect).action()
        return this
    }


    /**
     * 得到最终的 [Card] 结果。
     */
    public fun build(): Card = Card(theme, color, size, collect.toList())


    /**
     * 清除内部已经收集的所有 [CardModule].
     */
    public fun clear() {
        collect.clear()
    }
}


@DslMarker
@Retention(AnnotationRetention.BINARY)
public annotation class CardModuleBuildDsl

/**
 * 针对 [CardModule] 的集合的构建器。
 */
@CardModuleBuildDsl
public class CardModulesBuilder @JvmOverloads constructor(private val collect: MutableCollection<CardModule> = mutableListOf()) {
    private fun add(module: CardModule): CardModulesBuilder = also {
        collect.add(module)
    }

    /**
     * 直接增加一个 card module 实例。
     */
    @JvmName("module")
    public operator fun CardModule.unaryPlus(): CardModulesBuilder {
        return add(this)
    }


    //region headers

    /**
     * 增加一个 [CardModule.Header]。
     */
    public fun header(text: String): CardModulesBuilder {
        return add(CardModule.Header(text))
    }
    //endregion

    //region section
    /**
     * 添加一个 [CardModule.Section].
     */
    @JvmOverloads
    public fun section(
        mode: CardModule.SectionMode = CardModule.SectionMode.LEFT,
        text: CardElement,
        accessory: CardElement? = null,
    ): CardModulesBuilder {
        return add(CardModule.Section(mode, text, accessory))
    }


    //endregion

    //region image-group

    /**
     * 添加一个 [CardModule.ImageGroup].
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public fun imageGroup(elements: List<CardElement.Image>): CardModulesBuilder {
        return add(CardModule.ImageGroup(elements))
    }

    /**
     * 添加一个 [CardModule.ImageGroup].
     */
    public fun imageGroup(vararg elements: CardElement.Image): CardModulesBuilder {
        return imageGroup(elements.asList())
    }
    //endregion

    //region container
    /**
     * 添加一个 [CardModule.Container]
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public fun container(elements: List<CardElement.Image>): CardModulesBuilder {
        return add(CardModule.Container(elements))
    }

    /**
     * 添加一个 [CardModule.Container]
     */
    public fun container(vararg elements: CardElement.Image): CardModulesBuilder {
        return container(elements.asList())
    }
    //endregion

    //region action-group
    /**
     * 添加一个[ActionGroup].
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public fun actionGroup(elements: List<CardElement.Button>): CardModulesBuilder {
        return add(ActionGroup(elements))
    }


    /**
     * 添加一个[ActionGroup].
     */
    public fun actionGroup(vararg elements: CardElement.Button): CardModulesBuilder {
        return actionGroup(elements.asList())
    }
    //endregion

    //region context
    /**
     * 添加一个 [CardModule.Context]
     */

    @Suppress("MemberVisibilityCanBePrivate")
    public fun context(elements: List<CardElement>): CardModulesBuilder {
        return add(CardModule.Context(elements))
    }

    /**
     * 添加一个 [CardModule.Context]
     */
    public fun context(vararg elements: CardElement): CardModulesBuilder {
        return context(elements.asList())
    }
    //endregion

    //region divider
    /**
     * 添加一个 [CardModule.Divider].
     */
    public fun divider(): CardModulesBuilder = add(CardModule.Divider)
    //endregion

    //region files(file,audio,video)
    /**
     * 添加一个 [CardModule.Files.File]
     */
    public fun file(src: String, title: String, cover: String): CardModulesBuilder {
        return add(CardModule.Files.file(src, title, cover))
    }


    /**
     * 添加一个 [CardModule.Files.Audio]
     */
    public fun audio(src: String, title: String, cover: String): CardModulesBuilder {
        return add(CardModule.Files.audio(src, title, cover))
    }


    /**
     * 添加一个 [CardModule.Files.Video]
     */
    public fun video(src: String, title: String, cover: String): CardModulesBuilder {
        return add(CardModule.Files.video(src, title, cover))
    }


    //endregion

    //region countdown
    /**
     * 添加一个 [CardModule.Countdown].
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public fun countdown(mode: CardModule.CountdownMode, startTime: Long?, endTime: Long): CardModulesBuilder {
        return add(CardModule.Countdown(mode, startTime, endTime))
    }

    /**
     * 添加一个 [CardModule.Countdown].
     *
     * @param startTime 起始时间的毫秒时间戳。
     * @param endTime 到期时间的毫秒时间戳。
     */
    public fun countdownSecond(startTime: Long, endTime: Long): CardModulesBuilder {
        return countdown(CardModule.CountdownMode.SECOND, startTime, endTime)
    }

    /**
     * 添加一个 [CardModule.Countdown].
     *
     * @param endTime 到期时间的毫秒时间戳。
     */
    public fun countdownHour(endTime: Long): CardModulesBuilder {
        return countdown(CardModule.CountdownMode.HOUR, startTime = null, endTime = endTime)
    }

    /**
     * 添加一个 [CardModule.Countdown].
     *
     * @param endTime 到期时间的毫秒时间戳。
     */
    public fun countdownDay(endTime: Long): CardModulesBuilder {
        return countdown(CardModule.CountdownMode.DAY, startTime = null, endTime = endTime)
    }
    //endregion

    //region invite
    /**
     * 添加一个 [CardModule.Invite].
     */
    public fun invite(code: String): CardModulesBuilder {
        return add(CardModule.Invite(code))
    }
    //endregion

    //region collect action
    /**
     * 得到当前收集到的所有 [CardModule].
     */
    public fun build(): List<CardModule> {
        return collect.toList()
    }


    /**
     * 得到当前收集到的所有 [CardModule].
     */
    public fun clear() {
        collect.clear()
    }
    //endregion
}

