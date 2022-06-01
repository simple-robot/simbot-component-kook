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

import love.forte.simbot.Api4J
import love.forte.simbot.ID
import love.forte.simbot.Timestamp
import love.forte.simbot.action.UnsupportedActionException
import love.forte.simbot.component.kaiheila.message.KaiheilaMessageCreatedReceipt
import love.forte.simbot.definition.GuildMember
import love.forte.simbot.definition.Role
import love.forte.simbot.definition.UserStatus
import love.forte.simbot.kaiheila.api.guild.GuildMuteType
import love.forte.simbot.kaiheila.objects.SystemUser
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.utils.item.Items
import love.forte.simbot.utils.item.Items.Companion.emptyItems
import love.forte.simbot.utils.runInBlocking
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import love.forte.simbot.kaiheila.objects.User as KhlUser


// TODO sendSupport

/**
 *
 * 开黑啦组件下，[频道服务器][KaiheilaGuild] 的成员信息.
 *
 * @author ForteScarlet
 */
public interface KaiheilaGuildMember :
    GuildMember, KaiheilaComponentDefinition<KhlUser> {
    override val bot: KaiheilaComponentBot
    override val id: ID
    
    /**
     * 此成员对应的源用户实例。
     */
    override val source: KhlUser
    
    // region mute api
    /**
     * 取消禁言。没有参数的 [unmute] 默认情况下，代表使用类型 `1`: 麦克风静音。
     *
     * @see love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest
     */
    @JvmSynthetic
    override suspend fun unmute(): Boolean = unmute(GuildMuteType.TYPE_MICROPHONE)
    
    /**
     * 取消禁言。[type] 代表 [love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest] 的参数 `type`. 默认使用 `1`.
     *
     * @see love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest
     */
    @JvmSynthetic
    public suspend fun unmute(type: Int): Boolean
    
    
    /**
     * 对此用户进行静音操作。
     * 默认情况下，[mute] 代表使用类型 `1`: 麦克风静音。
     *
     * @see love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest
     */
    @JvmSynthetic
    override suspend fun mute(duration: Duration): Boolean = mute(duration, GuildMuteType.TYPE_MICROPHONE)
    
    /**
     * 对此用户进行静音操作。
     *
     * @see love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest
     */
    @JvmSynthetic
    public suspend fun mute(duration: Duration, type: Int): Boolean
    
    
    /**
     * 对此用户进行静音操作。
     * 默认情况下，[mute] 代表使用类型 `1`: 麦克风静音。
     *
     * @see love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest
     */
    @Api4J
    override fun muteBlocking(duration: Long, unit: TimeUnit): Boolean =
        runInBlocking { mute(unit.toMillis(duration).milliseconds) }
    
    /**
     * 对此用户进行静音操作。
     *
     * @see love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest
     */
    @Api4J
    public fun muteBlocking(time: Long, unit: TimeUnit, type: Int): Boolean =
        runInBlocking { mute(unit.toMillis(time).milliseconds, type) }
    
    /**
     * 取消禁言。没有参数的 [unmute] 默认情况下，代表使用类型 `1`: 麦克风静音。
     *
     * @see love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest
     */
    @Api4J
    override fun unmuteBlocking(): Boolean = runInBlocking { unmute() }
    
    
    /**
     * 取消禁言。[type] 代表 [love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest] 的参数 `type`. 默认使用 `1`.
     *
     * @see love.forte.simbot.kaiheila.api.guild.GuildMuteCreateRequest
     */
    @Api4J
    public fun unmuteBlocking(type: Int): Boolean = runInBlocking { unmute(type) }
    
    // endregion
    
    /**
     * 向当前频道对象发起一个新的聊天会话（私聊）并发送消息。如果当前类型为 [KaiheilaGuildSystemMember],
     * 则会抛出 [UnsupportedActionException] 异常。
     *
     * @throws UnsupportedActionException 如果目标不支持
     *
     */
    @JvmSynthetic
    override suspend fun send(text: String): KaiheilaMessageCreatedReceipt
    
    /**
     * 向当前频道对象发起一个新的聊天会话（私聊）并发送消息。如果当前类型为 [KaiheilaGuildSystemMember],
     * 则会抛出 [UnsupportedActionException] 异常。
     *
     * @throws UnsupportedActionException 如果目标不支持
     *
     */
    @JvmSynthetic
    override suspend fun send(message: Message): KaiheilaMessageCreatedReceipt
    
    /**
     * 向当前频道对象发起一个新的聊天会话（私聊）并发送消息。如果当前类型为 [KaiheilaGuildSystemMember],
     * 则会抛出 [UnsupportedActionException] 异常。
     *
     * @throws UnsupportedActionException 如果目标不支持
     *
     */
    @JvmSynthetic
    override suspend fun send(message: MessageContent): KaiheilaMessageCreatedReceipt
    
    /**
     * 阻塞的向当前频道对象发起一个新的聊天会话（私聊）并发送消息。如果当前类型为 [KaiheilaGuildSystemMember],
     * 则会抛出 [UnsupportedActionException] 异常。
     *
     * @throws UnsupportedActionException 如果目标不支持
     *
     */
    @Api4J
    override fun sendBlocking(text: String): KaiheilaMessageCreatedReceipt
    
    /**
     * 阻塞的向当前频道对象发起一个新的聊天会话（私聊）并发送消息。如果当前类型为 [KaiheilaGuildSystemMember],
     * 则会抛出 [UnsupportedActionException] 异常。
     *
     * @throws UnsupportedActionException 如果目标不支持
     *
     */
    @Api4J
    override fun sendBlocking(message: Message): KaiheilaMessageCreatedReceipt
    
    /**
     * 阻塞的向当前频道对象发起一个新的聊天会话（私聊）并发送消息。如果当前类型为 [KaiheilaGuildSystemMember],
     * 则会抛出 [UnsupportedActionException] 异常。
     *
     * @throws UnsupportedActionException 如果目标不支持
     *
     */
    @Api4J
    override fun sendBlocking(message: MessageContent): KaiheilaMessageCreatedReceipt
    
    @OptIn(Api4J::class)
    override val guild: KaiheilaGuild
    
    @OptIn(Api4J::class)
    override val organization: KaiheilaGuild
        get() = guild
    
    /**
     * 得到当前成员所在频道服务器。同 [guild].
     */
    @JvmSynthetic
    override suspend fun organization(): KaiheilaGuild = guild
    
    /**
     * 得到当前成员所在频道服务器。同 [guild].
     */
    @JvmSynthetic
    override suspend fun guild(): KaiheilaGuild = guild
    
    
    /**
     * 获取此成员所拥有的所有角色。
     *
     * Deprecated: 尚未实现。
     */
    @Deprecated(
        "Not support yet.",
        ReplaceWith("emptyItems()", "love.forte.simbot.utils.item.Items.Companion.emptyItems")
    )
    override val roles: Items<Role>
        get() = emptyItems()
    
    
    override val joinTime: Timestamp get() = Timestamp.notSupport()
    override val nickname: String
    override val avatar: String
    override val status: UserStatus
    override val username: String
    
    public companion object
    
}


/**
 * 使用 [SystemUser] 作为基础用户对象来作为一个频道内的用户。
 *
 * @see SystemUser
 */
public class KaiheilaGuildSystemMember(
    override val bot: KaiheilaComponentBot,
    override val guild: KaiheilaGuild,
) : KaiheilaGuildMember {
    override val source: SystemUser
        get() = SystemUser
    
    override val id: ID
        get() = source.id
    
    /**
     * 系统用户不支持禁言相关操作，永远得到 `false`.
     */
    @JvmSynthetic
    override suspend fun unmute(type: Int): Boolean = false
    
    /**
     * 系统用户不支持禁言相关操作，永远得到 `false`.
     */
    @JvmSynthetic
    override suspend fun mute(duration: Duration, type: Int): Boolean = false
    
    
    // region send 相关
    // 无法向系统用户发送消息
    private fun notSupport(): Nothing {
        throw UnsupportedActionException("Send message to system user (bot [$bot] in guild [${guild}]) ")
    }
    
    @JvmSynthetic
    override suspend fun send(text: String): KaiheilaMessageCreatedReceipt {
        notSupport()
    }
    
    @JvmSynthetic
    override suspend fun send(message: Message): KaiheilaMessageCreatedReceipt {
        notSupport()
    }
    
    @JvmSynthetic
    override suspend fun send(message: MessageContent): KaiheilaMessageCreatedReceipt {
        notSupport()
    }
    
    @OptIn(Api4J::class)
    override fun sendBlocking(text: String): KaiheilaMessageCreatedReceipt {
        notSupport()
    }
    
    @OptIn(Api4J::class)
    override fun sendBlocking(message: Message): KaiheilaMessageCreatedReceipt {
        notSupport()
    }
    
    @OptIn(Api4J::class)
    override fun sendBlocking(message: MessageContent): KaiheilaMessageCreatedReceipt {
        notSupport()
    }
    // endregion
    
    override val username: String
        get() = source.username
    override val nickname: String
        get() = source.nickname
    override val avatar: String
        get() = source.avatar
    override val status: UserStatus
        get() = STATUS
    
    public companion object {
        private val STATUS = UserStatus.builder().fakeUser().official().build()
    }
}