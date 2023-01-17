/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
 *  
 *  本文件是 simbot-component-kook 的一部分。
 *
 *  simbot-component-kook 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kook 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:  
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *  
 *   
 */
package love.forte.simbot.kook.api.invite

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import love.forte.simbot.ID
import love.forte.simbot.Simbot
import love.forte.simbot.kook.api.BaseKookApiRequestKey
import love.forte.simbot.kook.api.KookApiResult
import love.forte.simbot.kook.api.KookGetRequest
import love.forte.simbot.kook.api.appendIfNotnull
import love.forte.simbot.literal

/**
 *
 * [获取邀请列表](https://developer.kaiheila.cn/doc/http/invite#%E8%8E%B7%E5%8F%96%E9%82%80%E8%AF%B7%E5%88%97%E8%A1%A8)
 *
 * `/api/v3/invite/list`
 *
 * method: GET
 *
 */
public class InviteListRequest internal constructor(
    private val guildId: ID?,
    private val channelId: ID?,
    private val page: Int = -1,
    private val pageSize: Int = -1,
) : KookGetRequest<KookApiResult.ListData<InviteInfo>>() {
    public companion object Key : BaseKookApiRequestKey("invite", "list") {
        private val serializer = KookApiResult.ListData.serializer(InviteInfoImpl.serializer())
        
        /**
         * 构造 [InviteListRequest]
         * @param guildId 服务器 id. 服务器 id 或者频道 id 必须填一个
         * @param channelId 频道 id. 服务器 id 或者频道 id 必须填一个
         * @param page 目标页数. 不小于0时有效。
         * @param pageSize 每页数据数量. 不小于0时有效。
         */
        @JvmStatic
        @JvmOverloads
        public fun create(guildId: ID?, channelId: ID?, page: Int = -1, pageSize: Int = -1): InviteListRequest =
            InviteListRequest(guildId, channelId, page, pageSize)
    }
    
    init {
        Simbot.require(guildId != null || channelId != null) {
            "A guild id or channel id must exist"
        }
    }
    
    override val resultDeserializer: DeserializationStrategy<out KookApiResult.ListData<InviteInfo>>
        get() = serializer
    
    override val apiPaths: List<String>
        get() = apiPathList
    
    override fun ParametersBuilder.buildParameters() {
        appendIfNotnull("guild_id", guildId) { it.literal }
        appendIfNotnull("channel_id", channelId) { it.literal }
        if (page > 0) {
            append("page", page.toString())
        }
        if (pageSize > 0) {
            append("page_size", pageSize.toString())
        }
    }
}

