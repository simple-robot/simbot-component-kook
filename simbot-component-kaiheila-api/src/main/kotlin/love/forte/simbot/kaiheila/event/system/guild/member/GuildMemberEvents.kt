/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kaiheila.event.system.guild.member

import love.forte.simbot.kaiheila.event.EventParser
import love.forte.simbot.kaiheila.event.SysEventParser
import love.forte.simbot.kaiheila.event.system.SystemEventParserDefinition
import love.forte.simbot.kaiheila.event.system.guild.GuildEventExtraBody
import love.forte.simbot.kaiheila.event.system.registerParsers
import love.forte.simbot.kaiheila.event.system.sysParser


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
