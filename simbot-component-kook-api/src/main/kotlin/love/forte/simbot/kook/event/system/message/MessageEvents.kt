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

package love.forte.simbot.kook.event.system.message

import love.forte.simbot.kook.event.EventParser
import love.forte.simbot.kook.event.SysEventParser
import love.forte.simbot.kook.event.system.SystemEventParserDefinition
import love.forte.simbot.kook.event.system.registerParsers
import love.forte.simbot.kook.event.system.sysParser


/**
 *
 * 消息相关系统事件
 *
 * @author ForteScarlet
 */
public interface MessageEventExtraBody

/**
 * 私聊消息相关
 *
 */
public interface PrivateMessageEventExtraBody : MessageEventExtraBody


/**
 * 与消息事件相关的 sub type 常量。
 *
 * @see DeletedMessageEvent
 * @see UpdatedMessageEvent
 * @see PrivateAddedReactionEvent
 * @see PrivateDeletedReactionEvent
 * @see PrivateUpdatedMessageEvent
 *
 */
public object MessageEvents {

    //region message events
    /**
     * @see DeletedMessageEventBody
     */
    public const val DELETED_MESSAGE: String = "deleted_message"

    /**
     * @see UpdatedMessageEventBody
     */
    public const val UPDATED_MESSAGE: String = "updated_message"

    /**
     * @see DeletedMessageEventBody
     */
    public val deletedMessageEventParser: SysEventParser<DeletedMessageEventBody> =
        sysParser(DELETED_MESSAGE, DeletedMessageEventBodyImpl.serializer())

    /**
     * @see UpdatedMessageEventBody
     */
    public val updatedMessageEventParser: SysEventParser<UpdatedMessageEventBody> =
        sysParser(UPDATED_MESSAGE, UpdatedMessageEventBodyImpl.serializer())

    //endregion


    //region private message events
    /**
     * @see PrivateAddedReactionEventBody
     */
    public const val PRIVATE_ADDED_REACTION: String = "private_added_reaction"

    /**
     * @see PrivateDeletedReactionEventBody
     */
    public const val PRIVATE_DELETED_REACTION: String = "private_deleted_reaction"

    /**
     * @see PrivateUpdatedMessageEventBody
     */
    public const val UPDATED_PRIVATE_MESSAGE: String = "updated_private_message"


    /**
     * @see PrivateAddedReactionEventBody
     */
    public val privateAddedReactionEventParser: SysEventParser<PrivateAddedReactionEventBody> =
        sysParser(PRIVATE_ADDED_REACTION, PrivateAddedReactionEventBodyImpl.serializer())


    /**
     * @see PrivateDeletedReactionEventBody
     */
    public val privateDeletedReactionEventParser: SysEventParser<PrivateDeletedReactionEventBody> =
        sysParser(PRIVATE_DELETED_REACTION, PrivateDeletedReactionEventBodyImpl.serializer())


    /**
     * @see PrivateUpdatedMessageEventBody
     */
    public val privateUpdatedMessageEventParser: SysEventParser<PrivateUpdatedMessageEventBody> =
        sysParser(UPDATED_PRIVATE_MESSAGE, PrivateUpdatedMessageEventBodyImpl.serializer())


    //endregion


}


internal fun MutableMap<Any, EventParser<*, *>>.messageEventParsers() {
    registerParsers(
        MessageEvents.DELETED_MESSAGE,
        MessageEvents.deletedMessageEventParser
    )

    registerParsers(
        MessageEvents.UPDATED_MESSAGE,
        MessageEvents.updatedMessageEventParser
    )

    registerParsers(
        MessageEvents.PRIVATE_ADDED_REACTION,
        MessageEvents.privateAddedReactionEventParser
    )

    registerParsers(
        MessageEvents.PRIVATE_DELETED_REACTION,
        MessageEvents.privateDeletedReactionEventParser
    )

    registerParsers(
        MessageEvents.UPDATED_PRIVATE_MESSAGE,
        MessageEvents.privateUpdatedMessageEventParser
    )


}


/**
 * 事件定义。
 * @see DeletedMessageEventBody
 */
public object DeletedMessageEvent : SystemEventParserDefinition<DeletedMessageEventBody>() {
    override val parser: SysEventParser<DeletedMessageEventBody>
        get() = MessageEvents.deletedMessageEventParser
}



/**
 * 事件定义。
 * @see UpdatedMessageEventBody
 */
public object UpdatedMessageEvent : SystemEventParserDefinition<UpdatedMessageEventBody>() {
    override val parser: SysEventParser<UpdatedMessageEventBody>
        get() = MessageEvents.updatedMessageEventParser
}

/**
 * 事件定义。
 * @see PrivateAddedReactionEventBody
 */
public object PrivateAddedReactionEvent : SystemEventParserDefinition<PrivateAddedReactionEventBody>() {
    override val parser: SysEventParser<PrivateAddedReactionEventBody>
        get() = MessageEvents.privateAddedReactionEventParser
}

/**
 * 事件定义。
 * @see PrivateDeletedReactionEventBody
 */
public object PrivateDeletedReactionEvent : SystemEventParserDefinition<PrivateDeletedReactionEventBody>() {
    override val parser: SysEventParser<PrivateDeletedReactionEventBody>
        get() = MessageEvents.privateDeletedReactionEventParser
}

/**
 * 事件定义。
 * @see PrivateUpdatedMessageEventBody
 */
public object PrivateUpdatedMessageEvent : SystemEventParserDefinition<PrivateUpdatedMessageEventBody>() {
    override val parser: SysEventParser<PrivateUpdatedMessageEventBody>
        get() = MessageEvents.privateUpdatedMessageEventParser
}
