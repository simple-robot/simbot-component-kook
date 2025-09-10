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
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.kook.api.KookPostApi
import love.forte.simbot.kook.objects.template.SimpleTemplate
import love.forte.simbot.kook.objects.template.Template
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * [创建模板](https://developer.kookapp.cn/doc/reference)
 *
 * POST /api/v3/template/create
 *
 * @author ForteScarlet
 * @since 4.3.0
 */
public class CreateTemplateApi private constructor(
    private val title: String,
    private val content: String,
    private val description: String? = null,
    private val testData: String? = null,
    private val msgtype: Int? = null,
    private val type: Int? = null,
    private val testChannel: String? = null,
) : KookPostApi<Template>() {
    public companion object Factory {
        private val PATH = ApiPath.create("template", "create")


        /**
         * 构建 [CreateTemplateApi]
         *
         * @param title 模板名称
         * @param content 模板内容
         * @param description 模板描述，可选
         * @param testData 测试数据，可选
         * @param msgtype 消息类型，可选
         * @param type 模板类型，可选
         * @param testChannel 测试频道，可选
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            title: String,
            content: String,
            description: String? = null,
            testData: String? = null,
            msgtype: Int? = null,
            type: Int? = null,
            testChannel: String? = null
        ): CreateTemplateApi = CreateTemplateApi(title, content, description, testData, msgtype, type, testChannel)
    }

    override val apiPath: ApiPath
        get() = PATH

    override val resultDeserializationStrategy: DeserializationStrategy<SimpleTemplate>
        get() = SimpleTemplate.serializer()

    override fun createBody(): Any = Body(title, content, description, testData, msgtype, type, testChannel)

    @Serializable
    private data class Body(
        val title: String,
        val content: String,
        val description: String? = null,
        @SerialName("test_data")
        val testData: String? = null,
        val msgtype: Int? = null,
        val type: Int? = null,
        @SerialName("test_channel")
        val testChannel: String? = null,
    )
}