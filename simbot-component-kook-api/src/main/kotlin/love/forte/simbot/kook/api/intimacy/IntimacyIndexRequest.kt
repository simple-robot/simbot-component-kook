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

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.kook.api.ApiResultType
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookGetRequest

/**
 *
 * [获取用户亲密度](https://developer.kaiheila.cn/doc/http/intimacy#获取用户亲密度)
 *
 * `/api/v3/intimacy/index`
 *
 * method GET
 *
 */
public class IntimacyIndexRequest internal constructor(
    private val userId: ID,
) : KookGetRequest<IntimacyIndex>() {
    public companion object Key : BaseKookApiRequestKey("intimacy", "index") {
    
        /**
         * 构造 [IntimacyIndexRequest].
         * @param userId 用户ID
         *
         */
        @JvmStatic
        public fun create(userId: ID): IntimacyIndexRequest = IntimacyIndexRequest(userId)
    }

    override val resultDeserializer: DeserializationStrategy<IntimacyIndex>
        get() = IntimacyIndex.serializer()

    override val apiPaths: List<String> get() = apiPathList

    override fun ParametersBuilder.buildParameters() {
        append("user_id", userId.toString())
    }

}


/**
 * 好感度信息
 *
 *
 */
@Serializable
public data class IntimacyIndex @ApiResultType constructor(
    /**
     * 机器人给用户显示的形象图片地址
     */
    @SerialName("img_url")
    val imgUrl: String,
    /**
     * 机器人显示给用户的社交信息
     */
    @SerialName("social_info")
    val socialInfo: String,

    /**
     * 	用户上次查看的时间戳
     */
    @SerialName("last_read")
    val lastRead: Timestamp,
    /**
     * 形象图片的总列表
     */
    @SerialName("img_list")
    val imgList: List<Img>
) {

    /**
     * 好感度形象图片实例
     */
    @Serializable
    public data class Img @ApiResultType constructor(
        /**
         * 形象图片的 id
         */
        @Serializable(ID.AsCharSequenceIDSerializer::class)
        val id: ID,
        /**
         * 形象图片的地址
         */
        val url: String
    )

}



