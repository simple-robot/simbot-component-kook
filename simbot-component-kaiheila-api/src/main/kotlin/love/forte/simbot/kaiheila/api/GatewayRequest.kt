/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kaiheila.api

import io.ktor.http.*
import kotlinx.serialization.*
import love.forte.simbot.kaiheila.api.GatewayRequest.Compress
import love.forte.simbot.kaiheila.api.GatewayRequest.NotCompress


/**
 *
 * 开黑啦 v3-api的 [gateway](https://developer.kaiheila.cn/doc/http/gateway) 获取接口的请求参数。
 *
 * @see Compress
 * @see NotCompress
 *
 * @author ForteScarlet
 */
public sealed class GatewayRequest(private val isCompress: Boolean) : KaiheilaGetRequest<Gateway>() {
    public companion object Key : BaseApiRequestKey("gateway", "index")


    override fun ParametersBuilder.buildParameters() {
        append("compress", if (isCompress) "1" else "0")
    }

    override val resultDeserializer: DeserializationStrategy<out Gateway>
        get() = Gateway.serializer()

    override val apiPaths: List<String>
        get() = apiPathList

    /**
     * 压缩数据
     */
    public object Compress : GatewayRequest(true)

    /**
     * 不进行数据压缩
     */
    public object NotCompress : GatewayRequest(false)

}


/**
 * api [GatewayRequest] 的响应体。
 */
@Serializable
public data class Gateway @ApiResultType constructor(val url: String)

