/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kaiheila 的一部分。
 *
 *  simbot-component-kaiheila 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kaiheila 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

package love.forte.simbot.kaiheila.api.guild

import io.ktor.http.*
import kotlinx.serialization.*
import love.forte.simbot.*
import love.forte.simbot.kaiheila.api.*
import love.forte.simbot.kaiheila.objects.*


/**
 * [获取当前用户加入的服务器列表](https://developer.kaiheila.cn/doc/http/guild#%E8%8E%B7%E5%8F%96%E5%BD%93%E5%89%8D%E7%94%A8%E6%88%B7%E5%8A%A0%E5%85%A5%E7%9A%84%E6%9C%8D%E5%8A%A1%E5%99%A8%E5%88%97%E8%A1%A8).
 *
 * request method: `GET`
 *
 *
 *
 *
 *
 */
public class GuildListRequest @JvmOverloads constructor(
    /**
     * 目标页数
     */
    private val page: Int = -1,
    /**
     * 每页数据数量
     */
    private val pageSize: Int = -1,
    /**
     * 代表排序的字段, 比如-id代表id按DESC排序，id代表id按ASC排序。不一定有, 如果有，接口中会声明支持的排序字段。
     */
    private val sort: String? = null
) :
    KaiheilaGetRequest<KaiheilaApiResult.ListData<Guild>>() {
    public companion object Key : BaseApiRequestKey("guild", "list") {
        private val serializer = KaiheilaApiResult.ListData.serializer(GuildListElement.serializer())
    }

    override val apiPaths: List<String>
        get() = apiPathList

    override val resultDeserializer: DeserializationStrategy<out KaiheilaApiResult.ListData<Guild>>
        get() = serializer

    override fun ParametersBuilder.buildParameters() {
        if (page > 0) {
            append("page", page.toString())
        }
        if (pageSize > 0) {
            append("page_size", pageSize.toString())
        }
        appendIfNotnull("sort", sort)
    }

}


/**
 *
 * [获取当前用户加入的服务器列表](https://developer.kaiheila.cn/doc/http/guild#%E8%8E%B7%E5%8F%96%E5%BD%93%E5%89%8D%E7%94%A8%E6%88%B7%E5%8A%A0%E5%85%A5%E7%9A%84%E6%9C%8D%E5%8A%A1%E5%99%A8%E5%88%97%E8%A1%A8)
 * api的返回值。
 *
 */
@Serializable
internal data class GuildListElement @ApiResultType constructor(
    /**
     * 服务器id
     */
    @Serializable(ID.AsCharSequenceIDSerializer::class)
    override val id: ID,
    /**
     * 服务器名称
     */
    override val name: String,
    /**
     * 服务器主题
     */
    override val topic: String,
    /**
     * 服务器主的id
     */
    @SerialName("master_id")
    @Serializable(ID.AsCharSequenceIDSerializer::class)
    override val masterId: ID,
    /**
     * 	服务器icon的地址
     */
    override val icon: String,

    /**
     * 通知类型,
     * - `0` 代表默认使用服务器通知设置
     * - `1` 代表接收所有通知
     * - `2` 代表仅@被提及
     * - `3` 代表不接收通知
     */
    @SerialName("notify_type")
    override val notifyType: Int,

    /**
     * 服务器默认使用语音区域
     */
    override val region: String,
    /**
     * 是否为公开服务器
     */
    @SerialName("enable_open")
    override val enableOpen: Boolean,
    /**
     * 公开服务器id
     */
    @SerialName("open_id")
    @Serializable(ID.AsCharSequenceIDSerializer::class)
    override val openId: ID,
    /**
     * 	默认频道id
     */
    @SerialName("default_channel_id")
    @Serializable(ID.AsCharSequenceIDSerializer::class)
    override val defaultChannelId: ID,
    /**
     * 欢迎频道id
     */
    @SerialName("welcome_channel_id")
    @Serializable(ID.AsCharSequenceIDSerializer::class)
    override val welcomeChannelId: ID,
) : Guild {
    // TODO
    override val roles: List<Role> = emptyList()

    // TODO
    override val channels: List<Channel> = emptyList()

    // 可选的
    override val maximumChannel: Int = -1
    override val createTime: Timestamp = Timestamp.NotSupport
    override val currentMember: Int = -1
    override val maximumMember: Int = -1
}


@Serializable
public data class GuildApiRespSort(val id: Int)

public inline val GuildApiRespSort.isAsc: Boolean get() = id == 1
public inline val GuildApiRespSort.isDesc: Boolean get() = !isAsc



