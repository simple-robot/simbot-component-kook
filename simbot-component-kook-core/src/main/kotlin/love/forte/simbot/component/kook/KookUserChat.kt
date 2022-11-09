/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
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