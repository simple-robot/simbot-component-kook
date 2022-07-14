/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kook 的一部分。
 *
 *  simbot-component-kook 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kook 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
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