# 日志

## API 日志

开启 `love.forte.simbot.kook.api` 的 `DEBUG` 级别日志，
可以看到 **所有** API 请求过程中的信息，例如入参、结果等。

<warning>

这可能会泄露一些隐私信息，请注意保护日志安全。

</warning>

## Stdlib Bot 日志

在stdlib模块下的 `Bot` 中提供了两个日志命名：

<list>
<li>

`love.forte.simbot.kook.bot.${ticket.clientId}`

开启 `DEBUG` 和 `TRACE` 级别的日志可得到一些利于调试的、与事件之外的内容相关的日志。

</li>
<li>

`love.forte.simbot.kook.event.${ticket.clientId}`

开启 `DEBUG` 和 `TRACE` 级别的日志可得到一些利于调试的、与事件相关的日志。

</li>
</list>

其中 `${ticket.clientId}` 就是你 Bot 对应的信息。

## 组件库 KookBot 日志

在core模块下的 `KookBot` 中提供了
`love.forte.simbot.component.kook.bot.${sourceBot.ticket.clientId}`
的日志，开启它的 `DEBUG` 级别日志可以得到一些由组件库的 `KookBot`
额外提供的一些利于调试的日志。
