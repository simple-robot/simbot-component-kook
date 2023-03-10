/*
 * Copyright (c) 2021-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
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
public class ChannelCreateRequest internal constructor(
    private val guildId: ID,
    private val name: String,
    private val type: Int,
    private val parentId: ID?,
    private val limitAmount: Int,
    private val voiceQuality: Int,
) : KookPostRequest<Channel>() {
    @Suppress("MemberVisibilityCanBePrivate")
    public companion object Key : BaseKookApiRequestKey("channel", "create") {
        
        /** 语音品质：流畅 */
        public const val VOICE_QUALITY_SMOOTH: Int = 1
        /** 语音品质：正常 */
        public const val VOICE_QUALITY_NORMAL: Int = 2
        /** 语音品质：高品质 */
        public const val VOICE_QUALITY_HIGH: Int = 3
        
        
        /**
         * 构建 [ChannelCreateRequest].
         * @param guildId 服务器id
         * @param name 频道名称
         * @param type 频道类型，1 文字，2 语音，默认为文字
         * @param parentId 父分组id
         * @param limitAmount 语音频道人数限制，最大99
         * @param voiceQuality 语音音质，默认为2。
         * - 流畅: 1（[VOICE_QUALITY_SMOOTH]）
         * - 正常: 2（[VOICE_QUALITY_NORMAL]）
         * - 高质量: 3（[VOICE_QUALITY_HIGH]）
         */
        @JvmOverloads
        @JvmStatic
        public fun create(
            guildId: ID,
            name: String,
            type: Int = 1,
            parentId: ID? = null,
            limitAmount: Int = 99,
            voiceQuality: Int = VOICE_QUALITY_NORMAL,
        ): ChannelCreateRequest = ChannelCreateRequest(guildId, name, type, parentId, limitAmount, voiceQuality)
        
    }
    
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

