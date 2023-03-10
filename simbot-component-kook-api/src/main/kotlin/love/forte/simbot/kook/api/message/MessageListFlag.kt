/*
 * Copyright (c) 2022-2023. ForteScarlet.
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

package love.forte.simbot.kook.api.message

import kotlinx.serialization.Serializable

/**
 *
 * 对消息进行查询时的 `flag` 参数范围。
 *
 * @author ForteScarlet
 */
@Serializable
public enum class MessageListFlag {

    /**
     * 查询参考消息之前的消息，不包括参考消息。
     */
    BEFORE,

    /**
     * 查询以参考消息为中心，前后一定数量的消息。
     */
    AROUND,

    /**
     * 查询参考消息之后的消息，不包括参考消息。
     */
    AFTER,
}

