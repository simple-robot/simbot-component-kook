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
}