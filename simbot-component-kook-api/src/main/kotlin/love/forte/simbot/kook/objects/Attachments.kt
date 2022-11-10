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
