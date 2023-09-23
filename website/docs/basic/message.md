---
title: 消息
sidebar_position: 10
toc_max_heading_level: 4
---


import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import CodeBlock from '@theme/CodeBlock';
import {version} from '@site/static/version.json'


作为一个聊天软件的bot，消息的接收、处理与发送是必不可少的。在 KOOK 组件中，我们理所应当的提供了这方面的能力。


## API、标准库

在 API 模块和标准库中，我们提供了针对官方API的对应实现，因此官方的API中对消息的处理是如何的，在这两个模块中就是如何。

### 发送消息

:::note 消息API

与消息相关的API都在包路径 `love.forte.simbot.kook.api.message` 中。

:::

你可以通过下面这几个 API 来发送消息：

- `SendChannelMessageApi`: 发送频道聊天消息
- `SendDirectMessageApi`: 发送私信聊天消息

它们的各个参数等更详细的信息你可以在 [API文档](https://docs.simbot.forte.love/components/kook/) 中找到，这里就不再赘述了。
以 `SendChannelMessageApi` 为例，提供简单的示例：

<Tabs groupId="code">
<TabItem value="Kotlin" attributes={{'data-value': `Kotlin`}}>

```kotlin
// 用于请求的 Ktor HttpClient，如有必要则需要自行引入并选择需要使用的引擎。
// 参考：https://ktor.io/docs/http-client-engines.html
val client = HttpClient(CIO) {
    // config...
}

// 鉴权信息
// 'Bot' 后面跟的是bot的token，参考 https://developer.kookapp.cn/doc/reference
val authorization = "Bot xxxxxxxxxx"

// 构建要请求的API，大部分API都有一些可选或必须的参数。
val api = SendChannelMessageApi.create(targetId = "目标ID", content = "消息内容")

// 或其他构建方式
SendChannelMessageApi.create {
    content = ""
    type = 9
    type(SendMessageType.KMARKDOWN)
    nonce = "nonce"
    quote = "quote"
    tempTargetId = "tempTargetId"
}

// 得到结果
val result = api.requestData(client, authorization)
println("result = $result")
println("result.nonce = ${result.nonce}")
println("result.msgId = ${result.msgId}")
println("result.msgTimestamp = ${result.msgTimestamp}")
```

</TabItem>
<TabItem value="Java" attributes={{'data-value': `Java`}}>

```java
// 在Java中构建或获取一个 Ktor 的 HttpClient。
// 用于请求的 Ktor HttpClient，如有必要则需要自行引入并选择需要使用的引擎。
// 参考：https://ktor.io/docs/http-client-engines.html
// 此处以 ktor-cio 引擎为例。
var client = HttpClientKt.HttpClient(CIO.INSTANCE, config -> {
            // config...
            return Unit.INSTANCE;
        });

// 鉴权信息
// 'Bot' 后面跟的是bot的token，参考 https://developer.kookapp.cn/doc/reference
var authorization = "Bot xxxxxxxxxx";

// 构建api
var api = SendChannelMessageApi.create("目标ID", "消息内容");
// 或其他重载：
SendChannelMessageApi.create(SendMessageType.KMARKDOWN.getValue(), "目标ID", "消息内容");
SendChannelMessageApi.create(SendMessageType.KMARKDOWN.getValue(), "目标ID", "消息内容", "Quote引用ID", "nonce", "tempTargetId");
// 或使用builder：
SendChannelMessageApi.builder("目标ID", "消息内容")
        .type(SendMessageType.KMARKDOWN)
        .content("content")
        .quote("quote")
        .nonce("nonce")
        .tempTargetId("tempTargetId").build();

// 请求并得到结果
var result =  api.requestDataBlocking(client, authorization);
System.out.println("result.getMsgId() = " + result.getMsgId());
System.out.println("result.getNonce() = " + result.getNonce());
System.out.println("result.getMsgTimestamp() = " + result.getMsgTimestamp());
```

</TabItem>
<TabItem value="Java Async" attributes={{'data-value': `Java`}}>

```java
// 在Java中构建或获取一个 Ktor 的 HttpClient。
// 用于请求的 Ktor HttpClient，如有必要则需要自行引入并选择需要使用的引擎。
// 参考：https://ktor.io/docs/http-client-engines.html
// 此处以 ktor-cio 引擎为例。
var client = HttpClientKt.HttpClient(CIO.INSTANCE, config -> {
            // config...
            return Unit.INSTANCE;
        });

// 鉴权信息
// 'Bot' 后面跟的是bot的token，参考 https://developer.kookapp.cn/doc/reference
var authorization = "Bot xxxxxxxxxx";

// 构建api
var api = SendChannelMessageApi.create("目标ID", "消息内容");
// 或其他重载：
SendChannelMessageApi.create(SendMessageType.KMARKDOWN.getValue(), "目标ID", "消息内容");
SendChannelMessageApi.create(SendMessageType.KMARKDOWN.getValue(), "目标ID", "消息内容", "Quote引用ID", "nonce", "tempTargetId");
// 或使用builder：
SendChannelMessageApi.builder("目标ID", "消息内容")
        .type(SendMessageType.KMARKDOWN)
        .content("content")
        .quote("quote")
        .nonce("nonce")
        .tempTargetId("tempTargetId").build();

// 请求并得到结果
api.requestDataAsync(client, authorization).thenAccept(result -> {
        // 发送后的结果
        System.out.println("result = " + result);
        System.out.println("result.getMsgId() = " + result.getMsgId());
        System.out.println("result.getNonce() = " + result.getNonce());
        System.out.println("result.getMsgTimestamp() = " + result.getMsgTimestamp());
});
```

</TabItem>
</Tabs>

:::note USE BOT

在标准库中，你也可以构建一个 `Bot` 后直接通过 Bot 作为请求的凭证来源，而不再需要手动准备 `HttpClient` 和 `authorization` 了。

<Tabs groupId="code">
<TabItem value="Kotlin" attributes={{'data-value': `Kotlin`}}>

```kotlin
val bot = BotFactory.create(Ticket.botWsTicket("CLIENT_ID", "TOKEN")) {
    // 配置...
}

// 构建api...
val api = ...

// 得到结果
val result = api.requestBy(bot)
```

</TabItem>
<TabItem value="Java" attributes={{'data-value': `Java`}}>

```java
// 准备Ticket
var ticket = Ticket.botWsTicket("CLIENT_ID", "TOKEN");

// 配置过程省略...

// 构建bot
var bot = BotFactory.create(ticket);

// 构建api...
var api = ...
        
// 请求并得到结果
var result = api.requestByBlocking(bot);
```

</TabItem>
<TabItem value="Java Async" attributes={{'data-value': `Java`}}>

```java
// 准备Ticket
var ticket = Ticket.botWsTicket("CLIENT_ID", "TOKEN");

// 配置过程省略...

// 构建bot
var bot = BotFactory.create(ticket);

// 构建api...
var api = ...

// 请求并得到结果
api.requestByAsync(bot).thenAccept(result -> {
    // ...
});
```

</TabItem>
</Tabs>

:::

### 接收消息

想要接收消息，首先需要使用核心库注册并启动一个bot来订阅事件。通过订阅消息事件，我们便可以接收到消息。

<Tabs groupId="code">
<TabItem value="Kotlin" attributes={{'data-value': `Kotlin`}}>

```kotlin
val bot = BotFactory.create(Ticket.botWsTicket("CLIENT_ID", "TOKEN")) {
    // 配置...
}

bot.processor<TextEventExtra> { raw -> // this: Event<TextEventExtra>, raw: String
    println("原始JSON: $raw")
    println("event: $this")
    println("event.extra: ${this.extra}")
    println("content: ${this.content}")
}

bot.startAndJoin()
```

</TabItem>
<TabItem value="Java" attributes={{'data-value': `Java`}}>

```java
// 准备Ticket
var ticket = Ticket.botWsTicket("CLIENT_ID", "TOKEN");

// 配置过程省略...

// 构建bot
var bot = BotFactory.create(ticket);

bot.blockingProcessor(TextEventExtra.class, (event, raw) -> {
        System.out.println("raw = " + raw);
        System.out.println("event = " + event);
        System.out.println("event.extra = " + event.getExtra());
        System.out.println("content = " + event.getContent());
});

bot.startBlocking();
bot.joinBlocking();
```

</TabItem>
<TabItem value="Java Async" attributes={{'data-value': `Java`}}>

```java
// 准备Ticket
var ticket = Ticket.botWsTicket("CLIENT_ID", "TOKEN");

// 配置过程省略...

// 构建bot
var bot = BotFactory.create(ticket);

bot.asyncProcessor(TextEventExtra.class, (event, raw) -> {
        System.out.println("raw = " + raw);
        System.out.println("event = " + event);
        System.out.println("event.extra = " + event.getExtra());
        System.out.println("content = " + event.getContent());
        return CompletableFuture.completedFuture(null);
});

bot.startAsync().join();
```

</TabItem>
</Tabs>

其中的 `content` 便是消息内容了。配合事件中其他属性便可以解析、处理消息内容了。

## 核心库

在核心库中，对消息的发送和接收都有着 simbot API 的风格。


### 消息元素

KOOK组件核心库作为 simbot 的组件库之一，自然会对标准消息元素进行部分支持并提供一些其专属的消息元素类型。首先来看看兼容的标准消息元素：

#### PlainText

文本消息。这无需多言，文本消息自然能够被支持。

<Tabs groupId="code">
<TabItem value="Kotlin" attributes={{'data-value': `Kotlin`}}>

```kotlin
val channel = ...
        
channel.send("文本消息")
channel.send("文本消息".toText())
```

</TabItem>
<TabItem value="Java" attributes={{'data-value': `Java`}}>

```java
var channel = ...
        
channel.sendBlocking("文本消息");
channel.sendBlocking(Text.of("文本消息"));
```

</TabItem>
<TabItem value="Java Async" attributes={{'data-value': `Java`}}>

```java
var channel = ...
        
channel.sendAsync("文本消息");
channel.sendAsync(Text.of("文本消息"));
```

</TabItem>
</Tabs>

#### ResourceImage & Image

图片消息。可以用来发送图片。

<Tabs groupId="code">
<TabItem value="Kotlin" attributes={{'data-value': `Kotlin`}}>

```kotlin
val channel = ...

channel.send(Path("/xx/img.jpg").toResource().toImage())
```

</TabItem>
<TabItem value="Java" attributes={{'data-value': `Java`}}>

```java
var channel = ...

var path = Paths.get("/xx/img.jpg");
var resource = Resource.of(path);
var resourceImage = Image.of(resource);

channel.sendAsync(resourceImage);
```

</TabItem>
<TabItem value="Java Async" attributes={{'data-value': `Java`}}>

```java
var channel = ...

var path = Paths.get("/xx/img.jpg");
var resource = Resource.of(path);
var resourceImage = Image.of(resource);

channel.sendAsync(resourceImage);
```

</TabItem>
</Tabs>

#### At

提及。可以通过 `At` 提及一个**用户**(默认)、一个**频道**或一个**角色**。

<Tabs groupId="code">
<TabItem value="Kotlin" attributes={{'data-value': `Kotlin`}}>

```kotlin
val channel = ...

val atUser = At(123.ID)
val atChannel = At(123.ID, KookMessages.AT_TYPE_CHANNEL)
// 或：KookMessages.atChannel(123.ID)
val atRole = At(123.ID, KookMessages.AT_TYPE_ROLE)
// 或：KookMessages.atRole(123.ID)

channel.send(atUser)
channel.send(atUser + atChannel + atRole)
```

</TabItem>
<TabItem value="Java" attributes={{'data-value': `Java`}}>

```java
var channel = ...

var atUser = new At(Identifies.ID(123));
var atChannel = new At(Identifies.ID(123), KookMessages.AT_TYPE_CHANNEL);
// 或：KookMessages.atChannel(Identifies.ID(123));
var atRole = new At(Identifies.ID(123), KookMessages.AT_TYPE_ROLE);
// 或：KookMessages.atRole(Identifies.ID(123));

channel.sendBlocking(atUser);
channel.sendBlocking(Messages.toMessages(atUser, atChannel, atRole));
```

</TabItem>
<TabItem value="Java Async" attributes={{'data-value': `Java`}}>

```java
var channel = ...

var atUser = new At(Identifies.ID(123));
var atChannel = new At(Identifies.ID(123), KookMessages.AT_TYPE_CHANNEL);
// 或：KookMessages.atChannel(Identifies.ID(123));
var atRole = new At(Identifies.ID(123), KookMessages.AT_TYPE_ROLE);
// 或：KookMessages.atRole(Identifies.ID(123));

channel.sendAsync(atUser);
channel.sendAsync(Messages.toMessages(atUser, atChannel, atRole));
```

</TabItem>
</Tabs>

#### AtAll

提及。可以通过 `AtAll` 提及所有。

<Tabs groupId="code">
<TabItem value="Kotlin" attributes={{'data-value': `Kotlin`}}>

```kotlin
val channel = ...

channel.send(AtAll)
```

</TabItem>
<TabItem value="Java" attributes={{'data-value': `Java`}}>

```java
var channel = ...

channel.sendBlocking(AtAll.INSTANCE);
```

</TabItem>
<TabItem value="Java Async" attributes={{'data-value': `Java`}}>

```java
var channel = ...

channel.sendAsync(AtAll.INSTANCE);
```

</TabItem>
</Tabs>

#### Emoji

emoji。是指 [KMarkdown](https://developer.kookapp.cn/doc/kmarkdown) 中所述的 **"emoji"** （`:emoji:`）。

<Tabs groupId="code">
<TabItem value="Kotlin" attributes={{'data-value': `Kotlin`}}>

```kotlin
val channel = ...

channel.send(Emoji(123.ID))
```

</TabItem>
<TabItem value="Java" attributes={{'data-value': `Java`}}>

```java
var channel = ...

channel.sendBlocking(new Emoji(Identifies.ID(123)));
```

</TabItem>
<TabItem value="Java Async" attributes={{'data-value': `Java`}}>

```java
var channel = ...

channel.sendAsync(new Emoji(Identifies.ID(123)));
```

</TabItem>
</Tabs>

除了上述一些 simbot 标准消息类型以外，KOOK组件也提供了一些专属的消息类型来满足更全面的消息发送需求。

:::note 类型

这些消息类型通常实现了 `KookMessageElement` 接口，并且以 `Kook` 作为类名前缀。

:::

#### KookAssetMessage

提供一个 API 中上传得到的 `Asset` （附件）和它的类型并作为消息发送。

<Tabs groupId="code">
<TabItem value="Kotlin" attributes={{'data-value': `Kotlin`}}>

```kotlin
val channel = ...

// 通过API自行上传一个资源（可以用于避免不必要的多次上传）
val asset = CreateAssetApi.create(...).requestBy(...)

// 可以选择它是文件，还是图片
val imgMsg = KookAssetImage(asset)
val assetMsg = KookAsset(asset, MessageType.FILE) // 如果不是文件，你需要指定消息类型

channel.send(imgMsg)
channel.send(assetMsg)
```

</TabItem>
<TabItem value="Java" attributes={{'data-value': `Java`}}>

```java
var channel = ...

// 通过API自行上传一个资源（可以用于避免不必要的多次上传）
var asset = CreateAssetApi.create(...).requestByBlocking(...);

// 可以选择它是文件，还是图片
var imgMsg = new KookAssetImage(asset);
var assetMsg = new KookAsset(asset, MessageType.FILE); // 如果不是文件，你需要指定消息类型

channel.sendBlocking(imgMsg);
channel.sendBlocking(assetMsg);
```

</TabItem>
<TabItem value="Java Async" attributes={{'data-value': `Java`}}>

```java
var channel = ...

// 通过API自行上传一个资源（可以用于避免不必要的多次上传）
CreateAssetApi.create(...).requestByAsync(...).thenAccept(asset -> {
        // 可以选择它是文件，还是图片
        var imgMsg = new KookAssetImage(asset);
        var assetMsg = new KookAsset(asset, MessageType.FILE); // 如果不是文件，你需要指定消息类型

        channel.sendAsync(imgMsg);
        channel.sendAsync(assetMsg);
});
```

</TabItem>
</Tabs>


#### KookKMarkdownMessage

通过 `KookKMarkdownMessage` 可以做到直接发送一个完全自定义的 `KMarkdown` 消息。

<Tabs groupId="code">
<TabItem value="Kotlin" attributes={{'data-value': `Kotlin`}}>

```kotlin
val channel = ...

// 自行构建一个 KMarkdown，并直接作为消息发送。
val kmd = KookKMarkdownMessage(buildKMarkdown {
    bold("这是粗体")
    newLine()
    hide("隐藏")
    newLine()
    appendRawMd("直接拼接原始字符串，**不做解析**，_不做处理_")
    // 其他...
})

channel.send(kmd)
```

</TabItem>
<TabItem value="Java" attributes={{'data-value': `Java`}}>

```java
var channel = ...

// 构建KMarkdown
var builder = new KMarkdownBuilder();
builder.bold("加粗").newLine();
builder.hide("隐藏").newLine();
builder.appendRawMd("原始的文本，**不做处理**，_直接拼接_");
var kMarkdown = builder.build();

channel.sendBlocking(new KookKMarkdownMessage(kMarkdown));
```

</TabItem>
<TabItem value="Java Async" attributes={{'data-value': `Java`}}>

```java
var channel = ...

// 构建KMarkdown
var builder = new KMarkdownBuilder();
builder.bold("加粗").newLine();
builder.hide("隐藏").newLine();
builder.appendRawMd("原始的文本，**不做处理**，_直接拼接_");
var kMarkdown = builder.build();

channel.sendAsync(new KookKMarkdownMessage(kMarkdown));
```

</TabItem>
</Tabs>

#### KookCardMessage

通过 `KookKMarkdownMessage` 来发送 KOOK 中的 [卡片消息](https://developer.kookapp.cn/doc/cardmessage)。

<Tabs groupId="code">
<TabItem value="Kotlin" attributes={{'data-value': `Kotlin`}}>

```kotlin
val channel = ...

// 构建card消息
val cardMessage = KookCardMessage(buildCardMessage {
    card {
        color = "..."
        theme = Theme.PRIMARY
        modules {
            header("xxx")
            // ...
        }
        // ...
    }
})

// 也可以直接通过JSON解析
KookCardMessage(CardMessage.decode("{...}")) // card json value

channel.send(cardMessage)
```

</TabItem>
<TabItem value="Java" attributes={{'data-value': `Java`}}>

```java
var channel = ...

// 通过解析卡片消息JSON得到消息对象
var cardMsg = new KookCardMessage(CardMessage.decode("{...}"));

channel.sendBlocking(cardMsg);
```

</TabItem>
<TabItem value="Java Async" attributes={{'data-value': `Java`}}>

```java
var channel = ...

// 通过解析卡片消息JSON得到消息对象
var cardMsg = new KookCardMessage(CardMessage.decode("{...}"));

channel.sendAsync(cardMsg);
```

</TabItem>
</Tabs>

#### KookAtAllHere

KOOK 中有一个提及所有在线用户的功能，在KMarkdown中表现为 `(met)here(met)`。

<Tabs groupId="code">
<TabItem value="Kotlin" attributes={{'data-value': `Kotlin`}}>

```kotlin
val channel = ...

channel.send(KookAtAllHere)
```

</TabItem>
<TabItem value="Java" attributes={{'data-value': `Java`}}>

```java
var channel = ...

channel.sendBlocking(KookAtAllHere.INSTANCE);
```

</TabItem>
<TabItem value="Java Async" attributes={{'data-value': `Java`}}>

```java
var channel = ...

channel.sendAsync(KookAtAllHere.INSTANCE);
```

</TabItem>
</Tabs>


#### KookAttachmentMessage

将消息中的多媒体消息 `Attachments` 作为消息元素发送的消息类型。

:::note 差异

与 `Asset` 不同，`Attachments` 是出现在消息中的，包括发送后或接收的消息，而 `Asset` 是你主动上传的资源。

:::

<Tabs groupId="code">
<TabItem value="Kotlin" attributes={{'data-value': `Kotlin`}}>

```kotlin
val channel = ...

val attachments = ...
// 有4种类型可以选择使用，需要选择一个与 attachements 实际情况匹配的
val attachmentFile = KookAttachmentFile(attachments)
val attachmentImage = KookAttachmentImage(attachments)
val attachmentVideo = KookAttachmentVideo(attachments)
val attachment = KookAttachment(attachments)

channel.send(attachmentFile)
channel.send(attachmentImage)
channel.send(attachmentVideo)
channel.send(attachment)
```

</TabItem>
<TabItem value="Java" attributes={{'data-value': `Java`}}>

```java
var channel = ...

var attachments = ...;
// 有4种类型可以选择使用，需要选择一个与 attachements 实际情况匹配的
var attachmentFile = new KookAttachmentFile(attachments);
var attachmentImage = new KookAttachmentImage(attachments);
var attachmentVideo = new KookAttachmentVideo(attachments);
var attachment = new KookAttachment(attachments);

channel.sendBlocking(attachmentFile);
channel.sendBlocking(attachmentImage);
channel.sendBlocking(attachmentVideo);
channel.sendBlocking(attachment);
```

</TabItem>
<TabItem value="Java Async" attributes={{'data-value': `Java`}}>

```java
var channel = ...

var attachments = ...;
// 有4种类型可以选择使用，需要选择一个与 attachements 实际情况匹配的
var attachmentFile = new KookAttachmentFile(attachments);
var attachmentImage = new KookAttachmentImage(attachments);
var attachmentVideo = new KookAttachmentVideo(attachments);
var attachment = new KookAttachment(attachments);

channel.sendAsync(attachmentFile);
channel.sendAsync(attachmentImage);
channel.sendAsync(attachmentVideo);
channel.sendAsync(attachment);
```

</TabItem>
</Tabs>


### 发送消息

TODO

### 接收消息

TODO
