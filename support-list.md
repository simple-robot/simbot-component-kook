# 功能支持列表

## API

- [x] [Gateway API](https://developer.kookapp.cn/doc/http/gateway)
- [x] [Asset API](https://developer.kookapp.cn/doc/http/asset)
- [x] [Channel API](https://developer.kookapp.cn/doc/http/channel)
- [x] [Guild API & Guild Mute API](https://developer.kookapp.cn/doc/http/guild)
- [x] [Invite API](https://developer.kookapp.cn/doc/http/invite)
- [x] [Member API](https://developer.kookapp.cn/doc/http/channel-user)
- [x] [Guild Role API](https://developer.kookapp.cn/doc/http/guild-role)
- [x] [User API](https://developer.kookapp.cn/doc/http/user)
- [x] [UserChat API](https://developer.kookapp.cn/doc/http/user-chat)
- [x] [Message API](https://developer.kookapp.cn/doc/http/message)
- [x] [Direct message API](https://developer.kookapp.cn/doc/http/direct-message)
- [ ] [Intimacy API](https://developer.kookapp.cn/doc/http/intimacy)
- [ ] [Guild Emoji API](https://developer.kookapp.cn/doc/http/guild-emoji)
- [ ] [Blacklist API](https://developer.kookapp.cn/doc/http/blacklist)
- [ ] [Badge API](https://developer.kookapp.cn/doc/http/badge)
- [ ] [Game API](https://developer.kookapp.cn/doc/http/game)
- [ ] reaction 相关API

## 事件

- [x] 事件类型与解析
- [x] 事件订阅
- [x] [消息相关事件](https://developer.kookapp.cn/doc/event/message)
    - [x] 频道消息更新、删除
    - [x] 私聊消息更新、删除
- 系统事件
    - [x] 子频道相关事件（变更、消息pin等）
    - [x] 频道服务器相关事件（变更等）
    - [x] 频道成员相关事件
    - [x] 频道权限（`Role`）相关事件
    - [x] 频道用户封禁相关事件
    - [x] 用户相关事件（信息更新等）
    - [x] `Bot` 自身事件（加入、离开频道等）
    - [x] 其他事件（例如 `Card` 中的 button 被点击事件）
    - [ ] reaction 相关事件
- [x] 未知消息处理


## stdlib(标准库)模块

- [x] `Bot` 实现（包括与服务器的连接、事件的订阅）
- [x] Kotlin 多平台实现
- [x] ws压缩数据接收（仅在 `JVM` 平台和 `JS` 平台上支持、且 `JS` 默认不开启）

## core模块 (simbot组件)

- [x] `KookBot` 与API模块关联类型定义与实现
- [x] 与 API 模块对应的事件实现
- [x] 事件与内建缓存的刷新
