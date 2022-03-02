package love.forte.simbot.kaiheila.api

import love.forte.simbot.*


/**
 * 与 开黑啦api 相关的异常。
 * @author ForteScarlet
 */
@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
public open class KaiheilaApiException(public val code: Int, public val msg: String?, cause: Throwable? = null) :
    SimbotIllegalStateException("code: $code, message: $msg", cause)