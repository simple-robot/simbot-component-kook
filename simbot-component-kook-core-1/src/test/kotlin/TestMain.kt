import io.ktor.client.*
import io.ktor.client.engine.cio.*
import love.forte.simbot.kook.BotFactory
import love.forte.simbot.kook.Ticket
import love.forte.simbot.kook.api.message.SendChannelMessageApi
import love.forte.simbot.kook.api.message.SendMessageType
import love.forte.simbot.kook.create

suspend fun main() {

    // 用于请求的 Ktor HttpClient，如有必要则需要自行引入并选择需要使用的引擎。
// 参考：https://ktor.io/docs/http-client-engines.html
    val client = HttpClient(CIO) {
        // config...
    }

// 鉴权信息
// 'Bot' 后面跟的是bot的token，参考 https://developer.kookapp.cn/doc/reference
    val authorization = "Bot xxxxxxxxxx"

    val bot = BotFactory.create(Ticket.botWsTicket("CLIENT_ID", "TOKEN")) {
        // 配置...
    }

// 构建要请求的API，大部分API都有一些可选或必须的参数。
    val api = SendChannelMessageApi.create(targetId = "目标ID", content = "消息内容")

    // 或其他构建方式
    SendChannelMessageApi.create {
        content = ""
        type = 9
        type(SendMessageType.KMARKDOWN)
        nonce = "nonce"
        quote = "quote"
        tempTargetId = "tempTargetId"
    }

    val result = api.requestData(client, authorization)
    println("result = $result")
    println("result.nonce = ${result.nonce}")
    println("result.msgId = ${result.msgId}")
    println("result.msgTimestamp = ${result.msgTimestamp}")
}
