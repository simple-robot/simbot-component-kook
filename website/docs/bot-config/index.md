---
title: BOT配置文件
---


```json title='xxx.bot.json'
{
  "component": "simbot.kook",
  "ticket": {
    "clientId": "Your client ID",
    "token": "Your ws token"
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

### `component`

固定值 `simbot.kook`，**必填**，代表此配置文件为KOOK组件的。

### `ticket`

对 bot 身份进行校验、访问 KOOK API 以及连接KOOK服务器进行事件订阅时所需的 bot 票据信息。

#### `ticket.clientId`

BOT的 `Client ID`。

#### `ticket.token`

BOT使用 **websocket** 模式进行连接的 `token` .

:::tip 在哪儿?

可以在 [KOOK开发者平台-应用](https://developer.kookapp.cn/app/index) 中查看。

:::

:::caution 后日谈

日后此类**票据信息**会整合到 `ticket` 字段内。

```json
{
  "component": "simbot.kook",
  "ticket": {
    "clientId": "Your client ID",
    "token": "Your ws token"
  }
}
```

:::

### `config`

其他配置，可选。

#### `config.isCompress`

是否压缩数据。默认为 `true`。

> 参考 [Gateway API](https://developer.kookapp.cn/doc/http/gateway) 中的 `compress` 参数。

#### `config.syncPeriods`

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

**`syncPeriods.guild`**

对频道服务器进行同步的周期信息配置，单位毫秒。

**`syncPeriod`**

对频道服务器进行同步的周期，单位毫秒。 目前服务器同步的同时会去同步此服务器下的所有频道列表与成员列表。

默认为 `180000`，即 `180000毫秒 -> 180秒 -> 3分钟`。

进行配置的时候需要注意考虑调用频率上限等相关问题。

**`batchDelay`**

同步数据是分页分批次的同步。`batchDelay` 配置每批次后进行挂起等待的时间，单位毫秒。
可以通过调大此参数来减缓api的请求速率, 默认不等待。

配置此属性可一定程度上降低触发调用频率限制的风险。


<br/>

<details><summary>默认值的由来?</summary>

一拍脑瓜儿随便写的。

</details>

#### `config.clientEngineConfig` & `config.wsEngineConfig`

`clientEngineConfig` 和 `wsEngineConfig` 两个配置项类型相同，顾名思义它们分别是针对 `API client` 和 `ws` 场景下使用的 `HttpClient` 实例的引擎（通用）配置项。

它们的配置项都与 Ktor 的 `HttpClientEngineConfig` 的配置相同，没有额外的含义。

**`threadsCount`**

> Specifies network threads count advice.

更多参考 [Ktor文档](https://ktor.io/docs/http-client-engines.html#configure)

**`pipelining`**

> Enables HTTP pipelining advice.

更多参考 [Ktor文档](https://ktor.io/docs/http-client-engines.html#configure)


#### `config.timeout`

BOT内进行API请求时候的超时时间配置。（基于 [Ktor HttpTimeout](https://ktor.io/docs/timeout.html)）

:::info

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
    "timeout": {
      
    }
  }
}
```

则所有属性都会为 `null`。

:::

**`connectTimeoutMillis`**

> a time period required to process an HTTP call: from sending a request to receiving a response.

更多参考 [Ktor HttpTimeout](https://ktor.io/docs/timeout.html#configure_plugin)

**`requestTimeoutMillis`**

> a time period in which a client should establish a connection with a server.

更多参考 [Ktor HttpTimeout](https://ktor.io/docs/timeout.html#configure_plugin)

**`socketTimeoutMillis`**

> a maximum time of inactivity between two data packets when exchanging data with a server.

更多参考 [Ktor HttpTimeout](https://ktor.io/docs/timeout.html#configure_plugin)


#### `config.wsConnectTimeout`

ws连接超时时间，单位 `ms` 。默认为 `6000` 毫秒。

#### `config.isNormalEventProcessAsync`

`ProcessorType.NORMAL` 类型的事件处理器是否在异步中执行。默认为 `true`。
当为 `false` 时, `NORMAL` 的表现效果将会与 `PREPARE` 基本类似。

:::note

如果你不打算直接操作原始的 `Bot` 对象来注册一些原始的监听函数，
此配置项对你来说可能就没有太大的作用。

:::
