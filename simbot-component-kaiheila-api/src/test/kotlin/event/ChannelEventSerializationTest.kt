package event

import kotlinx.serialization.json.Json
import love.forte.simbot.kaiheila.event.system.SystemEventImpl
import love.forte.simbot.kaiheila.event.system.channel.AddedChannelExtraBodyImpl
import org.junit.jupiter.api.Test


/**
 *
 * @author ForteScarlet
 */
class ChannelEventSerializationTest {

    @Test
    fun addedChannel() {
        val jsonStr = """
            {
            	"channel_type": "GROUP",
            	"type": 255,
            	"target_id": "5033780667466154",
            	"author_id": "1",
            	"content": "[系统消息]",
            	"extra": {
            		"type": "added_channel",
            		"body": {
            			"id": "5569471183380420",
            			"name": "文字频道4",
            			"user_id": "2054026674",
            			"guild_id": "5033780667466154",
            			"is_category": 0,
            			"parent_id": "1851889887332806",
            			"level": null,
            			"slow_mode": 0,
            			"topic": "",
            			"type": 1,
            			"permission_overwrites": [{
            				"role_id": 0,
            				"allow": 0,
            				"deny": 0
            			}],
            			"permission_users": [],
            			"permission_sync": 1,
            			"mode": null,
            			"has_password": false,
            			"last_msg_content": "欢迎来到\"文字频道4\"",
            			"last_msg_id": ""
            		}
            	},
            	"msg_id": "e75ac457-53d8-4ca3-bfdc-19345b40bb8d",
            	"msg_timestamp": 1651823046459,
            	"nonce": "",
            	"from_type": 1
            }
        """.trimIndent()

        val json = Json {
            ignoreUnknownKeys = true
        }

        val serializer = AddedChannelExtraBodyImpl.serializer()
        val event = json.decodeFromString(SystemEventImpl.serializer(serializer), jsonStr)

        println(event)

    }

}