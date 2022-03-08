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

package love.forte.simbot.kaiheila.event.system.guild.role

import love.forte.simbot.kaiheila.event.*
import love.forte.simbot.kaiheila.event.system.*
import love.forte.simbot.kaiheila.event.system.guild.*


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


