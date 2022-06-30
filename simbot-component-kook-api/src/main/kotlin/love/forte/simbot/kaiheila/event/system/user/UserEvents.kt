/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kaiheila.event.system.user

import love.forte.simbot.kaiheila.event.EventParser
import love.forte.simbot.kaiheila.event.SysEventParser
import love.forte.simbot.kaiheila.event.system.SystemEventParserDefinition
import love.forte.simbot.kaiheila.event.system.registerParsers
import love.forte.simbot.kaiheila.event.system.sysParser


/**
 * [用户相关事件](https://developer.kaiheila.cn/doc/event/user) 的子事件type常量类。
 *
 * @see SelfExitedGuildEvent
 * @see SelfJoinedGuildEvent
 * @see UserExitedChannelEvent
 * @see UserJoinedChannelEvent
 * @see UserUpdatedEvent
 * @see MessageBtnClickEvent
 *
 */
public object UserEvents {
    /**
     * @see SelfExitedGuildEventBody
     */
    public const val SELF_EXITED_GUILD: String = "self_exited_guild"


    /**
     * @see SelfExitedGuildEventBody
     */
    public val selfExitedGuildEventParser: SysEventParser<SelfExitedGuildEventBody> =
        sysParser(SELF_EXITED_GUILD, SelfExitedGuildEventBodyImpl.serializer())

    /**
     * @see SelfJoinedGuildEventBody
     */
    public const val SELF_JOINED_GUILD: String = "self_joined_guild"

    /**
     * @see SelfJoinedGuildEventBody
     */
    public val selfJoinedGuildEventParser: SysEventParser<SelfJoinedGuildEventBody> =
        sysParser(SELF_JOINED_GUILD, SelfJoinedGuildEventBodyImpl.serializer())


    /**
     * @see UserExitedChannelEventBody
     */
    public const val EXITED_CHANNEL: String = "exited_channel"

    /**
     * @see UserExitedChannelEventBody
     */
    public val userExitedChannelEventParser: SysEventParser<UserExitedChannelEventBody> =
        sysParser(EXITED_CHANNEL, UserExitedChannelEventBodyImpl.serializer())

    /**
     * @see UserJoinedChannelEventBody
     */
    public const val JOINED_CHANNEL: String = "joined_channel"

    /**
     * @see SelfJoinedGuildEventBody
     */
    public val userJoinedChannelEventParser: SysEventParser<UserJoinedChannelEventBody> =
        sysParser(JOINED_CHANNEL, UserJoinedChannelEventBodyImpl.serializer())

    /**
     * @see UserUpdatedEventBody
     */
    public const val USER_UPDATED: String = "user_updated"

    /**
     * @see UserUpdatedEventBody
     */
    public val userUpdatedEventParser: SysEventParser<UserUpdatedEventBody> =
        sysParser(USER_UPDATED, UserUpdatedEventBodyImpl.serializer())

    /**
     * @see MessageBtnClickEventBody
     */
    public const val MESSAGE_BTN_CLICK: String = "message_btn_click"


    /**
     * @see MessageBtnClickEventBody
     */
    public val messageBtnClickEventParser: SysEventParser<MessageBtnClickEventBody> =
        sysParser(SELF_JOINED_GUILD, MessageBtnClickEventBodyImpl.serializer())

}


internal fun MutableMap<Any, EventParser<*, *>>.userEventParsers() {
    registerParsers(
        UserEvents.SELF_EXITED_GUILD,
        UserEvents.selfExitedGuildEventParser
    )

    registerParsers(
        UserEvents.SELF_JOINED_GUILD,
        UserEvents.selfJoinedGuildEventParser
    )


    registerParsers(
        UserEvents.EXITED_CHANNEL,
        UserEvents.userExitedChannelEventParser
    )

    registerParsers(
        UserEvents.JOINED_CHANNEL,
        UserEvents.userJoinedChannelEventParser
    )


    registerParsers(
        UserEvents.USER_UPDATED,
        UserEvents.userUpdatedEventParser
    )

    registerParsers(
        UserEvents.MESSAGE_BTN_CLICK,
        UserEvents.messageBtnClickEventParser
    )
}


/**
 * 事件定义。
 * @see SelfExitedGuildEventBody
 */
public object SelfExitedGuildEvent : SystemEventParserDefinition<SelfExitedGuildEventBody>() {
    override val parser: SysEventParser<SelfExitedGuildEventBody>
        get() = UserEvents.selfExitedGuildEventParser
}


/**
 * 事件定义。
 * @see SelfJoinedGuildEventBody
 */
public object SelfJoinedGuildEvent : SystemEventParserDefinition<SelfJoinedGuildEventBody>() {
    override val parser: SysEventParser<SelfJoinedGuildEventBody>
        get() = UserEvents.selfJoinedGuildEventParser
}

/**
 * 事件定义。
 * @see UserExitedChannelEventBody
 */
public object UserExitedChannelEvent : SystemEventParserDefinition<UserExitedChannelEventBody>() {
    override val parser: SysEventParser<UserExitedChannelEventBody>
        get() = UserEvents.userExitedChannelEventParser
}

/**
 * 事件定义。
 * @see UserJoinedChannelEventBody
 */
public object UserJoinedChannelEvent : SystemEventParserDefinition<UserJoinedChannelEventBody>() {
    override val parser: SysEventParser<UserJoinedChannelEventBody>
        get() = UserEvents.userJoinedChannelEventParser
}

/**
 * 事件定义。
 * @see UserUpdatedEventBody
 */
public object UserUpdatedEvent : SystemEventParserDefinition<UserUpdatedEventBody>() {
    override val parser: SysEventParser<UserUpdatedEventBody>
        get() = UserEvents.userUpdatedEventParser
}

/**
 * 事件定义。
 * @see MessageBtnClickEventBody
 */
public object MessageBtnClickEvent : SystemEventParserDefinition<MessageBtnClickEventBody>() {
    override val parser: SysEventParser<MessageBtnClickEventBody>
        get() = UserEvents.messageBtnClickEventParser
}