# Simple Robot Kook 组件

此为 [Simple Robot v3][simbot3] （以下简称为 `simbot3` ） 下基于simbot标准API对 [KOOK](https://www.kookapp.cn/) 的组件支持。

更多详情请参考 [simbot3][simbot3] 或者 [simbot3官网](https://simbot.forte.love)。


> ⚠️🔧 README 施工中。。。

## 支持情况

已经基本完成。

## 稳定程度

当前版本处于 **`ALPHA`** 版本阶段，即代表：
- 可能存在诸多[已知问题](https://github.com/simple-robot/simbot-component-kook/issues)和未知问题，
- 不保证API的稳定与兼容（可能随时发布存在不兼容内容的更新）
- 需要[**反馈**](https://github.com/simple-robot/simbot-component-kook/issues)或[**PR协助**](https://github.com/simple-robot/simbot-component-kook/pulls)

## 使用

<details open>
<summary>Gradle Kotlin DSL</summary>

**使用api模块**

```kotlin
implementation("love.forte.simbot.component:simbot-component-kook-api:$CP_KOOK_VERSION")
```

**使用stdlib模块**

```kotlin
implementation("love.forte.simbot.component:simbot-component-kook-stdlib:$CP_KOOK_VERSION")
```

**使用core模块**

```kotlin
implementation("love.forte.simbot:simbot-core:$SIMBOT_VERSION") // 必须显式引用simbot核心库（或其他衍生库，比如spring boot starter）
implementation("love.forte.simbot.component:simbot-component-kook-core:$CP_KOOK_VERSION")
```

</details>

<details>
<summary>Gradle Groovy</summary>

**使用api模块**

```groovy
implementation 'love.forte.simbot.component:simbot-component-kook-api:$CP_KOOK_VERSION'
```

**使用stdlib模块**

```groovy
implementation 'love.forte.simbot.component:simbot-component-kook-stdlib:$CP_KOOK_VERSION'
```

**使用core模块**

```groovy
implementation 'love.forte.simbot:simbot-core:$SIMBOT_VERSION' // 必须显式引用simbot核心库（或其他衍生库，比如spring boot starter）
implementation 'love.forte.simbot.component:simbot-component-kook-core:$CP_KOOK_VERSION'
```

</details>

<details>
<summary>Maven</summary>

**使用api模块**

```xml
<dependency>
    <groupId>love.forte.simbot.component</groupId>
    <artifactId>simbot-component-kook-api</artifactId>
    <version>${CP_KOOK_VERSION}</version>
</dependency>
```

**使用stdlib模块**

```xml
<dependency>
    <groupId>love.forte.simbot.component</groupId>
    <artifactId>simbot-component-kook-stdlib</artifactId>
    <version>${CP_KOOK_VERSION}</version>
</dependency>
```

**使用core模块**

```xml
<!-- 必须显式引用simbot核心库（或其他衍生库，比如spring boot starter） -->
<dependency>
    <groupId>love.forte.simbot</groupId>
    <artifactId>simbot-core</artifactId>
    <version>${CP_KOOK_VERSION}</version>
</dependency>
<dependency>
    <groupId>love.forte.simbot.component</groupId>
    <artifactId>simbot-component-kook-core</artifactId>
    <version>${CP_KOOK_VERSION}</version>
</dependency>
```

</details>


> simbot版本前往 [simbot3核心库][simbot3] 参考


## 文档

基本的核心API都由 [simbot3核心库][simbot3] 定义并提供。

KDoc (APIDoc) 可以前往 [文档引导](https://docs.simbot.forte.love) 处前往并查看 [KOOK文档](https://docs.simbot.forte.love/components/kook) 。

## 走马观花

<details>
<summary>简单示例</summary>


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
