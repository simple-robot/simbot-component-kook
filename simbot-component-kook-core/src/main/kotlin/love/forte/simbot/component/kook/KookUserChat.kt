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

package love.forte.simbot.component.kook

import love.forte.plugin.suspendtrans.annotation.JvmAsync
import love.forte.plugin.suspendtrans.annotation.JvmBlocking
import love.forte.simbot.ID
import love.forte.simbot.action.DeleteSupport
import love.forte.simbot.component.kook.message.KookMessageCreatedReceipt
import love.forte.simbot.component.kook.message.KookMessageReceipt
import love.forte.simbot.definition.Contact
import love.forte.simbot.definition.Stranger
import love.forte.simbot.kook.api.userchat.UserChatDeleteRequest
import love.forte.simbot.kook.api.userchat.UserChatView
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent

/**
 * Kook 的 [user-chat 私聊会话](https://developer.kaiheila.cn/doc/http/user-chat)。
 *
 * Kook 组件会将 [私聊会话][KookUserChat] 视为 [Stranger] 处理，同时实现 [Contact] 来提供可交流的联系人能力。
 *
 * ## 可删除的
 * Kook 中的聊天会话是可以通过 [UserChatDeleteRequest] 进行删除的。因此 [KookUserChat] 实现了 [DeleteSupport] 来支持 [删除操作][delete]。
 *
 */
public interface KookUserChat : Stranger, Contact, KookComponentDefinition<UserChatView>, DeleteSupport {
    /**
     * 私聊会话对应用户ID。
     *
     * 如果需要此会话的会话code而不是用于id，使用 [source] 来获取 [UserChatView.code]。
     *
     */
    override val id: ID
    override val bot: KookComponentBot
    override val source: UserChatView
    
    override val avatar: String get() = source.targetInfo.avatar
    override val username: String get() = source.targetInfo.username
    
    /**
     * 向当前好友（私聊会话）发送消息。
     */
    @JvmBlocking
    @JvmAsync
    override suspend fun send(message: Message): KookMessageReceipt
    
    /**
     * 向当前好友（私聊会话）发送消息。
     */
    @JvmBlocking
    @JvmAsync
    override suspend fun send(text: String): KookMessageCreatedReceipt
    
    /**
     * 向当前好友（私聊会话）发送消息。
     */
    @JvmBlocking
    @JvmAsync
    override suspend fun send(message: MessageContent): KookMessageReceipt
    
    // TODO Doc update.
    /**
     * 删除当前 [聊天会话][KookUserChat].
     *
     * 通过 [UserChatDeleteRequest] 删除当前聊天会话。除非 [delete] 抛出异常，否则 [delete] 始终得到 `true`。
     * 在删除的过程中会直接抛出请求 [UserChatDeleteRequest] 过程中可能产生的任何异常。
     *
     */
    @JvmSynthetic
    override suspend fun delete(): Boolean
}
