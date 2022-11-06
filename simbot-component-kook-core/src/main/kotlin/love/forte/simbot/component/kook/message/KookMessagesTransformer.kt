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

import love.forte.simbot.*
import love.forte.simbot.component.kook.KookComponentBot
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.kook.api.KookApiRequest
import love.forte.simbot.kook.api.asset.AssetCreateRequest
import love.forte.simbot.kook.api.message.MessageCreateRequest
import love.forte.simbot.kook.api.message.MessageType
import love.forte.simbot.kook.objects.AtTarget
import love.forte.simbot.kook.objects.KMarkdownBuilder
import love.forte.simbot.kook.objects.buildRawKMarkdown
import love.forte.simbot.message.*
import love.forte.simbot.resources.Resource.Companion.toResource
import java.net.URL

/**
 * 将一个 [Message] 转化为用于发送消息的请求api。
 *
 */
@OptIn(ExperimentalSimbotApi::class)
public suspend fun Message.toRequest0(
    bot: KookComponentBot,
    targetId: ID,
    quote: ID? = null,
    nonce: String? = null,
    tempTargetId: ID? = null,
): KookApiRequest<*>? {
    if (this is Message.Element<*>) {
        return this.elementToRequestOrNull(bot, targetId, quote, nonce, tempTargetId)
    }
    
    
    TODO()
}


/**
 * 尝试将一个单独的消息元素转化为用于发送消息的请求。
 */
@OptIn(ExperimentalSimbotApi::class)
private suspend fun Message.Element<*>.elementToRequestOrNull(
    bot: KookComponentBot,
    targetId: ID,
    quote: ID? = null,
    nonce: String? = null,
    tempTargetId: ID? = null,
): KookApiRequest<*>? {
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
        
        is KookMessageElement<*> -> when (this) {
            // 媒体资源
            is KookAssetMessage<*> -> request(type, asset.url)
            // KMarkdown
            is KookKMarkdownMessage -> request(MessageType.KMARKDOWN.type, kMarkdown.rawContent)
            // card message
            is KookCardMessage -> request(MessageType.CARD.type, cards.decode())
            
            // request message
            is KookRequestMessage -> this.request
            
            is KookAtAllHere -> {
                val content = buildRawKMarkdown {
                    at(AtTarget.Here)
                }
                
                request(MessageType.KMARKDOWN.type, content)
            }
            
            is KookAttachmentMessage -> {
                val attachmentType = attachment.type.lowercase()
                val type = when (attachment.type.lowercase()) {
                    "file" -> MessageType.FILE.type
                    "image" -> MessageType.IMAGE.type
                    "video" -> MessageType.VIDEO.type
                    else -> throw SimbotIllegalArgumentException("Unknown attachment type: $attachmentType")
                }
                
                val createRequest = AssetCreateRequest(URL(attachment.url).toResource(attachment.name))
                val asset = createRequest.requestDataBy(bot)
                
                request(type, asset.url)
            }
            
            
            // other, ignore.
            else -> null
        }
        
        // 需要上传的图片
        is ResourceImage -> {
            val asset = AssetCreateRequest(resource()).requestDataBy(bot)
            request(MessageType.IMAGE.type, asset.url)
        }
        
        // 其他任意图片类型, 直接使用id
        is Image<*> -> request(MessageType.IMAGE.type, id.literal)
        
        is At -> {
            val kmd = buildRawKMarkdown {
                val at = this@elementToRequestOrNull
                when (at.type) {
                    KookMessages.AT_TYPE_CHANNEL -> channel(at.target.literal)
                    KookMessages.AT_TYPE_ROLE -> role(at.target.literal)
                    KookMessages.AT_TYPE_USER -> at(at.target.literal)
                    else -> at(at.target.literal)
                }
            }
            
            request(MessageType.KMARKDOWN.type, kmd)
        }
        
        is Face -> {
            // guild emoji..?
            // val kmd = buildRawKMarkdown {
            //     val face = this@elementToRequestOrNull
            //     // serverEmoticons("", "")
            //
            // }
            // request(MessageType.KMARKDOWN.type, kmd)
            
            null
        }
        
        is Emoji -> {
            val kmd = buildRawKMarkdown {
                val emoji = this@elementToRequestOrNull
                emoji(emoji.id.literal)
                
            }
            
            request(MessageType.KMARKDOWN.type, kmd)
        }
        
        
        else -> null
    }
}


/**
 * 尝试将一个 [Message.Element] 拼接进目标的 KMarkdown 消息。
 */
@ExperimentalSimbotApi
private fun Message.Element<*>.appendToKMarkdownMessage(builder: KMarkdownBuilder): KMarkdownBuilder {
    TODO()
}



