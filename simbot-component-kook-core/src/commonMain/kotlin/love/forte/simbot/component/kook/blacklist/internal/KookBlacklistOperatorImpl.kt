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

package love.forte.simbot.component.kook.blacklist.internal

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import love.forte.simbot.ability.DeleteOption
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.literal
import love.forte.simbot.component.kook.blacklist.KookBlacklistItem
import love.forte.simbot.component.kook.blacklist.KookBlacklistOperator
import love.forte.simbot.component.kook.bot.KookBot
import love.forte.simbot.component.kook.util.requestData
import love.forte.simbot.kook.api.ListData
import love.forte.simbot.kook.api.blacklist.GetBlacklistListApi
import love.forte.simbot.kook.api.blacklist.createFlow

/**
 *
 * @author ForteScarlet
 */
internal class KookBlacklistOperatorImpl(private val bot: KookBot) : KookBlacklistOperator {
    override suspend fun list(
        guildId: ID,
        page: Int?,
        size: Int?
    ): ListData<KookBlacklistItem> {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun flow(guildId: ID, batchSize: Int?): Flow<KookBlacklistItem> {
        return GetBlacklistListApi.createFlow { page ->
            val api = GetBlacklistListApi.create(guildId = guildId.literal, page = page, pageSize = batchSize)
            bot.requestData(api)
        }.flatMapConcat { it.items.asFlow() }
            .map { TODO("Not yet implemented") }
    }

    override suspend fun add(
        guildId: ID,
        targetId: ID,
        remark: String?,
        delMsgDays: Int?
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(
        guildId: ID,
        targetId: ID,
        vararg options: DeleteOption
    ) {
        TODO("Not yet implemented")
    }
}