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

package love.forte.simbot.component.kook

import love.forte.simbot.ID
import love.forte.simbot.definition.Contact
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageReceipt
import love.forte.simbot.kook.objects.User as KkUser

/**
 *
 * Kook 组件下的联系人信息。
 *
 * @author ForteScarlet
 */
public interface KookContact : Contact {
    
    /**
     * id。
     */
    override val id: ID get() = sourceUser.id
    
    /**
     * 用户名。
     */
    override val username: String get() = sourceUser.username
    
    /**
     * Kook 的源用户信息。
     */
    public val sourceUser: KkUser
    
    /**
     * 联系人所属bot。
     */
    override val bot: KookComponentBot
    
    /**
     * 头像。
     */
    override val avatar: String get() = sourceUser.avatar
    
    /**
     * 发送消息。
     */
    @JvmSynthetic
    override suspend fun send(message: Message): MessageReceipt
    
    
}
