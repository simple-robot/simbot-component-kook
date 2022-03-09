package love.forte.simbot.component.kaihieila

import kotlinx.serialization.modules.*
import love.forte.simbot.*

/**
 *
 * 开黑啦组件的 [Component] 实现。
 *
 * @author ForteScarlet
 */
public class KaiheilaComponent : Component {
    override val id: ID
        get() = componentID

    override val componentSerializersModule: SerializersModule
        get() = messageSerializersModule


    /**
     * 组件 [KaiheilaComponent] 的注册器。
     *
     */
    public companion object Registrar : ComponentRegistrar<KaiheilaComponent, KaiheilaComponentConfiguration> {

        /**
         * 组件 [KaiheilaComponent] 的ID
         */
        @Suppress("MemberVisibilityCanBePrivate")
        public const val ID_VALUE: String = "simbot.kaiheila"

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
        public val messageSerializersModule: SerializersModule = SerializersModule {
            //

        }

        override fun register(block: KaiheilaComponentConfiguration.() -> Unit): KaiheilaComponent {
            // nothing config now

            return KaiheilaComponent()
        }
    }

}

/**
 *
 * [KaiheilaComponent] 注册时所使用的配置类。
 *
 */
public class KaiheilaComponentConfiguration


/**
 * [KaiheilaComponent] 的注册器工厂，用于支持组件的自动加载。
 *
 */
public class KaiheilaComponentRegistrarFactory :
    ComponentRegistrarFactory<KaiheilaComponent, KaiheilaComponentConfiguration> {
    override val registrar: ComponentRegistrar<KaiheilaComponent, KaiheilaComponentConfiguration>
        get() = KaiheilaComponent
}
