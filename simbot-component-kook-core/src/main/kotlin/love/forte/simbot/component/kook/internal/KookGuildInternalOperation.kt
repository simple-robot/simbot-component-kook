/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kook 的一部分。
 *
 *  simbot-component-kook 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kook 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

package love.forte.simbot.component.kook.internal

import love.forte.simbot.ID
import love.forte.simbot.component.kook.internal.KookChannelCategoryImpl.Companion.toCategory
import love.forte.simbot.component.kook.internal.KookChannelImpl.Companion.toKookChannel
import love.forte.simbot.component.kook.model.ChannelModel
import love.forte.simbot.component.kook.model.toModel
import love.forte.simbot.component.kook.util.requestBy
import love.forte.simbot.kook.api.channel.ChannelViewRequest
import love.forte.simbot.literal


internal inline fun KookGuildImpl.computeChannelCategories(
    id: String,
    crossinline action: (String, KookChannelCategoryImpl?) -> KookChannelCategoryImpl?,
): KookChannelCategoryImpl? {
    return internalChannelCategories.compute(id) { key, current ->
        action(key, current)
    }
}


internal inline fun KookGuildImpl.computeChannels(
    id: String,
    crossinline action: (String, KookChannelImpl?) -> KookChannelImpl?,
): KookChannelImpl? {
    return internalChannels.compute(id) { key, current ->
        action(key, current)
    }
}

internal fun KookGuildImpl.getInternalCategory(id: String): KookChannelCategoryImpl? = internalChannelCategories[id]

internal fun KookGuildImpl.getInternalChannel(id: String): KookChannelImpl? = internalChannels[id]
internal fun KookGuildImpl.getInternalChannel(id: ID): KookChannelImpl? = getInternalChannel(id.literal)

internal fun KookGuildImpl.removeInternalChannel(id: String): KookChannelImpl? = internalChannels.remove(id)

internal fun KookGuildImpl.removeInternalCategory(id: String): KookChannelCategoryImpl? =
    internalChannelCategories.remove(id)

/**
 * [computeMergeChannelModel] 中存在条件分支，不能完全保证原子性。
 */
internal suspend fun KookGuildImpl.computeMergeChannelModel(
    model: ChannelModel,
) {
    if (model.isCategory) {
        computeChannelCategories(model.id.literal) { _, current ->
            current?.also {
                it.source = model
            } ?: model.toCategory(this)
        }
    } else {
        // channel
        val categoryId = model.parentId
        val categoryIdLiteral = categoryId.literal
        val category: KookChannelCategoryImpl? = if (categoryIdLiteral.isEmpty()) {
            // no category
            null
        } else {
            val category: KookChannelCategoryImpl? = getInternalCategory(categoryIdLiteral)
                ?: queryChannelModel(categoryId)?.toCategory(this)
            
            if (category == null) {
                bot.logger.warn(
                    "Cannot find category(id={}) for channel {}.",
                    categoryIdLiteral,
                    model
                )
            }
            
            category
        }
        
        computeChannels(model.id.literal) { _, current ->
            current?.also {
                it.source = model
                if (category != null) {
                    val oldCategory = current.category
                    if (oldCategory == null || oldCategory.id != category.id) {
                        current.category = category
                    }
                }
            } ?: model.toKookChannel(baseBot, this, category)
        }
        
    }
}


internal suspend fun KookGuildImpl.queryChannelModel(channelId: ID): ChannelModel? {
    val model = ChannelViewRequest.create(channelId).let { req ->
        val result = req.requestBy(baseBot).takeIf { it.isSuccess }
        result?.parseData(baseBot.sourceBot.configuration.decoder, req.resultDeserializer)
    }
    
    return model?.toModel()
}

/**
 * 获取分类。
 * 如果当前没有，查询并合并为一个分类。
 */
internal suspend fun KookGuildImpl.findOrQueryAndComputeCategory(channelId: String): KookChannelCategoryImpl? {
    val category = getInternalCategory(channelId)
    if (category != null) return category
    
    return queryChannelModel(channelId.ID)
        ?.takeIf { it.isCategory }
        ?.also { model ->
            computeMergeChannelModel(model)
        }?.toCategory(this)
}


//// member


internal fun KookGuildImpl.getInternalMember(id: String): KookGuildMemberImpl? = internalMembers[id]
internal fun KookGuildImpl.getInternalMember(id: ID): KookGuildMemberImpl? = getInternalMember(id.literal)
