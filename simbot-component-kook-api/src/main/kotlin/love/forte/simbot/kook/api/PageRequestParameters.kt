/*
 *  Copyright (c) 2023-2023 ForteScarlet <ForteScarlet@163.com>
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

import io.ktor.http.*

/**
 * 用于KookAPI中分页查询的参数。
 *
 * 其中的属性如果小于0则视为无效。
 *
 */
public class PageRequestParameters(
    public var page: Int = -1,
    public var pageSize: Int = -1,
) {
    public constructor() : this(-1, -1)

    override fun toString(): String {
        return "PageRequestParameters(page = $page, pageSize=$pageSize)"
    }

    public companion object {
        /**
         * 常规情况下 page_size 最大为 50
         */
        public const val DEFAULT_MAX_PAGE_SIZE: Int = 50

        /**
         * 起始页码
         */
        public const val START_PAGE: Int = 1
    }
}

public fun PageRequestParameters.appendTo(builder: ParametersBuilder) {
    if (page > 0) {
        builder.append("page", page.toString())
    }
    if (pageSize > 0) {
        builder.append("page_size", pageSize.toString())
    }
}

