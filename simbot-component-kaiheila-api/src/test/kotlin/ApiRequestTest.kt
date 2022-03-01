import kotlinx.serialization.json.*
import love.forte.simbot.kaiheila.api.*
import kotlin.test.*

/**
 *
 * @author ForteScarlet
 */
class ApiRequestTest {

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    @Test
    fun listApi() {
        val jsonStr = """
            {
            "code": 0, 
            "message": "", 
            "data": {
              "items": [{"name": "forte"}, {"name": "forli"}],
              "meta": {
                "page": 1,
                "page_total": 1,
                "page_size": 10,
                "total": 2
              },
              "sort": { "name": 1 }
            }}
        """.trimIndent()

        val listData = json.decodeFromString(KaiheilaApiResult.List.serializer(User.serializer()), jsonStr)
        assert(listData.data.items.size == 2)
        assert(listData.isSuccess)
        assert(listData.data.meta == KaiheilaApiResult.List.Meta(1, 1, 10, 2))
        assert(listData.data.sort["name"] == 1)
    }

}

@kotlinx.serialization.Serializable
private data class User(val name: String)