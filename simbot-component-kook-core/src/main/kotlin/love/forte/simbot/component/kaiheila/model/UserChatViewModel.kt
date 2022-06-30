package love.forte.simbot.component.kaiheila.model

import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.kaiheila.api.userchat.UserChatTargetInfo
import love.forte.simbot.kaiheila.api.userchat.UserChatView

/**
 * 针对于 [UserChatView] 在核心模块中的可变数据模型。
 */
internal data class UserChatViewModel(
    override var code: ID,
    override var lastReadTime: Timestamp,
    override var latestMsgTime: Timestamp,
    override var unreadCount: Int,
    override var isFriend: Boolean,
    override var isBlocked: Boolean,
    override var isTargetBlocked: Boolean,
    override var targetInfo: UserChatTargetInfoModel,
) : UserChatView


/**
 * 针对于 [UserChatTargetInfo] 在核心模块中的可变数据模型。
 */
internal data class UserChatTargetInfoModel(
    override var id: ID,
    override var username: String,
    override var avatar: String,
    override var online: Boolean,
) : UserChatTargetInfo


/**
 * 将 [UserChatTargetInfo] 转化为 [UserChatTargetInfoModel].
 */
internal fun UserChatTargetInfo.toModel(copy: Boolean = false): UserChatTargetInfoModel {
    if (this is UserChatTargetInfoModel) {
        return if (copy) this.copy() else this
    }
    
    return UserChatTargetInfoModel(
        id = id,
        username = username,
        avatar = avatar,
        online = online
    )
}


/**
 * 将 [UserChatView] 转化为 [UserChatViewModel].
 */
internal fun UserChatView.toModel(copy: Boolean = false, copyTargetInfo: Boolean = false): UserChatViewModel {
    if (this is UserChatViewModel) {
        if (copy) {
            val targetInfo = if (copyTargetInfo) this.targetInfo.copy() else this.targetInfo
            return copy(targetInfo = targetInfo)
        }
        
        return this
    }
    
    return UserChatViewModel(
        code = code,
        lastReadTime = lastReadTime,
        latestMsgTime = latestMsgTime,
        unreadCount = unreadCount,
        isFriend = isFriend,
        isBlocked = isBlocked,
        isTargetBlocked = isTargetBlocked,
        targetInfo = targetInfo.toModel(copyTargetInfo)
    )
}