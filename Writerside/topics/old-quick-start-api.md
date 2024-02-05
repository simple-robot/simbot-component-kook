---
title: 使用API
sidebar_position: 2
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import CodeBlock from '@theme/CodeBlock';
import {version} from '@site/static/version.json'

API模块基于 **KMP** 构建项目，支持 **JVM、JS、Native** 平台，使用 [**Ktor**](https://ktor.io/) 作为API请求（http请求）的解决方案。

API模块是**独立的**，实质上并不依赖 simbot API。你可以单独使用它作为 [KOOK API](https://developer.kookapp.cn/doc/) 的底层封装库。

## 安装

**1. 安装 kook-api 依赖**

<tabs group="use-dependency">
<tab title="Gradle Kotlin DSL" group-key="Kts">

`Gradle` 的 [`Kotlin` 插件](https://kotlinlang.org/docs/gradle-configure-project.html#apply-the-plugin)：

```kotlin
// 使用 Gradle 的 kotlin 插件来允许自动选择依赖的对应平台，比如JVM或JS等。
plugin {
    kotlin("jvm") version "合适且较新的版本"
    // 或 multiplatform, 如果你使用多平台的话
    // 如果你使用 Java，也最好添加此插件，因此它可以帮助你自动选择 -jvm 的依赖，而不需要主动添加此后缀
}
```

依赖：

```kotlin
implementation("love.forte.simbot.component:simbot-component-kook-api:${version}") // 版本参考下文所述的 Releases
```

</tab>
<tab title="Gradle Groovy" group-key="Gradle">

`Gradle` 的 [`Kotlin` 插件](https://kotlinlang.org/docs/gradle-configure-project.html#apply-the-plugin)：

```gradle
// 使用 Gradle 的 kotlin 插件来允许自动选择依赖的对应平台，比如JVM或JS等。
plugin {
    id 'org.jetbrains.kotlin.jvm' version '合适且较新的版本'
    // 或 org.jetbrains.kotlin.multiplatform, 如果你使用多平台的话
    // 如果你使用 Java，也最好添加此插件，因此它可以帮助你自动选择 -jvm 的依赖，而不需要主动添加此后缀
}
```

依赖：

```gradle
implementation 'love.forte.simbot.component:simbot-component-kook-api:${version}' // 版本参考下文所述的 Releases
```

</tab>
<tab title="Maven" group-key="Maven">

```xml
<dependency>
    <groupId>love.forte.simbot.component</groupId>
    <!-- 在Maven中使用 '-jvm' 后缀来选择使用JVM平台库 -->
    <artifactId>simbot-component-kook-api-jvm</artifactId>
    <!-- 版本参考下文所述的 Releases -->
    <version>${version}</version>
</dependency>
```

</tab>
</Tabs>

:::info 版本参考

版本可前往 [**Releases**](https://github.com/simple-robot/simbot-component-kook/releases) 查看并选择。

:::


**2. 选择并安装合适的 Ktor Client 依赖**

前往 [Ktor: HTTP client Engines](https://ktor.io/docs/http-client-engines.html) 选择并使用一个合适的 HTTP Client 引擎。

例如：

<tabs group="use-dependency">
<tab title="Gradle Kotlin DSL" group-key="Kts" label="Kotlin/JVM">

以 [`CIO`](https://ktor.io/docs/http-client-engines.html#cio) 引擎为例：

```kotlin
// 或使用 runtimeOnly
implementation("io.ktor:ktor-client-cio:<合适且较新的Ktor版本>")
```

或者如果 Java 版本 `>= Java11`, 使用 [`Java`](https://ktor.io/docs/http-client-engines.html#java) 引擎：

```kotlin
// 或使用 runtimeOnly
implementation("io.ktor:ktor-client-java:<合适且较新的Ktor版本>")
```

</tab>
<tab title="Kotlin/JS" label="Kotlin/JS">

以 [`JS`](https://ktor.io/docs/http-client-engines.html#js) 引擎为例：

```kotlin
implementation("io.ktor:ktor-client-js:<合适且较新的Ktor版本>")
```


</tab>
<tab title="Kotlin/N" label="Kotlin/Native">

<tabs group="use-dependency-kt-native">
<tab title="WinHttp" label="WinHttp">

> see [`WinHttp`](https://ktor.io/docs/http-client-engines.html#winhttp)

```kotlin
implementation("io.ktor:ktor-client-winhttp:<合适且较新的Ktor版本>")
```

</tab>
<tab title="Darwin" label="Darwin">

> see [`Darwin`](https://ktor.io/docs/http-client-engines.html#darwin)

```kotlin
implementation("io.ktor:ktor-client-darwin:<合适且较新的Ktor版本>")
```

</tab>
<tab title="Curl" label="Curl">

> see [`Curl`](https://ktor.io/docs/http-client-engines.html#curl)

```kotlin
implementation("io.ktor:ktor-client-curl:<合适且较新的Ktor版本>")
```

</tab>
</Tabs>

</tab>
<tab title="Maven" group-key="Maven">

以 [`CIO`](https://ktor.io/docs/http-client-engines.html#cio) 引擎为例：

```xml

<dependency>
    <groupId>io.ktor</groupId>
    <artifactId>ktor-client-cio-jvm</artifactId>
    <version>合适且较新的Ktor版本</version>
    <!-- 在JVM平台下，如果只有一个引擎依赖，则默认会尝试通过 SPI 加载，因此可以使用 runtime 作用域 -->
    <!-- 如果想要手动指定引擎或配置，移除此作用域配置 -->
    <scope>runtime</scope>
</dependency>
```

或者如果 Java 版本 `>= Java11`, 使用 [`Java`](https://ktor.io/docs/http-client-engines.html#java) 引擎：

```xml
<dependency>
    <groupId>io.ktor</groupId>
    <artifactId>ktor-client-java</artifactId>
    <version>合适且较新的Ktor版本</version>
    <!-- 在JVM平台下，如果只有一个引擎依赖，则默认会尝试通过 SPI 加载，因此可以使用 runtime 作用域 -->
    <!-- 如果想要手动指定引擎或配置，移除此作用域配置 -->
    <scope>runtime</scope>
</dependency>
```

</tab>
</Tabs>

## 使用

:::tip 太多了

我们不会在此处一一列举所有的API做演示，这不太现实。
所有的API都在包路径 `love.forte.simbot.kook.api` 下，你可以通过 [API文档](https://docs.simbot.forte.love/)
或查阅源码的方式来寻找你所需要的API。

API包装类的命名也存在一定的规律，比如一个 `获取xxx列表` 的API通常会被命名为 `GetXxxListApi`。
它们的命名大部分与其对应的实际API地址有很大关系。

下文会选择一小部分API来做示例。

:::

### 获取用户频道服务器列表

以 [获取当前用户(BOT)加入的服务器列表](https://developer.kookapp.cn/doc/http/guild#获取当前用户加入的服务器列表)
为例。

<tabs group="code">
<tab title="Kotlin" label="Kotlin/JVM">

```kotlin
// 用于请求的 Ktor HttpClient，如有必要则需要自行引入并选择需要使用的引擎。
// 参考：https://ktor.io/docs/http-client-engines.html
val client = HttpClient(CIO) {
    // config...
}

// 鉴权信息
// 'Bot' 后面跟的是bot的token，参考 https://developer.kookapp.cn/doc/reference
val authorization = "Bot xxxxxxxxxx"

// 构建要请求的API，大部分API都有一些可选或必须的参数。
val api = GetGuildListApi.create()

// request data 会检测result，然后将真正的data结果返回 (或在验证失败的情况下抛出异常)
val guildListData: ListData<SimpleGuild> = api.requestData(client, authorization)

guildListData.items.forEach { println("guild: $it") }
// 也可以直接 guildListData.forEach { ... }
```

</tab>
<tab title="Kotlin/JS">

```kotlin
// 用于请求的 Ktor HttpClient，如有必要则需要自行引入并选择需要使用的引擎。
// 参考：https://ktor.io/docs/http-client-engines.html
val client = HttpClient(Js) {
    // config...
}

// 鉴权信息
// 'Bot' 后面跟的是bot的token，参考 https://developer.kookapp.cn/doc/reference
val authorization = "Bot xxxxxxxxxx"

// 构建要请求的API，大部分API都有一些可选或必须的参数。
val api = GetGuildListApi.create()

// request data 会检测result，然后将真正的data结果返回 (或在验证失败的情况下抛出异常)
val guildListData: ListData<SimpleGuild> = api.requestData(client, authorization)

guildListData.items.forEach { println("guild: $it") }
// 也可以直接 guildListData.forEach { ... }
```

</tab>
<tab title="Kotlin/Native">

> 此处以 `MingwX64` 平台环境为例：

```kotlin
// 用于请求的 Ktor HttpClient，如有必要则需要自行引入并选择需要使用的引擎。
// 参考：https://ktor.io/docs/http-client-engines.html
val client = HttpClient(WinHttp) {
    // config...
}

// 鉴权信息
// 'Bot' 后面跟的是bot的token，参考 https://developer.kookapp.cn/doc/reference
val authorization = "Bot xxxxxxxxxx"

// 构建要请求的API，大部分API都有一些可选或必须的参数。
val api = GetGuildListApi.create()

// request data 会检测result，然后将真正的data结果返回 (或在验证失败的情况下抛出异常)
val guildListData: ListData<SimpleGuild> = api.requestData(client, authorization)

guildListData.items.forEach { println("guild: $it") }
// 也可以直接 guildListData.forEach { ... }
```

</tab>
<tab title="Java">

```java
// 在Java中构建或获取一个 Ktor 的 HttpClient。
// 用于请求的 Ktor HttpClient，如有必要则需要自行引入并选择需要使用的引擎。
// 参考：https://ktor.io/docs/http-client-engines.html
// 此处以 ktor-cio 引擎为例。
var client = HttpClientKt.HttpClient(CIO.INSTANCE, config -> {
            // config...
            return Unit.INSTANCE;
        });

// 鉴权信息
// 'Bot' 后面跟的是bot的token，参考 https://developer.kookapp.cn/doc/reference
var authorization = "Bot xxxxxxxxxx";

// 构建要请求的API，大部分API都有一些可选或必须的参数。
var api = GetGuildListApi.create();

// requestDataBlocking 会检测result，然后将真正的data结果返回 (或在验证失败的情况下抛出异常)
var guildListData = api.requestDataBlocking(client, authorization);
for (var guild : guildListData) {
    System.out.println(guild);
}
```

</tab>
<tab title="Java Async">

```java
// 在Java中构建或获取一个 Ktor 的 HttpClient。
// 用于请求的 Ktor HttpClient，如有必要则需要自行引入并选择需要使用的引擎。
// 参考：https://ktor.io/docs/http-client-engines.html
// 此处以 ktor-cio 引擎为例。
var client = HttpClientKt.HttpClient(CIO.INSTANCE, config -> {
            // config...
            return Unit.INSTANCE;
        });

// 鉴权信息
// 'Bot' 后面跟的是bot的token，参考 https://developer.kookapp.cn/doc/reference
var authorization = "Bot xxxxxxxxxx";

// 构建要请求的API，大部分API都有一些可选或必须的参数。
var api = GetGuildListApi.create();

// requestDataAsync 会检测result，然后将真正的data结果返回 (或在验证失败的情况下抛出异常)
CompletableFuture<ListData<SimpleGuild>> guildListDataFuture = api.requestDataAsync(client, authorization);

// Use the future, or handle exception
guildListDataFuture.thenAccept(listData -> {
    for (var guild : listData) {
        System.out.println(guild);
    }
}).exceptionally(err -> {
    logger.error("err!", err);
    return null;
});
```

</tab>
</Tabs>

