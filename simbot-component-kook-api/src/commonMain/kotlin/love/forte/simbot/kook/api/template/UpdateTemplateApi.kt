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
import love.forte.simbot.kook.api.KookPostApi
import love.forte.simbot.kook.objects.template.SimpleTemplate
import love.forte.simbot.kook.objects.template.Template
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * [更新模板](https://developer.kookapp.cn/doc/reference)
 *
 * POST /api/v3/template/update
 *
 * @author ForteScarlet
 * @since 4.3.0
 */
public class UpdateTemplateApi private constructor(
    private val id: String,
    private val name: String? = null,
    private val content: String? = null,
    private val description: String? = null,
) : KookPostApi<Template>() {
    public companion object Factory {
        private val PATH = ApiPath.create("template", "update")

        /**
         * 构建 [UpdateTemplateApi]
         *
         * @param id 模板ID
         * @param name 模板名称，可选更新
         * @param content 模板内容，可选更新
         * @param description 模板描述，可选更新
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            id: String,
            name: String? = null,
            content: String? = null,
            description: String? = null
        ): UpdateTemplateApi = UpdateTemplateApi(id, name, content, description)
    }

    override val apiPath: ApiPath
        get() = PATH

    override val resultDeserializationStrategy: DeserializationStrategy<SimpleTemplate>
        get() = SimpleTemplate.serializer()

    override fun createBody(): Any = Body(id, name, content, description)

    @Serializable
    private data class Body(
        val id: String,
        val name: String? = null,
        val content: String? = null,
        val description: String? = null,
    )
}