/*
 * Copyright (c) 2023. ForteScarlet.
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

package love.forte.simbot.kook.api.template

import io.ktor.http.*
import kotlinx.serialization.json.Json
import love.forte.simbot.kook.Kook
import love.forte.simbot.kook.objects.template.TemplateList
import kotlin.test.*

/**
 * [GetTemplateListApi] 的测试。
 *
 * @author ForteScarlet
 */
class GetTemplateListApiTest {
    private val json = Json(Kook.DEFAULT_JSON) {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @Test
    fun testApiBasics() {
        val api = GetTemplateListApi.create()

        // 测试 API 属性
        assertEquals(HttpMethod.Get, api.method)
        assertEquals("https://www.kookapp.cn/api/v3/template/list", api.url.toString())
    }
    
    @Test
    fun testApiWithPagination() {
        val api = GetTemplateListApi.create(page = 2, pageSize = 10)

        // 测试 API 属性
        assertEquals(HttpMethod.Get, api.method)
        assertEquals("https://www.kookapp.cn/api/v3/template/list?page=2&page_size=10", api.url.toString())
    }

    @Test
    fun testSuccessfulResponseDeserialization() {
        // 基于模板 API 响应结构
        val successResponseJson = createSuccessResponseJson()

        val apiResult = json.decodeFromString(love.forte.simbot.kook.api.ApiResult.serializer(), successResponseJson)

        assertNotNull(apiResult)
        assertEquals(0, apiResult.code)
        assertEquals("操作成功", apiResult.message)

        val templateList = apiResult.parseData(json, TemplateList.serializer())
        assertNotNull(templateList)

        // 验证模板列表结构
        assertEquals(2, templateList.items.size)

        val firstTemplate = templateList.items[0]
        assertEquals("template_001", firstTemplate.id)
        assertEquals("Welcome Template", firstTemplate.name)
        assertEquals("Welcome to {{guild.name}}!", firstTemplate.content)
        assertEquals("Template for welcoming new users", firstTemplate.description)
        assertEquals(1, firstTemplate.status)
        assertEquals(1634567890000L, firstTemplate.createdAt)
        assertEquals(1634654290000L, firstTemplate.updatedAt)

        val secondTemplate = templateList.items[1]
        assertEquals("template_002", secondTemplate.id)
        assertEquals("Announcement Template", secondTemplate.name)
        assertEquals("**Important:** {{announcement}}", secondTemplate.content)
        assertNull(secondTemplate.description)
        assertEquals(1, secondTemplate.status)
        assertEquals(1634568890000L, secondTemplate.createdAt)
        assertEquals(1634655290000L, secondTemplate.updatedAt)

        // 验证元数据信息
        val meta = templateList.meta
        assertNotNull(meta, "Meta should not be null")
        assertEquals(1, meta.page)
        assertEquals(1, meta.pageTotal)
        assertEquals(20, meta.pageSize)
        assertEquals(2, meta.total)
    }

    @Test
    fun testEmptyResponseDeserialization() {
        // 测试空响应结构
        val emptyResponseJson = createEmptyResponseJson()

        val apiResult = json.decodeFromString(love.forte.simbot.kook.api.ApiResult.serializer(), emptyResponseJson)

        assertNotNull(apiResult)
        assertEquals(0, apiResult.code)
        assertEquals("操作成功", apiResult.message)

        val templateList = apiResult.parseData(json, TemplateList.serializer())
        assertNotNull(templateList, "Template list should not be null")
        assertTrue(templateList.items.isEmpty(), "Template list should be empty")

        val meta = templateList.meta
        assertNotNull(meta, "Meta should not be null")
        assertEquals(0, meta.total)
    }

    private fun createSuccessResponseJson(): String = """
        {
            "code": 0,
            "message": "操作成功",
            "data": {
                "items": [
                    {
                        "id": "template_001",
                        "name": "Welcome Template",
                        "content": "Welcome to {{guild.name}}!",
                        "description": "Template for welcoming new users",
                        "status": 1,
                        "created_at": 1634567890000,
                        "updated_at": 1634654290000
                    },
                    {
                        "id": "template_002",
                        "name": "Announcement Template",
                        "content": "**Important:** {{announcement}}",
                        "description": null,
                        "status": 1,
                        "created_at": 1634568890000,
                        "updated_at": 1634655290000
                    }
                ],
                "meta": {
                    "page": 1,
                    "page_total": 1,
                    "page_size": 20,
                    "total": 2
                }
            }
        }
    """.trimIndent()

    private fun createEmptyResponseJson(): String = """
        {
            "code": 0,
            "message": "操作成功",
            "data": {
                "items": [],
                "meta": {
                    "page": 1,
                    "page_total": 0,
                    "page_size": 20,
                    "total": 0
                }
            }
        }
    """.trimIndent()
}