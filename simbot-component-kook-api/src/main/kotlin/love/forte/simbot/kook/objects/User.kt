/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
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

package love.forte.simbot.kook.objects

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.LoggerFactory
import love.forte.simbot.LongID
import love.forte.simbot.definition.UserInfo
import love.forte.simbot.kook.api.ApiResultType
import org.slf4j.Logger


/**
 *
 *  Kook objects - [用户User](https://developer.kaiheila.cn/doc/objects#%E7%94%A8%E6%88%B7User)
 *
 *
 * 官方示例数据：
 * ```json
 * {
 *     "id": "2418200000",
 *     "username": "tz-un",
 *     "identify_num": "5618",
 *     "online": false,
 *     "avatar": "https://img.kook.cn/avatars/2020-02/xxxx.jpg/icon",
 *     "bot": false,
 *     "mobile_verified": true,
 *     "system": false,
 *     "mobile_prefix": "86",
 *     "mobile": "123****7890",
 *     "invited_count": 33,
 *     "nickname": "12316993",
 *     "roles": [
 *         111,
 *         112
 *     ]
 * }
 * ```
 *
 *
 * @author ForteScarlet
 */
public interface User : KookObjects, UserInfo {

    /** 用户的id */
    override val id: ID

    /** 用户名称 */
    override val username: String


    /**
     * 用户在当前服务器的昵称
     */
    public val nickname: String?


    /**
     * 用户名的认证数字，用户名正常为：user_name#identify_num
     */
    public val identifyNum: String

    /**
     * 当前是否在线
     */
    public val isOnline: Boolean

    /**
     * 用户是否为机器人
     */
    public val isBot: Boolean

    /**
     * 用户的状态, 0代表正常，10代表被封禁
     */
    public val status: Int

    /**
     * 用户的头像的url地址
     */
    override val avatar: String


    /**
     * vip用户的头像的url地址，可能为gif动图
     */
    public val vipAvatar: String?

    /**
     * 是否手机号已验证
     */
    public val mobileVerified: Boolean


    /**
     * 用户在当前服务器中的角色 id 组成的列表。
     */
    public val roles: List<ID>


}


/**
 * [User] 数据类实现。
 */
@Serializable
internal data class UserImpl(
    override val id: CharSequenceID,
    override val username: String,
    /**
     * 用户名的认证数字，用户名正常为：user_name#identify_num
     */
    @SerialName("identify_num")
    override val identifyNum: String,
    @SerialName("online")
    override val isOnline: Boolean,
    /**
     * 用户的状态, 0代表正常，10代表被封禁
     *
     * Allowed: 0|1|10
     */
    override val status: Int,
    @SerialName("bot")
    override val isBot: Boolean,
    override val avatar: String,

    // Maybe not exists.

    @SerialName("vip_avatar")
    override val vipAvatar: String? = null,
    override val nickname: String = "",
    override val mobileVerified: Boolean = false,
    override val roles: List<LongID> = emptyList(),
) : User {
    init {
        if (!(status == 0 || status == 1 || status == 10)) {
            logger.warn("Parameter 'status' must be 0、1(normal) or 10(banned), but {}", status)
            // check(status == 0 || status == 10) { "Parameter status must be 0(normal) or 10(banned), but $status" }
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger("love.forte.simbot.kook.objects.User")
    }
}

/**
 *
 * Mention part info.
 *
 * @see toUser
 */
@Suppress("MemberVisibilityCanBePrivate")
@Serializable
public data class MentionPart @ApiResultType constructor(
    val id: CharSequenceID,
    val username: String,
    @SerialName("full_name")
    val fullName: String,
    val avatar: String,
) {

    /**
     * 将这个 mention part 转化为 [User] 实例。
     */
    public fun toUser(): User = MentionPartUser(
        id = id,
        username = username,
        avatar = avatar,
    )
}


@Serializable
internal data class MentionPartUser(
    override val id: CharSequenceID,
    override val username: String,
    override val avatar: String,
) : User {
    override val nickname: String?
        get() = null
    override val identifyNum: String
        get() = ""
    override val isOnline: Boolean
        get() = false
    override val isBot: Boolean
        get() = false
    override val status: Int
        get() = 0
    override val vipAvatar: String?
        get() = null
    override val mobileVerified: Boolean
        get() = false
    override val roles: List<LongID>
        get() = emptyList()
}








