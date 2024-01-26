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
import kotlinx.serialization.Serializable
import love.forte.simbot.kook.api.ApiResultType
import love.forte.simbot.kook.api.KookPostApi
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic


/**
 * [上传文件/图片](https://developer.kookapp.cn/doc/http/asset#%E4%B8%8A%E4%BC%A0%E5%AA%92%E4%BD%93%E6%96%87%E4%BB%B6)
 *
 * > `Header` 中 `Content-Type` 必须为 `form-data`
 *
 * @author ForteScarlet
 */
public expect class CreateAssetApi private constructor(
    formDataContentProvider: () -> MultiPartFormDataContent
) : KookPostApi<Asset> {
    override val apiPath: ApiPath
    override val resultDeserializationStrategy: DeserializationStrategy<Asset>
    override val body: Any?

    public companion object Factory {
        /**
         * 提供文件字节数据作为上传API。
         *
         * @param fileBytes 文件数据
         * @param filename 使用在表单数据中 [HttpHeaders.ContentDisposition] 的 `filename` 属性，
         * 如果为 `null` 则会提供一个默认的文件名称 `unknown-file`。
         */
        @JvmStatic
        @JvmOverloads
        public fun create(fileBytes: ByteArray, filename: String? = null): CreateAssetApi

        /**
         * 提供文件数据的 [InputProvider] 作为上传API。
         *
         * @param fileProvider 文件数据 provider
         * @param filename 使用在表单数据中 [HttpHeaders.ContentDisposition] 的 `filename` 属性，
         * 如果为 `null` 则会提供一个默认的文件名称 `unknown-file`。
         */
        @JvmStatic
        @JvmOverloads
        public fun create(fileProvider: InputProvider, filename: String? = null): CreateAssetApi

        /**
         * 提供文件数据的 [ByteReadPacket] 作为上传API。
         *
         * *Note: 需要注意 [ByteReadPacket] 中的数据只能被使用一次。*
         *
         * @param fileByteReadPacket 文件数据 [ByteReadPacket]
         * @param filename 使用在表单数据中 [HttpHeaders.ContentDisposition] 的 `filename` 属性，
         * 如果为 `null` 则会提供一个默认的文件名称 `unknown-file`。
         */
        @JvmStatic
        @JvmOverloads
        public fun create(fileByteReadPacket: ByteReadPacket, filename: String? = null): CreateAssetApi


        /**
         * 提供文件数据的 [ChannelProvider] 作为上传API。
         *
         * @param fileChannelProvider 文件数据 [ChannelProvider]
         * @param filename 使用在表单数据中 [HttpHeaders.ContentDisposition] 的 `filename` 属性，
         * 如果为 `null` 则会提供一个默认的文件名称 `unknown-file`。
         */
        @JvmStatic
        @JvmOverloads
        public fun create(fileChannelProvider: ChannelProvider, filename: String? = null): CreateAssetApi
    }
}

/**
 * [CreateAssetApi] 创建资源后的响应结果
 *
 * @property url 资源的 url
 */
@Serializable
public data class Asset @ApiResultType constructor(val url: String)
