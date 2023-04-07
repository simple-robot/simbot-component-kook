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

import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


/**
 * 用于承载 mute job 的可传递容器。
 *
 * @author ForteScarlet
 */
internal class MuteJob(val lock: Mutex = Mutex()) {

    @Volatile
    internal var muteJob: Job? = null
        private set

    suspend fun remove(): Job? = lock.withLock {
        muteJob?.also {
            muteJob = null
        }
    }


    suspend inline fun update(block: (old: Job?) -> Job): Job? = lock.withLock {
        val old = muteJob
        muteJob = block(old)
        old
    }

}
