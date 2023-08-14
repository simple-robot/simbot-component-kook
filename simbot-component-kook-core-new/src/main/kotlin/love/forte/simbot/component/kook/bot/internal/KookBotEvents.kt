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
import love.forte.simbot.component.kook.event.*
import love.forte.simbot.component.kook.event.internal.KookBotSelfJoinedGuildEventImpl
import love.forte.simbot.component.kook.event.internal.KookMemberExitedChannelEventImpl
import love.forte.simbot.component.kook.event.internal.KookMemberJoinedChannelEventImpl
import love.forte.simbot.component.kook.event.internal.KookMemberJoinedGuildEventImpl
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
        // TODO 事件处理

        when (val ex = extra) {
            is TextExtra -> {
                // TODO 消息事件

            }

            is SystemExtra -> {

                // TODO 系统事件
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

                    //
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
                                channels[it.id] = KookChannelImpl(thisBot, it, guildId)
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

                    //
                    is SelfExitedGuildEventExtra -> {

                        lateinit var botMember: KookMemberImpl

                        val guild = inCacheModify {
                            // remove guilds
                            val guildId = ex.body.guildId
                            val removedGuild = guilds.remove(guildId)
                                ?: run {
                                    logger.warn("Unknown guild {} in event {}", guildId, event)
                                    return@inCacheModify null
                                }
                            // remove channels
                            channels.entries.removeIf { (_, v) -> v._guildId == guildId }
                            // remove members
                            val memberCacheIdPrefix = memberCacheIdGuildPrefix(guildId)
                            members.entries.removeIf { (k, v) ->
                                if (k.startsWith(memberCacheIdPrefix)) {
                                    // check botMember
                                    if (v.source.id == thisBot.sourceBot.botUserInfo.id) {
                                        botMember = v
                                    }
                                    return@removeIf true
                                }

                                false
                            }

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

                    // TODO

                    else -> TODO()
                }


            }
        }


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
