# Simple Robot KOOK 组件

此为 [Simple Robot v3][simbot3] （以下简称为 `simbot3` ） 下基于simbot标准API对 [KOOK API](https://www.kookapp.cn/) 的组件支持。

## 文档

了解**simbot3**: [simbot3官网](https://simbot.forte.love)

KOOK组件的**组件手册**: [组件手册][website]

> 版本进入稳定后考虑配置域名，目前暂未配置。

**API文档**: [API文档引导站](https://docs.simbot.forte.love)

> **Warning**
> README 施工中。。。

## 支持情况

前往查看 [支持列表](support-list.md)。

## 快速开始

前往 [组件手册][website] 阅读 **快速开始** 相关章节。

## 走马观花

<details>
<summary>简单示例</summary>

> 使用 Kotlin + `simbot-component-kook-core` 配合 `simboot-core-spring-boot-starter` 使用 `Spring Boot` 的情况下：

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

</details>


## License

`simbot-component-kook` 使用 `LGPLv3` 许可证开源。

```
This program is free software: you can redistribute it and/or modify it under the terms of 
the GNU Lesser General Public License as published by the Free Software Foundation, either 
version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with this 
program. If not, see <https://www.gnu.org/licenses/>.
```

[m-api]: simbot-component-kook-api
[m-stdlib]: simbot-component-kook-stdlib
[m-core]: simbot-component-kook-core
[simbot3]: https://github.com/simple-robot/simpler-robot

[website]: https://simple-robot.github.io/simbot-component-kook
