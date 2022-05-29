import io.ktor.http.*
import love.forte.simbot.kaiheila.KaiheilaApi
import kotlin.test.Test

/**
 *
 * @author ForteScarlet
 */
class KaiheilaApiTest {

    /**
     * @see KaiheilaApi.buildApiUrl
     */
    @Test
    fun urlBuildTest() {
        val name = "forte"
        val age = 24
        val url = KaiheilaApi.buildApiUrl("user", "foo") {
            append("name", name)
            append("age", age.toString())
        }

        val version = "v${KaiheilaApi.VERSION}"

        assert(url.encodedPath == "/api/$version/user/foo")
        assert(url.fullPath == "/api/$version/user/foo?name=$name&age=$age")
        assert(url.toString() == "https://${KaiheilaApi.HOST}/api/$version/user/foo?name=$name&age=$age")

        println(KaiheilaApi.baseUrl)
        println(KaiheilaApi.baseUrlWithoutVersion)
    }

}