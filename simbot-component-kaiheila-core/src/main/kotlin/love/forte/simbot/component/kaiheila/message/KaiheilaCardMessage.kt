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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.kaiheila.objects.Card
import love.forte.simbot.message.Message
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.kaiheila.objects.CardMessage as KhlCardMessage


/**
 * 将 [Card] 作为消息使用。
 * @author ForteScarlet
 */
@ExperimentalSimbotApi
@SerialName("khl.card")
@Serializable
public data class KaiheilaCardMessage(public val cards: KhlCardMessage) : KaiheilaMessageElement<KaiheilaCardMessage> {
    override val key: Message.Key<KaiheilaCardMessage>
        get() = Key

    public companion object Key : Message.Key<KaiheilaCardMessage> {
        override fun safeCast(value: Any): KaiheilaCardMessage? = doSafeCast(value)


        /**
         * 将 [Card] 作为 [KaiheilaCardMessage] 使用。
         */
        @JvmStatic
        @ExperimentalSimbotApi
        public fun KhlCardMessage.asMessage(): KaiheilaCardMessage = KaiheilaCardMessage(this)
    }
}