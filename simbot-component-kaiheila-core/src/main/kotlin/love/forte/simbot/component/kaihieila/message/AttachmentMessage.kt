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

import kotlinx.serialization.*
import love.forte.simbot.kaiheila.objects.*
import love.forte.simbot.message.*


/**
 *
 * 将 [Attachments] 作为消息对象。
 *
 * 通常在接收时使用。
 *
 * @author ForteScarlet
 */
@SerialName("khl.attachment")
@Serializable
public data class AttachmentMessage(public val attachment: Attachments) : KaiheilaMessageElement<AttachmentMessage> {

    override val key: Message.Key<AttachmentMessage>
        get() = Key

    public companion object Key : Message.Key<AttachmentMessage> {
        override fun safeCast(value: Any): AttachmentMessage? = doSafeCast(value)
    }
}

/**
 * 将 [Attachments] 转化为 [AttachmentMessage]。
 */
public fun Attachments.asMessage(): AttachmentMessage = AttachmentMessage(this)