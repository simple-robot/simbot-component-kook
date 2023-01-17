package love.forte.simbot.kook.api

import io.ktor.client.*
import love.forte.simbot.util.api.requestor.Requestor


/**
 * 用于提供给 [KookApiRequest] 作为请求器使用。
 * @author ForteScarlet
 */
public interface KookApiRequestor : Requestor {
    /**
     * 用于进行请求的 [HttpClient]
     */
    public val client: HttpClient
    
    /**
     * 使用的鉴权值。
     * 注意，这里是完整的 `Authorization` 请求头中应当存在的内容，
     * 例如 `Bot aaaabbbbccccdddd`
     */
    public val authorization: String
}
