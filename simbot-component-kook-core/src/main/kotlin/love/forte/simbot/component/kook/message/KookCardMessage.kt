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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.component.kook.message.KookCardMessage.Key.asMessage
import love.forte.simbot.kook.objects.card.Card
import love.forte.simbot.kook.objects.card.CardMessageBuilder
import love.forte.simbot.kook.objects.card.buildCardMessage
import love.forte.simbot.message.Message
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.kook.objects.card.CardMessage as KkCardMessage


/**
 * 将 [Card] 作为消息使用。
 * @author ForteScarlet
 */
@ExperimentalSimbotApi
@SerialName("kook.card")
@Serializable
public data class KookCardMessage(public val cards: KkCardMessage) : KookMessageElement<KookCardMessage> {
    override val key: Message.Key<KookCardMessage>
        get() = Key

    public companion object Key : Message.Key<KookCardMessage> {
        override fun safeCast(value: Any): KookCardMessage? = doSafeCast(value)

        /**
         * 将 [Card] 作为 [KookCardMessage] 使用。
         */
        @JvmStatic
        @ExperimentalSimbotApi
        public fun KkCardMessage.asMessage(): KookCardMessage = KookCardMessage(this)
    }
}

/**
 * 通过 [buildCardMessage] 构建 [CardMessage][KkCardMessage] 并包装为 [KookCardMessage]。
 *
 * @see KookCardMessage
 * @see buildCardMessage
 */
@ExperimentalSimbotApi
public inline fun kookCard(action: CardMessageBuilder.() -> Unit): KookCardMessage {
    return buildCardMessage(action).asMessage()
}
