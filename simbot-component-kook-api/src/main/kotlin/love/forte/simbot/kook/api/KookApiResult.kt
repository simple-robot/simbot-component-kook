/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kook.api

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement


/**
 *
 *  Kook Api的响应值标准格式。
 *
 * 参考 <https://developer.kaiheila.cn/doc/reference#%E5%B8%B8%E8%A7%84%20http%20%E6%8E%A5%E5%8F%A3%E8%A7%84%E8%8C%83>.
 *
 * 响应值类型无外乎三种形式：列表、对象、空。
 *
 * ```json
 *{
 *    "code" : 0, // integer, 错误码，0代表成功，非0代表失败，具体的错误码参见错误码一览
 *    "message" : "error info", // string, 错误消息，具体的返回消息会根据Accept-Language来返回。
 *    "data" : [], // mixed, 具体的数据。
 *}
 * ```
 *
 *
 * 当返回数据为对象时，使用 [KookApiRequest] 的预期结果即为目标结果。
 *
 * 当返回数据为空时，使用 [KookApiRequest] 的预期结果考虑使用不变常量，例如 [Unit].
 *
 * 当返回数据为列表时，使用 [KookApiRequest] 的预期结果考虑使用 [KookApiResult.ListData].
 *
 * @author ForteScarlet
 */
public object KookApiResult {
    /**
     * 代表成功的 '错误码'。
     */
    public const val SUCCESS_CODE: Int = 0

    /**
     * 响应体中作为 `code` 的属性名, Int类型。
     *
     * 错误码，0代表成功，非0代表失败。
     *
     * @see SUCCESS_CODE
     */
    public const val CODE_PROPERTY_NAME: String = "code"

    /**
     * 响应体中作为 `message` 的属性名。
     *
     * 错误消息，具体的返回消息会根据Accept-Language来返回。
     */
    public const val MESSAGE_PROPERTY_NAME: String = "message"


    /**
     * 响应体中作为 `data` 的属性名。
     */
    public const val DATA_PROPERTY_NAME: String = "data"


    /**
     * 当响应体中的 `data` 为列表类型时，其有特殊的数据格式:
     * ```json
     * {
     *      "items": [...],
     *      "meta": {
     *          "page": 1,
     *          "page_total": 10,
     *          "page_size": 50,
     *          "total": 480
     *      },
     *      "sort": { "id": 2 }
     * }
     * ```
     *
     * @see ListData
     *
     */
    public abstract class ListDataResponse<out T, SORT> {
        public abstract val items: List<T>
        public abstract val meta: ListMeta
        public abstract val sort: SORT
    }


    /**
     * 当返回值为列表时的分页响应元数据。
     */
    @Serializable
    public data class ListMeta(
        val page: Int,
        @SerialName("page_total")
        val pageTotal: Int,
        @SerialName("page_size")
        val pageSize: Int,
        val total: Int,
    )

    /**
     *
     * [ListData] 将 sort 类型直接视为 Map 进行解析。
     *
     * @param T 数据元素的类型。
     */
    @Serializable
    public class ListData<out T>(
        override val items: List<T> = emptyList(),
        override val meta: ListMeta,
        override val sort: Map<String, Int> = emptyMap()
    ) : ListDataResponse<T, Map<String, Int>>(), Iterable<T> by items {
        override fun toString(): String {
            return "ListData(items=$items, meta=$meta, sort=$sort)"
        }
    }


}

/**
 * 对 Kook Api标准响应数据的封装。
 */
@Suppress("MemberVisibilityCanBePrivate")
@Serializable
public class ApiResult @ApiResultType constructor(
    public val code: Int,
    public val message: String,
    public val data: JsonElement
) {

    /**
     * 当前api响应值的 [速率限制][RateLimit] 信息。会在当前类实例化之后在进行初始化。
     */
    @Transient
    public var rateLimit: RateLimit = RateLimit.DEFAULT

    /**
     * 此接口的响应码是否为成功的响应码.
     */
    public val isSuccess: Boolean get() = code == KookApiResult.SUCCESS_CODE

    /**
     * 提供解析参数来使用当前result中的data内容解析为目标结果。
     * 不会有任何判断，
     *
     * @throws SerializationException see [Json.decodeFromJsonElement].
     */
    public fun <T> parseData(json: Json, deserializationStrategy: DeserializationStrategy<out T>): T {
        return json.decodeFromJsonElement(deserializationStrategy, data)
    }

    /**
     * 当 [code] 为成功的时候解析 data 数据, 如果 [code] 不为成功([KookApiResult.SUCCESS_CODE]), 则抛出 [KookApiException] 异常。
     *
     * @throws KookApiException 如果 [code] 不为成功
     * @throws SerializationException see [Json.decodeFromJsonElement].
     */
    public fun <T> parseDataOrThrow(json: Json, deserializationStrategy: DeserializationStrategy<out T>): T {
        if (!isSuccess) {
            throw KookApiException(code, "$message, api=$this")
        }
        return parseData(json, deserializationStrategy)
    }


    override fun toString(): String = "ApiResult(code=$code, message=$message, rateLimit=$rateLimit, data=$data)"


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ApiResult) return false

        if (code != other.code) return false
        if (message != other.message) return false
        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code
        result = 31 * result + message.hashCode()
        result = 31 * result + data.hashCode()
        return result
    }


}


/**
 * [速率限制](https://developer.kaiheila.cn/doc/rate-limit) 请求头中的数据体。
 */
@Serializable
public data class RateLimit(
    /**
     * `X-Rate-Limit-Limit`, 一段时间内允许的最大请求次数
     */
    public val limit: Long,

    /**
     * `X-Rate-Limit-Remaining`, 一段时间内还剩下的请求数
     */
    public val remaining: Long,

    /**
     * `X-Rate-Limit-Reset`, 回复到最大请求次数需要等待的时间
     */
    public val reset: Long,

    /**
     * `X-Rate-Limit-Bucket`, 请求数的bucket
     */
    public val bucket: String,

    /**
     * `X-Rate-Limit-Global`, 触犯全局请求次数限制
     */
    public val isGlobalLimit: Boolean,

    ) {
    public companion object {
        public const val X_RATE_LIMIT_LIMIT: String = "X-Rate-Limit-Limit"
        public const val X_RATE_LIMIT_REMAINING: String = "X-Rate-Limit-Remaining"
        public const val X_RATE_LIMIT_RESET: String = "X-Rate-Limit-Reset"
        public const val X_RATE_LIMIT_BUCKET: String = "X-Rate-Limit-Bucket"
        public const val X_RATE_LIMIT_GLOBAL: String = "X-Rate-Limit-Global"

        public val DEFAULT: RateLimit = RateLimit(99999, 99999, 0, "default/not-init", false)
    }
}