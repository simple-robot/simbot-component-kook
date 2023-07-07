/*
 * Copyright (c) 2023. ForteScarlet.
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

package love.forte.simbot.kook.api.asset

import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.DeserializationStrategy
import love.forte.simbot.kook.api.KookPostApi

/**
 * [上传文件/图片](https://developer.kookapp.cn/doc/http/asset#%E4%B8%8A%E4%BC%A0%E5%AA%92%E4%BD%93%E6%96%87%E4%BB%B6)
 *
 * > `Header` 中 `Content-Type` 必须为 `form-data`
 *
 * @author ForteScarlet
 */
public actual class CreateAssetApi private actual constructor(
    private val formDataContentProvider: () -> MultiPartFormDataContent
) : KookPostApi<Asset>() {
    public actual companion object Factory {
        private val PATH = ApiPath.create("asset", "create")
        private const val DEFAULT_FILENAME = "unknown-file"
        private const val ASSET_API_FORM_PROPERTY_NAME = "file"

        /**
         * 提供文件字节数据作为上传API。
         *
         * @param fileBytes 文件数据
         * @param filename 使用在表单数据中 [HttpHeaders.ContentDisposition] 的 `filename` 属性，
         * 如果为 `null` 则会提供一个默认的文件名称 `unknown-file`。
         */
        public actual fun create(
            fileBytes: ByteArray,
            filename: String?
        ): CreateAssetApi = CreateAssetApi {
            MultiPartFormDataContent(formData {
                append(key = ASSET_API_FORM_PROPERTY_NAME, fileBytes, fileHeaders(filename))
            })
        }

        /**
         * 提供文件数据的 [InputProvider] 作为上传API。
         *
         * @param fileProvider 文件数据 provider
         * @param filename 使用在表单数据中 [HttpHeaders.ContentDisposition] 的 `filename` 属性，
         * 如果为 `null` 则会提供一个默认的文件名称 `unknown-file`。
         */
        public actual fun create(
            fileProvider: InputProvider,
            filename: String?
        ): CreateAssetApi = CreateAssetApi {
            MultiPartFormDataContent(formData {
                append(key = ASSET_API_FORM_PROPERTY_NAME, fileProvider, fileHeaders(filename))
            })
        }

        /**
         * 提供文件数据的 [ByteReadPacket] 作为上传API。
         *
         * *Note: 需要注意 [ByteReadPacket] 中的数据只能被使用一次。*
         *
         * @param fileByteReadPacket 文件数据 [ByteReadPacket]
         * @param filename 使用在表单数据中 [HttpHeaders.ContentDisposition] 的 `filename` 属性，
         * 如果为 `null` 则会提供一个默认的文件名称 `unknown-file`。
         */
        public actual fun create(
            fileByteReadPacket: ByteReadPacket,
            filename: String?
        ): CreateAssetApi = CreateAssetApi {
            MultiPartFormDataContent(formData {
                append(key = ASSET_API_FORM_PROPERTY_NAME, fileByteReadPacket, fileHeaders(filename))
            })
        }

        /**
         * 提供文件数据的 [ChannelProvider] 作为上传API。
         *
         * @param fileChannelProvider 文件数据 [ChannelProvider]
         * @param filename 使用在表单数据中 [HttpHeaders.ContentDisposition] 的 `filename` 属性，
         * 如果为 `null` 则会提供一个默认的文件名称 `unknown-file`。
         */
        public actual fun create(
            fileChannelProvider: ChannelProvider,
            filename: String?
        ): CreateAssetApi = CreateAssetApi {
            MultiPartFormDataContent(formData {
                append(key = ASSET_API_FORM_PROPERTY_NAME, fileChannelProvider, fileHeaders(filename))
            })
        }

        private fun fileHeaders(filename: String?): Headers = Headers.build {
            append(HttpHeaders.ContentDisposition, "filename=\"${filename ?: DEFAULT_FILENAME}\"")
        }
    }

    override val apiPath: ApiPath
        get() = PATH

    override val resultDeserializer: DeserializationStrategy<Asset>
        get() = Asset.serializer()

    override val body: MultiPartFormDataContent
        get() = formDataContentProvider()
}
