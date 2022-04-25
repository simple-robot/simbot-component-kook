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
import love.forte.simbot.message.At
import love.forte.simbot.message.AtAll
import love.forte.simbot.message.Message


/**
 *
 * 通知(mention)所有当前的 **在线用户**。
 *
 * 行为与概念与 [AtAll] 类似。
 *
 * **注意，目前来看，发消息bot无法at他人，
 * 因此目前此参数只能来自于事件，发送时会被忽略。
 * 此情况也适用于 [At] 和 [AtAll]。**
 *
 */
@SerialName("khl.AtAllHere")
@Serializable
public object KaiheilaAtAllHere : KaiheilaMessageElement<KaiheilaAtAllHere>, Message.Key<KaiheilaAtAllHere> {
    override val key: Message.Key<KaiheilaAtAllHere>
        get() = this

    override fun equals(other: Any?): Boolean = other === this

    override fun toString(): String = "AtAllHere"

    override fun safeCast(value: Any): KaiheilaAtAllHere? = if (value === this) this else null

}
