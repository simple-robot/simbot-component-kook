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

package love.forte.simbot.kook.event.system.guild.role

import love.forte.simbot.kook.event.EventParser
import love.forte.simbot.kook.event.SysEventParser
import love.forte.simbot.kook.event.system.SystemEventParserDefinition
import love.forte.simbot.kook.event.system.guild.GuildEventExtraBody
import love.forte.simbot.kook.event.system.registerParsers
import love.forte.simbot.kook.event.system.sysParser


/**
 *
 * 服务器角色相关事件 extra 的 body。
 *
 * @author ForteScarlet
 */
public interface GuildRoleEventExtraBody : GuildEventExtraBody


/**
 * 频道服务器角色相关定义。
 *
 * @see AddedRoleEvent
 * @see DeletedRoleEvent
 * @see UpdatedRoleEvent
 */
public object GuildRoleEvents {

    /** 服务器角色增加 */
    public const val ADDED_ROLE: String = "added_role"

    /** 服务器角色删除 */
    public const val DELETED_ROLE: String = "deleted_role"

    /** 服务器角色更新 */
    public const val UPDATED_ROLE: String = "updated_role"

    /** 服务器角色增加 */
    public val addedRoleEventParser: SysEventParser<AddedRoleEventBody> =
        sysParser(ADDED_ROLE, AddedRoleEventBodyImpl.serializer())

    /** 服务器角色删除 */
    public val deletedRoleEventParser: SysEventParser<DeletedRoleEventBody> =
        sysParser(DELETED_ROLE, DeletedRoleEventBodyImpl.serializer())

    /** 服务器角色更新 */
    public val updatedRoleEventParser: SysEventParser<UpdatedRoleEventBody> =
        sysParser(UPDATED_ROLE, UpdatedRoleEventBodyImpl.serializer())

}

internal fun MutableMap<Any, EventParser<*, *>>.guildRoleEventParsers() {
    registerParsers(
        GuildRoleEvents.ADDED_ROLE,
        GuildRoleEvents.addedRoleEventParser
    )
    registerParsers(
        GuildRoleEvents.DELETED_ROLE,
        GuildRoleEvents.deletedRoleEventParser
    )
    registerParsers(
        GuildRoleEvents.UPDATED_ROLE,
        GuildRoleEvents.updatedRoleEventParser
    )
}


/**
 * 事件定义。
 * @see AddedRoleEventBody
 */
public object AddedRoleEvent : SystemEventParserDefinition<AddedRoleEventBody>() {
    override val parser: SysEventParser<AddedRoleEventBody>
        get() = GuildRoleEvents.addedRoleEventParser
}

/**
 * 事件定义。
 * @see DeletedRoleEventBody
 */
public object DeletedRoleEvent : SystemEventParserDefinition<DeletedRoleEventBody>() {
    override val parser: SysEventParser<DeletedRoleEventBody>
        get() = GuildRoleEvents.deletedRoleEventParser
}

/**
 * 事件定义。
 * @see UpdatedRoleEventBody
 */
public object UpdatedRoleEvent : SystemEventParserDefinition<UpdatedRoleEventBody>() {
    override val parser: SysEventParser<UpdatedRoleEventBody>
        get() = GuildRoleEvents.updatedRoleEventParser
}


