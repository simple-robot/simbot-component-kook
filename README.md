# Simple Robot Kook 组件

此为 [Simple Robot v3][simbot3] （以下简称为 `simbot3` ） 下基于simbot标准API对 [Kook](https://www.kookapp.cn/) 的组件支持。

更多详情请参考 [simbot3][simbot3] 或者 [simbot3官网](https://simbot.forte.love)。


> ⚠️🔧 README 施工中。。。

## 支持情况

已经基本完成。

## 稳定程度

当前版本处于 **`ALPHA`** 版本阶段，即代表：
- 可能存在诸多[已知问题](https://github.com/simple-robot/simbot-component-kook/issues)和未知问题，
- 不保证API的稳定与兼容（可能随时发布存在不兼容内容的更新）
- 需要[反馈](https://github.com/simple-robot/simbot-component-kook/issues)或[PR](https://github.com/simple-robot/simbot-component-kook/pulls)协助

## 模块引导

### API

[api模块][m-api] 是用于提供对Kook中各内容（例如事件、API、对象等）的定义模块。此模块依赖 `simbot-api`，但仅最低限度的实现 `simbot-api` 中的部分类型，
不实现任何功能性内容（例如只实现 `id`、`username` 属性的获取，但是不考虑诸如 **消息发送** 等相关内容的实现 ）。

此模块定义封装Kook中绝大多数的API（例如获取属性、发送消息等）供于其他模块或外界使用。
API的封装于定义基于 [Ktor（v2）](https://ktor.io/)，如果仅希望获得一些对API的基本封装，则可以考虑单独使用此模块。

👉 [前往模块][m-api] 查看更多

### STDLIB

[stdlib模块][m-stdlib] 基于 [api模块][m-api]，是对 Kook Bot 的最**基础**实现。
stdlib意为标准库，其宗旨在于实现完整的 Kook Bot 对事件的接收与处理，并尽可能保留事件最原始的状态（不做过多的封装）。

标准库实现 Kook Bot 最基础的事件监听（`websocket based`），而对于功能交互则需要开发者自行借助 [api模块][m-api] 中提供的内容来完成，
这可以使得开发者对整个事件处理流程中拥有更多的掌控性或发挥空间。

如果你希望通过更原生的方式开发 Kook Bot ，可以考虑直接使用stdlib模块。

👉 [前往模块][m-stdlib] 查看更多

### CORE

[core模块][m-core] 基于 [stdlib模块][m-stdlib]，是通过 [stdlib模块][m-stdlib] 对 [simbot3][simbot3]
内容的封装，是 [simbot3][simbot3] 的 **Kook组件** (simbot-component-kook) 。

通过 [core模块][m-core] 你可以使用 [simbot3][simbot3] 风格的API进行快速开发，并与其他支持的组件进行协同。
[core模块][m-core] 实现 `simbot-api` 和 `simbot-core` 中绝大多数（可以被支持的）功能，包括事件的实现和功能性API等。

如果你希望使用拥有更高封装性的API或与其他simbot组件协同，又或是与Spring Boot整合，那么 [core模块][m-core] 可以是一种参考。

👉 [前往模块][m-core] 查看更多

## 走马观花

```kotlin
// simbot-core / simbot-boot
suspend fun KookContactMessageEvent.onEvent() {
    author().send("Hello World")
}
```


```kotlin
// simbot-boot
@Listener
@Filter("签到")
suspend fun KookChannelMessageEvent.onEvent() {
    reply("签到成功")
}
```

```kotlin
@Listener
@Filter("叫我{{name,.+}}")
suspend fun KookChannelMessageEvent.onEvent(@FilterValue("name") name: String) {
    group.send(At(author.id) + "好的，以后就叫你$name了".toText())
}
```

简单的完整示例：

```kotlin
suspend fun main() {
    createSimpleApplication {
        // 注册并使用Kook组件。
        useKook()
        
        // 注册各种监听函数
        listeners {
            // 监听联系人(私聊)消息
            // 此事件的逻辑：收到消息，回复一句"你说的是："，
            // 然后再复读一遍你说的话。
            ContactMessageEvent { event ->
                val contact: Contact = event.source()
                contact.send("你说的是：")
                contact.send(event.messageContent)
            }
        }
        
        // 注册kook的bot
        kookBots {
            val bot = register("client_id", "token")
            // bot需要start才能连接服务器、初始化信息等。
            bot.start()
        }
    }.join() // join, 挂起直到被终止。
}
```



[m-api]: simbot-component-kook-api
[m-stdlib]: simbot-component-kook-stdlib
[m-core]: simbot-component-kook-core
[simbot3]: https://github.com/simple-robot/simpler-robot
