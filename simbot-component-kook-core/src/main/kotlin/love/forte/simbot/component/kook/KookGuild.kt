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

package love.forte.simbot.component.kook

import love.forte.simbot.Api4J
import love.forte.simbot.ID
import love.forte.simbot.JavaDuration
import love.forte.simbot.definition.*
import love.forte.simbot.utils.item.Items
import love.forte.simbot.utils.item.Items.Companion.emptyItems
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import love.forte.simbot.kook.objects.Guild as KkGuild

/**
 *
 * Kook 组件中的频道服务器信息。
 *
 * @author ForteScarlet
 */
public interface KookGuild : Guild, KookComponentDefinition<KkGuild> {
    
    /**
     * 得到当前频道服务器所对应的api模块下的服务器对象。
     */
    override val source: KkGuild
    
    override val bot: KookComponentGuildBot
    
    override val currentMember: Int
    override val maximumMember: Int
    
    override val description: String
    override val icon: String
    override val id: ID
    override val name: String
    
    override val currentChannel: Int
    override val maximumChannel: Int
    
    // region owner api
    override val ownerId: ID
    
    @OptIn(Api4J::class)
    override val owner: KookGuildMember
    
    @JvmSynthetic
    override suspend fun owner(): KookGuildMember
    // endregion
    
    
    /**
     * 根据指定ID查询对应用户信息，或得到null。
     */
    @JvmSynthetic
    override suspend fun member(id: ID): KookGuildMember?
    
    /**
     * 根据指定ID查询对应用户信息，或得到null。
     */
    @OptIn(Api4J::class)
    override fun getMember(id: ID): KookGuildMember?
    
    /**
     * 查询用户列表。
     */
    override val members: Items<KookGuildMember>
    
    /**
     * 直接获取用户列表的副本。
     */
    public val memberList: List<KookGuildMember>
    
    /**
     * 获取当前频道服务器下的子频道序列。
     */
    override val children: Items<KookChannel>
    
    /**
     * 直接获取当前频道服务器下的子频道列表的副本。
     */
    public val channelList: List<KookChannel>
    
    /**
     * 获取当前频道服务器中所有分类频道的列表副本。
     */
    public val categories: List<KookChannelCategory>
    
    /**
     * 得到此频道服务器中代表“顶层”分类的分类对象。
     *
     * @see RootKookChannelCategory
     */
    public val rootCategory: RootKookChannelCategory
    
    /**
     * 获取一个指定id的分类类型。
     */
    public fun getCategory(id: ID): KookChannelCategory?
    
    // region role api
    /**
     * 获取当前频道服务器中配置的所有角色信息。
     *
     * Deprecated: 尚未支持
     */
    @Deprecated(
        "Not support yet.",
        ReplaceWith("emptyItems()", "love.forte.simbot.utils.item.Items.Companion.emptyItems")
    )
    override val roles: Items<Role>
        get() = emptyItems()
    
    // endregion
    
    // region mute api
    
    @Deprecated("Guild mute is not supported", ReplaceWith("false"))
    override suspend fun mute(duration: Duration): Boolean = false
    
    @OptIn(Api4J::class)
    @Deprecated("Guild mute is not supported", ReplaceWith("false"))
    override fun muteBlocking(time: Long, timeUnit: TimeUnit): Boolean = false
    
    @Deprecated("Guild mute is not supported", ReplaceWith("false"))
    override suspend fun unmute(): Boolean = false
    
    @OptIn(Api4J::class)
    @Deprecated("Guild mute is not supported", ReplaceWith("false"))
    override fun unmuteBlocking(): Boolean = false
    
    @OptIn(Api4J::class)
    @Deprecated("Guild mute is not supported", ReplaceWith("false"))
    override fun muteBlocking(): Boolean = false
    
    @OptIn(Api4J::class)
    @Deprecated("Guild mute is not supported", ReplaceWith("false"))
    override fun muteBlocking(duration: JavaDuration): Boolean = false
    
    // endregion
    
    
    /**
     * 频道服务器没有上层。
     */
    @JvmSynthetic
    override suspend fun previous(): Organization? = null
    
    /**
     * 频道服务器没有上层。
     */
    @OptIn(Api4J::class)
    override val previous: Organization?
        get() = null
}