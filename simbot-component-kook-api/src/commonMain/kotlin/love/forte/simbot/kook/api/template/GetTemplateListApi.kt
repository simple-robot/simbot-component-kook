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

import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.DeserializationStrategy
import love.forte.simbot.kook.api.KookGetApi
import love.forte.simbot.kook.api.ListData
import love.forte.simbot.kook.objects.template.SimpleTemplate
import love.forte.simbot.kook.objects.template.Template
import love.forte.simbot.kook.util.appendIfNotNull
import love.forte.simbot.kook.util.parameters
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * [获取模板列表](https://developer.kookapp.cn/doc/http/template)
 *
 * GET /api/v3/template/list
 *
 * @author ForteScarlet
 * @since 4.3.0
 */
public class GetTemplateListApi private constructor(
    /**
     * 目标页数
     */
    private val page: Int? = null,
    /**
     * 每页数据数量
     */
    private val pageSize: Int? = null,
) : KookGetApi<ListData<Template>>() {
    public companion object Factory {
        private val PATH = ApiPath.create("template", "list")
        private val SER = ListData.serializer(SimpleTemplate.serializer())

        /**
         * 构造 [获取模板列表][GetTemplateListApi] 请求。
         *
         * @param page 目标页数
         * @param pageSize 每页数据数量
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            page: Int? = null,
            pageSize: Int? = null
        ): GetTemplateListApi = GetTemplateListApi(page, pageSize)
    }

    override val apiPath: ApiPath get() = PATH
    override val resultDeserializationStrategy: DeserializationStrategy<ListData<Template>>
        get() = SER

    override fun urlBuild(builder: URLBuilder) {
        builder.parameters {
            appendIfNotNull("page", page) { it.toString() }
            appendIfNotNull("page_size", pageSize) { it.toString() }
        }
    }
}

/**
 * 批次量的通过 [GetTemplateListApi] 查询所有结果直至最后一次响应的 meta.page >= meta.pageTotal。
 *
 * @param block 通过一个页码参数来通过 [GetTemplateListApi] 发起一次请求
 */
public inline fun GetTemplateListApi.Factory.createFlow(
    crossinline block: suspend GetTemplateListApi.Factory.(page: Int) -> ListData<Template>
): Flow<ListData<Template>> = flow {
    var page = 1
    do {
        val templateList = block(page)
        emit(templateList)
        page = templateList.meta.page + 1
    } while (templateList.items.isNotEmpty() &&
        templateList.meta.page < templateList.meta.pageTotal
    )
}

/**
 * 批次量的通过 [GetTemplateListApi] 查询所有结果直至最后一次响应的 meta.page >= meta.pageTotal。
 *
 * @param block 通过一个页码参数来通过 [GetTemplateListApi] 发起一次请求
 */
public inline fun GetTemplateListApi.Factory.createItemFlow(
    crossinline block: suspend GetTemplateListApi.Factory.(page: Int) -> ListData<Template>
): Flow<Template> = flow {
    var page = 1
    do {
        val templateList = block(page)
        templateList.items.forEach { emit(it) }
        page = templateList.meta.page + 1
    } while (templateList.items.isNotEmpty() &&
        templateList.meta.page < templateList.meta.pageTotal
    )
}