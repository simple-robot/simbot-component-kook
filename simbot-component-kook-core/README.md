# Module simbot-component-kook-core

基于stdlib模块，通过stdlib模块对[simbot3](https://github.com/simple-robot/simpler-robot)
内容的封装，是simbot3的 **KOOK组件** (simbot-component-kook) 。

通过core模块你可以使用 simbot3 风格的API进行快速开发，并与其他支持的组件进行协同。
core模块实现 `simbot-api` 和 `simbot-core` 中绝大多数（可以被支持的）功能，包括事件的实现和功能性API等。

如果你希望使用拥有更高封装性的API或与其他simbot组件协同，又或是与Spring Boot整合，那么core模块可以是一种参考。

# Package love.forte.simbot.component.kook.role

与角色相关的内容，例如频道服务器的角色配置或某用户拥有的角色信息。

```
KookRole

KookGuildRole
KookMemberRole
```

