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

package love.forte.simbot.kaiheila.api.invite

import love.forte.simbot.ID
import love.forte.simbot.kaiheila.objects.User

/**
 * 邀请信息.
 *
 * see [获取邀请列表](https://developer.kaiheila.cn/doc/http/invite#%E8%8E%B7%E5%8F%96%E9%82%80%E8%AF%B7%E5%88%97%E8%A1%A8).
 *
 * @see InviteListRequest
 *
 */
public interface InviteInfo {
    /**
     * 服务器id
     *
     */
    public val guildId: ID

    /**
     * 频道id
     *
     */
    public val channelId: ID

    /**
     * url code
     *
     */
    public val urlCode: String

    /**
     * 地址
     *
     */
    public val url: String

    /**
     * 用户
     *
     */
    public val user: User
}