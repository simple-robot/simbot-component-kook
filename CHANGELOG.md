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

