/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.api.asset

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.utils.io.streams.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import love.forte.simbot.kook.api.ApiResultType
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest
import love.forte.simbot.resources.Resource
import love.forte.simbot.resources.URLResource
import love.forte.simbot.utils.RandomIDUtil
import org.slf4j.LoggerFactory
import java.net.URL


/**
 * [上传媒体文件](https://developer.kaiheila.cn/doc/http/asset).
 *
 * 创建（上传）文件资源，例如图片等。
 *
 *
 * @author ForteScarlet
 */
public class AssetCreateRequest internal constructor(
    private val resource: Resource,
    private val name: String? = resource.name,
    resourceContentType: ContentType? = null
) : KookPostRequest<AssetCreated>(false) {
    public companion object Key : BaseKookApiRequestKey("asset", "create") {
        private val logger = LoggerFactory.getLogger("love.forte.simbot.kook.api.asset.AssetCreateRequest")
        
        /**
         * 构造一个 [AssetCreateRequest].
         *
         * @param resource 资源对象。
         * @param name 需要能够体现出文件的扩展名（例如 `mov`, `jpg`），否则尽可能提供 `resourceContentType` 参数。
         * @param resourceContentType 资源的content类型。
         
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            resource: Resource,
            name: String? = resource.name,
            resourceContentType: ContentType? = null
        ): AssetCreateRequest {
            return AssetCreateRequest(resource, name, resourceContentType)
        }
    }
    
    private val contentType: ContentType
    private val inputProvider = InputProvider { resource.openStream().asInput() }
    
    init {
        contentType = resourceContentType ?: run {
            val index = name?.lastIndexOf('.') ?: -1
            if (index < 0) {
                ContentType.Any
            } else {
                when (name?.substring(index + 1)?.lowercase()) {
                    "mov" -> ContentType.Video.QuickTime
                    "mp4" -> ContentType.Video.MP4
                    "gif" -> ContentType.Image.GIF
                    "jpeg", "jpg" -> ContentType.Image.JPEG
                    "png" -> ContentType.Image.PNG
                    "ico" -> ContentType.Image.XIcon
                    else -> ContentType.Any
                }
            }
        }
        
        
    }
    
    override val resultDeserializer: DeserializationStrategy<out AssetCreated>
        get() = AssetCreated.serializer()
    
    override val apiPaths: List<String> get() = apiPathList
    
    override fun HttpRequestBuilder.requestFinishingAction() {
        setBody(this@AssetCreateRequest.body ?: EmptyContent)
        onUpload { bytesSentTotal, contentLength ->
            if (bytesSentTotal == 0L || bytesSentTotal.mod(10000L) == 0L) {
                logger.debug(
                    "Uploading {}, bytesSentTotal: {}, contentLength: {}",
                    resource,
                    bytesSentTotal,
                    contentLength
                )
            }
        }
    }
    
    override fun createBody(): Any {
        return MultiPartFormDataContent(
            formData {
                
                val headers = headersOf(
                    if (name != null) {
                        HttpHeaders.ContentDisposition to listOf("filename=$name")
                    } else {
                        HttpHeaders.ContentDisposition to listOf("filename=simbot-${RandomIDUtil.randomID()}")
                    }
                )
                
                append(
                    "file",
                    inputProvider,
                    headers
                )
            }
        )
    }
    
    
}

/**
 * api [AssetCreateRequest] 的响应体，得到上传后的资源路径。
 */
@Serializable
public data class AssetCreated @ApiResultType constructor(val url: String) {
    
    /**
     * 通过 [url] 构建一个 [URLResource].
     */
    public fun toResource(): URLResource = URLResource(URL(url))
}
