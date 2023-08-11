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
import love.forte.simbot.ID
import love.forte.simbot.JSTP
import love.forte.simbot.definition.ResourceContainer
import love.forte.simbot.delegate.getValue
import love.forte.simbot.delegate.stringID
import love.forte.simbot.kook.api.asset.Asset
import love.forte.simbot.kook.api.asset.CreateAssetApi
import love.forte.simbot.kook.messages.MessageType
import love.forte.simbot.message.Image
import love.forte.simbot.message.Message
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.resources.Resource.Companion.toResource
import love.forte.simbot.resources.URLResource
import java.net.URL

/**
 * 与上传后的媒体资源相关的消息类型。
 *
 * @see KookAsset
 * @see KookAssetImage
 *
 */
@Serializable
public sealed class KookAssetMessage<M : KookAssetMessage<M>> : KookMessageElement<M> {
    abstract override val key: Message.Key<M>

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

    public companion object Key : Message.Key<KookAssetMessage<*>> {
        override fun safeCast(value: Any): KookAssetMessage<*>? = doSafeCast(value)


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
) : KookAssetMessage<KookAsset>(), ResourceContainer {
    public constructor(asset: Asset, type: MessageType) : this(asset, type.type)

    /**
     * 通过 [Asset.url] 构建得到 [URLResource].
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public val urlResource: URLResource = URL(asset.url).toResource()

    /**
     * 通过 [Asset.url] 构建得到 [URLResource].
     *
     * @see urlResource
     */
    @JSTP
    override suspend fun resource(): URLResource = urlResource

    override val key: Message.Key<KookAsset>
        get() = Key

    public companion object Key : Message.Key<KookAsset> {
        override fun safeCast(value: Any): KookAsset? = doSafeCast(value)
    }
}


/**
 * 使用 [Asset] 作为一个 [Image] 消息类型。当前消息的ID等同于 [Asset.url].
 */
@SerialName("kook.asset.img")
@Serializable
public data class KookAssetImage(override val asset: Asset) : KookAssetMessage<KookAssetImage>(),
    Image<KookAssetImage> {
    override val type: Int
        get() = MessageType.IMAGE.type

    override val id: ID by stringID { asset.url }

    /**
     * 通过 [Asset.url] 构建得到 [URLResource].
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public val urlResource: URLResource = URL(asset.url).toResource()

    /**
     * 通过 [Asset.url] 构建得到 [URLResource].
     *
     * @see urlResource
     */
    @JSTP
    override suspend fun resource(): URLResource = urlResource

    override val key: Message.Key<KookAssetImage>
        get() = Key

    public companion object Key : Message.Key<KookAssetImage> {
        override fun safeCast(value: Any): KookAssetImage? = doSafeCast(value)
    }

}


