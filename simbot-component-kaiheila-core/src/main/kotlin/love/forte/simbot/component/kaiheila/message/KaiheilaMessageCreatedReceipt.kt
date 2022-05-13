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
import love.forte.simbot.kaiheila.api.message.DirectMessageDeleteRequest
import love.forte.simbot.kaiheila.api.message.MessageCreated
import love.forte.simbot.kaiheila.api.message.MessageDeleteRequest
import love.forte.simbot.message.MessageReceipt
import love.forte.simbot.randomID

// TODO
/**
 *
 */
public sealed interface KaiheilaMessageReceipt : MessageReceipt, DeleteSupport {
    public val result: Any?
    public val isDirect: Boolean
    
}



/**
 * 消息创建后的回执实例。
 * @author ForteScarlet
 */
@Suppress("MemberVisibilityCanBePrivate")
public class KaiheilaMessageCreatedReceipt(
    public val created: MessageCreated,
    public val isDirect: Boolean,
    private val bot: KaiheilaComponentBot
) : MessageReceipt, DeleteSupport {
    override val id: ID
        get() = created.msgId

    override val isSuccess: Boolean
        get() = true

    public val nonce: String? get() = created.nonce

    /**
     * 消息发送时间(服务器时间戳)
     */
    public val timestamp: Timestamp get() = created.msgTimestamp

    /**
     * 尝试删除（撤回）发送的这条消息。
     */
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
 * 也正因此，此回执不支持 [删除][DeleteSupport] 操作。
 *
 */
public class KaiheilaApiRequestedReceipt(
    public val result: Any?,
    public val isDirect: Boolean,
    // private val bot: KaiheilaComponentBot
) : MessageReceipt {
    override val id: ID = randomID()
    override val isSuccess: Boolean get() = true
}