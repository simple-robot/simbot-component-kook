/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kook 的一部分。
 *
 *  simbot-component-kook 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kook 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

package love.forte.simbot.component.kook.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.message.AtAll
import love.forte.simbot.message.Message


/**
 *
 * 通知(mention)所有当前的 **在线用户**。
 *
 * 行为与概念与 [AtAll] 类似。
 *
 * @see love.forte.simbot.kook.objects.AtTarget.Here
 *
 */
@SerialName("kook.AtAllHere")
@Serializable
public object KookAtAllHere : KookMessageElement<KookAtAllHere>, Message.Key<KookAtAllHere> {
    override val key: Message.Key<KookAtAllHere>
        get() = this

    override fun equals(other: Any?): Boolean = other === this

    override fun toString(): String = "AtAllHere"

    override fun safeCast(value: Any): KookAtAllHere? = if (value === this) this else null

}
