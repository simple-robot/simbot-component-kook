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
import love.forte.simbot.component.kook.event.*
import love.forte.simbot.component.kook.event.internal.*
import love.forte.simbot.component.kook.internal.KookChannelCategoryImpl
import love.forte.simbot.component.kook.internal.KookChannelImpl
import love.forte.simbot.component.kook.internal.KookGuildImpl
import love.forte.simbot.component.kook.internal.KookMemberImpl
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.event.Event
import love.forte.simbot.kook.api.ApiResultType
import love.forte.simbot.kook.api.channel.ChannelInfo
import love.forte.simbot.kook.api.guild.GetGuildViewApi
import love.forte.simbot.kook.api.member.GetGuildMemberListApi
import love.forte.simbot.kook.api.member.asItemFlow
import love.forte.simbot.kook.api.user.GetUserViewApi
import love.forte.simbot.kook.event.*
import love.forte.simbot.kook.objects.Channel
import love.forte.simbot.kook.event.Event as KEvent


internal fun KookBotImpl.registerEvent() {
    val thisBot = this
    sourceBot.processor {
        val event = this

        when (val ex = extra) {
            is TextExtra -> {
                val isBotSelf = thisBot.botUserInfo.id == event.authorId
                // 消息事件
                when (channelType) {
                    love.forte.simbot.kook.event.Event.ChannelType.PERSON -> {
                        if (isBotSelf) {
                            pushIfProcessable(KookBotSelfMessageEvent) {
                                KookBotSelfMessageEventImpl(thisBot, this.doAs())
                            }
                        } else {
                            pushIfProcessable(KookContactMessageEvent) {
                                KookContactMessageEventImpl(thisBot, this.doAs())
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
                                    author
                                )
                            }
                        } else {
                            pushIfProcessable(KookChannelMessageEvent) {
                                KookChannelMessageEventImpl(
                                    thisBot,
                                    event.doAs(),
                                    author,
                                    channel
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
                            members.remove(memberCacheId(guildId, ex.body.userId))
                        } ?: run {
                            logger.warn("No member ({}) removed in event {}", ex.body.userId, event)
                            return@processor
                        }

                        pushIfProcessable(KookMemberExitedGuildEvent) {
                            KookMemberJoinedGuildEventImpl(
                                thisBot,
                                event.doAs(),
                                guild,
                                removedMember
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
                            this.members[memberCacheId(guildId, userId)] = newMember
                            newMember
                        }

                        pushIfProcessable(KookMemberJoinedGuildEvent) {
                            KookMemberJoinedGuildEventImpl(
                                thisBot,
                                event.doAs(),
                                guild,
                                newMember
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
                                member
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
                                member
                            )
                        }
                    }

                    // Bot 加入服务器
                    is SelfJoinedGuildEventExtra -> {
                        val guildId = ex.body.guildId

                        val guildInfo = GetGuildViewApi.create(guildId).requestDataBy(thisBot)

                        // guild members sync
                        val members = GetGuildMemberListApi.asItemFlow { page ->
                            create(guildId = guildId, page = page)
                                .requestDataBy(thisBot)
                        }.buffer(200)

                        lateinit var botAsMember: KookMemberImpl

                        val guild = inCacheModify {
                            val guild = KookGuildImpl(thisBot, guildInfo)
                            guilds[guildId] = guild
                            guildInfo.channels.forEach {
                                channels[it.id] = KookChannelImpl(thisBot, it)
                            }

                            members.collect {
                                val member = KookMemberImpl(thisBot, it, guildId)
                                if (it.id == thisBot.sourceBot.botUserInfo.id) {
                                    botAsMember = member
                                }

                                this.members[memberCacheId(guildId, it.id)] = member
                            }

                            guild
                        }

                        pushIfProcessable(KookBotSelfJoinedGuildEvent) {
                            KookBotSelfJoinedGuildEventImpl(
                                thisBot,
                                event.doAs(),
                                guild,
                                botAsMember
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
                            val memberCacheIdPrefix = memberCacheIdGuildPrefix(guildId)
                            members.entries.removeIf { (k, _) -> k.startsWith(memberCacheIdPrefix) }

                            removedGuild
                        } ?: return@processor

                        pushIfProcessable(KookBotSelfExitedGuildEvent) {
                            KookBotSelfJoinedGuildEventImpl(
                                thisBot,
                                event.doAs(),
                                guild,
                                botMember
                            )
                        }
                    }

                    // 成员上线
                    is GuildMemberOnlineEventExtra -> {
                        pushIfProcessable(KookMemberOnlineEvent) {
                            KookMemberOnlineEventImpl(thisBot, event.doAs())
                        }
                    }

                    // 成员下线
                    is GuildMemberOfflineEventExtra -> {
                        pushIfProcessable(KookMemberOfflineEvent) {
                            KookMemberOfflineEventImpl(thisBot, event.doAs())
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
                                KookChannelCategoryImpl(thisBot, channelBody).also {
                                    categories[channelBody.id] = it
                                }
                            }

                            pushIfProcessable(KookAddedCategoryEvent) {
                                KookAddedCategoryEventImpl(
                                    thisBot,
                                    event.doAs(),
                                    guild,
                                    category
                                )
                            }
                        } else {
                            val channel = inCacheModify {
                                KookChannelImpl(thisBot, channelBody).also {
                                    channels[channelBody.id] = it
                                }
                            }

                            pushIfProcessable(KookAddedChannelEvent) {
                                KookAddedChannelEventImpl(
                                    thisBot,
                                    event.doAs(),
                                    guild,
                                    channel
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
                                KookChannelCategoryImpl(thisBot, channelBody).also {
                                    categories[channelBody.id] = it
                                }
                            }

                            pushIfProcessable(KookUpdatedCategoryEvent) {
                                KookUpdatedCategoryEventImpl(
                                    thisBot,
                                    event.doAs(),
                                    guild,
                                    category
                                )
                            }
                        } else {
                            val channel = inCacheModify {
                                KookChannelImpl(thisBot, channelBody).also {
                                    channels[channelBody.id] = it
                                }
                            }

                            pushIfProcessable(KookUpdatedChannelEvent) {
                                KookUpdatedChannelEventImpl(
                                    thisBot,
                                    event.doAs(),
                                    guild,
                                    channel
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

                        if (removed is KookChannelImpl) {
                            pushIfProcessable(KookDeletedChannelEvent) {
                                KookDeletedChannelEventImpl(
                                    thisBot,
                                    event.doAs(),
                                    guild,
                                    removed
                                )
                            }
                        } else if (removed is KookChannelCategoryImpl) {
                            pushIfProcessable(KookDeletedCategoryEvent) {
                                KookDeletedCategoryEventImpl(
                                    thisBot,
                                    event.doAs(),
                                    guild,
                                    removed
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
                                channel
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
                                channel
                            )
                        }
                    }

                    // 按钮点击
                    is MessageBtnClickEventExtra -> {
                        pushIfProcessable(KookMessageBtnClickEvent) {
                            KookMessageBtnClickEventImpl(
                                thisBot,
                                event.doAs()
                            )
                        }
                    }

                    else -> pushUnsupported(event)
                }


            }
        }


    }
}

@OptIn(DiscreetSimbotApi::class)
private suspend fun KookBotImpl.pushUnsupported(event: KEvent<EventExtra>) {
    pushIfProcessable(UnsupportedKookEvent) {
        UnsupportedKookEvent(
            this,
            event
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

// TODO
@OptIn(ApiResultType::class)
private fun Channel.toChannelInfo(): ChannelInfo {
    return ChannelInfo(
        id = this.id,
        name = this.name,
        isCategory = this.isCategory,
        userId = this.userId,
        parentId = this.parentId,
        level = this.level,
        type = this.type,
        limitAmount = -1
    )
}
