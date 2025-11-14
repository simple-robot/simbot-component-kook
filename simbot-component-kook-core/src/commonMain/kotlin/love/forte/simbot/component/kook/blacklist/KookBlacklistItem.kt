/*
 *     Copyright (c) 2025. ForteScarlet.
 *
 *     This file is part of simbot-component-kook.
 *
 *     simbot-component-kook is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     simbot-component-kook is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with simbot-component-kook,
 *     If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.component.kook.blacklist

import love.forte.simbot.ability.DeleteOption
import love.forte.simbot.ability.DeleteSupport
import love.forte.simbot.ability.StandardDeleteOption
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.time.Timestamp
import love.forte.simbot.kook.objects.User
import love.forte.simbot.suspendrunner.ST

/**
 * KOOK 服务器黑名单列表中的一个元素。
 *
 * @since 4.4.0
 *
 * @author ForteScarlet
 */
@ExperimentalBlacklistApi
public interface KookBlacklistItem : DeleteSupport {
    /**
     * 用户 ID
     */
    public val userId: ID

    /**
     * 服务器 ID
     */
    public val guildId: ID

    /**
     * 加入黑名单的时间
     */
    public val createdTime: Timestamp

    /**
     * 加入黑名单的原因
     */
    public val remark: String

    /**
     * 用户信息。
     */
    public val userInfo: User

    /**
     * 将此人移出黑名单列表。
     *
     * @throws RuntimeException 如果在请求API过程中出现任何非预期异常，
     * 并且没有提供 [StandardDeleteOption.IGNORE_ON_FAILURE]
     */
    @ST
    override suspend fun delete(vararg options: DeleteOption)
}