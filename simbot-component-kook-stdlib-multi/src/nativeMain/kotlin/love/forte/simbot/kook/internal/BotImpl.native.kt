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

import io.ktor.client.plugins.websocket.*
import love.forte.simbot.kook.BotConfiguration

/**
 * 由平台实现，使 ws client 支持 compress 解压缩。
 *
 * native 平台暂不支持。
 */
internal actual fun WebSockets.Config.supportCompress(
    bot: BotImpl,
    configuration: BotConfiguration,
    engineConfiguration: BotConfiguration.EngineConfiguration?
) {
    bot.botLogger.warn("Native platform does not support compress yet.")
}
