import io.ktor.http.*
import love.forte.simbot.kook.Kook
import kotlin.test.Test

/**
 *
 * @author ForteScarlet
 */
class KookTest {

    /**
     * @see Kook.buildApiUrl
     */
    @Test
    fun urlBuildTest() {
        val name = "forte"
        val age = 24
        val url = Kook.buildApiUrl("user", "foo") {
            append("name", name)
            append("age", age.toString())
        }

        val version = "v${Kook.VERSION}"

        assert(url.encodedPath == "/api/$version/user/foo")
        assert(url.fullPath == "/api/$version/user/foo?name=$name&age=$age")
        assert(url.toString() == "https://${Kook.HOST}/api/$version/user/foo?name=$name&age=$age")

        println(Kook.baseUrl)
        println(Kook.baseUrlWithoutVersion)
    }

}