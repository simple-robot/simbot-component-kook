package forte.example.spring

import forte.example.spring.app.SpringAppMain
import forte.example.spring.app.TestListener
import love.forte.simbot.OriginBotManager
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


/**
 *
 * @author ForteScarlet
 */
@SpringBootTest(classes = [SpringAppMain::class])
class KaiheilaSpringBootTest {

    @Autowired
    lateinit var testListener: TestListener

    @Test
    fun test() {
        OriginBotManager.forEach {
            println(it)
            println(it.all())
        }
    }

}