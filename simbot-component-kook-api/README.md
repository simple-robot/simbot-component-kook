# Module simbot-component-kook-api

用于提供对KOOK中各内容（例如事件、API、对象等）的定义模块。此模块依赖 `simbot-api`，但仅最低限度的实现 `simbot-api` 中的部分类型，
不实现任何功能性内容（例如只实现 `id`、`username` 属性的获取，但是不考虑诸如 **消息发送** 等相关内容的实现 ）。

此模块定义封装KOOK中绝大多数的API（例如获取属性、发送消息等）供于其他模块或外界使用。
API的封装于定义基于 [Ktor（v2）](https://ktor.io/)，如果仅希望获得一些对API的基本封装，则可以考虑单独使用此模块。

## 日志

当使用API的时候，会在名称 `love.forte.simbot.kook.api` 中输出相关的DEBUG日志。

其中，部分日志中出现向右的箭头代表为 `request` 相关的日志，例如 
```
API[555] =======> xxx
```

向左的则为 `response` 相关的日志，例如

```
API[222] <======== xxx
```

其中，`API` 后面的方括号中是当前请求API的对象实例信息，例如哈希值等。
