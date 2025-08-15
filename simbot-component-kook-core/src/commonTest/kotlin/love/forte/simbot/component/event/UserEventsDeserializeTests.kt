package love.forte.simbot.component.event

import kotlinx.serialization.json.Json
import love.forte.simbot.annotations.ExperimentalSimbotAPI
import love.forte.simbot.kook.Kook
import love.forte.simbot.kook.event.*
import kotlin.test.Test
import kotlin.test.assertIs

/**
 * Tests for KOOK user events deserialization.
 * 
 * Reference: https://developer.kookapp.cn/doc/event/user
 *
 * @author ForteScarlet
 */
class UserEventsDeserializeTests {
    private val json = Json(Kook.DEFAULT_JSON) {
        prettyPrint = true
    }

    @OptIn(ExperimentalSimbotAPI::class)
    @Test
    fun testDeserializeJoinedChannelEvent() {
        //language=json
        val rawJson =
            """{
  "s": 0,
  "d": {
    "channel_type": "GROUP",
    "type": 255,
    "target_id": "6016389910000000",
    "author_id": "1",
    "content": "[系统消息]",
    "extra": {
      "type": "joined_channel",
      "body": {
        "user_id": "2418200000",
        "channel_id": "9219038000000",
        "joined_at": 1612790368279
      }
    },
    "msg_id": "30a7f591-xxxx-322f35105524",
    "msg_timestamp": 1612790368279,
    "nonce": "",
    "verify_token": "xxx"
  },
  "sn": 42
}"""

        val ser = Signal.Event.serializer(SystemExtra.serializer())
        val deserialized = json.decodeFromString(ser, rawJson)
        assertIs<JoinedChannelEventExtra>(deserialized.data.extra)
    }

    @Test
    fun testDeserializeExitedChannelEvent() {
        //language=json
        val rawJson =
            """{
  "s": 0,
  "d": {
    "channel_type": "GROUP",
    "type": 255,
    "target_id": "6016389910000000",
    "author_id": "1",
    "content": "[系统消息]",
    "extra": {
      "type": "exited_channel",
      "body": {
        "user_id": "2418200000",
        "channel_id": "9219038000000",
        "exited_at": 1612790411267
      }
    },
    "msg_id": "386e533c-xxxxx-7ee2bded364e",
    "msg_timestamp": 1612790411274,
    "nonce": "",
    "verify_token": "xxx"
  },
  "sn": 43
}"""

        val ser = Signal.Event.serializer(SystemExtra.serializer())
        val deserialized = json.decodeFromString(ser, rawJson)
        assertIs<ExitedChannelEventExtra>(deserialized.data.extra)
    }

    @Test
    fun testDeserializeUserUpdatedEvent() {
        //language=json
        val rawJson =
            """{
  "s": 0,
  "d": {
    "channel_type": "PERSON",
    "type": 255,
    "target_id": "2862900000",
    "author_id": "1",
    "content": "[系统消息]",
    "extra": {
      "type": "user_updated",
      "body": {
        "user_id": "2418200000",
        "username": "ThisIsANewUsername",
        "avatar": "https://img.kaiheila.cn/avatars/2020-02/xxxx.jpg/icon"
      }
    },
    "msg_id": "02106a94-xxxx-d60f660485dd",
    "msg_timestamp": 1614055075487,
    "nonce": "",
    "verify_token": "xxxx"
  },
  "sn": 199
}"""

        val ser = Signal.Event.serializer(SystemExtra.serializer())
        val deserialized = json.decodeFromString(ser, rawJson)
        assertIs<UserUpdatedEventExtra>(deserialized.data.extra)
    }
}