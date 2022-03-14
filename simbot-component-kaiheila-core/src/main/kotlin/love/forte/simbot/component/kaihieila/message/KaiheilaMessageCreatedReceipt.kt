/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.component.kaihieila.message

import love.forte.simbot.*
import love.forte.simbot.action.*
import love.forte.simbot.component.kaihieila.*
import love.forte.simbot.component.kaihieila.util.*
import love.forte.simbot.kaiheila.api.message.*
import love.forte.simbot.message.*


/**
 * 消息创建后的回执实例。
 * @author ForteScarlet
 */
public class KaiheilaMessageCreatedReceipt(
    private val created: MessageCreated,
    private val direct: Boolean,
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
        val request = if (direct) DirectMessageDeleteRequest(id) else MessageDeleteRequest(id)
        return request.requestBy(bot).isSuccess
    }

    public companion object {
        internal fun MessageCreated.asReceipt(
            direct: Boolean,
            bot: KaiheilaComponentBot
        ): KaiheilaMessageCreatedReceipt = KaiheilaMessageCreatedReceipt(this, direct, bot)
    }

}