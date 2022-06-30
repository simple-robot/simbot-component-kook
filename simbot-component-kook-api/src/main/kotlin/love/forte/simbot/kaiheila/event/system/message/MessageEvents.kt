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

package love.forte.simbot.kaiheila.event.system.message

import love.forte.simbot.kaiheila.event.EventParser
import love.forte.simbot.kaiheila.event.SysEventParser
import love.forte.simbot.kaiheila.event.system.SystemEventParserDefinition
import love.forte.simbot.kaiheila.event.system.registerParsers
import love.forte.simbot.kaiheila.event.system.sysParser


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
