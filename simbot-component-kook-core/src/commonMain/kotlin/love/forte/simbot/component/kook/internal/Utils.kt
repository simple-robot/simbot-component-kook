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

import love.forte.simbot.ability.DeleteOption
import love.forte.simbot.ability.StandardDeleteOption
import love.forte.simbot.component.kook.bot.KookBot
import love.forte.simbot.component.kook.util.requestData
import love.forte.simbot.kook.api.channel.DeleteChannelApi

internal suspend fun KookBot.deleteChannel(id: String, options: Array<out DeleteOption>) {
    val api = DeleteChannelApi.create(id)

    if (StandardDeleteOption.IGNORE_ON_FAILURE in options) {
        runCatching { requestData(api) }
    } else {
        requestData(api)
    }
}