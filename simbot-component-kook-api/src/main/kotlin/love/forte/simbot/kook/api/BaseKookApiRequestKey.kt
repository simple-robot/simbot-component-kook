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

package love.forte.simbot.kook.api

import love.forte.simbot.kook.util.unmodifiable

/**
 *
 * 用于 [KookApiRequest] 实现类的伴生对象进行实现的辅助抽象类，
 * 用于提供 apiPathList等。
 * @author ForteScarlet
 */
public abstract class BaseKookApiRequestKey(vararg paths: String) {

    /**
     * 路径列表。
     * [apiPathList] 为不可变列表。
     */
    public val apiPathList: List<String> = paths.asList().unmodifiable()


}
