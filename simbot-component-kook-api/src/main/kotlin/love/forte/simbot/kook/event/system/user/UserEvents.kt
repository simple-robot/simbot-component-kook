/*
 * Copyright (c) 2021-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.event.system.user

import love.forte.simbot.kook.event.EventParser
import love.forte.simbot.kook.event.SysEventParser
import love.forte.simbot.kook.event.system.SystemEventParserDefinition
import love.forte.simbot.kook.event.system.registerParsers
import love.forte.simbot.kook.event.system.sysParser


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
