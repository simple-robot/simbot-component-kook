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

package love.forte.simbot.component.kook.message

import love.forte.simbot.annotations.ExperimentalSimbotAPI
import love.forte.simbot.ID
import love.forte.simbot.Simbot
import love.forte.simbot.Timestamp
import love.forte.simbot.component.kook.bot.KookBot
import love.forte.simbot.component.kook.util.requestResultBy
import love.forte.simbot.definition.BotContainer
import love.forte.simbot.delegate.getValue
import love.forte.simbot.delegate.stringID
import love.forte.simbot.delegate.timestamp
import love.forte.simbot.kook.api.message.DeleteChannelMessageApi
import love.forte.simbot.kook.api.message.DeleteDirectMessageApi
import love.forte.simbot.kook.api.message.SendMessageResult
import love.forte.simbot.message.AggregatedMessageReceipt
import love.forte.simbot.message.MessageReceipt
import love.forte.simbot.message.SingleMessageReceipt
import love.forte.simbot.message.aggregation

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
    override val bot: KookBot
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
 *
 * @author ForteScarlet
 */
@Suppress("MemberVisibilityCanBePrivate")
public class KookMessageCreatedReceipt(
    override val result: SendMessageResult,
    override val isDirect: Boolean,
    override val bot: KookBot,
) : SingleKookMessageReceipt() {
    override val id: ID by stringID { result.msgId }

    /**
     * 实例存在即成功。
     */
    override val isSuccess: Boolean
        get() = true

    public val nonce: String? get() = result.nonce

    /**
     * 消息发送时间(服务器时间戳)
     */
    public val timestamp: Timestamp by timestamp { result.msgTimestamp }

    /**
     * 尝试删除（撤回）发送的这条消息。
     */
    @JvmSynthetic
    override suspend fun delete(): Boolean {
        val api =
            if (isDirect) DeleteDirectMessageApi.create(result.msgId)
            else DeleteChannelMessageApi.create(result.msgId)

        return api.requestResultBy(bot).isSuccess
    }

    public companion object {
        internal fun SendMessageResult.asReceipt(
            direct: Boolean,
            bot: KookBot,
        ): KookMessageCreatedReceipt = KookMessageCreatedReceipt(this, direct, bot)
    }

}

/**
 * 消息发送后的回执。
 *
 * 与 [KookMessageCreatedReceipt] 不同的是，
 * [KookApiRequestedReceipt] 很可能并不是通过执行普通的消息api得到的，
 * 例如通过 [KookApiMessage] 执行了一个任意地请求。
 *
 * 也正因此，此回执不支持 [删除][delete] 操作。当使用 [delete] 时将会恒返回 `false`.
 *
 */
public class KookApiRequestedReceipt(
    override val result: Any?,
    override val isDirect: Boolean,
    override val bot: KookBot,
) : SingleKookMessageReceipt() {
    override val id: ID by stringID { random }

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
@ExperimentalSimbotAPI
public class KookAggregatedMessageReceipt private constructor(
    override val bot: KookBot,
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
        return aggregatedMessageReceipt[index] as SingleKookMessageReceipt
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
         * @param bot 如果为null，则会使用 receiver 中的第一个元素中的bot。
         * @throws IllegalArgumentException 如果 receiver 中元素为空
         */
        @JvmStatic
        @JvmOverloads
        public fun Collection<SingleKookMessageReceipt>.merge(bot: KookBot? = null): KookMessageReceipt {
            Simbot.require(isNotEmpty()) { "Unable to merge empty element iterator" }
            return KookAggregatedMessageReceipt(bot ?: first().bot, aggregation())
        }
    }

}
