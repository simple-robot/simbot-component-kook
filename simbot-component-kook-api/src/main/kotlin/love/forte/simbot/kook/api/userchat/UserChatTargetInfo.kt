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

package love.forte.simbot.kook.api.userchat

import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.definition.UserInfo


/**
 *
 * [UserChatView] 中的 `targetInfo` 属性。
 *
 */
public interface UserChatTargetInfo : UserInfo {
    override val id: ID
    override val username: String
    override val avatar: String
    public val online: Boolean
}



@kotlinx.serialization.Serializable
internal data class UserChatTargetInfoImpl(
    override val id: CharSequenceID,
    override val username: String,
    override val avatar: String,
    override val online: Boolean = false,
) : UserChatTargetInfo
