/*
 * Copyright (c) 2021-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook,
 * If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.api

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import love.forte.simbot.kook.api.GatewayRequest.*


/**
 *
 * Kook  v3-api的 [gateway](https://developer.kaiheila.cn/doc/http/gateway) 获取接口的请求参数。
 *
 * 如果是重连请求，使用 [Resume].
 *
 * @see Compress
 * @see NotCompress
 * @see Resume
 *
 * @author ForteScarlet
 */
public sealed class GatewayRequest(private val isCompress: Boolean) : KookGetRequest<Gateway>() {

    override fun ParametersBuilder.buildParameters() {
        append("compress", if (isCompress) "1" else "0")
        buildParameters0()
    }

    protected open fun ParametersBuilder.buildParameters0() {}

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


    /**
     * 重连获取路由时使用的api。
     */
    public class Resume internal constructor(internal val isCompress: Boolean, private val sn: Long, private val sessionId: String) :
        GatewayRequest(isCompress) {
        override fun ParametersBuilder.buildParameters0() {
            append("resume", "1")
            append("sn", sn.toString())
            append("session_id", sessionId)
        }
    }
    
    public companion object Key : BaseKookApiRequestKey("gateway", "index") {
    
        /**
         * 根据 [isCompress] 的值选择 [Compress] 或 [NotCompress].
         * @param isCompress 是否进行数据压缩
         * @see Compress
         * @see NotCompress
         */
        @JvmStatic
        public fun create(isCompress: Boolean): GatewayRequest {
            return if (isCompress) Compress else NotCompress
        }
    
        /**
         * 构建一个用于重新连接的 [Resume] 实例。
         * @see Resume
         */
        @JvmStatic
        public fun create(isCompress: Boolean, sn: Long, sessionId: String): Resume {
            return Resume(isCompress, sn, sessionId)
        }
        
    }
    
}

/**
 * 将一个 [GatewayRequest] 根据重连参数重构为用于重新连接的 [Resume].
 *
 */
@Suppress("unused")
public fun GatewayRequest.resume(sn: Long, sessionId: String): Resume {
    return when (this) {
        is Compress -> Resume(true, sn, sessionId)
        is NotCompress -> Resume(false, sn, sessionId)
        is Resume -> Resume(isCompress, sn, sessionId)
    }
}


/**
 * api [GatewayRequest] 的响应体。
 */
@Serializable
public data class Gateway @ApiResultType constructor(val url: String)

