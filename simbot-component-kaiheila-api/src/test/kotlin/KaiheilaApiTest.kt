import io.ktor.http.*
import love.forte.simbot.kaiheila.*
import kotlin.test.*

/**
 *
 * @author ForteScarlet
 */
class KaiheilaApiTest {

    /**
     * @see KaiheilaApi.buildApiUrl
     */
    @Test
    fun urlTest() {
        val name = "forte"
        val age = 24
        val url = KaiheilaApi.buildApiUrl("user", "foo") {
            append("name", name)
            append("age", age.toString())
        }

        assert(url.encodedPath == "/api/user/foo")
        assert(url.fullPath == "/api/user/foo?name=$name&age=$age")
        assert(url.toString() == "https://${KaiheilaApi.HOST}/api/user/foo?name=$name&age=$age")
    }

}