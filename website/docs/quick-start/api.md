---
title: 使用API
sidebar_position: 1
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import CodeBlock from '@theme/CodeBlock';
import {version} from '@site/static/version.json'

API模块是独立的，你可以单独使用它作为 [KOOK API](https://developer.kookapp.cn/doc/) 的封装库。

:::danger 全变了

API模块在 [**#82**](https://github.com/simple-robot/simbot-component-kook/issues/82) 的过程中会**大改**，
包括变更API接口的结果实现、事件类型的实现等，以及变更所有API的命名为 `GetXxxListApi` 风格等。

:::

## 安装

<Tabs groupId="use-dependency">
<TabItem value="Gradle Kotlin DSL">

<CodeBlock language="kotlin">{`
// 不要忘记使用 Gradle 的 kotlin 插件来允许自动选择对应平台，比如JVM或JS等。
implementation("love.forte.simbot.component:simbot-component-kook-api:${version}") // 版本参考下文所述的 Releases
`.trim()}</CodeBlock>

</TabItem>
<TabItem value="Gradle Groovy">

<CodeBlock language="gradle">{`
// 不要忘记使用 Gradle 的 kotlin 插件来允许自动选择对应平台，比如JVM或JS等。
implementation 'love.forte.simbot.component:simbot-component-kook-api:${version}' // 版本参考下文所述的 Releases
`.trim()}</CodeBlock>

</TabItem>
<TabItem value="Maven">

<CodeBlock language="xml">{`
<dependency>
    <groupId>love.forte.simbot.component</groupId>
    <!-- 在Maven中使用 '-jvm' 后缀来选择使用JVM平台库 -->
    <artifactId>simbot-component-kook-api</artifactId>
    <!-- 版本参考下文所述的 Releases -->
    <version>${version}</version>
</dependency>
`.trim()}</CodeBlock>

</TabItem>
</Tabs>


:::info 版本参考

版本可前往 [**Releases**](https://github.com/simple-robot/simbot-component-kook/releases) 查看并选择。

:::

## 使用

:::tip 太多了

我们不会在此处一一列举所有的API做演示，这不太现实。
所有的API都在包路径 `love.forte.simbot.kook.api` 下，你可以通过 [API文档](https://docs.simbot.forte.love/) 或查阅源码的方式来寻找你所需要的API。

API包装类的命名也存在一定的规律，比如一个 `获取某列表` 的API通常会被命名为 `XxxListRequest`。

下文会选择一小部分API来做示例。

:::

### 获取用户频道服务器列表

以 [获取当前用户(BOT)加入的服务器列表](https://developer.kookapp.cn/doc/http/guild#%E8%8E%B7%E5%8F%96%E5%BD%93%E5%89%8D%E7%94%A8%E6%88%B7%E5%8A%A0%E5%85%A5%E7%9A%84%E6%9C%8D%E5%8A%A1%E5%99%A8%E5%88%97%E8%A1%A8) 为例。

<Tabs groupId="code">
<TabItem value="Kotlin">

```kotlin
// 用于请求的 Ktor HttpClient，如有必要则需要自行引入并选择需要使用的引擎。
// 参考：https://ktor.io/docs/http-client-engines.html
val client = HttpClient(Java) {
    // config...
}

// 鉴权信息
// 'Bot' 后面跟的是bot的token，参考 https://developer.kookapp.cn/doc/reference
val authorization = "Bot xxxxxxxxxx"

// 构建要请求的API，大部分API都有一些可选或必须的参数。
val api = GuildListRequest.create()

// request data 会检测result，然后将真正的data结果返回 (或在验证失败的情况下抛出异常)
val guildListData = api.requestData(client, authorization)

guildListData.items.forEach { ... }
// 也可以直接 guildListData.forEach { ... }
```

</TabItem>
<TabItem value="Java">

```java
// 在Java中构建或获取一个 Ktor 的 HttpClient。
// 用于请求的 Ktor HttpClient，如有必要则需要自行引入并选择需要使用的引擎。
// 参考：https://ktor.io/docs/http-client-engines.html
// 此处以 ktor-java 引擎为例。
var client = HttpClientKt.HttpClient(Java.INSTANCE, config -> {
    // config...   
    return Unit.INSTANCE;
});

// 鉴权信息
// 'Bot' 后面跟的是bot的token，参考 https://developer.kookapp.cn/doc/reference
var authorization = "Bot xxxxxxxxxx";

// 构建要请求的API，大部分API都有一些可选或必须的参数。
var api = GuildListRequest.create();

// requestDataBlocking 会检测result，然后将真正的data结果返回 (或在验证失败的情况下抛出异常)
var guildListData = api.requestDataBlocking(client, authorization);

for (var guild : guildListData) {
    System.out.println(guild);
}
```

</TabItem>
</Tabs>

