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

import love.forte.simbot.ability.DeleteOption
import love.forte.simbot.ability.DeleteSupport
import love.forte.simbot.ability.StandardDeleteOption
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.StringID.Companion.ID
import love.forte.simbot.component.kook.bot.KookBot
import love.forte.simbot.definition.Channel
import love.forte.simbot.suspendrunner.ST
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
public interface KookChannel : Channel, DeleteSupport {
    /**
     * Channel 所属 bot
     * @since 4.2.0
     */
    public val bot: KookBot

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

    /**
     * 删除此频道。
     *
     * 如果 [options] 中不包括 [StandardDeleteOption.IGNORE_ON_FAILURE],
     * 则当API请求失败时错误会传播地抛出。
     *
     * 注意：[StandardDeleteOption.IGNORE_ON_FAILURE] 会捕获并隐藏任何产生的异常。
     * 因为 KOOK 文档中并未明确指出当"删除目标不存在"时的错误码或错误状态，因此 [StandardDeleteOption.IGNORE_ON_NO_SUCH_TARGET]
     * 暂时无法生效。
     * _如果实际上官方有相关说明或后续更新了相关说明，可以随时通过 ISSUES 或 PR 协助我们完善。_
     *
     *
     * @throws love.forte.simbot.kook.api.ApiResponseException
     * 如果 [options] 中不包括 [IGNORE_ON_FAILURE][StandardDeleteOption.IGNORE_ON_FAILURE],
     * 则当API请求失败时错误会传播地抛出。
     * @see love.forte.simbot.kook.api.channel.DeleteChannelApi
     * @since 4.1.6
     */
    @ST
    override suspend fun delete(vararg options: DeleteOption)
}
