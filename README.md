# Simple Robot Kook 组件

此为 [Simple Robot v3](https://github.com/ForteScarlet/simpler-robot) 下基于simbot标准API对 [Kook](https://www.kookapp.cn/) 的组件支持。

更多详情请参考 [Simple Robot v3 仓库](https://github.com/ForteScarlet/simpler-robot) 或者 [Simple Robot v3 官网](https://simbot.forte.love)


## 支持情况

已经基本完成。

> ⚠🔧 README 待施工中。。。



### 走马观花

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
suspend fun KookChannelMessageEvent.onEvent(name: String) {
    group.send(At(author.id) + "好的，以后就叫你$name了".toText())
}
```
