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
import love.forte.simbot.kook.BotConfiguration
import love.forte.simbot.kook.ProcessorType
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
 *       },
 *       "clientEngineConfig": {
 *            "threadsCount": null,
 *            "pipelining": null
 *       },
 *       "wsEngineConfig": {
 *            "threadsCount": null,
 *            "pipelining": null
 *       },
 *       "timeout": {
 *           "connectTimeoutMillis": 5000,
 *           "requestTimeoutMillis": 5000,
 *           "socketTimeoutMillis": null
 *       },
 *       "wsConnectTimeout": null,
 *       "isNormalEventProcessAsync": null
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

        /**
         * 参考 [BotConfiguration.clientEngineConfig]
         * @see BotConfiguration.clientEngineConfig
         */
        val clientEngineConfig: BotConfiguration.EngineConfiguration? = null,

        /**
         * 参考 [BotConfiguration.wsEngineConfig]
         * @see BotConfiguration.wsEngineConfig
         */
        val wsEngineConfig: BotConfiguration.EngineConfiguration? = null,

        /**
         * 参考 [BotConfiguration.timeout]
         *
         * @see BotConfiguration.timeout
         *
         */
        val timeout: BotConfiguration.TimeoutConfiguration? = null,

        /**
         * ws连接超时时间，单位 ms 。
         *
         * 更多说明参考 [BotConfiguration.wsConnectTimeout]
         *
         * @see BotConfiguration.wsConnectTimeout
         */
        val wsConnectTimeout: Long? = null,


        /**
         * [ProcessorType.NORMAL] 类型的事件处理器是否在异步中执行。
         *
         * 更多说明参考 [BotConfiguration.isNormalEventProcessAsync]
         *
         * @see BotConfiguration.isNormalEventProcessAsync
         */
        val isNormalEventProcessAsync: Boolean? = null,

        ) {
        public companion object
    }


    internal fun includeConfig(configuration: KookBotConfiguration) {
        if (config != null) {
            config.syncPeriods?.also { configuration.syncPeriods = it }

            config.isCompress?.also { configuration.botConfiguration.isCompress = it }
            config.clientEngineConfig?.also { configuration.botConfiguration.clientEngineConfig = it }
            config.wsEngineConfig?.also { configuration.botConfiguration.wsEngineConfig = it }
            config.timeout?.also { configuration.botConfiguration.timeout = it }

            config.wsConnectTimeout?.also { configuration.botConfiguration.wsConnectTimeout = it }
            config.isNormalEventProcessAsync?.also { configuration.botConfiguration.isNormalEventProcessAsync = it }
        }
    }


    public companion object
}

