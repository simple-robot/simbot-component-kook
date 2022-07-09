package love.forte.simbot.component.kook.internal

import kotlinx.coroutines.cancel
import love.forte.simbot.ID
import love.forte.simbot.component.kook.KookChannel
import love.forte.simbot.component.kook.KookChannelCategory
import love.forte.simbot.component.kook.NormalKookChannelCategory
import love.forte.simbot.component.kook.RootKookChannelCategory
import love.forte.simbot.component.kook.model.ChannelModel
import love.forte.simbot.literal
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.cancellation.CancellationException


internal abstract class KookChannelCategoryAgent {
    internal abstract val channelMap: ConcurrentHashMap<String, KookChannelImpl>
    internal abstract val asCategory: KookChannelCategory
    operator fun get(id: String): KookChannelImpl? = channelMap[id]
    fun cancel(reason: CancellationException? = null) {
        channelMap.values.forEach {
            it.cancel(reason)
        }
    }
    
    fun remove(id: String): KookChannelImpl? = channelMap.remove(id)
}

internal class NormalKookChannelCategoryImpl(
    @Volatile override var source: ChannelModel,
    override val channelMap: ConcurrentHashMap<String, KookChannelImpl> = ConcurrentHashMap(),
) : KookChannelCategoryAgent(), NormalKookChannelCategory {
    override val asCategory: KookChannelCategory
        get() = this
    
    override fun get(id: ID): KookChannel? = this[id.literal]
    override val channels: List<KookChannel>
        get() = channelMap.values.toList()
    
    override fun toString(): String {
        return "NormalKookChannelCategoryImpl(id=$id, name=$name, source=$source, channelSize=${channelMap.size})"
    }
}


internal class RootKookChannelCategoryImpl(override val channelMap: ConcurrentHashMap<String, KookChannelImpl> = ConcurrentHashMap()) :
    KookChannelCategoryAgent(), RootKookChannelCategory {
    override val asCategory: KookChannelCategory
        get() = this
    
    override fun get(id: ID): KookChannel? = this[id.literal]
    override val channels: List<KookChannel>
        get() = channelMap.values.toList()
    
    override fun toString(): String {
        return "RootKookChannelCategoryImpl(channelSize=${channelMap.size})"
    }
}