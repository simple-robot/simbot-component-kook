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

package love.forte.simbot.component.kook.message

import love.forte.plugin.suspendtrans.annotation.JvmAsync
import love.forte.plugin.suspendtrans.annotation.JvmBlocking
import love.forte.simbot.*
import love.forte.simbot.action.DeleteSupport
import love.forte.simbot.component.kook.KookComponentBot
import love.forte.simbot.component.kook.util.requestBy
import love.forte.simbot.definition.BotContainer
import love.forte.simbot.kook.api.message.DirectMessageDeleteRequest
import love.forte.simbot.kook.api.message.MessageCreated
import love.forte.simbot.kook.api.message.MessageDeleteRequest
import love.forte.simbot.message.MessageReceipt
import java.util.*

/**
 * Kook 进行消息回复、发送后得到的回执。
 *
 * @see KookMessageCreatedReceipt
 * @see KookApiRequestedReceipt
 */
public sealed interface KookMessageReceipt : MessageReceipt, BotContainer {
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
    override val bot: KookComponentBot
}


/**
 * 消息创建后的回执实例。
 * @author ForteScarlet
 */
@Suppress("MemberVisibilityCanBePrivate")
public class KookMessageCreatedReceipt(
    override val result: MessageCreated,
    override val isDirect: Boolean,
    override val bot: KookComponentBot,
) : KookMessageReceipt, DeleteSupport {
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
            bot: KookComponentBot,
        ): KookMessageCreatedReceipt = KookMessageCreatedReceipt(this, direct, bot)
    }
    
}

/**
 * 消息发送后的回执。
 *
 * 与 [KookMessageCreatedReceipt] 不同的是，
 * [KookApiRequestedReceipt] 很可能并不是通过执行的消息api，
 * 例如通过 [KookRequestMessage] 执行了一个任意地请求。
 *
 * 也正因此，此回执不支持 [删除][delete] 操作。当使用 [delete] 时将会恒返回 `false`.
 *
 */
public class KookApiRequestedReceipt(
    override val result: Any?,
    override val isDirect: Boolean,
    override val bot: KookComponentBot,
) : KookMessageReceipt {
    override val id: ID = randomID()
    override val isSuccess: Boolean get() = true
    
    /**
     * 不支持撤回，将会始终得到 `false`。
     *
     */
    override suspend fun delete(): Boolean = false
}

// TODO
/**
 * 多条消息发送后的回执。
 * [KookAggregationMessageReceipt] 的元素中不嵌套引用 [KookAggregationMessageReceipt] 类型。
 *
 */
@JvmBlocking
@JvmAsync
@ExperimentalSimbotApi
public class KookAggregationMessageReceipt private constructor(
    /**
     * id. 默认为一个无实际含义的随机ID。
     */
    override val id: ID,
    override val bot: KookComponentBot,
    private val receipts: List<KookMessageReceipt>,
) : KookMessageReceipt, Iterable<KookMessageReceipt> {
    /*
        这种'复数回执'未来有可能会被考虑合并至核心库，参考 https://github.com/simple-robot/simpler-robot/issues/497
     */
    
    /**
     * 是否成功。当内部持有的所有回执全部成功才视为成功。
     */
    override val isSuccess: Boolean
        get() = receipts.all { it.isSuccess }
    
    /**
     * 复数回执不存在结果。
     */
    override val result: Any?
        get() = null
    
    /**
     * 复数回执不属于私聊类型。
     */
    override val isDirect: Boolean
        get() = false
    
    override fun iterator(): Iterator<KookMessageReceipt> = receipts.iterator()
    override fun spliterator(): Spliterator<KookMessageReceipt> = receipts.spliterator()
    public operator fun get(index: Int): KookMessageReceipt = receipts[index]
    
    @get:JvmName("size")
    public val size: Int get() = receipts.size
    
    /**
     * 删除当前回执中的全部回执, 实际为使用 [deleteAll]
     *
     * @see deleteAll
     * @return 是否存在删除成功的结果。即 [deleteAll] 的结果 > 0
     *
     */
    override suspend fun delete(): Boolean {
        return deleteAll() > 0
    }
    
    /**
     * 删除全部回执。
     */
    public suspend fun deleteAll(): Int {
        var count = 0
        for (receipt in receipts) {
            if (receipt.delete()) {
                count++
            }
        }
        return count
    }
    
    public companion object {
    
        /**
         * 将多个 [KookMessageReceipt] 合并为一个聚合的回执。
         *
         * @param bot 如果为null，则会使用 receiver 中的第一个元素中的bot。
         * @throws IllegalArgumentException 如果 receiver 中元素为空
         */
        @JvmStatic
        @JvmOverloads
        public fun Iterable<KookMessageReceipt>.merge(
            id: ID = randomID(), bot: KookComponentBot? = null,
        ): KookMessageReceipt {
            var bot0: KookComponentBot? = bot
            val iter = iterator()
            Simbot.require(iter.hasNext()) { "Unable to merge empty element iterator" }
            
            val list = buildList<KookMessageReceipt>(if (this is Collection) size else 16) {
                for (receipt in this) {
                    if (bot0 == null) {
                        bot0 = receipt.bot
                    }
                    if (receipt is KookAggregationMessageReceipt) {
                        addAll(receipt.receipts)
                    } else {
                        add(receipt)
                    }
                }
            }
            
            Simbot.require(list.isNotEmpty()) { "Unable to merge empty element iterator" }
            
            return KookAggregationMessageReceipt(id, bot!!, list)
        }
    }
    
}
