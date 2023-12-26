# v3.3.0.0-beta.1

> Release & Pull Notes: [v3.3.0.0-beta.1](https://github.com/simple-robot/simpler-robot/releases/tag/v3.3.0.0-beta.1) 

- fix: 消息发送中增加对 AtAll 的支持 ([`9363c9e`](https://github.com/simple-robot/simpler-robot/commit/9363c9e))
- website: 配置域名 ([`bfc48ad`](https://github.com/simple-robot/simpler-robot/commit/bfc48ad))
- fix(doc): 修复部分对配置文件 `ticket` 的描述错误问题 ([`3841f05`](https://github.com/simple-robot/simpler-robot/commit/3841f05))
- CI: Qodana for branches 'main' ([`e813648`](https://github.com/simple-robot/simpler-robot/commit/e813648))

# v3.2.0.0-alpha.8

> Release & Pull Notes: [v3.2.0.0-alpha.8](https://github.com/simple-robot/simpler-robot/releases/tag/v3.2.0.0-alpha.8) 

- pref(stdlib): Bot.[blocking/async]Processor 的参数提供为更便于Jav使用的函数式类型 ([`78d6341`](https://github.com/simple-robot/simpler-robot/commit/78d6341))


> Release & Pull Notes: [v3.2.0.0-alpha.8-dev.4](https://github.com/simple-robot/simpler-robot/releases/tag/v3.2.0.0-alpha.8-dev.4) 

- feat: core模块下 `KookEvent`追加属性 `sourceEventContent` 来支持获取原始的JSON事件字符串 ([`0e3a4c5`](https://github.com/simple-robot/simpler-robot/commit/0e3a4c5))
- feat: api和core支持私聊消息更新、频道消息更新 ([`fe39082`](https://github.com/simple-robot/simpler-robot/commit/fe39082))
- feat: api和core支持私聊消息撤回、频道消息撤回 ([`015dfde`](https://github.com/simple-robot/simpler-robot/commit/015dfde))
- feat: 一些针对未知事件的处理 ([`2a5ac0d`](https://github.com/simple-robot/simpler-robot/commit/2a5ac0d))
- pref(core): simbot-component-kook-107 core模块bot配置文件增加更多属性支持 ([`1b7173a..7b6c404`](https://github.com/simple-robot/simpler-robot/compare/1b7173a..5712572))

    <details><summary><code>1b7173a..7b6c404</code></summary>

    - [`1b7173a`](https://github.com/simple-robot/simpler-robot/commit/1b7173a)
    - [`7b6c404`](https://github.com/simple-robot/simpler-robot/commit/7b6c404)

    </details>

- CI: website deploy ([`5712572`](https://github.com/simple-robot/simpler-robot/commit/5712572))

# v3.2.0.0-alpha.8-dev.3

> Release & Pull Notes: [v3.2.0.0-alpha.8-dev.3](https://github.com/simple-robot/simpler-robot/releases/tag/v3.2.0.0-alpha.8-dev.3) 


# v3.2.0.0-alpha.8-dev.2

> Release & Pull Notes: [v3.2.0.0-alpha.8-dev.2](https://github.com/simple-robot/simpler-robot/releases/tag/v3.2.0.0-alpha.8-dev.2) 

- core: Role API ([`11b83a1`](https://github.com/simple-robot/simpler-robot/commit/11b83a1))
- core: message events ([`68fcd22`](https://github.com/simple-robot/simpler-robot/commit/68fcd22))
- core: message ([`b6c8673`](https://github.com/simple-robot/simpler-robot/commit/b6c8673))
- WIP: Bot States ([`361f3c1`](https://github.com/simple-robot/simpler-robot/commit/361f3c1))
- WIP: Bot Impl ([`80c2018..045dfba`](https://github.com/simple-robot/simpler-robot/compare/80c2018..940f194))

    <details><summary><code>80c2018..045dfba</code></summary>

  - [`80c2018`](https://github.com/simple-robot/simpler-robot/commit/80c2018)
  - [`045dfba`](https://github.com/simple-robot/simpler-robot/commit/045dfba)

    </details>

- WIP: Bot impl ([`940f194`](https://github.com/simple-robot/simpler-robot/commit/940f194))
- WIP: Bot Impl ([`a31112f..baad32b`](https://github.com/simple-robot/simpler-robot/compare/a31112f..f2d2833))

    <details><summary><code>a31112f..baad32b</code></summary>

  - [`a31112f`](https://github.com/simple-robot/simpler-robot/commit/a31112f)
  - [`baad32b`](https://github.com/simple-robot/simpler-robot/commit/baad32b)

    </details>

- module: core ([`f2d2833`](https://github.com/simple-robot/simpler-robot/commit/f2d2833))
- build: new multiplatform module for stdlib ([`9cb5a6d`](https://github.com/simple-robot/simpler-robot/commit/9cb5a6d))
- API: channel message API ([`0652e41`](https://github.com/simple-robot/simpler-robot/commit/0652e41))
- Event: Channel 相关事件 ([`3e70465`](https://github.com/simple-robot/simpler-robot/commit/3e70465))
- Event: 定义 Event 相关 ([`13a9273`](https://github.com/simple-robot/simpler-robot/commit/13a9273))
- WIP: 定义 Event 相关 ([`eaccf93`](https://github.com/simple-robot/simpler-robot/commit/eaccf93))
- API: user-chat API ([`0cf370a`](https://github.com/simple-robot/simpler-robot/commit/0cf370a))
- API: mute API ([`27c5463..7b04e1a`](https://github.com/simple-robot/simpler-robot/compare/27c5463..5991e33))

    <details><summary><code>27c5463..7b04e1a</code></summary>

  - [`27c5463`](https://github.com/simple-robot/simpler-robot/commit/27c5463)
  - [`7b04e1a`](https://github.com/simple-robot/simpler-robot/commit/7b04e1a)

    </details>

- API: Invite API ([`5991e33`](https://github.com/simple-robot/simpler-robot/commit/5991e33))
- API: Guild Role API ([`ab51441`](https://github.com/simple-robot/simpler-robot/commit/ab51441))
- API: Channel API ([`6f91ef3`](https://github.com/simple-robot/simpler-robot/commit/6f91ef3))
- WIP: Channels API ([`a87551d`](https://github.com/simple-robot/simpler-robot/commit/a87551d))
- API: Create asset ([`5f17abb`](https://github.com/simple-robot/simpler-robot/commit/5f17abb))
- pref(api-multi): Card/KMarkdown 相关调整 ([`fd6f318`](https://github.com/simple-robot/simpler-robot/commit/fd6f318))
- Objects: Card 和 KMarkdown ([`b34ad3b..95ed31a`](https://github.com/simple-robot/simpler-robot/compare/b34ad3b..d0ff1b6))

    <details><summary><code>b34ad3b..95ed31a</code></summary>

  - [`b34ad3b`](https://github.com/simple-robot/simpler-robot/commit/b34ad3b)
  - [`95ed31a`](https://github.com/simple-robot/simpler-robot/commit/95ed31a)

    </details>

- API: 服务器助力历史 ([`d0ff1b6`](https://github.com/simple-robot/simpler-robot/commit/d0ff1b6))
- API: 删除服务器静音或闭麦 ([`af25e23`](https://github.com/simple-robot/simpler-robot/commit/af25e23))
- API: 服务器静音闭麦列表 ([`24e993c`](https://github.com/simple-robot/simpler-robot/commit/24e993c))
- API: 踢出服务器 ([`14a26d6`](https://github.com/simple-robot/simpler-robot/commit/14a26d6))
- API: 离开服务器 ([`d4dcd89`](https://github.com/simple-robot/simpler-robot/commit/d4dcd89))
- API: 修改成员昵称 ([`292f767`](https://github.com/simple-robot/simpler-robot/commit/292f767))
- API: 服务器成员列表 ([`29de952`](https://github.com/simple-robot/simpler-robot/commit/29de952))
- API: 服务器详情 ([`f982878`](https://github.com/simple-robot/simpler-robot/commit/f982878))
- API: 获取当前用户加入的服务器列表 ([`68301e6`](https://github.com/simple-robot/simpler-robot/commit/68301e6))
- build: 一些升级 ([`52018f6`](https://github.com/simple-robot/simpler-robot/commit/52018f6))
- fix(api): API请求不再强制要求 HttpClient 安装 ContentNegotiation 插件 ([`2cdb764`](https://github.com/simple-robot/simpler-robot/commit/2cdb764))
- refactor(api): 多平台的部分类型 ([`ad93c12`](https://github.com/simple-robot/simpler-robot/commit/ad93c12))
- pref(core): 简单调整 event 内部调度 ([`8c198bb`](https://github.com/simple-robot/simpler-robot/commit/8c198bb))
- refactor(api): 多平台的部分类型 ([`85f994f`](https://github.com/simple-robot/simpler-robot/commit/85f994f))
- pref(core): 简单调整 event 内部调度 ([`ae9668e..2d5f59d`](https://github.com/simple-robot/simpler-robot/compare/ae9668e..v3.0.0.0-alpha.7))

    <details><summary><code>ae9668e..2d5f59d</code></summary>

  - [`ae9668e`](https://github.com/simple-robot/simpler-robot/commit/ae9668e)
  - [`76f9a2a`](https://github.com/simple-robot/simpler-robot/commit/76f9a2a)
  - [`2d5f59d`](https://github.com/simple-robot/simpler-robot/commit/2d5f59d)

    </details>

# v3.2.0.0-alpha.8-dev.1

> Release & Pull Notes: [v3.2.0.0-alpha.8-dev.1](https://github.com/simple-robot/simpler-robot/releases/tag/v3.2.0.0-alpha.8-dev.1) 

- core: Role API ([`11b83a1`](https://github.com/simple-robot/simpler-robot/commit/11b83a1))
- core: message events ([`68fcd22`](https://github.com/simple-robot/simpler-robot/commit/68fcd22))
- core: message ([`b6c8673`](https://github.com/simple-robot/simpler-robot/commit/b6c8673))
- WIP: Bot States ([`361f3c1`](https://github.com/simple-robot/simpler-robot/commit/361f3c1))
- WIP: Bot Impl ([`80c2018..045dfba`](https://github.com/simple-robot/simpler-robot/compare/80c2018..940f194))

    <details><summary><code>80c2018..045dfba</code></summary>

    - [`80c2018`](https://github.com/simple-robot/simpler-robot/commit/80c2018)
    - [`045dfba`](https://github.com/simple-robot/simpler-robot/commit/045dfba)

    </details>

- WIP: Bot impl ([`940f194`](https://github.com/simple-robot/simpler-robot/commit/940f194))
- WIP: Bot Impl ([`a31112f..baad32b`](https://github.com/simple-robot/simpler-robot/compare/a31112f..f2d2833))

    <details><summary><code>a31112f..baad32b</code></summary>

    - [`a31112f`](https://github.com/simple-robot/simpler-robot/commit/a31112f)
    - [`baad32b`](https://github.com/simple-robot/simpler-robot/commit/baad32b)

    </details>

- module: core ([`f2d2833`](https://github.com/simple-robot/simpler-robot/commit/f2d2833))
- build: new multiplatform module for stdlib ([`9cb5a6d`](https://github.com/simple-robot/simpler-robot/commit/9cb5a6d))
- API: channel message API ([`0652e41`](https://github.com/simple-robot/simpler-robot/commit/0652e41))
- Event: Channel 相关事件 ([`3e70465`](https://github.com/simple-robot/simpler-robot/commit/3e70465))
- Event: 定义 Event 相关 ([`13a9273`](https://github.com/simple-robot/simpler-robot/commit/13a9273))
- WIP: 定义 Event 相关 ([`eaccf93`](https://github.com/simple-robot/simpler-robot/commit/eaccf93))
- API: user-chat API ([`0cf370a`](https://github.com/simple-robot/simpler-robot/commit/0cf370a))
- API: mute API ([`27c5463..7b04e1a`](https://github.com/simple-robot/simpler-robot/compare/27c5463..5991e33))

    <details><summary><code>27c5463..7b04e1a</code></summary>

    - [`27c5463`](https://github.com/simple-robot/simpler-robot/commit/27c5463)
    - [`7b04e1a`](https://github.com/simple-robot/simpler-robot/commit/7b04e1a)

    </details>

- API: Invite API ([`5991e33`](https://github.com/simple-robot/simpler-robot/commit/5991e33))
- API: Guild Role API ([`ab51441`](https://github.com/simple-robot/simpler-robot/commit/ab51441))
- API: Channel API ([`6f91ef3`](https://github.com/simple-robot/simpler-robot/commit/6f91ef3))
- WIP: Channels API ([`a87551d`](https://github.com/simple-robot/simpler-robot/commit/a87551d))
- API: Create asset ([`5f17abb`](https://github.com/simple-robot/simpler-robot/commit/5f17abb))
- pref(api-multi): Card/KMarkdown 相关调整 ([`fd6f318`](https://github.com/simple-robot/simpler-robot/commit/fd6f318))
- Objects: Card 和 KMarkdown ([`b34ad3b..95ed31a`](https://github.com/simple-robot/simpler-robot/compare/b34ad3b..d0ff1b6))

    <details><summary><code>b34ad3b..95ed31a</code></summary>

    - [`b34ad3b`](https://github.com/simple-robot/simpler-robot/commit/b34ad3b)
    - [`95ed31a`](https://github.com/simple-robot/simpler-robot/commit/95ed31a)

    </details>

- API: 服务器助力历史 ([`d0ff1b6`](https://github.com/simple-robot/simpler-robot/commit/d0ff1b6))
- API: 删除服务器静音或闭麦 ([`af25e23`](https://github.com/simple-robot/simpler-robot/commit/af25e23))
- API: 服务器静音闭麦列表 ([`24e993c`](https://github.com/simple-robot/simpler-robot/commit/24e993c))
- API: 踢出服务器 ([`14a26d6`](https://github.com/simple-robot/simpler-robot/commit/14a26d6))
- API: 离开服务器 ([`d4dcd89`](https://github.com/simple-robot/simpler-robot/commit/d4dcd89))
- API: 修改成员昵称 ([`292f767`](https://github.com/simple-robot/simpler-robot/commit/292f767))
- API: 服务器成员列表 ([`29de952`](https://github.com/simple-robot/simpler-robot/commit/29de952))
- API: 服务器详情 ([`f982878`](https://github.com/simple-robot/simpler-robot/commit/f982878))
- API: 获取当前用户加入的服务器列表 ([`68301e6`](https://github.com/simple-robot/simpler-robot/commit/68301e6))
- build: 一些升级 ([`52018f6`](https://github.com/simple-robot/simpler-robot/commit/52018f6))
- fix(api): API请求不再强制要求 HttpClient 安装 ContentNegotiation 插件 ([`2cdb764`](https://github.com/simple-robot/simpler-robot/commit/2cdb764))
- refactor(api): 多平台的部分类型 ([`ad93c12`](https://github.com/simple-robot/simpler-robot/commit/ad93c12))
- pref(core): 简单调整 event 内部调度 ([`8c198bb`](https://github.com/simple-robot/simpler-robot/commit/8c198bb))
- refactor(api): 多平台的部分类型 ([`85f994f`](https://github.com/simple-robot/simpler-robot/commit/85f994f))
- pref(core): 简单调整 event 内部调度 ([`ae9668e..2d5f59d`](https://github.com/simple-robot/simpler-robot/compare/ae9668e..v3.0.0.0-alpha.7))

    <details><summary><code>ae9668e..2d5f59d</code></summary>

    - [`ae9668e`](https://github.com/simple-robot/simpler-robot/commit/ae9668e)
    - [`76f9a2a`](https://github.com/simple-robot/simpler-robot/commit/76f9a2a)
    - [`2d5f59d`](https://github.com/simple-robot/simpler-robot/commit/2d5f59d)

    </details>


# v3.0.0.0-alpha.7

> Release & Pull Notes: [v3.0.0.0-alpha.7](https://github.com/simple-robot/simpler-robot/releases/tag/v3.0.0.0-alpha.7) 

- fix(event): 修复 /guild/view API 以及 Bot 加入/离开 频道服务器时事件推送的问题 ([`8479330`](https://github.com/simple-robot/simpler-robot/commit/8479330))
- feat(core): 核心模块提供 `KookTempTarget` 支持追加临时消息id ([`c6c419f`](https://github.com/simple-robot/simpler-robot/commit/c6c419f))
- feat(core): 支持 `KookTempTarget` 来追加指定一个 `temp_target_id` ([`6a18976`](https://github.com/simple-robot/simpler-robot/commit/6a18976))
- feat: 修复 `MessageBtnClickEvent` 无法被正确触发的问题；增加组件模块下对 `MessageBtnClickEvent` 的支持事件类型 `KookMessageBtnClickEvent` ([`aa2fda4`](https://github.com/simple-robot/simpler-robot/commit/aa2fda4))
- pref(stdlib): 优化 bot 内 DEBUG 等调试日志 ([`b3da397`](https://github.com/simple-robot/simpler-robot/commit/b3da397))
- build: 版本递增 ([`60920b3`](https://github.com/simple-robot/simpler-robot/commit/60920b3))
- fix(api): 尝试修复 `Card` 反序列化异常的问题 ([`0720f20`](https://github.com/simple-robot/simpler-robot/commit/0720f20))

# v3.0.0.0-alpha.6

> Release & Pull Notes: [v3.0.0.0-alpha.6](https://github.com/simple-robot/simpler-robot/releases/tag/v3.0.0.0-alpha.6) 

- fix(api): API请求不再强制要求 HttpClient 安装 ContentNegotiation 插件 ([`60deb93`](https://github.com/simple-robot/simpler-robot/commit/60deb93))

# v3.0.0.0-alpha.5

> Release & Pull Notes: [v3.0.0.0-alpha.5](https://github.com/simple-robot/simpler-robot/releases/tag/v3.0.0.0-alpha.5) 

- pref(core): 简单优化部分 KookComponentBot 内部实现 ([`41ac2e8`](https://github.com/simple-robot/simpler-robot/commit/41ac2e8))
- pref(stdlib): KookBot开放更多属性 (Client) ([`5b58ff8`](https://github.com/simple-robot/simpler-robot/commit/5b58ff8))
- fix: 反序列化策略的泛型更新 ([`0f25fb7`](https://github.com/simple-robot/simpler-robot/commit/0f25fb7))
- build: changelog generator ([`178b791`](https://github.com/simple-robot/simpler-robot/commit/178b791))
- pref(website): 快速开始的首页以及更多字体 ([`e054f15`](https://github.com/simple-robot/simpler-robot/commit/e054f15))
- pref(website): 字体位置 ([`c8b674e`](https://github.com/simple-robot/simpler-robot/commit/c8b674e))
- pref(website): 优化代码块与加粗表现 ([`047fc66`](https://github.com/simple-robot/simpler-robot/commit/047fc66))
- pref(website): 正文与标题字体 ([`1f16146`](https://github.com/simple-robot/simpler-robot/commit/1f16146))
- pref(api): 优化API中相关内容 ([`4c49703..da63e14`](https://github.com/simple-robot/simpler-robot/compare/4c49703..51343d5))

    <details><summary><code>4c49703..da63e14</code></summary>

    - [`4c49703`](https://github.com/simple-robot/simpler-robot/commit/4c49703)
    - [`da63e14`](https://github.com/simple-robot/simpler-robot/commit/da63e14)

    </details>

- pref(api): 调整优化bot配置中有关client的相关内容 ([`51343d5`](https://github.com/simple-robot/simpler-robot/commit/51343d5))
- fix(api): api不再需要安装ContentNegotiation ([`2fae5f0`](https://github.com/simple-robot/simpler-robot/commit/2fae5f0))
- fix: snapshot CI ([`23d34f8`](https://github.com/simple-robot/simpler-robot/commit/23d34f8))
- website: 发布组件手册网站 ([`a62358f`](https://github.com/simple-robot/simpler-robot/commit/a62358f))
- build: issues templates ([`3053656`](https://github.com/simple-robot/simpler-robot/commit/3053656))
- build: 更新dokka到 `v1.8.10` ([`eb30e77`](https://github.com/simple-robot/simpler-robot/commit/eb30e77))

