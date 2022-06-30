package love.forte.simbot.component.kaiheila

import kotlinx.serialization.Serializable
import love.forte.simbot.kook.KookBotConfiguration


/**
 *
 * 组件bot所使用的额外拓展配置类，应用于 [KaiheilaBotManager] 中。
 *
 * @author ForteScarlet
 */
public class KaiheilaComponentBotConfiguration(public var botConfiguration: KookBotConfiguration) {

    /**
     * 内部缓存对象信息的同步周期。
     *
     * 同步用于防止出现根据事件进行对象调整时出现遗漏而导致出现数据不对等的情况。同步会做如下操作：
     * - 如果当前缓存中不存在，但实际存在的实例，补充添加。
     * - 如果出现当前缓存存在，但实际不存在的实例，终止内部所有任务并移除。
     *
     * 目前暂时不会对预期内的对象进行信息同步（例如同步用户名等内容）
     *
     */
    public var syncPeriods: SyncPeriods = SyncPeriods()

    /**
     * 内部缓存对象信息的同步周期信息。
     *
     * 周期内各个不同的任务独立执行，如果需要自定义同步周期，请同时考虑api是否可能达到调用频率上限。
     */
    @Serializable
    public data class SyncPeriods(
        /**
         * 对频道服务器进行同步的周期，单位毫秒。
         * 服务器同步的同时会去同步此服务器下的所有频道列表与成员列表。
         */
        public val guildSyncPeriod: Long = 180_000L,

        /**
         * 同步数据是分页分批次的同步。[batchDelay] 配置每一批次后进行挂起等待的时间，单位毫秒。
         *
         * 可以通过调大此参数来减缓api的请求速率, 默认不等待。
         */
        public val batchDelay: Long = 0L,
        
    ) {
        @Deprecated("Unused.", ReplaceWith("Long.MAX_VALUE"))
        public val memberSyncPeriods: Long get() = Long.MAX_VALUE
    }
}