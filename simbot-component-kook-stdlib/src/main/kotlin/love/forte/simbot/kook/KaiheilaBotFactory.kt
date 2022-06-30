@file:JvmName("KaiheilaBotFactory")

package love.forte.simbot.kook

import love.forte.simbot.ID
import love.forte.simbot.kook.internal.KaiheilaBotImpl
import love.forte.simbot.literal

/**
 * 最基础的用于构建 [KaiheilaBot] 的函数，提供票据信息 和 配置信息。
 */
public fun kaiheilaBot(
    ticket: KaiheilaBot.Ticket,
    configuration: KaiheilaBotConfiguration = KaiheilaBotConfiguration()
): KaiheilaBot {
    return KaiheilaBotImpl(
        ticket, configuration
    )
}

/**
 * 最基础的用于构建 [KaiheilaBot] 的函数，提供票据信息和配置函数。
 */
public fun kaiheilaBot(ticket: KaiheilaBot.Ticket, block: KaiheilaBotConfiguration.() -> Unit): KaiheilaBot =
    kaiheilaBot(ticket, KaiheilaBotConfiguration().also(block))


/**
 * 提供 [clientId] [token] 和 [KaiheilaBotConfiguration] 构建得到一个 [KaiheilaBot].
 */
public fun kaiheilaBot(
    clientId: ID,
    token: String,
    configuration: KaiheilaBotConfiguration = KaiheilaBotConfiguration()
): KaiheilaBot = kaiheilaBot(SimpleTicket(clientId.literal, token), configuration)


/**
 * 提供 [clientId] [token] 和 [KaiheilaBotConfiguration] 构建得到一个 [KaiheilaBot].
 */
public fun kaiheilaBot(
    clientId: String,
    token: String,
    configuration: KaiheilaBotConfiguration = KaiheilaBotConfiguration()
): KaiheilaBot = kaiheilaBot(SimpleTicket(clientId, token), configuration)


/**
 * 提供 [clientId] [token] 和 [KaiheilaBotConfiguration] 构建得到一个 [KaiheilaBot].
 */
public fun kaiheilaBot(
    clientId: ID,
    token: String,
    block: KaiheilaBotConfiguration.() -> Unit
): KaiheilaBot = kaiheilaBot(SimpleTicket(clientId.literal, token), block)


/**
 * 提供 [clientId] [token] 和 [KaiheilaBotConfiguration] 构建得到一个 [KaiheilaBot].
 */
public fun kaiheilaBot(
    clientId: String,
    token: String,
    block: KaiheilaBotConfiguration.() -> Unit
): KaiheilaBot = kaiheilaBot(SimpleTicket(clientId, token), block)





