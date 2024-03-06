---
switcher-label: Java API 风格
---

# 使用核心库


<tldr>
<p>使用 <control>核心库(core 模块)</control> 配合 simbot4 核心库来将 KOOK 作为 simbot4 的组件之一应用在 <code>Application</code> 中。</p>
</tldr>

<note>
站在 simbot 核心库的视角来看，“KOOK组件的‘核心库’” 也可以被称为KOOK组件库。
</note>

## 安装

首先，要配合使用 simbot4，就必须添加 `simbot-core` 的依赖。组件对于 simbot 的库依赖一般都是仅编译器的。

<tip>

simbot 核心库的版本尽量不要低于 `v%minimum-core-version%`，可前往
[GitHub Releases](https://github.com/simple-robot/simpler-robot/releases)
查看各版本及其说明。

此处以 `simbot-core v%minimum-core-version%` 作为**示例**。

</tip>


<tabs group="build">
<tab title="Gradle(Kotlin DSL)" group-key="kts">

```Kotlin
// simbot4核心库
implementation("love.forte.simbot:simbot-core:%minimum-core-version%")
// KOOK组件库
implementation("love.forte.simbot.component:simbot-component-kook-core:%version%")
```

<tip>

如果你使用 Java 而不配合使用 Gradle 的 `kotlin` 插件,
那么你需要指定依赖的后缀为 `-jvm`。

```Kotlin
// simbot4核心库
implementation("love.forte.simbot:simbot-core-jvm:%minimum-core-version%")
// KOOK组件库
implementation("love.forte.simbot.component:simbot-component-kook-core-jvm:%version%")
```

</tip>

</tab>
<tab title="Gradle(Groovy)" group-key="groovy">

```Groovy
// simbot4核心库
implementation 'love.forte.simbot:simbot-core:%minimum-core-version%'
// KOOK组件库
implementation 'love.forte.simbot.component:simbot-component-kook-core:%version%'
```

<tip>

如果你使用 Java 而不配合使用 Gradle 的 `kotlin` 插件,
那么你需要指定依赖的后缀为 `-jvm`。

```Groovy
// simbot4核心库
implementation 'love.forte.simbot:simbot-core-jvm:%minimum-core-version%'
// KOOK组件库
implementation 'love.forte.simbot.component:simbot-component-kook-core-jvm:%version%'
```

</tip>

</tab>
<tab title="Maven" group-key="maven">

```xml
<!-- simbot4核心库 -->
<dependency>
    <groupId>love.forte.simbot</groupId>
    <artifactId>simbot-core-jvm</artifactId>
    <version>%minimum-core-version%</version>
</dependency>
<!-- KOOK组件库 -->
<dependency>
    <groupId>love.forte.simbot.component</groupId>
    <artifactId>simbot-component-kook-core-jvm</artifactId>
    <version>%version%</version>
</dependency>
```

</tab>
</tabs>

### 引擎选择

<include from="snippets.md" element-id="engine-choose" />


## 使用
### 安装到 Application

向 Application 中安装 `KookComponent` 和 `KookBotManager`。

<tabs group="Code">
<tab title="Kotlin" group-key="Kotlin">

```kotlin
val app = launchSimpleApplication {
    // 使用 useKook 简写，
    // 代表同时安装 `KookComponent` 和 `KookBotManager`
    useKook()
    
    // 其他配置..
}
```

</tab>
<tab title="Java" group-key="Java">


```java
var appFuture = Applications.launchApplicationAsync(Simple.INSTANCE, appConfigurer -> {
    // 安装 `KookComponent` 和 `KookBotManager`
    appConfigurer.install(KookComponent.Factory);
    appConfigurer.install(KookBotManager.Factory);
}).asFuture();
```
{switcher-key="%ja%"}

```java
var app = Applications.launchApplicationBlocking(Simple.INSTANCE, appConfigurer -> {
    // 安装 `KookComponent` 和 `KookBotManager`
    appConfigurer.install(KookComponent.Factory);
    appConfigurer.install(KookBotManager.Factory);
});
```
{switcher-key="%jb%"}

</tab>
</tabs>


### 注册事件处理器

<tabs group="Code">
<tab title="Kotlin" group-key="Kotlin">

```kotlin
val app = ...

// 注册各种事件处理器
app.listeners {
    // 注册一个事件处理器
    // 所有子频道消息事件
    // 其中就包括 KOOK 的消息事件，例如 KookChannelMessageEvent
    listen<ChatChannelMessageEvent> {
        println("context: $this")
        println("context.event: $event")

        // 返回事件处理结果
        EventResult.empty()
    }

    // 再注册一个事件处理器
    // 明确监听 KOOK 的消息事件
    // 使用 process 不需要返回值
    process<KookChannelMessageEvent> {
        println("context: $this")
        println("context.event: $event")
    }
}
```

</tab>
<tab title="Java" group-key="Java">


```java
// 假设通过 future 的 thenAccept 或其他什么地方得到了 Application
var app = ...;

// 注册一个事件处理器
// 所有子频道消息事件
// 其中就包括 KOOK 的消息事件，例如 KookChannelMessageEvent
eventDispatcher.register(EventListeners.async(ChatChannelMessageEvent.class, (context, event) -> {
    System.out.println("context: " + context);
    System.out.println("context.event: " + event);

    // 返回异步的事件处理结果
    return CompletableFuture.completedFuture(EventResult.empty());
}));

// 再注册一个事件处理器
// 明确监听 KOOK 的消息事件
eventDispatcher.register(EventListeners.async(QGAtMessageCreateEvent.class, (context, event) -> {
    System.out.println("context: " + context);
    System.out.println("context.event: " + event);

    // 返回异步的事件处理结果
    return CompletableFuture.completedFuture(EventResult.empty());
}));
```
{switcher-key="%ja%"}

```java
var app = ...;

// 注册一个事件处理器
// 所有子频道消息事件
// 其中就包括 KOOK 的消息事件，例如 KookChannelMessageEvent
eventDispatcher.register(EventListeners.block(ChatChannelMessageEvent.class, (context, event) -> {
    System.out.println("context: " + context);
    System.out.println("context.event: " + event);

    // 返回事件处理结果
    return EventResult.empty();
}));

// 再注册一个事件处理器
// 明确监听 KOOK 的消息事件
eventDispatcher.register(EventListeners.block(QGAtMessageCreateEvent.class, (context, event) -> {
    System.out.println("context: " + context);
    System.out.println("context.event: " + event);

    // 返回异步的事件处理结果
    return EventResult.empty();
}));
```
{switcher-key="%jb%"}

</tab>
</tabs>

### 注册、启动Bot


## 更多有关 simbot API

前往 [simbot官方手册](https://simbot.forte.love/) 阅读有关 simbot API 的各种介绍与示例吧！
