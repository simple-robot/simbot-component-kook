/*
 *     Copyright (c) 2023-2024. ForteScarlet.
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

import love.forte.simbot.component.kook.KookCategory
import love.forte.simbot.component.kook.KookCategoryChannel
import love.forte.simbot.component.kook.bot.internal.KookBotImpl
import love.forte.simbot.kook.objects.Channel
import kotlin.coroutines.CoroutineContext

internal class KookCategoryImpl(
    val bot: KookBotImpl,
    override val source: Channel,
    private val sourceChannel: KookCategoryChannel? = null
) : KookCategory {
    override fun toChannel(): KookCategoryChannel =
        sourceChannel ?: KookCategoryChannelImpl(bot, source, this)

    override fun toString(): String {
        return "KookCategory(id=${source.id}, name=${source.name})"
    }
}

internal class KookCategoryChannelImpl(
    val bot: KookBotImpl,
    override val source: Channel,
    private val sourceCategory: KookCategory? = null
) : KookCategoryChannel {
    override val coroutineContext: CoroutineContext
        get() = bot.subContext

    override val category: KookCategory
        get() = sourceCategory ?: KookCategoryImpl(bot, source)

    override fun toString(): String {
        return "KookChannelCategory(id=${source.id}, name=${source.name})"
    }
}
