package love.forte.simbot.component.kaiheila.model

import love.forte.simbot.ID
import love.forte.simbot.kaiheila.objects.User

/**
 * 针对于 [User] 在核心模块中的可变数据模型。
 */
internal data class UserModel(
    override var id: ID,
    override var username: String,
    override var nickname: String?,
    override var identifyNum: String,
    override var isOnline: Boolean,
    override var isBot: Boolean,
    override var status: Int,
    override var avatar: String,
    override var vipAvatar: String?,
    override var mobileVerified: Boolean,
    override var roles: List<ID>,
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