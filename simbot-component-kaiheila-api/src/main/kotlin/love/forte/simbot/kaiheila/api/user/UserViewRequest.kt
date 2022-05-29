/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kaiheila 的一部分。
 *
 *  simbot-component-kaiheila 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kaiheila 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

package love.forte.simbot.kaiheila.api.user

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import love.forte.simbot.ID
import love.forte.simbot.kaiheila.api.BaseApiRequestKey
import love.forte.simbot.kaiheila.api.KaiheilaGetRequest
import love.forte.simbot.kaiheila.objects.User
import love.forte.simbot.kaiheila.objects.UserImpl
import love.forte.simbot.literal

/**
 * 查询用户信息
 * @author ForteScarlet
 */
public class UserViewRequest(
    private val userId: ID,
    private val guildId: ID,
) : KaiheilaGetRequest<User>() {
    public companion object Key : BaseApiRequestKey("user", "view")

    override val resultDeserializer: DeserializationStrategy<out User>
        get() = UserImpl.serializer()

    override val apiPaths: List<String>
        get() = apiPathList

    override fun ParametersBuilder.buildParameters() {
        append("user_id", userId.literal)
        append("guild_id", guildId.literal)
    }
}
