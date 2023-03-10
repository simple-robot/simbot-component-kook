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

package love.forte.simbot.kook.event.system.guild

import love.forte.simbot.kook.event.Event
import love.forte.simbot.kook.event.EventParser
import love.forte.simbot.kook.event.SysEventParser
import love.forte.simbot.kook.event.system.SystemEventParserDefinition
import love.forte.simbot.kook.event.system.registerParsers
import love.forte.simbot.kook.event.system.sysParser


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
