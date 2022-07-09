package love.forte.simbot.component.kook

import love.forte.simbot.CharSequenceID
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.definition.Category
import love.forte.simbot.kook.objects.Channel
import love.forte.simbot.kook.objects.KookObjects


/**
 * Kook 组件中对于 [频道类型][Category] 的定义。
 *
 * [KookChannelCategory] 包装那些 [Channel.isCategory] 值为true的原始子频道类型。
 *
 * 分组两种可能的类型：[NormalKookChannelCategory] 和 [RootKookChannelCategory]。
 *
 * 如果某频道处于一个频道服务器的“顶层”，也就是说如果某个频道的 [Channel.parentId] 为空，
 * 则这个频道的分类类型为 [RootKookChannelCategory]；相反，如果某个子频道存在实际上的
 * 分类，则分类类型为 [NormalKookChannelCategory]。
 *
 * @see NormalKookChannelCategory
 * @see RootKookChannelCategory
 *
 */
public sealed interface KookChannelCategory : Category, KookComponentDefinition<KookObjects> {
    
    /**
     * 如果当前分类为 [NormalKookChannelCategory]，则 [source] 类型为 [Channel]，
     * 否则（如果为 [RootKookChannelCategory] ），则 [source] 类型为 [KookObjects]
     * 的一个无意义对象实现。
     */
    override val source: KookObjects
    
    /**
     * 此分类ID。
     *
     * 如果当前类型为 [RootKookChannelCategory]，则始终为空。
     */
    override val id: ID
    
    /**
     * 此分类名称。
     *
     * 如果当前类型为 [RootKookChannelCategory]，则始终为空。
     */
    override val name: String
    
    
    /**
     * 尝试从当前分组中寻找指定 [id] 的 [KookChannel]。
     *
     * _**ExperimentalSimbotApi**: 是否应该允许从Category中再次反向获取频道列表仍需考虑斟酌，因此此api未来可能会被考虑移除或重新设计。不要过于依赖此API。_
     */
    @ExperimentalSimbotApi
    public operator fun get(id: ID): KookChannel?
    
    /**
     * 得到此分组下的所有子频道的副本。
     *
     * _**ExperimentalSimbotApi**: 是否应该允许从Category中再次反向获取频道列表仍需考虑斟酌，因此此api未来可能会被考虑移除或重新设计。不要过于依赖此API。_
     */
    @ExperimentalSimbotApi
    public val channels: List<KookChannel>
}

/**
 * 一个普通的频道分类实现。与 [RootKookChannelCategory] 相对，[NormalKookChannelCategory] 代表那些实际存在的子频道分类。
 *
 */
public interface NormalKookChannelCategory : KookChannelCategory {
    /**
     * 这个分类的原本的来源类型。
     */
    override val source: Channel
    
    /**
     * 分类ID
     */
    override val id: ID
        get() = source.id
    
    /**
     * 分类名称
     */
    override val name: String
        get() = source.name
    
}

/**
 * [RootKookChannelCategory] 是用来描述作为“顶层分类”的分类，也就是在一个频道服务器中，不存在分类的那些子频道的“分类”。
 *
 * 需要注意的是，[RootKookChannelCategory] 不会被填充到 [KookChannel.category] 中。如果一个 [KookChannel]
 * 属于顶层分类，则这个 [KookChannel.category] 将会为null。
 */
public interface RootKookChannelCategory : KookChannelCategory {
    /**
     * 得到一个无意义的默认返回值。
     */
    override val source: KookObjects get() = InvalidKookObjects
    
    /**
     * 顶层分类的ID始终为空。
     */
    override val id: ID
        get() = CharSequenceID.EMPTY
    
    /**
     * 顶层分类不存在名称。
     */
    override val name: String
        get() = ""
    
    /**
     * 应用于 [RootKookChannelCategory.source] 中，作为 [KookObjects] 的无意义实现。
     */
    public object InvalidKookObjects : KookObjects
    
}