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
import love.forte.simbot.kook.api.message.DirectMessageCreateRequest
import love.forte.simbot.kook.api.message.MessageCreateRequest
import love.forte.simbot.kook.api.message.MessageType
import love.forte.simbot.kook.objects.AtTarget
import love.forte.simbot.kook.objects.KMarkdown
import love.forte.simbot.kook.objects.buildRawKMarkdown
import love.forte.simbot.message.*
import love.forte.simbot.resources.Resource.Companion.toResource
import java.net.URL

/**
 * 提供 Kook 组件中一些会用到的信息。
 *
 * @author ForteScarlet
 */
@Suppress("MemberVisibilityCanBePrivate")
public object KookMessages {
    
    /**
     * 当at(mention)的目标为用户时，[At.atType] 所使用的值。[AT_TYPE_USER] 也是 [At.atType] 的默认值。
     */
    public const val AT_TYPE_USER: String = "user"
    
    /**
     * 当at(mention)的目标为角色时，[At.atType] 所使用的值。
     */
    public const val AT_TYPE_ROLE: String = "role"
    
    /**
     * 当at(mention)的目标为频道时。用于使用 [KMarkdown] 类型发送的时候。
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

/**
 * 将一个 [Message] 转化为用于发送消息的请求api。
 *
 * 如果当前 [Message] 是一个消息链，则可能会根据消息类型的情况将消息转化为 `KMarkdown` 类型的消息。
 *
 */
@Deprecated(
    "Use Message.sendToChannel", ReplaceWith(
        "sendToChannel(bot, targetId, quote, nonce, tempTargetId)",
        "love.forte.simbot.component.kook.message.KookMessageReceipt"
    )
)
@OptIn(ExperimentalSimbotApi::class)
public suspend fun Message.toRequest(
    bot: KookComponentBot,
    targetId: ID,
    quote: ID? = null,
    nonce: String? = null,
    tempTargetId: ID? = null,
): KookApiRequest<*>? {
    when (this) {
        is Message.Element<*> -> return elementToRequestOrNull(bot, targetId, quote, nonce, tempTargetId)
        is Messages -> {
            // 只要不是纯文本消息，就使用Kmarkdown？
            // TODO 如果存在at，atAll，atAllRole，
            //  转为kmarkdown消息。
            
            // val content = buildRawKMarkdown {
            //
            // }
            //
            // MessageCreateRequest(
            //     type = MessageType.KMARKDOWN.type,
            //     targetId = targetId,
            //     content = content,
            //     quote = quote,
            //     nonce = nonce,
            //     tempTargetId = tempTargetId
            // )
            
            // for (i in this.indices.reversed()) {
            //
            // }
            
            // buildKMarkdown {
            //
            // }
            
            for (i in this.indices.reversed()) {
                val element = this[i]
                val request = element.elementToRequestOrNull(bot, targetId, quote, nonce, tempTargetId)
                if (request != null) return request
            }
            return null
        }
        // SingleMessage， Kook 中无支持类型
        else -> return null
    }
}


/**
 * 尝试将一个消息元素转化为用于发送消息的请求。
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
            is KookCardMessage -> request(MessageType.CARD.type, cards.encode())
            
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
        
        // 其他任意图片类型
        is Image<*> -> request(MessageType.IMAGE.type, id.literal)
        
        is At -> {
            // buildRawKMarkdown {
            //     // TODO
            // }
            // TODO
            null
        }
        
        is Face -> {
            // TODO
            null
        }
        
        is Emoji -> {
            // TODO
            null
        }
        
        
        else -> null
    }
}


/**
 * 将一个 [Message] 转化为用于发送消息的请求api。
 *
 */
@Deprecated(
    "Use Message.sendToDirectByTargetId", ReplaceWith(
        "sendToDirectByTargetId(bot, targetId, quote, nonce, tempTargetId)",
        "love.forte.simbot.component.kook.message.KookMessageReceipt"
    )
)
public suspend fun Message.toDirectRequest(
    bot: KookComponentBot,
    targetId: ID,
    quote: ID? = null,
    nonce: String? = null,
    tempTargetId: ID? = null,
): DirectMessageCreateRequest? {
    return (sendToChannel(bot, targetId, quote, nonce, tempTargetId) as? MessageCreateRequest)?.toDirect()
}
