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
import love.forte.simbot.kaiheila.api.message.*
import love.forte.simbot.message.*
import love.forte.simbot.message.Message

/**
 * 提供开黑啦组件中一些会用到的信息。
 *
 * @author ForteScarlet
 */
@Suppress("MemberVisibilityCanBePrivate")
public object KaiheilaMessages {

    /**
     * 当at(mention)的目标为用户时，[At.atType] 所使用的值。[AT_TYPE_USER] 也是 [At.atType] 的默认值。
     */
    public const val AT_TYPE_USER: String = "user"

    /**
     * 当at(mention)的目标为角色时，[At.atType] 所使用的值。
     */
    public const val AT_TYPE_ROLE: String = "role"

}

/**
 * 将一个 [Message] 转化为用于发送消息的请求api。
 *
 * 如果当前 [Message] 是一个消息链，则只会取**最后一个**符合条件的值。
 *
 */
public fun Message.toRequest(
    targetId: ID,
    quote: ID? = null,
    nonce: String? = null,
    tempTargetId: ID? = null,
): MessageCreateRequest? {
    when (this) {
        is Message.Element<*> -> return elementToRequestOrNull(targetId, quote, nonce, tempTargetId)
        is Messages -> {
            for (i in this.indices.reversed()) {
                val element = this[i]
                val request = element.elementToRequestOrNull(targetId, quote, nonce, tempTargetId)
                if (request != null) return request
            }
            return null
        }
        // SingleMessage，开黑啦中无支持类型
        else -> return null
    }
}


@OptIn(ExperimentalSimbotApi::class)
private fun Message.Element<*>.elementToRequestOrNull(
    targetId: ID,
    quote: ID? = null,
    nonce: String? = null,
    tempTargetId: ID? = null,
): MessageCreateRequest? {
    fun request(type: Int, content: String): MessageCreateRequest {
        return MessageCreateRequest(
            type = type,
            targetId = targetId,
            content = content,
            quote = quote,
            nonce = nonce,
            tempTargetId = tempTargetId,

            )
    }
    return when (this) {
        // 文本消息
        is PlainText<*> -> request(MessageType.TEXT.type, text)

        is KaiheilaMessageElement<*> -> when (this) {
            // 媒体资源
            is AssetMessage<*> -> request(type, asset.url)
            // KMarkdown
            is KMarkdownMessage -> request(MessageType.KMARKDOWN.type, kMarkdown.rawContent)
            // card message
            is CardMessage -> request(MessageType.CARD.type, cards.decode())

            // other, ignore.
            else -> null
        }

        // 任意图片类型
        is Image<*> -> request(MessageType.IMAGE.type, id.literal)


        else -> null
    }
}

/**
 * 将一个 [Message] 转化为用于发送消息的请求api。
 *
 * 如果当前 [Message] 是一个消息链，则只会取**最后一个**符合条件的值。
 *
 */
public fun Message.toDirectRequest(): DirectMessageCreateRequest? {

    TODO()
}
