package love.forte.simbot.component.kaihieila.internal

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import love.forte.simbot.*
import love.forte.simbot.component.kaihieila.*
import love.forte.simbot.definition.Role
import love.forte.simbot.kaiheila.objects.Channel
import love.forte.simbot.message.*
import java.util.stream.*
import kotlin.coroutines.*


/**
 *
 * @author ForteScarlet
 */
internal class KaiheilaChannelImpl(
    override val bot: KaiheilaComponentBotImpl,
    override val guild: KaiheilaGuildImpl,
    override val source: Channel
) : KaiheilaChannel, CoroutineScope {
    internal val job = SupervisorJob(guild.job)
    override val coroutineContext: CoroutineContext = guild.coroutineContext + job
    //internal lateinit var members: ConcurrentHashMap<String, KaiheilaMemberImpl>

    // internal suspend fun init() {
    // val membersMap = ConcurrentHashMap<String, KaiheilaMemberImpl>()
    // flow {
    //     var page = 0
    //     do {
    //         val result = GuildUserListRequest(guild.id, source.id, page = page++).requestDataBy(bot)
    //         result.items.forEach { emit(it) }
    //     } while (result.items.isNotEmpty())
    // }.map { user ->
    //     KaiheilaMemberImpl(bot, this, user).also { it.init() }
    // }.collect {
    //     membersMap[it.id.literal] = it
    // }
    //
    // members = membersMap
    // }

    override val guildId: ID
        get() = guild.id

    override val currentMember: Int
        get() = guild.currentMember

    override val maximumMember: Int
        get() = guild.maximumMember

    override val ownerId: ID
        get() = guild.ownerId

    override val owner: KaiheilaGuildMember
        get() = guild.owner

    override fun getMember(id: ID): KaiheilaGuildMember? = guild.getMember(id)
    override suspend fun member(id: ID): KaiheilaGuildMember? = guild.member(id)
    override fun getMembers(): Stream<out KaiheilaGuildMember> = guild.getMembers()
    override fun getMembers(groupingId: ID?): Stream<out KaiheilaGuildMember> = guild.getMembers(groupingId)
    override fun getMembers(groupingId: ID?, limiter: Limiter): Stream<out KaiheilaGuildMember> =
        guild.getMembers(groupingId, limiter)

    override fun getMembers(limiter: Limiter): Stream<out KaiheilaGuildMember> = guild.getMembers(limiter)
    override suspend fun members(groupingId: ID?, limiter: Limiter): Flow<KaiheilaGuildMember> =
        guild.members(groupingId, limiter)


    override suspend fun send(text: String): MessageReceipt {
        TODO("Not yet implemented")
    }

    override suspend fun send(message: Message): MessageReceipt {
        TODO("Not yet implemented")
    }

    override suspend fun send(message: MessageContent): MessageReceipt {
        TODO("Not yet implemented")
    }

    @Api4J
    override fun sendBlocking(text: String): MessageReceipt {
        TODO("Not yet implemented")
    }

    @Api4J
    override fun sendBlocking(message: Message): MessageReceipt {
        TODO("Not yet implemented")
    }

    @Api4J
    override fun sendBlocking(message: MessageContent): MessageReceipt {
        TODO("Not yet implemented")
    }

    @Api4J
    override fun getRoles(groupingId: ID?, limiter: Limiter): Stream<out Role> {
        TODO("Not yet implemented")
    }

    override suspend fun roles(groupingId: ID?, limiter: Limiter): Flow<Role> {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "KaiheilaChannel(source=$source)"
    }

}