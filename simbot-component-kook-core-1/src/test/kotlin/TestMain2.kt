import love.forte.simbot.DiscreetSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.component.kook.event.*
import love.forte.simbot.component.kook.kookBots
import love.forte.simbot.component.kook.message.*
import love.forte.simbot.component.kook.useKook
import love.forte.simbot.core.application.createSimpleApplication
import love.forte.simbot.core.event.listeners
import love.forte.simbot.core.scope.SimpleScope.continuousSession
import love.forte.simbot.kook.Ticket
import love.forte.simbot.kook.api.asset.CreateAssetApi
import love.forte.simbot.kook.messages.MessageType
import love.forte.simbot.kook.objects.Attachments
import love.forte.simbot.kook.objects.card.CardMessage
import love.forte.simbot.kook.objects.card.Theme
import love.forte.simbot.kook.objects.card.buildCardMessage
import love.forte.simbot.kook.objects.kmd.KMarkdown
import love.forte.simbot.kook.objects.kmd.buildKMarkdown
import love.forte.simbot.message.At
import love.forte.simbot.message.Emoji
import love.forte.simbot.message.Image.Key.toImage
import love.forte.simbot.message.plus
import love.forte.simbot.resources.Resource.Companion.toResource
import kotlin.io.path.Path

@OptIn(DiscreetSimbotApi::class)
suspend fun main() {

    val app = createSimpleApplication {
        useKook()
    }

    app.eventListenerManager.listeners {
        KookEvent { event ->
            println("Kook event: $event")
            println("Kook event: ${event.sourceEventContent}")
        }

        KookUpdatedMessageEvent { event ->
            println("KookUpdatedMessageEvent: $event")
            event.updatedMessageContent.messages.forEach {
                println("\t$it")
            }
        }

        KookChannelMessageEvent { event ->
            val channel = event.channel()

        }


    }

    app.kookBots {
        val bot = register(Ticket.botWsTicket("jqdlyHK85xe1i5Bo", "1/MTAyNTA=/C8DkNBZvSvotmqwpJ1ux/w==")) {
            syncPeriods = syncPeriods.copy(syncPeriods.guild.copy(syncPeriod = 0))
        }

        bot.start()
    }


    app.join()
}

