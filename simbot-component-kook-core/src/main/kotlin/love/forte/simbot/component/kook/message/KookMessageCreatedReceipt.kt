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

package love.forte.simbot.component.kook.message

import love.forte.plugin.suspendtrans.annotation.JvmAsync
import love.forte.plugin.suspendtrans.annotation.JvmBlocking
import love.forte.simbot.*
import love.forte.simbot.component.kook.KookComponentBot
import love.forte.simbot.component.kook.util.requestBy
import love.forte.simbot.definition.BotContainer
import love.forte.simbot.kook.api.message.DirectMessageDeleteRequest
import love.forte.simbot.kook.api.message.MessageCreated
import love.forte.simbot.kook.api.message.MessageDeleteRequest
import love.forte.simbot.message.AggregatedMessageReceipt
import love.forte.simbot.message.MessageReceipt
import love.forte.simbot.message.SingleMessageReceipt
import love.forte.simbot.message.aggregation
import java.util.*

/**
 * Kook 进行消息回复、发送后得到的回执。
 *
 * @see SingleKookMessageReceipt
 * @see KookAggregatedMessageReceipt
 */
public interface KookMessageReceipt : MessageReceipt, BotContainer {
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
 * 用于表示 [SingleMessageReceipt] 的 [KookMessageReceipt] 实现。
 *
 * @see KookMessageCreatedReceipt
 * @see KookApiRequestedReceipt
 */
public abstract class SingleKookMessageReceipt : SingleMessageReceipt(), KookMessageReceipt

/**
 * 消息创建后的回执实例。
 * @author ForteScarlet
 */
@Suppress("MemberVisibilityCanBePrivate")
public class KookMessageCreatedReceipt(
    override val result: MessageCreated,
    override val isDirect: Boolean,
    override val bot: KookComponentBot,
) : SingleKookMessageReceipt() {
    override val id: ID
        get() = result.msgId
    
    /**
     * 实例存在即成功。
     */
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
        val request = if (isDirect) DirectMessageDeleteRequest.create(id) else MessageDeleteRequest.create(id)
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
) : SingleKookMessageReceipt() {
    private var _id: ID? = null
    
    override val id: ID
        get() {
            return _id ?: synchronized(this) {
                _id ?: randomID().also {
                    _id = it
                }
            }
        }
    
    /**
     * 实例存在即成功。
     */
    override val isSuccess: Boolean get() = true
    
    /**
     * 不支持撤回，将会始终得到 `false`。
     *
     */
    override suspend fun delete(): Boolean = false
}

/**
 * 多条消息发送后的回执，其中会包含多个 [KookMessageReceipt]。
 */
@JvmBlocking
@JvmAsync
@ExperimentalSimbotApi
public class KookAggregatedMessageReceipt private constructor(
    override val bot: KookComponentBot,
    private val aggregatedMessageReceipt: AggregatedMessageReceipt,
) : AggregatedMessageReceipt(), KookMessageReceipt {
    
    /**
     * @see AggregatedMessageReceipt.isSuccess
     */
    override val isSuccess: Boolean
        get() = aggregatedMessageReceipt.isSuccess
    
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
    
    override val size: Int
        get() = aggregatedMessageReceipt.size
    
    override fun get(index: Int): SingleKookMessageReceipt {
        return aggregatedMessageReceipt.get(index) as SingleKookMessageReceipt
    }
    
    override fun iterator(): Iterator<SingleKookMessageReceipt> {
        // Collection.merge 保证了迭代器内部的元素类型
        @Suppress("UNCHECKED_CAST")
        return aggregatedMessageReceipt.iterator() as Iterator<SingleKookMessageReceipt>
    }
    
    public companion object {
        
        /**
         * 将多个 [KookMessageReceipt] 合并为一个聚合的回执。
         *
         * @param id 当前聚合回执中使用的id。默认为随机ID。
         * @param bot 如果为null，则会使用 receiver 中的第一个元素中的bot。
         * @throws IllegalArgumentException 如果 receiver 中元素为空
         */
        @JvmStatic
        @JvmOverloads
        public fun Collection<SingleKookMessageReceipt>.merge(bot: KookComponentBot? = null): KookMessageReceipt {
            Simbot.require(isNotEmpty()) { "Unable to merge empty element iterator" }
            return KookAggregatedMessageReceipt(bot ?: first().bot, aggregation())
        }
    }
    
}
