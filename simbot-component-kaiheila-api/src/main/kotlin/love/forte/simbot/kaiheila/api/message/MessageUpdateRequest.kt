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
package love.forte.simbot.kaiheila.api.message

import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.api.*

/**
 * [更新频道聊天消息](https://developer.kaiheila.cn/doc/http/message#%E6%9B%B4%E6%96%B0%E9%A2%91%E9%81%93%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF)
 *
 * *无返回参数*
 */
public class MessageUpdateRequest(
    /**
     * 消息 id
     */
    private val msgId: ID,

    /**
     * 消息内容
     */
    private val content: String,

    /**
     * 回复某条消息的 msgId。如果为空
     * （为空大概指空字符串：`""`，可以使用 [CharSequenceID.EMPTY] 或者 `"".ID` ），
     * 则代表删除回复，不传则无影响。
     */
    private val quote: ID? = null,

    /**
     * 用户 id，针对特定用户临时更新消息，必须是正常消息才能更新。与发送临时消息概念不同，但同样不保存数据库。
     */
    private val tempTargetId: ID? = null,
) : KaiheilaPostRequest<Unit>() {
    public companion object Key : BaseApiRequestKey("message", "update")

    override val resultDeserializer: DeserializationStrategy<out Unit> get() = Unit.serializer()
    override val apiPaths: List<String> get() = apiPathList
    override fun createBody(): Any = Body(msgId, content, quote, tempTargetId)

    @Serializable
    private data class Body(
        @SerialName("msg_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val msgId: ID,
        val content: String,
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val quote: ID? = null,
        @SerialName("temp_target_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val tempTargetId: ID? = null,
    )
}


/**
 * 通过 [MessageCreated] 构建 [MessageUpdateRequest] 实例。
 *
 * @see MessageCreated.toDirectUpdate
 */
public fun MessageCreated.toUpdate(
    content: String,
    quote: ID? = null
): MessageUpdateRequest =
    MessageUpdateRequest(msgId, content, quote)