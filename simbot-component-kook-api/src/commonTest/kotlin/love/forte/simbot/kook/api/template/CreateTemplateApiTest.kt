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

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import love.forte.simbot.kook.Kook
import love.forte.simbot.kook.api.ApiResult
import love.forte.simbot.kook.api.requestResult
import love.forte.simbot.kook.objects.template.SimpleTemplate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

/**
 * [CreateTemplateApi] 的测试。
 *
 * @author ForteScarlet
 */
class CreateTemplateApiTest {
    private val json = Json(Kook.DEFAULT_JSON) {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @Test
    fun testApiBasics() {
        val name = "Test Template"
        val content = "Hello {{user.name}}!"
        val description = "A test template"
        val api = CreateTemplateApi.create(name, content, description)

        // 测试 API 属性
        assertEquals(HttpMethod.Post, api.method)
        assertEquals("https://www.kookapp.cn/api/v3/template/create", api.url.toString())
    }

    @Test
    fun testRequestBodyStructureMinimal() = runTest {
        val title = "Minimal Template"
        val content = "Simple content"
        val api = CreateTemplateApi.create(title, content)

        // 使用 MockEngine 捕获实际请求体
        var capturedRequestBody: String? = null
        val mockEngine = MockEngine { request ->
            capturedRequestBody = request.body.toByteArray().decodeToString()
            respond(
                content = createSuccessResponseJson(),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClient(mockEngine)

        // 发送实际请求以捕获序列化后的请求体
        api.requestResult(client, "Bot test-token")

        // 验证捕获的 JSON 请求体结构
        assertNotNull(capturedRequestBody, "Request body should be captured")
        val requestJson = json.parseToJsonElement(capturedRequestBody).jsonObject

        // 验证 JSON 结构符合预期的序列化格式
        assertEquals(title, requestJson["title"]?.jsonPrimitive?.content)
        assertEquals(content, requestJson["content"]?.jsonPrimitive?.content)
        // 在最小情况下不应包含可选字段
        assertFalse(requestJson.containsKey("description"))
    }

    @Test
    fun testRequestBodyStructureComplete() = runTest {
        val title = "Complete Template"
        val content = "Welcome to {{guild.name}}, {{user.name}}!"
        val description = "A complete template with description"
        val api = CreateTemplateApi.create(title, content, description)

        // Capture actual request body using MockEngine
        var capturedRequestBody: String? = null
        val mockEngine = MockEngine { request ->
            capturedRequestBody = request.body.toByteArray().decodeToString()
            respond(
                content = createSuccessResponseJson(),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClient(mockEngine)

        // Make actual request to capture serialized body
        api.requestResult(client, "Bot test-token")

        // Validate the captured JSON body structure
        assertNotNull(capturedRequestBody, "Request body should be captured")
        val requestJson = json.parseToJsonElement(capturedRequestBody).jsonObject

        // Verify JSON structure matches expected serialization
        assertEquals(title, requestJson["title"]?.jsonPrimitive?.content)
        assertEquals(content, requestJson["content"]?.jsonPrimitive?.content)
        assertEquals(description, requestJson["description"]?.jsonPrimitive?.content)
        assertEquals(3, requestJson.size)
    }

    @Test
    fun testSuccessfulResponseDeserialization() {
        // 基于模板 API 响应结构
        val successResponseJson = createSuccessResponseJson()

        val apiResult = json.decodeFromString(ApiResult.serializer(), successResponseJson)

        assertNotNull(apiResult)
        assertEquals(0, apiResult.code)
        assertEquals("操作成功", apiResult.message)

        val template = apiResult.parseData(json, SimpleTemplate.serializer())
        assertNotNull(template)
        assertEquals("template_001", template.id)
        assertEquals("Welcome Template", template.name)
        assertEquals("Welcome to {{guild.name}}!", template.content)
        assertEquals("Template for welcoming new users", template.description)
        assertEquals(1, template.status)
        assertEquals(1634567890000L, template.createdAt)
        assertEquals(1634654290000L, template.updatedAt)
    }

    @Test
    fun testFactoryMethods() {
        val name = "Factory Test Template"
        val content = "Factory test content"
        val description = "Factory test description"

        // 测试带描述的 create 方法
        val api1 = CreateTemplateApi.create(name, content, description)
        assertNotNull(api1)

        // 测试不带描述的 create 方法
        val api2 = CreateTemplateApi.create(name, content)
        assertNotNull(api2)
    }

    private fun createSuccessResponseJson(): String = """
        {
            "code": 0,
            "message": "操作成功",
            "data": {
                "id": "template_001",
                "name": "Welcome Template",
                "content": "Welcome to {{guild.name}}!",
                "description": "Template for welcoming new users",
                "status": 1,
                "created_at": 1634567890000,
                "updated_at": 1634654290000
            }
        }
    """.trimIndent()
}