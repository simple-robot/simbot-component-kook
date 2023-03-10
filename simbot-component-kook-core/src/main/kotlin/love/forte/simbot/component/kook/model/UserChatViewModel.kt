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
