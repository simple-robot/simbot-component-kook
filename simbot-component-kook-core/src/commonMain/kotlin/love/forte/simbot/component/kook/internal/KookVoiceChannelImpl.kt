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

import love.forte.simbot.component.kook.KookVoiceChannel
import love.forte.simbot.component.kook.bot.internal.KookBotImpl
import love.forte.simbot.kook.objects.Channel
import kotlin.coroutines.CoroutineContext

/**
 *
 * @since 4.2.0
 * @author ForteScarlet
 */
internal class KookVoiceChannelImpl(
    bot: KookBotImpl,
    source: Channel,
) : AbstractKookChatCapableChannelImpl(bot, source),
    KookVoiceChannel {
    override val coroutineContext: CoroutineContext
        get() = bot.subContext

    override fun toString(): String {
        return "KookVoiceChannel(id=${source.id}, name=${source.name}, guildId=${source.guildId})"
    }
}

internal fun Channel.toVoiceChannel(bot: KookBotImpl): KookVoiceChannelImpl {
    return KookVoiceChannelImpl(bot, this)
}
