/*
 *
 *  * Copyright (c) 2021. ForteScarlet All rights reserved.
 *  * Project  simple-robot
 *  * File     MiraiAvatar.kt
 *  *
 *  * You can contact the author through the following channels:
 *  * github https://github.com/ForteScarlet
 *  * gitee  https://gitee.com/ForteScarlet
 *  * email  ForteScarlet@163.com
 *  * QQ     1149159218
 *
 */

package love.forte.simbot.kaiheila.objects

import kotlinx.serialization.*
import love.forte.simbot.*
import love.forte.simbot.definition.*


/**
 * 开黑啦objects - [频道Channel](https://developer.kaiheila.cn/doc/objects#%E9%A2%91%E9%81%93Channel)
 *
 * #### 示例
 *
 * ```json
 * {
 *     "id": "53002000000000",
 *     "name": "新的频道",
 *     "user_id": "2418239356",
 *     "guild_id": "6016389000000",
 *     "is_category": 0,
 *     "parent_id": "6016400000000000",
 *     "level": null,
 *     "slow_mode": 0,
 *     "topic": "新的频道的说明",
 *     "type": 1,
 *     "permission_overwrites": [
 *         {
 *             "role_id": 0,
 *             "allow": 0,
 *             "deny": 0
 *         }
 *     ],
 *     "permission_users": [],
 *     "permission_sync": 1
 * }
 * ```
 *
 *
 * @author ForteScarlet
 */
public interface Channel : KhlObjects, ChannelInfo {

    /** 频道id */
    override val id: CharSequenceID

    /** 频道名称 */
    override val name: String


    /** 创建者id */
    public val userId: CharSequenceID

    /** 服务器id */
    override val guildId: CharSequenceID

    /** 频道简介 */
    public val topic: String

    /** 是否为分组 */
    public val isCategory: Boolean

    /** 上级分组的id */
    public val parentId: String

    /** 排序level */
    public val level: Int

    /** 慢速模式下限制发言的最短时间间隔, 单位为秒(s) */
    public val slowMode: Int

    /** 频道类型: 1 文字频道, 2 语音频道 */
    public val type: Int

    /** 针对角色在该频道的权限覆写规则组成的列表 */
    public val permissionOverwrites: List<ChannelPermissionOverwrites>

    /** 针对用户在该频道的权限覆写规则组成的列表 */
    public val permissionUsers: List<String>

    /** 权限设置是否与分组同步, 1 or 0 */
    public val permissionSync: Int


    /**
     * [Channel] 的类型，一般出现在事件中。
     */
    @Serializable
    public enum class Type {
        GROUP, PERSON,
    }

    // impls

    override val ownerId: CharSequenceID get() = userId
    override val description: String get() = topic
    override val createTime: Timestamp get() = Timestamp.NotSupport

}


/**
 * 判断频道类型是否为 [Channel.Type.PERSON].
 */
public inline val Channel.Type.isPrivate: Boolean get() = this == Channel.Type.PERSON

/**
 * 判断频道类型是否为 [Channel.Type.PERSON].
 */
public inline val Channel.Type.isPerson: Boolean get() = isPrivate

/**
 * 判断频道类型是否为 [Channel.Type.GROUP].
 */
public inline val Channel.Type.isGroup: Boolean get() = this == Channel.Type.GROUP


/**
 * 针对角色在该频道的权限覆写规则组成的列表.
 * ```json
 *     "permission_overwrites": [
 *         {
 *             "role_id": 0,
 *             "allow": 0,
 *             "deny": 0
 *         }
 *     ],
 *
 * ```
 *
 */
public interface ChannelPermissionOverwrites {

    public val roleId: Int

    public val allow: Int

    public val deny: Int

    // companion object : SerializerModuleRegistrar {
    //     override fun SerializersModuleBuilder.serializerModule() {
    //         polymorphic(ChannelPermissionOverwrites::class) {
    //             subclass(ChannelPermissionOverwritesImpl::class)
    //             default { ChannelPermissionOverwritesImpl.serializer() }
    //         }
    //     }
    // }
}

