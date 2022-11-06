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
    public constructor(): this(-1, -1)
    
    override fun toString(): String {
        return "PageRequestParameters(page = $page, pageSize=$pageSize)"
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