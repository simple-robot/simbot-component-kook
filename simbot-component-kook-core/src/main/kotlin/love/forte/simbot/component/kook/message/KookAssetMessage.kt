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

package love.forte.simbot.component.kook.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.Api4J
import love.forte.simbot.ID
import love.forte.simbot.kook.api.asset.AssetCreateRequest
import love.forte.simbot.kook.api.asset.AssetCreated
import love.forte.simbot.kook.api.message.MessageType
import love.forte.simbot.message.Image
import love.forte.simbot.message.Message
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.resources.Resource
import love.forte.simbot.resources.URLResource

/**
 * 与上传后的媒体资源相关的消息类型。
 *
 * @see KookSimpleAssetMessage
 * @see KookAssetImage
 *
 */
@Serializable
public sealed class KookAssetMessage<M : KookAssetMessage<M>> : KookMessageElement<M> {
    abstract override val key: Message.Key<M>

    /**
     * 创建的文件资源。
     */
    public abstract val asset: AssetCreated

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
         * 使用当前的 [AssetCreated] 构建一个 [KookAssetMessage] 实例。
         * @param type 在发送时所需要使用的消息类型。通常选择为 [MessageType.IMAGE]、[MessageType.FILE]、[MessageType.VIDEO] 中的值，即 `2`、`3`、`4`。
         */
        @JvmStatic
        public fun AssetCreated.asMessage(type: Int): KookSimpleAssetMessage = KookSimpleAssetMessage(this, type)

        /**
         * 使用当前的 [AssetCreated] 构建一个 [KookAssetMessage] 实例。
         * @param type 在发送时所需要使用的消息类型。通常选择为 [MessageType.IMAGE]、[MessageType.FILE]、[MessageType.VIDEO] 中的值。
         */
        @JvmStatic
        public fun AssetCreated.asMessage(type: MessageType): KookSimpleAssetMessage = KookSimpleAssetMessage(this, type)


        /**
         * 使用当前的 [AssetCreated] 构建一个 [KookAssetImage] 实例。
         */
        @JvmStatic
        public fun AssetCreated.asImage(): KookAssetImage = KookAssetImage(this)
    }
}


/**
 *
 * Kook 组件中针对 [AssetCreateRequest] api 的请求响应的消息封装。
 *
 * @author ForteScarlet
 */
@SerialName("kook.asset.std")
@Serializable
public data class KookSimpleAssetMessage(
    /**
     * 创建的文件资源。
     */
    override val asset: AssetCreated,

    /**
     * 在发送时所需要使用的消息类型。通常选择为 [MessageType.IMAGE]、[MessageType.FILE]、[MessageType.VIDEO] 中的值，
     * 即 `2`、`3`、`4`。
     *
     * @see MessageType
     */
    @SerialName("assetType")
    override val type: Int
) : KookAssetMessage<KookSimpleAssetMessage>() {
    public constructor(asset: AssetCreated, type: MessageType) : this(asset, type.type)

    override val key: Message.Key<KookSimpleAssetMessage>
        get() = Key

    public companion object Key : Message.Key<KookSimpleAssetMessage> {
        override fun safeCast(value: Any): KookSimpleAssetMessage? = doSafeCast(value)
    }
}


/**
 * 使用 [AssetCreated] 作为一个 [Image] 消息类型。当前消息的ID等同于 [AssetCreated.url].
 */
@SerialName("kook.asset.img")
@Serializable
public data class KookAssetImage(override val asset: AssetCreated) : KookAssetMessage<KookAssetImage>(), Image<KookAssetImage> {
    override val type: Int
        get() = MessageType.IMAGE.type

    override val id: ID = asset.url.ID

    @OptIn(Api4J::class)
    override val resource: URLResource = asset.toResource()

    @JvmSynthetic
    override suspend fun resource(): Resource = resource

    override val key: Message.Key<KookAssetImage>
        get() = Key

    public companion object Key : Message.Key<KookAssetImage> {
        override fun safeCast(value: Any): KookAssetImage? = doSafeCast(value)
    }

}


