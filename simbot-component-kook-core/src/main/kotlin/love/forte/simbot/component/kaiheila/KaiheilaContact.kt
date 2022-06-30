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

import love.forte.simbot.ID
import love.forte.simbot.definition.Contact
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageReceipt
import love.forte.simbot.kaiheila.objects.User as KhlUser

/**
 *
 * 开黑啦组件下的联系人信息。
 *
 * @author ForteScarlet
 */
public interface KaiheilaContact : Contact {

    /**
     * id。
     */
    override val id: ID get() = sourceUser.id

    /**
     * 用户名。
     */
    override val username: String get() = sourceUser.username

    /**
     * 开黑啦的源用户信息。
     */
    public val sourceUser: KhlUser

    /**
     * 联系人所属bot。
     */
    override val bot: KaiheilaComponentBot

    /**
     * 发送消息。
     */
    @JvmSynthetic
    override suspend fun send(message: Message): MessageReceipt


    /**
     * 头像。
     */
    override val avatar: String get() = sourceUser.avatar




}