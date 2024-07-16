# Bot配置文件

<include from="snippets.md" element-id="to-main-doc" />

<tldr>
<p>在使用 <b>Spring Boot</b> 时自动注册 bot 所需的配置文件。</p>
</tldr>

<tip>

如果你在使用 Spring Boot，
将配置文件放在在你的资源目录中:
<path>resources/simbot-bots/</path>，
并以 `.bot.json` 作为扩展名，例如 `mybot.bot.json`。

这个扫描目录是可配置的。
这是属于 simbot4 Spring Boot starter 的配置，可参考
[simbot手册: 使用 Spring Boot 3](https://simbot.forte.love/start-use-spring-boot-3.html)。

</tip>

## 示例

```json
{
    "component": "simbot.kook",
    "ticket": {
      "clientId": "client ID",
      "token": "ws token"
    }
}
```
{collapsible="true" default-state="expanded" collapsed-title="简单示例"}

```json
{
  "component": "simbot.kook",
  "ticket": {
    "clientId": "client ID",
    "token": "ws token"
  },
  "config": {
    "isCompress": true,
    "syncPeriods": {
      "guild": {
        "syncPeriod": 180000,
        "batchDelay": 0
      },
      "clientEngineConfig": {
        "threadsCount": null,
        "pipelining": null
      },
      "wsEngineConfig": {
        "threadsCount": null,
        "pipelining": null
      },
      "timeout": {
        "connectTimeoutMillis": 5000,
        "requestTimeoutMillis": 5000,
        "socketTimeoutMillis": null
      },
      "wsConnectTimeout": null,
      "isNormalEventProcessAsync": null
    }
  }
}
```
{collapsible="true" default-state="collapsed" collapsed-title="完整示例"}

## 属性描述

<deflist>
<def title="component">


固定值 `simbot.kook`，**必填**，代表此配置文件为KOOK组件的。

</def>
<def title="ticket">

对 bot 身份进行校验、访问 KOOK API 以及连接KOOK服务器进行事件订阅时所需的 bot 票据信息。

<note title="在哪儿?">

可以在 [KOOK开发者平台-应用](https://developer.kookapp.cn/app/index) 中查看。

</note>

<deflist>
<def title="clientId">

BOT的 `Client ID`。

</def>
<def title="token">

BOT使用 **websocket** 模式进行连接的 `token` .

</def>
</deflist>

</def>
<def title="config">


其他配置，可选。

<deflist>
<def title="isCompress">


是否压缩数据。默认为 `true`。

> 参考 [Gateway API](https://developer.kookapp.cn/doc/http/gateway) 中的 `compress` 参数。

</def>
<def title="syncPeriods">


缓存对象信息的同步周期配置。

```json
{
  "config": {
    "syncPeriods": {
      "guild": {
        "syncPeriod": 180000,
        "batchDelay": 0
      }
    }
  }
}
```

<tip>

你也可以尝试直接**禁用定时同步**来观察数据是否会出现差错。

```json
{
  "config": {
    "syncPeriods": {
      "guild": {
        "syncPeriod": 0,
        "batchDelay": 0
      }
    }
  }
}
```

> _将 `syncPeriod` 设置为 `0` 即可关闭_

在预期中，仅通过事件的通知就应满足对内部缓存的同步更新。因此我们希望可以在完全禁用定时同步的情况下依旧可以保证缓存数据的准确性。
但是目前测试或反馈的数据仍然不足，我们无法完全预判禁用定时同步可能造成的后果或如果因此而产生缓存数据不准确的可能原因。

因此我们希望你在可控范围内更多的尝试**禁用定时同步**并在出现问题时及时[**反馈**](https://github.com/simple-robot/simpler-robot/issues/new/choose)，
这可以帮助我们完善内部的缓存机制。

感谢您的支持与贡献！

</tip>

<deflist>
<def title="guild">

对频道服务器进行同步的周期信息配置，单位毫秒。

</def>
<def title="guild.syncPeriod">

对频道服务器进行同步的周期，单位毫秒，大于`0`时有效。目前服务器同步的同时会去同步此服务器下的所有频道列表与成员列表。

默认为 `180000`，即 `180000毫秒 -> 180秒 -> 3分钟`。

进行配置的时候需要注意考虑调用频率上限等相关问题。

</def>
<def title="guild.batchDelay">

同步数据是分页分批次的同步。`batchDelay` 配置每批次后进行挂起等待的时间，单位毫秒。
可以通过调大此参数来减缓 API 的请求速率, 默认不等待。

配置此属性可一定程度上降低触发调用频率限制的风险。

<tip>

一拍脑瓜儿随便写的。

</tip>

</def>
</deflist>

</def>
<def title="clientEngineConfig &amp; wsEngineConfig">


`clientEngineConfig` 和 `wsEngineConfig` 两个配置项类型相同，
顾名思义它们分别是针对 `API client` 和 `ws` 场景下使用的 `HttpClient` 实例的引擎（通用）配置项。

它们的配置项都与 Ktor 的 `HttpClientEngineConfig` 的配置相同，没有额外的含义。

<deflist>
<def title="threadsCount">

> Specifies network threads count advice.

更多参考 [Ktor文档](https://ktor.io/docs/http-client-engines.html#configure)

</def>
<def title="pipelining">

> Enables HTTP pipelining advice.

更多参考 [Ktor文档](https://ktor.io/docs/http-client-engines.html#configure)

</def>
</deflist>

</def>
<def title="timeout">

BOT内进行API请求时候的超时时间配置。(基于 [Ktor HttpTimeout](https://ktor.io/docs/timeout.html))

<tip>

当 `timeout` 本身为null时，不会覆盖原本的默认配置。但如果 `timeout` 不为null，则会直接使用此对象内信息直接完整覆盖。

例如：

```json
{
  "config": {
    "timeout": null
  }
}
```

此时，`connectTimeoutMillis` 和 `requestTimeoutMillis` 都是默认的 `5000`，
而如果配置是：

```json
{
  "config": {
    "timeout": { }
  }
}
```

则所有属性都会为 `null`。

</tip>
<deflist>

<def title="connectTimeoutMillis">


> a time period required to process an HTTP call: from sending a request to receiving a response.

更多参考 [Ktor HttpTimeout](https://ktor.io/docs/timeout.html#configure_plugin)

</def>
<def title="requestTimeoutMillis">


> a time period in which a client should establish a connection with a server.

更多参考 [Ktor HttpTimeout](https://ktor.io/docs/timeout.html#configure_plugin)

</def>
<def title="socketTimeoutMillis">


> a maximum time of inactivity between two data packets when exchanging data with a server.

更多参考 [Ktor HttpTimeout](https://ktor.io/docs/timeout.html#configure_plugin)

</def>
</deflist>

</def>
<def title="wsConnectTimeout">


ws连接超时时间，单位 `ms` 。默认为 `6000` 毫秒。

</def>
<def title="isNormalEventProcessAsync">


`ProcessorType.NORMAL` 类型的事件处理器是否在异步中执行。默认为 `true`。
当为 `false` 时, `NORMAL` 的表现效果将会与 `PREPARE` 基本类似。

<tip>

如果你不打算直接操作原始的 `Bot` 对象来注册一些原始的监听函数，
此配置项对你来说可能就没有太大的作用。

</tip>


</def>
</deflist>
</def>
</deflist>
