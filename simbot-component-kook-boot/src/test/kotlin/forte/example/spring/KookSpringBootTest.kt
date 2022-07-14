package forte.example.spring

import forte.example.spring.app.TestListener
import love.forte.simbot.FragileSimbotApi
import love.forte.simbot.bot.OriginBotManager


/**
 *
 * @author ForteScarlet
 */
// @SpringBootTest(classes = [SpringAppMain::class])
class KookSpringBootTest {

    // @Autowired
    lateinit var testListener: TestListener

    // @Test
    @OptIn(FragileSimbotApi::class)
    fun test() {
        OriginBotManager.forEach {
            println(it)
            println(it.all())
        }
    }

}