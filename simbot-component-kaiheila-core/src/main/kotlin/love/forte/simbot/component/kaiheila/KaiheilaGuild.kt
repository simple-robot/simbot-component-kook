/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kaiheila 的一部分。
 *
 *  simbot-component-kaiheila 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kaiheila 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

package love.forte.simbot.component.kaiheila

import kotlinx.coroutines.flow.Flow
import love.forte.simbot.Api4J
import love.forte.simbot.ID
import love.forte.simbot.Limiter
import love.forte.simbot.definition.*
import java.util.concurrent.TimeUnit
import java.util.stream.Stream
import kotlin.time.Duration
import love.forte.simbot.kaiheila.objects.Guild as KhlGuild

/**
 *
 * 开黑啦组件中的频道服务器信息。
 *
 * @author ForteScarlet
 */
public interface KaiheilaGuild : Guild, KaiheilaComponentDefinition<KhlGuild> {

    /**
     * 得到当前频道服务器所对应的api模块下的服务器对象。
     */
    override val source: KhlGuild

    override val bot: KaiheilaComponentGuildMemberBot

    override val currentMember: Int
    override val maximumMember: Int

    override val description: String
    override val icon: String
    override val id: ID
    override val name: String

    override val currentChannel: Int
    override val maximumChannel: Int

    //region owner api
    override val ownerId: ID

    @OptIn(Api4J::class)
    override val owner: KaiheilaGuildMember
    
    @JvmSynthetic
    override suspend fun owner(): KaiheilaGuildMember
    //endregion


    //region member api
    /**
     * 根据指定ID查询对应用户信息，或得到null。
     */
    @JvmSynthetic
    override suspend fun member(id: ID): KaiheilaGuildMember?

    /**
     * 根据指定ID查询对应用户信息，或得到null。
     */
    override fun getMember(id: ID): KaiheilaGuildMember?


    /**
     * 查询用户列表。
     */
    @JvmSynthetic
    override suspend fun members(groupingId: ID?, limiter: Limiter): Flow<KaiheilaGuildMember>

    /**
     * 查询用户列表。
     */
    @OptIn(Api4J::class)
    override fun getMembers(groupingId: ID?, limiter: Limiter): Stream<out KaiheilaGuildMember>

    /**
     * 查询用户列表。
     */
    @OptIn(Api4J::class)
    override fun getMembers(): Stream<out KaiheilaGuildMember> = getMembers(null, Limiter)

    /**
     * 查询用户列表。
     */
    @OptIn(Api4J::class)
    override fun getMembers(groupingId: ID?): Stream<out KaiheilaGuildMember> = getMembers(groupingId, Limiter)

    /**
     * 查询用户列表。
     */
    @OptIn(Api4J::class)
    override fun getMembers(limiter: Limiter): Stream<out KaiheilaGuildMember> = getMembers(null, limiter)
    //endregion


    //region children api
    
    @JvmSynthetic
    override suspend fun children(groupingId: ID?, limiter: Limiter): Flow<KaiheilaChannel>
    @OptIn(Api4J::class)
    override fun getChildren(groupingId: ID?, limiter: Limiter): Stream<out KaiheilaChannel>
    
    
    @JvmSynthetic
    override suspend fun children(groupingId: ID?): Flow<KaiheilaChannel> = children(groupingId, Limiter)
    @OptIn(Api4J::class)
    override fun getChildren(): Stream<out KaiheilaChannel> = getChildren(null, Limiter)

    @OptIn(Api4J::class)
    override fun getChildren(groupingId: ID?): Stream<out KaiheilaChannel> = getChildren(groupingId, Limiter)

    //endregion

    //region role api
    // TODO
    @JvmSynthetic
    override suspend fun roles(groupingId: ID?, limiter: Limiter): Flow<Role>

    @Api4J
    override fun getRoles(groupingId: ID?, limiter: Limiter): Stream<out Role>

    //endregion

    //region mute api

    @Deprecated("Guild mute is not supported", ReplaceWith("false"))
    override suspend fun mute(duration: Duration): Boolean = false

    @OptIn(Api4J::class)
    @Deprecated("Guild mute is not supported", ReplaceWith("false"))
    override fun muteBlocking(duration: Long, unit: TimeUnit): Boolean = false

    @Deprecated("Guild mute is not supported", ReplaceWith("false"))
    override suspend fun unmute(): Boolean = false

    @OptIn(Api4J::class)
    @Deprecated("Guild mute is not supported", ReplaceWith("false"))
    override fun unmuteBlocking(): Boolean = false

    //endregion


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