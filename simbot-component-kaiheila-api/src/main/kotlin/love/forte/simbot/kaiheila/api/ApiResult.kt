package love.forte.simbot.kaiheila.api

import kotlinx.serialization.*
import kotlinx.serialization.json.*


/**
 *
 * 开黑啦Api的响应值标准格式。
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
 * 当返回数据为对象时，使用 [KaiheilaApiRequest] 的预期结果即为目标结果。
 *
 * 当返回数据为空时，使用 [KaiheilaApiRequest] 的预期结果考虑使用不变常量，例如 [Unit].
 *
 * 当返回数据为列表时，使用 [KaiheilaApiRequest] 的预期结果考虑使用 [KaiheilaApiResult.ListData].
 *
 * @author ForteScarlet
 */
public object KaiheilaApiResult {
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
     *@param T 数据元素的类型。
     */
    @Serializable
    public data class ListData<T>(
        public val items: List<T> = emptyList(),
        public val meta: ListMeta,
        public val sort: Map<String, Int> = emptyMap()
    ) : Iterable<T> by items

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

}

/**
 * 对开黑啦Api标准响应数据的封装。
 */
@Suppress("MemberVisibilityCanBePrivate")
@Serializable
public class ApiResult(
    public val code: Int,
    public val message: String,
    public val data: JsonElement
) {

    public val isSuccess: Boolean get() = code == KaiheilaApiResult.SUCCESS_CODE

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
     * 当 [code] 为成功的时候解析 data 数据, 如果 [code] 不为成功([KaiheilaApiResult.SUCCESS_CODE]), 则抛出 [KaiheilaApiException] 异常。
     *
     * @throws KaiheilaApiException 如果 [code] 不为成功
     * @throws SerializationException see [Json.decodeFromJsonElement].
     */
    public fun <T> parseDataOrThrow(json: Json, deserializationStrategy: DeserializationStrategy<out T>): T {
        if (!isSuccess) {
            throw KaiheilaApiException(code, message)
        }
        return parseData(json, deserializationStrategy)
    }


    override fun toString(): String = "ApiResult(code=$code, message=$message, data=$data)"


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

