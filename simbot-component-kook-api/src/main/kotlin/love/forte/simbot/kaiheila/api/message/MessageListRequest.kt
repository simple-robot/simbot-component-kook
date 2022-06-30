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

package love.forte.simbot.kaiheila.api.message

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import love.forte.simbot.ID
import love.forte.simbot.kaiheila.api.BaseKookApiRequestKey
import love.forte.simbot.kaiheila.api.KookApiResult
import love.forte.simbot.kaiheila.api.KookGetRequest
import love.forte.simbot.kaiheila.api.appendIfNotnull

/**
 * [获取频道聊天消息列表](https://developer.kaiheila.cn/doc/http/message#%E8%8E%B7%E5%8F%96%E9%A2%91%E9%81%93%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF%E5%88%97%E8%A1%A8)
 *
 * @author ForteScarlet
 */
public class MessageListRequest(
    /**
     * 频道 id
     */
    private val targetId: ID,

    /**
     * 参考消息 id，不传则默认为最新的消息 id
     */
    private val msgId: ID? = null,

    /**
     * 是否查询置顶消息（置顶消息只支持查询最新的消息）。
     */
    private val pin: Boolean = false,

    /**
     * 查询模式，有三种模式可以选择。不传则默认查询最新的消息.
     *
     * @see MessageListFlag
     */
    private val flag: MessageListFlag? = null,

    /**
     * 当前分页消息数量, 如果小于等于零则不提供此参数。服务器此参数默认 50
     */
    private val pageSize: Int = -1
) : KookGetRequest<KookApiResult.ListData<ChannelMessageDetails>>() {
    public companion object Key : BaseKookApiRequestKey("message", "list") {
        private val serializer = KookApiResult.ListData.serializer(ChannelMessageDetailsImpl.serializer())
    }

    override val apiPaths: List<String>
        get() = apiPathList

    override val resultDeserializer: DeserializationStrategy<out KookApiResult.ListData<ChannelMessageDetails>>
        get() = serializer

    override fun ParametersBuilder.buildParameters() {
        append("target_id", targetId.toString())
        appendIfNotnull("msgId", msgId)
        appendIfNotnull("pin", if (pin) 1 else 0)
        appendIfNotnull("flag", flag) { it.name.lowercase() }
        appendIfNotnull("msg_id", msgId)
        if (pageSize > 0) {
            append("page_size", pageSize.toString())
        }
    }
}