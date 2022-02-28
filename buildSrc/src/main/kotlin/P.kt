
object P {
    object Simbot {
        val isSnapshot: Boolean = true // TODO
        const val GROUP = "love.forte.simbot"
        const val VERSION = "3.0.0.preview.4.1"
    }
    object ComponentKaiheila {
        val isSnapshot: Boolean get() = Simbot.isSnapshot
        const val GROUP = "${Simbot.GROUP}.component"
        // 3: v3 api
        const val VERSION = "${Simbot.VERSION}-3.0.1"
    }
}