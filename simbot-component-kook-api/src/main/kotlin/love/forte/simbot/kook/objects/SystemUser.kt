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

package love.forte.simbot.kook.objects

import love.forte.simbot.ID
import love.forte.simbot.kook.objects.SystemUser.id

/**
 *
 * 当 [id] == `1` 的时候，用户代表为 _系统用户_ 。
 *
 * 参考 [事件结构/格式说明](https://developer.kaiheila.cn/doc/event/event-introduction) 中事件结构的 `author_id` 字段说明。
 *
 * @see User
 */
public object SystemUser : User {
    /**
     * 系统用户的默认ID值。
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public const val ID_VALUE: String = "1"

    override val id: ID = ID_VALUE.ID

    override val username: String
        get() = "System"
    override val nickname: String
        get() = "System"

    override val identifyNum: String
        get() = "0000"
    override val isOnline: Boolean
        get() = true
    override val isBot: Boolean
        get() = true
    override val status: Int
        get() = 0
    override val avatar: String
        get() = "https://www.kookapp.cn/css/image/logo.png"
    override val vipAvatar: String
        get() = avatar
    override val mobileVerified: Boolean
        get() = true
    override val roles: List<ID>
        get() = emptyList()


}
