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

package love.forte.simbot.component.kook

import kotlinx.serialization.Serializable

/**
 * `.bot` 配置文件读取的配置信息实体, 用于接收从 [BotVerifyInfo] 中的序列化信息。
 *
 * 在 [KookBotVerifyInfoConfiguration] 中，[clientId] 和 [token] 为必选项，
 * 存在于当前配置属性的最外层。除了必选项以外还存在部分可选项存在于 [KookBotVerifyInfoConfiguration.Config] 类型中，
 * 作为 [config][KookBotVerifyInfoConfiguration.config] 属性使用。
 *
 * 简化json e.g.
 * ```json
 * {
 *   "component": "simbot.kook",
 *   "clientId": "Your client ID",
 *   "token": "Your ws token"
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
 *          "guildSyncPeriod": 60000,
 *          "memberSyncPeriods": 60000
 *          }
 *      }
 * }
 * ```
 *
 */
@Serializable
public data class KookBotVerifyInfoConfiguration(
    /**
     * client id
     */
    val clientId: String,

    /**
     * token
     */
    val token: String,

    // TODO ticket

    /**
     * 额外的部分可选配置属性。
     */
    val config: Config = Config.DEFAULT,
) {

    /**
     * 在 [KookBotVerifyInfoConfiguration] 中除了必须的bot信息以外的可选配置信息。
     *
     */
    @Serializable
    public data class Config(
        /**
         * 是否压缩数据。
         */
        val isCompress: Boolean = true,

        /**
         * 缓存对象信息的同步周期
         */
        val syncPeriods: KookComponentBotConfiguration.SyncPeriods = KookComponentBotConfiguration.SyncPeriods(),

        ) {
        public companion object {
            /**
             * [Config] 全默认属性实例。
             *
             */
            @JvmField
            public val DEFAULT: Config = Config()
        }
    }


    internal fun includeConfig(configuration: KookComponentBotConfiguration) {
        configuration.botConfiguration.isCompress = config.isCompress
        configuration.syncPeriods = config.syncPeriods
    }


    public companion object {
        // TODO
//        internal val serializersModule = SerializersModule {

//        }
    }
}
