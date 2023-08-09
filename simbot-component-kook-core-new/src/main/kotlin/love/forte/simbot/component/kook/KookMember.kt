/*
 * Copyright (c) 2023. ForteScarlet.
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

package love.forte.simbot.component.kook

import kotlinx.coroutines.CoroutineScope
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.definition.GuildMember
import kotlin.coroutines.CoroutineContext
import love.forte.simbot.kook.objects.User as KUser


/**
 * 一个 KOOK 的频道成员。
 *
 * @author ForteScarlet
 */
public interface KookMember : GuildMember, CoroutineScope {

    /**
     * 源于 [bot] 的上下文。
     */
    override val coroutineContext: CoroutineContext
        get() = bot.coroutineContext

    /**
     * 所属 Bot.
     */
    override val bot: KookBot

    /**
     * 得到此 Member 内对应的 api 模块下的原始 user 信息。
     */
    public val source: KUser

    /**
     * 成员ID
     */
    override val id: ID
        get() = source.id.ID

    /**
     * 成员头像
     */
    override val avatar: String
        get() = source.avatar

    /**
     * 成员用户名
     */
    override val username: String
        get() = source.username

    /**
     * 成员昵称
     */
    override val nickname: String
        get() = source.nickname ?: ""

    /**
     * 用户名的认证数字
     *
     * @see KUser.identifyNum
     */
    public val identifyNum: String
        get() = source.identifyNum

    /**
     * KOOK 不支持获取成员加入时间
     */
    @Deprecated(
        "'Member joinTime' does not supported in KOOK",
        ReplaceWith("Timestamp.notSupport()", "love.forte.simbot.Timestamp")
    )
    override val joinTime: Timestamp
        get() = Timestamp.notSupport()

    // TODO guild
    // TODO channel
    // TODO send
    // TODO mute
    // TODO roles

    // TODO SystemUser
}
