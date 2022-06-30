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

package love.forte.simbot.kook.api.message

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import love.forte.simbot.ID
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookApiResult
import love.forte.simbot.kook.api.KookGetRequest
import love.forte.simbot.kook.api.appendIfNotnull
import love.forte.simbot.kook.api.message.DirectMessageListRequest.Key.byChatCode
import love.forte.simbot.kook.api.message.DirectMessageListRequest.Key.byTargetId

/**
 * [获取私信聊天消息列表](https://developer.kook.cn/doc/http/direct-message#%E8%8E%B7%E5%8F%96%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF%E5%88%97%E8%A1%A8)
 *
 * method: GET
 *
 * @see byChatCode
 * @see byTargetId
 */
public class DirectMessageListRequest private constructor(
    /**
     * 私信会话 Code。chat_code 与 target_id 必须传一个
     */
    private val chatCode: ID?,
    /**
     * 目标用户 id，后端会自动创建会话。有此参数之后可不传 chat_code参数
     */
    private val targetId: ID?,
    /**
     * 参考消息 id，不传则默认为最新的消息 id
     */
    private val msgId: ID? = null,
    /**
     * 查询模式，有三种模式可以选择。不传则默认查询最新的消息
     */
    private val flag: MessageListFlag? = null,
) : KookGetRequest<KookApiResult.ListData<DirectMessageDetails>>() {
    public companion object Key : BaseKookApiRequestKey("direct-message", "list") {
        @JvmStatic
        @JvmOverloads
        @JvmName("getInstanceByChatCode")
        public fun byChatCode(
            chatCode: ID,
            msgId: ID? = null,
            flag: MessageListFlag? = null,
        ): DirectMessageListRequest {
            return DirectMessageListRequest(
                chatCode = chatCode, targetId = null, msgId = msgId, flag = flag
            )
        }

        @JvmStatic
        @JvmOverloads
        @JvmName("getInstanceByTargetId")
        public fun byTargetId(
            targetId: ID,
            msgId: ID? = null,
            flag: MessageListFlag? = null,
        ): DirectMessageListRequest {
            return DirectMessageListRequest(
                chatCode = null, targetId = targetId, msgId = msgId, flag = flag
            )
        }
    }

    override val resultDeserializer: DeserializationStrategy<out KookApiResult.ListData<DirectMessageDetails>>
        get() = KookApiResult.ListData.serializer(DirectMessageDetails.serializer)

    override val apiPaths: List<String> get() = apiPathList

    override fun ParametersBuilder.buildParameters() {
        appendIfNotnull("chat_code", chatCode)
        appendIfNotnull("target_id", targetId)
        appendIfNotnull("msg_id", msgId)
        appendIfNotnull("flag", flag) { it.name.lowercase() }
    }
}
