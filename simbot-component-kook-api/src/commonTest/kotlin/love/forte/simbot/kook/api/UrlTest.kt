package love.forte.simbot.kook.api

import io.ktor.http.*
import love.forte.simbot.kook.Kook
import love.forte.simbot.kook.util.buildUrl
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 *
 * @author ForteScarlet
 */
class UrlTest {

    /**
     * The [KTOR-4402](https://youtrack.jetbrains.com/issue/KTOR-4402/The-pathSegments-returns-empty-strings-for-trailing-slashes).
     */
    @Test
    fun testUrlPathSegments() {
        assertEquals(
            listOf("", "api", "v3"),
            Url("https://www.kookapp.cn/api/v3").pathSegments,
            "Url.pathSegments should be ['', 'api', 'v3']"
        )

        assertEquals(
            listOf("", "api", "v3"),
            Kook.SERVER_URL_WITH_VERSION.pathSegments,
            "Kook.SERVER_URL_WITH_VERSION.pathSegments should be ['', 'api', 'v3']"
        )

        val api = buildUrl(Kook.SERVER_URL_WITH_VERSION) {}
        assertEquals(
            listOf("", "api", "v3"),
            api.pathSegments,
            "api.pathSegments should be ['', 'api', 'v3']"
        )
    }

}