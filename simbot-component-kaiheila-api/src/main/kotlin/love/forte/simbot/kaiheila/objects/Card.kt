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

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import love.forte.simbot.Simbot


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
@Serializable(CardMessageSerializer::class)
public data class CardMessage(public val cards: List<Card>) : List<Card> by cards {
    init {
        check(cards.size <= 5) { "A card message can only allow up to 5 cards." }
    }

    /**
     * 将当前的cardMessage序列化为所需json数据字符串。
     *
     * 如果 json configuration 中的 [JsonConfiguration.classDiscriminator] != 'type',
     * 则会重构一个新的 [Json] 使用。
     *
     */
    @JvmOverloads
    public fun decode(decoder: Json = DEFAULT_DECODER): String {
        if (decoder.configuration.classDiscriminator == "type") {
            return decoder.encodeToString(serializer(), this)
        }
        val decoder0 = Json(from = decoder) {
            classDiscriminator = "type"
        }

        return decoder0.encodeToString(serializer(), this)

    }

    public companion object Serializer {
        private val DEFAULT_DECODER: Json = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
    }
}

public object CardMessageSerializer : KSerializer<CardMessage> {
    private val listSerializer = ListSerializer(Card.serializer())
    override fun deserialize(decoder: Decoder): CardMessage {
        val cards = decoder.decodeSerializableValue(listSerializer)
        return CardMessage(cards)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor = listSerialDescriptor(listSerializer.descriptor)

    override fun serialize(encoder: Encoder, value: CardMessage) {
        return listSerializer.serialize(encoder, value)
    }
}


@Serializable
public enum class Theme {
    @SerialName("primary")
    PRIMARY,

    @SerialName("success")
    SUCCESS,

    @SerialName("danger")
    DANGER,

    @SerialName("warning")
    WARNING,

    @SerialName("info")
    INFO,

    @SerialName("secondary")
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
public enum class Size {

    @SerialName("xs")
    XS,

    @SerialName("sm")
    SM,

    @SerialName("md")
    MD,

    @SerialName("lg")
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
 * [卡片](https://developer.kaiheila.cn/doc/cardmessage#%E5%8D%A1%E7%89%87).
 *
 * e.g.
 *
 * ```json
 * {
 *     "type": "card",
 *     "theme" : "primary|warning|danger|info|none...", // 卡片风格，默认为primay
 *     "color":"#aaaaaa", //色值
 *     "size": "sm|lg", //目前只支持sm与lg, 如果不填为lg。 lg仅在PC端有效, 在移动端不管填什么，均为sm。
 *     "modules": [
 *         // modules...
 *     ]
 * }
 * ```
 *
 */
@Serializable
@SerialName("card")
public data class Card @JvmOverloads constructor(
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
    val modules: List<CardModule>,
)


/**
 * [元素](https://developer.kaiheila.cn/doc/cardmessage#%E5%85%83%E7%B4%A0)
 *
 */
@Serializable
public sealed class CardElement {

    /**
     * 标记普通的文本消息类型，即指 [PlainText] 和 [KMarkdown].
     */
    @Serializable
    public sealed class Text : CardElement()

    /**
     * 普通文本
     * - 为了方便书写，所有plain-text的使用处可以简单的用字符串代替。
     * - 最大2000个字
     *
     * @property content 文本内容
     * @property emoji 如果为true,会把emoji的shortcut转为emoji
     */
    @Serializable
    @SerialName(PlainText.TYPE)
    public data class PlainText(
        /**
         * 为了方便书写，所有plain-text的使用处可以简单的用字符串代替。
         */
        val content: String,
        /**
         * emoji为布尔型，默认为true。如果为true,会把emoji的shortcut转为emoji
         */
        val emoji: Boolean = true,
    ) : Text() {
        public companion object {
            public const val TYPE: String = "plain-text"
        }
    }


    /**
     * ## kmarkdown
     *
     * 作用说明： 显示文字。
     *
     * e.g.
     *
     * ```json
     * {
     *     "type": "kmarkdown",
     *     "content" : "**hello**",
     * }
     * ```
     *
     */
    @Serializable
    @SerialName(KMarkdown.TYPE)
    public data class KMarkdown(
        /**
         * KMarkDown内容字符串。
         */
        public val content: String
    ) : Text() {
        public constructor(content: love.forte.simbot.kaiheila.objects.KMarkdown) : this(content.rawContent)

        public companion object {
            /**
             * [KMarkdown] 的 type 值。
             */
            public const val TYPE: String = "kmarkdown"
        }
    }


    /**
     * 图片
     *
     * 作用说明： 显示图片。
     *
     * 规则：
     * - 图片类型（MimeType）限制说明：目前仅支持image/jpeg, image/gif, image/png这 3 种
     * - 图片的 [size] 默认为 lg
     *
     * e.g.
     * ```json
     * {
     *     "type": "image",
     *     "src" : "",
     *     "alt" : "",
     *     "size" : "sm|lg", // size只用在图文混排  图片组大小固定
     *     "circle" : true|false,
     * }
     * ```
     */
    @Serializable
    @SerialName(Image.TYPE)
    public data class Image @JvmOverloads constructor(
        public val src: String,
        public val alt: String,
        public val size: Size = Size.Default,
        public val circle: Boolean = false
    ) : CardElement() {
        public companion object {
            public const val TYPE: String = "image"
        }
    }

    /**
     * 按钮
     *
     * 作用说明： 提供交互的功能
     *
     * 规则：
     * - [value] 只能为 [String]
     * - [text] 可以为 [PlainText], [KMarkdown]
     * - [click] 代表用户点击的事件,默认为""，代表无任何事件。
     * - 当为 link 时，会跳转到 value 代表的链接;
     * - 当为 return-val 时，系统会通过系统消息将消息 id,点击用户 id 和 value 发回给发送者，
     * 发送者可以根据自己的需求进行处理,消息事件参见[button 点击事件](https://developer.kaiheila.cn/doc/event/user#Card%20%E6%B6%88%E6%81%AF%E4%B8%AD%E7%9A%84%20Button%20%E7%82%B9%E5%87%BB%E4%BA%8B%E4%BB%B6)。
     * 私聊和频道内均可使用按钮点击事件。
     *
     *
     * e.g.
     * ```json
     * {
     *     "type": "button",
     *     "theme": "primary|warning|info|danger|...", //按钮的主题颜色
     *     "value": "", //要传递的value，为string
     *     "click": "", //click时的事件类型， return-val 返回value值
     *     "text": "",
     * }
     * ```
     *
     */
    @Serializable
    @SerialName(Button.TYPE)
    public data class Button @JvmOverloads constructor(
        public val theme: Theme = Theme.Default,
        public val value: String = "",
        public val click: String = "",
        public val text: Text
    ) : CardElement() {
        public companion object {
            public const val TYPE: String = "button"
        }
    }


    /**
     * [结构体](https://developer.kaiheila.cn/doc/cardmessage#%E7%BB%93%E6%9E%84%E4%BD%93)
     * 的 **区域文本**。
     *
     * 作用说明： 支持分栏结构，将模块分为左右两栏，根据顺序自动排列，支持更自由的文字排版模式，提高可维护性
     *
     * e.g.
     * ```json
     * {
     *     "type": "paragraph",
     *     "cols": 3, //移动端忽略该参数
     *     "fields" : [
     *     ],
     * }
     * ```
     *
     * 规则：
     * - [cols] 是 int,可以的取值为 1-3
     * - [fields] 可以的元素为 [text][PlainText] 或 [kmarkdown][KMarkdown].
     * - `paragraph` 最多有 50 个元素
     *
     */
    @Serializable
    @SerialName(Paragraph.TYPE)
    public data class Paragraph(
        public val cols: Int,
        public val fields: List<Text>
    ) : CardElement() {
        init {
            Simbot.require(cols in 1..3) { "Cols must in 1..3, but $cols" }
        }

        public companion object {
            public const val TYPE: String = "paragraph"
        }
    }


}


/**
 * [模块](https://developer.kaiheila.cn/doc/cardmessage#%E6%A8%A1%E5%9D%97)
 *
 */
@Serializable
public sealed class CardModule {

    /**
     * 标题[模块](https://developer.kaiheila.cn/doc/cardmessage#%E6%A8%A1%E5%9D%97)
     *
     * 标题模块只能支持展示标准文本（text），突出标题样式。
     *
     * 说明：
     * - text 为文字元素且 content 不能超过 100 个字
     *
     * e.g.
     * ```json
     * {
     *     "type": "header",
     *     "text" : {
     *         "type": "plain-text",
     *         "content": "标题1"
     *     }
     * }
     * ```
     *
     */
    @Serializable
    @SerialName(Header.TYPE)
    public data class Header(public val text: String) : CardModule() {
        init {
            check(text.length <= 100) { "Content text length can only allow up to 100." }
        }

        public companion object {
            public const val TYPE: String = "header"
        }
    }

    /**
     * Sect内容模块
     *
     * 作用说明： 结构化的内容，显示文本+其它元素。
     *
     * 说明：
     *  - [text] 可以为 [plain-text][CardElement.PlainText], [kmarkdown][CardElement.KMarkdown] 或者 [paragraph][CardElement.Paragraph].
     *  - [accessory] 可以为 [image][CardElement.Image] 或者 [button][CardElement.Button].
     *  - `button` 不能放置在左侧
     *  - [mode] 代表 accessory 是放置在左侧还是在右侧
     *
     * e.g.
     * ```json
     * {
     *     "type": "section",
     *     "mode" : "left|right", //accessory在左侧还是在右侧
     *     "text" : {
     *         "type": "plain-text|kmarkdown|paragraph",
     *         //...
     *     },
     *     "accessory": {
     *         "type": "image|button",
     *         //...
     *     }
     * }
     * ```
     *
     */
    @Serializable
    @SerialName(Section.TYPE)
    public data class Section @JvmOverloads constructor(
        /**
         * accessory在左侧还是在右侧
         */
        public val mode: SectionMode = SectionMode.LEFT,
        public val text: CardElement,
        public val accessory: CardElement,

        ) : CardModule() {
        init {
            Simbot.require(text is CardElement.Text || text is CardElement.Paragraph) {
                "The type of 'text' must be one of CardElement.Text(plain-text or kmarkdown) or CardElement.Paragraph"
            }
            Simbot.require(accessory is CardElement.Image || accessory is CardElement.Button) {
                "The type of 'accessory' must be one of CardElement.Image or CardElement.Button"
            }
        }


        public companion object {
            public const val TYPE: String = "section"
        }
    }

    /**
     * [Section] 中的 [Section.mode] 属性类型，代表 [Section.accessory] 是放置在左侧还是在右侧。
     */
    @Serializable
    public enum class SectionMode {
        /**
         * 左侧
         */
        @SerialName("left")
        LEFT,

        /**
         * 右侧
         */
        @SerialName("right")
        RIGHT
    }

    private abstract class CardElementListWithTypesSerializer<E : CardElement> : KSerializer<List<E>> {
        private val serializer = ListSerializer(CardElement.serializer())

        @OptIn(ExperimentalSerializationApi::class)
        override val descriptor: SerialDescriptor = listSerialDescriptor(serializer.descriptor)

        protected abstract fun List<CardElement>.filter(): List<E>

        override fun deserialize(decoder: Decoder): List<E> {
            return serializer.deserialize(decoder).filter()
        }

        override fun serialize(encoder: Encoder, value: List<E>) {
            serializer.serialize(encoder, value)
        }

    }

    private object CardImageListWithTypeSerializer : CardElementListWithTypesSerializer<CardElement.Image>() {
        override fun List<CardElement>.filter(): List<CardElement.Image> {
            return filterIsInstance<CardElement.Image>()
        }
    }

    private object CardButtonListWithTypeSerializer : CardElementListWithTypesSerializer<CardElement.Button>() {
        override fun List<CardElement>.filter(): List<CardElement.Button> {
            return filterIsInstance<CardElement.Button>()
        }
    }

    /**
     * 图片组模块
     *
     * 作用说明： 1 到多张图片的组合
     *
     * 说明：
     * - [elements] 只能有 [image][CardElement.Image] 元素，只能有 1-9 张图片
     *
     * e.g.
     * ```json
     * {
     *     "type" : "image-group",
     *     "elements": [
     *         //图片元素，其它元素无效
     *     ],
     * }
     * ```
     *
     */
    @Serializable
    @SerialName(ImageGroup.TYPE)
    public data class ImageGroup(@Serializable(CardImageListWithTypeSerializer::class) public val elements: List<CardElement.Image>) :
        CardModule() {
        init {
            Simbot.require(elements.size in 1..9) { "The size of elements must be 1..9, but ${elements.size}" }
        }

        public companion object {
            public const val TYPE: String = "image-group"
        }

    }


    /**
     * 容器模块
     *
     * 作用说明： 1 到多张图片的组合，与图片组模块不同，图片并不会裁切为正方形。多张图片会纵向排列。
     *
     * 说明：
     * - elements 只能有 image 元素，只能有 1-9 张图片
     *
     *  e.g.
     * ```json
     * {
     *     "type" : "container",
     *     "elements": [
     *         //图片元素，其它元素无效
     *     ],
     * }
     * ```
     *
     */
    @Serializable
    @SerialName(Container.TYPE)
    public data class Container(@Serializable(CardImageListWithTypeSerializer::class) public val elements: List<CardElement.Image>) :
        CardModule() {
        init {
            Simbot.require(elements.size in 1..9) { "The size of elements must be 1..9, but ${elements.size}" }
        }

        public companion object {
            public const val TYPE: String = "container"
        }
    }


    /**
     * 交互模块
     *
     * 作用说明： 交互模块中包含交互控件元素，目前支持的交互控件为按钮（button）
     *
     * 说明：
     * - elements 中只能为 button
     * - elements 最多只能有 4 个
     *
     *  e.g.
     * ```json
     * {
     *     "type" : "action-group",
     *     "elements": [
     *         // buttons
     *     ],
     * }
     * ```
     *
     */
    @Serializable
    @SerialName(ActionGroup.TYPE)
    public data class ActionGroup(@Serializable(CardButtonListWithTypeSerializer::class) public val elements: List<CardElement.Button>) :
        CardModule() {
        init {
            Simbot.require(elements.size <= 4) { "The size of elements must be <= 4, but ${elements.size}" }
        }

        public companion object {
            public const val TYPE: String = "action-group"
        }
    }


    /**
     * 备注模块
     *
     * 作用说明： 展示图文混合的内容。
     *
     * 说明：
     * - elements 中可以为：[plain-text][CardElement.PlainText], [kmarkdown][CardElement.KMarkdown], [image][CardElement.Image].
     * - elements 最多可包含 10 个元素
     *
     * e.g.
     * ```json
     * {
     *     "type": "context",
     *     "elements": [],
     * }
     * ```
     *
     */
    @Serializable
    @SerialName(Context.TYPE)
    public data class Context(public val elements: List<CardElement>) {
        init {
            Simbot.require(elements.size <= 10) { "The size of elements must be <= 10, but ${elements.size}" }
            Simbot.require(elements.all { it is CardElement.Text || it is CardElement.Image }) {
                "The type of element must be one of 'CardElement.Text(plain-text or kmarkdown)' or 'CardElement.Image'"
            }
        }

        public companion object {
            public const val TYPE: String = "context"
        }
    }


    /**
     * 分割线模块
     *
     * 作用说明： 展示分割线。
     *
     * e.g.
     * ```json
     * {
     *     "type": "divider",
     * }
     * ```
     *
     *
     */
    @Serializable
    @SerialName(Divider.TYPE)
    public object Divider : CardElement() {
        public const val TYPE: String = "divider"
    }


    /**
     * 文件模块
     *
     * 作用说明： 展示文件，目前有三种，文件，视频和音频。
     *
     */
    @Serializable
    public sealed class Files : CardElement() {
        /**
         * 文件|音频|视频地址
         */
        public abstract val src: String

        /**
         * 标题
         */
        public abstract val title: String

        /**
         * 封面图
         */
        public abstract val cover: String


        /**
         * 文件。
         */
        @Serializable
        @SerialName(FILE_TYPE)
        public data class File(override val src: String, override val title: String, override val cover: String) :
            Files()

        /**
         * 音频。
         */
        @Serializable
        @SerialName(AUDIO_TYPE)
        public data class Audio(override val src: String, override val title: String, override val cover: String) :
            Files()

        /**
         * 视频。
         */
        @Serializable
        @SerialName(VIDEO_TYPE)
        public data class Video(override val src: String, override val title: String, override val cover: String) :
            Files()

        public companion object {
            public const val FILE_TYPE: String = "file"
            public const val AUDIO_TYPE: String = "audio"
            public const val VIDEO_TYPE: String = "video"

            @JvmStatic
            public fun file(src: String, title: String, cover: String): File = File(src, title, cover)

            @JvmStatic
            public fun audio(src: String, title: String, cover: String): Audio = Audio(src, title, cover)

            @JvmStatic
            public fun video(src: String, title: String, cover: String): Video = Video(src, title, cover)

        }
    }

    /**
     * 倒计时模块
     *
     * 作用说明： 展示倒计时。
     *
     * 规则：
     * - [mode] 主要是倒计时的样式，目前支持三种样式。
     * - [startTime] 和 [endTime] 为毫秒时间戳，[startTime] 和 [endTime] 不能小于服务器当前时间戳。
     *
     * e.g.
     * ```json
     *{
     *     "type": "countdown",
     *     "endTime" : 1608819168000, //到期的毫秒时间戳
     *     "startTime" : 1608819168000, //起始的毫秒时间戳，仅当mode为second才有这个字段
     *     "mode" : "day,hour,second", //倒计时样式, 按天显示，按小时显示或者按秒显示
     * }
     * ```
     *
     */
    @Serializable
    @SerialName(Countdown.TYPE)
    public data class Countdown(
        public val startTime: Long,
        public val endTime: Long,
        public val mode: CountdownMode
    ) : CardModule() {
        public companion object {
            public const val TYPE: String = "countdown"
        }
    }

    /**
     * 倒计时样式, 按天显示，按小时显示或者按秒显示。
     *
     * 用于 [Countdown] 中的 [Countdown.mode] 属性
     */
    @Serializable
    public enum class CountdownMode {
        @SerialName("day")
        DAY,

        @SerialName("hour")
        HOUR,

        @SerialName("second")
        SECOND
    }


    /**
     * 邀请模块
     *
     * 作用说明： 提供服务器邀请/语音频道邀请
     *
     * e.g.
     * ```json
     * { "type": "invite", "code": "邀请链接或者邀请码" }
     * ```
     *
     */
    @Serializable
    @SerialName(Invite.TYPE)
    public data class Invite(
        public val code: String
    ) : CardModule() {
        public companion object {
            public const val TYPE: String = ""
        }
    }


}







