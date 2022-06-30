/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kook.api.channel

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.ID
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest
import love.forte.simbot.kook.objects.Channel


/**
 *
 * [创建频道](https://developer.kaiheila.cn/doc/http/channel#%E5%88%9B%E5%BB%BA%E9%A2%91%E9%81%93)
 *
 * request method: POST
 *
 * @author ForteScarlet
 */
public class ChannelCreateRequest @JvmOverloads constructor(
    /** 是 服务器id */
    private val guildId: ID,
    /** 是 频道名称 */
    private val name: String,
    /** 否 频道类型，1 文字，2 语音，默认为文字 */
    private val type: Int = 1,
    /** 否 父分组id */
    private val parentId: ID? = null,
    /** 否 语音频道人数限制，最大99 */
    private val limitAmount: Int = 99,
    /** 否 语音音质，默认为2。1流畅，2正常，3高质量 */
    private val voiceQuality: Int = 2,
) : KookPostRequest<Channel>() {
    public companion object Key : BaseKookApiRequestKey("channel", "create")

    init {
        require(type in 1..2) { "The 'type' must be 1(Text) or 2(Voice), but $type" }
        require(limitAmount in 1..99) { "The 'limitAmount' must between 1 and 99, but $limitAmount" }
        require(voiceQuality in 1..3) { "The 'voiceQuality' must between 1 and 3, but $voiceQuality" }
    }

    override val resultDeserializer: DeserializationStrategy<out Channel>
        get() = ChannelView.serializer()

    override val apiPaths: List<String>
        get() = apiPathList

    override fun createBody(): Any = Body(guildId, name, type, parentId, limitAmount, voiceQuality)

    @Serializable
    private data class Body(
        /** 是 服务器id */
        @SerialName("guild_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val guildId: ID,

        /** 是 频道名称 */
        val name: String,

        /** 否 频道类型，1 文字，2 语音，默认为文字 */
        val type: Int = 1,

        /** 否 父分组id */
        @SerialName("parent_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val parentId: ID? = null,

        /** 否 语音频道人数限制，最大99 */
        @SerialName("limit_amount")
        val limitAmount: Int = 99,

        /** 否 语音音质，默认为2。1流畅，2正常，3高质量 */
        @SerialName("voice_quality")
        val voiceQuality: Int = 2,
    )
}

