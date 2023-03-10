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

package love.forte.simbot.component.kook.model

import love.forte.simbot.ID
import love.forte.simbot.kook.objects.User

/**
 * 针对于 [User] 在核心模块中的可变数据模型。
 */
internal data class UserModel(
    override val id: ID,
    override val username: String,
    override val nickname: String?,
    override val identifyNum: String,
    override val isOnline: Boolean,
    override val isBot: Boolean,
    override val status: Int,
    override val avatar: String,
    override val vipAvatar: String?,
    override val mobileVerified: Boolean,
    override val roles: List<ID>,
) : User


internal fun User.toModel(copy: Boolean = false): UserModel {
    if (this is UserModel) {
        return if (copy) this.copy() else this
    }
    
    
    return UserModel(
        id = id,
        username = username,
        nickname = nickname,
        identifyNum = identifyNum,
        isOnline = isOnline,
        isBot = isBot,
        status = status,
        avatar = avatar,
        vipAvatar = vipAvatar,
        mobileVerified = mobileVerified,
        roles = roles,
    )
}
