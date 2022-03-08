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

@file:Suppress("unused")

package love.forte.simbot.kaiheila.event

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import love.forte.simbot.kaiheila.api.*
import love.forte.simbot.kaiheila.event.KhlSignalReconnectException.Companion.reconnectException

public typealias Signal_0 = Signal.Event
public typealias Signal_1 = Signal.Hello
public typealias Signal_2 = Signal.Ping
public typealias Signal_3 = Signal.Pong
public typealias Signal_4 = Signal.Resume
public typealias Signal_5 = Signal.Reconnect
public typealias Signal_6 = Signal.ResumeAck


/**
 * 提供一个参数，得到一个json字符串的工厂。
 */
public interface JsonValueFactory<T> {
    public fun jsonValue(value: T): String
}


/**
 * 开黑啦 websocket概述 - [信令](https://developer.kaiheila.cn/doc/websocket#%E4%BF%A1%E4%BB%A4%E6%A0%BC%E5%BC%8F)
 *
 * 信令基本格式：
 * ```json
 * {
 *     "s" : 1,  // int, 信令，详情参考信令说明
 *     "d" : [], // 数据字段mixed
 *     "sn" : 0, // int, 该字段并不一定有，只在s=0时有，与webhook一致。
 * }
 * ```
 *
 */
@Serializable
public sealed class Signal<T> {

    /**
     * int, 信令，详情参考信令说明
     */
    public abstract val s: Int

    /**
     * 数据字段mixed
     */
    public abstract val d: T


    //region 信令[0] - EVENT
    /**
     * ## 信令0 - [EVENT](https://developer.kaiheila.cn/doc/websocket#%E4%BF%A1%E4%BB%A4[0]%20EVENT)
     *
     * 方向： server->client
     *  说明： 在正常连接状态下，收到的消息事件等。
     *  参数列表：
     *
     *  具体参见 [Event](https://developer.kaiheila.cn/doc/event)
     *
     *  注意： 该消息会有 `sn`, 代表消息序号, 针对当前 `session` 的消息的序号, 客户端需记录该数字,并按顺序接收消息， `resume` 时需传入该参数才能完成。
     *
     */
    @Serializable
    public data class Event(
        override val s: Int,
        override val d: JsonElement,
        public val sn: Int,
    ) : Signal<JsonElement>()
    //endregion


    //region 信令[1] - HELLO
    /**
     * ## 信令1 - [HELLO](https://developer.kaiheila.cn/doc/websocket#%E4%BF%A1%E4%BB%A4[1]%20HELLO)
     *
     * 方向： server->client
     *
     * 说明： 当我们成功连接websocket后，客户端应该在6s内收到该包，否则认为连接超时。
     *
     * 成功示例：
     * ```json
     * {
     *    "s": 1,
     *    "d": {
     *        "code": 0,
     *        "session_id": "xxxx"
     *    }
     * }
     *
     * ```
     * 失败：
     *
     * | 状态码 |   含义   | 备注 |
     * |-------|---------|-----|
     * | 40100 | 缺少参数 |  |
     * | 40101 | 无效的token |  |
     * | 40102 | token验证失败 |  |
     * | 40103 | token过期 | 需要重新连接 |
     *
     * 示例：
     * ```json
     * {
     *     "s": 1,
     *     "d": {
     *         "code": 40103
     *     }
     * }
     * ```
     *
     */
    @Serializable
    public data class Hello(override val d: HelloPack) : Signal<HelloPack>() {
        override val s: Int get() = 1
    }

    @Serializable
    public data class HelloPack(val code: Int, @SerialName("session_id") val sessionId: String?)
    //endregion


    //region 信令[2] - PING
    /**
     * 信令2 - [PING](https://developer.kaiheila.cn/doc/websocket#%E4%BF%A1%E4%BB%A4[2]%20PING)
     *
     * 方向： client -> server
     * 说明： 每隔30s(随机-5，+5),将当前的最大 sn 传给服务端,客户端应该在6s内收到PONG, 否则心跳超时。
     *
     * ```json
     * {
     *   "s": 2,
     *   "sn": 6
     * }
     * ```
     *
     */
    @Serializable
    public data class Ping(public val sn: Int) : Signal<Unit>() {
        override val s: Int get() = 2
        override val d: Unit get() = Unit

        public companion object : JsonValueFactory<Ping> {
            @JvmStatic
            override fun jsonValue(value: Ping): String = """{"s":2,"sn":${value.sn}}"""

        }
    }
    //endregion


    //region 信令[3] - PONG
    /**
     * ## 信令3 - [PONG](https://developer.kaiheila.cn/doc/websocket#%E4%BF%A1%E4%BB%A4[3]%20PONG)
     *
     * 方向： server -> client
     *
     * 说明： 回应客户端发出的ping
     *
     * 示例：
     *
     * ```json
     *  { "s": 3 }
     * ```
     */
    @Serializable
    public object Pong : Signal<Unit>() {
        override val s: Int get() = 3
        override val d: Unit get() = Unit
    }
    //endregion


    //region 信令[4] - Resume
    /**
     *
     * ## 信令[4] RESUME
     *
     * 当链接未断开时，客户端需传入当前收到的最后一个 sn 序号 例:
     *
     * ```json
     * {
     *   "s": 4,
     *   "sn": 100
     * }
     * ```
     */
    @Serializable
    public data class Resume(public val sn: Int) : Signal<Unit>() {
        override val s: Int get() = 4
        override val d: Unit get() = Unit

        public companion object : JsonValueFactory<Resume> {
            @JvmStatic
            override fun jsonValue(value: Resume): String = """{"s":4,"sn":${value.sn}}"""

        }
    }
    //endregion


    //region 信令[5] - RECONNECT
    /**
     * ## 信令5 - [RECONNECT](https://developer.kaiheila.cn/doc/websocket#%E4%BF%A1%E4%BB%A4[5]%20RECONNECT)
     *
     * 方向： server->client
     *
     * 说明： 服务端通知客户端, 代表该连接已失效, 请重新连接。客户端收到后应该主动断开当前连接。
     *
     * 注意： 客户端收到该信令代表因为某些原因导致当前连接已失效, 需要进行以下操作以避免消息丢失.
     *
     * 1. 重新获取 gateway;
     * 2. 清空本地的 sn 计数;
     * 3. 清空本地消息队列.
     *
     * 状态码	描述
     * - 40106	resume 失败, 缺少参数
     * - 40107	当前 session 已过期 (resume 失败, PING的sn无效)
     * - 40108	无效的 sn , 或 sn 已经不存在 (resume 失败, PING的 sn 无效)
     *
     * 示例：
     *  ```json
     *  {
     *      "s": 5
     *      "d": {
     *          "code": 41008,
     *          "err": "Missing params"
     *      }
     *  }
     *
     *  ```
     *
     */
    @Serializable
    public data class Reconnect(override val d: ReconnectPack) : Signal<ReconnectPack>() {
        override val s: Int get() = 5
    }


    @Serializable
    public data class ReconnectPack(val code: Int, val err: String? = null)

    /**
     * 响应值
     */
    public enum class ReconnectCode(public val code: Int, public val err: String) {
        RESUME_FAIL_MISS_PARAM(40106, "resume 失败, 缺少参数"),
        SESSION_EXPIRED(40107, "当前 session 已过期 (resume 失败, PING的sn无效)"),
        SN_INVALID_OR_NON_EXISTENT(40108, "无效的sn , 或 sn 已经不存在 (resume 失败, PING的 sn 无效)"),

        /** 其他未知 */
        UNKNOWN(-99999, "未知错误")
        ;

        public companion object {
            @JvmStatic
            public fun byCode(code: Int): ReconnectCode {
                return when (code) {
                    RESUME_FAIL_MISS_PARAM.code -> RESUME_FAIL_MISS_PARAM
                    SESSION_EXPIRED.code -> SESSION_EXPIRED
                    SN_INVALID_OR_NON_EXISTENT.code -> SN_INVALID_OR_NON_EXISTENT
                    else -> UNKNOWN
                }
            }
        }
    }

    //endregion


    //region 信令[6] - RESUME ACK
    /**
     *
     * ## 信令6 - [RESUME ACK](https://developer.kaiheila.cn/doc/websocket#%E4%BF%A1%E4%BB%A4[6]%20RESUME%20ACK)
     *
     * 方向： server->client
     *
     * 说明： 服务端通知客户端 resume 动作成功，中间所有离线消息已经全部发送成功
     *
     * 示例：
     * ```json
     * {
     *     "s": 6
     *     "d": {
     *         "session_id": "xxxx-xxxxxx-xxx-xxx"
     *     }
     * }
     * ```
     *
     */
    @Serializable
    public data class ResumeAck(override val s: Int, override val d: ResumeAckPack) : Signal<ResumeAckPack>()

    /**
     * [ResumeAck.d] 的数据体.
     */
    @Serializable
    public data class ResumeAckPack(@SerialName("session_id") val sessionId: String)


    //endregion


}


/**
 * 尝试获取一个事件的外层 type 属性字段，并转化为 type。
 *
 * @throws KhlSignalException 当出现预料之外的格式时。
 */
public inline val Signal_0.type: Event.Type
    get() {
        if (d !is JsonObject) throw KhlSignalException("Event json property 'd' is not a object type: $d")

        val type = d["type"]

        if (type !is JsonPrimitive) throw KhlSignalException("Event json property 'type' is not a primitive type: $type")

        return type.intOrNull?.let { Event.Type.byTypeOr(it, Event.Type.UNKNOWN) }
            ?: throw KhlSignalException("Unknown event type property: $type")
    }

/**
 * 尝试获取一个事件的内部 `extra` 的 `type` 属性字段(的 [JsonPrimitive] 类型 )。
 *
 * @throws KhlSignalException 当出现预料之外的格式时。
 *
 */
public inline val Signal_0.extraTypePrimitive: JsonPrimitive
    get() {
        if (d !is JsonObject) throw KhlSignalException("Event json property 'd' is not a object type: $d")

        val extra = d["extra"]

        if (extra !is JsonObject) throw KhlSignalException("Event json property 'extra' is not a object type: $extra")

        val type = extra["type"]

        if (type !is JsonPrimitive) throw KhlSignalException("Event json property 'type' in property 'extra' is not a primitive type.")

        return type

    }

/**
 * 尝试获取一个事件的内部 `extra` 的 `type` 属性字段。
 *
 * @throws KhlSignalException 当出现预料之外的格式时。
 *
 * @return 只可能是 [Event.Type] 或 [String] 类型。
 */
public inline val Signal_0.extraType: Any
    get() {
        val type = extraTypePrimitive
        val intValue = type.intOrNull
        if (intValue != null) {
            return Event.Type.byTypeOr(intValue) ?: Event.Type.UNKNOWN
        }

        return type.contentOrNull
            ?: throw KhlSignalException("Unknown type of event json property 'type' in property 'extra': $type")
    }


/**
 * 开黑啦信令异常。
 */
public open class KhlSignalException : KhlRuntimeException {
    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}


/**
 * 开黑啦 [信令5 - Reconnect][Signal.Reconnect] 异常。
 *
 * @see Signal.Reconnect
 * @see reconnectException
 */
public open class KhlSignalReconnectException : KhlSignalException {
    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)

    public companion object {
        @JvmStatic
        public fun reconnectException(resp: Signal.ReconnectPack): KhlSignalReconnectException {
            val code = resp.code
            val err = resp.err ?: Signal.ReconnectCode.byCode(code).err
            return KhlSignalReconnectException("code: $code, err: $err")
        }
    }
}
