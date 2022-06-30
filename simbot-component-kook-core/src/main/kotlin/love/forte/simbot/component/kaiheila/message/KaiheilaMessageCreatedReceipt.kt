/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kaiheila 的一部分。
 *
 *  simbot-component-kaiheila 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kaiheila 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

package love.forte.simbot.component.kaiheila.message

import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.action.DeleteSupport
import love.forte.simbot.component.kaiheila.KaiheilaComponentBot
import love.forte.simbot.component.kaiheila.util.requestBy
import love.forte.simbot.definition.BotContainer
import love.forte.simbot.kaiheila.api.message.DirectMessageDeleteRequest
import love.forte.simbot.kaiheila.api.message.MessageCreated
import love.forte.simbot.kaiheila.api.message.MessageDeleteRequest
import love.forte.simbot.message.MessageReceipt
import love.forte.simbot.randomID

/**
 *  Kook 进行消息回复、发送后得到的回执。
 *
 * @see KaiheilaMessageCreatedReceipt
 * @see KaiheilaApiRequestedReceipt
 */
public sealed interface KaiheilaMessageReceipt : MessageReceipt, BotContainer {
    /**
     * 此次消息发送的回执内容。
     *
     */
    public val result: Any?
    
    /**
     * 是否为私聊消息。
     */
    public val isDirect: Boolean
    
    /**
     * 相关的bot。
     */
    override val bot: KaiheilaComponentBot
}



/**
 * 消息创建后的回执实例。
 * @author ForteScarlet
 */
@Suppress("MemberVisibilityCanBePrivate")
public class KaiheilaMessageCreatedReceipt(
    override val result: MessageCreated,
    override val isDirect: Boolean,
    override val bot: KaiheilaComponentBot
) : KaiheilaMessageReceipt, DeleteSupport {
    override val id: ID
        get() = result.msgId
    

    override val isSuccess: Boolean
        get() = true

    public val nonce: String? get() = result.nonce

    /**
     * 消息发送时间(服务器时间戳)
     */
    public val timestamp: Timestamp get() = result.msgTimestamp

    /**
     * 尝试删除（撤回）发送的这条消息。
     */
    @JvmSynthetic
    override suspend fun delete(): Boolean {
        val request = if (isDirect) DirectMessageDeleteRequest(id) else MessageDeleteRequest(id)
        return request.requestBy(bot).isSuccess
    }

    public companion object {
        internal fun MessageCreated.asReceipt(
            direct: Boolean,
            bot: KaiheilaComponentBot
        ): KaiheilaMessageCreatedReceipt = KaiheilaMessageCreatedReceipt(this, direct, bot)
    }

}

/**
 * 消息发送后的回执。
 *
 * 与 [KaiheilaMessageCreatedReceipt] 不同的是，
 * [KaiheilaApiRequestedReceipt] 很可能并不是通过执行的消息api，
 * 例如通过 [KaiheilaRequestMessage] 执行了一个任意的请求。
 *
 * 也正因此，此回执不支持 [删除][delete] 操作。当使用 [delete] 时将会恒返回 `false`.
 *
 */
public class KaiheilaApiRequestedReceipt(
    override val result: Any?,
    override val isDirect: Boolean,
    override val bot: KaiheilaComponentBot
) : KaiheilaMessageReceipt {
    override val id: ID = randomID()
    override val isSuccess: Boolean get() = true
    
    /**
     * 不支持撤回，将会始终得到 `false`。
     *
     */
    override suspend fun delete(): Boolean = false
}