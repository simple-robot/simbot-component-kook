/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.component.kaihieila.message

import kotlinx.serialization.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.api.asset.*
import love.forte.simbot.kaiheila.api.message.*
import love.forte.simbot.message.*
import love.forte.simbot.message.Message
import love.forte.simbot.resources.*

/**
 * 与上传后的媒体资源相关的消息类型。
 *
 * @see SimpleAssetMessage
 * @see AssetImage
 *
 */
@Serializable
public sealed class AssetMessage<M : AssetMessage<M>> : KaiheilaMessageElement<M> {
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

    public companion object Key : Message.Key<AssetMessage<*>> {
        override fun safeCast(value: Any): AssetMessage<*>? = doSafeCast(value)


        /**
         * 使用当前的 [AssetCreated] 构建一个 [AssetMessage] 实例。
         * @param type 在发送时所需要使用的消息类型。通常选择为 [MessageType.IMAGE]、[MessageType.FILE]、[MessageType.VIDEO] 中的值，即 `2`、`3`、`4`。
         */
        @JvmStatic
        public fun AssetCreated.asMessage(type: Int): SimpleAssetMessage = SimpleAssetMessage(this, type)

        /**
         * 使用当前的 [AssetCreated] 构建一个 [AssetMessage] 实例。
         * @param type 在发送时所需要使用的消息类型。通常选择为 [MessageType.IMAGE]、[MessageType.FILE]、[MessageType.VIDEO] 中的值。
         */
        @JvmStatic
        public fun AssetCreated.asMessage(type: MessageType): SimpleAssetMessage = SimpleAssetMessage(this, type)


        /**
         * 使用当前的 [AssetCreated] 构建一个 [AssetImage] 实例。
         */
        @JvmStatic
        public fun AssetCreated.asImage(): AssetImage = AssetImage(this)
    }
}


/**
 *
 * 开黑啦组件中针对 [AssetCreateRequest] api 的请求响应的消息封装。
 *
 * @author ForteScarlet
 */
@SerialName("khl.asset.std")
@Serializable
public data class SimpleAssetMessage(
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
    override val type: Int
) : AssetMessage<SimpleAssetMessage>() {
    public constructor(asset: AssetCreated, type: MessageType) : this(asset, type.type)

    override val key: Message.Key<SimpleAssetMessage>
        get() = Key

    public companion object Key : Message.Key<SimpleAssetMessage> {
        override fun safeCast(value: Any): SimpleAssetMessage? = doSafeCast(value)
    }
}


/**
 * 使用 [AssetCreated] 作为一个 [Image] 消息类型。当前消息的ID等同于 [AssetCreated.url].
 */
@SerialName("khl.asset.img")
@Serializable
public data class AssetImage(override val asset: AssetCreated) : AssetMessage<AssetImage>(), Image<AssetImage> {
    override val type: Int
        get() = MessageType.IMAGE.type

    override val id: ID = asset.url.ID
    private val resource = asset.toResource()

    override suspend fun resource(): Resource = resource

    override val key: Message.Key<AssetImage>
        get() = Key

    public companion object Key : Message.Key<AssetImage> {
        override fun safeCast(value: Any): AssetImage? = doSafeCast(value)
    }

}


