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

@file:JvmName("UserEvents")

package love.forte.simbot.kaiheila.event.system.user

import kotlinx.serialization.*
import love.forte.simbot.kaiheila.event.*


/**
 * [用户相关事件](https://developer.kaiheila.cn/doc/event/user) 的子事件type常量类。
 */
public object UserEventSubTypeConstants {
    /**
     * @see SelfExitedGuildEventBody
     */
    public const val SELF_EXITED_GUILD: String = "self_exited_guild"

    /**
     * @see SelfJoinedGuildEventBody
     */
    public const val SELF_JOINED_GUILD: String = "self_joined_guild"

    /**
     * @see UserExitedChannelEventBody
     */
    public const val EXITED_CHANNEL: String = "exited_channel"

    /**
     * @see UserJoinedChannelEventBody
     */
    public const val JOINED_CHANNEL: String = "joined_channel"

    /**
     * @see UserUpdatedEventBody
     */
    public const val USER_UPDATED: String = "user_updated"

    /**
     * @see MessageBtnClickEventBody
     */
    public const val MESSAGE_BTN_CLICK: String = "message_btn_click"

}


internal fun MutableMap<Any, EventParser<*, *>>.userEventParsers() {
    registerParsers<SelfExitedGuildEventBody>(
        UserEventSubTypeConstants.SELF_EXITED_GUILD,
        SelfExitedGuildEventBodyImpl.serializer()
    )

    registerParsers<SelfJoinedGuildEventBody>(
        UserEventSubTypeConstants.SELF_JOINED_GUILD,
        SelfJoinedGuildEventBodyImpl.serializer()
    )


    registerParsers<UserExitedChannelEventBody>(
        UserEventSubTypeConstants.EXITED_CHANNEL,
        UserExitedChannelEventBodyImpl.serializer()
    )

    registerParsers<UserJoinedChannelEventBody>(
        UserEventSubTypeConstants.JOINED_CHANNEL,
        UserJoinedChannelEventBodyImpl.serializer()
    )


    registerParsers<UserUpdatedEventBody>(
        UserEventSubTypeConstants.USER_UPDATED,
        UserUpdatedEventBodyImpl.serializer()
    )

    registerParsers<MessageBtnClickEventBody>(
        UserEventSubTypeConstants.MESSAGE_BTN_CLICK,
        MessageBtnClickEventBodyImpl.serializer()
    )
}


private inline fun <reified B> MutableMap<Any, EventParser<*, *>>.registerParsers(
    subType: String,
    serializer: KSerializer<out B>
) {
    @Suppress("RemoveExplicitTypeArguments")
    this[subType] = SysEventParser<B>(
        Event.Type.SYS,
        subType,
        serializer
    )
}
