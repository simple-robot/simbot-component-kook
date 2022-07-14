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
import love.forte.simbot.Timestamp
import love.forte.simbot.kook.api.userchat.UserChatTargetInfo
import love.forte.simbot.kook.api.userchat.UserChatView

/**
 * 针对于 [UserChatView] 在核心模块中的可变数据模型。
 */
internal data class UserChatViewModel(
    override val code: ID,
    override val lastReadTime: Timestamp,
    override val latestMsgTime: Timestamp,
    override val unreadCount: Int,
    override val isFriend: Boolean,
    override val isBlocked: Boolean,
    override val isTargetBlocked: Boolean,
    override val targetInfo: UserChatTargetInfoModel,
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