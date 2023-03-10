/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.component.kook

import love.forte.plugin.suspendtrans.annotation.JvmAsync
import love.forte.plugin.suspendtrans.annotation.JvmBlocking
import love.forte.simbot.*
import love.forte.simbot.action.UnsupportedActionException
import love.forte.simbot.component.kook.message.KookMessageCreatedReceipt
import love.forte.simbot.component.kook.message.KookMessageReceipt
import love.forte.simbot.component.kook.role.KookMemberRole
import love.forte.simbot.definition.GuildMember
import love.forte.simbot.kook.api.guild.GuildMuteType
import love.forte.simbot.kook.objects.SystemUser
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.utils.item.Items
import love.forte.simbot.utils.item.Items.Companion.emptyItems
import love.forte.simbot.utils.runInBlocking
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import love.forte.simbot.kook.objects.User as KkUser


/**
 *
 * Kook 组件下，[频道服务器][KookGuild] 的成员信息.
 *
 * @author ForteScarlet
 */
public interface KookGuildMember : GuildMember, KookComponentDefinition<KkUser> {
    override val bot: KookComponentBot
    override val id: ID
    
    /**
     * 此成员对应的源用户实例。
     */
    override val source: KkUser
    
    // region mute api
    /**
     * 取消禁言。没有参数的 [unmute] 默认情况下，代表使用类型 `1`: 麦克风静音。
     *
     * @see love.forte.simbot.kook.api.guild.GuildMuteCreateRequest
     */
    @JvmSynthetic
    override suspend fun unmute(): Boolean = unmute(GuildMuteType.TYPE_MICROPHONE)
    
    /**
     * 取消禁言。[type] 代表 [love.forte.simbot.kook.api.guild.GuildMuteCreateRequest] 的参数 `type`. 默认使用 [GuildMuteType.TYPE_MICROPHONE]，即 `1`.
     *
     * @see love.forte.simbot.kook.api.guild.GuildMuteCreateRequest
     */
    @JvmBlocking
    @JvmAsync
    public suspend fun unmute(type: Int): Boolean
    
    /**
     * 对此用户进行静音操作。
     * 默认情况下，[mute] 代表使用类型 `1`: 麦克风静音。
     *
     * @throws IllegalArgumentException 如果持续时间小于0
     * @see love.forte.simbot.kook.api.guild.GuildMuteCreateRequest
     */
    @JvmSynthetic
    override suspend fun mute(time: Long, timeUnit: TimeUnit): Boolean =
        mute(time, timeUnit, GuildMuteType.TYPE_MICROPHONE)
    
    /**
     * 对此用户进行静音操作。
     * 默认情况下，[mute] 代表使用类型 `1`: 麦克风静音。
     *
     * @throws IllegalArgumentException 如果持续时间小于0
     * @see love.forte.simbot.kook.api.guild.GuildMuteCreateRequest
     */
    @JvmSynthetic
    public suspend fun mute(time: Long, timeUnit: TimeUnit, type: Int): Boolean = mute(timeUnit.toMillis(time), type)
    
    /**
     * 对此用户进行静音操作。
     * 默认情况下，[mute] 代表使用类型 `1`: 麦克风静音。
     *
     * @throws IllegalArgumentException 如果持续时间小于0
     * @see love.forte.simbot.kook.api.guild.GuildMuteCreateRequest
     */
    @JvmSynthetic
    override suspend fun mute(duration: Duration): Boolean = mute(duration.inWholeMilliseconds)
    
    /**
     * 对此用户进行静音操作。
     *
     * @throws IllegalArgumentException 如果持续时间小于0
     * @see love.forte.simbot.kook.api.guild.GuildMuteCreateRequest
     */
    @JvmBlocking
    @JvmAsync
    public suspend fun mute(duration: Duration, type: Int): Boolean = mute(duration.inWholeMilliseconds, type)
    
    
    /**
     * 对此用户进行静音操作。
     *
     * @throws IllegalArgumentException 如果持续时间小于0
     * @see love.forte.simbot.kook.api.guild.GuildMuteCreateRequest
     */
    @JvmBlocking
    @JvmAsync
    public suspend fun mute(durationMillis: Long, type: Int = GuildMuteType.TYPE_MICROPHONE): Boolean
    
    
    /**
     * 对此用户进行静音操作。
     *
     * @throws IllegalArgumentException 如果持续时间小于0
     * @see love.forte.simbot.kook.api.guild.GuildMuteCreateRequest
     */
    @Api4J
    override fun muteBlocking(duration: JavaDuration): Boolean {
        return muteBlocking(duration, GuildMuteType.TYPE_MICROPHONE)
    }
    
    /**
     * 对此用户进行静音操作。
     *
     * @throws IllegalArgumentException 如果持续时间小于0
     * @see love.forte.simbot.kook.api.guild.GuildMuteCreateRequest
     */
    @Api4J
    public fun muteBlocking(duration: JavaDuration, type: Int): Boolean {
        return runInBlocking { mute(duration.toMillis(), type) }
    }
    // endregion
    
    /**
     * 向当前频道对象发起一个新的聊天会话（私聊）并发送消息。如果当前类型为 [KookGuildSystemMember],
     * 则会抛出 [UnsupportedActionException] 异常。
     *
     * @throws UnsupportedActionException 如果目标不支持
     *
     */
    @JvmBlocking
    @JvmAsync
    override suspend fun send(text: String): KookMessageCreatedReceipt
    
    /**
     * 向当前频道对象发起一个新的聊天会话（私聊）并发送消息。如果当前类型为 [KookGuildSystemMember],
     * 则会抛出 [UnsupportedActionException] 异常。
     *
     * @throws UnsupportedActionException 如果目标不支持
     *
     */
    @JvmBlocking
    @JvmAsync
    override suspend fun send(message: Message): KookMessageReceipt
    
    /**
     * 向当前频道对象发起一个新的聊天会话（私聊）并发送消息。如果当前类型为 [KookGuildSystemMember],
     * 则会抛出 [UnsupportedActionException] 异常。
     *
     * @throws UnsupportedActionException 如果目标不支持
     *
     */
    @JvmBlocking
    @JvmAsync
    override suspend fun send(message: MessageContent): KookMessageReceipt
    
    
    /**
     * 得到当前成员所在频道服务器。同 [guild].
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun guild(): KookGuild
    
    /**
     * 得到当前成员所在频道服务器。同 [guild].
     */
    @JvmBlocking(asProperty = true, suffix = "")
    @JvmAsync(asProperty = true)
    override suspend fun organization(): KookGuild = guild()
    
    
    /**
     * 获取此成员所拥有的所有角色。
     *
     * _Note: [roles] 尚在实验阶段，可能会在未来做出变更。_
     */
    @ExperimentalSimbotApi
    override val roles: Items<KookMemberRole>

    
    override val joinTime: Timestamp get() = Timestamp.notSupport()
    override val nickname: String
    override val avatar: String
    override val username: String
    
    public companion object
    
}


/**
 * 使用 [SystemUser] 作为基础用户对象来作为一个频道内的用户。
 *
 * @see SystemUser
 */
public class KookGuildSystemMember(
    override val bot: KookComponentBot,
    private val _guild: KookGuild,
) : KookGuildMember {
    override val source: SystemUser
        get() = SystemUser
    
    override val id: ID
        get() = source.id


    /**
     * 系统角色视为无权限。
     *
     * _Note: [roles] 尚在实验阶段，可能会在未来做出变更。_
     */
    @ExperimentalSimbotApi
    override val roles: Items<KookMemberRole> = emptyItems()

    override suspend fun guild(): KookGuild = _guild
    
    /**
     * 系统用户不支持禁言相关操作，永远得到 `false`.
     */
    @JvmSynthetic
    override suspend fun unmute(type: Int): Boolean = false
    
    /**
     * 系统用户不支持禁言相关操作，永远得到 `false`.
     */
    @JvmSynthetic
    override suspend fun mute(durationMillis: Long, type: Int): Boolean = false
    
    
    // region send 相关
    // 无法向系统用户发送消息
    private fun notSupport(): Nothing {
        throw UnsupportedActionException("Send message to system user (bot [$bot] in guild [${_guild}]) ")
    }
    
    @JvmSynthetic
    override suspend fun send(text: String): KookMessageCreatedReceipt {
        notSupport()
    }
    
    @JvmSynthetic
    override suspend fun send(message: Message): KookMessageCreatedReceipt {
        notSupport()
    }
    
    @JvmSynthetic
    override suspend fun send(message: MessageContent): KookMessageCreatedReceipt {
        notSupport()
    }
    // endregion
    
    override val username: String
        get() = source.username
    override val nickname: String
        get() = source.nickname
    override val avatar: String
        get() = source.avatar
    
    public companion object
}
