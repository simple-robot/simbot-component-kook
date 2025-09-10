/*
 *     Copyright (c) 2023-2025. ForteScarlet.
 *
 *     This file is part of simbot-component-kook.
 *
 *     simbot-component-kook is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     simbot-component-kook is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with simbot-component-kook,
 *     If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.objects.template

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.kook.api.ApiResultType

/**
 * 消息模板对象，表示一个消息模板。
 *
 * [消息模板](https://developer.kookapp.cn/doc/http/template)
 *
 * @author ForteScarlet
 * @since 4.3.0
 */
public interface Template {
    /**
     * 模板 ID
     */
    public val id: String

    /**
     * 模板名称
     */
    public val name: String

    /**
     * 模板内容
     */
    public val content: String

    /**
     * 模板描述
     */
    public val description: String?

    /**
     * 模板状态
     */
    public val status: Int

    /**
     * 创建时间戳
     */
    public val createdAt: Long

    /**
     * 更新时间戳
     */
    public val updatedAt: Long
}

/**
 * [Template] 的简单实现。
 *
 * @since 4.3.0
 */
@Serializable
public data class SimpleTemplate @ApiResultType constructor(
    override val id: String,
    override val name: String,
    override val content: String,
    override val description: String? = null,
    override val status: Int,
    @SerialName("created_at")
    override val createdAt: Long,
    @SerialName("updated_at")
    override val updatedAt: Long
) : Template

/**
 * 模板列表响应包装器
 *
 * @since 4.3.0
 */
@Serializable
public data class TemplateList @ApiResultType constructor(
    val items: List<SimpleTemplate>,
    val meta: TemplateListMeta? = null
)

/**
 * 模板列表响应的元数据
 *
 * @since 4.3.0
 */
@Serializable
public data class TemplateListMeta @ApiResultType constructor(
    val page: Int? = null,
    @SerialName("page_total")
    val pageTotal: Int? = null,
    @SerialName("page_size")
    val pageSize: Int? = null,
    val total: Int? = null
)