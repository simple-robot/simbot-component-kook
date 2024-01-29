/*
 * Copyright (c) 2022-2023. ForteScarlet.
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

package love.forte.simbot.component.kook.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.StringID.Companion.ID
import love.forte.simbot.kook.api.asset.Asset
import love.forte.simbot.kook.api.asset.CreateAssetApi
import love.forte.simbot.kook.messages.MessageType
import love.forte.simbot.message.Image
import love.forte.simbot.message.RemoteImage
import kotlin.jvm.JvmStatic

/**
 * 与上传后的媒体资源相关的消息类型。
 *
 * @see KookAsset
 * @see KookAssetImage
 *
 */
@Serializable
public sealed class KookAssetMessage<M : KookAssetMessage<M>> : KookMessageElement<M> {
    /**
     * 创建的文件资源。
     */
    public abstract val asset: Asset

    /**
     * 在发送时所需要使用的消息类型。通常选择为 [MessageType.IMAGE]、[MessageType.FILE]、[MessageType.VIDEO] 中的值，
     * 即 `2`、`3`、`4`。
     *
     * @see MessageType
     */
    public abstract val type: Int

    public companion object {

        /**
         * 使用当前的 [Asset] 构建一个 [KookAssetMessage] 实例。
         * @param type 在发送时所需要使用的消息类型。通常选择为 [MessageType.IMAGE]、[MessageType.FILE]、[MessageType.VIDEO] 中的值，即 `2`、`3`、`4`。
         */
        @JvmStatic
        public fun Asset.asMessage(type: Int): KookAsset = KookAsset(this, type)

        /**
         * 使用当前的 [Asset] 构建一个 [KookAssetMessage] 实例。
         * @param type 在发送时所需要使用的消息类型。通常选择为 [MessageType.IMAGE]、[MessageType.FILE]、[MessageType.VIDEO] 中的值。
         */
        @JvmStatic
        public fun Asset.asMessage(type: MessageType): KookAsset = KookAsset(this, type)


        /**
         * 使用当前的 [Asset] 构建一个 [KookAssetImage] 实例。
         */
        @JvmStatic
        public fun Asset.asImage(): KookAssetImage = KookAssetImage(this)
    }
}

/**
 * Kook 组件中针对 [CreateAssetApi] api 的请求响应的消息封装。
 *
 * @author ForteScarlet
 */
@SerialName("kook.asset.std")
@Serializable
public data class KookAsset(
    /**
     * 创建的文件资源。
     */
    override val asset: Asset,

    /**
     * 在发送时所需要使用的消息类型。通常选择为 [MessageType.IMAGE]、[MessageType.FILE]、[MessageType.VIDEO] 中的值，
     * 即 `2`、`3`、`4`。
     *
     * @see MessageType
     */
    @SerialName("assetType")
    override val type: Int
) : KookAssetMessage<KookAsset>() {
    public constructor(asset: Asset, type: MessageType) : this(asset, type.type)

//    /**
//     * 通过 [Asset.url] 构建得到 [URLResource].
//     */
//    @Suppress("MemberVisibilityCanBePrivate")
//    public val urlResource: URLResource = URL(asset.url).toResource()
//
//    /**
//     * 通过 [Asset.url] 构建得到 [URLResource].
//     *
//     * @see urlResource
//     */
//    @JvmSynthetic
//    override suspend fun resource(): URLResource = urlResource
//
//    @Api4J
//    override val resource: URLResource
//        get() = urlResource
//    @Api4J
//    override val resourceAsync: CompletableFuture<out URLResource>
//        get() = CompletableFuture.completedFuture(urlResource)
}


/**
 * 使用 [Asset] 作为一个 [Image] 消息类型。
 * [Asset] 是上传后的产物，因此 [KookAssetImage] 可以被视为 [RemoteImage]。
 */
@SerialName("kook.asset.img")
@Serializable
public data class KookAssetImage(override val asset: Asset) : KookAssetMessage<KookAssetImage>(), RemoteImage {
    override val type: Int
        get() = MessageType.IMAGE.type

    override val id: ID
        get() = asset.url.ID
}


