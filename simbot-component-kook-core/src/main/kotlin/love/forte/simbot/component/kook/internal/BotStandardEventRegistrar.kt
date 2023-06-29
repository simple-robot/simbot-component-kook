/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook,
 * If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.component.kook.internal

import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import love.forte.simbot.DiscreetSimbotApi
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.component.kook.event.*
import love.forte.simbot.component.kook.internal.event.*
import love.forte.simbot.component.kook.model.toModel
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.definition.UserInfo
import love.forte.simbot.event.Event.Key
import love.forte.simbot.kook.api.user.UserViewRequest
import love.forte.simbot.kook.event.Event
import love.forte.simbot.kook.event.message.MessageEvent
import love.forte.simbot.kook.event.system.SystemEvent
import love.forte.simbot.kook.event.system.channel.*
import love.forte.simbot.kook.event.system.guild.member.ExitedGuildEventBody
import love.forte.simbot.kook.event.system.guild.member.GuildMemberOfflineEventBody
import love.forte.simbot.kook.event.system.guild.member.GuildMemberOnlineEventBody
import love.forte.simbot.kook.event.system.guild.member.JoinedGuildEventBody
import love.forte.simbot.kook.event.system.user.*
import love.forte.simbot.kook.objects.Channel
import love.forte.simbot.kook.objects.SystemUser
import love.forte.simbot.literal

/**
 * 注册各种标准事件。
 */
internal suspend fun Event<*>.register(bot: KookComponentBotImpl) {
    when (this) {
        // 消息事件
        is MessageEvent<*> -> registerMessageEvent(bot)

        // 系统事件
        is SystemEvent<*, Event.Extra.Sys<*>> -> registerSystemEvent(bot)

        else -> {
            // 目前不太可能会触发此处
            pushUnsupported(bot)
        }
    }
}


@OptIn(DiscreetSimbotApi::class)
private suspend fun Event<*>.pushUnsupported(bot: KookComponentBotImpl) {
    bot.pushIfProcessable(UnsupportedKookEvent) {
        UnsupportedKookEvent(bot, this)
    }
}

/**
 * 消息事件
 */
private suspend fun MessageEvent<*>.registerMessageEvent(bot: KookComponentBotImpl) {
    when (channelType) {
        Channel.Type.PERSON -> {
            if (bot.isMe(authorId)) {
                bot.pushIfProcessable(KookBotSelfMessageEvent) {
                    KookBotSelfMessageEventImpl(bot, this)
                }
            } else {
                bot.pushIfProcessable(KookContactMessageEvent) {
                    KookContactMessageEventImpl(bot, this)
                }
            }
        }

        Channel.Type.GROUP -> {
            val guild = bot.internalGuild(extra.guildId) ?: return
            val author = guild.getInternalMember(authorId) ?: return
            val channel = guild.getInternalChannel(targetId) ?: return
            if (bot.isMe(authorId)) {
                bot.pushIfProcessable(KookBotSelfChannelMessageEvent) {
                    KookBotSelfChannelMessageEventImpl(
                        bot,
                        this,
                        channel,
                        author
                    )
                }
            } else {
                // push event
                bot.pushIfProcessable(KookChannelMessageEvent) {
                    KookChannelMessageEventImpl(
                        bot,
                        this,
                        author,
                        channel
                    )
                }
            }
        }
        // 其他未知事件类型
        else -> pushUnsupported(bot)
    }
}

/**
 * 系统事件
 */
@Suppress("UnnecessaryOptInAnnotation")
@OptIn(ExperimentalSimbotApi::class)
private suspend fun SystemEvent<*, *>.registerSystemEvent(bot: KookComponentBotImpl) {
    // 准备资源

    // target_id
    // 发送目的,
    // 频道消息类时, 代表的是频道 channel_id，
    // 如果 channel_type 为 GROUP 组播且 type 为 255 系统消息时，则代表服务器 guild_id

    val guild: KookGuildImpl? = if (channelType == Channel.Type.GROUP) {
        bot.internalGuild(targetId)
    } else {
        null
    }

//    val guild = bot.internalGuild(targetId) ?: return

    // author_id
    // 发送者 id, 1 代表系统

    val author = guild?.let { g ->
        g.getInternalMember(authorId)
            ?: toMemberIfSystem(authorId, bot, g)
//            ?: return // TODO ?
    }

    @Suppress("UNCHECKED_CAST")
    when (val body = extra.body) {
        // region 成员变更相关
        // 某人退出事件
        is UserExitedChannelEventBody -> {
            // remove this user.
            guild ?: return
            val removedMember = guild.internalMembers.remove(body.userId.literal)
                ?.also { it.cancel() }
                ?: return

            bot.pushIfProcessable(KookMemberExitedChannelEvent) {
                val channel = guild.getInternalChannel(body.channelId) ?: return
                KookMemberExitedChannelEventImpl(
                    bot,
                    this as Event<Event.Extra.Sys<UserExitedChannelEventBody>>,
                    channel,
                    removedMember
                )
            }
        }

        is UserJoinedChannelEventBody -> bot.pushIfProcessable(KookMemberJoinedChannelEvent) {
            guild ?: return
            val channel = guild.getInternalChannel(body.channelId) ?: return
            KookMemberJoinedChannelEventImpl(
                bot,
                this as Event<Event.Extra.Sys<UserJoinedChannelEventBody>>,
                channel,
                author ?: return
            )
        }

        is ExitedGuildEventBody -> {

            bot.pushIfProcessable(KookMemberExitedGuildEvent) {
                KookMemberExitedGuildEventImpl(
                    bot,
                    this as Event<Event.Extra.Sys<ExitedGuildEventBody>>,
                    guild ?: return,
                    author ?: return
                )
            }
        }

        is JoinedGuildEventBody -> bot.pushIfProcessable(KookMemberJoinedGuildEvent) {
            KookMemberJoinedGuildEventImpl(
                bot,
                this as Event<Event.Extra.Sys<JoinedGuildEventBody>>,
                guild ?: return,
                author ?: return
            )
        }

        is SelfExitedGuildEventBody -> bot.pushIfProcessable(KookBotSelfExitedGuildEvent) {
            KookBotSelfExitedGuildEventImpl(
                bot,
                this as Event<Event.Extra.Sys<SelfExitedGuildEventBody>>,
                guild ?: return,
                author ?: return
            )
        }

        is SelfJoinedGuildEventBody -> bot.pushIfProcessable(KookBotSelfJoinedGuildEvent) {
            KookBotSelfJoinedGuildEventImpl(
                bot,
                this as Event<Event.Extra.Sys<SelfJoinedGuildEventBody>>,
                guild ?: return,
                author ?: return
            )
        }

        // online
        is GuildMemberOnlineEventBody -> {
            val userInfo = bot.findUserInGuilds(body.userId, body.guilds) ?: return

            bot.pushIfProcessable(KookUserOnlineStatusChangedEvent.Online) {
                KookMemberOnlineEventImpl(
                    bot,
                    this as Event<Event.Extra.Sys<GuildMemberOnlineEventBody>>,
                    userInfo
                )
            }
        }

        // offline
        is GuildMemberOfflineEventBody -> {
            val userInfo = bot.findUserInGuilds(body.userId, body.guilds) ?: return

            bot.pushIfProcessable(KookUserOnlineStatusChangedEvent.Offline) {
                KookMemberOfflineEventImpl(
                    bot,
                    this as Event<Event.Extra.Sys<GuildMemberOfflineEventBody>>,
                    userInfo
                )
            }
        }

        // endregion

        // region 用户信息更新事件
        is UserUpdatedEventBody -> bot.pushIfProcessable(KookUserUpdatedEvent) {
            KookUserUpdatedEventImpl(
                bot,
                this as Event<Event.Extra.Sys<UserUpdatedEventBody>>
            )
        }
        // endregion

        // region 频道变更事件
        is AddedChannelExtraBody -> bot.pushIfProcessable(KookAddedChannelChangedEvent) {
            guild ?: return
            val channel = guild.getInternalChannel(body.id) ?: return
            KookAddedChannelChangedEventImpl(
                bot,
                this as Event<Event.Extra.Sys<AddedChannelExtraBody>>,
                guild, channel
            )
        }

        is UpdatedChannelExtraBody -> bot.pushIfProcessable(KookUpdatedChannelChangedEvent) {
            guild ?: return
            val channel = guild.getInternalChannel(body.id) ?: return
            KookUpdatedChannelChangedEventImpl(
                bot,
                this as Event<Event.Extra.Sys<UpdatedChannelExtraBody>>,
                guild, channel
            )
        }

        is DeletedChannelExtraBody -> bot.pushIfProcessable(KookDeletedChannelChangedEvent) {
            guild ?: return
            val channel = guild.getInternalChannel(body.id) ?: return
            KookDeletedChannelChangedEventImpl(
                bot,
                this as Event<Event.Extra.Sys<DeletedChannelExtraBody>>,
                guild, channel
            )
        }

        is PinnedMessageExtraBody -> bot.pushIfProcessable(KookPinnedMessageEvent) {
            guild ?: return
            val channel = guild.getInternalChannel(body.channelId) ?: return
            val operator = guild.getInternalMember(body.operatorId)
            KookPinnedMessageEventImpl(
                bot,
                this as Event<Event.Extra.Sys<PinnedMessageExtraBody>>,
                guild, channel, operator
            )
        }

        is UnpinnedMessageExtraBody -> bot.pushIfProcessable(KookUnpinnedMessageEvent) {
            guild ?: return
            val channel = guild.getInternalChannel(body.channelId) ?: return
            val operator = guild.getInternalMember(body.operatorId)
            KookUnpinnedMessageEventImpl(
                bot,
                this as Event<Event.Extra.Sys<UnpinnedMessageExtraBody>>,
                guild, channel, operator
            )
        }

        // endregion

        // btn
        is MessageBtnClickEventBody -> bot.pushIfProcessable(KookMessageBtnClickEvent) {
            KookMessageBtnClickEventImpl(
                bot,
                this as Event<Event.Extra.Sys<MessageBtnClickEventBody>>
            )
        }

        // other..?


        // 其他未知事件类型
        else -> pushUnsupported(bot)
    }


}

private suspend fun KookComponentBotImpl.findUserInGuilds(userId: ID, guildIds: Collection<ID>): UserInfo? {
    return guildIds.ifEmpty { return null }
        .asSequence()
        .mapNotNull { id -> internalGuild(id) }
        .mapNotNull { g -> g.getInternalMember(userId) }
        .firstOrNull() ?: userId.let {
        UserViewRequest.create(userId, guildIds.first()).requestDataBy(bot)
    }
}


private suspend inline fun KookComponentBotImpl.pushIfProcessable(
    eventKey: Key<*>,
    block: () -> love.forte.simbot.event.Event?,
): Boolean {
    if (eventProcessor.isProcessable(eventKey)) {
        val event = block() ?: return false
        if (isEventProcessAsync) {
            launch { eventProcessor.push(event) }
        } else {
            eventProcessor.push(event)
        }
        return true
    }

    return false
}


private fun toMemberIfSystem(
    id: ID,
    bot: KookComponentBotImpl,
    guild: KookGuildImpl,
): KookGuildMemberImpl? {
    if (id != SystemUser.id) {
        return null
    }

    return KookGuildMemberImpl(bot, guild, SystemUser.toModel())
}
