package love.forte.simbot.component.kaihieila.message

import kotlinx.serialization.*
import love.forte.simbot.message.*


/**
 *
 * 通知(mention)所有当前的 **在线用户**。
 *
 * 行为与概念与 [AtAll] 类似。
 *
 */
@SerialName("khl.AtAllHere")
@Serializable
public object AtAllHere : Message.Element<AtAllHere>, Message.Key<AtAllHere> {
    override val key: Message.Key<AtAllHere>
        get() = this

    override fun equals(other: Any?): Boolean = other === this

    override fun toString(): String = "AtAllHere"

    override fun safeCast(value: Any): AtAllHere? = if (value === this) this else null

}



/**
 * @see AtAllHere
 */
public typealias MentionAllHere = AtAllHere