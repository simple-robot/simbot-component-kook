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

package love.forte.simbot.component.kook

import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.StringID.Companion.ID
import love.forte.simbot.definition.Channel
import kotlin.coroutines.CoroutineContext
import love.forte.simbot.kook.objects.Channel as KChannel


/**
 * 一个基于 Kook Channel 的类型定义。
 *
 * @see KookChatChannel
 * @see KookCategoryChannel
 *
 * @author ForteScarlet
 */
public interface KookChannel : Channel {
    override val coroutineContext: CoroutineContext

    override val category: KookCategory?

    /**
     * 得到此实例内对应的 api 模块下的原始 channel 信息。
     *
     * @see KChannel
     */
    public val source: KChannel

    /**
     * 频道ID
     */
    override val id: ID
        get() = source.id.ID

    /**
     * 频道名称
     */
    override val name: String
        get() = source.name

}
