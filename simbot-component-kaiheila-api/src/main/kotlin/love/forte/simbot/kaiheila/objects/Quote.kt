/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kaiheila.objects

import kotlinx.serialization.*
import love.forte.simbot.*


/**
 *
 * [引用消息Quote](https://developer.kaiheila.cn/doc/objects#%E5%BC%95%E7%94%A8%E6%B6%88%E6%81%AFQuote)
 *
 * @author ForteScarlet
 */
public interface Quote {

    /**
     * Id 引用消息id
     */
    public val id: CharSequenceID

    /**
     * Type 引用消息类型
     */
    public val type: Int

    /**
     * Content 	引用消息内容
     */
    public val content: String

    /**
     * Create at 引用消息创建时间（毫秒）
     */
    public val createAt: Timestamp

    /**
     * Author 作者的用户信息
     */
    public val author: User

    public companion object {
        internal val serializer: KSerializer<out Quote> = QuoteImpl.serializer()
    }
}


@Serializable
internal data class QuoteImpl(
    override val id: CharSequenceID,
    override val type: Int,
    override val content: String,
    @SerialName("create_at")
    override val createAt: Timestamp,
    override val author: UserImpl
) : Quote