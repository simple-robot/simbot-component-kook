package love.forte.simbot.kaiheila.api.user

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.kaiheila.api.KaiheilaPostRequest
import love.forte.simbot.kaiheila.util.unmodifiableListOf


/**
 * [下线机器人](https://developer.kaiheila.cn/doc/http/user#%E4%B8%8B%E7%BA%BF%E6%9C%BA%E5%99%A8%E4%BA%BA)
 *
 * @author ForteScarlet
 */
public object OfflineRequest : KaiheilaPostRequest<Unit>() {
    override val resultDeserializer: DeserializationStrategy<out Unit>
        get() = Unit.serializer()

    override val apiPaths: List<String> = unmodifiableListOf("user", "offline")

}