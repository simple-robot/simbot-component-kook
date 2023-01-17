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

import io.ktor.client.statement.*
import io.ktor.http.*


// TODO
/**
 * Kook  [超速限制](https://developer.kaiheila.cn/doc/rate-limit) 异常。
 *
 * @author ForteScarlet
 */
public open class ApiRateLimitException : KookRuntimeException {
    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

/**
 * [超速限制](https://developer.kaiheila.cn/doc/rate-limit) 中的部分常量信息。
 *
 * @author ForteScarlet
 */
public object ApiRateLimits {

    /**
     * 一段时间内允许的最大请求次数的请求头。
     */
    public const val RATE_LIMIT_LIMIT_HEAD: String = "X-Rate-Limit-Limit"

    /**
     * 一段时间内还剩下的请求数的请求头。
     */
    public const val RATE_LIMIT_REMAINING_HEAD: String = "X-Rate-Limit-Remaining"

    /**
     *回复到最大请求次数需要等待的时间的请求头。
     */
    public const val RATE_LIMIT_RESET_HEAD: String = "X-Rate-Limit-Reset"

    /**
     * 请求数的bucket的请求头。
     */
    public const val RATE_LIMIT_BUCKET_HEAD: String = "X-Rate-Limit-Bucket"

    /**
     * 触犯全局请求次数限制的请求头。
     */
    public const val RATE_LIMIT_GLOBAL_HEAD: String = "X-Rate-Limit-Global"


}

/**
 * 尝试获取 [HttpResponse] 中的 [ApiRateLimits.RATE_LIMIT_LIMIT_HEAD] 信息。
 */
public inline val HttpResponse.rateLimit: Long? get() = headers.rateLimit

/**
 * 尝试获取 [HttpResponse] 中的 [ApiRateLimits.RATE_LIMIT_REMAINING_HEAD] 信息。
 */
public inline val HttpResponse.rateRemaining: Long? get() = headers.rateRemaining

/**
 * 尝试获取 [HttpResponse] 中的 [ApiRateLimits.RATE_LIMIT_RESET_HEAD] 信息。
 */
public inline val HttpResponse.rateReset: Long? get() = headers.rateReset

/**
 * 尝试获取 [HttpResponse] 中的 [ApiRateLimits.RATE_LIMIT_BUCKET_HEAD] 信息。
 */
public inline val HttpResponse.rateBucket: String? get() = headers.rateBucket

/**
 * [HttpResponse] 中是否存在 [ApiRateLimits.RATE_LIMIT_GLOBAL_HEAD]。
 */
public inline val HttpResponse.isRateGlobal: Boolean get() = headers.isRateGlobal



/**
 * 尝试获取 [Headers] 中的 [ApiRateLimits.RATE_LIMIT_LIMIT_HEAD] 信息。
 */
public inline val Headers.rateLimit: Long? get() = this[ApiRateLimits.RATE_LIMIT_LIMIT_HEAD]?.toLongOrNull()

/**
 * 尝试获取 [Headers] 中的 [ApiRateLimits.RATE_LIMIT_REMAINING_HEAD] 信息。
 */
public inline val Headers.rateRemaining: Long? get() = this[ApiRateLimits.RATE_LIMIT_REMAINING_HEAD]?.toLongOrNull()

/**
 * 尝试获取 [Headers] 中的 [ApiRateLimits.RATE_LIMIT_RESET_HEAD] 信息。
 */
public inline val Headers.rateReset: Long? get() = this[ApiRateLimits.RATE_LIMIT_RESET_HEAD]?.toLongOrNull()

/**
 * 尝试获取 [Headers] 中的 [ApiRateLimits.RATE_LIMIT_BUCKET_HEAD] 信息。
 */
public inline val Headers.rateBucket: String? get() = this[ApiRateLimits.RATE_LIMIT_BUCKET_HEAD]

/**
 * [Headers] 中是否存在 [ApiRateLimits.RATE_LIMIT_GLOBAL_HEAD]。
 */
public inline val Headers.isRateGlobal: Boolean get() = this[ApiRateLimits.RATE_LIMIT_GLOBAL_HEAD] != null



