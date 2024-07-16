---
switcher-label: Java API 风格
---
# 消息发送

<include from="snippets.md" element-id="to-main-doc" />

在 KOOK 组件中，主要有两种发送消息的方式。

<include from="snippets.md" element-id="need-help"/>


<list type="decimal">
<li>直接构建并使用 API 发送消息。这是最原始的消息发送方式。</li>
<li>

在使用 simbot 核心库时，配合使用 **消息元素 `Message.Element`** 发送消息。

</li>
</list>

本章将主要介绍第 **1** 种方式: 使用 API 发送消息。而与**消息元素**相关的内容可前往参考 
<a href="message-element.md"/>。

## 使用 API

KOOK API 中用于发送消息的 API 主要就是 **向子频道发送消息** 和 **向用户发送私聊消息**。
它们的 API 封装分别为：

- `SendChannelMessageApi`: [发送频道聊天消息](https://developer.kookapp.cn/doc/http/message#发送频道聊天消息)
- `SendDirectMessageApi`:  [发送私信聊天消息](https://developer.kookapp.cn/doc/http/direct-message#发送私信聊天消息)

<note>

它们可以在
<a href="api-list.md" />
中找到。

</note>

这两个 API 的大致使用方式很类似，因此此处只选其一 `SendChannelMessageApi` 作为实例。

### 仅在API模块使用  {id="only_api"}

与在 
<a href="use-api.md" />
中的示例类似，
当你不依赖其他模块，仅依赖 **_API 模块 `simbot-component-kook-api`_** 时，
你可以使用比较贴合原始的方式直接使用 API。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

TODO

```Kotlin
// TODO
```

</tab>
<tab title="Java" group-key="Java">

TODO

```Java
// TODO
```

</tab>
</tabs>

### 在标准库中使用 {id="use_in_stdlib"}

当你依赖使用 **_标准库模块 `simbot-component-kook-stdlib`_** 时，
标准库提供的 `Bot` 中基本已经包含了请求 API 所需要的基本信息，
因此其会提供一些扩展/辅助方法来简化你的请求逻辑。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

TODO

```Kotlin
// TODO
```

</tab>
<tab title="Java" group-key="Java">

TODO

```Java
// TODO
```

</tab>
</tabs>

### 在组件库配合simbot4核心库时使用

<warning>

虽然在使用组件库配合simbot4核心库时，我们更建议你使用 **消息元素** (
可参考
<a href="message-element.md" />
)，而不是直接使用原始的API类型发送消息，但凡事都有例外。

**如果你明确确定此时必须要使用原始的API请求**，继续阅读即可。

</warning>

其实在组件库中，`KookBot` 类型可以直接提供它内部包含的标准库 `Bot`，
因此你可以获取到 `KookBot.sourceBot` 后，直接用 [标准库的方式](#use_in_stdlib) 进行请求。

<tabs>
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val kookBot: KookBot = ...
val bot = kookBot.sourceBot // 得到标准库的 Bot
```

</tab>
<tab title="Java" group-key="Java">

```Java
KookBot kookBot = ...;
Bot bot = kookBot.getSourceBot(); // 得到标准库的 Bot
```

</tab>
</tabs>
