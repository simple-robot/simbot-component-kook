package forte.example.spring

import forte.example.spring.app.TestListener
import love.forte.simbot.OriginBotManager


/**
 *
 * @author ForteScarlet
 */
// @SpringBootTest(classes = [SpringAppMain::class])
class KaiheilaSpringBootTest {

    // @Autowired
    lateinit var testListener: TestListener

    // @Test
    fun test() {
        OriginBotManager.forEach {
            println(it)
            println(it.all())
        }
    }

}