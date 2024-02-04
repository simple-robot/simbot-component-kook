---
switcher-label: Java API 风格
---

<var name="jr" value="Reactor"/>

# 使用 API

<tldr>
<p>本章节介绍如何使用 <control>API 模块</control> 来构建、请求一个KOOK的API。</p>
</tldr>

<tip>

<control>API 模块</control> 是一个“基础”的模块，它仅提供针对 KOOK API 的封装，
没有 Bot、事件处理等功能，是一种“底层库”。

API 模块无法直接作为 Simple Robot 组件使用。

</tip>

<procedure collapsible="true" default-state="collapsed" title="适用场景">

当你不确定自己的应用场景是否应该选择 **直接使用** API 模块时，
这里为你提供了一些参考：

<procedure title="适用">

- 你的应用只需要一个 API 实现库，你希望使用更**原始的**风格调用API。
- 你的应用**不需要**、或希望**自行处理**对事件的订阅与解析。（包括一切工作，例如建立网络连接）

</procedure>
<procedure title="不适用">

- 你希望库实现事件订阅能力，可以替你处理网络连接、事件调度等。
- 你希望有一个 Bot 实现，它可以管理网络连接的生命周期、可以注册事件处理逻辑、替你接收事件，并进行处理。

</procedure>
</procedure>

## 安装

<tabs group="build">
<tab title="Gradle(Kotlin DSL)" group-key="kts">

```Kotlin
implementation("love.forte.simbot.component:simbot-component-kook-api:%version%")
```

<tip>

如果你使用 Java 而不配合使用 Gradle 的 `kotlin` 插件,
那么你需要指定依赖的后缀为 `-jvm`。

```Kotlin
implementation("love.forte.simbot.component:simbot-component-kook-api-jvm:%version%")
```

</tip>

</tab>
<tab title="Gradle(Groovy)" group-key="groovy">

```Groovy
implementation 'love.forte.simbot.component:simbot-component-kook-api:%version%'
```

<tip>

如果你使用 Java 而不配合使用 Gradle 的 `kotlin` 插件,
那么你需要指定依赖的后缀为 `-jvm`。

```Groovy
implementation 'love.forte.simbot.component:simbot-component-kook-api-jvm:%version%'
```

</tip>

</tab>
<tab title="Maven" group-key="maven">

```xml
<dependency>
    <groupId>love.forte.simbot.component</groupId>
    <!-- Maven 需要添加 `-jvm` 后缀来选择使用 JVM 平台 -->
    <artifactId>simbot-component-kook-api-jvm</artifactId>
    <version>%version%</version>
</dependency>
```

</tab>
</tabs>

### 引擎选择

<include from="snippets.md" element-id="engine-choose" />

## 使用

KOOK组件的API模块提供了针对
[KOOK API](https://developer.kookapp.cn/doc/reference)
的基本对应封装。

API封装的命名与API具有一定关联，例如 [获取当前用户加入的服务器列表](https://developer.kookapp.cn/doc/http/guild#获取当前用户加入的服务器列表)：

<compare first-title="API" second-title="API封装" type="top-bottom">

```HTTP
GET /api/v3/guild/list
```

```
love.forte.simbot.kook.api.guild.GetGuildListApi
```
</compare>

> 所有的API实现均在包路径 `love.forte.simbot.kook.api` 中。

API的应用大差不差，因此此处仅使用部分类型作为示例，
不会演示所有API。
如果想浏览或寻找需要的 API，可前往 [APIDoc引导](https://docs.simbot.forte.love)
中进入KOOK组件的 KDoc 查阅，或可以简单的借助IDE的智能提示进行寻找。

以 [获取当前用户加入的服务器列表](https://developer.kookapp.cn/doc/http/guild#获取当前用户加入的服务器列表) 为例。

<tabs group="Code">
<tab title="Kotlin" group-key="Kotlin">

```kotlin
// 准备参数
// 用于请求的 authorization，參考 [[[KOOK开发者文档|https://developer.kookapp.cn/doc/reference]]]
val authorization = "Bot 1111222233334444"
// 用于请求的 Ktor HttpClient，如有必要则需要自行引入并选择需要使用的引擎。
// 参考：[[[Ktor Engines|https://ktor.io/docs/http-client-engines.html]]]
val client = HttpClient()

// 使用 GetGuildListApi 获取频道列表
// 创建了一个参数 page = 1, pageSize = 20 的 GetGuildListApi，并使用上述准备好的参数进行请求。
val api = GetGuildListApi.create(page = 1, pageSize = 20)
val resultList = api.requestData(client, authorization)

resultList.forEach { // it: SimpleGuild
    ...
}
```

也可以通过额外的扩展函数来获得一个**全量数据**的数据流。

```kotlin
// 准备参数
// 用于请求的 authorization，參考 [[[KOOK开发者文档|https://developer.kookapp.cn/doc/reference]]]
val authorization = "Bot 1111222233334444"
// 用于请求的 Ktor HttpClient，如有必要则需要自行引入并选择需要使用的引擎。
// 参考：[[[Ktor Engines|https://ktor.io/docs/http-client-engines.html]]]
val client = HttpClient()

// 使用 GetGuildListApi 获取频道列表
// 创建了一个基于 GetGuildListApi 获取全量数据的流，并使用上述准备好的参数进行请求。
val resultFlow = GetGuildListApi.createItemFlow { page ->
    create(page = page, pageSize = 20).requestData(client, authorization)
}
resultFlow.collect { // it: SimpleGuild
    println(it)
}
```

</tab>
<tab title="Java" group-key="Java">

```java
// 准备参数
// 用于请求的 authorization，參考 [[[KOOK开发者文档|https://developer.kookapp.cn/doc/reference]]]
var authorization = "Bot 1111222233334444";
// 用于请求的 Ktor HttpClient，如有必要则需要自行引入并选择需要使用的引擎。
// 参考：https://ktor.io/docs/http-client-engines.html
var client = HttpClientJvmKt.HttpClient(($1) -> Unit.INSTANCE);
// 或者通过jvm平台库提供的工具类来构建一个默认的 client。（需要环境中存在一种引擎）
var newClient = ApiRequests.createHttpClient();

// 使用 GetGuildListApi 获取频道列表,
// 创建了一个 page = 1, pageSize = 20 的 GetGuildListApi
var api = GetGuildListApi.create(1, 20);

ApiRequests.requestDataAsync(api, client, authorization)
        .thenAccept(guildList -> {
            for (var guild : guildList) {
                // ...
            }
        });
```
{switcher-key="%ja%"}

```java
// 准备参数
// 用于请求的 authorization，參考 [[[KOOK开发者文档|https://developer.kookapp.cn/doc/reference]]]
var authorization = "Bot 1111222233334444";
// 用于请求的 Ktor HttpClient，如有必要则需要自行引入并选择需要使用的引擎。
// 参考：https://ktor.io/docs/http-client-engines.html
var client = HttpClientJvmKt.HttpClient(($1) -> Unit.INSTANCE);
// 或者通过jvm平台库提供的工具类来构建一个默认的 client。（需要环境中存在一种引擎）
var newClient = ApiRequests.createHttpClient();

// 使用 GetGuildListApi 获取频道列表,
// 创建了一个 page = 1, pageSize = 20 的 GetGuildListApi
var api = GetGuildListApi.create(1, 20);

// 发起请求并得到结果
var guildList = ApiRequests.requestDataBlocking(api, client, authorization);

for (var guild : guildList) {
    // ...
}
```
{switcher-key="%jb%"}


```java
// 准备参数
// 用于请求的 authorization，參考 [[[KOOK开发者文档|https://developer.kookapp.cn/doc/reference]]]
var authorization = "Bot 1111222233334444";
// 用于请求的 Ktor HttpClient，如有必要则需要自行引入并选择需要使用的引擎。
// 参考：https://ktor.io/docs/http-client-engines.html
var client = HttpClientJvmKt.HttpClient(($1) -> Unit.INSTANCE);
// 或者通过jvm平台库提供的工具类来构建一个默认的 client。（需要环境中存在一种引擎）
var newClient = ApiRequests.createHttpClient();

// 使用 GetGuildListApi 获取频道列表,
// 创建了一个 page = 1, pageSize = 20 的 GetGuildListApi
var api = GetBotGuildListApi.create(1. 20);

// 发起请求并得到结果
ApiRequests.requestDataReserve(api, client, authorization)
        // 使用此转化器需要确保运行时环境中存在 
        // [[[kotlinx-coroutines-reactor|https://github.com/Kotlin/kotlinx.coroutines/tree/master/reactive]]] 的相关依赖。
        .transform(SuspendReserves.mono())
        .subscribe(guildList -> {
            for (var guild : guildList) {
                // ...
            }
        });
```
{switcher-key="%jr%"}

</tab>
</Tabs>

