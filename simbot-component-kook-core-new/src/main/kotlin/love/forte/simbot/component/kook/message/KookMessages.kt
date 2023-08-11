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

import love.forte.simbot.ID
import love.forte.simbot.message.At

/**
 * 提供 Kook 组件中一些会用到的信息。
 *
 * @author ForteScarlet
 */
@Suppress("MemberVisibilityCanBePrivate")
public object KookMessages {

    /**
     * 当at(mention)的目标为用户时，[At.type] 所使用的值。[AT_TYPE_USER] 也是 [At.type] 的默认值。
     */
    public const val AT_TYPE_USER: String = "user"

    /**
     * 当at(mention)的目标为角色时，[At.type] 所使用的值。
     */
    public const val AT_TYPE_ROLE: String = "role"

    /**
     * 当at(mention)的目标为频道时。用于使用 `KMarkdown` 类型发送的时候。
     */
    public const val AT_TYPE_CHANNEL: String = "channel"


    /**
     * 构建一个 at(mention) 用户的 [At] 消息对象。
     */
    @JvmStatic
    public fun atUser(id: ID): At = At(target = id, type = AT_TYPE_USER, originContent = "(met)$id(met)")

    /**
     * 构建一个 at(mention) 整个角色的 [At] 消息对象。
     */
    @JvmStatic
    public fun atRole(id: ID): At = At(target = id, type = AT_TYPE_ROLE, originContent = "(rol)$id(rol)")

    /**
     * 构建一个 at(mention) 频道的 [At] 消息对象。
     */
    @JvmStatic
    public fun atChannel(id: ID): At = At(target = id, type = AT_TYPE_CHANNEL, originContent = "(chn)$id(chn)")

}

//
//
///**
// * 尝试将一个消息元素转化为用于发送消息的请求。
// */
//@OptIn(ExperimentalSimbotApi::class)
//private suspend fun Message.Element<*>.elementToRequestOrNull(
//    bot: KookBot,
//    targetId: ID,
//    quote: ID? = null,
//    nonce: String? = null,
//    tempTargetId: ID? = null,
//): KookApi<*>? {
//    fun request(type: Int, content: String): SendChannelMessageApi {
//        return SendChannelMessageApi.create {
//            this.type = type
//            this.targetId = targetId.literal
//            this.content = content
//            this.quote = quote?.literal
//            this.nonce = nonce
//            this.tempTargetId = tempTargetId?.literal
//        }
//    }
//
//    return when (this) {
//        // 文本消息
//        is PlainText<*> -> request(MessageType.TEXT.type, text)
//
//        is KookMessageElement<*> -> when (this) {
//            // 媒体资源
//            is KookAssetMessage<*> -> request(type, asset.url)
//            // KMarkdown
//            is KookKMarkdownMessage -> request(MessageType.KMARKDOWN.type, kMarkdown.rawContent)
//            // card message
//            is KookCardMessage -> request(MessageType.CARD.type, cards.encode())
//
//            // request message
//            is KookRequestMessage -> this.request
//
//            is KookAtAllHere -> {
//                val content = buildRawKMarkdown {
//                    at(AtTarget.Here)
//                }
//
//                request(MessageType.KMARKDOWN.type, content)
//            }
//
//            is KookAttachmentMessage -> {
//                val attachmentType = attachment.type.lowercase()
//                val type = when (attachment.type.lowercase()) {
//                    "file" -> MessageType.FILE.type
//                    "image" -> MessageType.IMAGE.type
//                    "video" -> MessageType.VIDEO.type
//                    else -> throw SimbotIllegalArgumentException("Unknown attachment type: $attachmentType")
//                }
//
//                val createRequest = AssetCreateRequest.create(URL(attachment.url).toResource(attachment.name))
//                val asset = createRequest.requestDataBy(bot)
//
//                request(type, asset.url)
//            }
//
//
//            // other, ignore.
//            else -> null
//        }
//
//        // 需要上传的图片
//        is ResourceImage -> {
//            val asset = AssetCreateRequest.create(resource()).requestDataBy(bot)
//            request(MessageType.IMAGE.type, asset.url)
//        }
//
//        // 其他任意图片类型
//        is Image<*> -> request(MessageType.IMAGE.type, id.literal)
//
//        is At -> {
//            // buildRawKMarkdown {
//            //     // TODO
//            // }
//            // TODO
//            null
//        }
//
//        is Face -> {
//            // TODO
//            null
//        }
//
//        is Emoji -> {
//            // TODO
//            null
//        }
//
//
//        else -> null
//    }
//}
//
//
///**
// * 将一个 [Message] 转化为用于发送消息的请求api。
// *
// */
//@Deprecated(
//    "Use Message.sendToDirectByTargetId", ReplaceWith(
//        "sendToDirectByTargetId(bot, targetId, quote, nonce, tempTargetId)",
//        "love.forte.simbot.component.kook.message.KookMessageReceipt"
//    )
//)
//public suspend fun Message.toDirectRequest(
//    bot: KookComponentBot,
//    targetId: ID,
//    quote: ID? = null,
//    nonce: String? = null,
//    tempTargetId: ID? = null,
//): DirectMessageCreateRequest? {
//    return suspendCoroutine {
//        it.resume(null)
//    }
//}
