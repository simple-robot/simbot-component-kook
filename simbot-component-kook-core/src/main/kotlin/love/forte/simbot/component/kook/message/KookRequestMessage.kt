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

import love.forte.simbot.kook.api.KookApiRequest
import love.forte.simbot.kook.api.message.MessageCreateRequest
import love.forte.simbot.message.Message
import love.forte.simbot.message.doSafeCast

/**
 * 提供一个 [KookApiRequest] 作为原始的消息发送请求（例如 [MessageCreateRequest]）。
 *
 * 此消息会直接使用 [request] 作为消息发送的请求。
 *
 * 这是一个**仅用于发送**的消息，且**不支持**序列化。
 *
 * @see KookApiRequest
 *
 * @author ForteScarlet
 */
@KookSendOnlyMessage
public data class KookRequestMessage(public val request: KookApiRequest<*>) :
    KookMessageElement<KookRequestMessage> {

    override val key: Message.Key<KookRequestMessage>
        get() = Key

    public companion object Key : Message.Key<KookRequestMessage> {
        override fun safeCast(value: Any): KookRequestMessage? = doSafeCast(value)

        /**
         * 通过 [KookRequestMessage] 构建 [KookRequestMessage].
         */
        @JvmStatic
        public fun KookApiRequest<*>.toRequest(): KookRequestMessage = KookRequestMessage(this)

    }
}

