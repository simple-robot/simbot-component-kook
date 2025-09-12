/*
 *     Copyright (c) 2025. ForteScarlet.
 *
 *     This file is part of simbot-component-kook.
 *
 *     simbot-component-kook is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     simbot-component-kook is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with simbot-component-kook,
 *     If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.component.kook.internal

import love.forte.simbot.common.id.literal
import love.forte.simbot.component.kook.KookChannel
import love.forte.simbot.component.kook.KookChannelUpdater
import love.forte.simbot.component.kook.bot.internal.KookBotImpl
import love.forte.simbot.component.kook.util.requestDataBy
import love.forte.simbot.kook.api.channel.UpdateChannelApi
import love.forte.simbot.kook.api.channel.toChannel

/**
 * @author ForteScarlet
 */
internal class KookChannelUpdaterImpl(override val channel: KookChannel, private val bot: KookBotImpl) :
    KookChannelUpdater {
    override var builder: UpdateChannelApi.Builder = UpdateChannelApi.builder(channel.id.literal)

    override suspend fun execute(): KookChannel {
        val api = builder.build()
        val result = api.requestDataBy(channel.bot)
        val resultChannel = result.toChannel()

        val updatedChannel = if (result.isCategory) {
            resultChannel.toCategoryChannel(bot, resultChannel.toCategory(bot, null))
        } else {
            resultChannel.toChatChannel(bot, null)
        }

        return updatedChannel
    }
}