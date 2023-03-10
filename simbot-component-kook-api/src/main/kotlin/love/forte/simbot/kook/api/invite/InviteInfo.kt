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

package love.forte.simbot.kook.api.invite

import love.forte.simbot.ID
import love.forte.simbot.kook.objects.User

/**
 * 邀请信息.
 *
 * see [获取邀请列表](https://developer.kaiheila.cn/doc/http/invite#%E8%8E%B7%E5%8F%96%E9%82%80%E8%AF%B7%E5%88%97%E8%A1%A8).
 *
 * @see InviteListRequest
 *
 */
public interface InviteInfo {
    /**
     * 服务器id
     *
     */
    public val guildId: ID

    /**
     * 频道id
     *
     */
    public val channelId: ID

    /**
     * url code
     *
     */
    public val urlCode: String

    /**
     * 地址
     *
     */
    public val url: String

    /**
     * 用户
     *
     */
    public val user: User
}
