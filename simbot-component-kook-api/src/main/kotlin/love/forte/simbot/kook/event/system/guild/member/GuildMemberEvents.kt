/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.event.system.guild.member

import love.forte.simbot.kook.event.EventParser
import love.forte.simbot.kook.event.SysEventParser
import love.forte.simbot.kook.event.system.SystemEventParserDefinition
import love.forte.simbot.kook.event.system.guild.GuildEventExtraBody
import love.forte.simbot.kook.event.system.registerParsers
import love.forte.simbot.kook.event.system.sysParser


/**
 *
 * 服务器成员相关事件 extra 的 body。
 *
 * @author ForteScarlet
 */
public interface GuildMemberEventExtraBody : GuildEventExtraBody


/**
 * 服务器成员相关定义。
 *
 * @see JoinedGuildEvent
 * @see ExitedGuildEvent
 * @see UpdatedGuildMemberEvent
 * @see GuildMemberOnlineEvent
 * @see GuildMemberOfflineEvent
 */
public object GuildMemberEvents {

    /** 新成员加入服务器 */
    public const val JOINED_GUILD: String = "joined_guild"

    /** 服务器成员退出 */
    public const val EXITED_GUILD: String = "exited_guild"

    /** 服务器成员信息更新 */
    public const val UPDATED_GUILD_MEMBER: String = "updated_guild_member"

    /** 服务器成员上线 */
    public const val GUILD_MEMBER_ONLINE: String = "guild_member_online"

    /** 服务器成员下线 */
    public const val GUILD_MEMBER_OFFLINE: String = "guild_member_offline"

    /** 新成员加入服务器 */
    public val joinedGuildEventParser: SysEventParser<JoinedGuildEventBody> =
        sysParser(JOINED_GUILD, JoinedGuildEventBodyImpl.serializer())

    /** 服务器成员退出 */
    public val exitedGuildEventParser: SysEventParser<ExitedGuildEventBody> =
        sysParser(EXITED_GUILD, ExitedGuildEventBodyImpl.serializer())

    /** 服务器成员信息更新 */
    public val updatedGuildMemberEventParser: SysEventParser<UpdatedGuildMemberEventBody> =
        sysParser(UPDATED_GUILD_MEMBER, UpdatedGuildMemberEventBodyImpl.serializer())

    /** 服务器成员上线 */
    public val guildMemberOnlineEventParser: SysEventParser<GuildMemberOnlineEventBody> =
        sysParser(GUILD_MEMBER_ONLINE, GuildMemberOnlineEventBodyImpl.serializer())

    /** 服务器成员下线 */
    public val guildMemberOfflineEventParser: SysEventParser<GuildMemberOfflineEventBody> =
        sysParser(GUILD_MEMBER_OFFLINE, GuildMemberOfflineEventBodyImpl.serializer())


}

internal fun MutableMap<Any, EventParser<*, *>>.guildMemberEventParsers() {
    registerParsers(GuildMemberEvents.JOINED_GUILD, GuildMemberEvents.joinedGuildEventParser)
    registerParsers(GuildMemberEvents.EXITED_GUILD, GuildMemberEvents.exitedGuildEventParser)
    registerParsers(GuildMemberEvents.UPDATED_GUILD_MEMBER, GuildMemberEvents.updatedGuildMemberEventParser)
    registerParsers(GuildMemberEvents.GUILD_MEMBER_ONLINE, GuildMemberEvents.guildMemberOnlineEventParser)
    registerParsers(GuildMemberEvents.GUILD_MEMBER_OFFLINE, GuildMemberEvents.guildMemberOfflineEventParser)
}

/**
 * 事件定义。
 * @see JoinedGuildEventBody
 */
public object JoinedGuildEvent : SystemEventParserDefinition<JoinedGuildEventBody>() {
    override val parser: SysEventParser<JoinedGuildEventBody>
        get() = GuildMemberEvents.joinedGuildEventParser
}

/**
 * 事件定义。
 * @see ExitedGuildEventBody
 */
public object ExitedGuildEvent : SystemEventParserDefinition<ExitedGuildEventBody>() {
    override val parser: SysEventParser<ExitedGuildEventBody>
        get() = GuildMemberEvents.exitedGuildEventParser
}

/**
 * 事件定义。
 * @see UpdatedGuildMemberEventBody
 */
public object UpdatedGuildMemberEvent : SystemEventParserDefinition<UpdatedGuildMemberEventBody>() {
    override val parser: SysEventParser<UpdatedGuildMemberEventBody>
        get() = GuildMemberEvents.updatedGuildMemberEventParser
}

/**
 * 事件定义。
 * @see GuildMemberOnlineEventBody
 */
public object GuildMemberOnlineEvent : SystemEventParserDefinition<GuildMemberOnlineEventBody>() {
    override val parser: SysEventParser<GuildMemberOnlineEventBody>
        get() = GuildMemberEvents.guildMemberOnlineEventParser
}

/**
 * 事件定义。
 * @see GuildMemberOfflineEventBody
 */
public object GuildMemberOfflineEvent : SystemEventParserDefinition<GuildMemberOfflineEventBody>() {
    override val parser: SysEventParser<GuildMemberOfflineEventBody>
        get() = GuildMemberEvents.guildMemberOfflineEventParser
}
