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
 * Tests for [UpdateTemplateApi].
 *
 * @author ForteScarlet
 */
class UpdateTemplateApiTest {
    private val json = Json(Kook.DEFAULT_JSON) {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @Test
    fun testApiBasics() {
        val id = "template_001"
        val name = "Updated Template"
        val content = "Updated content"
        val description = "Updated description"
        val api = UpdateTemplateApi.create(id, name, content, description)

        // Test API properties
        assertEquals(HttpMethod.Post, api.method)
        assertEquals("https://www.kookapp.cn/api/v3/template/update", api.url.toString())
    }

    @Test
    fun testRequestBodyStructurePartialUpdate() = runTest {
        val id = "template_001"
        val name = "Updated Name Only"
        val api = UpdateTemplateApi.create(id, name = name)

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
        assertEquals(id, requestJson["id"]?.jsonPrimitive?.content)
        assertEquals(name, requestJson["name"]?.jsonPrimitive?.content)
        // Optional fields should not be present in partial update
        assertFalse(requestJson.containsKey("content"))
        assertFalse(requestJson.containsKey("description"))
    }

    @Test
    fun testRequestBodyStructureCompleteUpdate() = runTest {
        val id = "template_001"
        val name = "Completely Updated Template"
        val content = "Welcome to {{guild.name}}, {{user.name}}! Updated version."
        val description = "A completely updated template"
        val api = UpdateTemplateApi.create(id, name, content, description)

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
        assertEquals(id, requestJson["id"]?.jsonPrimitive?.content)
        assertEquals(name, requestJson["name"]?.jsonPrimitive?.content)
        assertEquals(content, requestJson["content"]?.jsonPrimitive?.content)
        assertEquals(description, requestJson["description"]?.jsonPrimitive?.content)
        assertEquals(4, requestJson.size)
    }

    @Test
    fun testRequestBodyStructureContentOnly() = runTest {
        val id = "template_001"
        val content = "Only content updated"
        val api = UpdateTemplateApi.create(id, content = content)

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
        assertEquals(id, requestJson["id"]?.jsonPrimitive?.content)
        assertEquals(content, requestJson["content"]?.jsonPrimitive?.content)
        // Other optional fields should not be present
        assertFalse(requestJson.containsKey("name"))
        assertFalse(requestJson.containsKey("description"))
    }

    @Test
    fun testSuccessfulResponseDeserialization() {
        // Based on template API response structure
        val successResponseJson = createSuccessResponseJson()

        val apiResult = json.decodeFromString(ApiResult.serializer(), successResponseJson)

        assertNotNull(apiResult)
        assertEquals(0, apiResult.code)
        assertEquals("操作成功", apiResult.message)

        val template = apiResult.parseData(json, SimpleTemplate.serializer())
        assertNotNull(template)
        assertEquals("template_001", template.id)
        assertEquals("Updated Template", template.name)
        assertEquals("Updated content", template.content)
        assertEquals("Updated description", template.description)
        assertEquals(1, template.status)
        assertEquals(1634567890000L, template.createdAt)
        assertEquals(1634654290000L, template.updatedAt)
    }

    @Test
    fun testFactoryMethods() {
        val id = "template_001"
        val name = "Factory Test Template"
        val content = "Factory test content"
        val description = "Factory test description"

        // Test create method with all parameters
        val api1 = UpdateTemplateApi.create(id, name, content, description)
        assertNotNull(api1)

        // Test create method with only ID and name
        val api2 = UpdateTemplateApi.create(id, name = name)
        assertNotNull(api2)

        // Test create method with only ID and content
        val api3 = UpdateTemplateApi.create(id, content = content)
        assertNotNull(api3)

        // Test create method with only ID and description
        val api4 = UpdateTemplateApi.create(id, description = description)
        assertNotNull(api4)
    }

    private fun createSuccessResponseJson(): String = """
        {
            "code": 0,
            "message": "操作成功",
            "data": {
                "id": "template_001",
                "name": "Updated Template",
                "content": "Updated content",
                "description": "Updated description",
                "status": 1,
                "created_at": 1634567890000,
                "updated_at": 1634654290000
            }
        }
    """.trimIndent()
}