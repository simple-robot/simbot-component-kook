# Simple Robot - KOOK 组件

此为 [Simple Robot v3][simbot3] （以下简称为 `simbot3` ） 下基于simbot标准API对 [KOOK API](https://www.kookapp.cn/) 的组件支持。

**Simple Robot - KOOK 组件** <small>以下简称KOOK组件</small> 
是一个基于 [KMP](https://kotlinlang.org/docs/multiplatform.html)、
支持多平台（`JVM`、`JS`、native）且JVM平台库对Java友好、
（特定模块）实现simbot3标准API的 **KOOK 机器人API** 依赖库。

**KOOK组件**提供多平台的KOOK API、bot事件订阅等功能实现的底层库，
以及基于simbot3标准API的高级功能应用库。

## 文档

了解**simbot3**: [simbot3官网](https://simbot.forte.love)

KOOK组件的**组件手册**: [组件手册][website]

> [!note]
> 手册与simbot3官网均部署于GitHub Pages。
> 为了更好的浏览体验，**推荐**开启魔法后访问。

**API文档**: [API文档引导站](https://docs.simbot.forte.love)

## 支持情况

前往查看 [支持列表](support-list.md)。

## 快速开始

前往 [组件手册][website] 阅读 **快速开始** 相关章节。

## 走马观花

<details>
<summary>简单示例</summary>

> [!note]
> Java开发者可直接参考 [组件手册][website] 中**快速开始**相关示例的Java部分代码。

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
> (使用 Kotlin + `simbot-component-kook-core`, 非 Spring Boot 的情况下：)

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

## 应用案例

如果你想看看通过 simbot 开发的bot是什么样子，不妨添加我们亲爱的 [法欧莉斯卡雷特](https://www.kookapp.cn/app/oauth2/authorize?id=10250&permissions=197958144&client_id=jqdlyHK85xe1i5Bo&redirect_uri=&scope=bot) 并使用 `@法欧莉 今天的我` 来看看效果吧~


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

[website]: https://component-kook.simbot.forte.love/
