/*
 * Copyright (c) 2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook,
 * If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.kook.event

import kotlinx.serialization.Serializable


/**
 * Kook Event - [事件Event](https://developer.kaiheila.cn/doc/eventhttps://developer.kookapp.cn/doc/event/event-introduction
 *
 *
 *
 */
@Serializable
public abstract class Event {

    /**
     * 消息通道类型, `GROUP` 为组播消息, `PERSON` 为单播消息, `BROADCAST` 为广播消息
     */
    public abstract val channelType: String

    /**
     * 事件的类型。
     *
     * - 1:文字消息
     * - 2:图片消息
     * - 3:视频消息
     * - 4:文件消息
     * - 8:音频消息
     * - 9:KMarkdown
     * - 10:card消息
     * - 255:系统消息
     * - 其它的暂未开放
     */
    public abstract val type: Int

    /**
     * 发送目的, 频道消息类时, 代表的是频道 `channel_id`，如果 `channel_type` 为 `GROUP` 组播且 `type` 为 `255` 系统消息时，则代表服务器 `guild_id`
     */
    public abstract val targetId: String

    /**
     * 发送者 id, `1` 代表系统
     */
    public abstract val authorId: String

    /**
     * 消息内容, 文件，图片，视频时，content 为 url
     */
    public abstract val content: String

    /**
     * 消息的 id
     */
    public abstract val msgId: String

    /**
     * 消息发送时间的毫秒时间戳
     */
    public abstract val msgTimestamp: Long

    /**
     * 随机串，与用户消息发送 api 中传的 nonce 保持一致
     */
    public abstract val nonce: String

    /**
     * 不同的消息类型，结构不一致
     */
    public abstract val extra: Any? // TODO


}



public abstract class EventExtra
