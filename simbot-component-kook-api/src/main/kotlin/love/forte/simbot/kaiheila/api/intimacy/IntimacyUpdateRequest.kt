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
package love.forte.simbot.kaiheila.api.intimacy

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.ID
import love.forte.simbot.Simbot
import love.forte.simbot.kaiheila.api.BaseApiRequestKey
import love.forte.simbot.kaiheila.api.KaiheilaPostRequest

/**
 * [更新用户亲密度](https://developer.kaiheila.cn/doc/http/intimacy#更新用户亲密度)
 *
 * `/api/v3/intimacy/update`
 *
 * method POST
 */
public class IntimacyUpdateRequest @JvmOverloads constructor(
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
) : KaiheilaPostRequest<Unit>() {
    public companion object Key : BaseApiRequestKey("intimacy", "update")

    init {
        Simbot.require(score == null || score in 0..2200) { "Score must in 0 .. 2200" }
        Simbot.require(socialInfo?.length?.let { it <= 500 } ?: true) { "Social info must <= 500." }
    }

    override val resultDeserializer: DeserializationStrategy<out Unit>
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