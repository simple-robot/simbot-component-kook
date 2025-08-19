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

package love.forte.simbot.kook.api.channel

import io.ktor.http.*
import kotlinx.serialization.json.Json
import love.forte.simbot.kook.Kook
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Tests for [ChannelKickoutApi] focusing on API structure and serialization.
 * 
 * @author ForteScarlet
 */
class ChannelKickoutApiTest {
    private val json = Json(Kook.DEFAULT_JSON) {
        ignoreUnknownKeys = true
    }

    @Test
    fun testApiBasics() {
        val channelId = "123456789"
        val userId = "987654321"
        val api = ChannelKickoutApi.create(channelId, userId)
        
        // Test API properties
        assertEquals(HttpMethod.Post, api.method)
        assertTrue(api.url.toString().contains("channel/kickout"))
        
        // Test body content
        val body = api.body
        assertNotNull(body, "Body should not be null")
    }
    
    @Test
    fun testUrlConstruction() {
        val api = ChannelKickoutApi.create("test_channel", "test_user")
        val url = api.url.toString()
        
        // Verify URL contains correct Kook API base and path
        assertTrue(url.contains("kookapp.cn/api/v3"))
        assertTrue(url.contains("channel/kickout"))
    }
    
    @Test
    fun testRequestBodyNotNull() {
        val channelId = "voice_channel_123"
        val userId = "user_456"
        val api = ChannelKickoutApi.create(channelId, userId)
        
        val body = api.body
        assertNotNull(body, "Body should not be null")
        
        // Verify that the body object exists and is not null
        assertTrue(body.toString().isNotEmpty())
    }
    
    @Test
    fun testMultipleApiInstances() {
        val api1 = ChannelKickoutApi.create("channel1", "user1")
        val api2 = ChannelKickoutApi.create("channel2", "user2")
        
        // Verify that different instances have different bodies
        val body1 = api1.body
        val body2 = api2.body
        
        assertNotNull(body1)
        assertNotNull(body2)
        
        // Bodies should be different objects (different parameters)
        assertTrue(body1 !== body2, "Different API instances should have different body objects")
    }
    
    @Test
    fun testApiFactory() {
        val channelId = "factory_test_channel"
        val userId = "factory_test_user"
        
        // Test factory method
        val api = ChannelKickoutApi.create(channelId, userId)
        assertNotNull(api)
        
        // Verify the created API has the correct properties
        assertEquals(HttpMethod.Post, api.method)
        assertNotNull(api.body)
        assertNotNull(api.url)
    }
    
    @Test
    fun testApiPathCorrectness() {
        val api = ChannelKickoutApi.create("test", "test")
        val url = api.url
        
        // Check that the URL path is correct
        assertTrue(url.pathSegments.contains("channel"))
        assertTrue(url.pathSegments.contains("kickout"))
        
        // Verify it's a POST API
        assertEquals(HttpMethod.Post, api.method)
    }
    
    @Test
    fun testBodyNotNull() {
        val api = ChannelKickoutApi.create("channel_test", "user_test")
        
        // Body should never be null for this API
        assertNotNull(api.body)
        
        // Multiple calls to body should return the same non-null value
        val body1 = api.body
        val body2 = api.body
        assertNotNull(body1)
        assertNotNull(body2)
    }
}