import io.ktor.http.*
import kotlinx.serialization.json.Json
import love.forte.simbot.kook.api.ApiResult
import love.forte.simbot.kook.api.KookApiResult
import love.forte.simbot.kook.api.RateLimit
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
        listDataResult.httpStatus = HttpStatusCode(200, "OK")
        listDataResult.raw = jsonStr
        listDataResult.rateLimit = RateLimit.DEFAULT

        val listData = listDataResult.parseDataOrThrow(json, KookApiResult.ListData.serializer(User.serializer()))
        assert(listData.items.size == 2)
        assert(listDataResult.isSuccess)
        assert(listData.meta == KookApiResult.ListMeta(1, 1, 10, 2))
        assert(listData.sort["name"] == 1)
    }

}

@kotlinx.serialization.Serializable
private data class User(val name: String)
