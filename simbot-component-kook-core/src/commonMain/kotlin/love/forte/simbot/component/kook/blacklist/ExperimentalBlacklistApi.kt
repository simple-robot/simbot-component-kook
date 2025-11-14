/*
 *     Copyright (c) 2025. ForteScarlet.
 *
 *     This file is part of simbot-component-kook.
 *
 *     simbot-component-kook is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     simbot-component-kook is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with simbot-component-kook,
 *     If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.component.kook.blacklist

/**
 * 与频道服务器黑名单列表相关的试验性 API。
 * 这些 API 仍处于试验性阶段，可能会随时被更改、删除，不保证稳定性。
 *
 * @since 4.4.0
 * @author ForteScarlet
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@MustBeDocumented
@RequiresOptIn(
    message = "与频道服务器黑名单列表相关的试验性 API。" +
            "这些 API 仍处于试验性阶段，可能会随时被更改、删除，不保证稳定性。",
    level = RequiresOptIn.Level.WARNING
)
public annotation class ExperimentalBlacklistApi
