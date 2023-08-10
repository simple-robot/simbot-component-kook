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

package love.forte.simbot.component.kook.bot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.bot.BotVerifyInfo
import love.forte.simbot.component.kook.bot.KookBotVerifyInfoConfiguration.Ticket
import love.forte.simbot.kook.TokenType

/**
 * `.bot` 配置文件读取的配置信息实体, 用于接收从 [BotVerifyInfo] 中的序列化信息。
 *
 * 在 [KookBotVerifyInfoConfiguration] 中，[Ticket.clientId] 和 [Ticket.token] 为必选项，
 * 存在于当前配置属性的最外层。除了必选项以外还存在部分可选项存在于 [KookBotVerifyInfoConfiguration.Config] 类型中，
 * 作为 [config][KookBotVerifyInfoConfiguration.config] 属性使用。
 *
 * 简化json e.g.
 * ```json
 * {
 *   "component": "simbot.kook",
 *   "ticket": {
 *     "clientId": "Your client ID",
 *     "token": "Your ws token",
 *   }
 * }
 * ```
 *
 * 完整json e.g.
 * ```json
 * {
 *  "component": "simbot.kook",
 *  "clientId": "Your client ID",
 *  "token": "Your ws token",
 *  "config": {
 *      "isCompress": true,
 *      "syncPeriods": {
 *          "guild": {
 *              "syncPeriod": 180000,
 *              "batchDelay": 0
 *          }
 *       }
 *    }
 * }
 * ```
 *
 */
@Serializable
public data class KookBotVerifyInfoConfiguration(
    /**
     * 票据信息
     */
    val ticket: Ticket,

    /**
     * 额外的部分可选配置属性。
     */
    val config: Config? = null
) {

    /**
     * Bot Ticket 信息
     */
    @Serializable
    public data class Ticket(
        override val clientId: String,
        override val token: String,
        @SerialName("tokenType")
        val tokenTypePrefix: String = TokenType.BOT.prefix
    ) : love.forte.simbot.kook.Ticket {
        override val type: TokenType
            get() = TokenType.byPrefix(tokenTypePrefix)
    }


    /**
     * 在 [KookBotVerifyInfoConfiguration] 中除了必须的bot信息以外的可选配置信息。
     *
     */
    @Serializable
    public data class Config(
        /**
         * 是否压缩数据。
         */
        val isCompress: Boolean? = null,

        /**
         * 缓存对象信息的同步周期
         */
        val syncPeriods: KookBotConfiguration.SyncPeriods? = null,

        ) {
        public companion object

    }


    internal fun includeConfig(configuration: KookBotConfiguration) {
        if (config != null) {
            config.isCompress?.also { configuration.botConfiguration.isCompress = it }
            config.syncPeriods?.also { configuration.syncPeriods = it }
        }
        // TODO
    }


    public companion object {
        // TODO
//        internal val serializersModule = SerializersModule {

//        }
    }
}
