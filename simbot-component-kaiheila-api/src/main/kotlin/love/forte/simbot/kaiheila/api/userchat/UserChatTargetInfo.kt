package love.forte.simbot.kaiheila.api.userchat

import love.forte.simbot.*
import love.forte.simbot.definition.*
import love.forte.simbot.kaiheila.api.*

/**
 *
 * [UserChatView] 中的 `targetInfo` 属性。
 *
 */
@kotlinx.serialization.Serializable
public data class UserChatTargetInfo @ApiResultType constructor(
    override val id: CharSequenceID,
    override val username: String,
    override val avatar: String,
    val online: Boolean = false,
) : UserInfo