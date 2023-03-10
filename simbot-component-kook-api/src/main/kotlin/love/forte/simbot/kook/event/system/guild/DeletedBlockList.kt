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

package love.forte.simbot.kook.event.system.guild

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID


/**
 *
 * [服务器取消封禁用户](https://developer.kaiheila.cn/doc/event/guild#服务器取消封禁用户)
 *
 * `deleted_block_list`
 *
 * @author ForteScarlet
 */
public interface DeletedBlockListExtraBody : GuildEventExtraBody {
    /**
     * 操作人ID
     */
    public val operatorId: ID

    /**
     * 被封禁成员id列表
     * 用户id
     */
    public val userId: List<ID>
}

/**
 *
 * [服务器取消封禁用户](https://developer.kaiheila.cn/doc/event/guild#服务器取消封禁用户)
 *
 * `deleted_block_list`
 *
 * @author ForteScarlet
 */
@Serializable
internal data class DeletedBlockListExtraBodyImpl(
    /**
     * 操作人ID
     */
    @SerialName("operator_id")
    override val operatorId: CharSequenceID,
    /**
     * 被封禁成员id列表
     * 用户id
     */
    @SerialName("user_id")
    override val userId: List<CharSequenceID>
) : DeletedBlockListExtraBody
