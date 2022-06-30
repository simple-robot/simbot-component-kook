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

package love.forte.simbot.component.kaiheila.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.Api4J
import love.forte.simbot.ID
import love.forte.simbot.kaiheila.api.asset.AssetCreateRequest
import love.forte.simbot.kaiheila.api.asset.AssetCreated
import love.forte.simbot.kaiheila.api.message.MessageType
import love.forte.simbot.message.Image
import love.forte.simbot.message.Message
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.resources.Resource
import love.forte.simbot.resources.URLResource

/**
 * 与上传后的媒体资源相关的消息类型。
 *
 * @see KaiheilaSimpleAssetMessage
 * @see KaiheilaAssetImage
 *
 */
@Serializable
public sealed class KaiheilaAssetMessage<M : KaiheilaAssetMessage<M>> : KaiheilaMessageElement<M> {
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

    public companion object Key : Message.Key<KaiheilaAssetMessage<*>> {
        override fun safeCast(value: Any): KaiheilaAssetMessage<*>? = doSafeCast(value)


        /**
         * 使用当前的 [AssetCreated] 构建一个 [KaiheilaAssetMessage] 实例。
         * @param type 在发送时所需要使用的消息类型。通常选择为 [MessageType.IMAGE]、[MessageType.FILE]、[MessageType.VIDEO] 中的值，即 `2`、`3`、`4`。
         */
        @JvmStatic
        public fun AssetCreated.asMessage(type: Int): KaiheilaSimpleAssetMessage = KaiheilaSimpleAssetMessage(this, type)

        /**
         * 使用当前的 [AssetCreated] 构建一个 [KaiheilaAssetMessage] 实例。
         * @param type 在发送时所需要使用的消息类型。通常选择为 [MessageType.IMAGE]、[MessageType.FILE]、[MessageType.VIDEO] 中的值。
         */
        @JvmStatic
        public fun AssetCreated.asMessage(type: MessageType): KaiheilaSimpleAssetMessage = KaiheilaSimpleAssetMessage(this, type)


        /**
         * 使用当前的 [AssetCreated] 构建一个 [KaiheilaAssetImage] 实例。
         */
        @JvmStatic
        public fun AssetCreated.asImage(): KaiheilaAssetImage = KaiheilaAssetImage(this)
    }
}


/**
 *
 *  Kook 组件中针对 [AssetCreateRequest] api 的请求响应的消息封装。
 *
 * @author ForteScarlet
 */
@SerialName("khl.asset.std")
@Serializable
public data class KaiheilaSimpleAssetMessage(
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
) : KaiheilaAssetMessage<KaiheilaSimpleAssetMessage>() {
    public constructor(asset: AssetCreated, type: MessageType) : this(asset, type.type)

    override val key: Message.Key<KaiheilaSimpleAssetMessage>
        get() = Key

    public companion object Key : Message.Key<KaiheilaSimpleAssetMessage> {
        override fun safeCast(value: Any): KaiheilaSimpleAssetMessage? = doSafeCast(value)
    }
}


/**
 * 使用 [AssetCreated] 作为一个 [Image] 消息类型。当前消息的ID等同于 [AssetCreated.url].
 */
@SerialName("khl.asset.img")
@Serializable
public data class KaiheilaAssetImage(override val asset: AssetCreated) : KaiheilaAssetMessage<KaiheilaAssetImage>(), Image<KaiheilaAssetImage> {
    override val type: Int
        get() = MessageType.IMAGE.type

    override val id: ID = asset.url.ID

    @OptIn(Api4J::class)
    override val resource: URLResource = asset.toResource()

    @JvmSynthetic
    override suspend fun resource(): Resource = resource

    override val key: Message.Key<KaiheilaAssetImage>
        get() = Key

    public companion object Key : Message.Key<KaiheilaAssetImage> {
        override fun safeCast(value: Any): KaiheilaAssetImage? = doSafeCast(value)
    }

}


