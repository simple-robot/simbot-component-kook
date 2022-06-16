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

package love.forte.simbot.component.kaiheila

import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import love.forte.simbot.*
import love.forte.simbot.component.kaiheila.message.*
import love.forte.simbot.kaiheila.objects.KMarkdown
import love.forte.simbot.kaiheila.objects.RawValueKMarkdown
import love.forte.simbot.message.Message

/**
 *
 * 开黑啦组件的 [Component] 实现。
 *
 * @author ForteScarlet
 */
public class KaiheilaComponent @InternalSimbotApi constructor() : Component {
    /**
     * 组件的唯一标识ID。
     */
    override val id: ID
        get() = componentID
    
    /**
     * 开黑啦组件中所涉及到的序列化模块。
     *
     */
    override val componentSerializersModule: SerializersModule
        get() = messageSerializersModule
    
    override fun toString(): String = TO_STRING_VALUE
    
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is KaiheilaComponent) return false
        
        return id == other.id
    }
    
    override fun hashCode(): Int {
        return id.hashCode()
    }
    
    /**
     * 组件 [KaiheilaComponent] 的注册器。
     *
     */
    public companion object Factory : ComponentFactory<KaiheilaComponent, KaiheilaComponentConfiguration> {
        
        /**
         * 组件 [KaiheilaComponent] 的ID
         */
        @Suppress("MemberVisibilityCanBePrivate")
        public const val ID_VALUE: String = "simbot.kaiheila"
        
        private const val TO_STRING_VALUE = "KaiheilaComponent(id=$ID_VALUE)"
        
        /**
         * 组件的ID实例。
         */
        public val componentID: CharSequenceID = ID_VALUE.ID
        
        /**
         * 注册器的唯一标识。
         */
        override val key: Attribute<KaiheilaComponent> = attribute(ID_VALUE)
        
        /**
         * [KaiheilaComponent] 组件所使用的消息序列化信息。
         */
        @OptIn(ExperimentalSimbotApi::class)
        public val messageSerializersModule: SerializersModule = SerializersModule {
            fun PolymorphicModuleBuilder<KaiheilaMessageElement<*>>.include() {
                subclass(KaiheilaSimpleAssetMessage::class, KaiheilaSimpleAssetMessage.serializer())
                subclass(KaiheilaAssetImage::class, KaiheilaAssetImage.serializer())
                subclass(KaiheilaAtAllHere::class, KaiheilaAtAllHere.serializer())
                subclass(KaiheilaAttachmentMessage::class, KaiheilaAttachmentMessage.serializer())
                subclass(KaiheilaCardMessage::class, KaiheilaCardMessage.serializer())
                subclass(KaiheilaKMarkdownMessage::class, KaiheilaKMarkdownMessage.serializer())
            }
            polymorphic(KMarkdown::class) {
                subclass(RawValueKMarkdown::class, RawValueKMarkdown.serializer())
            }
            
            polymorphic(KaiheilaMessageElement::class) {
                include()
            }
            
            polymorphic(Message.Element::class) {
                include()
            }
        }
        
        /**
         * 构建一个 [KaiheilaComponent] 实例。
         */
        @OptIn(InternalSimbotApi::class)
        override suspend fun create(configurator: KaiheilaComponentConfiguration.() -> Unit): KaiheilaComponent {
            KaiheilaComponentConfiguration.configurator()
            return KaiheilaComponent()
        }
    }
    
}

/**
 *
 * [KaiheilaComponent] 注册时所使用的配置类。
 *
 * 目前对于开黑啦组件来讲没有需要配置的内容，因此 [KaiheilaComponentConfiguration] 中暂无可配置属性。
 *
 */
public object KaiheilaComponentConfiguration


/**
 * [KaiheilaComponent] 的注册器工厂，用于支持组件的自动加载。
 *
 */
public class KaiheilaComponentAutoRegistrarFactory :
    ComponentAutoRegistrarFactory<KaiheilaComponent, KaiheilaComponentConfiguration> {
    override val registrar: KaiheilaComponent.Factory
        get() = KaiheilaComponent
}
