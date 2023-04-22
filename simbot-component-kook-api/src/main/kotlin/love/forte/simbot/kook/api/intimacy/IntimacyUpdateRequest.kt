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
package love.forte.simbot.kook.api.intimacy

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.ID
import love.forte.simbot.Simbot
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest

/**
 * [更新用户亲密度](https://developer.kaiheila.cn/doc/http/intimacy#更新用户亲密度)
 *
 * `/api/v3/intimacy/update`
 *
 * method POST
 */
public class IntimacyUpdateRequest internal constructor(
    /**
     * 用户 id
     */
    private val userId: ID,
    /**
     * 亲密度，0-2200
     */
    private val score: Int? = null,
    /**
     * 机器人与用户的社交信息，500 字以内
     */
    private val socialInfo: String? = null,
    /**
     * 表情ID
     */
    private val imgId: ID? = null
) : KookPostRequest<Unit>() {
    public companion object Key : BaseKookApiRequestKey("intimacy", "update") {
    
        /**
         * 构造 [IntimacyUpdateRequest].
         *
         * @param userId 用户 id
         * @param score 亲密度，0-2200
         * @param socialInfo 机器人与用户的社交信息，500 字以内
         * @param imgId 表情ID
         *
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            userId: ID,
            score: Int? = null,
            socialInfo: String? = null,
            imgId: ID? = null
        ): IntimacyUpdateRequest = IntimacyUpdateRequest(userId, score, socialInfo, imgId)
    }
    
    init {
        Simbot.require(score == null || score in 0..2200) { "Score must in 0 .. 2200" }
        Simbot.require(socialInfo?.length?.let { it <= 500 } ?: true) { "Social info must <= 500." }
    }
    
    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()
    override val apiPaths: List<String>
        get() = apiPathList
    
    override fun createBody(): Any = Body(userId, score, socialInfo, imgId)
    
    @Serializable
    private data class Body(
        @SerialName("user_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        private val userId: ID,
        private val score: Int?,
        @SerialName("social_info")
        private val socialInfo: String?,
        @SerialName("img_id")
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        private val imgId: ID?
    )
}
