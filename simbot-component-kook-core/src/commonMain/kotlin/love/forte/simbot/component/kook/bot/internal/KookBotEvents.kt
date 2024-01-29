/*
 * Copyright (c) 2023. ForteScarlet.
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

package love.forte.simbot.component.kook.bot.internal

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import love.forte.simbot.DiscreetSimbotApi
import love.forte.simbot.annotations.FragileSimbotAPI
import love.forte.simbot.component.kook.event.*
import love.forte.simbot.component.kook.event.internal.*
import love.forte.simbot.component.kook.internal.KookCategoryChannelImpl
import love.forte.simbot.component.kook.internal.KookChatChannelImpl
import love.forte.simbot.component.kook.internal.KookGuildImpl
import love.forte.simbot.component.kook.internal.KookMemberImpl
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.event.Event
import love.forte.simbot.kook.api.guild.GetGuildViewApi
import love.forte.simbot.kook.api.member.GetGuildMemberListApi
import love.forte.simbot.kook.api.member.createItemFlow
import love.forte.simbot.kook.api.user.GetUserViewApi
import love.forte.simbot.kook.event.*
import love.forte.simbot.kook.objects.SimpleUser
import love.forte.simbot.kook.stdlib.ProcessorType
import love.forte.simbot.kook.event.Event as KEvent
import love.forte.simbot.kook.objects.User as KUser


@OptIn(FragileSimbotAPI::class)
internal fun KookBotImpl.registerEvent() {
    val thisBot = this
    sourceBot.processor(ProcessorType.PREPARE) { rawEvent ->
        val event = this

        when (val ex = extra) {
            is TextExtra -> {
                val isBotSelf = thisBot.botUserInfo.id == event.authorId
                // 消息事件
                when (channelType) {
                    love.forte.simbot.kook.event.Event.ChannelType.PERSON -> {
                        if (isBotSelf) {
                            pushIfProcessable(KookBotSelfMessageEvent) {
                                KookBotSelfMessageEventImpl(thisBot, this.doAs(), rawEvent)
                            }
                        } else {
                            pushIfProcessable(KookContactMessageEvent) {
                                KookContactMessageEventImpl(thisBot, this.doAs(), rawEvent)
                            }
                        }
                    }

                    love.forte.simbot.kook.event.Event.ChannelType.GROUP -> {
                        val guildId = ex.guildId!!

                        val channel = internalChannel(targetId)
                            ?: run {
                                logger.warn("Unknown channel {} in event {}", targetId, event)
                                return@processor
                            }

                        val author = internalMember(guildId, authorId)
                            ?: run {
                                logger.warn("Unknown member {} in event {}", authorId, event)
                                return@processor
                            }

                        if (isBotSelf) {
                            pushIfProcessable(KookBotSelfChannelMessageEvent) {
                                KookBotSelfChannelMessageEventImpl(
                                    thisBot,
                                    event.doAs(),
                                    channel,
                                    author,
                                    rawEvent
                                )
                            }
                        } else {
                            pushIfProcessable(KookChannelMessageEvent) {
                                KookChannelMessageEventImpl(
                                    thisBot,
                                    event.doAs(),
                                    author,
                                    channel,
                                    rawEvent
                                )
                            }
                        }

                    }

                    else -> {

                    }
                }
            }

            is SystemExtra -> {
                when (ex) {
                    // 某人退出频道服务器
                    is ExitedGuildEventExtra -> {
                        val guildId = event.targetId
                        val guild = internalGuild(event.targetId)
                            ?: run {
                                logger.warn("Unknown guild {} in event {}", guildId, event)
                                return@processor
                            }

                        val removedMember = inCacheModify {
                            removeMember(guildId, ex.body.userId)
                        } ?: run {
                            logger.warn("No member ({}) removed in event {}", ex.body.userId, event)
                            return@processor
                        }

                        pushIfProcessable(KookMemberExitedGuildEvent) {
                            KookMemberExitedGuildEventImpl(
                                thisBot,
                                event.doAs(),
                                guild,
                                removedMember,
                                rawEvent
                            )
                        }
                    }

                    // 某人加入频道服务器
                    is JoinedGuildEventExtra -> {
                        val guildId = event.targetId
                        val userId = ex.body.userId

                        val guild = internalGuild(guildId)
                            ?: run {
                                logger.warn("Unknown guild {} in event {}", guildId, event)
                                return@processor
                            }

                        val userInfo = GetUserViewApi.create(userId, guildId)
                            .requestDataBy(thisBot)

                        val newMember = inCacheModify {
                            val newMember = KookMemberImpl(thisBot, userInfo, guildId)
                            setMember(guildId, userId, newMember)
                            newMember
                        }

                        pushIfProcessable(KookMemberJoinedGuildEvent) {
                            KookMemberJoinedGuildEventImpl(
                                thisBot,
                                event.doAs(),
                                guild,
                                newMember,
                                rawEvent
                            )
                        }
                    }

                    // 某成员进入某子频道
                    is JoinedChannelEventExtra -> {
                        val guildId = event.targetId
                        val userId = ex.body.userId
                        val channelId = ex.body.channelId

                        val channel = internalChannel(channelId)
                            ?: run {
                                logger.warn("Unknown channel {} in event {}", channelId, event)
                                return@processor
                            }

                        val member = thisBot.internalMember(guildId, userId)
                            ?: run {
                                logger.warn("Unknown member {} in event {}", userId, event)
                                return@processor
                            }

                        pushIfProcessable(KookMemberJoinedChannelEvent) {
                            KookMemberJoinedChannelEventImpl(
                                thisBot,
                                event.doAs(),
                                channel,
                                member,
                                rawEvent
                            )
                        }
                    }

                    // 某成员离开某子频道
                    is ExitedChannelEventExtra -> {
                        val guildId = event.targetId
                        val userId = ex.body.userId
                        val channelId = ex.body.channelId

                        val channel = internalChannel(channelId)
                            ?: run {
                                logger.warn("Unknown channel {} in event {}", channelId, event)
                                return@processor
                            }

                        val member = thisBot.internalMember(guildId, userId)
                            ?: run {
                                logger.warn("Unknown member {} in event {}", userId, event)
                                return@processor
                            }

                        pushIfProcessable(KookMemberExitedChannelEvent) {
                            KookMemberExitedChannelEventImpl(
                                thisBot,
                                event.doAs(),
                                channel,
                                member,
                                rawEvent
                            )
                        }
                    }

                    // Bot 加入服务器
                    is SelfJoinedGuildEventExtra -> {
                        val guildId = ex.body.guildId

                        val guildInfo = GetGuildViewApi.create(guildId).requestDataBy(thisBot)

                        // guild members sync
                        val members = GetGuildMemberListApi.createItemFlow { page ->
                            create(guildId = guildId, page = page)
                                .requestDataBy(thisBot)
                        }.buffer(200)

                        lateinit var botAsMember: KookMemberImpl

                        val guild = inCacheModify {
                            val guild = KookGuildImpl(thisBot, guildInfo)
                            guilds[guildId] = guild
                            guildInfo.channels.forEach {
                                channels[it.id] = KookChatChannelImpl(thisBot, it)
                            }

                            members.collect {
                                val member = KookMemberImpl(thisBot, it, guildId)
                                if (it.id == thisBot.sourceBot.botUserInfo.id) {
                                    botAsMember = member
                                }

                                setMember(guildId, it.id, member)
                            }

                            guild
                        }

                        pushIfProcessable(KookBotSelfJoinedGuildEvent) {
                            KookBotSelfJoinedGuildEventImpl(
                                thisBot,
                                event.doAs(),
                                guild,
                                botAsMember,
                                rawEvent
                            )
                        }
                    }

                    // Bot 离开服务器
                    is SelfExitedGuildEventExtra -> {
                        val guildId = ex.body.guildId
                        val botMember: KookMemberImpl = internalMember(ex.body.guildId, thisBot.botUserInfo.id)
                            ?: run {
                                logger.warn("unknown bot self {} as member in event {}", thisBot.botUserInfo.id, event)
                                return@processor
                            }

                        val guild = inCacheModify {
                            // remove guilds
                            val removedGuild = guilds.remove(guildId)
                                ?: run {
                                    logger.warn("Unknown guild {} in event {}", guildId, event)
                                    return@inCacheModify null
                                }
                            // remove channels
                            channels.entries.removeIf { (_, v) -> v.source.guildId == guildId }
                            // remove members
                            members.entries.removeIf { (k, _) -> k.guildId == guildId }

                            removedGuild
                        } ?: return@processor

                        pushIfProcessable(KookBotSelfExitedGuildEvent) {
                            KookBotSelfExitedGuildEventImpl(
                                thisBot,
                                event.doAs(),
                                guild,
                                botMember,
                                rawEvent
                            )
                        }
                    }

                    // 成员上线
                    is GuildMemberOnlineEventExtra -> {
                        pushIfProcessable(KookMemberOnlineEvent) {
                            KookMemberOnlineEventImpl(thisBot, event.doAs(), rawEvent)
                        }
                    }

                    // 成员下线
                    is GuildMemberOfflineEventExtra -> {
                        pushIfProcessable(KookMemberOfflineEvent) {
                            KookMemberOfflineEventImpl(thisBot, event.doAs(), rawEvent)
                        }
                    }

                    // 新增频道
                    is AddedChannelEventExtra -> {
                        val channelBody = ex.body
                        val guild = internalGuild(channelBody.guildId)
                            ?: run {
                                logger.warn("Unknown guild {} in event {}", channelBody.guildId, event)
                                return@processor
                            }

                        if (channelBody.isCategory) {
                            val category = inCacheModify {
                                KookCategoryChannelImpl(thisBot, channelBody).also {
                                    categories[channelBody.id] = it
                                }
                            }

                            pushIfProcessable(KookAddedCategoryEvent) {
                                KookAddedCategoryEventImpl(
                                    thisBot,
                                    event.doAs(),
                                    guild,
                                    category,
                                    rawEvent
                                )
                            }
                        } else {
                            val channel = inCacheModify {
                                KookChatChannelImpl(thisBot, channelBody).also {
                                    channels[channelBody.id] = it
                                }
                            }

                            pushIfProcessable(KookAddedChannelEvent) {
                                KookAddedChannelEventImpl(
                                    thisBot,
                                    event.doAs(),
                                    guild,
                                    channel,
                                    rawEvent
                                )
                            }
                        }


                    }

                    // 更新频道
                    is UpdatedChannelEventExtra -> {
                        val channelBody = ex.body
                        val guild = internalGuild(channelBody.guildId)
                            ?: run {
                                logger.warn("Unknown guild {} in event {}", channelBody.guildId, event)
                                return@processor
                            }

                        if (channelBody.isCategory) {
                            val category = inCacheModify {
                                KookCategoryChannelImpl(thisBot, channelBody).also {
                                    categories[channelBody.id] = it
                                }
                            }

                            pushIfProcessable(KookUpdatedCategoryEvent) {
                                KookUpdatedCategoryEventImpl(
                                    thisBot,
                                    event.doAs(),
                                    guild,
                                    category,
                                    rawEvent
                                )
                            }
                        } else {
                            val channel = inCacheModify {
                                KookChatChannelImpl(thisBot, channelBody).also {
                                    channels[channelBody.id] = it
                                }
                            }

                            pushIfProcessable(KookUpdatedChannelEvent) {
                                KookUpdatedChannelEventImpl(
                                    thisBot,
                                    event.doAs(),
                                    guild,
                                    channel,
                                    rawEvent
                                )
                            }
                        }


                    }

                    // 删除频道
                    is DeletedChannelEventExtra -> {
                        val guild = internalGuild(event.targetId)
                            ?: run {
                                logger.warn("Unknown guild {} in event {}", event.targetId, event)
                                return@processor
                            }

                        val removed = inCacheModify {
                            channels.remove(ex.body.id) ?: categories.remove(ex.body.id)
                        } ?: run {
                            logger.warn("No channel or category ({}) removed in event {}", ex.body.id, event)
                            return@processor
                        }

                        if (removed is KookChatChannelImpl) {
                            pushIfProcessable(KookDeletedChannelEvent) {
                                KookDeletedChannelEventImpl(
                                    thisBot,
                                    event.doAs(),
                                    guild,
                                    removed,
                                    rawEvent
                                )
                            }
                        } else if (removed is KookCategoryChannelImpl) {
                            pushIfProcessable(KookDeletedCategoryEvent) {
                                KookDeletedCategoryEventImpl(
                                    thisBot,
                                    event.doAs(),
                                    guild,
                                    removed,
                                    rawEvent
                                )
                            }
                        }

                    }

                    // 置顶消息
                    is PinnedMessageEventExtra -> {
                        val guild = internalGuild(event.targetId)
                            ?: run {
                                logger.warn("Unknown guild {} in event {}", event.targetId, event)
                                return@processor
                            }

                        val channelId = ex.body.channelId

                        val channel = internalChannel(channelId)
                            ?: run {
                                logger.warn("Unknown channel {} in event {}", channelId, event)
                                return@processor
                            }

                        pushIfProcessable(KookPinnedMessageEvent) {
                            KookPinnedMessageEventImpl(
                                thisBot,
                                event.doAs(),
                                guild,
                                channel,
                                rawEvent
                            )
                        }
                    }

                    // 取消置顶消息
                    is UnpinnedMessageEventExtra -> {
                        val guild = internalGuild(event.targetId)
                            ?: run {
                                logger.warn("Unknown guild {} in event {}", event.targetId, event)
                                return@processor
                            }

                        val channelId = ex.body.channelId

                        val channel = internalChannel(channelId)
                            ?: run {
                                logger.warn("Unknown channel {} in event {}", channelId, event)
                                return@processor
                            }

                        pushIfProcessable(KookUnpinnedMessageEvent) {
                            KookUnpinnedMessageEventImpl(
                                thisBot,
                                event.doAs(),
                                guild,
                                channel,
                                rawEvent
                            )
                        }
                    }

                    // 频道消息删除
                    is DeletedMessageEventExtra -> {
                        pushIfProcessable(KookDeletedChannelMessageEvent) {
                            KookDeletedChannelMessageEventImpl(thisBot, event.doAs(), rawEvent)
                        }
                    }

                    // 频道消息更新
                    is UpdatedMessageEventExtra -> {
                        pushIfProcessable(KookUpdatedChannelMessageEvent) {
                            KookUpdatedChannelMessageEventImpl(thisBot, event.doAs(), rawEvent)
                        }
                    }

                    // 按钮点击
                    is MessageBtnClickEventExtra -> {
                        pushIfProcessable(KookMessageBtnClickEvent) {
                            KookMessageBtnClickEventImpl(
                                thisBot,
                                event.doAs(),
                                rawEvent
                            )
                        }
                    }

                    // 服务器成员信息更新
                    is UpdatedGuildMemberEventExtra -> {
                        val guild = internalGuild(event.targetId)
                            ?: run {
                                logger.warn("Unknown guild {} in event {}", event.targetId, event)
                                return@processor
                            }


                        var oldMember: KookMemberImpl? = null
                        val newNickname = ex.body.nickname

                        val newMember = thisBot.inCacheModify {
                            val key = memberCacheId(event.targetId, ex.body.userId)
                            members.compute(key) { k, old ->
                                if (old == null) {
                                    return@compute null
                                }

                                // copy source
                                oldMember = old
                                val newSource = old.source.copyWithNewNickname(newNickname)
                                KookMemberImpl(thisBot, newSource, old.guildIdValue)
                            }
                        }

                        if (oldMember == null || newMember == null) {
                            logger.warn("Unknown member {} in event {} for update.", event.targetId, event)
                            return@processor
                        }

                        pushIfProcessable(KookMemberUpdatedEvent) {
                            KookMemberUpdatedEventImpl(
                                thisBot,
                                event.doAs(),
                                guild,
                                newMember,
                                oldMember!!,
                                rawEvent
                            )
                        }
                    }

                    // 用户信息更新
                    is UserUpdatedEventExtra -> {
                        pushIfProcessable(KookUserUpdatedEvent) {
                            KookUserUpdatedEventImpl(
                                thisBot,
                                event.doAs(),
                                rawEvent
                            )
                        }
                    }

                    // 私聊消息删除
                    is DeletedPrivateMessageEventExtra -> {
                        pushIfProcessable(KookDeletedPrivateMessageEvent) {
                            KookDeletedPrivateMessageEventImpl(
                                thisBot,
                                event.doAs(),
                                rawEvent
                            )
                        }
                    }

                    // 私聊消息更新
                    is UpdatedPrivateMessageEventExtra -> {
                        pushIfProcessable(KookUpdatedPrivateMessageEvent) {
                            KookUpdatedPrivateMessageEventImpl(
                                thisBot,
                                event.doAs(),
                                rawEvent
                            )
                        }
                    }

                    else -> pushUnsupported(event, rawEvent)
                }


            }

            is UnknownExtra -> {
                pushUnsupported(event, rawEvent)
            }
        }


    }
}

@OptIn(DiscreetSimbotApi::class)
private suspend fun KookBotImpl.pushUnsupported(event: KEvent<EventExtra>, sourceEventJson: String) {
    pushIfProcessable(UnsupportedKookEvent) {
        UnsupportedKookEvent(
            this,
            event,
            sourceEventJson
        )
    }
}


private suspend inline fun KookBotImpl.pushIfProcessable(
    eventKey: Event.Key<*>,
    block: () -> Event?,
): Boolean {
    if (eventProcessor.isProcessable(eventKey)) {
        val event = block() ?: return false
        if (isNormalEventProcessAsync) {
            launch { eventProcessor.push(event) }
        } else {
            eventProcessor.push(event)
        }
        return true
    }

    return false
}

@Throws(ClassCastException::class)
@Suppress("UNCHECKED_CAST")
private inline fun <reified T : EventExtra> KEvent<*>.doAs(): KEvent<T> = this as KEvent<T>


private fun KUser.copyWithNewNickname(nickname: String): KUser {
    if (this is UserCopyWithNewNickname) {
        return copy(nickname = nickname)
    }

    if (this is SimpleUser) {
        return copy(nickname = nickname)
    }

    return UserCopyWithNewNickname(nickname, this)
}

private data class UserCopyWithNewNickname(override val nickname: String, val source: KUser) : KUser by source
