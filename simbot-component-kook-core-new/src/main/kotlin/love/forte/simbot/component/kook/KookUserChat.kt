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
import love.forte.simbot.JST
import love.forte.simbot.action.DeleteSupport
import love.forte.simbot.component.kook.bot.KookBot
import love.forte.simbot.component.kook.message.KookMessageCreatedReceipt
import love.forte.simbot.component.kook.message.KookMessageReceipt
import love.forte.simbot.definition.Contact
import love.forte.simbot.definition.Stranger
import love.forte.simbot.kook.api.userchat.DeleteUserChatApi
import love.forte.simbot.kook.api.userchat.TargetInfo
import love.forte.simbot.kook.api.userchat.UserChatView
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent

/**
 * KOOK 的 [user-chat 私聊会话](https://developer.kaiheila.cn/doc/http/user-chat)。
 *
 * KOOK 组件会将 [私聊会话][KookUserChat] 视为 [Stranger] 处理，同时实现 [Contact] 来提供可交流的联系人能力。
 *
 * ## 可删除的
 * KOOK 中的聊天会话是可以通过 [DeleteUserChatApi] 进行删除的。因此 [KookUserChat] 实现了 [DeleteSupport] 来支持 [删除操作][delete]。
 *
 * @author ForteScarlet
 */
public interface KookUserChat : Stranger, Contact, DeleteSupport {
    /**
     * 私聊会话的会话 `code` , 同 [source.code][UserChatView.code] 。
     *
     * 如果需要此会话对应的目标用户id，使用 [targetId]。
     *
     * @see UserChatView.code
     * @see targetId
     */
    override val id: ID

    /**
     * 私聊会话的会话对应的目标用户id , 同 [source.targetInfo.id][TargetInfo.id] 。
     *
     * 如果需要此会话的会话 `code`，使用 [id] 来获取。
     *
     * @see id
     * @see TargetInfo.id
     */
    public val targetId: ID

    override val bot: KookBot

    /**
     * 用户会话源信息对象。
     */
    public val source: UserChatView

    override val avatar: String get() = source.targetInfo.avatar
    override val username: String get() = source.targetInfo.username

    /**
     * 向当前好友（私聊会话）发送消息。
     */
    @JST
    override suspend fun send(message: Message): KookMessageReceipt

    /**
     * 向当前好友（私聊会话）发送消息。
     */
    @JST
    override suspend fun send(text: String): KookMessageCreatedReceipt

    /**
     * 向当前好友（私聊会话）发送消息。
     */
    @JST
    override suspend fun send(message: MessageContent): KookMessageReceipt

    /**
     * 删除当前 [聊天会话][KookUserChat].
     *
     * 通过 [DeleteUserChatApi] 删除当前聊天会话。除非 [delete] 抛出异常，否则 [delete] 基本上总是会得到 `true`。
     * 在删除的过程中会直接抛出 [DeleteUserChatApi] 在请求过程中可能产生的任何异常。
     *
     */
    @JvmSynthetic
    override suspend fun delete(): Boolean
}
