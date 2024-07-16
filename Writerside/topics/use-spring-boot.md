---
switcher-label: Java API 风格
---

<var name="jr" value="Reactor"/>

<include from="snippets.md" element-id="to-main-doc" />

# 使用 Spring Boot


<tldr>
<p>使用 <control>核心库(core 模块)</control> 配合 simbot4 Spring Boot starter 来将KOOK组件作为 simbot4 的组件之一应用在 <code>Spring Boot 3</code> 中。</p>
</tldr>


## 准备

### Java 17

simbot4 的 Spring Boot starter 基于 Spring Boot 3，因此 Java 的版本至少为 **Java17** 。

### 创建 Spring Boot 3 项目

首先你得有个 Spring Boot 3 项目, 你可以前往 [Spring Initializr][spring.start]
或者借助 IDE (比如 IDEA) 的相关功能创建一个 Spring Boot 3 的项目。
你可以自由选择需要添加的任何其他 Spring Boot 组件，比如 `spring-boot-starter-web` 之类的。

<note title="相关参考">

- [Spring Initializr][spring.start]
- [Spring Quickstart Guide](https://spring.io/quickstart/)

</note>

## 安装

<tip>

simbot 核心库的版本尽量不要低于 `v%minimum-core-version%`，可前往
[GitHub Releases](https://github.com/simple-robot/simpler-robot/releases)
查看各版本及其说明。

此处以 `v%minimum-core-version%` 作为**示例**。

</tip>

<tabs group="build">
<tab title="Gradle(Kotlin DSL)" group-key="kts">

```Kotlin
// simbot4核心库
implementation("love.forte.simbot:simbot-core-spring-boot-starter:%minimum-core-version%")
// KOOK组件库
implementation("love.forte.simbot.component:simbot-component-kook-core:%kook-version%")
```

<tip>

如果你使用 Java 而不配合使用 Gradle 的 `kotlin` 插件,
那么你需要为组件库指定依赖的后缀为 `-jvm`。

```Kotlin
// simbot4核心库
implementation("love.forte.simbot:simbot-core-spring-boot-starter:%minimum-core-version%")
// KOOK组件库
implementation("love.forte.simbot.component:simbot-component-kook-core-jvm:%kook-version%")
```

</tip>

</tab>
<tab title="Gradle(Groovy)" group-key="groovy">

```Groovy
// simbot4核心库
implementation 'love.forte.simbot:simbot-core-spring-boot-starter:%minimum-core-version%'
// KOOK组件库
implementation 'love.forte.simbot.component:simbot-component-kook-core:%kook-version%'
```

<tip>

如果你使用 Java 而不配合使用 Gradle 的 `kotlin` 插件,
那么你需要为组件库指定依赖的后缀为 `-jvm`。

```Kotlin
// simbot4核心库
implementation 'love.forte.simbot:simbot-core-spring-boot-starter:%minimum-core-version%'
// KOOK组件库
implementation 'love.forte.simbot.component:simbot-component-kook-core-jvm:%kook-version%'
```

</tip>

</tab>
<tab title="Maven" group-key="maven">

```xml
<!-- simbot4核心库 -->
<dependency>
    <groupId>love.forte.simbot</groupId>
    <artifactId>simbot-core-spring-boot-starter</artifactId>
    <version>%minimum-core-version%</version>
</dependency>
<!-- KOOK组件库 -->
<dependency>
    <groupId>love.forte.simbot.component</groupId>
    <artifactId>simbot-component-kook-core-jvm</artifactId>
    <version>%kook-version%</version>
</dependency>
```

</tab>
</tabs>

### 引擎选择

<include from="snippets.md" element-id="engine-choose" />


## Bot 配置

在你的资源目录中: 
<path>resources/simbot-bots/</path>
中创建任意的一个或多个bot配置文件，并以 `.bot.json` 作为扩展名，
例如 `mybot.bot.json`。

配置文件的内容可前往参考 [Bot配置文件](bot-config.md) 章节。

> 这个扫描目录是**可配置的**。
> 这属于 simbot4 Spring Boot starter 的配置，可参考 
> [simbot手册: 使用 Spring Boot 3](https://simbot.forte.love/start-use-spring-boot-3.html)。

## 使用
### 添加启动注解

在你的启动类上添加 `@EnableSimbot` 注解来启用 simbot。

<tabs group="Code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
@EnableSimbot // 启用 simbot
@SpringBootApplication
class MyApplication

fun main(args: Array<String>) {
    runApplicarion<MyApplication>(*args)
}
```

</tab>
<tab title="Java" group-key="Java">

```Java
@EnableSimbot // 启用 simbot
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

</tab>
</tabs>


KOOK组件支持使用 SPI **自动加载** 组件和插件。
添加依赖到项目环境中、编写完 Bot 配置文件后，可前往
[simbot手册: 使用 Spring Boot 3](https://simbot.forte.love/start-use-spring-boot-3.html) 
了解更多 simbot4 Spring Boot starter 的可配置内容以及启动注解等信息。

## 更多相关参考

- 前往 [simbot官方手册](https://simbot.forte.love/) 阅读有关 simbot API 的相关介绍与示例。
- 前往 [simbot手册: 使用 Spring Boot 3](https://simbot.forte.love/start-use-spring-boot-3.html)
  了解更多 simbot4 Spring Boot starter 的可配置内容以及启动注解等信息。


## 简单示例

几个简单的**事件监听**示例。

<tabs>
<tab title="输出事件日志">
<tabs group="Code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
@EnableSimbot // 启用 simbot
@SpringBootApplication
class MyApplication

fun main(args: Array<String>) {
    runApplicarion<MyApplication>(*args)
}

private val logger = LoggerFactory.getLogger(MyHandlers::class.java)

@Component // 需要交给Spring管理
class MyHandlers {
    @Listener // 标记事件处理函数
    suspend fun handle(event: Event) {
        logger.info("Event: {}", event)
    }
}
```

</tab>
<tab title="Java" group-key="Java">

```Java
@EnableSimbot // 启用 simbot
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

    @Component // 需要交给Spring管理
    public static class MyHandlers {
        private static final Logger logger = Logger.getLogger(MyHandlers.class);
      
        @Listener // 标记事件处理函数
        public void handle(Event event) {
            logger.info("Event: {}", event)
        }
    }
}
```

</tab>
</tabs>
</tab>
<tab title="消息回复(通用)">

此处以 "子频道消息事件" 为例，此事件类型为 `ChatChannelMessageEvent`。

事件逻辑：当收到文本内 **包含"你好"** 的消息，回复"你也好"。

<tabs group="Code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
@EnableSimbot // 启用 simbot
@SpringBootApplication
class MyApplication

fun main(args: Array<String>) {
    runApplicarion<MyApplication>(*args)
}

@Component // 需要交给Spring管理
class MyHandlers {
    @Listener // 标记事件处理函数
    @Filter(  // 简单的事件过滤注解
        value = "你好", // 需要包含的文字
        matchType = MatchType.TEXT_CONENT // 调整匹配模式为 "文本包含"
    )
    // 如果需要，添加此注解 @ContentTrim 来去除匹配用的文本前后的空白字符。
    // 当 Bot 被 at 时，被解析出的文本内容很有可能存在一些前后空格，
    // 所以此时使用 @ContentTrim 会有所帮助
    @ContentTrim 
    suspend fun handle(event: ChatChannelMessageEvent) {
        event.reply("你也好")
    }
}
```

</tab>
<tab title="Java" group-key="Java">

```Java
@EnableSimbot // 启用 simbot
@SpringBootApplication
public class MyApplication {
  public static void main(String[] args) {
    SpringApplication.run(MyApplication.class, args);
  }

  @Component // 需要交给Spring管理
  public static class MyHandlers {
    @Listener // 标记事件处理函数
    @Filter(  // 简单的事件过滤注解
            value = "你好", // 需要包含的文字
            matchType = MatchType.TEXT_CONENT // 调整匹配模式为 "文本包含"
    )
    // 如果需要，添加此注解 @ContentTrim 来去除匹配用的文本前后的空白字符。
    // 当 Bot 被 at 时，被解析出的文本内容很有可能存在一些前后空格，
    // 所以此时使用 @ContentTrim 会有所帮助
    @ContentTrim
    public CompletableFuture<?> handle(ChatChannelMessageEvent event) {
      return event.replyAsync("你也好");
              // 当使用异步API (CompletableFuture) 时，
              // 你需要格外注意异常处理问题。
              // 如果你不 return，
              // 那么你就要处理异常，否则你无法感知到异步任务中出现的错误。
              // 比如：
              // .whenComplete((value, e) -> {
              //   if (e != null) {
              //     // 比如输出错误日志
              //   }
              // })
    }
  }
}
```
{switcher-key="%ja%"}

```Java
@EnableSimbot // 启用 simbot
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

    @Component // 需要交给Spring管理
    public static class MyHandlers {
        @Listener // 标记事件处理函数
        @Filter(  // 简单的事件过滤注解
                value = "你好", // 需要包含的文字
                matchType = MatchType.TEXT_CONENT // 调整匹配模式为 "文本包含"
        )
        // 如果需要，添加此注解 @ContentTrim 来去除匹配用的文本前后的空白字符。
        // 当 Bot 被 at 时，被解析出的文本内容很有可能存在一些前后空格，
        // 所以此时使用 @ContentTrim 会有所帮助
        @ContentTrim
        public void handle(ChatChannelMessageEvent event) {
            event.replyBlocking("你也好");
        }
    }
}
```
{switcher-key="%jb%"}

```Java
@EnableSimbot // 启用 simbot
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

    @Component // 需要交给Spring管理
    public static class MyHandlers {
        @Listener // 标记事件处理函数
        @Filter(  // 简单的事件过滤注解
                value = "你好", // 需要包含的文字
                matchType = MatchType.TEXT_CONENT // 调整匹配模式为 "文本包含"
        )
        // 如果需要，添加此注解 @ContentTrim 来去除匹配用的文本前后的空白字符。
        // 当 Bot 被 at 时，被解析出的文本内容很有可能存在一些前后空格，
        // 所以此时使用 @ContentTrim 会有所帮助
        @ContentTrim
        public Mono<?> handle(ChatChannelMessageEvent event) {
            return event.replyReserve("你也好")
                    // 使用此转化器需要确保运行时环境中存在 
                    // [[[kotlinx-coroutines-reactor|https://github.com/Kotlin/kotlinx.coroutines/tree/master/reactive]]] 的相关依赖。
                    .transform(SuspendReserves.mono());
                    // 当使用异步API (比如此处的 Mono) 时，
                    // 你需要格外注意异常处理问题。
                    // 如果你不 return，
                    // 那么你就要处理异常，否则你无法感知到异步任务中出现的错误。
                    // 比如：
                    // .doOnError(err -> {
                    //     // 比如输出错误日志
                    // });
        }
    }
}
```
{switcher-key="%jr%"}

</tab>
</tabs>
</tab>
<tab title="消息回复(KOOK专属)">

此处以 "KOOK的子频道消息事件" 为例，
此事件类型为 `KookChannelMessageEvent`。

这个事件是KOOK组件里实现的专有事件类型，它继承
`ChatChannelMessageEvent`。

事件逻辑：当收到文本内 **包含"你好"** 的消息，回复"你也好"。

<tabs group="Code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
@EnableSimbot // 启用 simbot
@SpringBootApplication
class MyApplication

fun main(args: Array<String>) {
    runApplicarion<MyApplication>(*args)
}

@Component // 需要交给Spring管理
class MyHandlers {
    @Listener // 标记事件处理函数
    @Filter(  // 简单的事件过滤注解
        value = "你好", // 需要包含的文字
        matchType = MatchType.TEXT_CONENT // 调整匹配模式为 "文本包含"
    )
    // 如果需要，添加此注解 @ContentTrim 来去除匹配用的文本前后的空白字符。
    // 当 Bot 被 at 时，被解析出的文本内容很有可能存在一些前后空格，
    // 所以此时使用 @ContentTrim 会有所帮助
    @ContentTrim 
    suspend fun handle(event: KookChannelMessageEvent) {
        event.reply("你也好")
    }
}
```

</tab>
<tab title="Java" group-key="Java">

```Java
@EnableSimbot // 启用 simbot
@SpringBootApplication
public class MyApplication {
  public static void main(String[] args) {
    SpringApplication.run(MyApplication.class, args);
  }

  @Component // 需要交给Spring管理
  public static class MyHandlers {
    @Listener // 标记事件处理函数
    @Filter(  // 简单的事件过滤注解
            value = "你好", // 需要包含的文字
            matchType = MatchType.TEXT_CONENT // 调整匹配模式为 "文本包含"
    )
    // 如果需要，添加此注解 @ContentTrim 来去除匹配用的文本前后的空白字符。
    // 当 Bot 被 at 时，被解析出的文本内容很有可能存在一些前后空格，
    // 所以此时使用 @ContentTrim 会有所帮助
    @ContentTrim
    public CompletableFuture<?> handle(QGAtMessageCreateEvent event) {
      return event.replyAsync("你也好");
              // 当使用异步API (CompletableFuture) 时，
              // 你需要格外注意异常处理问题。
              // 如果你不 return，
              // 那么你就要处理异常，否则你无法感知到异步任务中出现的错误。
              // 比如：
              // .whenComplete((value, e) -> {
              //   if (e != null) {
              //     // 比如输出错误日志
              //   }
              // })
    }
  }
}
```
{switcher-key="%ja%"}

```Java
@EnableSimbot // 启用 simbot
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

    @Component // 需要交给Spring管理
    public static class MyHandlers {
        @Listener // 标记事件处理函数
        @Filter(  // 简单的事件过滤注解
                value = "你好", // 需要包含的文字
                matchType = MatchType.TEXT_CONENT // 调整匹配模式为 "文本包含"
        )
        // 如果需要，添加此注解 @ContentTrim 来去除匹配用的文本前后的空白字符。
        // 当 Bot 被 at 时，被解析出的文本内容很有可能存在一些前后空格，
        // 所以此时使用 @ContentTrim 会有所帮助
        @ContentTrim
        public void handle(QGAtMessageCreateEvent event) {
            event.replyBlocking("你也好");
        }
    }
}
```
{switcher-key="%jb%"}

```Java
@EnableSimbot // 启用 simbot
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

    @Component // 需要交给Spring管理
    public static class MyHandlers {
        @Listener // 标记事件处理函数
        @Filter(  // 简单的事件过滤注解
                value = "你好", // 需要包含的文字
                matchType = MatchType.TEXT_CONENT // 调整匹配模式为 "文本包含"
        )
        // 如果需要，添加此注解 @ContentTrim 来去除匹配用的文本前后的空白字符。
        // 当 Bot 被 at 时，被解析出的文本内容很有可能存在一些前后空格，
        // 所以此时使用 @ContentTrim 会有所帮助
        @ContentTrim
        public Mono<?> handle(QGAtMessageCreateEvent event) {
            return event.replyReserve("你也好")
                    // 使用此转化器需要确保运行时环境中存在 
                    // [[[kotlinx-coroutines-reactor|https://github.com/Kotlin/kotlinx.coroutines/tree/master/reactive]]] 的相关依赖。
                    .transform(SuspendReserves.mono());
                    // 当使用响应式API (比如此处的 Mono) 时，
                    // 你需要格外注意异常处理问题。
                    // 如果你不 return，
                    // 那么你就要处理异常，否则你无法感知到异步任务中出现的错误。
                    // 甚至你如果不 return，可能都无法真正的执行逻辑，因为这个 Mono 没有被消费。
                    // 比如：
                    // .doOnError(err -> {
                    //     // 比如输出错误日志
                    // });
        }
    }
}
```
{switcher-key="%jr%"}

</tab>
</tabs>
</tab>
</tabs>


[spring.start]: https://start.spring.io
