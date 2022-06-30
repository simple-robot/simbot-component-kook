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

package love.forte.simbot.component.kaiheila.internal

import kotlinx.coroutines.launch
import love.forte.simbot.DiscreetSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.component.kaiheila.KaiheilaComponentBot
import love.forte.simbot.component.kaiheila.event.*
import love.forte.simbot.component.kaiheila.internal.event.*
import love.forte.simbot.component.kaiheila.model.toModel
import love.forte.simbot.component.kaiheila.util.requestDataBy
import love.forte.simbot.definition.UserInfo
import love.forte.simbot.event.Event.Key
import love.forte.simbot.kaiheila.api.user.UserViewRequest
import love.forte.simbot.kaiheila.event.Event
import love.forte.simbot.kaiheila.event.message.MessageEvent
import love.forte.simbot.kaiheila.event.system.SystemEvent
import love.forte.simbot.kaiheila.event.system.channel.*
import love.forte.simbot.kaiheila.event.system.guild.member.ExitedGuildEventBody
import love.forte.simbot.kaiheila.event.system.guild.member.GuildMemberOfflineEventBody
import love.forte.simbot.kaiheila.event.system.guild.member.GuildMemberOnlineEventBody
import love.forte.simbot.kaiheila.event.system.guild.member.JoinedGuildEventBody
import love.forte.simbot.kaiheila.event.system.user.*
import love.forte.simbot.kaiheila.objects.Channel
import love.forte.simbot.kaiheila.objects.SystemUser

/**
 * 注册各种标准事件。
 */
internal suspend fun Event<*>.register(bot: KaiheilaComponentBotImpl) {
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
private fun Event<*>.pushUnsupported(bot: KaiheilaComponentBotImpl) {
    bot.pushIfProcessable(UnsupportedKaiheilaEvent) {
        UnsupportedKaiheilaEvent(bot, this)
    }
}

/**
 * 消息事件
 */
private fun MessageEvent<*>.registerMessageEvent(bot: KaiheilaComponentBotImpl) {
    when (channelType) {
        Channel.Type.PERSON -> {
            if (bot.isMe(authorId)) {
                bot.pushIfProcessable(KaiheilaBotSelfPersonMessageEvent) {
                    KaiheilaBotSelfPersonMessageEventImpl(bot, this)
                }
            } else {
                bot.pushIfProcessable(KaiheilaNormalPersonMessageEvent) {
                    KaiheilaNormalPersonMessageEventImpl(bot, this)
                }
            }
        }
        Channel.Type.GROUP -> {
            val guild = bot.internalGuild(extra.guildId) ?: return
            val author = guild.internalMember(authorId) ?: return
            val channel = guild.internalChannel(targetId) ?: return
            if (bot.isMe(authorId)) {
                bot.pushIfProcessable(KaiheilaBotSelfGroupMessageEvent) {
                    KaiheilaBotSelfGroupMessageEventImpl(
                        bot,
                        this,
                        channel = channel,
                        member = author
                    )
                }
            } else {
                // push event
                bot.pushIfProcessable(KaiheilaNormalGroupMessageEvent) {
                    KaiheilaNormalGroupMessageEventImpl(
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
private suspend fun SystemEvent<*, *>.registerSystemEvent(bot: KaiheilaComponentBotImpl) {

    // 准备资源
    val guild = bot.internalGuild(targetId) ?: return

    val author =
        guild.internalMember(authorId)
            ?: toMemberIfSystem(authorId, bot, guild)
            ?: return

    @Suppress("UNCHECKED_CAST")
    when (val body = extra.body) {
        //region 成员变更相关
        is UserExitedChannelEventBody -> bot.pushIfProcessable(KaiheilaMemberExitedChannelEvent) {
            val channel = guild.internalChannel(body.channelId) ?: return
            KaiheilaMemberExitedChannelEventImpl(
                bot,
                this as Event<Event.Extra.Sys<UserExitedChannelEventBody>>,
                channel,
                author
            )
        }

        is UserJoinedChannelEventBody -> bot.pushIfProcessable(KaiheilaMemberJoinedChannelEvent) {
            val channel = guild.internalChannel(body.channelId) ?: return
            KaiheilaMemberJoinedChannelEventImpl(
                bot,
                this as Event<Event.Extra.Sys<UserJoinedChannelEventBody>>,
                channel,
                author
            )
        }

        is ExitedGuildEventBody -> bot.pushIfProcessable(KaiheilaMemberExitedGuildEvent) {
            KaiheilaMemberExitedGuildEventImpl(
                bot,
                this as Event<Event.Extra.Sys<ExitedGuildEventBody>>,
                guild,
                author
            )
        }

        is JoinedGuildEventBody -> bot.pushIfProcessable(KaiheilaMemberJoinedGuildEvent) {
            KaiheilaMemberJoinedGuildEventImpl(
                bot,
                this as Event<Event.Extra.Sys<JoinedGuildEventBody>>,
                guild,
                author
            )
        }

        is SelfExitedGuildEventBody -> bot.pushIfProcessable(KaiheilaBotSelfExitedGuildEvent) {
            KaiheilaBotSelfExitedGuildEventImpl(
                bot,
                this as Event<Event.Extra.Sys<SelfExitedGuildEventBody>>,
                guild,
                author
            )
        }

        is SelfJoinedGuildEventBody -> bot.pushIfProcessable(KaiheilaBotSelfJoinedGuildEvent) {
            KaiheilaBotSelfJoinedGuildEventImpl(
                bot,
                this as Event<Event.Extra.Sys<SelfJoinedGuildEventBody>>,
                guild,
                author
            )
        }

        // online
        is GuildMemberOnlineEventBody -> {
            val userInfo = bot.findUserInGuilds(body.userId, body.guilds) ?: return

            bot.pushIfProcessable(KaiheilaUserOnlineStatusChangedEvent.Online) {
                KaiheilaMemberOnlineEventImpl(
                    bot,
                    this as Event<Event.Extra.Sys<GuildMemberOnlineEventBody>>,
                    userInfo
                )
            }
        }

        // offline
        is GuildMemberOfflineEventBody -> {
            val userInfo = bot.findUserInGuilds(body.userId, body.guilds) ?: return

            bot.pushIfProcessable(KaiheilaUserOnlineStatusChangedEvent.Offline) {
                KaiheilaMemberOfflineEventImpl(
                    bot,
                    this as Event<Event.Extra.Sys<GuildMemberOfflineEventBody>>,
                    userInfo
                )
            }
        }

        //endregion

        //region 用户信息更新事件
        is UserUpdatedEventBody -> bot.pushIfProcessable(KaiheilaUserUpdatedEvent) {
            KaiheilaUserUpdatedEventImpl(
                bot,
                this as Event<Event.Extra.Sys<UserUpdatedEventBody>>
            )
        }
        //endregion

        // region 频道变更事件
        is AddedChannelExtraBody -> bot.pushIfProcessable(KaiheilaAddedChannelChangedEvent) {
            val channel = guild.internalChannel(body.id) ?: return
            KaiheilaAddedChannelChangedEventImpl(
                bot,
                this as Event<Event.Extra.Sys<AddedChannelExtraBody>>,
                guild, channel
            )
        }
        is UpdatedChannelExtraBody -> bot.pushIfProcessable(KaiheilaUpdatedChannelChangedEvent) {
            val channel = guild.internalChannel(body.id) ?: return
            KaiheilaUpdatedChannelChangedEventImpl(
                bot,
                this as Event<Event.Extra.Sys<UpdatedChannelExtraBody>>,
                guild, channel
            )
        }
        is DeletedChannelExtraBody -> bot.pushIfProcessable(KaiheilaDeletedChannelChangedEvent) {
            val channel = guild.internalChannel(body.id) ?: return
            KaiheilaDeletedChannelChangedEventImpl(
                bot,
                this as Event<Event.Extra.Sys<DeletedChannelExtraBody>>,
                guild, channel
            )
        }
        is PinnedMessageExtraBody -> bot.pushIfProcessable(KaiheilaPinnedMessageEvent) {
            val channel = guild.internalChannel(body.channelId) ?: return
            val operator = guild.internalMember(body.operatorId)
            KaiheilaPinnedMessageEventImpl(
                bot,
                this as Event<Event.Extra.Sys<PinnedMessageExtraBody>>,
                guild, channel, operator
            )
        }
        is UnpinnedMessageExtraBody -> bot.pushIfProcessable(KaiheilaUnpinnedMessageEvent) {
            val channel = guild.internalChannel(body.channelId) ?: return
            val operator = guild.internalMember(body.operatorId)
            KaiheilaUnpinnedMessageEventImpl(
                bot,
                this as Event<Event.Extra.Sys<UnpinnedMessageExtraBody>>,
                guild, channel, operator
            )
        }

        // endregion


        // TODO other..


        // 其他未知事件类型
        else -> pushUnsupported(bot)
    }


}

private suspend fun KaiheilaComponentBotImpl.findUserInGuilds(userId: ID, guildIds: Collection<ID>): UserInfo? {
    return guildIds.ifEmpty { return null }
        .asSequence()
        .mapNotNull { id -> internalGuild(id) }
        .mapNotNull { g -> g.internalMember(userId) }
        .firstOrNull() ?: userId.let {
        UserViewRequest(userId, guildIds.first()).requestDataBy(bot)
    }
}


private inline fun KaiheilaComponentBot.pushIfProcessable(
    eventKey: Key<*>,
    block: () -> love.forte.simbot.event.Event?,
): Boolean {
    if (eventProcessor.isProcessable(eventKey)) {
        val event = block() ?: return false
        launch { eventProcessor.push(event) }
        return true
    }

    return false
}


private fun toMemberIfSystem(
    id: ID,
    bot: KaiheilaComponentBotImpl,
    guild: KaiheilaGuildImpl
): KaiheilaGuildMemberImpl? {
    if (id != SystemUser.id) {
        return null
    }

    return KaiheilaGuildMemberImpl(bot, guild, SystemUser.toModel())
}