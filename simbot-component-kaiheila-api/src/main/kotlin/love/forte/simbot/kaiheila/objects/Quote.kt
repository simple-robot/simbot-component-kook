/*
 *
 *  * Copyright (c) 2021. ForteScarlet All rights reserved.
 *  * Project  simple-robot
 *  * File     MiraiAvatar.kt
 *  *
 *  * You can contact the author through the following channels:
 *  * github https://github.com/ForteScarlet
 *  * gitee  https://gitee.com/ForteScarlet
 *  * email  ForteScarlet@163.com
 *  * QQ     1149159218
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
public data class QuoteImpl(
    override val id: CharSequenceID,
    override val type: Int,
    override val content: String,
    @SerialName("create_at")
    override val createAt: Timestamp,
    override val author: UserImpl
) : Quote