/*
 * Copyright (c) 2022-2023. ForteScarlet.
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
package love.forte.simbot.kook.api.invite

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.ID
import love.forte.simbot.Simbot
import love.forte.simbot.kook.api.ApiResultType
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookPostRequest
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.DURATION_HALF_DAY
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.DURATION_HALF_HOUR
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.DURATION_NEVER
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.DURATION_ONE_DAY
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.DURATION_ONE_HOUR
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.DURATION_ONE_WEEK
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.DURATION_SEVEN_DAYS
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.DURATION_SIX_HOURS
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.DURATION_TWELVE_HOURS
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.SETTING_TIMES_1
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.SETTING_TIMES_10
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.SETTING_TIMES_100
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.SETTING_TIMES_25
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.SETTING_TIMES_5
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.SETTING_TIMES_50
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.SETTING_TIMES_FIFTY
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.SETTING_TIMES_FIVE
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.SETTING_TIMES_ONE
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.SETTING_TIMES_ONE_HUNDRED
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.SETTING_TIMES_TEN
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.SETTING_TIMES_TWENTY_FIVE
import love.forte.simbot.kook.api.invite.InviteCreateRequest.Key.SETTING_TIMES_UNLIMITED

/**
 *
 * [创建邀请链接](https://developer.kaiheila.cn/doc/http/invite#创建邀请链接)
 *
 * `/api/v3/invite/create`
 *
 * method POST
 */
public class InviteCreateRequest internal constructor(
    /**
     * 服务器 id. 服务器 id 或者频道 id 必须填一个
     */
    private val guildId: ID? = null,
    /**
     * 频道 id. 服务器 id 或者频道 id 必须填一个
     */
    private val channelId: ID? = null,
    /**
     * 邀请链接有效时长（秒），默认 7 天。
     * 可选值：
     * - 0 => 永不; see [DURATION_NEVER].
     * - 1800 => 0.5 小时; see [DURATION_NEVER].
     * - 3600 => 1 个小时; see [DURATION_NEVER].
     * - 21600 => 6 个小时; see [DURATION_NEVER].
     * - 43200 => 12 个小时; see [DURATION_NEVER].
     * - 86400 => 1 天; see [DURATION_NEVER].
     * - 604800 => 7 天; see [DURATION_NEVER].
     */
    private val duration: Int = DURATION_ONE_WEEK,
    
    /**
     * 设置的次数，默认-1。可选值：
     * - -1 => 无限制；
     * - 1 => 1 次使用；
     * - 5 => 5 次使用；
     * - 10 => 10 次使用；
     * - 25 => 25 次使用；
     * - 50 => 50 次使用；
     * - 100 => 100 次使用；
     */
    private val settingTimes: Int = SETTING_TIMES_UNLIMITED

) : KookPostRequest<InviteCreated>() {
    
    
    @Suppress("MemberVisibilityCanBePrivate")
    public companion object Key : BaseKookApiRequestKey("invite", "create") {
        
        //region duration常量
        /** 0 => 永不 */
        public const val DURATION_NEVER: Int = 0
        
        /** 1800 => 0.5 小时 => 半个小时 */
        public const val DURATION_HALF_HOUR: Int = 1800
        
        /** 3600 => 1 个小时 */
        public const val DURATION_ONE_HOUR: Int = 3600
        
        /** 21600 => 6 个小时 */
        public const val DURATION_SIX_HOURS: Int = 21600
        
        /** 43200 => 12 个小时 => 半天 */
        public const val DURATION_TWELVE_HOURS: Int = 43200
        
        /**
         * 43200 => 12 个小时 => 半天
         * @see DURATION_TWELVE_HOURS
         * */
        public const val DURATION_HALF_DAY: Int = DURATION_TWELVE_HOURS
        
        /** 86400 => 1 天; */
        public const val DURATION_ONE_DAY: Int = 86400
        
        /** 604800 => 7 天 */
        public const val DURATION_SEVEN_DAYS: Int = 604800
        
        /**
         * 604800 => 7 天;
         * @see DURATION_SEVEN_DAYS
         */
        public const val DURATION_ONE_WEEK: Int = DURATION_SEVEN_DAYS
        //endregion
        
        //region settingTimes常量
        /** -1 => 无限制 */
        public const val SETTING_TIMES_UNLIMITED: Int = -1
        
        /** 1 => 1 次使用 */
        public const val SETTING_TIMES_ONE: Int = 1
        
        /** 1 => 1 次使用 */
        public const val SETTING_TIMES_1: Int = SETTING_TIMES_ONE
        
        /** 5 => 5 次使用 */
        public const val SETTING_TIMES_FIVE: Int = 5
        
        /** 5 => 5 次使用 */
        public const val SETTING_TIMES_5: Int = SETTING_TIMES_FIVE
        
        /** -10 => 10 次使用 */
        public const val SETTING_TIMES_TEN: Int = 10
        
        /** -10 => 10 次使用 */
        public const val SETTING_TIMES_10: Int = SETTING_TIMES_TEN
        
        /** 25 => 25 次使用 */
        public const val SETTING_TIMES_TWENTY_FIVE: Int = 25
        
        /** 25 => 25 次使用 */
        public const val SETTING_TIMES_25: Int = SETTING_TIMES_TWENTY_FIVE
        
        /** 50 => 50 次使用 */
        public const val SETTING_TIMES_FIFTY: Int = 50
        
        /** 50 => 50 次使用 */
        public const val SETTING_TIMES_50: Int = SETTING_TIMES_FIFTY
        
        /** 100 => 100 次使用 */
        public const val SETTING_TIMES_ONE_HUNDRED: Int = 100
        
        /** 100 => 100 次使用 */
        public const val SETTING_TIMES_100: Int = SETTING_TIMES_ONE_HUNDRED
        //endregion
        
        /**
         * 根据完整参数构造 [InviteCreateRequest]. 也可参考 [byGuild] 或 [byChannel] 来避免手误。
         *
         * @see byGuild
         * @see byChannel
         *
         * @param guildId 服务器 id. [服务器 id][guildId] 或者 [频道 id][channelId] 必须填一个
         * @param channelId 频道 id. [服务器 id][guildId] 或者 [频道 id][channelId] 必须填一个
         * @param duration 邀请链接有效时长（秒），默认 [7天][DURATION_ONE_WEEK]。
         * 可选值：
         * - 0 => 永不; see [DURATION_NEVER].
         * - 1800 => 0.5 小时; see [DURATION_HALF_HOUR].
         * - 3600 => 1 个小时; see [DURATION_ONE_HOUR].
         * - 21600 => 6 个小时; see [DURATION_SIX_HOURS].
         * - 43200 => 12 个小时; see [DURATION_TWELVE_HOURS].
         * - 86400 => 1 天; see [DURATION_ONE_DAY].
         * - 604800 => 7 天; see [DURATION_SEVEN_DAYS].
         *
         * 你可以参考 [InviteCreateRequest] 中以 `DURATION_` 开头的常量，例如 [DURATION_NEVER].
         *
         *
         * @see DURATION_NEVER
         * @see DURATION_HALF_HOUR
         * @see DURATION_ONE_HOUR
         * @see DURATION_SIX_HOURS
         * @see DURATION_TWELVE_HOURS
         * @see DURATION_HALF_DAY
         * @see DURATION_ONE_DAY
         * @see DURATION_SEVEN_DAYS
         * @see DURATION_ONE_WEEK
         *
         * @param settingTimes 设置的次数，默认[-1][SETTING_TIMES_UNLIMITED]。可选值：
         * - -1 => 无限制；
         * - 1 => 1 次使用；
         * - 5 => 5 次使用；
         * - 10 => 10 次使用；
         * - 25 => 25 次使用；
         * - 50 => 50 次使用；
         * - 100 => 100 次使用；
         *
         * 你可以参考 [InviteCreateRequest] 中以 `SETTING_TIMES_` 开头的常量，例如 [SETTING_TIMES_UNLIMITED].
         *
         * @see SETTING_TIMES_UNLIMITED
         * @see SETTING_TIMES_ONE
         * @see SETTING_TIMES_1
         * @see SETTING_TIMES_FIVE
         * @see SETTING_TIMES_5
         * @see SETTING_TIMES_TEN
         * @see SETTING_TIMES_10
         * @see SETTING_TIMES_TWENTY_FIVE
         * @see SETTING_TIMES_25
         * @see SETTING_TIMES_FIFTY
         * @see SETTING_TIMES_50
         * @see SETTING_TIMES_ONE_HUNDRED
         * @see SETTING_TIMES_100
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            guildId: ID? = null,
            channelId: ID? = null,
            duration: Int = DURATION_ONE_WEEK,
            settingTimes: Int = SETTING_TIMES_UNLIMITED
        ): InviteCreateRequest =
            InviteCreateRequest(guildId, channelId, duration, settingTimes)
        
        
        /**
         * 通过 channelId 构建请求。参数含义描述参考 [create]
         * @see create
         */
        @JvmStatic
        @JvmOverloads
        public fun byChannel(
            channelId: ID,
            duration: Int = DURATION_ONE_WEEK,
            settingTimes: Int = SETTING_TIMES_UNLIMITED
        ): InviteCreateRequest {
            return create(null, channelId, duration, settingTimes)
        }
        
        /**
         * 通过 guildId 构建请求。参数含义描述参考 [create]
         * @see create
         */
        @JvmStatic
        @JvmOverloads
        public fun byGuild(
            guildId: ID,
            duration: Int = DURATION_ONE_WEEK,
            settingTimes: Int = SETTING_TIMES_UNLIMITED
        ): InviteCreateRequest {
            return create(guildId, null, duration, settingTimes)
        }
        
    }
    
    init {
        Simbot.require(guildId != null || channelId != null) {
            "A guild id or channel id must exist"
        }
    }
    
    override val resultDeserializer: DeserializationStrategy<out InviteCreated>
        get() = InviteCreated.serializer()
    
    override val apiPaths: List<String> get() = apiPathList
    
    override fun createBody(): Any = Body(
        guildId,
        channelId,
        duration,
        settingTimes
    )
    
    @Serializable
    private data class Body(
        @SerialName("guild_id") @Serializable(ID.AsCharSequenceIDSerializer::class)
        val guildId: ID?,
        @SerialName("channel_id") @Serializable(ID.AsCharSequenceIDSerializer::class)
        val channelId: ID?,
        val duration: Int,
        @SerialName("setting_times")
        val settingTimes: Int
    
    )
    
    
}


/**
 * API [InviteCreateRequest] 的响应体。
 */
@Serializable
public data class InviteCreated @ApiResultType constructor(public val url: String)
