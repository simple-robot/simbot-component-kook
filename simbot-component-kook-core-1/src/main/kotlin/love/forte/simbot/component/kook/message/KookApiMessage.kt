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

import love.forte.simbot.kook.api.KookApi
import love.forte.simbot.kook.api.message.SendChannelMessageApi
import love.forte.simbot.message.Message
import love.forte.simbot.message.doSafeCast


/**
 * 提供一个 [KookApi] 作为原始的消息发送请求（例如 [SendChannelMessageApi]）。
 *
 * 此消息会在发送时直接通过 [api] 发起一个请求，但是不会处理它的响应。
 *
 * 默认情况下此 API 请求过程中产生的异常会直接抛出。这可能会影响并中断消息发送的流程。
 *
 * 这是一个**仅用于发送**的消息，且**不支持**序列化。
 *
 * @see KookApi
 *
 * @author ForteScarlet
 */
@KookSendOnlyMessage
public data class KookApiMessage(public val api: KookApi<*>) : KookMessageElement<KookApiMessage> {
    override val key: Message.Key<KookApiMessage>
        get() = Key

    public companion object Key : Message.Key<KookApiMessage> {
        override fun safeCast(value: Any): KookApiMessage? = doSafeCast(value)

        /**
         * 通过 [KookApi] 构建 [KookApiMessage].
         */
        @JvmStatic
        public fun KookApi<*>.toRequest(): KookApiMessage =
            KookApiMessage(this)

    }
}

