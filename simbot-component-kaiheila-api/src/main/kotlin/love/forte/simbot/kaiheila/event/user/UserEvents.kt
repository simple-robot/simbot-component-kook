/*
 *
 *  * Copyright (c) 2021. ForteScarlet All rights reserved.
 *  * Project  simple-robot
 *  * File     MiraiAvatar.kt
 *  *
 *  * You can contact the author through the following channels:
 *  * github https://github.com/ForteScarlet
 *  * gitee  https://gitee.com/ForteScarlet
 *  * email  ForteScarlet@163.com
 *  * QQ     1149159218
 *
 */

@file:JvmName("UserEvents")

package love.forte.simbot.kaiheila.event.user

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
