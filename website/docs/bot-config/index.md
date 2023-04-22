---
title: BOT配置文件
---

:::caution 待施工

待施工

:::

```json title='xxx.bot.json'
{
  "component": "simbot.kook",
  "clientId": "Your client ID",
  "token": "Your ws token",
  "config": {
    "isCompress": true,
    "syncPeriods": {
      "guildSyncPeriod": 60000,
      "memberSyncPeriods": 60000
    }
  }
}
```

### `component`

固定值 `simbot.kook`，**必填**，代表此配置文件为KOOK组件的。

### `clientId`

BOT的 **`Client ID`**。

### `token`

BOT使用 **websocket** 模式进行连接的 **`Token`** .

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
      "guildSyncPeriod": 180000,
      "batchDelay": 0
    }
  }
}
```

**`guildSyncPeriod`**

对频道服务器进行同步的周期，单位毫秒，**大于`0`时生效**。服务器同步的同时会去同步此服务器下的所有频道列表与成员列表。

默认为 `180000`，即 `180000毫秒 -> 180秒 -> 3分钟`。

进行配置的时候需要注意调用频率上限等相关问题。


**`batchDelay`**

当 `guildSyncPeriod` 生效时，在数据同步的过程中每一次查询（即批次）后挂起等待的时间，单位毫秒。

默认为 `0`，即不等待。

通过配置此属性可一定程度上降低触发调用频率限制的风险。

<br/>

<details><summary>默认值的由来?</summary>

一拍脑瓜儿随便写的。

</details>
