/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kook.api.channel

import io.ktor.client.*
import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.kook.api.*
import love.forte.simbot.kook.objects.Channel
import love.forte.simbot.kook.objects.ChannelPermissionOverwrites
import love.forte.simbot.kook.objects.impl.ChannelPermissionOverwritesImpl
import love.forte.simbot.literal


/**
 * [获取频道列表](https://developer.kaiheila.cn/doc/http/channel#%E8%8E%B7%E5%8F%96%E9%A2%91%E9%81%93%E5%88%97%E8%A1%A8)
 *
 * request method: GET
 *
 */
public class ChannelListRequest internal constructor(
    private val guildId: ID,
    private val page: Int? = null,
    private val pageSize: Int? = null,
    private val type: Int? = null,
) : KookGetRequest<KookApiResult.ListData<ChannelInfo>>() {
    public companion object Key : BaseKookApiRequestKey("channel", "list") {
        private val serializer = KookApiResult.ListData.serializer(ChannelInfoImpl.serializer())
    
        /**
         * 构建 [ChannelListRequest]
         * @param guildId 频道服务器ID
         * @param page 目标页数
         * @param pageSize 每页数据数量
         * @param type 频道类型, `1`为文字，`2`为语音, 默认为`1`.
         *
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            guildId: ID,
            page: Int? = null,
            pageSize: Int? = null,
            type: Int? = null,
        ): ChannelListRequest = ChannelListRequest(guildId, page, pageSize, type)
    }
    
    override val resultDeserializer: DeserializationStrategy<out KookApiResult.ListData<ChannelInfo>>
        get() = serializer
    
    override val apiPaths: List<String>
        get() = apiPathList
    
    override fun ParametersBuilder.buildParameters() {
        append("guild_id", guildId.literal)
        appendIfNotnull("page", page)
        appendIfNotnull("page_size", pageSize)
        appendIfNotnull("type", type)
    }
    
    @JvmSynthetic
    override suspend fun requestData(
        client: HttpClient,
        authorization: String,
        decoder: Json,
    ): KookApiResult.ListData<ChannelInfo> {
        val data = super.requestData(client, authorization, decoder)
        data.items.forEach {
            (it as? ChannelInfoImpl)?.guildIdLate = guildId
        }
        return data
    }
    
}

/**
 * Api [ChannelListRequest] 的响应体。
 * 会在使用 [ChannelListRequest.requestData] 的时候对 guildId 进行初始化。
 */
public interface ChannelInfo : Channel {
    /**
     * 频道id
     */
    override val id: ID
    
    /**
     *	频道名称
     */
    override val name: String
    
    /**
     * 是否为分组类型
     */
    override val isCategory: Boolean
    
    /**
     *	频道创建者id
     */
    override val userId: ID
    
    /**
     *	父分组频道id
     */
    override val parentId: ID
    
    /**
     * 频道排序
     */
    override val level: Int
    
    /**
     * 频道类型
     */
    override val type: Int
    
    /**
     * 人数限制
     */
    override val maximumMember: Int
    override val topic: String
    override val slowMode: Int
    override val permissionOverwrites: List<ChannelPermissionOverwrites>
    override val permissionUsers: List<ID>
    override val permissionSync: Int
}


@Serializable
internal data class ChannelInfoImpl @ApiResultType constructor(
    /**
     * 频道id
     */
    override val id: CharSequenceID,
    /**
     * 频道名称
     */
    override val name: String,
    /**
     * 是否为分组类型
     */
    @SerialName("is_category") override val isCategory: Boolean,
    /**
     * 频道创建者id
     */
    @SerialName("user_id") override val userId: CharSequenceID,
    /**
     * 父分组频道id
     */
    @SerialName("parent_id") override val parentId: CharSequenceID,
    /**
     * 频道排序
     */
    override val level: Int,
    /**
     * 频道类型
     */
    override val type: Int,
    
    /**
     * 人数限制
     */
    @SerialName("limit_amount") override val maximumMember: Int,
    
    
    override val topic: String = "",
    @SerialName("slow_mode") override val slowMode: Int = 0,
    @SerialName("permission_overwrites") override val permissionOverwrites: List<ChannelPermissionOverwritesImpl> = emptyList(),
    // @SerialName("permission_users")
    // public val permissionUserEntities: List<UserImpl> = emptyList(),
    @SerialName("permission_sync") override val permissionSync: Int = 0,
) : ChannelInfo {
    
    override val permissionUsers: List<CharSequenceID> get() = emptyList() // permissionUserEntities.map { it.id }
    
    
    @Transient
    internal lateinit var guildIdLate: ID
    override val guildId: ID get() = guildIdLate
    
    override val currentMember: Int
        get() = -1
    
    override val icon: String
        get() = ""
    
    
}





