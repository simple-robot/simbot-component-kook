package forte.example.spring.app

import love.forte.simboot.annotation.Listener
import love.forte.simboot.spring.autoconfigure.EnableSimbot
import love.forte.simbot.event.ContactMessageEvent
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.stereotype.Component


/**
 *
 * @author ForteScarlet
 */
@EnableSimbot
@SpringBootApplication
open class SpringAppMain

@Component
open class TestListener {

    @Listener
    suspend fun ContactMessageEvent.onMsg() {
        println(messageContent)
        user().send(messageContent)
    }

}