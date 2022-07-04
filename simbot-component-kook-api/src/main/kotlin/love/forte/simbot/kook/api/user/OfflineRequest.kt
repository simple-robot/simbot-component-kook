package love.forte.simbot.kook.api.user

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.kook.api.KookPostRequest
import love.forte.simbot.kook.util.unmodifiableListOf


/**
 * [下线机器人](https://developer.kaiheila.cn/doc/http/user#%E4%B8%8B%E7%BA%BF%E6%9C%BA%E5%99%A8%E4%BA%BA)
 *
 * @author ForteScarlet
 */
public object OfflineRequest : KookPostRequest<Unit>() {
    override val resultDeserializer: DeserializationStrategy<out Unit>
        get() = Unit.serializer()

    override val apiPaths: List<String> = unmodifiableListOf("user", "offline")

}