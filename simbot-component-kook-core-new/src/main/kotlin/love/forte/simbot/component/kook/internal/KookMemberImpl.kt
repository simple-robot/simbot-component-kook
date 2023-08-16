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

package love.forte.simbot.component.kook.internal

import kotlinx.coroutines.flow.flow
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.JavaDuration
import love.forte.simbot.component.kook.KookMember
import love.forte.simbot.component.kook.bot.internal.KookBotImpl
import love.forte.simbot.component.kook.kookGuildNotExistsException
import love.forte.simbot.component.kook.message.KookMessageReceipt
import love.forte.simbot.component.kook.role.internal.KookGuildRoleImpl
import love.forte.simbot.component.kook.role.internal.KookMemberRoleImpl
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.delegate.getValue
import love.forte.simbot.delegate.stringID
import love.forte.simbot.kook.api.guild.CreateGuildMuteApi
import love.forte.simbot.kook.api.guild.DeleteGuildMuteApi
import love.forte.simbot.kook.api.user.GetUserViewApi
import love.forte.simbot.kook.objects.User
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.utils.item.Items
import love.forte.simbot.utils.item.effectedItemsByFlow
import java.util.concurrent.TimeUnit
import kotlin.time.Duration


/**
 *
 * @author ForteScarlet
 */
internal class KookMemberImpl(
    override val bot: KookBotImpl,
    override val source: User,
    internal val guildIdValue: String
) : KookMember {
    override val guildId: ID by stringID { guildIdValue }

    private val guildValue
        get() = bot.internalGuild(guildIdValue) ?: throw kookGuildNotExistsException(guildIdValue)


    override suspend fun send(message: Message): KookMessageReceipt {
        return asContact().send(message)
    }

    override suspend fun send(message: MessageContent): KookMessageReceipt {
        return asContact().send(message)
    }

    override suspend fun send(text: String): KookMessageReceipt {
        return asContact().send(text)
    }

    override suspend fun guild(): KookGuildImpl = guildValue

    @ExperimentalSimbotApi
    override val roles: Items<KookMemberRoleImpl>
        get() = effectedItemsByFlow {
            flow {
                val view = GetUserViewApi.create(source.id, guildIdValue).requestDataBy(bot)
                val guildRoleMap = mutableMapOf<Long, KookGuildRoleImpl>()

                guildValue.roles.collect { r ->
                    guildRoleMap[r.source.roleId] = r
                }

                view.roles?.forEach { rid ->
                    val role = guildRoleMap[rid] ?: return@forEach
                    emit(KookMemberRoleImpl(bot, this@KookMemberImpl, guildIdValue, role))
                }
            }
        }

    // TODO
    private suspend fun mute0(type: Int, milli: Long): Boolean {
        val api = CreateGuildMuteApi.create(guildIdValue, source.id, type)

        TODO("Not yet implemented")
    }


    override suspend fun mute(type: Int): Boolean = mute0(type, 0L)

    @ExperimentalSimbotApi
    override suspend fun mute(type: Int, duration: Duration): Boolean = mute0(type, duration.inWholeMilliseconds)

    @ExperimentalSimbotApi
    override suspend fun mute(type: Int, duration: JavaDuration): Boolean = mute0(type, duration.toMillis())

    @ExperimentalSimbotApi
    override suspend fun mute(type: Int, time: Long, timeUnit: TimeUnit): Boolean = mute0(type, timeUnit.toMillis(time))

    override suspend fun unmute(type: Int): Boolean {
        //  TODO cancel MuteJob?
        DeleteGuildMuteApi.create(guildIdValue, source.id, type).requestDataBy(bot)

        return true
    }

    private suspend fun asContact(): KookUserChatImpl = bot.contact(id)
}
