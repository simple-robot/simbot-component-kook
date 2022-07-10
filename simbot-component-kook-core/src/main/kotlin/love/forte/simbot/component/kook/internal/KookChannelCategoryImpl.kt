package love.forte.simbot.component.kook.internal

import love.forte.simbot.ID
import love.forte.simbot.component.kook.KookChannelCategory
import love.forte.simbot.component.kook.model.ChannelModel


internal data class KookChannelCategoryImpl(
    override val guild: KookGuildImpl,
    @Volatile override var source: ChannelModel,
) : KookChannelCategory, MutableChannelModelContainer {
    override val id: ID
        get() = source.id
    override val name: String
        get() = source.name
}


internal fun ChannelModel.asCategory(guild: KookGuildImpl): KookChannelCategoryImpl = KookChannelCategoryImpl(guild ,this)