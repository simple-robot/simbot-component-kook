package love.forte.simbot.kaiheila.api

import kotlinx.serialization.*
import love.forte.simbot.kaiheila.api.KaiheilaApiResult.List.Data


/**
 *
 * 开黑啦Api的响应值标准格式。
 *
 * 参考 <https://developer.kaiheila.cn/doc/reference#%E5%B8%B8%E8%A7%84%20http%20%E6%8E%A5%E5%8F%A3%E8%A7%84%E8%8C%83>.
 *
 * 响应值类型无外乎三种形式：[列表][KaiheilaApiResult.List]、[对象][KaiheilaApiResult.Obj]、[空][KaiheilaApiResult.Empty]。
 *
 * @author ForteScarlet
 */
@SerialName("khl.result")
@Serializable
public sealed class KaiheilaApiResult<T> {
    public companion object {
        /**
         * 代表成功的错误码。
         */
        public const val SUCCESS_CODE: Int = 0
    }

    /**
     * 错误码，0代表成功，非0代表失败。
     */
    public abstract val code: Int

    /**
     * 错误消息，具体的返回消息会根据Accept-Language来返回。
     */
    public abstract val message: String

    /**
     * 具体的数据。
     */
    public abstract val data: T

    /**
     * [data] 为对象格式的响应值。
     */
    @SerialName("khl.result.obj")
    @Serializable
    public data class Obj<T>(
        override val code: Int,
        override val message: String,
        override val data: T
    ) : KaiheilaApiResult<T>()

    /**
     * [data] 为列表格式的响应值。
     */
    @SerialName("khl.result.list")
    @Serializable
    public data class List<T>(
        override val code: Int,
        override val message: String,
        /**
         * 列表返回数据。其中，[Data.items] 代表真正的列表元素。
         */
        override val data: Data<T>
    ) : KaiheilaApiResult<Data<T>>() {

        /**
         * 作为列表返回数据类型时候的 [KaiheilaApiResult.data] 数据格式。
         *
         * @param IT 数据元素的类型。
         */
        @Serializable
        public data class Data<IT>(
            public val items: kotlin.collections.List<IT> = emptyList(),
            public val meta: Meta,
            public val sort: Map<String, Int> = emptyMap()
        ) : Iterable<IT> by items

        /**
         * 当返回值为列表时的分页响应元数据。
         */
        @Serializable
        public data class Meta(
            val page: Int,
            @SerialName("page_total")
            val pageTotal: Int,
            @SerialName("page_size")
            val pageSize: Int,
            val total: Int,
        )
    }

    /**
     * 结果为空的响应值。
     */
    @SerialName("khl.result.empty")
    @Serializable
    public data class Empty(override val code: Int, override val message: String) : KaiheilaApiResult<Unit>() {
        override val data: Unit get() = Unit
    }


}


/**
 * 判断 [KaiheilaApiResult] 的错误码是否代表成功。
 */
public inline val KaiheilaApiResult<*>.isSuccess: Boolean get() = code == KaiheilaApiResult.SUCCESS_CODE