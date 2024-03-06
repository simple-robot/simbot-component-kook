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

import io.ktor.client.request.forms.*
import io.ktor.utils.io.streams.*
import love.forte.simbot.annotations.ExperimentalSimbotAPI
import love.forte.simbot.SimbotIllegalArgumentException
import love.forte.simbot.component.kook.bot.KookBot
import love.forte.simbot.component.kook.message.KookAggregatedMessageReceipt.Companion.merge
import love.forte.simbot.component.kook.message.KookMessageCreatedReceipt.Companion.asReceipt
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.kook.api.KookApi
import love.forte.simbot.kook.api.asset.CreateAssetApi
import love.forte.simbot.kook.api.message.SendChannelMessageApi
import love.forte.simbot.kook.api.message.SendDirectMessageApi
import love.forte.simbot.kook.api.message.SendMessageResult
import love.forte.simbot.kook.messages.MessageType
import love.forte.simbot.kook.objects.kmd.AtTarget
import love.forte.simbot.kook.objects.kmd.KMarkdownBuilder
import love.forte.simbot.literal
import love.forte.simbot.message.*
import love.forte.simbot.resources.ByteArrayResource
import love.forte.simbot.resources.FileResource
import love.forte.simbot.resources.PathResource
import love.forte.simbot.resources.URLResource
import love.forte.simbot.utils.view.isNotEmpty
import java.net.URL


private fun createRequest(
    type: Int,
    content: String,
    targetId: String,
    quote: String?,
    nonce: String?,
    tempTargetId: String?,
): KookApi<*> {
    return SendChannelMessageApi.create(
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
 * 届时将会返回 [KookAggregatedMessageReceipt].
 *
 * 消息的发送会更**倾向于**整合为一条或较少条消息，如果出现多条消息，
 * 则 [quote] 会只被**第一条**消息所使用，而 [nonce] 和 [tempTargetId] 则会重复使用。
 *
 *
 * @return 消息最终的发送结果回执。如果为 null 则代表没有有效消息发送。
 */
public suspend fun Message.sendToChannel(
    bot: KookBot,
    targetId: String,
    quote: String? = null,
    nonce: String? = null,
    tempTargetId: String? = null,
    defaultTempTargetId: String? = null,
): KookMessageReceipt? = send0(bot, targetId, NOT_DIRECT, quote, nonce, tempTargetId, defaultTempTargetId)


/**
 * 将消息发送给目标。此消息如果是个消息链，则有可能会被拆分为多条消息发送，
 * 届时将会返回 [KookAggregatedMessageReceipt].
 *
 * 消息的发送会更**倾向于**整合为一条或较少条消息，如果出现多条消息，
 * 则 [quote] 会只被**第一条**消息所使用，而 [nonce] 和 [tempTargetId] 则会重复使用。
 *
 *
 * @return 消息最终的发送结果回执。如果为 null 则代表没有有效消息发送。
 */
public suspend fun Message.sendToDirectByTargetId(
    bot: KookBot,
    targetId: String,
    quote: String? = null,
    nonce: String? = null,
    tempTargetId: String? = null,
): KookMessageReceipt? = send0(bot, targetId, DIRECT_TYPE_BY_TARGET, quote, nonce, tempTargetId)

/**
 * 将消息发送给目标。此消息如果是个消息链，则有可能会被拆分为多条消息发送，
 * 届时将会返回 [KookAggregatedMessageReceipt].
 *
 * 消息的发送会更**倾向于**整合为一条或较少条消息，如果出现多条消息，
 * 则 [quote] 会只被**第一条**消息所使用，而 [nonce] 和 [tempTargetId] 则会重复使用。
 *
 *
 * @return 消息最终的发送结果回执。如果为 null 则代表没有有效消息发送。
 */
public suspend fun Message.sendToDirectByChatCode(
    bot: KookBot,
    chatCode: String,
    quote: String? = null,
    nonce: String? = null,
    tempTargetId: String? = null,
): KookMessageReceipt? = send0(bot, chatCode, DIRECT_TYPE_BY_CODE, quote, nonce, tempTargetId)


/**
 * 将消息发送给目标。此消息如果是个消息链，则有可能会被拆分为多条消息发送，
 * 届时将会返回 [KookAggregatedMessageReceipt].
 *
 * 消息的发送会更**倾向于**整合为一条或较少条消息，如果出现多条消息，
 * 则 [quote] 会只被**第一条**消息所使用，而 [nonce] 和 [tempTargetId] 则会重复使用。
 *
 * @param defaultTempTargetId 如果存在 [KookTempTarget]
 *
 * @return 消息最终的发送结果回执。如果为 null 则代表没有有效消息发送。
 */
@OptIn(ExperimentalSimbotAPI::class)
private suspend fun Message.send0(
    bot: KookBot,
    targetId: String,
    directType: Int,
    quote: String? = null,
    nonce: String? = null,
    tempTargetId: String? = null,
    defaultTempTargetId: String? = null,
): KookMessageReceipt? {
    data class TempTargetIdWrapper(var tempTargetId: String?)

//    var quote0 = quote

    fun doRequest(type: Int, content: String, nonce: String?, quote: String?, tempTargetId: String?): KookApi<*> {
        return when (directType) {
            NOT_DIRECT -> SendChannelMessageApi.create(
                type = type,
                targetId = targetId,
                content = content,
                quote = quote,
                nonce = nonce,
                tempTargetId = tempTargetId
            )

            DIRECT_TYPE_BY_TARGET -> SendDirectMessageApi.createByTargetId(targetId, content, type, quote, nonce)
            DIRECT_TYPE_BY_CODE -> SendDirectMessageApi.createByChatCode(targetId, content, type, quote, nonce)
            else -> throw SimbotIllegalArgumentException("Unknown direct type: $directType")
        }
    }

    val message = this
    var kMarkdownBuilder: KMarkdownBuilder? = null
    var quote0 = quote
    val tempWrapper = TempTargetIdWrapper(tempTargetId)

//    val requests: List<KookApiRequest<*>> = buildList(if (this is Message.Element<*>) 1 else (this as Messages).size) {
    val requests: List<() -> KookApi<*>> =
        buildList(if (this is Message.Element<*>) 1 else (this as Messages).size) {
            // 清算 kmd
            fun liquidationKmd() {
                kMarkdownBuilder?.let { kmb ->
                    val currentQuote = quote0
                    quote0 = null
                    add {
                        doRequest(
                            MessageType.KMARKDOWN.type,
                            kmb.buildRaw(),
                            nonce,
                            currentQuote,
                            tempWrapper.tempTargetId
                        )
                    }
                    kMarkdownBuilder = null
                }
            }

            suspend fun process(isSingle: Boolean, message: Message.Element<*>) {
                when (message) {
                    is KookApiMessage -> {
                        liquidationKmd()
                        add { message.api }
                    }

                    is KookTempTarget -> when (message) {
                        is KookTempTarget.Target -> tempWrapper.tempTargetId = message.id.literal
                        is KookTempTarget.Current -> tempWrapper.tempTargetId = defaultTempTargetId
                    }

                    else -> {
                        message.elementToRequest(bot, isSingle, { type, content ->
                            liquidationKmd()
                            add { doRequest(type, content, nonce, null, tempWrapper.tempTargetId) }
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

    fun Any?.toReceipt(): SingleKookMessageReceipt {
        return if (this is SendMessageResult) {
            this.asReceipt(false, bot)
        } else {
            KookApiRequestedReceipt(this, false, bot)
        }
    }

    return when {
        requests.isEmpty() -> {
            null
        }

        requests.size == 1 -> {
            requests.first()().requestDataBy(bot).toReceipt()
        }

        else -> {
            requests.map { req -> req().requestDataBy(bot).toReceipt() }.merge(bot = bot)
        }
    }
}


/**
 * 尝试将一个单独的消息元素转化为用于发送消息的请求。
 *
 * 如果 [isSingleElement] 为 `true`，则消息元素类型为 [PlainText] 时将会使用 `doRequest(MessageType.TEXT.type, message.text)` 发送而不使用KMarkdown。
 *
 * 不需要处理 [KookApiMessage], 外层自行处理。
 *
 */
@OptIn(ExperimentalSimbotAPI::class)
private suspend inline fun Message.Element<*>.elementToRequest(
    bot: KookBot,

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
    when (val message = this) {
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

            is AtAll -> {
                withinKmd {
                    at(AtTarget.All)
                }
            }

            is KookAtAllHere -> {
                withinKmd {
                    at(AtTarget.Here)
                }
            }

            is KookAttachmentMessage -> {
                val type: MessageType = when (message) {
                    is KookAttachment -> when (val attType = message.attachment.type.lowercase()) {
                        "file" -> MessageType.FILE
                        "image" -> MessageType.IMAGE
                        "video" -> MessageType.VIDEO
                        else -> throw IllegalArgumentException("Unknown attachment type: $attType")
                    }

                    is KookAttachmentFile -> MessageType.FILE
                    is KookAttachmentImage -> MessageType.IMAGE
                    is KookAttachmentVideo -> MessageType.VIDEO
                }

                // TODO just re-upload and send, waiting for fix.
                //  see https://github.com/simple-robot/simbot-component-kook/issues/75

                val createRequest =
                    CreateAssetApi.create(URL(message.attachment.url), message.attachment.name)
                val asset = createRequest.requestDataBy(bot)

                doRequest(type.type, asset.url)
            }

            else -> {
                // other, ignore.
            }
        }

        // 需要上传的图片
        is ResourceImage -> {
            val assetApi: CreateAssetApi = when (val resource = message.resource()) {
                is URLResource -> CreateAssetApi.create(resource.url)
                is FileResource -> CreateAssetApi.create(resource.file)
                is PathResource -> CreateAssetApi.create(resource.path)
                is ByteArrayResource -> CreateAssetApi.create(resource.bytes)
                else -> CreateAssetApi.create(InputProvider { resource.openStream().asInput() }, resource.name)
            }

            val asset = assetApi.requestDataBy(bot)
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
            // TODO serverEmoticons?
//            withinKmd {
//                serverEmoticons(message.id.literal)
//            }
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





