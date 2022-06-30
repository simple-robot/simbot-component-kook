/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
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
 * [获取用户亲密度](https://developer.kook.cn/doc/http/intimacy#获取用户亲密度)
 *
 * `/api/v3/intimacy/index`
 *
 * method GET
 *
 */
public class IntimacyIndexRequest(
    /**
     * 用户ID
     */
    private val userId: ID,
) : KookGetRequest<IntimacyIndex>() {
    public companion object Key : BaseKookApiRequestKey("intimacy", "index")

    override val resultDeserializer: DeserializationStrategy<out IntimacyIndex>
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



