# Simple Robot 开黑啦组件

##          

## 支持情况

| 功能                                               | 开启情况    | 相关模块   | 备注                                                               |
|--------------------------------------------------|---------|--------|------------------------------------------------------------------|
| 开黑啦api的定义与实现                                     | 完成      | api    |                                                                  |
| 开黑啦事件的定义与实现                                      | 完成      | api    |                                                                  |
| 开黑啦的BOT对象定义（ws连接）                                | 完成      | stdlib |                                                                  |
| 开黑啦的KMarkdown基础定义                                | 完成(实验中) | api    |                                                                  |
| 开黑啦的Card基础定义                                     | 完成(实验中) | api    |                                                                  |
| 对接simbot-BOT实现(`Bot`、`BotManager`、`Component`实现) | 完成      | core   |                                                                  |
| 对接simbot-api实现(例如 `send`、`getGuilds()` )         | 完成      | core   | 开黑啦中的 `user-chat` 暂时视为`Friend`对象，但是私聊消息仅实现 `ContactMessageEvent` |
| 对接simbot-boot                                    | 完成      | boot   |                                                                  |
| 对接simbot-事件实现                                    | *完成部分*  | core   | 目前仅实现了ChannelMessageEvent 和 ContactMessageEvent，即频道消息与私聊消息       |
| 稳定性测试                                            | 极少      | -      | 尚未进行一定规模程度的稳定性测试                                                 |


