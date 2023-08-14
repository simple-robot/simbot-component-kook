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

import kotlinx.coroutines.launch
import love.forte.simbot.component.kook.event.KookMemberJoinedGuildEvent
import love.forte.simbot.component.kook.event.internal.KookMemberJoinedGuildEventImpl
import love.forte.simbot.component.kook.internal.KookMemberImpl
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.event.Event
import love.forte.simbot.kook.api.user.GetUserViewApi
import love.forte.simbot.kook.event.ExitedGuildEventExtra
import love.forte.simbot.kook.event.JoinedGuildEventExtra
import love.forte.simbot.kook.event.SystemExtra
import love.forte.simbot.kook.event.TextExtra


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

                        @Suppress("UNCHECKED_CAST")
                        pushIfProcessable(KookMemberJoinedGuildEvent) {
                            KookMemberJoinedGuildEventImpl(
                                thisBot,
                                event as love.forte.simbot.kook.event.Event<JoinedGuildEventExtra>,
                                guild,
                                newMember
                            )
                        }
                    }


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
