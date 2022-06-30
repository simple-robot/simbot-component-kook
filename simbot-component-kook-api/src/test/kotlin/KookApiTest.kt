import io.ktor.http.*
import love.forte.simbot.kook.KookApi
import kotlin.test.Test

/**
 *
 * @author ForteScarlet
 */
class KookApiTest {

    /**
     * @see KookApi.buildApiUrl
     */
    @Test
    fun urlBuildTest() {
        val name = "forte"
        val age = 24
        val url = KookApi.buildApiUrl("user", "foo") {
            append("name", name)
            append("age", age.toString())
        }

        val version = "v${KookApi.VERSION}"

        assert(url.encodedPath == "/api/$version/user/foo")
        assert(url.fullPath == "/api/$version/user/foo?name=$name&age=$age")
        assert(url.toString() == "https://${KookApi.HOST}/api/$version/user/foo?name=$name&age=$age")

        println(KookApi.baseUrl)
        println(KookApi.baseUrlWithoutVersion)
    }

}