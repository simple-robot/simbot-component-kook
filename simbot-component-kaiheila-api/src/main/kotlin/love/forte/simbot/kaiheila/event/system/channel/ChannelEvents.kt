/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kaiheila 的一部分。
 *
 *  simbot-component-kaiheila 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kaiheila 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

@file:JvmName("GuildEvents")

package love.forte.simbot.kaiheila.event.system.channel

import love.forte.simbot.kaiheila.event.Event
import love.forte.simbot.kaiheila.event.EventParser
import love.forte.simbot.kaiheila.event.SysEventParser
import love.forte.simbot.kaiheila.event.system.SystemEventParserDefinition
import love.forte.simbot.kaiheila.event.system.registerParsers
import love.forte.simbot.kaiheila.event.system.sysParser


/**
 *
 * [频道相关事件列表](https://developer.kaiheila.cn/doc/event/channel)
 *
 * _Note: 由于官方尚未明确频道相关事件中相似的几个事件body之间的共同性，因此此接口暂不提供任何抽象定义。_
 *
 * @see Event.Extra.Sys.body
 */
public interface ChannelEventExtraBody


/**
 * 频道相关事件的部分信息。
 *
 *
 * @see AddedReactionEvent
 * @see DeletedReactionEvent
 * @see AddedChannelEvent
 * @see UpdatedChannelEvent
 * @see DeletedChannelEvent
 * @see PinnedMessageEvent
 * @see UnpinnedMessageEvent
 *
 */
public object ChannelEvents {

    /** 频道内用户添加 reaction */
    public const val ADDED_REACTION: String = "added_reaction"

    /** 频道内用户取消 reaction */
    public const val DELETED_REACTION: String = "deleted_reaction"

    /** 新增频道 */
    public const val ADDED_CHANNEL: String = "added_channel"

    /** 修改频道信息 */
    public const val UPDATED_CHANNEL: String = "updated_channel"

    /** 删除频道 */
    public const val DELETED_CHANNEL: String = "deleted_channel"

    /** 新的频道置顶消息 */
    public const val PINNED_MESSAGE: String = "pinned_message"

    /** 取消频道置顶消息 */
    public const val UNPINNED_MESSAGE: String = "unpinned_message"


    //// parsers

    /** 频道内用户添加 reaction */
    public val addedReactionParser: SysEventParser<AddedReactionExtraBody> =
        sysParser(ADDED_REACTION, AddedReactionExtraBodyImpl.serializer())

    /** 频道内用户取消 reaction */
    public val deletedReactionParser: SysEventParser<DeletedReactionExtraBody> =
        sysParser(DELETED_REACTION, DeletedReactionExtraBodyImpl.serializer())

    /** 新增频道 */
    public val addedChannelParser: SysEventParser<AddedChannelExtraBody> =
        sysParser(ADDED_CHANNEL, AddedChannelExtraBodyImpl.serializer())

    /** 修改频道信息 */
    public val updatedChannelParser: SysEventParser<UpdatedChannelExtraBody> =
        sysParser(UPDATED_CHANNEL, UpdatedChannelExtraBodyImpl.serializer())

    /** 删除频道 */
    public val deletedChannelParser: SysEventParser<DeletedChannelExtraBody> =
        sysParser(DELETED_CHANNEL, DeletedChannelExtraBodyImpl.serializer())

    /** 新的频道置顶消息 */
    public val pinnedMessageParser: SysEventParser<PinnedMessageExtraBody> =
        sysParser(PINNED_MESSAGE, PinnedMessageExtraBodyImpl.serializer())

    /** 取消频道置顶消息 */
    public val unpinnedMessageParser: SysEventParser<UnpinnedMessageExtraBody> =
        sysParser(UNPINNED_MESSAGE, UnpinnedMessageExtraBodyImpl.serializer())

}

internal fun MutableMap<Any, EventParser<*, *>>.channelEventParsers() {
    registerParsers(
        ChannelEvents.ADDED_REACTION,
        ChannelEvents.addedReactionParser
    )
    registerParsers(
        ChannelEvents.DELETED_REACTION,
        ChannelEvents.deletedReactionParser
    )
    registerParsers(
        ChannelEvents.ADDED_CHANNEL,
        ChannelEvents.addedChannelParser
    )
    registerParsers(
        ChannelEvents.UPDATED_CHANNEL,
        ChannelEvents.updatedChannelParser
    )
    registerParsers(
        ChannelEvents.DELETED_CHANNEL,
        ChannelEvents.deletedChannelParser
    )
    registerParsers(
        ChannelEvents.PINNED_MESSAGE,
        ChannelEvents.pinnedMessageParser
    )
    registerParsers(
        ChannelEvents.UNPINNED_MESSAGE,
        ChannelEvents.unpinnedMessageParser
    )
}


/**
 * 事件定义。
 * @see AddedReactionExtraBody
 */
public object AddedReactionEvent : SystemEventParserDefinition<AddedReactionExtraBody>() {
    override val parser: SysEventParser<AddedReactionExtraBody>
        get() = ChannelEvents.addedReactionParser
}


/**
 * 事件定义。
 * @see DeletedReactionExtraBody
 */
public object DeletedReactionEvent : SystemEventParserDefinition<DeletedReactionExtraBody>() {
    override val parser: SysEventParser<DeletedReactionExtraBody>
        get() = ChannelEvents.deletedReactionParser
}


/**
 * 事件定义。
 * @see AddedChannelExtraBody
 */
public object AddedChannelEvent : SystemEventParserDefinition<AddedChannelExtraBody>() {
    override val parser: SysEventParser<AddedChannelExtraBody>
        get() = ChannelEvents.addedChannelParser
}


/**
 * 事件定义。
 * @see UpdatedChannelExtraBody
 */
public object UpdatedChannelEvent : SystemEventParserDefinition<UpdatedChannelExtraBody>() {
    override val parser: SysEventParser<UpdatedChannelExtraBody>
        get() = ChannelEvents.updatedChannelParser
}


/**
 * 事件定义。
 * @see DeletedChannelExtraBody
 */
public object DeletedChannelEvent : SystemEventParserDefinition<DeletedChannelExtraBody>() {
    override val parser: SysEventParser<DeletedChannelExtraBody>
        get() = ChannelEvents.deletedChannelParser
}


/**
 * 事件定义。
 * @see PinnedMessageExtraBody
 */
public object PinnedMessageEvent : SystemEventParserDefinition<PinnedMessageExtraBody>() {
    override val parser: SysEventParser<PinnedMessageExtraBody>
        get() = ChannelEvents.pinnedMessageParser
}


/**
 * 事件定义。
 * @see UnpinnedMessageExtraBody
 */
public object UnpinnedMessageEvent : SystemEventParserDefinition<UnpinnedMessageExtraBody>() {
    override val parser: SysEventParser<UnpinnedMessageExtraBody>
        get() = ChannelEvents.unpinnedMessageParser
}
