# 使用 Spring Boot

## 前言

本编文档介绍使用:

- `simboot-core-spring-boot-starter`: `simbot3` 的 Spring Boot Starter
- `simbot-component-kook-core`: `simbot3` 的 KOOK 组件

来编写搭建一个基于simbot和Spring的KOOK机器人项目。

## 前提准备

首先你应当准备至少一个可用的 [KOOK机器人](https://developer.kookapp.cn/app/index) 。

## 项目构建

**1. 准备 Spring Boot 项目**

首先准备一个SpringBoot项目。可以考虑前往 [start.spring.io](https://start.spring.io) 或借助IDE等工具。

**2. 添加 simbot 依赖**

然后**额外添加**两个我们需要的依赖：
- `love.forte.simbot.boot:simboot-core-spring-boot-starter` 
  ([**版本参考**](https://github.com/simple-robot/simpler-robot/releases))
- `love.forte.simbot.component:simbot-component-kook-core`
  ([**版本参考**](https://github.com/simple-robot/simbot-component-kook/releases))

:::info 保持住

注意，在使用 Spring Boot 的时候你需要一些能够使程序保持运行的组件，例如通过 `spring-web` 启用一个服务器，否则程序可能会自动终止。
因为simbot的 starter 并不提供维持程序运行的能力。

当然，你也可以选择使用一个线程来自行实现程序保活。

:::

<tabs group="use-dependency">
<tab title="Gradle Kotlin DSL" group-key="Kts">

```Kotlin
// simbot core starter  
implementation("love.forte.simbot.boot:simboot-core-spring-boot-starter:3.3.0") // 版本请参考前文的参考链接
// KOOK组件  
implementation("love.forte.simbot.component:simbot-component-kook-core:3.3.0.0-beta1") // 版本可参考前文的参考链接
```

</tab>
<tab title="Gradle Groovy" group-key="Gradle">

```Gradle
// simbot core starter
implementation 'love.forte.simbot.boot:simboot-core-spring-boot-starter:3.3.0'
// KOOK组件
implementation 'love.forte.simbot.component:simbot-component-kook-core:3.3.0.0-beta1'
```

</tab>
<tab title="Maven" group-key="Maven">

```xml
<!-- simbot core starter -->
<dependency>
    <groupId>love.forte.simbot.boot</groupId>
    <artifactId>simboot-core-spring-boot-starter</artifactId>
    <version>3.3.0</version>
</dependency>
<!-- KOOK组件 -->
<dependency>
    <groupId>love.forte.simbot.component</groupId>
    <artifactId>simbot-component-kook-core</artifactId>
    <version>3.3.0.0-beta1</version>
</dependency>
```

</tab>
</tabs>

**3. 选择并安装合适的 Ktor Client 依赖**

前往 [Ktor: HTTP client Engines](https://ktor.io/docs/http-client-engines.html) 选择并使用一个合适的、支持 websocket 连接 的 HTTP Client 引擎。

:::caution 限制条件

**注意:** 你需要选择一个支持**HTTP 1.1**和**WS Client**的引擎。部分引擎可能不支持**WS Client**，请注意区分。

各引擎实现的限制可参考 [Ktor文档](https://ktor.io/docs/http-client-engines.html#limitations)。

:::

例如：

<tabs group="use-dependency">
<tab title="Gradle Kotlin DSL" group-key="Kts">

以 [`CIO`](https://ktor.io/docs/http-client-engines.html#cio) 引擎为例：

```kotlin
// 或使用 runtimeOnly
implementation("io.ktor:ktor-client-cio:<合适且较新的Ktor版本>")
```

如果没有使用 `Gradle` 的 [`Kotlin` 插件](https://kotlinlang.org/docs/gradle-configure-project.html#apply-the-plugin)，则主动分配平台后缀：

```kotlin
// 或使用 runtimeOnly
implementation("io.ktor:ktor-client-cio-jvm:<合适且较新的Ktor版本>")
```

或者如果 Java 版本 `>= Java11`, 可以使用 [`Java`](https://ktor.io/docs/http-client-engines.html#java) 引擎：

```kotlin
// 或使用 runtimeOnly
implementation("io.ktor:ktor-client-java:<合适且较新的Ktor版本>")
```

</tab>
<tab title="Gradle Groovy" group-key="Gradle">

以 [`CIO`](https://ktor.io/docs/http-client-engines.html#cio) 引擎为例：

```groovy
// 或使用 runtimeOnly
implementation 'io.ktor:ktor-client-cio:<合适且较新的Ktor版本>'
```

如果没有使用 `Gradle` 的 [`Kotlin` 插件](https://kotlinlang.org/docs/gradle-configure-project.html#apply-the-plugin)，则主动分配平台后缀：

```kotlin
// 或使用 runtimeOnly
implementation 'io.ktor:ktor-client-cio-jvm:<合适且较新的Ktor版本>'
```

或者如果 Java 版本 `>= Java11`, 可以使用 [`Java`](https://ktor.io/docs/http-client-engines.html#java) 引擎：

```groovy
// 或使用 runtimeOnly
implementation 'io.ktor:ktor-client-java:<合适且较新的Ktor版本>'
```

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
</Tabs>

## BOT配置

接下来，在项目**资源文件**目录下的 `simbot-bots` 文件夹中创建一个用于配置bot的配置文件 `xxx.bot.json` ( 文件名随意，扩展名应为 `.bot` 或 `.bot.json` ) 。

配置文件的具体内容则参考章节 [**BOT配置文件**](old-bot-config.md) 。

> _此路径以 IDEA 的项目结构风格为准，如果是其他IDE，使用对应的资源文件目录。_
> 
> ```
> ${PROJECT_SRC}/main/resources/simbot-bots/xxx.bot.json
> ```

:::tip 可配置

如果想要修改此路径，可在 Spring Boot 的配置文件中进行配置：

<tabs group="spring-boot-config">
<tab title="properties">

```
# 自定义配置bot资源文件的扫描路径。
# 默认为 classpath:simbot-bots/*.bot*
# 如果要使用本地文件可以使用 `file:` 开头
simbot.bot-configuration-resources[0]=classpath:simbot-bots/*.bot*
```

</tab>
<tab title="YAML">

```yaml
simbot:
  # 自定义配置bot资源文件的扫描路径。
  # 默认为 classpath:simbot-bots/*.bot*
  # 如果要使用本地文件可以使用 `file:` 开头
  bot-configuration-resources:
    - 'classpath:simbot-bots/*.bot*'
```

</tab>
</Tabs>

:::


## 启动类

像每一个 Spring Boot 应用一样，你需要一个启动类，并通过标注 `@EnableSimbot` 来启用 `simbot` ：

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```kotlin title='com.example.App.kt'
@EnableSimbot
@SpringBootApplication
class App

fun main(vararg args: String) {
    runApplication<App>(args = args)
}
```

</tab>
<tab title="Java" group-key="Java">

:::tip 早有预防

如果你在Java中遇到了无法引用 `@EnableSimbot` 等情况，或许可以参考 [**这篇FAQ**](https://simple-robot-library.github.io/simbot3-website/faq/包引用异常/)。

:::

```java title='com.example.App.java'
@EnableSimbot
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

</tab>

</Tabs>


## 监听事件

接下来就是逻辑代码所在的地方了，编写一个监听函数并监听一个事件。

此处我们监听 `ChannelMessageEvent`，也就是 **_子频道的消息事件_**。

假设：要求bot必须**被AT**，并且说一句 `你好`，此时bot会**引用**用户发送的消息并回复 `你也好!` ，类似于：

```
用户: 
@BOT 你好

BOT:
> 用户: @BOT 你好
你也好! 
```

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```kotlin title='com.example.listener.ExampleListener.kt'
import love.forte.simboot.annotation.ContentTrim
import love.forte.simboot.annotation.Filter
import love.forte.simboot.annotation.Listener
import love.forte.simbot.event.ChannelMessageEvent

@Component
class ExampleListener {
    
    @Listener
    @Filter(value = "你好", targets = Filter.Targets(atBot = true))
    @ContentTrim // 当匹配被at时，将'at'这个特殊消息移除后，剩余的文本消息大概率存在前后空格，通过此注解在匹配的时候忽略前后空格
    suspend fun onChannelMessage(event: ChannelMessageEvent) { // 将要监听的事件类型放在参数里，即代表监听此类型的消息
        event.reply("你也好!")
    }
}


```

</tab>
<tab title="Java" label="Java Blocking" group-key="Java">


```java title='com.example.listener.ExampleListener.java'
import love.forte.simboot.annotation.ContentTrim
import love.forte.simboot.annotation.Filter
import love.forte.simboot.annotation.Listener
import love.forte.simbot.event.ChannelMessageEvent
        
@Component
public class ExampleListener {
    
    @Listener
    @Filter(value = "你好", targets = @Filter.Targets(atBot = true))
    @ContentTrim // 当匹配被at时，将'at'这个特殊消息移除后，剩余的文本消息大概率存在前后空格，通过此注解在匹配的时候忽略前后空格
    public void onChannelMessage(ChannelMessageEvent event) { // 将要监听的事件类型放在参数里，即代表监听此类型的消息
        // Java中的阻塞式API
        event.replyBlocking("你也好!");
    }
    
}
```

</tab>
<tab title="Java Async" group-key="Java-A">

```java title='com.example.listener.ExampleListener.java'
import love.forte.simboot.annotation.ContentTrim
import love.forte.simboot.annotation.Filter
import love.forte.simboot.annotation.Listener
import love.forte.simbot.event.ChannelMessageEvent
        
@Component
public class ExampleListener {
    
    @Listener
    @Filter(value = "你好", targets = @Filter.Targets(atBot = true))
    @ContentTrim // 当匹配被at时，将'at'这个特殊消息移除后，剩余的文本消息大概率存在前后空格，通过此注解在匹配的时候忽略前后空格
    public CompletableFuture<?> onChannelMessage(ChannelMessageEvent event) { // 将要监听的事件类型放在参数里，即代表监听此类型的消息
        // 将 CompletableFuture 作为返回值，simbot会以非阻塞的形式处理它
        return event.replyAsync("你也好!");
    }
    
}
```

</tab>
<tab title="Java Reactive" group-key="Java-R">

:::tip 有要求

如果返回值是需要第三方库的响应式类型，那么你的项目环境依赖中必须存在 `Kotlin courotines` 对其的支持库才可使用。
你可以参考simbot文档: [响应式的处理结果](https://simple-robot-library.github.io/simbot3-website/docs/basic/event-listener#可响应式的处理结果) 。

:::

```java title='com.example.listener.ExampleListener.java'
import love.forte.simboot.annotation.ContentTrim
import love.forte.simboot.annotation.Filter
import love.forte.simboot.annotation.Listener
import love.forte.simbot.event.ChannelMessageEvent
        
@Component
public class ExampleListener {
    
    @Listener
    @Filter(value = "你好", targets = @Filter.Targets(atBot = true))
    @ContentTrim // 当匹配被at时，将'at'这个特殊消息移除后，剩余的文本消息大概率存在前后空格，通过此注解在匹配的时候忽略前后空格
    public Mono<?> onChannelMessage(ChannelMessageEvent event) { // 将要监听的事件类型放在参数里，即代表监听此类型的消息
        // 将 Mono 等响应式类型作为返回值，simbot会以非阻塞的形式处理它
        return Mono.fromCompletionStage(event.replyAsync("你也好!"));
    }
    
}
```

</tab>
</Tabs>

## 启动

接下来，启动程序并在你的沙箱频道中@它试试看吧。

当然，如果遇到了预期外的问题也不要慌，积极反馈问题才能使我们变得更好，可以前往 [Issues](https://github.com/simple-robot/simpler-robot/issues) 反馈问题、[社区](https://github.com/orgs/simple-robot/discussions) 提出疑问。

## 更多

前往 [simbot3官网](https://simple-robot-library.github.io/simbot3-website/) 来了解simbot3的各**通用能力**。

前往 [API文档](https://docs.simbot.forte.love) 或通过源码翻阅、搜索并了解具体的功能。

前往 [社区](https://github.com/orgs/simple-robot/discussions) 提出疑惑。

最终，本手册网站可能会时不时的更新一些实用性的应用文档。

:::info 精力有限 

编写使用手册是极其消耗精力的事情。

我们会首先保证**源码的文档注释**的全面性，同时由此保证**API文档**的说明始终是紧随源码生成而完整的。（API文档会随着版本的发布自动部署）

其次，我们会尽可能的及时并全面的回复[**社区**](https://github.com/orgs/simple-robot/discussions)中的相关疑惑。

最终，我们才会考虑完善本手册网站。

:::
