import love.forte.simbot.kook.*
import love.forte.simbot.kook.event.Event
import love.forte.simbot.kook.event.EventExtra
import love.forte.simbot.kook.event.TextEventExtra

suspend fun main() {

    val bot = BotFactory.create(Ticket.botWsTicket("CLIENT_ID", "TOKEN")) {
        // 配置...
    }

    bot.processor<TextEventExtra> { raw -> // this: Event<TextEventExtra>, raw: String
        println("原始JSON: $raw")
        println("event: $this")
        println("event.extra: ${this.extra}")
        println("content: ${this.content}")
    }

    bot.startAndJoin()
}
