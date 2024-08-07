---
switcher-label: Java API 风格
---

# 使用标准库

<include from="snippets.md" element-id="to-main-doc" />

<tldr>
<p>本章节介绍如何使用 <control>标准库(stdlib模块)</control> 来构建 Bot 实例、订阅并处理事件。</p>
</tldr>

<tip>

<control>标准库模块</control> 是一个“较为基础”的模块，它在 API 模块之上，
仅提供**最基础**的 Bot 实现和事件处理。是一种**轻量级实现库**。

标准库模块无法直接作为 Simple Robot 组件使用。

</tip>

<procedure collapsible="true" default-state="collapsed" title="适用场景">

当你不确定自己的应用场景是否应该选择 **直接使用** 标准库模块时，
这里为你提供了一些参考：

<procedure title="适用">

- 你希望有一个 Bot 实现，它可以支持订阅事件、管理网络连接的生命周期、可以注册事件处理逻辑、替你接收事件，并进行处理/调度。
- 你不需要太多额外的功能，例如处理事件时，如果你想要发消息，那么**自行构建** API 模块中提供的实现。
- 你不需要多组件协同。
- 你不需要 Simple Robot 标准库提供的任何功能。

</procedure>
<procedure title="不适用">


- 你希望使用 Simple Robot 标准库提供的功能。
- 你希望使用一个有更多**高级功能**封装的库，而不是一个仅有基础功能的库。例如处理事件时，你只需要组装好消息文本或封装好的**消息元素对象**，并简单的调用 `send` 即可发送，不需要自行构建 API、自行发送请求。
- 你希望能够支持多组件协同。
- 你希望使用支持 Spring Boot 的库。

</procedure>
</procedure>

## 安装

<tabs group="build">
<tab title="Gradle(Kotlin DSL)" group-key="kts">

```Kotlin
implementation("love.forte.simbot.component:simbot-component-kook-stdlib:%kook-version%")
```

<tip>

如果你使用 Java 而不配合使用 Gradle 的 `kotlin` 插件,
那么你需要指定依赖的后缀为 `-jvm`。

```Kotlin
implementation("love.forte.simbot.component:simbot-component-kook-stdlib-jvm:%kook-version%")
```

</tip>

</tab>
<tab title="Gradle(Groovy)" group-key="groovy">

```Groovy
implementation 'love.forte.simbot.component:simbot-component-kook-stdlib:%kook-version%'
```

<tip>

如果你使用 Java 而不配合使用 Gradle 的 `kotlin` 插件,
那么你需要指定依赖的后缀为 `-jvm`。

```Groovy
implementation 'love.forte.simbot.component:simbot-component-kook-stdlib-jvm:%kook-version%'
```

</tip>

</tab>
<tab title="Maven" group-key="maven">

```xml
<dependency>
    <groupId>love.forte.simbot.component</groupId>
    <!-- Maven 需要添加 `-jvm` 后缀来选择使用 JVM 平台 -->
    <artifactId>simbot-component-kook-stdlib-jvm</artifactId>
    <version>%kook-version%</version>
</dependency>
```

</tab>
</tabs>

### 引擎选择

<include from="snippets.md" element-id="engine-choose" />



## 使用

KOOK组件的标准库模块在 [API模块](use-api.md) 的基础之上提供了构建 Bot、订阅并处理事件的能力。

<tabs group="Code">
<tab title="Kotlin" group-key="Kotlin">

```kotlin
// 准备 bot 的必要信息
val clientId = "xxxx"
val token = "xxxx"
// 用于注册 bot 的 “票据” 信息。
val ticket = Ticket.botWsTicket(clientId, token)

// 构建一个 Bot，并可选的进行一些配置。
val bot = BotFactory.create(ticket) {
    // 比如禁用API请求的超时时长限制
    disableTimeout()

    // 一些其他的配置...
}

// 注册事件有一些不同但类似的方式
// 1️⃣ 通过 subscribe 注册一个普通的事件处理器，此处理器会接收并处理所有类型的事件
//     subscribe 是最基本的注册方式，也是其他方式的最终汇集点
bot.subscribe { raw ->
    // raw 代表事件的原始JSON字符串
    // this: Event<*>, 也就是解析出来的事件结构体
    println("event: $this")
    println("event.extra: $extra")
    println("raw: $raw")
}

// 2️⃣ 通过 subscribe<EventExtraType> 注册一个针对具体 Event<EventExtra> 事件类型的事件处理器，
//     它只有在接收到的 Event.extra 与目标类型一致时才会处理。
//     此示例展示处理 KMarkdownEventExtra 也就 KMarkdown 类型的消息事件，
//     并在对方发送了包含 'stop' 的文本时终止 bot。
bot.subscribe<KMarkdownEventExtra> {
    // raw 代表事件的原始JSON字符串
    // this: Event<KMarkdownEventExtra>, 也就是解析出来的事件结构体，
    // 并且 extra 类型为指定的 KMarkdownEventExtra
    println("event kmarkdown: ${extra.kmarkdown}")

    if ("stop" in extra.kmarkdown.rawContent) {
        // 终止 bot
        bot.cancel()
    }
}

// 启动 bot, 此时会开始获取ws、连接并接收消息。
bot.start()

// 挂起 bot，直到它结束（被终止）
bot.join()
```

</tab>
<tab title="Java" group-key="Java">


```java
// 准备 bot 的必要信息
var clientId = "xxxx";
var token = "xxxx";
// 用于注册 bot 的 “票据” 信息。
var ticket = Ticket.botWsTicket(clientId, token);

// 构建一个 Bot，并可选的进行一些配置。
Bot bot = BotFactory.create(ticket, config -> {
    // 各种配置...
    // 比如禁用API请求的超时时长限制
    config.disableTimeout();

    // 一些其他的配置...
});

// 通过 subscribe 注册一个普通的事件处理器，
// 此处理器会接收并处理所有类型的事件
bot.subscribe(EventProcessors.async((event, raw) -> {
    // raw 代表事件的原始JSON字符串
    // event: Event<*>, 也就是解析出来的事件结构体
    System.out.println("event: " + event);
    System.out.println("event.extra: " + event.getExtra());
    System.out.println("raw: " + raw);

    // 异步处理器必须返回 CompletableFuture
    return CompletableFuture.completedFuture(null);
}));

// 通过 subscribe 注册一个普通的事件处理器，
// 此处理器会接收并处理指定的类型 AtMessageCreate 的事件
// 此示例展示处理 AtMessageCreate 也就公域是消息事件，并在对方发送了包含 'stop' 的文本时终止 bot。
bot.subscribe(EventProcessors.async(KMarkdownEventExtra.class, (event, raw) -> {
    // raw 代表事件的原始JSON字符串
    // event: Event<? extends KMarkdownEventExtra>, 也就是解析出来的事件结构体
    System.out.println("event kmarkdown: " + event.getExtra().getKmarkdown());

    if (event.getExtra().getKmarkdown().getRawContent().contains("stop")) {
        // 终止 bot
        bot.close();
    }

    // 异步处理器必须返回 CompletableFuture
    return CompletableFuture.completedFuture(null);
}));

// 异步地启动bot，并在 bot 启动完成后，
// 将 bot 转化为 future，这个 future 会在 bot 被终止时完成
// 其实也可以把 startAsync 和 asFuture 拆开写，效果相同。
var future = bot.startAsync()
        .thenCompose(unit -> bot.asFuture());

// 阻塞当前线程，直到 bot 被终止。
future.join();
```
{switcher-key="%ja%"}

```java
// 准备 bot 的必要信息
var clientId = "xxxx";
var token = "xxxx";
// 用于注册 bot 的 “票据” 信息。
var ticket = Ticket.botWsTicket(clientId, token);

// 构建一个 Bot，并可选的进行一些配置。
Bot bot = BotFactory.create(ticket, config -> {
    // 各种配置...
    // 比如禁用API请求的超时时长限制
    config.disableTimeout();

    // 一些其他的配置...
});

// 通过 subscribe 注册一个普通的事件处理器，
// 此处理器会接收并处理所有类型的事件
bot.subscribe(EventProcessors.async((event, raw) -> {
    // raw 代表事件的原始JSON字符串
    // event: Event<*>, 也就是解析出来的事件结构体
    System.out.println("event: " + event);
    System.out.println("event.extra: " + event.getExtra());
    System.out.println("raw: " + raw);
}));

// 通过 subscribe 注册一个普通的事件处理器，
// 此处理器会接收并处理指定的类型 AtMessageCreate 的事件
// 此示例展示处理 AtMessageCreate 也就公域是消息事件，并在对方发送了包含 'stop' 的文本时终止 bot。
    bot.subscribe(EventProcessors.block(KMarkdownEventExtra.class, (event, raw) -> {
    // raw 代表事件的原始JSON字符串
    // event: Event<? extends KMarkdownEventExtra>, 也就是解析出来的事件结构体
    System.out.println("event kmarkdown: " + event.getExtra().getKmarkdown());

    if (event.getExtra().getKmarkdown().getRawContent().contains("stop")) {
    // 终止 bot
    bot.close();
    }
}));

// 启动bot
bot.startBlocking();

// 阻塞当前线程，直到 bot 被终止。
bot.joinBlocking();
```
{switcher-key="%jb%"}


<tip switcher-key="%ja%">
当使用异步API时候，请尽可能避免在异步的事件处理器中使用任何阻塞API。
</tip>

</tab>
</tabs>

