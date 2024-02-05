# 使用标准库


stdlib标准库模块基于 **KMP** 构建项目，支持 **JVM、JS、Native** 平台，使用 [**Ktor**](https://ktor.io/) 作为API请求（http请求）和事件订阅（WS连接）的解决方案。

标准库模块是**独立的**，实质上并不依赖 simbot API。你可以单独使用它作为 [KOOK API](https://developer.kookapp.cn/doc/) 与事件订阅能力的底层封装库。

标准库模块依赖API模块。

## 安装

**1. 安装 kook-stdlib 依赖**

<tabs group="use-dependency">
<tab title="Gradle Kotlin DSL" group-key="Kts">

`Gradle` 的 [`Kotlin` 插件](https://kotlinlang.org/docs/gradle-configure-project.html#apply-the-plugin)：

```Kotlin
// 使用 Gradle 的 kotlin 插件来允许自动选择依赖的对应平台，比如JVM或JS等。
plugin {
    kotlin("jvm") version "合适且较新的版本"
    // 或 multiplatform, 如果你使用多平台的话
    // 如果你使用 Java，也最好添加此插件，因此它可以帮助你自动选择 -jvm 的依赖，而不需要主动添加此后缀
}
```

依赖：

```Kotlin
implementation("love.forte.simbot.component:simbot-component-kook-stdlib:3.3.0.0-beta1")
```

</tab>
<tab title="Gradle Groovy" group-key="Gradle">

`Gradle` 的 [`Kotlin` 插件](https://kotlinlang.org/docs/gradle-configure-project.html#apply-the-plugin)：

```Gradle
// 使用 Gradle 的 kotlin 插件来允许自动选择依赖的对应平台，比如JVM或JS等。
plugin {
    id 'org.jetbrains.kotlin.jvm' version '合适且较新的版本'
    // 或 org.jetbrains.kotlin.multiplatform, 如果你使用多平台的话
    // 如果你使用 Java，也最好添加此插件，因此它可以帮助你自动选择 -jvm 的依赖，而不需要主动添加此后缀
}
```

依赖：

```Gradle
implementation 'love.forte.simbot.component:simbot-component-kook-stdlib:3.3.0.0-beta1'
```

</tab>
<tab title="Maven" group-key="Maven">

```xml
<dependency>
    <groupId>love.forte.simbot.component</groupId>
    <!-- 在Maven中使用 '-jvm' 后缀来选择使用JVM平台库 -->
    <artifactId>simbot-component-kook-stdlib-jvm</artifactId>
    <!-- 版本参考下文所述的 Releases -->
    <version>3.3.0.0-beta1</version>
</dependency>
```

</tab>
</tabs>

**2. 选择并安装合适的 Ktor Client 依赖**

前往 [Ktor: HTTP client Engines](https://ktor.io/docs/http-client-engines.html) 选择并使用一个合适的 HTTP Client 引擎。

:::caution 限制条件

**注意:** 你需要选择一个支持**HTTP 1.1**和**WS Client**的引擎。部分引擎可能不支持WS Client，请注意区分。

各引擎实现的限制可参考 [Ktor文档](https://ktor.io/docs/http-client-engines.html#limitations)。

:::

例如：

<tabs group="use-dependency">
<tab title="Gradle Kotlin DSL" label="Kotlin/JVM" group-key="Kts">

以 [`CIO`](https://ktor.io/docs/http-client-engines.html#cio) 引擎为例：

```kotlin
// 或使用 runtimeOnly
implementation("io.ktor:ktor-client-cio:<合适且较新的Ktor版本>")
```

或者如果 Java 版本 `>= Java11`, 可以使用 [`Java`](https://ktor.io/docs/http-client-engines.html#java) 引擎：

```kotlin
// 或使用 runtimeOnly
implementation("io.ktor:ktor-client-java:<合适且较新的Ktor版本>")
```

</tab>
<tab title="Kotlin/JS" label="Kotlin/JS" group-key="Kts">

以 [`JS`](https://ktor.io/docs/http-client-engines.html#js) 引擎为例：

```kotlin
implementation("io.ktor:ktor-client-js:<合适且较新的Ktor版本>")
```


</tab>
<tab title="Kotlin/N" label="Kotlin/Native" group-key="Kts">

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
<tab title="CIO" label="CIO">

> see [`CIO`](https://ktor.io/docs/http-client-engines.html#cio)

```kotlin
implementation("io.ktor:ktor-client-cio:<合适且较新的Ktor版本>")
```

</tab>
</tabs>

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

或者如果 Java 版本 `>= Java11`, 可以使用 [`Java`](https://ktor.io/docs/http-client-engines.html#java) 引擎：

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
</tabs>

## 使用

### 构建BOT、订阅事件

<tabs group="code">
<tab title="Kotlin" label="Kotlin/JVM" group-key="Kotlin-JVM">

```kotlin
// 构建一个 bot
val bot = BotFactory.create(Ticket.botWsTicket(clientId = "CLIENT_ID", token = "TOKEN")) {
    // bot相关的配置, 例如：
    // 配置bot进行client请求和ws连接时使用的Ktor引擎
    // JVM平台下，默认不配置则会尝试自动加载环境中可能存在的引擎
    clientEngineFactory = CIO
    wsEngineFactory = CIO

    // 是否启用 compress, 默认为true
    isCompress = true

    // 禁用API请求时的超时配置
    disableTimeout()

    // 或者自定义超时配置
    timeout {
        requestTimeoutMillis = 5000L
        connectTimeoutMillis = 5000L
    }

    // 以及其他...
}

// 注册事件处理器
bot.processor { raw ->
    // this: Event<*>, 也就是本次接收到的事件体。
    // raw: ws连接接收到的原始事件的JSON字符串
    println("event: $this")
    println("raw:   $raw")
}

// 也可以根据事件的 extra 订阅一个具体类型的事件
bot.processor<TextExtra> { raw ->
    // this: Event<TextExtra>, 也就是本次接收到的事件体。只有 extra 的类型匹配才会触发此事件处理器
    // raw: ws连接接收到的原始事件的JSON字符串
    println("event: $this")
    println("extra: $extra")
    println("raw:   $raw")
}

// 也可以根据事件类型 Event.Type 的某个值订阅与之匹配的事件
bot.processor(Event.Type.KMARKDOWN) { raw ->
    // this: Event<TextExtra>, 也就是本次接收到的事件体。this.event 与 Type 参数匹配
    // raw: ws连接接收到的原始事件的JSON字符串
    println("event: $this")
    println("type:  $type")
    println("raw:   $raw")
}

// 可以查询bot自身的信息
val me = bot.me()
println("me: $me")

// 启动BOT。调用 start 或其他衍生函数 bot 才会开始连接到 KOOK 服务器并订阅消息。
// bot.join() 会挂起，直到 bot 被关闭，例如在某处调用 bot.close() 或 bot中的父Job被关闭
// startAndJoin() 组合了 start() 和 join()
bot.startAndJoin()
```

一个简化版的示例：

```kotlin
// 构建bot
// 需要确保runtime环境中有可用的ktor引擎，且同时支持HTTP和ws client
val bot = BotFactory.create(Ticket.botWsTicket(clientId = "CLIENT_ID", token = "TOKEN"))

// 订阅某个具体事件
bot.processor<TextExtra> { raw ->
    // this: Event<TextExtra>, 也就是本次接收到的事件体。只有 extra 的类型匹配才会触发此事件处理器
    // raw: ws连接接收到的原始事件的JSON字符串
    println("event: $this")
    println("extra: $extra")
    println("raw:   $raw")
}

// 启动BOT。调用 start 或其他衍生函数 bot 才会开始连接到 KOOK 服务器并订阅消息。
// bot.join() 会挂起，直到 bot 被关闭，例如在某处调用 bot.close() 或 bot中的父Job被关闭
// startAndJoin() 组合了 start() 和 join()
bot.startAndJoin()
```

</tab>
<tab title="Kotlin/JS" group-key="Kotlin-JS">

```kotlin
// 构建一个 bot
val bot = BotFactory.create(Ticket.botWsTicket(clientId = "CLIENT_ID", token = "TOKEN")) {
    // bot相关的配置, 例如：
    // 配置bot进行client请求和ws连接时使用的Ktor引擎
    // JS平台下，默认会依赖ktor-js（因为js平台似乎没别的可选），因此此处可以省略
    clientEngineFactory = Js
    wsEngineFactory = Js

    // 是否启用 compress
    // JS 平台下默认为 **false**
    // JS 平台支持开启 compress, 但是可能不够稳定，因此如果非必要，可以不用开启。
    isCompress = false

    // 禁用API请求时的超时配置
    disableTimeout()

    // 或者自定义超时配置
    timeout {
        requestTimeoutMillis = 5000L
        connectTimeoutMillis = 5000L
    }

    // 以及其他...
}

// 注册事件处理器
bot.processor { raw ->
    // this: Event<*>, 也就是本次接收到的事件体。
    // raw: ws连接接收到的原始事件的JSON字符串
    println("event: $this")
    println("raw:   $raw")
}

// 也可以根据事件的 extra 订阅一个具体类型的事件
bot.processor<TextExtra> { raw ->
    // this: Event<TextExtra>, 也就是本次接收到的事件体。只有 extra 的类型匹配才会触发此事件处理器
    // raw: ws连接接收到的原始事件的JSON字符串
    println("event: $this")
    println("extra: $extra")
    println("raw:   $raw")
}

// 也可以根据事件类型 Event.Type 的某个值订阅与之匹配的事件
bot.processor(Event.Type.KMARKDOWN) { raw ->
    // this: Event<TextExtra>, 也就是本次接收到的事件体。this.event 与 Type 参数匹配
    // raw: ws连接接收到的原始事件的JSON字符串
    println("event: $this")
    println("type:  $type")
    println("raw:   $raw")
}

// 可以查询bot自身的信息
val me = bot.me()
println("me: $me")

// 启动BOT。调用 start 或其他衍生函数 bot 才会开始连接到 KOOK 服务器并订阅消息。
// bot.join() 会挂起，直到 bot 被关闭，例如在某处调用 bot.close() 或 bot中的父Job被关闭
// startAndJoin() 组合了 start() 和 join()
bot.startAndJoin()
```

一个简化版的示例：

```kotlin
// 构建bot
val bot = BotFactory.create(Ticket.botWsTicket(clientId = "CLIENT_ID", token = "TOKEN"))

// 订阅某个具体事件
bot.processor<TextExtra> { raw ->
    // this: Event<TextExtra>, 也就是本次接收到的事件体。只有 extra 的类型匹配才会触发此事件处理器
    // raw: ws连接接收到的原始事件的JSON字符串
    println("event: $this")
    println("extra: $extra")
    println("raw:   $raw")
}

// 启动BOT。调用 start 或其他衍生函数 bot 才会开始连接到 KOOK 服务器并订阅消息。
// bot.join() 会挂起，直到 bot 被关闭，例如在某处调用 bot.close() 或 bot中的父Job被关闭
// startAndJoin() 组合了 start() 和 join()
bot.startAndJoin()
```

</tab>
<tab title="Kotlin/Native" group-key="Kotlin-N">

> 此处以 `MingwX64` 平台环境为例：

```kotlin
// 构建一个 bot
val bot = BotFactory.create(Ticket.botWsTicket(clientId = "CLIENT_ID", token = "TOKEN")) {
    // bot相关的配置, 例如：
    // 配置bot进行client请求和ws连接时使用的Ktor引擎
    // 不同的native平台请注意选择合适的引擎
    clientEngineFactory = WinHttp
    wsEngineFactory = WinHttp

    // 是否启用 compress
    // native 平台下默认为 **false**, 且 **不支持** 开启。
    // isCompress = false

    // 禁用API请求时的超时配置
    disableTimeout()

    // 或者自定义超时配置
    timeout {
        requestTimeoutMillis = 5000L
        connectTimeoutMillis = 5000L
    }

    // 以及其他...
}

// 注册事件处理器
bot.processor { raw ->
    // this: Event<*>, 也就是本次接收到的事件体。
    // raw: ws连接接收到的原始事件的JSON字符串
    println("event: $this")
    println("raw:   $raw")
}

// 也可以根据事件的 extra 订阅一个具体类型的事件
bot.processor<TextExtra> { raw ->
    // this: Event<TextExtra>, 也就是本次接收到的事件体。只有 extra 的类型匹配才会触发此事件处理器
    // raw: ws连接接收到的原始事件的JSON字符串
    println("event: $this")
    println("extra: $extra")
    println("raw:   $raw")
}

// 也可以根据事件类型 Event.Type 的某个值订阅与之匹配的事件
bot.processor(Event.Type.KMARKDOWN) { raw ->
    // this: Event<TextExtra>, 也就是本次接收到的事件体。this.event 与 Type 参数匹配
    // raw: ws连接接收到的原始事件的JSON字符串
    println("event: $this")
    println("type:  $type")
    println("raw:   $raw")
}

// 可以查询bot自身的信息
val me = bot.me()
println("me: $me")

// 启动BOT。调用 start 或其他衍生函数 bot 才会开始连接到 KOOK 服务器并订阅消息。
// bot.join() 会挂起，直到 bot 被关闭，例如在某处调用 bot.close() 或 bot中的父Job被关闭
// startAndJoin() 组合了 start() 和 join()
bot.startAndJoin()
```

一个简化版的示例：

```kotlin
// 构建bot
val bot = BotFactory.create(Ticket.botWsTicket(clientId = "CLIENT_ID", token = "TOKEN")) {
    // 配置bot进行client请求和ws连接时使用的Ktor引擎
    // 不同的native平台请注意选择合适的引擎
    clientEngineFactory = WinHttp
    wsEngineFactory = WinHttp
}

// 订阅某个具体事件
bot.processor<TextExtra> { raw ->
    // this: Event<TextExtra>, 也就是本次接收到的事件体。只有 extra 的类型匹配才会触发此事件处理器
    // raw: ws连接接收到的原始事件的JSON字符串
    println("event: $this")
    println("extra: $extra")
    println("raw:   $raw")
}

// 启动BOT。调用 start 或其他衍生函数 bot 才会开始连接到 KOOK 服务器并订阅消息。
// bot.join() 会挂起，直到 bot 被关闭，例如在某处调用 bot.close() 或 bot中的父Job被关闭
// startAndJoin() 组合了 start() 和 join()
bot.startAndJoin()
```

</tab>
<tab title="Java" group-key="Java">

```java
// 准备Ticket
final var ticket = Ticket.botWsTicket("CLIENT_ID", "TOKEN");

// 准备bot配置
final var botConfiguration = new BotConfiguration();
// bot相关的配置, 例如：

// 配置bot进行client请求和ws连接时使用的Ktor引擎
// JVM平台下，默认不配置则会尝试自动加载环境中可能存在的引擎
botConfiguration.setClientEngineFactory(CIO.INSTANCE);
botConfiguration.setWsEngineFactory(CIO.INSTANCE);

// 是否启用 compress, 默认为true
botConfiguration.setCompress(true);

// 禁用API请求时的超时配置
botConfiguration.disableTimeout();

// 或者自定义超时配置
final var timeoutConfiguration = new BotConfiguration.TimeoutConfiguration();
timeoutConfiguration.setRequestTimeoutMillis(5000L);
timeoutConfiguration.setConnectTimeoutMillis(5000L);
botConfiguration.setTimeout(timeoutConfiguration);

// 以及其他...

// 构建bot
final var bot = BotFactory.create(ticket, botConfiguration);// botConfiguration 也可省略，如果都用默认的话

// 注册事件处理器
bot.blockingProcessor((event, raw) -> {
    // event: Event<*>, 也就是本次接收到的事件体。
    // raw: ws连接接收到的原始事件的JSON字符串
    System.out.println("event = " + event);
    System.out.println("raw = " + raw);
});

// 也可以根据事件的 extra 订阅一个具体类型的事件
bot.blockingProcessor(TextExtra.class, (event, raw) -> {
    // event: Event<TextExtra>, 也就是本次接收到的事件体。只有 extra 的类型匹配才会触发此事件处理器
    // raw: ws连接接收到的原始事件的JSON字符串
    System.out.println("event = " + event);
    System.out.println("event.getExtra() = " + event.getExtra());
    System.out.println("raw = " + raw);
});

// 也可以根据事件类型 Event.Type 的某个值订阅与之匹配的事件
bot.blockingProcessor(Event.Type.KMARKDOWN, (event, raw) -> {
    // event: Event<TextExtra>, 也就是本次接收到的事件体。this.event 与 Type 参数匹配
    // raw: ws连接接收到的原始事件的JSON字符串
    System.out.println("event = " + event);
    System.out.println("event.getType() = " + event.getType());
    System.out.println("raw = " + raw);
});

// 可以查询bot自身的信息
final var me = bot.getMe();
System.out.println("me = " + me);

// 启动BOT。调用 startBlocking 或其他衍生函数 bot 才会开始连接到 KOOK 服务器并订阅消息。
// startAndJoin() 组合了 start() 和 join()
bot.startBlocking();

// bot.joinBlocking() 会阻塞当前线程，直到 bot 被关闭，例如在某处调用 bot.close() 或 bot中的父Job被关闭
bot.joinBlocking();
```

一个简化版的示例：

```java
// 准备Ticket
final var ticket = Ticket.botWsTicket("CLIENT_ID", "TOKEN");

// 构建bot
final var bot = BotFactory.create(ticket);// botConfiguration 也可省略，如果都用默认的话

// 订阅某个具体事件
bot.blockingProcessor(TextExtra.class, (event, raw) -> {
    // event: Event<TextExtra>, 也就是本次接收到的事件体。只有 extra 的类型匹配才会触发此事件处理器
    // raw: ws连接接收到的原始事件的JSON字符串
    System.out.println("event = " + event);
    System.out.println("event.getExtra() = " + event.getExtra());
    System.out.println("raw = " + raw);
});


// 启动BOT。调用 startBlocking 或其他衍生函数 bot 才会开始连接到 KOOK 服务器并订阅消息。
bot.startBlocking();

// bot.joinBlocking() 会阻塞当前线程，直到 bot 被关闭，例如在某处调用 bot.close() 或 bot中的父Job被关闭
bot.joinBlocking();
```


</tab>
<tab title="Java Async" group-key="Java-A">

```java
// 准备Ticket
final var ticket = Ticket.botWsTicket("CLIENT_ID", "TOKEN");

// 准备bot配置
final var botConfiguration = new BotConfiguration();
// bot相关的配置, 例如：

// 配置bot进行client请求和ws连接时使用的Ktor引擎
// JVM平台下，默认不配置则会尝试自动加载环境中可能存在的引擎
botConfiguration.setClientEngineFactory(CIO.INSTANCE);
botConfiguration.setWsEngineFactory(CIO.INSTANCE);

// 是否启用 compress, 默认为true
botConfiguration.setCompress(true);

// 禁用API请求时的超时配置
botConfiguration.disableTimeout();

// 或者自定义超时配置
final var timeoutConfiguration = new BotConfiguration.TimeoutConfiguration();
timeoutConfiguration.setRequestTimeoutMillis(5000L);
timeoutConfiguration.setConnectTimeoutMillis(5000L);
botConfiguration.setTimeout(timeoutConfiguration);

// 以及其他...

// 构建bot
final var bot = BotFactory.create(ticket, botConfiguration);// botConfiguration 也可省略，如果都用默认的话

// 注册事件处理器
bot.asyncProcessor((event, raw) -> {
    // event: Event<*>, 也就是本次接收到的事件体。
    // raw: ws连接接收到的原始事件的JSON字符串
    System.out.println("event = " + event);
    System.out.println("raw = " + raw);

    // ⚠ 不要在任何异步函数体中调用阻塞API。
    // 例如：bot.getMe(),  xxxBlocking等。
    // 任何有 xxxAsync 等价替代的函数都是阻塞函数。
    
   // 假设有个异步任务，返回一个异步结果
    return CompletableFuture.completedFuture(null);
});

// 也可以根据事件的 extra 订阅一个具体类型的事件
bot.asyncProcessor(TextExtra.class, (event, raw) -> {
    // event: Event<TextExtra>, 也就是本次接收到的事件体。只有 extra 的类型匹配才会触发此事件处理器
    // raw: ws连接接收到的原始事件的JSON字符串
    System.out.println("event = " + event);
    System.out.println("event.getExtra() = " + event.getExtra());
    System.out.println("raw = " + raw);

    // 假设有个异步任务，返回一个异步结果
    return CompletableFuture.completedFuture(null);
});

// 也可以根据事件类型 Event.Type 的某个值订阅与之匹配的事件
bot.asyncProcessor(Event.Type.KMARKDOWN, (event, raw) -> {
    // event: Event<TextExtra>, 也就是本次接收到的事件体。this.event 与 Type 参数匹配
    // raw: ws连接接收到的原始事件的JSON字符串
    System.out.println("event = " + event);
    System.out.println("event.getType() = " + event.getType());
    System.out.println("raw = " + raw);

    // 假设有个异步任务，返回一个异步结果
    return CompletableFuture.completedFuture(null);
});

// 可以查询bot自身的信息
// 如果有需要，记得处理异常
bot.getMeAsync().thenAccept(me -> {
    // 得到结果
    System.out.println("me = " + me);
});

// 启动BOT。调用 startAsync 或其他衍生函数 bot 才会开始连接到 KOOK 服务器并订阅消息。
// 如果有需要，记得处理异常
bot.startAsync();

// bot.asFuture() 会转化为一个活跃的 CompletableFuture，直到 bot 被关闭，例如在某处调用 bot.close() 或 bot中的父Job被关闭
bot.asFuture().whenComplete((unit, throwable) -> {
    System.out.println("Bot被终止了。");
    System.out.println("throwable: " + throwable);
});
```

一个简化版的示例：

```java
// 准备Ticket
final var ticket = Ticket.botWsTicket("CLIENT_ID", "TOKEN");
// 构建bot
final var bot = BotFactory.create(ticket);

// 订阅某个具体事件
bot.asyncProcessor(TextExtra.class, (event, raw) -> {
    // event: Event<TextExtra>, 也就是本次接收到的事件体。只有 extra 的类型匹配才会触发此事件处理器
    // raw: ws连接接收到的原始事件的JSON字符串
    System.out.println("event = " + event);
    System.out.println("event.getExtra() = " + event.getExtra());
    System.out.println("raw = " + raw);

    // 假设有个异步任务，返回一个异步结果
    // ⚠ 不要在任何异步函数体中调用阻塞API。
    // 例如：bot.getMe(),  xxxBlocking等。
    // 任何有 xxxAsync 等价替代的函数都是阻塞函数。
    return CompletableFuture.completedFuture(null);
});

// 启动BOT。调用 startAsync 或其他衍生函数 bot 才会开始连接到 KOOK 服务器并订阅消息。
// bot.asFuture() 会转化为一个活跃的 CompletableFuture，直到 bot 被关闭，例如在某处调用 bot.close() 或 bot中的父Job被关闭
bot.startAsync().thenCompose(unit -> bot.asFuture()).whenComplete((unit, throwable) -> {
    System.out.println("Bot被终止了。");
    System.out.println("throwable: " + throwable);
});
```

</tab>
</tabs>

### 使用BOT请求API

在标准库中，我们也针对 `Bot` 提供了一些用于请求API的便捷方法，免除你每次都需要提供 `HttpClient` 和 `authorization` 的问题。

以**获取频道服务器列表API**为例：

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

> 省略配置过程，Kotlin各平台的使用方式是一致的。

```kotlin
// 构建bot
val bot = BotFactory.create(Ticket.botWsTicket(clientId = "CLIENT_ID", token = "TOKEN")) {
    // 配置过程省略
}

// 获取频道列表API
val api = GetGuildListApi.create()

// 通过 bot 请求
val dataList = api.requestBy(bot)

dataList.forEach { println("guild: $it") }
```

</tab>
<tab title="Java" group-key="Java">

```java
// 准备Ticket
final var ticket = Ticket.botWsTicket("CLIENT_ID", "TOKEN");

// 配置过程省略...

// 构建bot
final var bot = BotFactory.create(ticket);

// 构建api
final var api = GetGuildListApi.create();

// 请求并得到结果
final var listData = api.requestByBlocking(bot);

for (SimpleGuild guild : listData) {
    System.out.println("guild = " + guild);
}
```


</tab>
<tab title="Java Async" group-key="Java-A">

```java
// 准备Ticket
final var ticket = Ticket.botWsTicket("CLIENT_ID", "TOKEN");

// 配置过程省略...

// 构建bot
final var bot = BotFactory.create(ticket);

// 构建api
final var api = GetGuildListApi.create();

// 请求并得到结果
api.requestByAsync(bot).thenAccept(listData -> {
    for (SimpleGuild guild : listData) {
        System.out.println("guild = " + guild);
    }
});
```

</tab>
</tabs>
