package love.forte.simbot.component.kaihieila

import love.forte.simbot.*
import love.forte.simbot.kaiheila.*


/**
 *
 * 开黑啦组件在simbot下的组件 [Bot] 实现。
 *
 * @author ForteScarlet
 */
public abstract class KaiheilaComponentBot : Bot {
    /**
     * botID。此id通常代表 client id, 即 [KaiheilaBot.Ticket.clientId].
     */
    override val id: ID get() = sourceBot.ticket.clientId

    /**
     * 判断此ID是否代表当前bot。可能代表bot的id有可能是 [clientId][KaiheilaBot.Ticket.clientId],
     * 也有可能是此bot在系统中作为User时候的 user id.
     *
     * 对于 user id 的判断，只有当至少执行过一次 [start] 来启动bot的时候才会生效匹配，在那之前将只会通过 [id] 进行匹配。
     *
     */
    abstract override fun isMe(id: ID): Boolean

    /**
     * 得到在stdlib标准库模块下所提供的开黑啦bot实例。
     */
    public abstract val sourceBot: KaiheilaBot

    /**
     * 得到对应的组件实例。
     */
    abstract override val component: KaiheilaComponent





    // todo ..
}