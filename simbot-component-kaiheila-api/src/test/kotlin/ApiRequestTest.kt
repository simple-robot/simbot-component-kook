import kotlinx.serialization.json.Json
import love.forte.simbot.kaiheila.api.ApiResult
import love.forte.simbot.kaiheila.api.KaiheilaApiResult
import kotlin.test.Test

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

        val listDataResult = json.decodeFromString(ApiResult.serializer(), jsonStr)
        val listData = listDataResult.parseDataOrThrow(json, KaiheilaApiResult.ListData.serializer(User.serializer()))
        assert(listData.items.size == 2)
        assert(listDataResult.isSuccess)
        assert(listData.meta == KaiheilaApiResult.ListMeta(1, 1, 10, 2))
        assert(listData.sort["name"] == 1)
    }

}

@kotlinx.serialization.Serializable
private data class User(val name: String)