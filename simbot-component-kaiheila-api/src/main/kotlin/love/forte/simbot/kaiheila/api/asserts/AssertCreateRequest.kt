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

package love.forte.simbot.kaiheila.api.asserts

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.utils.io.streams.*
import kotlinx.serialization.*
import love.forte.simbot.kaiheila.api.*
import love.forte.simbot.resources.*
import java.net.*


/**
 *
 * 创建（上传）文件资源，例如图片等。
 *
 *
 * @param resource 资源对象。
 * @param name 需要能够体现出文件的扩展名（以文件扩展名结尾），否则尽可能提供 `resourceContentType` 参数。
 * @param resourceContentType 资源的content类型。
 * @author ForteScarlet
 */
public class AssertCreateRequest(
    private val resource: Resource,
    private val name: String? = resource.name,
    resourceContentType: ContentType? = null
) : KaiheilaPostRequest<AssetCreated>(false) {
    public companion object Key : BaseApiRequestKey("assert", "create")

    private val contentType: ContentType

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
        headers {
            remove(HttpHeaders.ContentType)
        }
    }

    override fun createBody(): Any {
        return MultiPartFormDataContent(
            formData {

                val headers = if (name != null) {
                    headersOf(
                        HttpHeaders.ContentDisposition to listOf("filename=$name")
                    )
                } else {
                    headersOf()
                }

                append(
                    "file",
                    InputProvider { resource.openStream().asInput() },
                    headers
                )
            }
        )
    }


}

/**
 * api [AssertCreateRequest] 的响应体，得到上传后的资源路径。
 */
@Serializable
public data class AssetCreated @ApiResultType constructor(val url: String) {

    /**
     * 通过 [url] 构建一个 [URLResource].
     */
    public fun toResource(): URLResource = URLResource(URL(url))
}