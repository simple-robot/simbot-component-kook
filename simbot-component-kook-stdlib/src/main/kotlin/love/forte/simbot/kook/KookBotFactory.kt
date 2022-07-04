@file:JvmName("KookBotFactory")

package love.forte.simbot.kook

import love.forte.simbot.ID
import love.forte.simbot.kook.internal.KookBotImpl
import love.forte.simbot.literal

/**
 * 最基础的用于构建 [KookBot] 的函数，提供票据信息 和 配置信息。
 */
public fun kookBot(
    ticket: KookBot.Ticket,
    configuration: KookBotConfiguration = KookBotConfiguration()
): KookBot {
    return KookBotImpl(
        ticket, configuration
    )
}

/**
 * 最基础的用于构建 [KookBot] 的函数，提供票据信息和配置函数。
 */
public fun kookBot(ticket: KookBot.Ticket, block: KookBotConfiguration.() -> Unit): KookBot =
    kookBot(ticket, KookBotConfiguration().also(block))


/**
 * 提供 [clientId] [token] 和 [KookBotConfiguration] 构建得到一个 [KookBot].
 */
public fun kookBot(
    clientId: ID,
    token: String,
    configuration: KookBotConfiguration = KookBotConfiguration()
): KookBot = kookBot(SimpleTicket(clientId.literal, token), configuration)


/**
 * 提供 [clientId] [token] 和 [KookBotConfiguration] 构建得到一个 [KookBot].
 */
public fun kookBot(
    clientId: String,
    token: String,
    configuration: KookBotConfiguration = KookBotConfiguration()
): KookBot = kookBot(SimpleTicket(clientId, token), configuration)


/**
 * 提供 [clientId] [token] 和 [KookBotConfiguration] 构建得到一个 [KookBot].
 */
public fun kookBot(
    clientId: ID,
    token: String,
    block: KookBotConfiguration.() -> Unit
): KookBot = kookBot(SimpleTicket(clientId.literal, token), block)


/**
 * 提供 [clientId] [token] 和 [KookBotConfiguration] 构建得到一个 [KookBot].
 */
public fun kookBot(
    clientId: String,
    token: String,
    block: KookBotConfiguration.() -> Unit
): KookBot = kookBot(SimpleTicket(clientId, token), block)





