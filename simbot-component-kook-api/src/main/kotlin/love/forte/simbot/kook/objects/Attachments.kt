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
import love.forte.simbot.kook.objects.impl.AttachmentsImpl


/**
 *
 * [附加的多媒体数据Attachments](https://developer.kaiheila.cn/doc/objects#%E9%99%84%E5%8A%A0%E7%9A%84%E5%A4%9A%E5%AA%92%E4%BD%93%E6%95%B0%E6%8D%AEAttachments)
 *
 * @author ForteScarlet
 */
public interface Attachments {
    /**
     * Type 多媒体类型.
     * - `file` -> 文件/附件
     * - `image` -> 图片
     * - `video` -> 视频
     * - 可能还有其他？
     */
    public val type: String

    /**
     * Url 多媒体地址
     */
    public val url: String

    /**
     * Name 多媒体名
     */
    public val name: String

    /**
     * Size 大小 单位（B）
     * 假如无法获取，得到-1.
     */
    public val size: Long

    public companion object {
        internal val serializer: KSerializer<out Attachments> = AttachmentsImpl.serializer()
    }
    
}
