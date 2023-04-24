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

package love.forte.simbot.kook.objects

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * [用户 User](https://developer.kookapp.cn/doc/objects#%E7%94%A8%E6%88%B7%20User)
 *
 * @author ForteScarlet
 */
public interface User {
    /**
     * 用户的id
     */
    public val id: String

    /**
     * 用户名称
     */
    public val username: String

    /**
     * 用户在当前服务器的昵称
     */
    public val nickname: String?


    /**
     * 用户名的认证数字，用户名正常为：`user_name#identify_num`
     */
    public val identifyNum: String
    // ser name: identify_num

    /**
     * 当前是否在线
     */
    public val isOnline: Boolean
    // ser name: online

    /**
     * 用户是否为机器人
     */
    public val isBot: Boolean
    // ser name: bot

    /**
     *用户的状态, `0` 和 `1` 代表正常，`10` 代表被封禁
     */
    public val status: Int

    /**
     * 用户的头像的url地址
     */
    public val avatar: String


    /**
     * vip用户的头像的url地址，可能为gif动图
     */
    public val vipAvatar: String?

    /**
     * 是否手机号已验证
     */
    public val isMobileVerified: Boolean
    // ser name: mobile_verified


    /**
     * 用户在当前服务器中的角色 id 组成的列表。为null则代表信息中未包含此信息。
     */
    public val roles: List<Int>?

    public companion object {
        /**
         * [User.status] 为 `10` 时所代表的 `"被封禁"`
         */
        public const val BANNED_STATUS: Int = 10
    }

}

/**
 * 如果 [User.status] 为 [`10`][User.BANNED_STATUS] 则代表封禁。
 *
 */
public inline val User.isStatusBanned: Boolean get() = status == User.BANNED_STATUS


/**
 * [User] 的基础实现。
 *
 * @see User
 */
@Serializable
public data class SimpleUser(
    override val id: String,
    override val username: String,
    override val avatar: String,
    /**
     * 用户在当前服务器的昵称，默认为 `null`
     */
    override val nickname: String? = null,
    /**
     * 用户名的认证数字，用户名正常为：user_name#identify_num
     *
     * 没有 `identify_num` 的情况下，会**尝试**从 [username] 中切割 `#` 并解析出 identifyNum 的值，
     * 无法得到结果时使用空字符串。
     */
    @SerialName("identify_num") override val identifyNum: String = username.split("#", limit = 2).let { if (it.size < 2) it[0] else "" },
    /**
     * 当前是否在线，默认为 `false`
     */
    @SerialName("online") override val isOnline: Boolean = false,
    /**
     * 用户是否为机器人，默认为 `false`
     */
    @SerialName("bot") override val isBot: Boolean = false,
    /**
     * 用户的状态, 0 和 1 代表正常，10 代表被封禁，默认情况下视为正常状态 `0`
     */
    override val status: Int = 0,
    /**
     * vip用户的头像的url地址，可能为gif动图，默认为 `null`
     */
    @SerialName("vip_avatar") override val vipAvatar: String? = null,
    /**
     * 是否手机号已验证，默认为 `false`
     */
    @SerialName("mobile_verified") override val isMobileVerified: Boolean = false,
    /**
     * 用户在当前服务器中的角色 id 组成的列表，默认为 `null`
     */
    override val roles: List<Int>? = null
) : User
