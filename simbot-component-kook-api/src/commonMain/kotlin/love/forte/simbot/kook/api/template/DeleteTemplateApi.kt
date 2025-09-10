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

package love.forte.simbot.kook.api.template

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import love.forte.simbot.kook.api.ApiResultType
import love.forte.simbot.kook.api.KookPostApi
import kotlin.jvm.JvmStatic

/**
 * [删除模板](https://developer.kookapp.cn/doc/reference)
 *
 * POST /api/v3/template/delete
 *
 * @author ForteScarlet
 * @since 4.3.0
 */
public class DeleteTemplateApi private constructor(
    private val id: String,
) : KookPostApi<DeleteTemplateResult>() {
    public companion object Factory {
        private val PATH = ApiPath.create("template", "delete")

        /**
         * 构建 [DeleteTemplateApi]
         *
         * @param id 要删除的模板ID
         */
        @JvmStatic
        public fun create(id: String): DeleteTemplateApi = DeleteTemplateApi(id)
    }

    override val apiPath: ApiPath
        get() = PATH

    override val resultDeserializationStrategy: DeserializationStrategy<DeleteTemplateResult>
        get() = DeleteTemplateResult.serializer()

    override fun createBody(): Any = Body(id)

    @Serializable
    private data class Body(
        val id: String,
    )
}

/**
 * 删除模板 API 响应结果
 *
 * @since 4.3.0
 */
@Serializable
public data class DeleteTemplateResult @ApiResultType constructor(
    /**
     * 删除是否成功
     */
    val success: Boolean = true,
    /**
     * 可选的响应消息
     */
    val message: String? = null
)