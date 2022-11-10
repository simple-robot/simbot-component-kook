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
import love.forte.simbot.component.kook.message.KookAggregationMessageReceipt.Companion.merge
import love.forte.simbot.component.kook.message.KookMessageCreatedReceipt.Companion.asReceipt
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.kook.api.KookApiRequest
import love.forte.simbot.kook.api.asset.AssetCreateRequest
import love.forte.simbot.kook.api.message.DirectMessageCreateRequest
import love.forte.simbot.kook.api.message.MessageCreateRequest
import love.forte.simbot.kook.api.message.MessageCreated
import love.forte.simbot.kook.api.message.MessageType
import love.forte.simbot.kook.objects.AtTarget
import love.forte.simbot.kook.objects.KMarkdownBuilder
import love.forte.simbot.message.*
import love.forte.simbot.resources.Resource.Companion.toResource
import java.net.URL


private fun createRequest(
    type: Int,
    content: String,
    targetId: ID,
    quote: ID?,
    nonce: String?,
    tempTargetId: ID?,
): KookApiRequest<*> {
    return MessageCreateRequest(
        type = type,
        targetId = targetId,
        content = content,
        quote = quote,
        nonce = nonce,
        tempTargetId = tempTargetId,
    )
}

private const val NOT_DIRECT = 0
private const val DIRECT_TYPE_BY_TARGET = 1
private const val DIRECT_TYPE_BY_CODE = 2

/**
 * 将消息发送给目标。此消息如果是个消息链，则有可能会被拆分为多条消息发送，
 * 届时将会返回 [KookAggregationMessageReceipt].
 *
 * 消息的发送会更**倾向于**整合为一条或较少条消息，如果出现多条消息，
 * 则 [quote] 会只被**第一条**消息所使用，而 [nonce] 和 [tempTargetId] 则会重复使用。
 *
 *
 * @return 消息最终的发送结果回执。如果为 null 则代表没有有效消息发送。
 */
public suspend fun Message.sendToChannel(
    bot: KookComponentBot,
    targetId: ID,
    quote: ID? = null,
    nonce: String? = null,
    tempTargetId: ID? = null,
): KookMessageReceipt? = send0(bot, targetId, NOT_DIRECT, quote, nonce, tempTargetId)


/**
 * 将消息发送给目标。此消息如果是个消息链，则有可能会被拆分为多条消息发送，
 * 届时将会返回 [KookAggregationMessageReceipt].
 *
 * 消息的发送会更**倾向于**整合为一条或较少条消息，如果出现多条消息，
 * 则 [quote] 会只被**第一条**消息所使用，而 [nonce] 和 [tempTargetId] 则会重复使用。
 *
 *
 * @return 消息最终的发送结果回执。如果为 null 则代表没有有效消息发送。
 */
public suspend fun Message.sendToDirectByTargetId(
    bot: KookComponentBot,
    targetId: ID,
    quote: ID? = null,
    nonce: String? = null,
    tempTargetId: ID? = null,
): KookMessageReceipt? = send0(bot, targetId, DIRECT_TYPE_BY_TARGET, quote, nonce, tempTargetId)

/**
 * 将消息发送给目标。此消息如果是个消息链，则有可能会被拆分为多条消息发送，
 * 届时将会返回 [KookAggregationMessageReceipt].
 *
 * 消息的发送会更**倾向于**整合为一条或较少条消息，如果出现多条消息，
 * 则 [quote] 会只被**第一条**消息所使用，而 [nonce] 和 [tempTargetId] 则会重复使用。
 *
 *
 * @return 消息最终的发送结果回执。如果为 null 则代表没有有效消息发送。
 */
public suspend fun Message.sendToDirectByChatCode(
    bot: KookComponentBot,
    chatCode: ID,
    quote: ID? = null,
    nonce: String? = null,
    tempTargetId: ID? = null,
): KookMessageReceipt? = send0(bot, chatCode, DIRECT_TYPE_BY_CODE, quote, nonce, tempTargetId)


/**
 * 将消息发送给目标。此消息如果是个消息链，则有可能会被拆分为多条消息发送，
 * 届时将会返回 [KookAggregationMessageReceipt].
 *
 * 消息的发送会更**倾向于**整合为一条或较少条消息，如果出现多条消息，
 * 则 [quote] 会只被**第一条**消息所使用，而 [nonce] 和 [tempTargetId] 则会重复使用。
 *
 *
 * @return 消息最终的发送结果回执。如果为 null 则代表没有有效消息发送。
 */
@OptIn(ExperimentalSimbotApi::class)
private suspend fun Message.send0(
    bot: KookComponentBot,
    targetId: ID,
    directType: Int,
    quote: ID? = null,
    nonce: String? = null,
    tempTargetId: ID? = null,
): KookMessageReceipt? {
    var quote0 = quote
    fun doRequest(type: Int, content: String): KookApiRequest<*> {
        return when (directType) {
            NOT_DIRECT -> MessageCreateRequest(
                type = type,
                targetId = targetId,
                content = content,
                quote = quote0,
                nonce = nonce,
                tempTargetId = tempTargetId,
            )
            
            DIRECT_TYPE_BY_TARGET -> DirectMessageCreateRequest.byTargetId(targetId, content, type, quote0, nonce)
            DIRECT_TYPE_BY_CODE -> DirectMessageCreateRequest.byChatCode(targetId, content, type, quote0, nonce)
            else -> throw SimbotIllegalArgumentException("Unknown direct type: $directType")
        }.also {
            quote0 = null
        }
    }
    
    val message = this
    
    var kMarkdownBuilder: KMarkdownBuilder? = null
    
    val requests: List<KookApiRequest<*>> = buildList(if (this is Message.Element<*>) 1 else (this as Messages).size) {
        // 清算 kmd
        fun liquidationKmd() {
            kMarkdownBuilder?.let { kmb ->
                add(doRequest(MessageType.KMARKDOWN.type, kmb.buildRaw()))
                kMarkdownBuilder = null
            }
        }
        
        suspend fun process(isSingle: Boolean, message: Message.Element<*>) {
            when (message) {
                is KookRequestMessage -> {
                    liquidationKmd()
                    add(message.request)
                }
                
                else -> {
                    message.elementToRequest(bot, isSingle, { type, content ->
                        liquidationKmd()
                        add(doRequest(type, content))
                    }) { block ->
                        block(kMarkdownBuilder ?: KMarkdownBuilder().also { kMarkdownBuilder = it })
                    }
                }
            }
        }
        
        when (message) {
            is Message.Element<*> -> process(true, message)
            
            is Messages -> {
                if (message.isNotEmpty()) {
                    val isSingle = message.size == 1
                    message.forEach { m ->
                        process(isSingle, m)
                    }
                }
            }
        }
        
        liquidationKmd()
    }
    
    
    /*
        return if (result is MessageCreated) {
                result.asReceipt(false, baseBot)
            } else {
                KookApiRequestedReceipt(result, false, baseBot)
            }
     */
    
    fun Any?.toReceipt(): KookMessageReceipt {
        return if (this is MessageCreated) {
            this.asReceipt(false, bot)
        } else {
            KookApiRequestedReceipt(this, false, bot)
        }
    }
    
    when {
        requests.isEmpty() -> {
            return null
        }
        
        requests.size == 1 -> {
            return requests.first().requestDataBy(bot).toReceipt()
        }
        
        else -> {
            return requests.map { req -> req.requestDataBy(bot).toReceipt() }.merge(bot = bot)
        }
    }
}


/**
 * 尝试将一个单独的消息元素转化为用于发送消息的请求。
 *
 * 如果 [isSingleElement] 为true，则消息元素类型为 [PlainText] 时将会使用 `doRequest(MessageType.TEXT.type, message.text)` 发送而不使用KMarkdown。
 *
 * 不需要处理 [KookRequestMessage], 外层自行处理。
 *
 */
@OptIn(ExperimentalSimbotApi::class)
private suspend inline fun Message.Element<*>.elementToRequest(
    bot: KookComponentBot,
    
    /**
     * 实际上的消息元素数量。如果需要发送的消息只有一个，那么处理时可能会选择直接使用 doRequest 而不是拼接内容到 kmd 中。
     *
     * 至少为1。
     */
    isSingleElement: Boolean,
    
    /**
     * 得到（进行）一次请求
     */
    doRequest: (type: Int, content: String) -> Unit,
    
    /**
     * 将信息填充到 kmd 中。kmd的发送是自动的，其产生于：
     * 执行中途的 doRequest 之前和全部元素遍历之后。
     *
     * 当发生过一次请求的生成后，withinKmd中的构建器将会是一个新的构建器。
     *
     */
    withinKmd: (KMarkdownBuilder.() -> Unit) -> Unit,
) {
    val message = this
    
    when (message) {
        // 文本消息
        is PlainText<*> -> {
            if (isSingleElement) {
                doRequest(MessageType.TEXT.type, message.text)
            } else {
                withinKmd {
                    text(message.text)
                }
            }
        }
        
        is KookMessageElement<*> -> when (message) {
            // 媒体资源
            is KookAssetMessage<*> -> doRequest(message.type, message.asset.url)
            // KMarkdown
            is KookKMarkdownMessage -> doRequest(MessageType.KMARKDOWN.type, message.kMarkdown.rawContent)
            // card message
            is KookCardMessage -> doRequest(MessageType.CARD.type, message.cards.encode())
            
            // is KookRequestMessage -> {
            //     this.request
            // }
            
            is KookAtAllHere -> {
                withinKmd {
                    at(AtTarget.Here)
                }
            }
            
            is KookAttachmentMessage -> {
                val type = when (val attachmentType = message.attachment.type.lowercase()) {
                    "file" -> MessageType.FILE.type
                    "image" -> MessageType.IMAGE.type
                    "video" -> MessageType.VIDEO.type
                    else -> throw SimbotIllegalArgumentException("Unknown attachment type: $attachmentType")
                }
                
                // TODO just send, waiting for fix.
                
                val createRequest = AssetCreateRequest(URL(message.attachment.url).toResource(message.attachment.name))
                val asset = createRequest.requestDataBy(bot)
                
                doRequest(type, asset.url)
            }
            
            else -> {
                // other, ignore.
            }
        }
        
        // 需要上传的图片
        is ResourceImage -> {
            val asset = AssetCreateRequest(message.resource()).requestDataBy(bot)
            doRequest(MessageType.IMAGE.type, asset.url)
        }
        
        // 其他任意图片类型, 直接使用id
        is Image<*> -> doRequest(MessageType.IMAGE.type, message.id.literal)
        
        // std at
        is At -> {
            withinKmd {
                when (message.type) {
                    KookMessages.AT_TYPE_CHANNEL -> channel(message.target.literal)
                    KookMessages.AT_TYPE_ROLE -> role(message.target.literal)
                    KookMessages.AT_TYPE_USER -> at(message.target.literal)
                    else -> at(message.target.literal)
                }
            }
        }
        
        is Face -> {
            // TODO guild emoji..?
            //  serverEmoticons?
        }
        
        is Emoji -> {
            withinKmd {
                emoji(message.id.literal)
                
            }
        }
        
        
        else -> {
            // other..?
        }
    }
}





