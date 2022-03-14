/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
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

@file:Suppress("unused")

package love.forte.simbot.kaiheila.objects

import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*
import love.forte.simbot.*


/**
 * [卡片消息](https://developer.kaiheila.cn/doc/cardmessage)
 *
 * > `cardmessage` 主要由json构成，在卡片消息中，有四种类别的卡片结构：
 *
 *     - 卡片，目前只有card。
 *     - 模块，主要有section, header, context等。
 *     - 元素：主要有plain-text, image, button等。
 *     - 结构：目前只有paragraph。
 *
 *  消息的主要结构
 *
 *     - 一个卡片消息最多只允许5个卡片
 *     - 一个卡片可以有多个模块，但是一个卡片消息总共不允许超过50个模块
 *
 *     ```json
 *     [
 *      {
 *          "type": "card",
 *              //...
 *              "modules" : [
 *                  // ...
 *              ]
 *          }
 *          // 其它card
 *      ]
 *     ```
 *
 * 主要结构说明
 * 所有的元素都有相似的结构，大体如下：
 * ```json
 * {
 *     "type" : "类别"，
 *     "foo" : "bar",   //属性参数
 *     "modules|elements|fields": [], //子元素，根据type类别有不同的值，卡片的为modules,模块的子元素为elements,结构的为fields。
 * }
 * ```
 * @author ForteScarlet
 */
@ExperimentalSimbotApi
@Serializable(CardMessageSerializer::class)
public class CardMessage(private val delegate: List<Card>) : List<Card> by delegate {
    init {
        check(delegate.size <= 5) { "A card message can only allow up to 5 cards." }
    }

    public fun decode(decoder: Json = DEFAULT_DECODER): String {
        return decoder.encodeToString(serializer(), this)
    }

    public companion object Serializer {
        private val DEFAULT_DECODER: Json = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }

        // public val serializer = ListSerializer(Card.serializer())

        // public fun <T : Card> serializer(elementSerializer: KSerializer<T>): KSerializer<List<T>> =
        //     ListSerializer(elementSerializer)
    }
}

@ExperimentalSimbotApi
public object CardMessageSerializer : KSerializer<CardMessage> {
    private val listSerializer = ListSerializer(Card.serializer())
    override fun deserialize(decoder: Decoder): CardMessage {
        val cards = decoder.decodeSerializableValue(listSerializer)
        return CardMessage(cards)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor =
        listSerialDescriptor(listSerializer.descriptor) // SerialDescriptor("CardMessage", listSerializer.descriptor)

    override fun serialize(encoder: Encoder, value: CardMessage) {
        return listSerializer.serialize(encoder, value)
    }
}


@Serializable
@OptIn(ExperimentalSerializationApi::class)
public enum class Theme {
    @SerialName("primary")
    @JsonNames("primary", "PRIMARY")
    PRIMARY,

    @SerialName("success")
    @JsonNames("success", "SUCCESS")
    SUCCESS,

    @SerialName("danger")
    @JsonNames("danger", "DANGER")
    DANGER,

    @SerialName("warning")
    @JsonNames("warning", "WARNING")
    WARNING,

    @SerialName("info")
    @JsonNames("info", "INFO")
    INFO,

    @SerialName("secondary")
    @JsonNames("secondary", "SECONDARY")
    SECONDARY,
    ;

    public companion object {
        @JvmStatic
        @get:JvmName("getDefault")
        public val Default: Theme
            get() = PRIMARY
    }
}

@Serializable
@OptIn(ExperimentalSerializationApi::class)
public enum class Size {

    @SerialName("xs")
    @JsonNames("xs", "XS")
    XS,

    @SerialName("sm")
    @JsonNames("sm", "SM")
    SM,

    @SerialName("md")
    @JsonNames("md", "MD")
    MD,

    @SerialName("lg")
    @JsonNames("lg", "LG")
    LG,
    ;

    public companion object {
        @JvmStatic
        @get:JvmName("getDefault")
        public val Default: Size
            get() = LG
    }
}


/**
 * [CardMessage] 中的卡片元素.
 *
 */
@Serializable
@ExperimentalSimbotApi
public sealed class Card {
    public abstract val type: String


    /**
     * [卡片](https://developer.kaiheila.cn/doc/cardmessage#%E5%8D%A1%E7%89%87)
     *
     */
    @Serializable
    public data class Card(
        /**
         * 卡片风格，默认为primary
         */
        val theme: Theme = Theme.PRIMARY,

        /**
         * 色值
         */
        val color: String? = null,

        /**
         * 目前只支持sm与lg, 如果不填为lg。 lg仅在PC端有效, 在移动端不管填什么，均为sm。
         */
        val size: Size = Size.SM,

        /**
         * modules只能为模块.
         * - 单个card模块数量不限制，但是总消息最多只能有50个模块.
         * - theme, size参见全局字段说明 ,卡片中，size只允许lg和sm
         * - color代表卡片边框具体颜色，如果填了，则使用该color，如果未填，则使用theme来渲染卡片颜色。
         */
        val modules: List<@Contextual Module>,
    ) : love.forte.simbot.kaiheila.objects.Card() {
        override val type: String
            get() = "card"

    }


    /**
     * [元素](https://developer.kaiheila.cn/doc/cardmessage#%E5%85%83%E7%B4%A0)
     *
     */
    @Serializable
    public abstract class Element(override val type: String) : love.forte.simbot.kaiheila.objects.Card() {

        /**
         * 普通文本
         * - 为了方便书写，所有plain-text的使用处可以简单的用字符串代替。
         * - 最大2000个字
         *
         * @property content
         * @property emoji
         * @constructor Create empty Plain text
         */
        @Serializable
        public data class PlainText(
            /**
             * 为了方便书写，所有plain-text的使用处可以简单的用字符串代替。
             */
            val content: String,
            /**
             * emoji为布尔型，默认为true。如果为true,会把emoji的shortcut转为emoji
             */
            val emoji: Boolean = true,
        ) : Element("plain-text")


    }


    /**
     * [模块](https://developer.kaiheila.cn/doc/cardmessage#%E6%A8%A1%E5%9D%97)
     * @param type
     */
    @Serializable
    public abstract class Module(override val type: String) : love.forte.simbot.kaiheila.objects.Card() {
        // companion object : SerializerModuleRegistrar {
        //     override fun SerializersModuleBuilder.serializerModule() {
        //         polymorphic(Module::class) {
        //             subclass(Header::class)
        //         }
        //     }
        // }


        @Serializable
        public data class Header(val text: String) : Module("header") {
            init {
                check(text.length <= 100) { "Content text length can only allow up to 100." }
            }
        }

        /**
         * Sect内容模块
         * 作用说明： 结构化的内容，显示文本+其它元素。ion
         *
         */
        @Serializable
        public data class Section(
            val mode: String = "left"
        ) : Module("section")

    }


}





