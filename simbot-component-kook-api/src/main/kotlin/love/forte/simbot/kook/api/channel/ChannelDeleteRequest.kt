/*
 * Copyright (c) 2022-2023. ForteScarlet.
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
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.ID
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest


/**
 *
 * [删除频道](https://developer.kaiheila.cn/doc/http/channel#%E5%88%A0%E9%99%A4%E9%A2%91%E9%81%93)
 *
 *
 * @author ForteScarlet
 */
public class ChannelDeleteRequest internal constructor(private val channelId: ID) : KookPostRequest<Unit>() {
    public companion object Key : BaseKookApiRequestKey("channel", "delete") {
    
        /**
         * 构建 [ChannelDeleteRequest].
         * @param channelId 要删除的频道的ID
         */
        @JvmStatic
        public fun create(channelId: ID): ChannelDeleteRequest {
            return ChannelDeleteRequest(channelId)
        }
        
    }

    override val resultDeserializer: DeserializationStrategy<out Unit>
        get() = Unit.serializer()

    override val apiPaths: List<String>
        get() = apiPathList

    override fun createBody(): Any = Body(channelId)

    @Serializable
    private data class Body(
        @SerialName("channel_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val channelId: ID
    )

}
