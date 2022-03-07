/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kaiheila.api.user

import kotlinx.serialization.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.api.*
import love.forte.simbot.kaiheila.objects.*
import java.util.*

/**
 * [获取当前用户信息](https://developer.kaiheila.cn/doc/http/user#%E8%8E%B7%E5%8F%96%E5%BD%93%E5%89%8D%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF)
 *
 * @author ForteScarlet
 */
public object MeRequest : KaiheilaGetRequest<Me>() {
    override val apiPaths: List<String> = Collections.unmodifiableList(listOf("user", "me"))

    override val resultDeserializer: DeserializationStrategy<out Me>
        get() = Me.serializer()
}


/**
 * api [MeRequest] 的响应体。
 */
@Serializable
public data class Me @ApiResultType constructor(
    override val id: CharSequenceID,
    override val username: String,
    @SerialName("identify_num")
    override val identifyNum: String,
    @SerialName("online")
    override val isOnline: Boolean,
    override val status: Int,
    override val avatar: String,
    override val vipAvatar: String? = null,
    @SerialName("bot")
    override val isBot: Boolean,
    @SerialName("mobile_verified")
    override val mobileVerified: Boolean,
    override val nickname: String = username,
    override val roles: List<LongID> = emptyList(),
) : User