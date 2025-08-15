package love.forte.simbot.component.event

import kotlinx.serialization.json.Json
import love.forte.simbot.annotations.ExperimentalSimbotAPI
import love.forte.simbot.kook.Kook
import love.forte.simbot.kook.event.*
import kotlin.test.Test
import kotlin.test.assertIs

/**
 * Tests for KOOK guild events deserialization.
 * 
 * Reference: https://developer.kookapp.cn/doc/event/guild
 *
 * @author ForteScarlet
 */
class GuildEventsDeserializeTests {
    private val json = Json(Kook.DEFAULT_JSON) {
        prettyPrint = true
    }

    @OptIn(ExperimentalSimbotAPI::class)
    @Test
    fun testDeserializeUpdatedGuildEvent() {
        //language=json
        val rawJson =
            """{
  "s": 0,
  "d": {
    "channel_type": "GROUP",
    "type": 255,
    "target_id": "601630000000",
    "author_id": "1",
    "content": "[系统消息]",
    "extra": {
      "type": "updated_guild",
      "body": {
        "id": "601630000000",
        "name": "test111",
        "topic": "测试服务器主题",
        "user_id": "2418xxx",
        "icon": "https://xxx/icons/2020-05/YQyfHxxx.png/icon",
        "notify_type": 1,
        "region": "shanghai",
        "enable_open": 1,
        "open_id": 1123123123,
        "default_channel_id": "4881800000000",
        "welcome_channel_id": "4881800000000"
      }
    },
    "msg_id": "0108feaf-xxx-7d70145468f0",
    "msg_timestamp": 1612764956322,
    "nonce": "",
    "verify_token": "xxx"
  },
  "sn": 9
}"""

        val ser = Signal.Event.serializer(SystemExtra.serializer())
        val deserialized = json.decodeFromString(ser, rawJson)
        assertIs<UpdateGuildEventExtra>(deserialized.data.extra)
    }

    @Test
    fun testDeserializeDeletedGuildEvent() {
        //language=json
        val rawJson =
            """{
  "s": 0,
  "d": {
    "channel_type": "GROUP",
    "type": 255,
    "target_id": "xxx",
    "author_id": "1",
    "content": "[系统消息]",
    "extra": {
      "type": "deleted_guild",
      "body": {
        "id": "xxx",
        "name": "testDel",
        "topic": "删除的服务器主题",
        "user_id": "2418200000",
        "icon": "",
        "notify_type": 2,
        "region": "beijing",
        "enable_open": 0,
        "open_id": 0,
        "default_channel_id": "xxxx",
        "welcome_channel_id": "0"
      }
    },
    "msg_id": "3d2bdb08-xxxx-faa2b9e77394",
    "msg_timestamp": 1614086485182,
    "nonce": "",
    "verify_token": "xxx"
  },
  "sn": 210
}"""

        val ser = Signal.Event.serializer(SystemExtra.serializer())
        val deserialized = json.decodeFromString(ser, rawJson)
        assertIs<DeleteGuildEventExtra>(deserialized.data.extra)
    }

    @Test
    fun testDeserializeAddedBlockListEvent() {
        //language=json
        val rawJson =
            """{
  "s": 0,
  "d": {
    "channel_type": "GROUP",
    "type": 255,
    "target_id": "xxxx",
    "author_id": "1",
    "content": "[系统消息]",
    "extra": {
      "type": "added_block_list",
      "body": {
        "operator_id": "xxxx",
        "remark": "频繁发广告",
        "user_id": ["3751918xx", "xxxxxxxxx"]
      }
    },
    "msg_id": "6dfaf089-xxxxx-63e8d55602d4",
    "msg_timestamp": 1613997198323,
    "nonce": "",
    "verify_token": "xxx"
  },
  "sn": 183
}"""

        val ser = Signal.Event.serializer(SystemExtra.serializer())
        val deserialized = json.decodeFromString(ser, rawJson)
        assertIs<AddBlockListEventExtra>(deserialized.data.extra)
    }

    @Test
    fun testDeserializeDeletedBlockListEvent() {
        //language=json
        val rawJson =
            """{
  "s": 0,
  "d": {
    "channel_type": "GROUP",
    "type": 255,
    "target_id": "xxxx",
    "author_id": "1",
    "content": "[系统消息]",
    "extra": {
      "type": "deleted_block_list",
      "body": {
        "operator_id": "xxx",
        "remark": "解除封禁",
        "user_id": ["3751918xx"]
      }
    },
    "msg_id": "14230d28-xxxx-b84609119e01",
    "msg_timestamp": 1613997311786,
    "nonce": "",
    "verify_token": "xxx"
  },
  "sn": 184
}"""

        val ser = Signal.Event.serializer(SystemExtra.serializer())
        val deserialized = json.decodeFromString(ser, rawJson)
        assertIs<DeleteBlockListEventExtra>(deserialized.data.extra)
    }

    @Test
    fun testDeserializeAddedEmojiEvent() {
        //language=json
        val rawJson =
            """{
  "s": 0,
  "d": {
    "channel_type": "GROUP",
    "type": 255,
    "target_id": "xxxxxxxxxxxxxx",
    "author_id": "1",
    "content": "[系统消息]",
    "extra": {
      "type": "added_emoji",
      "body": {
        "id": "xxxxxxxxx/xxxxxxxxx",
        "name": "xxxxxxx"
      }
    },
    "msg_id": "14230d28-xxxx-b84609119e01",
    "msg_timestamp": 1613997311786,
    "nonce": "",
    "verify_token": "xxx"
  },
  "sn": 184
}"""

        val ser = Signal.Event.serializer(SystemExtra.serializer())
        val deserialized = json.decodeFromString(ser, rawJson)
        assertIs<AddedEmojiEventExtra>(deserialized.data.extra)
    }

    @Test
    fun testDeserializeRemovedEmojiEvent() {
        //language=json
        val rawJson =
            """{
  "s": 0,
  "d": {
    "channel_type": "GROUP",
    "type": 255,
    "target_id": "xxxxxxxxxxxxxx",
    "author_id": "1",
    "content": "[系统消息]",
    "extra": {
      "type": "removed_emoji",
      "body": {
        "id": "xxxxxxxxx/xxxxxxxxx",
        "name": "xxxxxxx"
      }
    },
    "msg_id": "14230d28-xxxx-b84609119e01",
    "msg_timestamp": 1613997311786,
    "nonce": "",
    "verify_token": "xxx"
  },
  "sn": 184
}"""

        val ser = Signal.Event.serializer(SystemExtra.serializer())
        val deserialized = json.decodeFromString(ser, rawJson)
        assertIs<RemovedEmojiEventExtra>(deserialized.data.extra)
    }

    @Test
    fun testDeserializeUpdatedEmojiEvent() {
        //language=json
        val rawJson =
            """{
  "s": 0,
  "d": {
    "channel_type": "GROUP",
    "type": 255,
    "target_id": "xxxxxxxxxxxxxx",
    "author_id": "1",
    "content": "[系统消息]",
    "extra": {
      "type": "updated_emoji",
      "body": {
        "id": "xxxxxxxxx/xxxxxxxxx",
        "name": "xxxxxxx"
      }
    },
    "msg_id": "14230d28-xxxx-b84609119e01",
    "msg_timestamp": 1613997311786,
    "nonce": "",
    "verify_token": "xxx"
  },
  "sn": 184
}"""

        val ser = Signal.Event.serializer(SystemExtra.serializer())
        val deserialized = json.decodeFromString(ser, rawJson)
        assertIs<UpdatedEmojiEventExtra>(deserialized.data.extra)
    }
}