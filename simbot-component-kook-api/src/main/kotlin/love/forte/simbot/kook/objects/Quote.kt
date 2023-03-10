/*
 * Copyright (c) 2021-2023. ForteScarlet.
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

package love.forte.simbot.kook.objects

import kotlinx.serialization.KSerializer
import love.forte.simbot.CharSequenceID
import love.forte.simbot.Timestamp
import love.forte.simbot.kook.objects.impl.QuoteImpl


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
