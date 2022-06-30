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

package love.forte.simbot.kaiheila.event.system.guild

import love.forte.simbot.kaiheila.event.Event
import love.forte.simbot.kaiheila.event.EventParser
import love.forte.simbot.kaiheila.event.SysEventParser
import love.forte.simbot.kaiheila.event.system.SystemEventParserDefinition
import love.forte.simbot.kaiheila.event.system.registerParsers
import love.forte.simbot.kaiheila.event.system.sysParser


/**
 *
 *
 */
// public interface GuildEventExtra<B : GuildEventExtraBody> : Event.Extra.Sys<B>


/**
 *
 * [服务器相关事件列表](https://developer.kaiheila.cn/doc/event/guild)
 *
 * @see Event.Extra.Sys.body
 */
public interface GuildEventExtraBody


/**
 * 频道服务器相关定义。
 *
 * @see AddedBlockListEvent
 * @see DeletedBlockListEvent
 * @see DeletedGuildEvent
 * @see UpdatedGuildEvent
 *
 */
public object GuildEvents {

    /** 服务器封禁用户 */
    public const val ADDED_BLOCK_LIST: String = "added_block_list"

    /** 服务器取消封禁用户 */
    public const val DELETED_BLOCK_LIST: String = "deleted_block_list"

    /** 服务器删除 */
    public const val DELETED_GUILD: String = "deleted_guild"

    /** 服务器信息更新 */
    public const val UPDATED_GUILD: String = "updated_guild"


    /** 服务器封禁用户事件解析器 */
    public val addedBlockListParser: SysEventParser<AddedBlockListExtraBody> =
        sysParser(ADDED_BLOCK_LIST, AddedBlockListExtraBodyImpl.serializer())

    /** 服务器取消封禁用户事件解析器 */
    public val deletedBlockListParser: SysEventParser<DeletedBlockListExtraBody> =
        sysParser(DELETED_BLOCK_LIST, DeletedBlockListExtraBodyImpl.serializer())

    /** 服务器删除事件解析器 */
    public val deletedGuildParser: SysEventParser<DeletedGuildExtraBody> =
        sysParser(DELETED_GUILD, DeletedGuildExtraBodyImpl.serializer())

    /** 服务器信息更新事件解析器 */
    public val updatedGuildParser: SysEventParser<UpdatedGuildExtraBody> =
        sysParser(UPDATED_GUILD, UpdatedGuildExtraBodyImpl.serializer())


}

internal fun MutableMap<Any, EventParser<*, *>>.guildEventParsers() {
    registerParsers(
        GuildEvents.ADDED_BLOCK_LIST,
        GuildEvents.addedBlockListParser
    )
    registerParsers(
        GuildEvents.DELETED_BLOCK_LIST,
        GuildEvents.deletedBlockListParser
    )
    registerParsers(
        GuildEvents.DELETED_GUILD,
        GuildEvents.deletedGuildParser
    )
    registerParsers(
        GuildEvents.UPDATED_GUILD,
        GuildEvents.updatedGuildParser
    )
}


/**
 * 事件定义。
 * @see AddedBlockListExtraBody
 */
public object AddedBlockListEvent : SystemEventParserDefinition<AddedBlockListExtraBody>() {
    override val parser: SysEventParser<AddedBlockListExtraBody>
        get() = GuildEvents.addedBlockListParser
}

/**
 * 事件定义。
 * @see DeletedBlockListExtraBody
 */
public object DeletedBlockListEvent : SystemEventParserDefinition<DeletedBlockListExtraBody>() {
    override val parser: SysEventParser<DeletedBlockListExtraBody>
        get() = GuildEvents.deletedBlockListParser
}

/**
 * 事件定义。
 * @see DeletedGuildExtraBody
 */
public object DeletedGuildEvent : SystemEventParserDefinition<DeletedGuildExtraBody>() {
    override val parser: SysEventParser<DeletedGuildExtraBody>
        get() = GuildEvents.deletedGuildParser
}

/**
 * 事件定义。
 * @see UpdatedGuildExtraBody
 */
public object UpdatedGuildEvent : SystemEventParserDefinition<UpdatedGuildExtraBody>() {
    override val parser: SysEventParser<UpdatedGuildExtraBody>
        get() = GuildEvents.updatedGuildParser
}