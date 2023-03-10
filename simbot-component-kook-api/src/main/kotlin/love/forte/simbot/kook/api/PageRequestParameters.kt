/*
 * Copyright (c) 2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook,
 * If not, see <https://www.gnu.org/licenses/>.
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

