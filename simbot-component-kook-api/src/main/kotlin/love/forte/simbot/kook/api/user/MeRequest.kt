/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.api.user

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.LongID
import love.forte.simbot.kook.api.ApiResultType
import love.forte.simbot.kook.api.KookGetRequest
import love.forte.simbot.kook.objects.User
import love.forte.simbot.kook.util.unmodifiableListOf

/**
 * [获取当前用户信息](https://developer.kaiheila.cn/doc/http/user#%E8%8E%B7%E5%8F%96%E5%BD%93%E5%89%8D%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF)
 *
 * @author ForteScarlet
 */
public object MeRequest : KookGetRequest<Me>() {
    override val apiPaths: List<String> = unmodifiableListOf("user", "me")

    override val resultDeserializer: DeserializationStrategy<out Me>
        get() = Me.serializer()
}


/**
 * api [MeRequest] 的响应体。
 */
@Serializable
public data class Me @ApiResultType constructor(
    override val id: CharSequenceID,
    override val username: String,
    @SerialName("identify_num")
    override val identifyNum: String,
    @SerialName("online")
    override val isOnline: Boolean,
    override val status: Int,
    override val avatar: String,
    override val vipAvatar: String? = null,
    @SerialName("bot")
    override val isBot: Boolean,
    @SerialName("mobile_verified")
    override val mobileVerified: Boolean,
    override val nickname: String = username,
    override val roles: List<LongID> = emptyList(),
) : User
