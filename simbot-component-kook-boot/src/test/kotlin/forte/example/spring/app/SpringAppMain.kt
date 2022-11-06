package forte.example.spring.app

import love.forte.simboot.annotation.Listener
import love.forte.simboot.spring.autoconfigure.EnableSimbot
import love.forte.simbot.component.kook.event.KookContactMessageEvent
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component


/**
 *
 * @author ForteScarlet
 */
@EnableSimbot
@SpringBootApplication
open class SpringAppMain

fun main() {
    runApplication<SpringAppMain>()
}

@Component
open class TestListener {

    @Listener
    suspend fun KookContactMessageEvent.onMsg() {
        println(messageContent.messages)
        reply(messageContent.messages)
    }

}