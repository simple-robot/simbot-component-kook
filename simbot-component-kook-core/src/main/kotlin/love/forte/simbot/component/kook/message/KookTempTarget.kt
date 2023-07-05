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

package love.forte.simbot.component.kook.message

import kotlinx.serialization.Serializable
import love.forte.simbot.ID
import love.forte.simbot.message.Message
import love.forte.simbot.message.doSafeCast

// TODO

/**
 *
 * @author ForteScarlet
 */
@Serializable
public data class KookTempTarget(val id: ID) : KookMessageElement<KookTempTarget> {
    override val key: Message.Key<KookTempTarget> get() = Key

    public companion object Key : Message.Key<KookTempTarget> {
        override fun safeCast(value: Any): KookTempTarget? = doSafeCast(value)
    }
}
