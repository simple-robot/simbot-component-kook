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

package love.forte.simbot.kook.internal

import love.forte.simbot.kook.api.GetGatewayApi
import love.forte.simbot.logger.Logger
import love.forte.simbot.util.stageloop.StageLoop


internal abstract class Stage : love.forte.simbot.util.stageloop.Stage<Stage>() {
    internal abstract val bot: BotImpl
    internal abstract val botLogger: Logger
}

internal data class ReconnectInfo(val sn: Long, val sessionId: String)

internal class Connect(
    override val bot: BotImpl,
    override val botLogger: Logger,
    private val isCompress: Boolean,
    /**
     * 重连次数，如果为 1 则为第一次连接。
     */
    private val time: Int = 1,
    /**
     * 重连信息，如果存在则为重连。
     */
    private val reconnect: ReconnectInfo? = null,
) : Stage() {
    override suspend fun invoke(loop: StageLoop<Stage>) {
        // 获取 gateway
        botLogger.debug("Requesting for gateway info... time: {}", time)

        val gateway = GetGatewayApi.create(isCompress).requestBy(bot)
        // TODO

        TODO("Not yet implemented")
    }
}
